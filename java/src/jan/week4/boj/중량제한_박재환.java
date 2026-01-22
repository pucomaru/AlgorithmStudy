package jan.week4.boj;

import java.util.*;
import java.io.*;

public class 중량제한_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static final int MAX = 1_000_000_005;

    static int n;
    static int s, e;
    static List<int[]>[] roads;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        roads = new List[n+1];
        for(int i=0; i<n+1; i++) roads[i] = new ArrayList<>();

        /**
         * 모든 다리는 양방향으로 이어져있다.
         */
        while(m-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());

            roads[from].add(new int[] {to, weight});
            roads[to].add(new int[] {from, weight});
        }

        st = new StringTokenizer(br.readLine().trim());
        s = Integer.parseInt(st.nextToken());
        e = Integer.parseInt(st.nextToken());

        System.out.println(findMaxRoute());
    }

    static long findMaxRoute() {
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(b[1], a[1]));
        int[] maxWeight = new int[n+1];

        // 초기 위치 설정
        maxWeight[s] = MAX;     // 최대 중량으로 출발
        pq.offer(new int[] {s, MAX});

        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int from = cur[0];
            int curMaxWeight = cur[1];

            if(maxWeight[from] > curMaxWeight) continue;

            for(int[] road : roads[from]) {
                int to = road[0];
                int candidateMaxWeight = road[1];

                if(maxWeight[to] < Math.min(candidateMaxWeight, curMaxWeight)) {
                    maxWeight[to] = Math.min(candidateMaxWeight, curMaxWeight);
                    pq.offer(new int[] {to, maxWeight[to]});
                }
            }
        }

        return maxWeight[e];
    }
}
