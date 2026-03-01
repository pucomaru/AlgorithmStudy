package feb.week4.boj;

import java.util.*;
import java.io.*;

public class 동전2_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, k;
    static int[] arr;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        arr = new int[n];
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(br.readLine().trim());

        System.out.println(minCoinCombi());
    }
    static final int INF = 1_000_007;
    static int minCoinCombi() {
        /**
         * 동전은 여러번 사용할 수 있다.
         */
        int[] dp = new int[k+1];
        Arrays.fill(dp, INF);

        dp[0] = 0;      // 동전을 한개도 사용하지 않고 만들 수 있는 경우
        for(int coin : arr) {       // 사용할 동전
            for(int i=coin; i<k+1; i++) {
                dp[i] = Math.min(dp[i], dp[i-coin] + 1);
            }
        }

        return dp[k] == INF ? -1 : dp[k];
    }
}
