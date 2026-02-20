package feb.week3.boj;

import java.util.*;
import java.io.*;

public class 에바쿰_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n;
    static int[] arr;
    static long[] tree;
    static long[] lazy;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        int q1 = Integer.parseInt(st.nextToken());
        int q2 = Integer.parseInt(st.nextToken());

        arr = new int[n+1];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=1; i<n+1; i++) arr[i] = Integer.parseInt(st.nextToken());

        tree = new long[4 * (n+1)];
        lazy = new long[4 * (n+1)];
        buildTree(1, 1, n);
        int total = q1 + q2;
        while(total-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());
            if(cmd == 1) {
                int s = Integer.parseInt(st.nextToken());
                int e = Integer.parseInt(st.nextToken());
                if (s > e) {
                    int tmp = s;
                    s = e;
                    e = tmp;
                }
                sb.append(query(1, 1, n, s, e)).append('\n');
            } else if(cmd == 2) {
                int s = Integer.parseInt(st.nextToken());
                int e = Integer.parseInt(st.nextToken());
                long v = Long.parseLong(st.nextToken());
                if (s > e) {
                    int tmp = s;
                    s = e;
                    e = tmp;
                }
                update(1, 1, n, s, e, v);
            }
        }
    }

    static long buildTree(int id, int l, int r) {
        if(l == r) return tree[id] = arr[l];
        int mid = l + (r - l)/2;
        return tree[id] = buildTree(2*id, l, mid) + buildTree(2*id+1, mid+1, r);
    }

    static void push(int id, int l, int r) {
        if(lazy[id] == 0 || l == r) return;

        long lazyV = lazy[id];
        int mid = l + (r - l)/2;

        tree[2*id] += ((mid-l+1) * lazyV);
        lazy[2*id] += lazyV;

        tree[2*id+1] += ((r-mid) * lazyV);
        lazy[2*id+1] += lazyV;

        lazy[id] = 0;
    }

    static long query(int id, int l, int r, int s, int e) {
        if(r < s || l > e) return 0L;
        if(l >= s && r <= e) return tree[id];
        push(id, l, r);
        int mid = l + (r - l)/2;
        return query(2*id, l, mid, s, e) + query(2*id+1, mid+1, r, s, e);
    }

    static void update(int id, int l, int r, int s, int e, long v) {
        if(r < s || l > e) return;
        if(l >= s && r <= e) {
            tree[id] += ((r - l + 1) * v);
            lazy[id] += v;
            return;
        }
        push(id, l, r);
        int mid = l + (r - l)/2;
        update(2*id, l, mid, s, e, v);
        update(2*id+1, mid+1, r, s, e, v);
        tree[id] = tree[2*id] + tree[2*id+1];
    }
}
