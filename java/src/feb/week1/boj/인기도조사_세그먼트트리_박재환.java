package feb.week1.boj;

import java.util.*;
import java.io.*;

public class 인기도조사_세그먼트트리_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static final int SECOND = 1;
    static final int MINUTE = 60 * SECOND;
    static final int HOUR = 60 * MINUTE;
    static final int DAY = 24 * HOUR;

    static long[] tree;
    static long[] lazy;

    static StringTokenizer st;
    static void init() throws IOException {
        tree = new long[DAY * 4];        // 세그먼트 트리
        lazy = new long[DAY * 4];        // 지연 기록

        int inputCount = Integer.parseInt(br.readLine().trim());
        while (inputCount-- > 0) {
            st = new StringTokenizer(br.readLine(), " :-");
            int s = convertToSecond();
            int e = convertToSecond();

            if (s <= e) {
                update(1, 0, DAY - 1, s, e, 1);
            } else {        // 자정을 넘기는 경우
                update(1, 0, DAY - 1, s, DAY - 1, 1);
                update(1, 0, DAY - 1, 0, e, 1);
            }
        }
        int outputCount = Integer.parseInt(br.readLine().trim());
        while (outputCount-- > 0) {
            st = new StringTokenizer(br.readLine(), " :-");
            int s = convertToSecond();
            int e = convertToSecond();

            long sum;
            int len;

            if (s <= e) {
                sum = query(1, 0, DAY - 1, s, e);
                len = e - s + 1;
            } else {        // 자정을 넘기는 경우
                sum = query(1, 0, DAY - 1, s, DAY - 1)
                        + query(1, 0, DAY - 1, 0, e);
                len = (DAY - s) + (e + 1);      // DAY-1 - s + 1 => DAY - s, e - 0 + 1 => e + 1
            }
            sb.append(String.format("%.10f", (double) sum / len)).append('\n');
        }
    }
    static void update(int id, int l, int r, int s, int e, long targetValue) {
        if(r < s || l > e) return;  // 범위 벗어남
        if(l >= s && r <= e) {      // 완전하게 포함
            // LAZY 기록 후 종료
            tree[id] += targetValue * (r-l+1);      // 자식의 개수만큼 증가
            lazy[id] += targetValue;                // 자식에게 넘길 값
            return;
        }
        // 일부만 포함 -> LAZY 전파 후 이어서 탐색
        push(id, l, r);
        int mid = l + (r-l)/2;
        update(id*2, l, mid, s, e, targetValue);
        update(id*2+1, mid+1, r, s, e, targetValue);
        tree[id] = tree[id*2] + tree[id*2+1];
    }
    static long query(int id, int l, int r, int s, int e) {
        if(r < s || l > e) return 0L;               // 범위 벗어남
        if(l >= s && r <= e) return tree[id];      // 완전하게 포함

        push(id, l, r);
        int mid = l + (r-l)/2;
        return query(id*2, l, mid, s, e) + query(id*2+1, mid+1, r, s, e);
    }
    static void push(int id, int l, int r) {
        if(lazy[id] == 0 || l == r) return;     // 전파할 값이 없거나, 리프노드인 경우
        int mid = l + (r-l)/2;
        long lazyValue = lazy[id];

        // 왼쪽 자식 전파
        tree[id*2] += lazyValue * (mid-l+1);
        lazy[id*2] += lazyValue;
        // 오른쪽 자식 전파
        tree[id*2+1] += lazyValue * (r-mid);    // r - (mid+1) + 1 => r - mid
        lazy[id*2+1] += lazyValue;

        // 전파 완료
        lazy[id] = 0;
    }
    static int convertToSecond() {
        int h = Integer.parseInt(st.nextToken()) * HOUR;
        int m = Integer.parseInt(st.nextToken()) * MINUTE;
        int s = Integer.parseInt(st.nextToken()) * SECOND;
        return h+m+s;
    }
}
