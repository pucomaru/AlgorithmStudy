package jan.week2.boj;

import java.util.*;
import java.io.*;

public class 외판원순회_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * 1~N번까지 번호가 매겨져 있다.
     * 도시 사이에는 길이 없을 수도 있다.
     *
     * 한 도시에서 출발해 N개의 도시를 모두 거쳐 다시 원래 도시로 돌아오는 순회 경로를 구한다.
     * => TSP
     *
     * N 은 최대 16이다.
     */
    static StringTokenizer st;
    static int n;
    static int[][] map;
    static int FULL;
    static int[][] costArr;
    static final int INF = 16_000_001;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        FULL = (1 << n) -1;
        map = new int[n][n];
        costArr = new int[n][1<<n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            Arrays.fill(costArr[x], -1);
            for(int y=0; y<n; y++) map[x][y] = Integer.parseInt(st.nextToken());
        }
        System.out.println(findMinCostToTSP(0, 1));;
    }

    static int findMinCostToTSP(int cur, int visited) {     // 현 위치와, 방문 상태 (비트마스킹)
        // 모두 방문 했다면 -> 다시 초기 위치로 돌아가는 비용 반환
        if(visited == FULL) return map[cur][0] == 0 ? INF : map[cur][0];
        // 이전에 먼저 방문한 적이 있다면 값 재사용
        if(costArr[cur][visited] != -1) return costArr[cur][visited];

        costArr[cur][visited] = INF;
        for(int next=0; next<n; next++) {
            // 이미 이전에 방문 했다면
            if((visited & 1<<next) != 0) continue;
            if(map[cur][next] == 0) continue;

            int totalCost = map[cur][next] + findMinCostToTSP(next, visited | 1 << next);

            costArr[cur][visited] = Math.min(costArr[cur][visited], totalCost);
        }
        return costArr[cur][visited];
    }
}
