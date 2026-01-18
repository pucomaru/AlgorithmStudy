package jan.week3.boj;

import java.util.*;
import java.io.*;

public class 문제집_박재환 {
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
    static int n, m;        // 문제의 수, 선행 정보
    static int[] inEdges;
    static List<Integer>[] connections;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        inEdges = new int[n+1];
        connections = new List[n+1];
        for(int i=0; i<n+1; i++) connections[i] = new ArrayList<>();

        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int prev = Integer.parseInt(st.nextToken());
            int next = Integer.parseInt(st.nextToken());
            /**
             * 선행 문제를 풀어, 다음 문제 해금 : connections
             * 문제를 풀기위해 진행되어야하는 선행 문제 : inEdges
             */
            connections[prev].add(next);
            inEdges[next]++;
        }

        solveProblems();
    }

    static void solveProblems() {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Integer::compare);

        // 1. 현재 바로 풀 수 있는 문제
        for(int i=1; i<n+1; i++) {
            if(inEdges[i] == 0) pq.offer(i);
        }

        // 2. 하나하나 해금하기
        while(!pq.isEmpty()) {
            int problem = pq.poll();

            for(int connection : connections[problem]) {
                if(--inEdges[connection] == 0) pq.offer(connection);
            }

            sb.append(problem).append(' ');
        }
    }
}
