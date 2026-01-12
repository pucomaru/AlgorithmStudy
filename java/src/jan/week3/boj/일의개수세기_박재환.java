package jan.week3.boj;

import java.util.*;
import java.io.*;

public class 일의개수세기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static long a, b;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        a = Long.parseLong(st.nextToken());
        b = Long.parseLong(st.nextToken());

        long result = getOneBitsCount(b) - getOneBitsCount(a-1);
        System.out.println(result);
    }

    static long getOneBitsCount(long num) {
        long sum = 0L;
        for(int k=0; k<=60; k++) {
            sum += countOneBits(num, k);
        }
        return sum;
    }

    static long countOneBits(long num, int k) {
        long half = 1L << k;
        long block = 1L << (k+1);

        long total = num + 1;
        long full = total / block;
        long rest = total % block;

        long ones = full * half;
        ones += (Math.max(0L, rest - half));
        return ones;
    }

}
