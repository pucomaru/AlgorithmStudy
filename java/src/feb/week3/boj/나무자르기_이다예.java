package feb.week3.boj;

import java.io.*;
import java.util.*;

public class 나무자르기_이다예 {
    static BufferedReader br;
    static StringTokenizer st;
    static int N;
    static long M;
    static int[] trees;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        M = Long.parseLong(st.nextToken());

        trees = new int[N];

        st = new StringTokenizer(br.readLine());

        int maxHeight = 0;
        for (int i = 0; i < N; i++) {
            int h = Integer.parseInt(st.nextToken());
            trees[i] = h;
            if (h > maxHeight) maxHeight = h;
        }

        int start = 0;
        int end = maxHeight;
        int ans = 0;

        while (start <= end) {
            int mid = (end + start) / 2;

            long sum = 0;
            for (int h : trees) {
                if (h > mid) sum += (h - mid);
            }

            if (sum >= M) {
                ans = mid;
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }

        System.out.println(ans);
    }
}