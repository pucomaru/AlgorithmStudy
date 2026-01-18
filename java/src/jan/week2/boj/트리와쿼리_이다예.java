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

        // 그래프 정보 저장
        for (int i = 0 ; i < N - 1 ; i++){
            st = new StringTokenizer(br.readLine());
            int num1 = Integer.parseInt(st.nextToken());
            int num2 = Integer.parseInt(st.nextToken());

            ///
            graph[i]
        }
    }


}