package jan.week5.boj;

import java.util.*;
import java.io.*;

public class 우주탐사선_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    /**
     * 모든 행성을 탐사하는데 걸리는 최소 시간을 계산한다.
     * 모든 행성을 탐사하는데 걸리는 최소 시간을 계산하여라
     */
    static final int INF = 10 * 1000 + 5;
    static int ALL;
    static int planetCount;
    static int[][] planetDist;
    static int[][] dp;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        planetCount = Integer.parseInt(st.nextToken());
        int launchPlanet = Integer.parseInt(st.nextToken());
        ALL = (1 << planetCount) - 1;
        planetDist = new int[planetCount][planetCount];
        for(int i=0; i<planetCount; i++) {
            Arrays.fill(planetDist[i], INF);
            planetDist[i][i] = 0;
        }

        for(int i=0; i<planetCount; i++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int j=0; j<planetCount; j++) {
                planetDist[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        findAllDist(planetCount);
        dp = new int[planetCount][(1 << planetCount)];
        for(int i=0; i<planetCount; i++) Arrays.fill(dp[i], -1);
        System.out.println(findMinDist(launchPlanet, (1 << launchPlanet)));
    }

    static void findAllDist(int planetCount) {
        for(int mid=0; mid<planetCount; mid++) {
            for(int start=0; start<planetCount; start++) {
                for(int end=0; end<planetCount; end++) {
                    planetDist[start][end] =
                            Math.min(planetDist[start][mid] + planetDist[mid][end], planetDist[start][end]);
                }
            }
        }
    }

    static int findMinDist(int cur, int visited) {
        if(visited == ALL) return 0;
        if(dp[cur][visited] != -1) return dp[cur][visited];
        int minCost = INF;
        for(int next=0; next<planetCount; next++) {
            if((visited & (1 << next)) != 0) continue;
            int cost =
                    planetDist[cur][next] +
                            findMinDist(next, visited | (1 << next));
            minCost = Math.min(minCost, cost);
        }

        return dp[cur][visited] = minCost;
    }
}
