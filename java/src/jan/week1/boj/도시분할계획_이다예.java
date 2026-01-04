package jan.week1.boj;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Arrays;

// Kruskal 이용
// 1. 일단 간선을 오름차 정렬
// 2. MST이 완성 되면 ... MST이 됐다는건 간선이 (N-1)개가 됐다는거
// 3. 2개의 마을을 만들라면 제일 가중치가 큰 간선하나를 없애는거
public class 도시분할계획_이다예 {
    static BufferedReader br;
    static StringTokenizer st;

    // N = 집의 개수 / M = 길의 개수
    static int N, M;
    // 길 유지비를 저장한 2차원 배열
    static int[][] road;

    // 부모 노드 저장 배열
    static int[] parent;

    // 유니온
    static void union(int[] parent, int x, int y) {
        x = find(parent, x);
        y = find(parent, y);
        if (x < y) parent[y] = x;
        else parent[x] = y;
    }

    // 파인드
    static int find(int[] parent, int x) {
        if (parent[x] == x) return x;
        else return parent[x] = find(parent, parent[x]);
    }

    // 크루스칼
    static int kruskal(int[][] road, int[] parent, int M) {
        int cost = 0;
        int countM = 0;

        // 간선의 개수가 N-2 될 시 종료
        for (int i = 0; i < road.length; i++) {
            if (countM ==   N - 2) return cost;
            if ((find(parent, parent[road[i][0]]) != find(parent, parent[road[i][1]]))) {
                cost += road[i][2];
                union(parent, parent[road[i][0]], parent[road[i][1]]);
                countM++;
            }

        }
        return 0;
    }

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());

        road = new int[M][3];

        int result = 0;

        // 길 유지비 정보 저장
        for (int i=0; i < M ; i++){
            st= new StringTokenizer(br.readLine());
            int home1 = Integer.parseInt(st.nextToken());
            int home2 = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());

            road[i][0] = home1;
            road[i][1] = home2;
            road[i][2] = cost;

        }

        // 길 유지비 배열 정렬
        // (o1, o2) -> o1[3] - o2[3] 는 람다식
        // road 2차원배열에서 매개변수 (ex, 1행 / 2행) 두개를 비교해서 각 매개변수 배열의 3번째 값을
        // 비교하고 그 값이 음수면 o1이 앞으로 오고 (o1이 더작다는뜻) 아니면 o2가 뒤로
        Arrays.sort(road, (o1, o2) -> o1[2] - o2[2]);


        int[] parent = new int[N+1];

        // parent 배열 초기화
        for (int i = 1 ; i < parent.length ; i++ ){
            parent[i] = i ;
        }

        result = kruskal(road, parent, M);

        System.out.println(result);
    }
}
