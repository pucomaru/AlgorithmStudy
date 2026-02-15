package feb.week2.boj;

import java.util.*;
import java.io.*;

public class XOR_박재환 {
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
    static int[] arr;
    static void init() throws IOException {
        int n = Integer.parseInt(br.readLine().trim());
        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());

        SegmentTree tree = new SegmentTree(n);
        int m = Integer.parseInt(br.readLine().trim());
        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());
            switch(cmd) {
                case 1: {
                    int s = Integer.parseInt(st.nextToken());
                    int e = Integer.parseInt(st.nextToken());
                    int v = Integer.parseInt(st.nextToken());
                    tree.update(1, 0, n-1, s, e, v);
                    break;
                }
                case 2: {
                    int s = Integer.parseInt(st.nextToken());
                    int e = Integer.parseInt(st.nextToken());
                    sb.append(tree.query(1, 0, n-1, s, e)).append('\n');
                    break;
                }
            }
        }
    }
    static class SegmentTree {
        int size;
        int[] tree;
        int[] lazy;

        SegmentTree(int n) {
            this.size = n;
            this.tree = new int[4*n];
            this.lazy = new int[4*n];

            buildTree(1, 0, n-1);
        }

        int buildTree(int id, int l, int r) {
            if(l == r) return tree[id] = arr[l];
            int mid = l + (r - l)/2;
            return tree[id] = buildTree(2*id, l, mid) ^ buildTree(2*id+1, mid+1, r);
        }

        void update(int id, int l, int r, int s, int e, int v) {
            if(r < s || l > e) return;
            if(l >= s && r <= e) {
                /**
                 * [l ~ r] 까지 v 에 대한 xor 연산을 적용
                 *
                 * 원소의 개수가 짝수면 XOR 값은 변하지 않음
                 * 홀수면 변함
                 */
                if((r-l+1) % 2 != 0) tree[id] ^= v;
                lazy[id] ^= v;
                return;
            }
            push(id, l, r);
            int mid = l + (r - l)/2;
            update(2*id, l, mid, s, e, v);
            update(2*id+1, mid+1, r, s, e, v);
            tree[id] = tree[2*id] ^ tree[2*id+1];
        }
        int query(int id, int l, int r, int s, int e) {
            if(r < s || l > e) return 0;
            if(l >= s && r <= e) return tree[id];
            push(id, l, r);
            int mid = l + (r - l)/2;
            return query(2*id, l, mid, s, e) ^ query(2*id+1, mid+1, r, s, e);
        }
        void push(int id, int l, int r) {
            if(lazy[id] == 0 || l == r) return;

            int xorV = lazy[id];
            int mid = l + (r - l)/2;
            if((mid-l+1) % 2 != 0) tree[2*id] ^= xorV;
            lazy[2*id] ^= xorV;
            if((r-mid) % 2 != 0) tree[2*id+1] ^= xorV;
            lazy[2*id+1] ^= xorV;

            lazy[id] = 0;
        }
    }
}
