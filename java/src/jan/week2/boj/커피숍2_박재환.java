package jan.week2.boj;

import java.util.*;
import java.io.*;

public class 커피숍2_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }

    /**+
     *  연속으로 구간합을 구해야한다.
     *  - DP로 모든 구간 합을 저장하는 방법
     *  - 세그먼트 트리
     *
     *  단, 문제에서 'N번재 수를 고쳐야한다' 는 조건이 있다.
     *  -> 반복적인 중간 값 교체가 일어난다.
     *
     *  => Segment Tree or Lazy Propagation Segment Tree
     *
     *  => 조회, 교체가 한 번씩 일어남 => 그냥 Segment Tree 로 가능
     */
    static StringTokenizer st;
    static int n, q;
    static int[] arr;
    static long[] tree;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 정수의 개수
        q = Integer.parseInt(st.nextToken());       // 턴의 수
        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());

        // 1. Segment Tree 생성
        tree = new long[4*n];
        makeTree(0, n-1, 1);

        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            // Note : x > y 로 주어지는 경우, y~x 번째이다.
            int l = Integer.parseInt(st.nextToken())-1;
            int r = Integer.parseInt(st.nextToken())-1;
            if(r < l) {
                int tmp = r;
                r = l;
                l = tmp;
            }
            sb.append(query(0, n-1, 1, l, r)).append('\n');
            int targetId = Integer.parseInt(st.nextToken())-1;
            int targetValue = Integer.parseInt(st.nextToken());
            update(0, n-1, 1, targetId, targetValue);
            arr[targetId] = targetValue;
        }
    }

    static long makeTree(int s, int e, int id) {
        if(s == e) return tree[id] = arr[s];        // 리프 노드에 도달한 경우
        // 하위 노드부터 탐색해서 바텀업으로 값을 채움
        int mid = s + (e-s)/2;
        return tree[id] = makeTree(s, mid, 2*id) + makeTree(mid+1, e, 2*id+1);
    }

    static Long query(int s, int e, int id, int l, int r) {
        if(s > r || e < l) return 0L;           // query의 범위를 완전하게 벗어나는 경우

        if(s >= l && e <= r) return tree[id];   // query 가 범위를 완전하게 포함하는 경우
        // 더 나눠서 탐색해야하는 경우
        int mid = s + (e-s)/2;
        return query(s, mid, 2*id, l, r) + query(mid+1, e, 2*id+1, l, r);
    }

    static void update(int s, int e, int id, int targetId, int targetValue) {
        if(targetId < s || targetId > e) return;    // update 범위를 완전히 벗어나는 경우

        // 값을 교체하는 것
        tree[id] = tree[id] + targetValue - arr[targetId];
        if(s == e) return;
        int mid = s + (e-s)/2;
        update(s, mid, 2*id, targetId, targetValue);
        update(mid+1, e, 2*id+1, targetId, targetValue);
    }
}
