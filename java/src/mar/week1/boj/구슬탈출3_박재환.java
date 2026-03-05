package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 구슬탈출3_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static char[][] board;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        board = new char[n][m];
        for(int x=0; x<n; x++) {
            String input = br.readLine().trim();
            for(int y=0; y<m; y++) {
                board[x][y] = input.charAt(y);
            }
        }

        findMinTilt();
    }
    static class State {
        Glass red, blue;
        int turn;
        String history;

        State(int rx, int ry, int bx, int by) {
            this.red = new Glass(rx, ry);
            this.blue = new Glass(bx, by);
            this.turn = 0;
            this.history = "";
        }

        State(Glass red, Glass blue, int turn, String history) {
            this.red = red;
            this.blue = blue;
            this.turn = turn;
            this.history = history;
        }
    }
    static class Glass {
        int x, y;
        Glass(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    static final char[] dStr = {'R','D','L','U'};
    static final int[] dx = {0,1,0,-1};
    static final int[] dy = {1,0,-1,0};
    static void findMinTilt() {
        Queue<State> q = new ArrayDeque<>();
        boolean[][][][] visited = new boolean[n][m][n][m];

        State init = initState();
        q.offer(init);
        visited[init.red.x][init.red.y][init.blue.x][init.blue.y] = true;

        while(!q.isEmpty()) {
            State cur = q.poll();
            // 1. 10번 이하로 뺄 수 없다면
            if(cur.turn >= 10) continue;

            for(int dir=0; dir<4; dir++) {
                Glass newRed = rollGlass(cur.red, dir);
                Glass newBlue = rollGlass(cur.blue, dir);

                if(holeIn(newBlue)) continue;
                if(holeIn(newRed)) {
                    System.out.printf("%d\n%s", cur.turn+1, cur.history+dStr[dir]);
                    return;
                }
                // 두 구슬이 겹치는 경우
                // 거리 비례, 더 많이 움직인 구슬을 한 칸 뒤로 이동
                if(newRed.x == newBlue.x && newRed.y == newBlue.y) {
                    int redDist = Math.abs(cur.red.x - newRed.x) + Math.abs(cur.red.y - newRed.y);
                    int blueDist = Math.abs(cur.blue.x - newBlue.x) + Math.abs(cur.blue.y - newBlue.y);
                    if(redDist > blueDist) {
                        newRed.x -= dx[dir];
                        newRed.y -= dy[dir];
                    } else {
                        newBlue.x -= dx[dir];
                        newBlue.y -= dy[dir];
                    }
                }

                if(visited[newRed.x][newRed.y][newBlue.x][newBlue.y]) continue;
                visited[newRed.x][newRed.y][newBlue.x][newBlue.y] = true;
                q.offer(new State(newRed, newBlue, cur.turn+1, cur.history+dStr[dir]));
            }
        }
        System.out.println(-1);
    }
    static Glass rollGlass(Glass glass, int dir) {
        int x = glass.x;
        int y = glass.y;

        while(true) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(board[nx][ny] == '#') break;
            x = nx;
            y = ny;
            if(board[x][y] == 'O') break;
        }
        return new Glass(x, y);
    }
    static State initState() {
        int rx =-1, ry = -1, bx = -1, by = -1;
        for(int x=0; x<n; x++) {
            for(int y=0; y<m; y++) {
                if(board[x][y] == 'R') { rx = x; ry = y; }
                else if(board[x][y] == 'B') { bx = x; by = y; }
            }
        }
        return new State(rx, ry, bx, by);
    }
    static boolean holeIn(Glass glass) { return board[glass.x][glass.y] == 'O'; }
}
