package feb.week3.boj;

import java.util.*;
import java.io.*;

public class 용액_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n;
    static int[] arr;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());
        /**
         * 정수들이 오름차순으로 주어진다.
         * [작은 값 -> 큰 값]
         *
         * [투포인터]
         * l = 0, r = n-1
         * arr[l] + arr[r] == 0 -> 최적값 (탐색할 필요 X)
         * arr[l] + arr[r] < 0 -> 음수 (작은 값을 좀 더 올려야함 -> l++)
         * arr[l] + arr[r] > 0 -> 양수 (큰 값을 좀 더 내려야함 -> r--)
         */
        int l = 0, r = n-1;
        int sum = Integer.MAX_VALUE;
        int lBest = -1, rBest = -1;
        while(l < r) {      // l 과 r 이 같아질 수 없음
            if(arr[l] + arr[r] == 0) {
                sum = 0;
                lBest = arr[l];
                rBest = arr[r];
                break;
            }

            int tempSum = arr[l] + arr[r];
            if(tempSum > 0) {
                if(Math.abs(tempSum) < Math.abs(sum)) {
                    sum = tempSum;
                    lBest = arr[l];
                    rBest = arr[r];
                }
                r--;
            }else {
                if(Math.abs(tempSum) < Math.abs(sum)) {
                    sum = tempSum;
                    lBest = arr[l];
                    rBest = arr[r];
                }
                l++;
            }
        }
        System.out.println(lBest + " " + rBest);
    }
}
