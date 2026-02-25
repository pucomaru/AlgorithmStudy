package feb.week4.boj;

import java.util.*;
import java.io.*;

public class 면접보는승범이네_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m, k;
    static int[] destination;
    static List<int[]>[] forward;       // 정방향
    static List<int[]>[] reverse;       // 역방향
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        forward = new List[n+1];
        reverse = new List[n+1];
        for(int i=0; i<n+1; i++) {
            forward[i] = new ArrayList<>();
            reverse[i] = new ArrayList<>();
        }

        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int dist = Integer.parseInt(st.nextToken());
            forward[from].add(new int[] {to, dist});
            reverse[to].add(new int[] {from, dist});
        }

        destination = new int[k];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<k; i++) destination[i] = Integer.parseInt(st.nextToken());

        long[] result = findFarCity();
        System.out.printf("%d\n%d", result[0], result[1]);
    }
    static final long INF = Long.MAX_VALUE;
    static PriorityQueue<long[]> pq;
    static long[] dist;
    static long[] findFarCity() {
        pq = new PriorityQueue<>((a, b) -> Long.compare(a[1], b[1]));
        dist = new long[n+1];
        Arrays.fill(dist, INF);

        for(int start : destination) {
            pq.offer(new long[] {start, 0});
            dist[start] = 0;
        }

        findShortestRoute();

        long[] result = new long[2];
        for(int i=1; i<n+1; i++) {
            if(dist[i] == INF) continue;
            if(result[1] < dist[i]) {
                result[0] = i;
                result[1] = dist[i];
            } else if(result[1] == dist[i]) {
                result[0] = Math.min(i, result[0]);
            }
        }
        return result;
    }
    static void findShortestRoute() {
        while(!pq.isEmpty()) {
           long[] cur = pq.poll();
           int from = (int)cur[0];
           long acc = cur[1];

           if(dist[from] < acc) continue;

           for(int[] next : reverse[from]) {
               int to = next[0];
               int cost = next[1];

               if(dist[to] > cost + acc) {
                   dist[to] = cost + acc;
                   pq.offer(new long[] {to, dist[to]});
               }
           }
       }
    }
}
