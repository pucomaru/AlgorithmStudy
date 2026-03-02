package feb.week4.codetree;

import java.util.*;
import java.io.*;

public class 코드트리투어_박재환 {
    /**
     * 각 도시 0 ~ N-1
     * 도로는 무방향
     */
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static final int BUILD_LAND = 100;
    static final int MAKE_PACKAGE = 200;
    static final int DELETE_PACKAGE = 300;
    static final int SELL_PACKAGE = 400;
    static final int CHANGE_START = 500;
    static StringTokenizer st;
    static void init() throws IOException {
        int tc = Integer.parseInt(br.readLine().trim());
        while(tc-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());
            if(cmd == BUILD_LAND) {
                buildLand();
            } else if(cmd == MAKE_PACKAGE) {
                makePackage();
            } else if(cmd == DELETE_PACKAGE) {
                deletePackage();
            } else if(cmd == SELL_PACKAGE) {
                sb.append(sellPackage()).append('\n');
            } else if(cmd ==CHANGE_START) {
                changeStart();
            }
        }
    }
    static int n, m;
    static int start;
    static List<int[]>[] roads;
    static Map<Integer, Package> packages;
    static PriorityQueue<Package> bestPackage;
    static void buildLand() {
        packages = new HashMap<>();
        bestPackage = new PriorityQueue<>();
        start = 0;                  // 처음 출발지는 0

        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        roads = new List[n];        // 도시(0~(n-1))
        for(int i=0; i<n; i++) roads[i] = new ArrayList<>();

        for(int i=0; i<m; i++) {
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());
            roads[from].add(new int[] {to, cost});
            roads[to].add(new int[] {from, cost});
        }

        getAllCost();
    }
    static class Package implements Comparable<Package>{
        int id;
        int revenue;
        int dest;
        long profit;

        Package(int id, int revenue, int dest) {
            this.id = id;
            this.revenue = revenue;
            this.dest = dest;
        }

        void setProfit(long cost) {
            this.profit = this.revenue - cost;
        }

        public int compareTo(Package o) {
            if(o.profit == this.profit) return Integer.compare(this.id, o.id);
            return Long.compare(o.profit, this.profit);
        }
    }
    static void makePackage() {
        int id = Integer.parseInt(st.nextToken());
        int revenue = Integer.parseInt(st.nextToken());
        int dest = Integer.parseInt(st.nextToken());
        Package pk = new Package(id, revenue, dest);
        pk.setProfit(dist[dest]);
        packages.put(id, pk);
        bestPackage.offer(pk);
    }
    static void deletePackage() {
        int id = Integer.parseInt(st.nextToken());
        if(packages.get(id) == null) return;
        Package removePackage = packages.get(id);
        packages.remove(id);
//        bestPackage.remove(removePackage);        => PQ 에서 remove 는 O(N)
    }
    static int sellPackage() {
        /**
         * 최적화가 필요한 함수
         * -> 모든 Package 를 다 탐색 O(N) -> 최대 30,000 번
         * sellPackage() 최대 30,000 번 호출
         *
         * -> TLE
         */
        while(!bestPackage.isEmpty()) {
            Package cur = bestPackage.peek();
            // LAZY Delete 적용
            if(packages.get(cur.id) == null) {
                bestPackage.poll();
                continue;
            }
            if(cur.profit < 0) break;

            // 판매 가능
            bestPackage.poll();
            packages.remove(cur.id);
            return cur.id;
        }
        return -1;
    }
    static void changeStart() {
        /**
         * 최대 15번 호출
         * -> 이 함수에서 시간 막 써도 될 듯
         */
        int id = Integer.parseInt(st.nextToken());
        start = id;
        getAllCost();
        bestPackage.clear();
        for(Package pk : packages.values()) {
            pk.setProfit(dist[pk.dest]);
            bestPackage.offer(pk);
        }
    }
    static final long INF = Long.MAX_VALUE;
    static long[] dist;
    static void getAllCost() {
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[1],b[1]));
        dist = new long[n];
        Arrays.fill(dist, INF);

        dist[start] = 0;
        pq.offer(new long[] {start, 0});

        while(!pq.isEmpty()) {
            long[] cur = pq.poll();
            int from = (int)cur[0];
            long acc = cur[1];

            if(dist[from] < acc) continue;
            for(int[] conn : roads[from]) {
                int to = conn[0];
                int cost = conn[1];
                if(dist[to] > cost + acc) {
                    dist[to] = cost + acc;
                    pq.offer(new long[] {to, dist[to]});
                }
            }
        }
    }
}
