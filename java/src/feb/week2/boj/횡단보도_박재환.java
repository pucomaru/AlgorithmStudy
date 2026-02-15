package feb.week2.boj;

import java.util.*;
import java.io.*;

public class 횡단보도_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * 사람이 지나갈 수 있는 N개의 지역이 있고, 그 지역 사이를 잇는 몇 개의 횡단보도가 있다.
     * 파란불이 들어오는 순서를 알고있다.
     *
     * 횡단보도의 주기는 총 M분이며 1분마다 신호가 바뀐다.
     * 횡단보도를 이용해 반대편 지역으로 이동하는데 1분이 걸린다.
     */
    static StringTokenizer st;
    static int n, m;
    static List<CrossWalk>[] crossWalks;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        crossWalks = new List[n+1];
        for(int i=0; i<n+1; i++) crossWalks[i] = new ArrayList<>();
        for(int time=0; time<m; time++) {
            st = new StringTokenizer(br.readLine().trim());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            crossWalks[a].add(new CrossWalk(b, time));
            crossWalks[b].add(new CrossWalk(a, time));
        }

        System.out.println(findFastestRoute());
    }
    static class CrossWalk {
        int to;
        int time;

        CrossWalk(int to, int time) {
            this.to = to;
            this.time = time;
        }
    }
    static long findFastestRoute() {
        PriorityQueue<State> pq = new PriorityQueue<>();
        long[] dist = new long[n+1];
        Arrays.fill(dist, Long.MAX_VALUE);

        dist[1] = 0;
        pq.offer(new State(1,0));

        while(!pq.isEmpty()) {
            State cur = pq.poll();
            int node = cur.node;
            long time = cur.time;

            if(time > dist[node]) continue;
            if(node == n) return time;

            for(CrossWalk crossWalk : crossWalks[node]) {
                int next = crossWalk.to;
                int openTime = crossWalk.time;

                long nextTime = nextTime(time, openTime);
                nextTime++;     // 이동시간

                if(nextTime < dist[next]) {
                    dist[next] = nextTime;
                    pq.offer(new State(next, dist[next]));
                }
            }
        }
        return dist[n];
    }
    static class State implements Comparable<State> {
        int node;
        long time;

        State(int node, long time) {
            this.node = node;
            this.time = time;
        }

        @Override
        public int compareTo(State o) {
            return Long.compare(this.time, o.time);
        }
    }

    /**
     * 현재 시간에서 가장 빠르게 건널 수 있는 시간을 구한다.
     *
     * time + k*M ≥ curTime
     * => k ≥ (curTime - time) / m
     * 하지만 시간은 정수이므로, 올림 연산
     */
    static long nextTime(long curTime, int time) {
        if(curTime <= time) return time;
        long nextTime = (curTime-time+m-1)/m;       // 몇 번째 주기인지 구하기 => time + k*M ≥ curTime
        return time + nextTime * m;
    }
}
