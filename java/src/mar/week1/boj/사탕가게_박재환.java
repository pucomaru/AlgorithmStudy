package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 사탕가게_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static class Candy {
        int v;
        double p;

        Candy(int v, double p) { this.v = v; this.p = p; }
    }
    static StringTokenizer st;
    static int n;
    static double max;
    static Candy[] candies;
    static void init() throws IOException {
        while(true) {
            st = new StringTokenizer(br.readLine().trim());
            n = Integer.parseInt(st.nextToken());
            max = Double.parseDouble(st.nextToken());
            if(n == 0 && max == 0.00) break;

            candies = new Candy[n];
            for(int i=0; i<n; i++) {
                st = new StringTokenizer(br.readLine().trim());
                int v = Integer.parseInt(st.nextToken());
                double p = Double.parseDouble(st.nextToken());
                Candy candy = new Candy(v, p);
                candies[i] = candy;
            }

            sb.append(getMaxValue()).append('\n');
        }
    }

    static final int INF = -987654321;
    static int getMaxValue() {
        int intMax = (int)Math.round(max * 100);
        int[] dp = new int[intMax+1];
        Arrays.fill(dp, INF);
        dp[0] = 0;
        for(Candy c : candies) {
            int intP = (int)Math.round(c.p*100);
            for(int i=0; i<=intMax-intP; i++) {
                if(dp[i] == INF) continue;
                dp[i+intP] = Math.max(dp[i+intP], dp[i] + c.v);
            }
        }
        int ans = 0;
        for (int i = 0; i <= intMax; i++) ans = Math.max(ans, dp[i]);
        return ans;
    }
}
