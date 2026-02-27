package feb.week4.boj;

import java.util.*;
import java.io.*;

public class 택배배송_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static List<int[]>[] connections;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        connections = new List[n+1];
        for(int i=0; i<n+1; i++) connections[i] = new ArrayList<>();
        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            connections[from].add(new int[] {to, cost});
            connections[to].add(new int[] {from, cost});
        }

        System.out.println(getMinCost());
    }
    static final long INF = Long.MAX_VALUE;
    static long getMinCost() {
        PriorityQueue<long[]> pq = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
        long[] dist = new long[n+1];
        Arrays.fill(dist, INF);

        pq.offer(new long[] {1, 0});
        dist[1] = 0;

        while(!pq.isEmpty()) {
            long[] cur = pq.poll();
            int from = (int)cur[0];
            long acc = cur[1];

            if(dist[from] < acc) continue;

            for(int[] connction : connections[from]) {
                int to = connction[0];
                int cost = connction[1];

                if(dist[to] > acc + cost) {
                    dist[to] = acc + cost;
                    pq.offer(new long[] {to, dist[to]});
                }
            }
        }
        return dist[n];
    }
}
