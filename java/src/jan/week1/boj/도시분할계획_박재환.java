package jan.week1.boj;

import java.util.*;
import java.io.*;

public class 도시분할계획_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * 마을은 N개의 집과, M개의 길로 연결되어있다.
     * 길은 어느 방향으로든지 다닐 수 있다. (양방향)
     * 각 길마다 길을 유지하는데 드는 유지비가 있다.
     * 임의의 두 집 사이에 경로가 항상 존재한다.
     *
     * 마을 길의 유지비의 힙을 최소로 하고싶다. -> 최소스패님트리
     */
    static StringTokenizer st;
    static int n, m;
    static PriorityQueue<int[]> roads;          // [from, to, cost]
    static List<int[]>[] graph;                   // [from]<[to, cost]>
    static void init() throws IOException {
        roads = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));     // 유지비용을 기준으로 오름차순 정렬
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());   // 집의 개수
        m = Integer.parseInt(st.nextToken());   // 길의 개수
        graph = new List[n+1];
        for(int i=0; i<n+1; i++) graph[i] = new ArrayList<>();
        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());
            roads.offer(new int[] {from, to, cost});
            graph[to].add(new int[] {from, cost});
            graph[from].add(new int[] {to, cost});
        }

        System.out.println(getMinMaintainCost_Kruskal());
        System.out.println(getMinMaintainCost_Prim());
    }

    static long getMinMaintainCost_Kruskal() {
        /**
         * [최소스패닝트리(MST)]
         * - 최소한의 비용으로, 모든 노드를 연결
         * -> N개의 노드가 있다면, N-1 개의 간선으로 모든 노드를 연결 가능
         * ---
         * 크루스칼 알고리즘 사용 -> Union Find 알고리즘 사용
         *
         * 문제 조건 중 두 개의 마을로 분할하려한다.
         * => 마을을 분할할 때, 분리된 마을 내의 집들은 서로 연결되어야한다.
         * => MST에서 가장 큰 간선을 하나를 끊는다.
         * ---
         * 프림 알고리즘 사용
         */
        make();
        int certainRoads = 0;
        long totalCost = 0;
        int maxCost = 0;
        while(!roads.isEmpty()) {
            int[] road = roads.poll();
            int from = road[0];
            int to = road[1];
            int cost = road[2];

            if(union(from, to)) {
                totalCost += cost;
                maxCost = Math.max(cost, maxCost);
                if(++certainRoads == n-1) break;
            }
        }
        return totalCost - maxCost;
    }

    static int[] parents;
    static void make() {
        parents = new int[n+1];
        for(int i=0; i<n+1; i++) parents[i] = i;
    }

    static int find(int target) {
        if(parents[target] == target) return parents[target];

        return parents[target] = find(parents[target]);
    }

    static boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if(rootA == rootB) return false;      // 사이클 내

        parents[rootB] = rootA;
        return true;
    }

    static long getMinMaintainCost_Prim() {
        PriorityQueue<int[]> roads = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        boolean[] visited = new boolean[n+1];
        int totalCost = 0;
        int maxCost = 0;

        roads.offer(new int[] {1, 0});

        while(!roads.isEmpty()) {
            int[] cur = roads.poll();
            int from = cur[0];
            int cost = cur[1];

            if(visited[from]) continue;
            visited[from] = true;
            totalCost += cost;
            maxCost = Math.max(cost, maxCost);

            for(int[] connInfo : graph[from]) {
                int to = connInfo[0];
                int toCost = connInfo[1];

                if(visited[to]) continue;
                roads.offer(new int[] {to, toCost});
            }
        }

        return totalCost - maxCost;
    }
}
