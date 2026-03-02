package feb.week4.boj;

import java.util.*;
import java.io.*;

public class K번째최단경로찾기_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static final int INF = Integer.MAX_VALUE;

    static StringTokenizer st;
    static int n, m, k;
    static List<int[]>[] connections;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 도시 수
        m = Integer.parseInt(st.nextToken());       // 도로 수
        k = Integer.parseInt(st.nextToken());       // k번째의 최단 경로 구하기

        connections = new List[n+1];
        for (int i = 1; i <= n; i++) connections[i] = new ArrayList<>();
        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int dist = Integer.parseInt(st.nextToken());
            connections[from].add(new int[] {to, dist});
        }

        int[][] dist = getKRoute();
        for(int i=1; i<n+1; i++) {
            sb.append(dist[i][k-1] == INF ? -1 : dist[i][k-1]).append('\n');
        }
    }
    static int[][] getKRoute() {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        int[][] dist = new int[n+1][k];
        for(int i=0; i<n+1; i++) Arrays.fill(dist[i], INF);

        pq.offer(new int[] {1, 0});
        dist[1][0] = 0;

        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int from = cur[0];
            int curDist = cur[1];

            for(int[] connection : connections[from]) {
                int to = connection[0];
                int nextDist = connection[1];

                int totalDist = curDist + nextDist;     // 다음 위치까지 경로
                if (dist[to][k - 1] > totalDist) {
                    dist[to][k - 1] = totalDist;

                    // 삽입 정렬
                    for (int i = k - 1; i > 0; i--) {
                        if (dist[to][i] < dist[to][i - 1]) {
                            int tmp = dist[to][i];
                            dist[to][i] = dist[to][i - 1];
                            dist[to][i - 1] = tmp;
                        } else {
                            break;
                        }
                    }

                    pq.offer(new int[]{to, totalDist});
                }

            }
        }

        return dist;
    }
}
