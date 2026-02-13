package feb.week2.boj;

import java.util.*;
import java.io.*;

public class 수들의합7_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static final int SUM = 0;
    static final int MODIFY = 1;
    static final long DUMMY = 0;

    static StringTokenizer st;
    static int n, m;
    static int[] arr;
    static long[] tree;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        arr = new int[n];
        tree = new long[4*n];
        buildTree(1, 0, n-1);

        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());
            if(cmd == SUM) {
                int i = Integer.parseInt(st.nextToken())-1;
                int j = Integer.parseInt(st.nextToken())-1;
                sb.append(sum(i, j)).append('\n');
            }
            else if(cmd == MODIFY) {
                int id = Integer.parseInt(st.nextToken())-1;
                int target = Integer.parseInt(st.nextToken());
                modify(id, target);
            }
        }
    }
    static long sum(int i, int j) {
        int s = Math.min(i, j);
        int e = Math.max(i, j);
        return query(1, 0, n-1, s, e);
    }
    static void modify(int id, int target) {
        update(1, 0, n-1, id, id, target);
    }
    static long buildTree(int id, int l, int r) {
        if(l == r) return tree[id] = arr[l];

        int mid = l + (r-l)/2;
        return tree[id] = buildTree(2*id, l, mid) + buildTree(2*id+1, mid+1, r);
    }
    static long query(int id, int l, int r, int s, int e) {
        if(r < s || l > e) return DUMMY;       // 범위를 벗어남 (의미 없음)
        if(l >= s && r <= e) return tree[id];  // 완전하게 포함됨

        int mid = l + (r-l)/2;
        return query(2*id, l, mid, s, e) + query(2*id+1, mid+1, r, s, e);
    }
    static void update(int id, int l, int r, int s, int e, int targetValue) {
        if(r < s || l > e) return;       // 범위를 벗어남 (의미 없음)
        if(l >= s && r <= e) {
            tree[id] -= arr[l];
            arr[l] = targetValue;
            tree[id] += arr[l];
            return;
        }

        int mid = l + (r-l)/2;
        update(2*id, l, mid, s, e, targetValue);
        update(2*id+1, mid+1, r, s, e, targetValue);
        tree[id] = tree[2*id] + tree[2*id+1];
    }
}
