package jan.week1.boj;

import java.util.*;
import java.io.*;

public class 부분합_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    static StringTokenizer st;
    static int n, s;
    static int[] arr;
    static int minLen;
    static void init() throws IOException {
        minLen = 100_001;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        s = Integer.parseInt(st.nextToken());

        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());
        findMinLen();
        System.out.println(minLen == 100_001 ? 0 : minLen);
    }

    /**
     * 투포인터 사용
     * => 초기시작 위치를 모두 0으로 초기화
     *
     * l++ => 구간합에서 값을 빼줌
     * r++ => 구간합에 값을 더함
     */
    static void findMinLen() {
        int l=0, r=0, sum=0;

        while(r < n) {
            sum += arr[r];

            while(sum >= s) {
                sum -= arr[l];
                minLen = Math.min(minLen, (r-l)+1);
                l++;
            }
            r++;
        }
    }
}
