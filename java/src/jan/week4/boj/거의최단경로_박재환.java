package jan.week4.boj;

import java.util.*;
import java.io.*;

public class 거의최단경로_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            st = new StringTokenizer(br.readLine().trim());
            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());

            if (n == 0 && m == 0) break;   // 종료 조건

            init(br);
        }

        br.close();
    }

    static final int INF = 500 * 1000 + 1;

    static StringTokenizer st;
    static int n, m;        // 정점 수, 간선 수
    static int s, e;
    static List<int[]>[] graph;
    static int minDist;
    static void init(BufferedReader br) throws IOException {
        minDist = INF;

        graph = new List[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        st = new StringTokenizer(br.readLine().trim());
        s = Integer.parseInt(st.nextToken());
        e = Integer.parseInt(st.nextToken());

        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int dist = Integer.parseInt(st.nextToken());
            graph[from].add(new int[]{to, dist});
        }

        findAllMinDist();
        almostMinDist();

        System.out.println(minDist == INF ? -1 : minDist);
    }

    /**
     * 최단 경로를 찾는다.
     */
    static void findAllMinDist() {
        // 거리를 기준으로 오름차순 정렬
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        List<Integer>[] prev = new List[n+1];       // 최단 경로를 기록
        for(int i=0; i<n+1; i++) prev[i] = new ArrayList<>();
        int[] distArr = new int[n+1];
        Arrays.fill(distArr, INF);

        // 초기 위치 설정
        distArr[s] = 0;
        pq.offer(new int[] {s, 0});

        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int from = cur[0];
            int accDist = cur[1];

            if(distArr[from] < accDist) continue;

            for(int[] connNode : graph[from]) {
                int to = connNode[0];
                int connDist = connNode[1];

                if(distArr[to] > accDist + connDist) {
                    distArr[to] = accDist + connDist;
                    // 최단 경로 기록
                    prev[to].clear();       // 이전 경로 초기화
                    prev[to].add(from);
                    pq.offer(new int[] {to, distArr[to]});
                } else if(distArr[to] == accDist + connDist) {  // 다른 최단 경로
                    prev[to].add(from);
                }
            }
        }

        excludeMinDist(prev);
    }

    static boolean[][] blocked;      // from -> to 간선을 제거
    static void excludeMinDist(List<Integer>[] prev) {
        blocked = new boolean[n+1][n+1];

        Queue<Integer> q = new ArrayDeque<>();
        boolean[] visited = new boolean[n+1];

        q.offer(e);
        visited[e] = true;

        while(!q.isEmpty()) {
            int to = q.poll();

            for(int from : prev[to]) {
                blocked[from][to] = true;

                if(visited[from]) continue;
                q.offer(from);
                visited[from] = true;
            }
        }
    }

    static void almostMinDist() {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        int[] distArr = new int[n+1];
        Arrays.fill(distArr, INF);

        // 초기 위치 설정
        distArr[s] = 0;
        pq.offer(new int[] {s, 0});

        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int from = cur[0];
            int accDist = cur[1];

            if(distArr[from] < accDist) continue;

            for(int[] connNode : graph[from]) {
                int to = connNode[0];
                int connDist = connNode[1];
                if(blocked[from][to]) continue;
                if(distArr[to] > accDist + connDist) {
                    distArr[to] = accDist + connDist;
                    pq.offer(new int[] {to, distArr[to]});
                }
            }
        }

        minDist = distArr[e];
    }
}
