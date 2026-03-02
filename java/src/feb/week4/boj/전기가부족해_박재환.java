package feb.week4.boj;

import java.util.*;
import java.io.*;

public class 전기가부족해_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m, k;
    static int[] generates;     // 발전소
    static PriorityQueue<int[]> pq;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        generates = new int[k];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<k; i++) generates[i] = Integer.parseInt(st.nextToken());

        pq = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));
        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());
            /**
             * MST(Kruskal)
             * - 간선의 집합만 필요
             * - 무방향 그래프이지만, 간선의 정보만 있으면 됨
             */
            int[] connection = new int[] {from, to, cost};
            pq.offer(connection);
        }
        System.out.print(findMinCost());
    }
    static int findMinCost() {
        make();
        /**
         * 발전소끼리는 먼저 연결
         * - 첫번째 발전소에 다른 발전소를 미리 모두 연결
         */
        for(int i=1; i<k; i++) {
            union(generates[0], generates[i]);
        }
        int totalCost = 0, edge = 0;

        while(!pq.isEmpty()) {
            int[] connection = pq.poll();
            int from = connection[0];
            int to = connection[1];
            int cost = connection[2];

            if(union(from, to)) {
                totalCost += cost;
                if(++edge == (n-k)) break;
            }
        }
        return totalCost;
    }
    static int[] parents;
    static void make() {
        parents = new int[n+1];
        for(int i=0; i<n+1; i++) parents[i] = i;
    }
    static int find(int a) {
        if(a == parents[a]) return a;
        return parents[a] = find(parents[a]);
    }
    static boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if(rootA == rootB) return false;

        parents[rootA] = rootB;
        return true;
    }
}
