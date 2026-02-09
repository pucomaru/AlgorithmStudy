package feb.week1.boj;

import java.util.*;
import java.io.*;

public class 수열과쿼리21_박재환 {
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
        n = Integer.parseInt(br.readLine().trim());
        arr = new int[n];
        tree = new long[4*n];
        lazy = new long[4*n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());
        initTree(1, 0, n-1);
        int turn = Integer.parseInt(br.readLine().trim());
        while(turn-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int command = Integer.parseInt(st.nextToken());
            switch(command) {
                case 1: {       // 구간 변경
                    int s = Integer.parseInt(st.nextToken())-1;
                    int e = Integer.parseInt(st.nextToken())-1;
                    int v = Integer.parseInt(st.nextToken());
                    update(1, 0, n-1, s, e, v);
                    break;
                }
                case 2: {      // 구간 출력
                    int targetId = Integer.parseInt(st.nextToken())-1;
                    sb.append(query(1, 0, n-1, targetId, targetId)).append('\n');
                    break;
                }
            }
        }
    }
    static long initTree(int id, int l, int r) {
        if(l == r) return tree[id] = arr[l];
        int mid = l + (r-l)/2;
        return tree[id] = initTree(id*2, l, mid) + initTree(id*2+1, mid+1, r);
    }
    static void update(int id, int l, int r, int s, int e, long v) {
        if(r < s || l > e) return;
        if(l >= s && r <= e) {      // 완전하게 포함
            tree[id] += (v * (r-l+1));
            lazy[id] += v;
            return;
        }
        // 부분 포함
        push(id, l, r);
        int mid = l + (r-l)/2;
        update(id*2, l, mid, s, e, v);
        update(id*2+1, mid+1, r, s, e, v);
        tree[id] = tree[id*2] + tree[id*2+1];
    }
    static long query(int id, int l, int r, int s, int e) {
        if(r < s || l > e) return 0;
        if(l >= s && r <= e) return tree[id];
        push(id, l, r);
        int mid = l + (r-l)/2;
        return query(id*2, l, mid, s, e) + query(id*2+1, mid+1, r, s, e);
    }
    static void push(int id, int l, int r) {
        if(lazy[id] == 0 || l == r) return;
        long v = lazy[id];
        int mid = l + (r-l)/2;
        tree[id*2] += v * (mid-l+1);
        lazy[id*2] += v;
        tree[id*2+1] += v * (r-mid);
        lazy[id*2+1] += v;
        lazy[id] = 0;
    }
}
