package jan.week1.boj;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class 줄세우기_이다예 {
    static BufferedReader br;
    static StringTokenizer st;
    // 아이들의 수
    static int N;
    // 아이들 줄 배치 전
    static int[] line;
    static int[] dp;

    // 증가 부분수열 구하기
    // 증가 부분수열에 속하는 아이들은 자리를 안옮겨도되니
    // (N - 증가 부분수열 최대 길이 값) 이 답이 됨
    // 증가 부분수열에 속한 애들 제외한 아이들이 자리를 옮기면 되니까
    public static void main(String[] args) throws IOException{
        br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        line = new int[N];

        for (int i = 0 ; i < N ; i++){
            int kidNumber = Integer.parseInt(br.readLine());
            line[i] = kidNumber;
        }

        int[] dp = new int[N];

        // dp 최소값 1 ( 본인 포함)
        for (int i = 0; i < dp.length; i++){
            dp[i] = 1;
        }

        for (int i= 1 ; i < dp.length; i++){
            for (int j = 0; j < i ; j++){
                if (line[j] < line[i] && dp[i] < 1 + dp[j]){
                    dp[i] = dp[j] + 1;
                }
            }
        }

        int result = 0 ;
        for (int i = 0; i < dp.length ; i++){
            if (result < dp[i]){
                result = dp[i];
            }
        }

        System.out.println(N - result);
    }
}
