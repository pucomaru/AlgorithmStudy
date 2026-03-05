package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 불켜기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static final int ENCODE = 107;

    static StringTokenizer st;
    static int n, m;
    static Map<Integer, List<int[]>> switches;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        switches = new HashMap<>();
        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x1 = Integer.parseInt(st.nextToken())-1;
            int y1 = Integer.parseInt(st.nextToken())-1;
            int x2 = Integer.parseInt(st.nextToken())-1;
            int y2 = Integer.parseInt(st.nextToken())-1;

            int key = x1 * ENCODE + y1;
            switches.computeIfAbsent(key, k -> new ArrayList<>()).add(new int[] {x2, y2});
        }

        System.out.println(maxLightRoom());
    }
    static final int[] dx = {0,1,0,-1};
    static final int[] dy = {1,0,-1,0};

    static Queue<int[]> q;
    static boolean[][] lights;
    static boolean[][] visited;
    static int maxLightRoom() {
        q = new ArrayDeque<>();
        lights = new boolean[n][n];
        visited = new boolean[n][n];

        lights[0][0] = true;
        q.offer(new int[] {0,0});
        visited[0][0] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0];
            int curY = cur[1];
            light(curX, curY);

            for(int dir=0; dir<4; dir++) {
                int nx = curX + dx[dir];
                int ny = curY + dy[dir];

                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(visited[nx][ny] || !lights[nx][ny]) continue;

                lights[nx][ny] = true;
                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny});
            }
        }

        return countLightRoom();
    }
    static int countLightRoom() {
        int count = 0;
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(lights[x][y]) count++;
            }
        }
        return count;
    }
    static void light(int x, int y) {
        int key = x * ENCODE + y;
        if(switches.get(key) == null) return;

        for(int[] light : switches.get(key)) {
            int nx = light[0];
            int ny = light[1];
            if(lights[nx][ny]) continue;        // 이전에 불이 켜져있던 경우, 동일한 경로 탐색

            // 새롭게 불을 켠 방
            lights[nx][ny] = true;
            /**
             * 새롭게 불이 켜진 방 근처에, 이전에 방문이력이 있는 방이 있다면
             * 새롭게 불이 켜진 방으로 이동이 가능하다.
             */
            for(int dir=0; dir<4; dir++) {
                int nnx = nx + dx[dir];
                int nny = ny + dy[dir];
                if(nnx < 0 || nny < 0 || nnx >= n || nny >= n) continue;
                if(!visited[nnx][nny]) continue;

                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny});
                break;
            }
        }
    }
}
