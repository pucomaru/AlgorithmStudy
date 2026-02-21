package feb.week3.boj;

import java.util.*;
import java.io.*;

public class 타임머신_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n;
    static List<int[]>[] connections;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        connections = new List[n+1];
        for(int i=0; i<n+1; i++) connections[i] = new ArrayList<>();

        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());
            connections[from].add(new int[] {to, cost});
        }

        // 출발 위치
        long[] dist = new long[n+1];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[1] = 0;

        for(int i=1; i<n; i++) {              // 최단 거리는 최대 i 번의 간선만 사용해서 구해야함 -> 모든 노드가 연결되어있다고 가정했을 때 최대 n-1개의 간선 존재
            for(int from=1; from<n+1; from++) {
                if(dist[from] == Long.MAX_VALUE) continue;

                for(int[] connection : connections[from]) {
                    int to = connection[0];
                    int cost = connection[1];

                    if(dist[to] > cost + dist[from]) {
                        dist[to] = cost + dist[from];
                    }
                }
            }
        }

        for(int from=1; from<n+1; from++) {         // 이미 위에서 최단 거리를 구했는데, 또 최단거리가 생긴다면 -> 사이클 존재
            if(dist[from] == Long.MAX_VALUE) continue;

            for(int[] connection : connections[from]) {
                int to = connection[0];
                int cost = connection[1];

                if(dist[to] > cost + dist[from]) {
                    System.out.println(-1);
                    return;
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        for(int i=2; i<n+1; i++) {
            if(dist[i] == Long.MAX_VALUE) {
                sb.append(-1);
            }else sb.append(dist[i]);
            sb.append('\n');
        }
        System.out.println(sb);
    }
}
