package jan.week2.boj;

import java.util.*;
import java.io.*;

public class 벽타기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * 높이가 H, 너비가 W 인 맵의 시작점에서 끝점까지 이동하려한다.
     * 각 칸은 벽 또는 빈칸이다.
     *
     * 상 하 좌 우 이동이 가능하지만, 벽으로는 이동할 수 없다.
     * 한 칸을 이동하는 데에는 1초가 걸린다.
     *
     * 하지만 벽을 타고 이동하면 0초만에, 상 하 좌 우 인접한 칸으로 이동할 수 있다.
     * - 어떤 빈 칸의 상 하 좌 우 중 하나가 벽이면 벽에 인접한 칸이라고 한다.
     * - 벽에 인접한 칸에서 벽에 인접한 칸으로 이동하면 벽을 타고 이동한다.
     *
     * => 가중치가 0 또는 1
     * => 최단 거리 -> BFS
     * => 0-1 BFS
     */
    static int INF = 300000;

    static StringTokenizer st;
    static int H, W;
    static char[][] map;
    static int[] start, end;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        H = Integer.parseInt(st.nextToken());
        W = Integer.parseInt(st.nextToken());
        map = new char[H][W];
        for(int x=0; x<H; x++) {
            String str = br.readLine().trim();
            for(int y=0; y<W; y++) {
                map[x][y] = str.charAt(y);
                if(map[x][y] == 'S') start = new int[] {x, y};
                else if(map[x][y] == 'E') end = new int[] {x, y};
            }
        }

        System.out.println(getMinTime());
    }

    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static int getMinTime() {
        Deque<int[]> q = new ArrayDeque<>();        // [x, y, time]
        int[][] timeTable = new int[H][W];
        for(int i=0; i<H; i++) Arrays.fill(timeTable[i], INF);

        q.offerLast(new int[] {start[0], start[1], 0});
        timeTable[start[0]][start[1]] = 0;

        while(!q.isEmpty()) {
            int[] cur = q.pollFirst();
            int curX = cur[0];
            int curY = cur[1];
            int curTime = cur[2];
            if(curX == end[0] && curY == end[1]) return curTime;

            // 벽에 인접한 칸인지 확인
            boolean isNearWall = isNearWall(curX, curY);
            for(int dir=0; dir<4; dir++) {
                int nx = curX + dx[dir];
                int ny = curY + dy[dir];

                if(isNotMap(nx, ny)) continue;
                if(map[nx][ny] == '#') continue;

                if(isNearWall && isNearWall(nx ,ny)) {
                    if(timeTable[nx][ny] <= curTime) continue;
                    timeTable[nx][ny] = curTime;
                    q.offerFirst(new int[] {nx, ny, curTime});
                }
                else {
                    if(timeTable[nx][ny] <= curTime+1) continue;
                    timeTable[nx][ny] = curTime+1;
                    q.offerLast(new int[] {nx, ny, curTime+1});
                }
            }
        }

        return -1;
    }

    /**
     * 벽에 인접한 칸은, 벽에 인접한 칸으로만 이동 가능하다.
     */
    static boolean isNearWall(int x, int y) {
        for(int dir=0; dir<4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            if(isNotMap(nx, ny)) continue;
            if(map[nx][ny] == '#') return true;
        }
        return false;
    }

    static boolean isNotMap(int x, int y) {
        return x < 0 || y < 0 || x >= H || y >= W;
    }
}
