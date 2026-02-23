package feb.week3.swea;

import java.util.*;
import java.io.*;

public class 입국심사_박재환 {
    /**
     * 1 ~ N번의 입국심사대
     * k 번 입국심사대에서 걸리는 시간 t[k]
     * -> 심사 끝나면 지연없이 다음사람 심사
     *
     * 초기에는 모두 비어있음
     * 모두 심사를 받기위해 걸리는 최소 시간
     *
     * 동시간에 빠지고, 심사 시작 가능
     */
     static BufferedReader br;
     static StringBuilder sb;
     public static void main(String[] args) throws IOException {
         br = new BufferedReader(new InputStreamReader(System.in));
         sb = new StringBuilder();
         int tc = Integer.parseInt(br.readLine().trim());
         for(int i=0; i<tc; i++) {
             sb.append('#').append(i+1).append(' ').append(init()).append('\n');
         }
         br.close();
         System.out.println(sb);
     }
     static StringTokenizer st;
     static int n, m;
     static int[] tables;
     static int maxTime;
     static long init() throws IOException {
         maxTime = 0;

         st = new StringTokenizer(br.readLine().trim());
         n = Integer.parseInt(st.nextToken());          // 입국심사대 수
         m = Integer.parseInt(st.nextToken());          // 대기 인원 수
         tables = new int[n];
         for(int i=0; i<n; i++) {
             tables[i] = Integer.parseInt(br.readLine().trim());
             maxTime = Math.max(maxTime, tables[i]);
         }
         return solution();
     }
     static long solution() {
         /**
          * M : 10 ** 9
          * table[k] : 10 ** 9
          * => 입력 범위가 너무 큼
          *
          * [문제]
          * 모든 인원이 심사를 받기위해 걸리는 최소 시간을 구해라
          * => 시간 T 안에 모든 인원 처리가 가능한가? => Yes / No
          * => 시간 T 가 커질 수록 가능성이 증가/감소 한다. => 단조
          */
         // 이분탐색을 이용해 T 를 구한다.
         // 각 심사대가 T 시간에 처리할 수 있는 인원 T/table[k]
         long l = 0, r = (long)maxTime * m;
         long totalTime = r;
         while(l <= r) {
             long mid = l + (r-l)/2;
             int sum = 0;
             for(int i : tables) {
                 sum += (mid / i);
                 if(sum >= m) break;        // 가지치기
             }
             if(sum >= m) {     // 해당 시간안에 처리할 수 있다면 -> 시간을 줄여본다.
                 r = mid - 1;
                 totalTime = Math.min(totalTime, mid);
             } else {
                 l = mid + 1;
             }
         }
         return totalTime;
     }
}
