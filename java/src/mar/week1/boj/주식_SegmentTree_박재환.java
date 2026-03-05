package mar.week1.boj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class 주식_SegmentTree_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for(int i=0; i<tc; i++) {
            sb.append("Case ").append('#').append(i+1).append('\n').append(init()).append('\n');
        }
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n, k;
    static int[] arr;
    static int[] tree;
    static int init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());

        // 1. 좌표 압축
        compress();
        // 2. 트리 생성
        tree = new int[4*size];
        buildTree();

        return tree[1] >= k ? 1 : 0;
    }
    static int size;
    static int[] compressed;
    static void compress() {
        int[] copy = new int[n];
        for(int i=0; i<n; i++) copy[i] = arr[i];
        Arrays.sort(copy);

        int id = 0;
        Map<Integer, Integer> map = new HashMap<>();
        for(int i : copy) {
            if(map.get(i) == null) map.put(i, id++);
        }

        size = id;
        compressed = new int[n];
        for(int i=0; i<n; i++) compressed[i] = map.get(arr[i]);
    }
    static void buildTree() {
        for(int i : compressed) {
            int prev = query(1, 0, size-1, 0, i-1);
            int cur = prev + 1;
            update(1, 0, size-1, i, i, cur);
        }
    }
    static int query(int id, int l, int r, int s, int e) {
        if(r < s || l > e) return 0;
        if(l >= s && r <= e) return tree[id];

        int mid = l + (r - l)/2;
        int left = query(2*id, l, mid, s, e);
        int right = query(2*id+1, mid+1, r, s, e);
        return Math.max(left, right);
    }
    static void update(int id, int l, int r, int s, int e, int v) {
        if(r < s || l > e) return;
        if(l >= s && r <= e) {
            tree[id] = v;
            return;
        }

        int mid = l + (r - l)/2;
        update(2*id, l, mid, s, e, v);
        update(2*id+1, mid+1, r, s, e, v);
        tree[id] = Math.max(tree[2*id], tree[2*id+1]);
    }
}
