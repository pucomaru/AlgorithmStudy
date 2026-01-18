package jan.week2.boj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;

// 인접행렬 ,인접리스트 차이 알기
public class 트리와쿼리_이다예 {

    static BufferedReader br;
    static StringTokenizer st;

    // N : 트리의 정점의 수 / R : 루트의 번호 / Q : 쿼리의 수
    static int N, R, Q;
    // 그래프
    static ArrayList<Integer>[] graph;

    // 서브트리 배열 (본인 포함 자식 노드까지 몇개있는지?)
    static int[] subtree;
    // 노드 방문했는지 배열
    static boolean[] visited;
    public static void main (String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());

        // 트리의 정점의 수
        N = Integer.parseInt(st.nextToken());
        // 루트 번호
        R = Integer.parseInt(st.nextToken());
        // 쿼리의 수
        Q = Integer.parseInt(st.nextToken());

        // 트리 = 사이클이 없는 그래프
        // 그래프 정보
        graph = new ArrayList[N+1];

        for(int i=1; i < N+1 ; i++){
            graph[i] = new ArrayList<>();
        }

        subtree = new int[N+1];
        visited = new boolean[N+1];

        // 그래프 정보 저장
        for (int i = 0 ; i < N - 1 ; i++){
            st = new StringTokenizer(br.readLine());
            int num1 = Integer.parseInt(st.nextToken());
            int num2 = Integer.parseInt(st.nextToken());

            graph[num1].add(num2);
            graph[num2].add(num1);
        }

        dfs(R);

        for (int i = 0; i < Q; i++){
            int queryNum = Integer.parseInt(br.readLine());
            System.out.println(subtree[queryNum]);
        }
    }

    static void dfs(int now){

        // 서브 트리 계산할 때 일단 본인 포함은 +1해야하니까
        subtree[now] = 1;
        visited[now] = true;

        for (int next : graph[now]){
            if (!visited[next]){
                dfs(next);
                subtree[now] += subtree[next];
            }
        }
    }

}