package feb.week3.programmers;

import java.util.*;

public class 보물지도_박재환 {
    public static void main(String[] args) {
        int n = 5;
        int m = 4;
        int[][] hole = {{1, 4}, {2, 1}, {2, 2}, {2, 3}, {2, 4}, {3, 3}, {4, 1}, {4, 3}, {5, 3}};
        보물지도_박재환 problem = new 보물지도_박재환();
        int result = problem.solution(n, m, hole);
        System.out.println(result);
    }
    final int[] dx = {0,1,0,-1};
    final int[] dy = {1,0,-1,0};

    int[][] board;
    int n, m;
    public int solution(int n, int m, int[][] hole) {
        board = new int[m][n];
        this.n = n;
        this.m = m;
        /**
         * 시작 위치 [1,1] -> [0,0] 으로 변경
         * 한 칸 이동하는데 1 소모
         *
         * 보물은 [n-1,m-1]
         *
         * 신발을 신고 뛰면 한 번에 두 칸을 이동 가능 -> 함정도 건너뛸 수 있음
         * -> 한 번밖에 사용못함
         */

        for(int[] h : hole) {       // 함정 표시
            int x = h[1]-1;
            int y = h[0]-1;
            board[x][y] = -1;
        }

        for(int[] arr : board) System.out.println(Arrays.toString(arr));

        return findFastRout();
    }
    int findFastRout() {
        Queue<int[]> q = new ArrayDeque<>();
        int[][][] visited = new int[m][n][2];       // [x][y][0 / 1]
        for(int x=0; x<m; x++) {
            for(int y=0; y<n; y++) {
                Arrays.fill(visited[x][y], Integer.MAX_VALUE);
            }
        }
        q.offer(new int[] {0, 0, 0, 0});
        visited[0][0][0] = 0;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int jump = cur[2];
            int move = cur[3];

            if(x == m-1 && y == n-1) {
                printMap(visited);

                return move;
            }
            if(move > visited[x][y][jump]) continue;

            for(int dir=0; dir<4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                if(nx < 0 || nx >= m || ny < 0 || ny >= n) continue;        // 격자를 벗어나는 경우
                if(board[nx][ny] == -1 && jump == 0) {       // 함정인 경우 -> 뛰어넘어
                    nx += dx[dir];
                    ny += dy[dir];
                    if(nx < 0 || nx >= m || ny < 0 || ny >= n) continue;       // 격자를 벗어나는 경우
                    if(board[nx][ny] == -1) continue;                           // 또 함정인 경우
                    if(visited[nx][ny][1] <= move + 1) continue;
                    visited[nx][ny][1] = move + 1;
                    q.offer(new int[] {nx, ny, 1, move + 1});
                    continue;
                }
                if(board[nx][ny] == -1) continue;
                // 장애물이 없는 경우
                // 점프하거나 그냥 한 칸 이동하거나 가능
                if(visited[nx][ny][jump] > move + 1) {      // 현재 상태 그대로 이동
                    visited[nx][ny][jump] = move + 1;
                    q.offer(new int[] {nx, ny, jump, move + 1});
                }
                /**
                 * 일반 평지에서도 점프가 가능
                 * -> 가는길에 함정이 없는 경우가 있음
                 */
                if(jump == 0) {
                    nx += dx[dir];
                    ny += dy[dir];
                    if(nx < 0 || nx >= m || ny < 0 || ny >= n) continue;        // 격자를 벗어나는 경우
                    if(board[nx][ny] == -1) continue;                           // 함정인 경우
                    if(visited[nx][ny][1] <= move + 1) continue;
                    visited[nx][ny][1] = move + 1;
                    q.offer(new int[] {nx, ny, 1, move + 1});
                }
            }
        }
        return -1;
    }

    void printMap(int[][][] visited) {
        for(int x=0; x<m; x++) {
            for(int y=0; y<n; y++) {
                int v = Math.min(visited[x][y][0], visited[x][y][1]);
                System.out.print(v == Integer.MAX_VALUE ? "-" : v);
            }
            System.out.println();
        }
    }
}
