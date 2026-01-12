package jan.week2.boj;

import java.util.*;
import java.io.*;

public class 트리와쿼리_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n, r, q;
    static List<Integer>[] graph;
    static int[] nodes;
    static void init() throws IOException {
        /**
         * 정점의 수가 최대 100,000
         * => n^2 으로는 불가능
         *
         * => O(nlog n), O(n), O(log n) 접근 방법 사용
         */
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 트리 정점의 수
        nodes = new int[n+1];
        graph = new List[n+1];
        Arrays.fill(nodes, 1);
        for(int i=0; i<n+1; i++) graph[i] = new ArrayList<>();
        r = Integer.parseInt(st.nextToken());       // 루트의 번호
        q = Integer.parseInt(st.nextToken());       // 쿼리의 수
        for(int i=0; i<n-1; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            graph[a].add(b);
            graph[b].add(a);
        }

        getSubTreeCount(0, r);

        while(q-- > 0) {
            int node = Integer.parseInt(br.readLine().trim());
            sb.append(nodes[node]).append('\n');
        }
    }

    static void getSubTreeCount(int prev, int cur) {    // 이전 노드, 현재 노드
        /**
         * 이전 노드를 통해, 사이클을 판별한다.
         */
        for(int connNode : graph[cur]) {
            if(prev == connNode) continue;      // 이전 노드로 되돌아 가는 경우 (사이클 방지)

            getSubTreeCount(cur, connNode);     // 다음 노드 탐색
            nodes[cur] += nodes[connNode];      // 다음 노드의 탐색을 마치면, 이전 합과 더함
        }

    }
}
