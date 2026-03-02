package feb.week4.boj;

import java.util.*;
import java.io.*;

public class 웜홀_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static class Edge {
        int to, cost;
        Edge(int to, int cost) {
            this.to = to;
            this.cost = cost;
        }
    }
    static StringTokenizer st;
    static int n, m, w;
    static List<Edge>[] roads;
    static void init() throws IOException {
        int tc = Integer.parseInt(br.readLine().trim());
        while(tc-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            n = Integer.parseInt(st.nextToken());       // 지점 수
            m = Integer.parseInt(st.nextToken());       // 도로 수
            w = Integer.parseInt(st.nextToken());       // 웜홀 수

            roads = new List[n+1];
            for(int i=1; i<n+1; i++) roads[i] = new ArrayList<>();
            for(int i=0; i<m; i++) {        // 도로
                st = new StringTokenizer(br.readLine().trim());
                int from = Integer.parseInt(st.nextToken());
                int to = Integer.parseInt(st.nextToken());
                int cost = Integer.parseInt(st.nextToken());
                roads[from].add(new Edge(to, cost));
                roads[to].add(new Edge(from, cost));
            }
            for(int i=0; i<w; i++) {        // 웜홀
                st = new StringTokenizer(br.readLine().trim());
                int from = Integer.parseInt(st.nextToken());
                int to = Integer.parseInt(st.nextToken());
                int cost = Integer.parseInt(st.nextToken());
                roads[from].add(new Edge(to, -cost));
            }
            sb.append(getAllCost() ? "YES" : "NO").append('\n');
        }
    }
    static boolean getAllCost() {
        int[] dist = new int[n+1];
        /**
         * 별다른 초기화를 하지 않음
         * [0,0,..0] => 모든 위치에서 출발
         */
        for(int i=1; i<n; i++) {
            for(int from=1; from<n+1 ;from++) {
                for(Edge edge : roads[from]) {
                    if(dist[edge.to] > dist[from] + edge.cost) dist[edge.to] = dist[from] + edge.cost;
                }
            }
        }
        for(int from=1; from<n+1 ;from++) {
            for(Edge edge : roads[from]) {
                if(dist[edge.to] > dist[from] + edge.cost) return true;
            }
        }
        return false;
    }
}
