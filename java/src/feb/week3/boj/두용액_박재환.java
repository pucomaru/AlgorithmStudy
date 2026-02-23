package feb.week3.boj;

import java.util.*;
import java.io.*;

public class 두용액_박재환 {
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
        Result result = findNearestNum();
        System.out.println(result.a + " " + result.b);
    }
    static class Result {
        int a, b;
        int sum;
        Result(int a, int b, int sum) {
            this.a = a;
            this.b = b;
            this.sum = sum;
        }
    }
    static Result findNearestNum() {
        Result result = null;
        Arrays.sort(arr);           // 오름차순 정렬
        int l=0, r=n-1;
        while(l<r) {
            int a = arr[l];
            int b = arr[r];
            int sum = Math.abs(a+b);
            if(result == null || result.sum > sum) {
                result = new Result(a, b, sum);
            }
            if(sum == 0) break;         // 더 이상 최적값을 찾을 수 없음
            // 포인터 위치 변경
            if(a+b > 0) r--;            // 두 수의 합이 양수 -> 큰 값을 줄여봄
            else l++;                   // 두 수의 합이 음수 -> 작은 값을 줄여봄
        }
        return result;
    }
}
