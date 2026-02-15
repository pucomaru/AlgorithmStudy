package feb.week2.boj;

import java.util.*;
import java.io.*;

public class 벽부수고이동하기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * N x M
     * - 0 : 이동 가능
     * - 1 : 이동 불가 (벽)
     *
     * 벽을 부수고 이동하는 것이 빠른 경우, 벽을 한 개까지 부수도 이동해도 된다.
     *
     * -> 3차원 방문 배열 [N][M][2]
     */
    static StringTokenizer st;
    static int n, m;
    static int[][] board;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        board = new int[n][m];
        for(int x=0; x<n; x++) {
            String input = br.readLine().trim();
            for(int y=0; y<m; y++) board[x][y] = input.charAt(y) - '0';
        }

        System.out.println(solution());
    }
    static int solution() {
        int[] dx = {0,1,0,-1};
        int[] dy = {1,0,-1,0};

        Queue<int[]> q = new ArrayDeque<>();

        boolean[][][] visited = new boolean[n][m][2];   // [0] : 벽 안부숨, [1] : 벽 부숨
        // 초기위치 설정
        q.offer(new int[] {0,0,1,0});   // x, y, 누적이동, 부순 벽
        visited[0][0][0] = true;        // 초기 위치도 이동경로에 포함 ( 초기 상태, 벽을 부수지 않음 )

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0];
            int curY = cur[1];
            int curBlock = cur[2];
            int curBreak = cur[3];

            if(curX == n-1 && curY == m-1) return curBlock;

            for(int d=0; d<4; d++) {
                int nx = curX + dx[d];
                int ny = curY + dy[d];
                
                if(nx < 0 || ny < 0 || nx >= n || ny >= m) continue;
                if(visited[nx][ny][curBreak]) continue;
                /**
                 * 빈 칸이거나, 벽일 수 있음
                 */
                if(board[nx][ny] == 1) {        // 벽이라면, (1) 피한다. (2) 부순다.
                    if(curBreak == 0) {
                        visited[nx][ny][curBreak+1] = true;
                        q.offer(new int[]{nx, ny, curBlock + 1, curBreak + 1});
                    }
                    continue;
                }
                visited[nx][ny][curBreak] = true;
                q.offer(new int[] {nx, ny, curBlock+1, curBreak});
            }
        }
        return -1;
    }
}
