package feb.week2.swea;

import java.util.*;
import java.io.*;

public class 벌꿀채취_박재환 {
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
    static int solution() {
        maxProfit = 0;
        findAllCombi(0, 0, new int[2]);
        return maxProfit;
    }
    /**
     * [벌꿀 통 선택]
     * 가로로 연속한 M 개의 벌꿀통을 선택한다.
     * 이때 모든 벌꿀통의 합이 C 이하라면 한번에 모두 채취가 가능하다.
     *
     * C를 넘긴다면, 넘지 않는 칸 만큼만 채취한다.
     *
     * 겹칠 수 없으므로, 백트래킹 완전탐색
     */
    static int partialMax;
    static void findAllCombi(int selectedId, int arrId, int[] max) {
        if(selectedId == 2) {
            maxProfit = Math.max(maxProfit, max[0] + max[1]);
            return;
        }

        for(int id=arrId; id<n*n; id++) {
            int x = id/n;
            int y = id%n;

            if(y+m > n) continue;       // 연속해서 선택할 수 없음
            int[] candidate = new int[m];
            for(int i=0; i<m; i++) {
                candidate[i] = board[x][y+i];
            }
            partialMax = 0;
            getMaxPartialSum(0, candidate, 0, 0);
            max[selectedId] = partialMax;
            findAllCombi(selectedId+1, id+m, max);
        }
    }
    static void getMaxPartialSum(int id, int[] candidate, int sum, int profit) {
        if(sum > c) return;
        if(id == m) {
            partialMax = Math.max(partialMax, profit);
            return;
        }
        getMaxPartialSum(id+1, candidate, sum, profit);
        getMaxPartialSum(id+1, candidate, sum + candidate[id], profit + (candidate[id] * candidate[id]));
    }
}
