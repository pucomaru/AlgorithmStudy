package mar.week1.boj;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class 자동차공장_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static int seq = 0;
    static int[] in, out;
    static long[] salary, tree, lazy;
    static List<Integer>[] slave;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        in = new int[n+1];
        out = new int[n+1];
        salary = new long[n+1];
        slave = new List[n+1];
        for(int i=0; i<n+1; i++) slave[i] = new ArrayList<>();

        // 상근이
        st = new StringTokenizer(br.readLine().trim());
        salary[1] = Long.parseLong(st.nextToken());
        for(int i = 2; i<n+1; i++) {
            st = new StringTokenizer(br.readLine().trim());
            salary[i] = Long.parseLong(st.nextToken());
            int master = Integer.parseInt(st.nextToken());
            slave[master].add(i);
        }
        makeBound(1);

        tree = new long[4*n];
        lazy = new long[4*n];
        StringBuilder sb = new StringBuilder();
        while(m-- > 0){
            st = new StringTokenizer(br.readLine());
            String cmd = st.nextToken();

            if(cmd.equals("p")){
                int a = Integer.parseInt(st.nextToken());
                int x = Integer.parseInt(st.nextToken());

                int l = in[a] + 1;
                int r = out[a];
                update(1, 1, seq, l, r, x);
            }
            else{
                int a = Integer.parseInt(st.nextToken());
                long bonus = query(1, 1, seq, in[a], in[a]);
                sb.append(salary[a] + bonus).append("\n");
            }
        }

        System.out.print(sb);
    }
    static void makeBound(int empId) {
        /**
         * empId 의 범위를 구한다.
         * in : empId 가 시작하는 범위
         * out : empId 가 끝나는 범위
         */
        in[empId] = ++seq;
        for(int i : slave[empId]) makeBound(i);
        out[empId] = seq;
    }
    static void update(int id, int l, int r, int s, int e, int v) {
        if(r < s || l > e) return;
        if(l >= s && r <= e) {
            tree[id] += v * (r - l + 1);
            lazy[id] += v;      // 자기 자신의 월급은 올리지 않음
            return;
        }
        push(id, l, r);
        int mid = l + (r-l)/2;
        update(2*id, l, mid, s, e, v);
        update(2*id+1, mid+1, r, s, e, v);
        tree[id] = tree[2*id] + tree[2*id+1];
    }
    static long query(int id, int l, int r, int s, int e) {
        if(r < s || l > e) return 0;
        push(id, l, r);
        if(l >= s && r <= e) return tree[id];
        int mid = l + (r-l)/2;
        return query(2*id, l, mid, s, e) + query(2*id+1, mid+1, r, s, e);
    }
    static void push(int id, int l, int r) {
        if(l == r || lazy[id] == 0) return;

        long propagation = lazy[id];

        int mid = l + (r-l)/2;
        tree[2*id] += (propagation * (mid-l+1));
        lazy[2*id] += propagation;
        tree[2*id+1] += (propagation * (r-mid));
        lazy[2*id+1] += propagation;
        lazy[id] = 0;
    }
}
