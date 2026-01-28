package jan.week5.boj;

import java.util.*;
import java.io.*;

public class 이분그래프_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    /**
     * 각 집합에 속한 정점끼리는 서로 인접하지 않도록 분할한다.
     */
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        int tc = Integer.parseInt(br.readLine().trim());
        while(tc-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int v = Integer.parseInt(st.nextToken());
            int e = Integer.parseInt(st.nextToken());
            List<Integer>[] graph = new List[v+1];
            int[] groups = new int[v+1];
            for(int i=0; i<v+1; i++) graph[i] = new ArrayList<>();
            while(e-- > 0) {
                st = new StringTokenizer(br.readLine().trim());
                int from = Integer.parseInt(st.nextToken());
                int to = Integer.parseInt(st.nextToken());
                graph[from].add(to);
                graph[to].add(from);
            }
            boolean binaryGraph = true;
            for(int i=1; i<v+1; i++) {
                if(groups[i] != 0) continue;
                if(!checkAdjNode(i, graph, groups)) {
                    binaryGraph = false;
                    break;
                }
            }
            System.out.println(binaryGraph ? "YES" : "NO");
        }
    }

    static boolean checkAdjNode(int node, List<Integer>[] graph, int[] groups) {
        Queue<Integer> q = new ArrayDeque<>();
        groups[node] = 1;
        q.offer(node);

        while(!q.isEmpty()) {
            int cur = q.poll();

            for(int next : graph[cur]) {
                if(groups[next] == 0) {
                    groups[next] = -groups[cur];
                    q.offer(next);
                } else if(groups[next] == groups[cur]) return false;
            }
        }
        return true;
    }
}
