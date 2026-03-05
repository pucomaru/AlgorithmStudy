package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 벼락치기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static class Phase {
        int t;
        int s;

        Phase(int t, int s) { this.t = t; this.s = s; }
    }
    static StringTokenizer st;
    static int n, t;
    static Phase[] phases;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        t = Integer.parseInt(st.nextToken());

        phases = new Phase[n];
        for(int i=0; i<n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int t = Integer.parseInt(st.nextToken());
            int s = Integer.parseInt(st.nextToken());
            Phase phase = new Phase(t, s);
            phases[i] = phase;
        }

        minTimeMaxScore();
    }

    static final int INF = 987654321;
    static void minTimeMaxScore() {
        int perfect = 0;
        for(Phase p : phases) perfect += p.s;

        int[] dp = new int[perfect+1];
        Arrays.fill(dp, INF);
        dp[0] = 0;
        for(Phase p : phases) {
            for(int i=perfect; i>=0; i--) {
                if(dp[i] == INF) continue;

                int nScore = i + p.s;
                if(dp[nScore] > dp[i] + p.t) dp[nScore] = dp[i] + p.t;
            }
        }

        for(int i=perfect; i>=0; i--) {
            if(dp[i] <= t) {
                System.out.println(i);
                return;
            }
        }
    }
}
