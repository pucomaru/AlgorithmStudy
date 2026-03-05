package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 내리막길_박재환 {
    /**
     * 격자의 각 칸은 높이
     * 이동은 상하좌우 인접한 위치로만
     *
     * (0,0) -> (n,m)
     * 항상 높이가 더 낮은 지점으로만 이동
     * 내리막으로만 이동가능한 경로의 개수
     */
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int m, n;
    static int[][] board;
    static int[][] dp;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        m = Integer.parseInt(st.nextToken());
        n = Integer.parseInt(st.nextToken());

        board = new int[m][n];
        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int j=0; j<n; j++) board[i][j] = Integer.parseInt(st.nextToken());
        }

        dp = new int[m][n];
        for(int i=0; i<m; i++) Arrays.fill(dp[i], -1);
        System.out.println(findAllRoute(0, 0));
        for(int i=0; i<m; i++) System.out.println(Arrays.toString(dp[i]));
    }
    static final int[] dx = {0,1,0,-1};
    static final int[] dy = {1,0,-1,0};
    static int findAllRoute(int x, int y) {
        if (dp[x][y] != -1) return dp[x][y];
        if(x == m-1 && y == n-1) return 1;
        int h = board[x][y];
        dp[x][y] = 0;
        for(int d=0; d<4; d++) {
            int nx = x + dx[d];
            int ny = y + dy[d];
            if(nx < 0 || ny < 0 || nx >= m || ny >= n) continue;
            if(board[nx][ny] >= h) continue;

            dp[x][y] += findAllRoute(nx, ny);
        }
        return dp[x][y];
    }
}
