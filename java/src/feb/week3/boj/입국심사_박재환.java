package feb.week3.boj;

import java.util.*;
import java.io.*;

public class 입국심사_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n;
    static long m;
    static long[] arr;
    static long maxTime;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Long.parseLong(st.nextToken());

        maxTime = 0;
        arr = new long[n];
        for(int i=0; i<n; i++) {
            arr[i] = Long.parseLong(br.readLine().trim());
            maxTime = Math.max(maxTime, arr[i]);
        }

        System.out.println(findMinTime());
    }

    static long findMinTime() {
        long l = 1, r = maxTime * m;

        while(l < r) {
            long mid = l + (r-l)/2;
            long ableCnt = 0;
            for(long i : arr) {
                ableCnt += (mid/i);
                if(ableCnt >= m) break;
            }

            // 만약 mid 시간 안에 처리가 가능하다면, 더 줄여본다.
            if(ableCnt >= m) r = mid;
            else l = mid+1;
        }
        return l;
    }
}
