package feb.week3.swea;

import java.util.*;
import java.io.*;

public class 혁진이의프로그램검증_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for(int i=0; i<tc; i++) {
            sb.append('#').append(i+1).append(' ').append(init()).append('\n');
        }
        br.close();
        System.out.println(sb);
    }
    static class State {
        int x, y;
        int dir;
        int memory;

        State(int x, int y, int dir, int memory) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.memory = memory;
        }
    }
    static final String YES = "YES";
    static final String NO = "NO";
    static StringTokenizer st;
    static int r,c;
    static char[][] board;
    static String init() throws IOException {
        /**
         * 문자들은 2차원 격자 모양으로 주어진다.
         *
         * 초기 위치는 가장 왼쪽 위에 있는 문자, 이동 방향은 오은쪽
         * 다음 이동이 2차원 격자 밖으로 나간다면, 반대편에 있는 위치로 이동한다.
         */
        st = new StringTokenizer(br.readLine().trim());
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        board = new char[r][c];
        for(int x=0; x<r; x++) {
            String input = br.readLine().trim();
            for(int y=0; y<c; y++) board[x][y] = input.charAt(y);
        }

        return canExit() ? YES : NO;
    }
    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static boolean canExit() {
        Queue<State> q = new ArrayDeque<>();
        // 메모리의 범위는 0 ~ 15
        boolean[][][][] visited = new boolean[r][c][4][16];

        // 초기 위치 설정
        q.offer(new State(0,0,0,0));
        visited[0][0][0][0] = true;

        while(!q.isEmpty()) {
            State cur = q.poll();
            int x = cur.x;
            int y = cur.y;
            int dir = cur.dir;
            int memory = cur.memory;

            char cmd = board[cur.x][cur.y];

            if(cmd == '@') return true;       // 종료 조건

            if('0' <= cmd && '9' >= cmd) memory = cmd - '0';
            else if('+' == cmd) memory = (memory+1)%16;
            else if('-' == cmd) memory = (memory-1+16)%16;
            else if('>' == cmd) dir = 0;
            else if('v' == cmd) dir = 1;
            else if('<' == cmd) dir = 2;
            else if('^' == cmd) dir = 3;
            else if('_' == cmd) dir = memory > 0 ? 2 : 0;
            else if('|' == cmd) dir = memory > 0 ? 3 : 1;

            if('?' == cmd) {
                for(int d=0; d<4; d++) {
                    int nx = (x + dx[d] + r) % r;
                    int ny = (y + dy[d] + c) % c;
                    if (!visited[nx][ny][d][memory]) {
                        visited[nx][ny][d][memory] = true;
                        q.offer(new State(nx, ny, d, memory));
                    }
                }
                continue;
            }

            int nx = (x + dx[dir] + r) % r;
            int ny = (y + dy[dir] + c) % c;

            if (!visited[nx][ny][dir][memory]) {
                visited[nx][ny][dir][memory] = true;
                q.offer(new State(nx, ny, dir, memory));
            }
        }
        return false;
    }
}
