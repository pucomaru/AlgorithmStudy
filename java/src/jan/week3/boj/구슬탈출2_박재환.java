package jan.week3.boj;

import java.util.*;
import java.io.*;

public class 구슬탈출2_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * N x M 크기의 격자가 있다.
     * 가장 바깥 행과 열을 모두 막혀있다.
     *
     * 빨간 구슬, 파란 구슬이 각각 하나씩 들어가 있다.
     * => 빨간 구슬만 빼내고 싶다.
     *
     * 이리 저리 굴려가며 빼야한다.
     * - 왼쪽으로 기울이기
     * - 오른쪽으로 기울이기
     * - 위쪽으로 기울이기
     * - 아래쪽으로 기울이기
     *
     * 각 동작에서 공은 동시에 움직인다.
     * 한 번의 동작은 구슬이 더 이상 움직이지 않을 때까지이다.
     *
     * . : 빈 칸
     * # : 장애물 또는 벽
     * o : 구멍
     * R : 빨간 구슬
     * B : 파란 구슬
     *
     * => 10 번 이하로 움직여서 뺄 수 없다면 -1을 출력한다.
     */
    static StringTokenizer st;
    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};

    static int n, m;
    static char[][] map;

    static boolean[][][][] visited;     // [rx][ry][bx][by]
    static int[] end;
    static int minChangeCount;
    static void init() throws IOException {
        State state = new State();
        minChangeCount = Integer.MAX_VALUE;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        map = new char[n][m];
        visited = new boolean[n][m][n][m];
        for(int x=0; x<n; x++) {
            String input = br.readLine().trim();
            for(int y=0; y<m; y++) {
                map[x][y] = input.charAt(y);
                if(map[x][y] == 'R') state.setRedBead(x, y);
                else if(map[x][y] == 'B') state.setBlueBead(x, y);
                else if(map[x][y] == 'O') end = new int[] {x, y};
            }
        }
        findMinTurnChange(state);
        System.out.println(minChangeCount == Integer.MAX_VALUE ? -1 : minChangeCount);
    }

    static class State {
        int rx, ry;
        int bx, by;
        int dirChangeCount;

        State() {
            this.dirChangeCount = 0;
        }

        State(int rx, int ry, int bx, int by, int dirChangeCount) {
            this.rx = rx;
            this.ry = ry;
            this.bx = bx;
            this.by = by;
            this.dirChangeCount = dirChangeCount;
        }

        void setRedBead(int x, int y) {
            this.rx = x;
            this.ry = y;
        }
        void setBlueBead(int x, int y) {
            this.bx = x;
            this.by = y;
        }
    }

    /**
     * 구슬을 더 이상 움직일 수 없는 위치까지 움직이기
     */
    static int[] rollBead( int x, int y, int dir) {
        int moveCount = 0;

        while(true) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            if(map[nx][ny] == '#') break;

            x = nx;
            y = ny;
            moveCount++;        // 이동한 거리 카운트

            if(map[nx][ny] == 'O') break;
        }

        return new int[] {x, y, moveCount};
    }

    static void findMinTurnChange(State state) {
        Queue<State> q = new ArrayDeque<>();
        q.offer(state);
        visited[state.rx][state.ry][state.bx][state.by] = true;

        while(!q.isEmpty()) {
            State cur = q.poll();

            if(cur.dirChangeCount >= 10) continue;   // 제한 턴을 넘긴 경우

            for(int dir=0; dir<4; dir++) {
                int[] newRed = rollBead(cur.rx, cur.ry, dir);
                int[] newBlue = rollBead(cur.bx, cur.by, dir);

                // 파란색이 먼저 빠진 경우
                if(newBlue[0] == end[0] && newBlue[1] == end[1]) continue;
                // 빨간색이 빠진 경우 -> 정답
                if(newRed[0] == end[0] && newRed[1] == end[1]) {
                    minChangeCount = Math.min(minChangeCount, cur.dirChangeCount+1);
                    return;
                }

                // 만약 구슬이 겹쳐있다면
                if(newRed[0] == newBlue[0] && newRed[1] == newBlue[1]) {
                    // 더 많이 이동한 구슬을 한 칸 뒤로 이동시킨다.
                    if(newRed[2] > newBlue[2]) {
                        newRed[0] -= dx[dir];
                        newRed[1] -= dy[dir];
                    } else {
                        newBlue[0] -= dx[dir];
                        newBlue[1] -= dy[dir];
                    }
                }

                // 같은 조건으로 방문한 적이 있는지 확인
                if(visited[newRed[0]][newRed[1]][newBlue[0]][newBlue[1]]) continue;

                visited[newRed[0]][newRed[1]][newBlue[0]][newBlue[1]] = true;
                q.offer(new State(newRed[0], newRed[1], newBlue[0], newBlue[1], cur.dirChangeCount+1));
            }
        }
    }
}
