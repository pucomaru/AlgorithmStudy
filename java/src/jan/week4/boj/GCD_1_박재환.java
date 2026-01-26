package jan.week4.boj;

import java.util.*;
import java.io.*;

public class GCD_1_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    static void init(BufferedReader br) throws IOException {
        long n = Long.parseLong(br.readLine().trim());       // 최대 10**12

        long result = n;
        for(long i=2; i*i <= n; i++) {
            if (n % i == 0) {
                while (n % i == 0) {         // 소인수분해
                    n /= i;
                }
                result = result / i * (i - 1);
            }
        }

        if(n>1) result = result / n * (n-1);

        System.out.println(result);
    }
}
