package feb.week1.boj;

import java.util.*;
import java.io.*;

public class 궁금한민호_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n;
    static int[][] map;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        map = new int[n][n];
        /**
         * 주어지는 경로는 이미 최소거리
         */
        for(int from=0; from<n; from++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int to=0; to<n; to++) map[from][to] = Integer.parseInt(st.nextToken());
        }
        System.out.println(getMinCostAndMinRoad());
    }
    static int getMinCostAndMinRoad() {
        boolean[][] unUsed = new boolean[n][n];
        /**
         * 플로이드 워셜을 이용해서 해당 거리가 최소 거리가 맞는지 검증
         */
        for(int mid=0; mid<n; mid++) {
            for(int from=0; from<n; from++) {
                for(int to=0; to<n; to++) {
                    if(mid == from || mid == to || from == to) continue;            // 실제 다른 도시와 연결된 간선만 비교대상으로 판단
                    if(map[from][to] > map[from][mid] + map[mid][to]) return -1;    // 최소 경로가 아니였을 때
                    else if(map[from][to] == map[from][mid] + map[mid][to]) {       // 동일한 비용의 경로가 존재한다면 -> 도로의 개수가 최소가 되어야하므로, 경유 도로 제거
                        unUsed[from][to] = true;
                    }
                }
            }
        }
        int sum = 0;
        for(int from=0; from<n; from++) {
            for(int to=0; to<n; to++) {
                if(unUsed[from][to]) continue;
                sum += map[from][to];
                unUsed[from][to] = true;
                unUsed[to][from] = true;
            }
        }
        return sum;
    }
}
