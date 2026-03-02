package feb.week4.boj;

import java.util.*;
import java.io.*;

public class 나만안되는연애_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static int[] genders;
    static PriorityQueue<int[]> pq;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        genders = new int[n+1];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=1; i<n+1; i++) {
            String gender = st.nextToken();
            if(gender.equals("M")) genders[i] = 0;
            else if(gender.equals("W")) genders[i] = 1;
        }

        pq = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));
        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());
            pq.offer(new int[] {from, to, cost});
        }

        System.out.println(findMinCost());
    }

    static int findMinCost() {
        int totalCost = 0;
        int edge = 0;

        makeTree();
        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int from = cur[0];
            int to = cur[1];
            int cost = cur[2];
            if(genders[from] == genders[to]) continue;
            if(!union(from, to)) continue;
            totalCost += cost;
            if(++edge == n-1) break;
        }

        return edge == n-1 ? totalCost : -1;
    }
    static int[] parents;
    static int[] ranks;
    static void makeTree() {
        parents = new int[n+1];
        ranks = new int[n+1];
        for(int i=0; i<n+1; i++) parents[i] = i;
    }
    static int find(int a) {
        if(parents[a] == a) return a;
        return parents[a] = find(parents[a]);
    }
    static boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if(rootB == rootA) return false;

        if(ranks[rootA] < ranks[rootB]) parents[rootA] = rootB;
        else if(ranks[rootA] > ranks[rootB]) parents[rootB] = rootA;
        else {
            parents[rootB] = rootA;
            ranks[rootA]++;
        }

        return true;
    }
}
