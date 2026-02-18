package feb.week3.boj;

import java.util.*;
import java.io.*;

public class 수열과쿼리37_박재환 {
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
    static void input() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        arr = new int[n+1];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=1; i<n+1; i++) arr[i] = Integer.parseInt(st.nextToken());
    }
    static int n;
    static int[] arr;
    static void init() throws IOException {
        input();
        SegmentTree tree = new SegmentTree(n);
        tree.buildTree(1, 1, n);
        int queryCount = Integer.parseInt(br.readLine().trim());
        while(queryCount-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());
            if(cmd == 1) {
                int targetId = Integer.parseInt(st.nextToken());
                int targetValue = Integer.parseInt(st.nextToken());
                tree.update(1, 1, n, targetId, targetId, targetValue);
            }
            else if(cmd == 2) {
                int s = Integer.parseInt(st.nextToken());
                int e = Integer.parseInt(st.nextToken());
                sb.append(tree.evenQuery(1, 1, n, s, e)).append('\n');
            }
            else if(cmd == 3) {
                int s = Integer.parseInt(st.nextToken());
                int e = Integer.parseInt(st.nextToken());
                sb.append(tree.oddQuery(1, 1, n, s, e)).append('\n');
            }
        }
    }
    static class SegmentTree {
        /**
         * 갱신 자체가 구간에 대한 업데이트는 아님
         */
        int size;
        int[] even;
        int[] odd;

        SegmentTree(int n) {
            this.size = n;
            this.even = new int[4*size];
            this.odd = new int[4*size];
        }

        void buildTree(int id, int l, int r) {
            if(l == r) {        // 리프노드라면
                // 현재 원소가 짝수인지 홀수인지 확인 후 갱신
                if(arr[l]%2 == 0) even[id]++;
                else odd[id]++;
                return;
            }
            int mid = l + (r-l)/2;
            buildTree(2*id, l, mid);
            buildTree(2*id+1, mid+1, r);

            // 홀수와 짝수 노드를 구분해서 갱신
            even[id] = even[2*id] + even[2*id+1];
            odd[id] = odd[2*id] + odd[2*id+1];
        }

        void update(int id, int l, int r, int s, int e, int v) {
            if(r < s || l > e) return;
            if(l >= s && r <= e) {      // 리프노드
                // 기존 값을 확인한 뒤, 갱신
                int prev = arr[s];
                if(prev%2 == 0) even[id]--;
                else odd[id]--;
                // 새로운 값 갱신
                if(v%2 == 0) even[id]++;
                else odd[id]++;
                arr[l] = v;
                return;
            }
            int mid = l + (r-l)/2;
            update(2*id, l, mid, s, e, v);
            update(2*id+1, mid+1, r, s, e, v);
            // 홀수와 짝수 노드를 구분해서 갱신
            even[id] = even[2*id] + even[2*id+1];
            odd[id] = odd[2*id] + odd[2*id+1];
        }
        int evenQuery(int id, int l, int r, int s, int e) {
            if(r < s || l > e) return 0;
            if(l >= s && r <= e) return even[id];
            int mid = l + (r-l)/2;
            return evenQuery(2*id, l, mid, s, e) + evenQuery(2*id+1, mid+1, r, s, e);
        }
        int oddQuery(int id, int l, int r, int s, int e) {
            if(r < s || l > e) return 0;
            if(l >= s && r <= e) return odd[id];
            int mid = l + (r-l)/2;
            return oddQuery(2*id, l, mid, s, e) + oddQuery(2*id+1, mid+1, r, s, e);
        }
    }
}
