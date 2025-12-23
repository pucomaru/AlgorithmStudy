package dec_4.boj;

import java.util.*;
import java.io.*;

public class 서강그라운드_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * 각 지역은 일정한 길이의 길로 다른 지역과 연결되어 있음
     * 양방향 통행이 가능함
     * 낙하한 지역을 중심으로 거리가 수색 범위(m) 이내의 모든 지역의 아이템을 습득 가능함
     *
     * => 예은이가 얻을 수 있는 아이템의 최대 개수
     */
    static StringTokenizer st;
    static final int INF = 1_000_000_000;
    static int n, m, r;
    static int[] items;
    static int[][] map;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken()); // 지역의 개수
        m = Integer.parseInt(st.nextToken()); // 수색 범위
        r = Integer.parseInt(st.nextToken()); // 길의 개수
        items = new int[n+1];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=1; i<n+1; i++) items[i] = Integer.parseInt(st.nextToken());
        /**
         * 지역의 개수가 최대 100 개
         * Mesh 형태로 모든 지역이 연결되어 있고, 모든 지역이 수색 범위 내라고 할 때
         * O(n^3)
         *
         * => 문제에서 요구하는 것은, 현재 내 위치에서 다른 지역까지의 거리
         * => 플로이드 워셜 접근 (O(n^3))
         */
        map = new int[n+1][n+1];
        // map 배열 초기화
        for(int i=0; i<n+1; i++) {
            for(int j=0; j<n+1; j++) {
                if(i==j) map[i][j] = 0;     // 자기 자신
                else map[i][j] = INF;
            }
        }
        // 길 정보 입력
        while(r-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            int dist = Integer.parseInt(st.nextToken());
            // 양방향 이동이 가능
            map[a][b] = dist;
            map[b][a] = dist;
        }
        findMaxItemArea();
        System.out.println(maxItemCnt);
    }

    static int maxItemCnt;
    static void findMaxItemArea() {
        findAllDist();
        maxItemCnt = 0;
        for(int to=1; to<n+1; to++) {
            int sum = 0;
            for(int from=1; from<n+1; from++) {
                if(map[to][from] == INF || map[to][from] > m) continue;
                sum += items[from];
            }
            maxItemCnt = Math.max(maxItemCnt, sum);
        }
    }

    /**
     * 플로이드 워셜
     */
    static void findAllDist() {
        for(int mid=1; mid<n+1; mid++) {
            for(int to=1; to<n+1; to++) {
                for(int from=1; from<n+1; from++) {
                    if(map[to][mid] == INF || map[mid][to] == INF) continue;
                    map[to][from] = Math.min(map[to][from], map[to][mid] + map[mid][from]);
                }
            }
        }
    }
}
