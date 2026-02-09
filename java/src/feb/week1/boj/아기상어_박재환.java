package feb.week1.boj;

import java.util.*;
import java.io.*;

public class 아기상어_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * N x N
     * 물고기 M 마리와 아기상어 1마리가 있다.
     *
     * 초기 아기상어 크기는 2
     * 1초에 상하좌우 한 칸 씩 이동
     *
     * 자기보다 큰 물고기가 있는 칸으로 지나갈 수 없다
     * 자기보다 작은 물고기만 먹을 수 있다
     *
     * 더 이상 먹을 수 있는 물고기가 공간에 없다면 -> 엄마한테 도움
     * 먹을 수 있는 물고기가 1마리 -> 이동
     * 여러마리 -> 가장 가까운 물고기 먹으러 이동
     * - 지나가는 칸의 개수
     * - 가까운 물고기가 많다면 가장 위, 가장 왼쪽 물고기
     *
     * 물고기를 먹으면 그 칸은 빈칸이 된다.
     * 자신의 크기와 같은 수의 물고기를 먹을 때마다 크기가 1 증가한다.
     */
    static StringTokenizer st;
    static int n;
    static int[][] map;
    static Shark shark;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        map = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) {
                map[x][y] = Integer.parseInt(st.nextToken());
                if(map[x][y] == 9) shark = new Shark(x, y);
            }
        }

        System.out.println(solution());
    }
    static PriorityQueue<Fish> pq;
    static int solution() {
        pq = new PriorityQueue<>((a, b) -> {
            if(a.dist != b.dist) return Integer.compare(a.dist, b.dist);
            if(a.x != b.x) return Integer.compare(a.x, b.x);
            return Integer.compare(a.y, b.y);
        });
        int totalTime = 0;
        while (findFish()) {
            // 먹을 수 있는 물고기가 있는 경우
            Fish target = pq.poll();
            totalTime += target.dist;
            shark.eat();
            shark.x = target.x;
            shark.y = target.y;

        }
        return totalTime;
    }

    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static boolean findFish() {
        pq.clear();     // 우선순위 큐 초기화

        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];

        q.offer(new int[] {shark.x, shark.y, 0});
        visited[shark.x][shark.y] = true;
        map[shark.x][shark.y] = 0;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int dist = cur[2];

            for(int dir=0; dir<4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(visited[nx][ny] || map[nx][ny] > shark.size) continue;
                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny, dist+1});
                if(map[nx][ny] > 0 && map[nx][ny] < shark.size)  pq.offer(new Fish(nx, ny, dist+1));
            }
        }

        return !pq.isEmpty();       // 먹을 수 있는 물고기 후보가 있다면 true, 없다면 false
    }

    static class Fish {
        int x, y;
        int dist;
        Fish(int x, int y, int dist) {
            this.x = x;
            this.y = y;
            this.dist = dist;
        }
    }

    static class Shark {
        int x, y;
        int size;
        int eat;

        Shark(int x, int y) {
            this.x = x;
            this.y = y;
            this.size = 2;
            this.eat = 0;
        }

        void eat() {
            eat = eat + 1;
            if(eat == size) {
                size++;
                eat = 0;
            }
        }
    }
}
