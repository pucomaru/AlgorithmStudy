package jan.week4.boj;

import java.util.*;
import java.io.*;

public class StronglyConnectedComponent {
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init(br);
        br.close();
    }

    /**
     * SCC 는 정점의 최대 부분집합이다.
     * -> 서로 다른 임의의 두 점 u, v 에 대해서 서로 이동할 수 있는 경로가 모두 존재한다.
     *
     * 각 정점에서의 사이클을 찾는다.v ❌
     *
     * [타잔 알고리즘]
     * - 방문하지 않은 정점에 대해 방문 순서에 따라 id 를 매긴다.
     * - 정점을 스택에 삽입한다.
     * - 정점과 연결된 모든 정점을 비교해 부모 정점을 찾는다.
     * - 그 정점이 자기 자신이면 스택에서 자기 자신을 찾을 때까지 빼서 SCC 를 구한다.
     */
    static List<Integer>[] graph;
    static List<int[]> results;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        results = new ArrayList<>();

        st = new StringTokenizer(br.readLine().trim());
        int v = Integer.parseInt(st.nextToken());       // 정점의 수
        int e = Integer.parseInt(st.nextToken());       // 간선의 수
        graph = new List[v+1];
        for(int i=0; i<v+1; i++) graph[i] = new ArrayList<>();

        while(e-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            graph[from].add(to);
        }

        tarjan(v);
        printAnswer();
    }

    static int idx;       // 탐색 정점마다 고유 id 부여
    static ArrayDeque<Integer> stack;
    static int[] idxArr;       // 방문 순서
    static int[] low;       // 되돌아 갈 수 있는 가장 작은 번호
    static boolean[] onStack;
    static void tarjan(int v) {
        idx = 0;
        stack = new ArrayDeque<>();
        idxArr = new int[v+1];
        low = new int[v+1];
        onStack = new boolean[v+1];

        for(int node=1; node<v+1; node++) {
            if(idxArr[node] != 0) continue;
            dfs(node);
        }
    }

    static void dfs(int node) {
        // 첫 방문 시, 고유 번호로, low 를 세팅
        idxArr[node] = low[node] = ++idx;
        stack.offerLast(node);
        onStack[node] = true;

        for(int next : graph[node]) {
            if (idxArr[next] == 0) {        // 아직 방문하지 않은 노드
                dfs(next);
                // 자식이 어디까지 올라갈 수 있는지
                low[node] = Math.min(low[node], low[next]);
            } else if(onStack[next]) {
                low[node] = Math.min(low[node], idxArr[next]);
            }
        }

        if(low[node] == idxArr[node]) {
            List<Integer> scc = new ArrayList<>();
            while(true) {
                int v = stack.pollLast();
                onStack[v] = false;
                scc.add(v);
                if(v == node) break;
            }
            listToArr(scc);
        }
    }

    static void listToArr(List<Integer> list) {
        Collections.sort(list);     // 오름차순 정렬
        int[] arr = new int[list.size()];
        for(int i=0; i<arr.length; i++) arr[i] = list.get(i);

        results.add(arr);
    }

    static void printAnswer() {
        Collections.sort(results, (a, b) -> Integer.compare(a[0], b[0]));
        sb.append(results.size()).append('\n');
        for(int[] arr : results) {
            for(int i : arr) sb.append(i).append(' ');
            sb.append(-1).append('\n');
        }
        System.out.println(sb);
    }
}
