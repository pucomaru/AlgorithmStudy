package jan.week3.boj;

import java.util.*;
import java.io.*;

public class 피리부는사나이_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * 방향 [U, D, L, R]
     *
     * SAFE ZONE 에 들어가면 피리 소리를 듣지 못한다.
     * => 성우가 설정해 놓은 방향을 분석해서 최소 개수의 SAFE ZONE 을 만든다.
     */
    static StringTokenizer st;
    // [U, D, L ,R]
    static int[] dx = {-1, 1, 0, 0};
    static int[] dy = {0, 0, -1, 1};
    static int n, m;
    static char[][] map;
    static int[][] state;
    static int safeZoneCount = 0;
    static void init() throws IOException {
        safeZoneCount = 0;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        map = new char[n][m];
        /**
         * 싱태를 저장할 베열
         * 0 : 아직 탐색하지 않음
         * 1 : 탐색 중
         * 2 : 탐색 완료
         */
        state = new int[n][m];
        for(int x=0; x<n; x++) {
            String input = br.readLine().trim();
            for(int y=0; y<m; y++) {
                map[x][y] = input.charAt(y);
            }
        }
        /**
         * 그래프의 사이클을 찾는다?
         * => 움직이는 방향이 이미 정해져 있으므로
         * => 사이클의 개수 => 최소 safeZone 의 개수
         */
        for(int x=0; x<n; x++) {
            for(int y=0; y<m; y++) {
                if(state[x][y] != 0) continue;

                findCycle(x, y);
            }
        }

        System.out.println(safeZoneCount);
    }

    static void findCycle(int x, int y) {
        int curX = x, curY = y;

        while(true) {
            state[curX][curY] = 1;

            int dir = getDir(map[curX][curY]);

            int nx = curX + dx[dir];
            int ny = curY + dy[dir];

            if(state[nx][ny] == 0) {    // 아직 탐색하지 않은 곳이라면, 계속해서 탐색
                curX = nx;
                curY = ny;
                continue;
            }

            if(state[nx][ny] == 1) safeZoneCount++; // 현재 탐색중인 경로에서 사이클을 찾은 경우

            // state == 1 또는 state == 2 를 만난 경우
            // => 2 를 만났을 때는, 다른 사이클로 귀속 됨 -> 따라서 safeZoneCount 를 세어주지 않음
            // => 1 을 만났을 때는, 세로운 사이클을 찾은 것으로 -> safeZoneCount 를 세어주고, 경로를 탐색 완료 처리함
            curX = x; curY = y;
            while(state[curX][curY] == 1) {
                state[curX][curY] = 2;

                dir = getDir(map[curX][curY]);

                curX += dx[dir];
                curY += dy[dir];
            }
            break;
        }
    }

    static int getDir(char c) {
        if (c == 'U') return 0;
        if (c == 'D') return 1;
        if (c == 'L') return 2;
        return 3;
    }
}
