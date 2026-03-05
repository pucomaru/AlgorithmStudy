package mar.week1.boj;

import java.util.*;
import java.io.*;

public class Coins_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n;
    static int[] coins;
    static int target;
    static void init() throws IOException {
        int tc = Integer.parseInt(br.readLine().trim());
        while(tc-- > 0) {
            n = Integer.parseInt(br.readLine().trim());
            coins = new int[n];
            st = new StringTokenizer(br.readLine().trim());
            for(int i=0; i<n; i++) coins[i] = Integer.parseInt(st.nextToken());
            target = Integer.parseInt(br.readLine().trim());
            findAllCombi();
        }
    }
    static void findAllCombi() {
        int[] dp = new int[target+1];
        dp[0] = 1;      // 0원을 만드는 방법 = 1(아무 동전도 사용하지 않음)
        for(int coin : coins) {
            for(int i=coin; i<=target; i++) {
                dp[i] += dp[i-coin];
            }
        }
        System.out.println(dp[target]);
    }
}
