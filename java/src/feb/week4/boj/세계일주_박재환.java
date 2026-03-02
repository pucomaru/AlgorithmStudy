package feb.week4.boj;

import java.util.*;
import java.io.*;

public class 세계일주_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static class Country {
        int x, y;
        Country(int x, int y) { this.x = x; this.y = y; }
    }
    static int FULL;
    static StringTokenizer st;
    static int n;
    static Country[] countries;
    static double[][] distArr;
    static double[][] dp;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        countries = new Country[n];
        for(int i=0; i<n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            Country country = new Country(x, y);
            countries[i] = country;
        }

        FULL = (1 << n) - 1;
        distArr = new double[n][n];
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(i==j) {
                    distArr[i][j] = 0;
                } else {
                    double dx = countries[i].x - countries[j].x;
                    double dy = countries[i].y - countries[j].y;
                    distArr[i][j] = Math.sqrt(dx*dx + dy*dy);
                }
            }
        }

        dp = new double[n][FULL];
        for(int i=0; i<n; i++) Arrays.fill(dp[i], -1);
        System.out.println(travelAllCountry(0, 1));
    }
    static final int INF = Integer.MAX_VALUE;
    static double travelAllCountry(int curCountry, int visited) {
        if(visited == FULL) {                   // 모든 도시를 방문한 경우
            return distArr[curCountry][0];      // 시작점으로 복귀
        }

        if(dp[curCountry][visited] != -1) return dp[curCountry][visited];       // 이미 방문했다면

        dp[curCountry][visited] = INF;
        for(int next=0; next<n; next++) {
            if((visited & (1<<next)) != 0) continue;        // 이미 방문한 곳 패스
            double candidate = distArr[curCountry][next]
                    + travelAllCountry(next, visited | (1<<next));
            dp[curCountry][visited] =
                    Math.min(dp[curCountry][visited], candidate);
        }
        return dp[curCountry][visited];
    }
}
