package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 성대나라의물탱크_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static final int UPDATE = 1;
    static final int QUERY = 2;

    static StringTokenizer st;
    static int n, c;
    static int seq;
    static int[] in, out, depth;
    static long[] tree;
    static List<Integer>[] connections;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 도시 수
        c = Integer.parseInt(st.nextToken());       // 수도 번호

        connections = new List[n+1];
        for(int i=0; i<n+1; i++) connections[i] = new ArrayList<>();
        for(int i=0; i<n-1; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int parents = Integer.parseInt(st.nextToken());
            int child = Integer.parseInt(st.nextToken());
            // 무방향 그래프
            connections[parents].add(child);
            connections[child].add(parents);
        }
        seq = 0;
        in = new int[n+1];
        out = new int[n+1];
        depth = new int[n+1];
        makeBound(c, -1, 1);
        tree = new long[4*n];
        StringBuilder sb = new StringBuilder();
        int cmdCnt = Integer.parseInt(br.readLine().trim());
        while(cmdCnt-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());
            if(cmd == UPDATE) {
                int node = Integer.parseInt(st.nextToken());
                update(1, 1, seq, in[node], in[node], 1);
            }
            else if(cmd == QUERY) {
                int node = Integer.parseInt(st.nextToken());
                sb.append(query(1, 1, seq, in[node], out[node]) * depth[node]).append('\n');
            }
        }
        System.out.println(sb);
    }
    static void makeBound(int id, int prev, int d) {
        in[id] = ++seq;
        depth[id] = d;
        for(int i : connections[id]) {
            if(i == prev) continue;
            makeBound(i, id, d+1);
        }
        out[id] = seq;
    }
    static void update(int id, int l, int r, int s, int e, int v) {
        if(r < s || l > e) return;
        if(l >= s && r <= e) {
            tree[id] += v;
            return;
        }
        int mid = l + (r - l)/2;
        update(2*id, l, mid, s, e, v);
        update(2*id+1, mid+1, r, s, e, v);
        tree[id] = tree[2*id] + tree[2*id+1];
    }
    static long query(int id, int l, int r, int s, int e) {
        if(r < s || l > e) return 0;
        if(l >= s && r <= e) return tree[id];

        int mid = l + (r - l)/2;
        return query(2*id, l, mid, s, e) + query(2*id+1, mid+1, r, s, e);
    }
}
