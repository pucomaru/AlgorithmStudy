package feb.week2.codetree;

import java.util.*;
import java.io.*;

public class 개구리의여행_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    /**
     * N x N
     * - 안전한 돌 (.)
     * - 미끄러운 돌 (S)
     * - 천적이 사는 돌 (#)
     *
     * 개구리의 초기 점프력은 1
     *
     * [점프]
     * - 상하좌우 k만큼 이동 가능
     * - 이동하려는 위치에 돌이 없다면 이동 불가
     * - 미끄러운 돌로 이동 불가
     * - 천적이 사는 돌 이동 불가
     * - 점프 시 1만큼 시간 소요
     *
     * [점프력 증가]
     * - 점프력을 1 증가 -> 최대 5까지 증가
     * - 점프력 증가 후 k 라고 할 때, K**2 만큼 시간 소요
     *
     * [점프력 감소]
     * - 점프력 감소
     * - 기존 점프력 K, (1 ~ K-1 로 감소)
     * - 1만큼 시간 소요
     *
     * q번의 여행 계획 -> 최대한 짧은 시간 내 여행 끝
     */
    static StringTokenizer st;
    static int n;
    static char[][] board;
    static int sx, sy, ex, ey;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        board = new char[n][n];
        for(int x=0; x<n; x++) {
            String line = br.readLine().trim();
            for(int y=0; y<n; y++) board[x][y] = line.charAt(y);
        }
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            // 출발
            sx = Integer.parseInt(st.nextToken())-1;
            sy = Integer.parseInt(st.nextToken())-1;
            // 도착
            ex = Integer.parseInt(st.nextToken())-1;
            ey = Integer.parseInt(st.nextToken())-1;
            sb.append(solution()).append('\n');
        }
    }
    static final int INF = 987654321;
    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static int solution() {
        /**
         * 각 칸에서 할 수 있는 행동
         *
         * 1. 점프
         * 2. 점프력 증가
         * 3. 점프력 감소
         * => 각 칸에서 가중치가 다르다. -> BFS 불가,
         */
        PriorityQueue<State> pq = new PriorityQueue<>();
        int[][][] visited = new int[n][n][6];
        for(int x=0; x<n; x++){
            for(int y=0; y<n; y++) Arrays.fill(visited[x][y], INF);
        }

        // 초기 위치
        pq.offer(new State(sx, sy, 1, 0));      // x, y, jump, time
        visited[sx][sy][1] = 0;

        while(!pq.isEmpty()) {
            State cur = pq.poll();
            int x = cur.x;
            int y = cur.y;
            int j = cur.jump;
            int t = cur.time;
            if(visited[x][y][j] < t) continue;
            if(x == ex && y == ey) return t;
            // 1. 점프
            for(int d=0; d<4; d++) {
                int nx = x + (dx[d]*j);
                int ny = y + (dy[d]*j);

                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                // 지나가는 경로에 천적이 있는지
                if(!checkPath(x, y, j, d)) continue;
                if(board[nx][ny] != '.') continue;
                if(visited[nx][ny][j] > t + 1) {
                    visited[nx][ny][j] = t + 1;
                    pq.offer(new State(nx, ny, j, visited[nx][ny][j]));
                }
            }
            // 2. 점프력 증가
            if(j < 5) {
                int nj = j + 1;
                int nt = t + (nj*nj);
                if(visited[x][y][nj] > nt) {
                    visited[x][y][nj] = nt;
                    pq.offer(new State(x, y, nj, visited[x][y][nj]));
                }
            }
            // 3. 점프력 감소
            if(j > 1) {
                for(int i=1; i<j; i++) {
                    if(visited[x][y][i] > t + 1) {
                        visited[x][y][i] = t + 1;
                        pq.offer(new State(x, y, i, visited[x][y][i]));
                    }
                }
            }
        }

        return -1;
    }
    static class State implements Comparable<State> {
        int x, y;
        int jump;
        int time;
        State(int x, int y, int jump, int time) {
            this.x = x;
            this.y = y;
            this.jump = jump;
            this.time = time;
        }
        public int compareTo(State o) {
            return Integer.compare(this.time, o.time);
        }
    }
    static boolean checkPath(int x, int y, int j, int dir) {
        switch(dir) {
            case 1: case 3:
                while(j-- > 0) {
                    x += dx[dir];
                    if(board[x][y] == '#') return false;
                }
                break;
            default :
                while(j-- > 0) {
                    y += dy[dir];
                    if(board[x][y] == '#') return false;
                }
        }
        return true;
    }
}

