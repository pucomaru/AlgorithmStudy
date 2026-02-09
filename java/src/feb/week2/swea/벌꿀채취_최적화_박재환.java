package feb.week2.swea;

import java.util.*;
import java.io.*;

public class 벌꿀채취_최적화_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for(int i=1; i<tc+1; i++) {
            sb.append('#').append(i).append(' ');
            init();
            sb.append('\n');
        }
        br.close();
        System.out.println(sb);
    }
    /**
     * N x N 격자의 각 칸에 꿀의 양이 주어진다.
     * 두 명의 일꾼이 있다.
     * 각 M개의 벌통을 채취할 수 있다.
     *      단 가로로 연속된 M개의 벌통을 선택해야한다.
     * 서로 겹쳐서 선택할 수 없다.
     *
     * 두 일꾼이 채취할 수 있는 꿀의 최대 양은 C 이다.
     * => C 를 초과했을 때는, 하나의 벌통만 선택해야한다.
     *
     * 수익은 각 용기의 꿀의양 ** 2
     *
     */
    static StringTokenizer st;
    static int n, m, c;
    static int[][] board;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());

        board = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }
        sb.append(solution());
    }
    static int maxProfit;
    static int[][] profitBoard;
    static int solution() {
        maxProfit = 0;
        profitBoard = new int[n][n];
        getPreProfit();
        for (int x1 = 0; x1 < n; x1++) {
            for (int y1 = 0; y1 + m <= n; y1++) {
                int p1 = profitBoard[x1][y1];

                for (int x2 = x1; x2 < n; x2++) {
                    int startY = (x1 == x2) ? y1 + m : 0;
                    for (int y2 = startY; y2 + m <= n; y2++) {
                        int p2 = profitBoard[x2][y2];
                        maxProfit = Math.max(maxProfit, p1 + p2);
                    }
                }
            }
        }
        return maxProfit;
    }
    static int partialMax;
    static void getPreProfit() {
        for(int x=0; x<n; x++) {
            for(int y=0; y+m<=n; y++) {
                partialMax = 0;
                getMaxPartialProfit(x, y, 0, 0, 0);
                profitBoard[x][y] = partialMax;
            }
        }
    }

    static void getMaxPartialProfit(int x, int y, int id, int sum, int profit) {
        if(sum > c) return;
        if(id == m) {
            partialMax = Math.max(partialMax, profit);
            return;
        }

        getMaxPartialProfit(x, y, id+1, sum, profit);
        int value = board[x][y+id];
        getMaxPartialProfit(x, y, id+1, sum + value, profit + (value * value));
    }
}
