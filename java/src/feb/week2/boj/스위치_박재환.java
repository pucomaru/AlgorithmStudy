package feb.week2.boj;

import java.util.*;
import java.io.*;

public class 스위치_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    /**
     * N개의 스위치가 있다 1 ~ N 번호를 갖늗다.
     *
     * [A ~ B 스위치 상태를 반전시키는 것]
     * [C ~ D 사이의 스위치 중 켜져 있는 상태의 스위치의 개수를 세는 것]
     */
    static StringTokenizer st;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        int n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        SegmentTree tree = new SegmentTree(n);
        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());
            switch(cmd) {
                case 0: {
                    int s = Integer.parseInt(st.nextToken());
                    int e = Integer.parseInt(st.nextToken());
                    tree.update(1, 1, n, s, e);
                    break;
                }
                case 1: {
                    int s = Integer.parseInt(st.nextToken());
                    int e = Integer.parseInt(st.nextToken());
                    sb.append(tree.query(1, 1, n, s, e)).append('\n');
                    break;
                }
            }
        }
    }
    static class SegmentTree {
        int size;
        int[] tree;         // 각 구간에서 켜진 스위치의 개수
        boolean[] lazy;     // 스위치 반전 명령 기록

        SegmentTree(int n) {
            size = n;
            tree = new int[4*(n+1)];
            lazy = new boolean[4*(n+1)];
        }

        int query(int id, int l, int r, int s, int e) {
            if(r < s || l > e) return 0;
            if(l >= s && r <= e) return tree[id];
            push(id, l, r);     // 전파

            int mid = l + (r - l)/2;
            return query(2*id, l, mid, s, e) + query(2*id+1, mid+1, r, s, e);
        }

        void update(int id, int l, int r, int s, int e) {
            /**
             * [s, e]
             * 구간의 스위치를 반전시킨다.
             */
            if(r < s || l > e) return;      // 범위에 완전하게 포함되지 않는 경우
            if(l >= s && r <= e) {
                tree[id] = (r-l+1) - tree[id];
                lazy[id] = !lazy[id];
                return;
            }
            push(id, l, r);     // 전파

            int mid = l + (r - l)/2;
            update(2*id, l, mid, s, e);
            update(2*id+1, mid+1, r, s, e);
            tree[id] = tree[2*id] + tree[2*id+1];
        }

        void push(int id, int l, int r) {
            if(!lazy[id] || l == r) return;           // 전파할 내용이 없음 / 리프 노드임

            int mid = l + (r - l)/2;
            tree[2*id] = (mid-l+1) - tree[2*id];
            lazy[2*id] = !lazy[2*id];
            tree[2*id+1] = (r-mid) - tree[2*id+1];
            lazy[2*id+1] = !lazy[2*id+1];

            lazy[id] = !lazy[id];
        }
    }
}
