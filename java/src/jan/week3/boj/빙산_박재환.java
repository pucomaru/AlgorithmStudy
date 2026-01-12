package jan.week3.boj;

import java.util.*;
import java.io.*;

public class 빙산_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static int[][] map;
    static Queue<int[]> glaciers;
    static void init() throws IOException {
        glaciers = new ArrayDeque<>();

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        map = new int[n][m];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<m; y++) {
                map[x][y] = Integer.parseInt(st.nextToken());
                if(map[x][y] != 0) glaciers.offer(new int[] {x, y});
            }
        }

        System.out.println(solution());
    }

    static int solution() {
        int years = 0;
        while(!glaciers.isEmpty()) {
            afterOneYear();
            years++;
            if(findGlacierGroups()) return years;
        }
        return 0;
    }

    static boolean findGlacierGroups() {
        if(glaciers.isEmpty()) return false;

        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][m];

        int[] firstGlacier = glaciers.peek();
        q.offer(new int[] {firstGlacier[0], firstGlacier[1]});
        visited[firstGlacier[0]][firstGlacier[1]] = true;
        int glacierSize = 1;

        while(!q.isEmpty()) {
            int[] glacier = q.poll();
            int x = glacier[0];
            int y = glacier[1];

            for(int dir=0; dir<4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(isNotBoard(nx, ny) || visited[nx][ny]) continue;
                if(map[nx][ny] == 0) continue;

                q.offer(new int[] {nx, ny});
                visited[nx][ny] = true;
                glacierSize++;
            }
        }

        return glacierSize < glaciers.size();
    }



    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static void afterOneYear() {
        Queue<int[]> tempGlaciers = new ArrayDeque<>();

        /**
         * 지연 갱신 필요
         * -> 바로바로 갱신하는 경우, 부정확할 수 있음
         */
        while(!glaciers.isEmpty()) {
            int[] glacier = glaciers.poll();
            int x = glacier[0];
            int y = glacier[1];

            int nearSea = 0;
            for(int dir=0; dir<4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(isNotBoard(nx, ny)) continue;
                if(map[nx][ny] == 0) nearSea++;
            }

            tempGlaciers.offer(new int[] {x, y, nearSea});
        }

        // 끝나고 갱신
        while(!tempGlaciers.isEmpty()) {
            int[] glacier = tempGlaciers.poll();
            int x = glacier[0];
            int y = glacier[1];
            int nearSea = glacier[2];

            map[x][y] = Math.max(map[x][y] - nearSea, 0);

            if(map[x][y] > 0) glaciers.offer(new int[] {x, y});
        }
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= m;
    }
}

/*
5 7
0 0 0 0 0 0 0
0 2 4 5 3 0 0
0 3 0 2 5 2 0
0 7 6 2 4 0 0
0 0 0 0 0 0 0
 */
