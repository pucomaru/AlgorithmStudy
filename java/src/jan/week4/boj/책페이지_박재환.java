package jan.week4.boj;

import java.util.*;
import java.io.*;

public class 책페이지_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static void init(BufferedReader br) throws IOException {
        long endPage = Long.parseLong(br.readLine().trim());
        long[] count = new long[10];        // 최대 자릿수 10 ** 9
        for(long i=1; i<=endPage; i*=10) {  // 자릿수 단위로 탐색
            /**
             * endPage = 3456
             * ---
             * i = 1
             * high = 345
             * cur = 6
             * low = 0
             * ---
             * i = 10
             * high = 34
             * cur = 5
             * low = 6
             * ---
             * ...
             */
            long high = endPage / (i*10);   // 현재 자리보다 큰 자리
            long cur = (endPage / i) % 10;  // 현재 자리
            long low = (endPage % i);       // 현재 자리보다 작은 자리

            for(int j=1; j<10; j++) {
                count[j] += high * i;
                if(cur > j) count[j] += i;
                else if(cur == j) count[j] += (low + 1);
            }

            if(high > 0) {
                count[0] += (high-1) * i;
                if (cur > 0) count[0] += i;
                else if (cur == 0) count[0] += low + 1;
            }
        }

        StringBuilder sb = new StringBuilder();
        for(long l : count) sb.append(l).append(' ');
        System.out.println(sb);
    }
}
