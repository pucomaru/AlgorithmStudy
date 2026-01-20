package jan.week4.boj;

import java.util.*;
import java.io.*;

public class 수열과쿼리16_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static final int INF = 1_000_000_001;
    static final Node DUMMY = new Node(INF, INF);

    static StringTokenizer st;
    static int n;
    static int[] arr;
    static Node[] tree;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        arr = new int[n];
        tree = new Node[4*n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());
        makeTree(1, 0, n-1);
        int tc = Integer.parseInt(br.readLine().trim());
        while(tc-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int command = Integer.parseInt(st.nextToken());
            switch(command) {
                case 1: {
                    int targetIdx = Integer.parseInt(st.nextToken())-1;
                    int targetValue = Integer.parseInt(st.nextToken());
                    update(1, 0, n-1, targetIdx, targetValue);
                    break;
                }
                case 2: {
                    int l = Integer.parseInt(st.nextToken())-1;
                    int r = Integer.parseInt(st.nextToken())-1;
                    sb.append(query(1, 0, n-1, l, r).idx+1).append('\n');
                    break;
                }
            }
        }
    }
    static Node min(Node a, Node b) {
        if(a.value > b.value) return b;
        if(a.value < b.value) return a;
        return a.idx > b.idx ? b : a;
    }

    static class Node {
        int idx;
        int value;

        Node(int idx, int value) {
            this.idx = idx;
            this.value = value;
        }

        void setValue(int value) { this.value = value; }
    }

    static Node makeTree(int idx, int s, int e) {
        if(s == e) return tree[idx] = new Node(s, arr[s]);

        int mid = s + (e-s)/2;
        return tree[idx] = min(makeTree(idx*2, s, mid), makeTree(idx*2+1, mid+1, e));
    }

    static Node query(int idx, int s, int e, int l, int r) {
        if (r < s || l > e) return DUMMY;     // 최소값을 조회하는 문제 -> 최대값 반환 ( 값에 영향 X )

        if (l <= s && r >= e) return tree[idx];      // 현재 쿼리에 범위가 완전하게 포함되는 경우
        int mid = s + (e - s) / 2;
        return min(query(idx * 2, s, mid, l, r), query(idx * 2 + 1, mid + 1, e, l, r));
    }

    static void update(int idx, int s, int e, int targetIdx, int targetValue) {
        if(targetIdx < s || targetIdx > e) return;

        if(s == e) {    // 리프노드까지 도달했다면
            arr[s] = targetValue;
            tree[idx].setValue(targetValue);
            return;
        }

        // 바텀업 방식으로 갱신
        int mid = s + (e - s) / 2;
        update(idx*2, s, mid, targetIdx, targetValue);
        update(idx*2+1, mid+1, e, targetIdx, targetValue);

        tree[idx] = min(tree[idx*2], tree[idx*2+1]);
    }
}
