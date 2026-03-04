package mar.week1.boj;

import java.io.*;
import java.util.*;

public class 일로더하기_이다예 {

    static BufferedReader br;
    static int T;
    static int N;
    static int result;

    public static void main(String[] args) throws IOException {

        br = new BufferedReader(new InputStreamReader(System.in));
        T = Integer.parseInt(br.readLine());

        // 테스트케이스 T만큼 돔
        for (int i = 0 ; i < T; i++){

            // 합이 n이 되는 케이스 몇개?
            N = Integer.parseInt(br.readLine());

            result = 0;

            findMethod(0);

            System.out.println(result);
        }

    }

    static void findMethod(int sum){

        if (sum > N) return;

        for(int i = 1 ; i < 4 ; i++){
            sum += i;
            // 합이 N이 됐으면 더 들어갈 필요가 없으니 return
            if(sum == N) {
                result++;
            }
            else {
                findMethod(sum);
                sum -= i;
            }
        }
    }
}
