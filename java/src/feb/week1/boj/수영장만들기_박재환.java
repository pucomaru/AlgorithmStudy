package feb.week1.boj;

import java.util.*;
import java.io.*;

public class 수영장만들기_박재환 {
    /**
     * 바깥으로 물이 세지 않는 칸들에, 주변 벽보다 낮은 만큼 물이 고인다.
     *
     * 갇혀있는 영역을 찾는다. => BFS / DFS
     */
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static int[][] map;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        map = new int[n][m];
        for(int x=0; x<n; x++) {
            String input = br.readLine().trim();
            for(int y=0; y<m; y++) map[x][y] = input.charAt(y) - '0';
        }

        int result = solution();
        System.out.println(result);
    }
    static int solution() {
        /**
         * 칸의 높이는 1 ~ 9
         * => 물의 높이 1 ~ 8
         */
        int result = 0;
        for(int height=2; height<10; height++) {
            result += checkWaterHeight(height);
        }
        return result;
    }
    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static int checkWaterHeight(int height) {
        /**
         * 물이 채워질 수 없는 칸을 체크한다.
         */
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][m];

        // 시작 후보지점 정하기
        // 경계이거나, 물 높이보다 낮은 칸
        // => 물이 흘러나갈 수 있는 칸의 후보
        for(int x=0; x<n; x++) {
            for(int y=0; y<m; y++) {
                if(x == 0 || y == 0 || x == n-1 || y == m-1) {      // 경계인경우
                    if(!visited[x][y] && map[x][y] < height) {
                        visited[x][y] = true;
                        q.offer(new int[] {x, y});
                    }
                }
            }
        }
        // 확장
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];

            for(int dir=0; dir<4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if(nx < 0 || ny < 0 || nx >= n || ny >= m) continue;
                if(visited[nx][ny] || map[nx][ny] >= height) continue;

                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny});
            }
        }

        int water = 0;
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                if (map[x][y] < height && !visited[x][y]) {
                    water++;
                }
            }
        }
        return water;
    }
}
