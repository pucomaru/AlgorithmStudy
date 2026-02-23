package feb.week4.boj;

import java.util.*;
import java.io.*;

public class 소트게임_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, k;
    static int[] arr;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());
        System.out.println(findMinCost());
    }
    static int findMinCost() {
        Queue<String> q = new ArrayDeque<>();
        Map<String, Integer> map = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        for(int i : arr) sb.append(i);

        String initArr = sb.toString();
        q.offer(initArr);
        map.put(initArr, 0);

        sb.setLength(0);        // StringBuilder 초기화
        for(int i=1; i<n+1; i++) sb.append(i);
        String target = sb.toString();      // 최종 목표

        while(!q.isEmpty()) {
            String cur = q.poll();
            int cnt = map.get(cur);
            if(cur.equals(target)) {
                return cnt;
            }

            for(int i=0; i<=n-k; i++) {     // 완탐으로 뒤집기
                String next = reverse(cur, i, i+k-1);

                if(map.get(next) == null) {
                    map.put(next, cnt+1);
                    q.offer(next);
                }
            }
        }
        return -1;
    }

    static String reverse(String str, int l, int r) {
         StringBuilder sb = new StringBuilder();
         sb.append(str.substring(0, l));
         sb.append(new StringBuilder(str.substring(l, r+1)).reverse());
         sb.append(str.substring(r+1));
         return sb.toString();
    }

}
