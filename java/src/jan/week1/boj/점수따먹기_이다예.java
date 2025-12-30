package jan.week1.boj;

// 백준 1749 점수 따먹기

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;
public class 점수따먹기_이다예 {

    static int N, M;
    static int[][] matrix;
    static BufferedReader br;
    static StringTokenizer st;
    static int maxSum = Integer.MIN_VALUE;

    static void kadane(int top) {
        int[] colSum = new int[M];

        for (int bottom = top; bottom < N; bottom++) {

            for (int i = 0; i < M; i++) {
                colSum[i] += matrix[bottom][i];
            }

            int cur = colSum[0];
            int best = colSum[0];

            for (int i = 1; i < M; i++) {
                cur = Math.max(colSum[i], cur + colSum[i]);
                best = Math.max(best, cur);
            }
            maxSum = Math.max(maxSum, best);

        }

    }
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());

        // 행
        N = Integer.parseInt(st.nextToken());
        // 열
        M = Integer.parseInt(st.nextToken());

        // 행렬
        matrix = new int[N][M];

        // 2차원 배열에 숫자들 넣기
        for(int i=0; i < N ; i++){
            st = new StringTokenizer(br.readLine());
            for (int j=0; j < M ; j++){
                matrix[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        for (int top= 0; top < N ; top++){
            kadane(top);
        }

        System.out.println(maxSum);
    }
}
