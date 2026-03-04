package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 로봇청소기_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int w, h;
    static char[][] board;
    static Map<Integer, Integer> dusts;
    static void init() throws IOException {
        while(true) {
            st = new StringTokenizer(br.readLine().trim());
            w = Integer.parseInt(st.nextToken());
            h = Integer.parseInt(st.nextToken());
            if(w == 0 && h == 0) break;

            board = new char[h][w];
            dusts = new HashMap<>();
            int rx = -1, ry = -1, id = 0;
            for (int x = 0; x < h; x++) {
                String line = br.readLine().trim();
                for (int y = 0; y < w; y++) {
                    board[x][y] = line.charAt(y);
                    if(board[x][y] == 'o') {
                        rx = x;
                        ry = y;
                    } else if(board[x][y] == '*') {
                        int key = x * 27 + y;
                        dusts.put(key, id++);
                    }
                }
            }
            int result = efficientRoute(rx, ry);
            sb.append(result).append('\n');
        }
    }
    static class Robot {
        int x, y;
        int cleanDust;
        int move;

        Robot(int x, int y, int cleanDust, int move) {
            this.x = x;
            this.y = y;
            this.cleanDust = cleanDust;
            this.move = move;
        }
    }
    static final int[] dx = {0,1,0,-1};
    static final int[] dy = {1,0,-1,0};
    static int ALL;
    static int efficientRoute(int rx, int ry) {
        int dustCnt = dusts.size();
        ALL = (1 << dustCnt) - 1;
        Queue<Robot> q = new ArrayDeque<>();
        boolean[][][] visited = new boolean[h][w][ALL+1];

        q.offer(new Robot(rx, ry, 0, 0));
        visited[rx][ry][0] = true;

        while(!q.isEmpty()) {
            Robot cur = q.poll();
            if(cur.cleanDust == ALL) {     // 모든 청소를 마침
                return cur.move;
            }
            for(int i=0; i<4; i++) {
                int nx = cur.x + dx[i];
                int ny = cur.y + dy[i];

                if(nx < 0 || ny < 0 || nx+1 > h || ny+1 > w) continue;
                if(board[nx][ny] == 'x') continue;

                int nextDustState = cur.cleanDust;
                if(board[nx][ny] == '*') {
                    int key = nx * 27 + ny;
                    int nextDust = 1 << dusts.get(key);

                    // 이미 치운 먼지인지 확인
                    if((nextDust & nextDustState) == 0) nextDustState |= nextDust;
                }

                if (visited[nx][ny][nextDustState]) continue;
                visited[nx][ny][nextDustState] = true;
                q.offer(new Robot(nx, ny, nextDustState, cur.move+1));
            }
        }
        return -1;
    }
}
