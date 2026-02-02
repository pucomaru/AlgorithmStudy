package jan.week5.boj;

import java.util.*;
import java.io.*;

public class 해킹_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        init(br, sb);
        br.close();
        System.out.println(sb);
    }
    static final int INF = 1000 * 10000 + 7;
    /**
     * 서로 의존하는 컴퓨턷즐은 하나 둘 전염되기 시작한다.
     */
    static void init(BufferedReader br, StringBuilder sb) throws IOException {
        StringTokenizer st;
        int tc = Integer.parseInt(br.readLine().trim());
        int computerCount, dependencyCount, hackedComputer;
        List<int[]>[] connections;
        while(tc-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            computerCount = Integer.parseInt(st.nextToken());
            dependencyCount = Integer.parseInt(st.nextToken());
            hackedComputer = Integer.parseInt(st.nextToken());
            connections = new List[computerCount+1];
            for(int i=0; i<computerCount+1; i++) connections[i] = new ArrayList<>();
            while(dependencyCount-- >0) {
                st = new StringTokenizer(br.readLine().trim());
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                int time = Integer.parseInt(st.nextToken());
                connections[b].add(new int[] {a, time});
            }
            Result result = solution(computerCount, connections, hackedComputer);
            sb.append(result.hackedComputerCount).append(' ').append(result.totalTime).append('\n');
        }
    }
//    static Set<Integer> computer;
//    static int maxTime;
//    static Result solution(int computerCount, List<int[]>[] connections, int hackedComputer) {
//        computer = new HashSet<>();
//        maxTime = 0;
//        /**
//         * hackedComputer 로부터 의존 관계의 모든 컴퓨터를 확인한다.
//         *
//         * 마지막 컴퓨터가 감염되기까지의 시간이 필요하다.
//         * (DFS, BFS)
//         *
//         * DFS 재귀 호출하며, 더 이상 감염시킬 컴퓨터가 없을 때, 시간 값으로 max 값 갱신
//         * ❌ Node 의 개수 1만, Edge 의 개수 10만
//         * => 10억 시간초과
//         */
//        int[] infected = new int[computerCount+1];
//        Arrays.fill(infected, INF);
//        infected[hackedComputer] = 0;
//        computer.add(hackedComputer);
//        infectionComputer(hackedComputer, 0 , infected, connections);
//        return new Result(computer.size(), maxTime);
//    }
//
//    static void infectionComputer(int cur, int totalTime, int[] infected, List<int[]>[] connections) {
//        boolean hasAdj = false;     // 의존 컴퓨터를 가지고 있는지
//
//        for(int[] connection : connections[cur]) {
//            int next = connection[0];
//            int time = connection[1];
//            if(infected[next] < totalTime + time) continue;
//            hasAdj = true;
//            computer.add(next);
//            infected[next] = totalTime + time;
//            infectionComputer(next, infected[next], infected, connections);
//        }
//
//        if(!hasAdj) maxTime = Math.max(maxTime, totalTime);
//    }
//
    static class Result {
        int hackedComputerCount;
        int totalTime;
        Result(int hackedComputerCount, int totalTime) {
            this.hackedComputerCount = hackedComputerCount;
            this.totalTime = totalTime;
        }
    }

    static Result solution(int computerCount, List<int[]>[] connections, int hackedComputer) {
        /**
         * 다익스트라를 사용해서, 현재 hackedComputer 에서 이동가능한 모든 경로를 탐색
         * => 그 중 최대 값을 반환
         */
        int[] dpArr = new int[computerCount+1];
        Arrays.fill(dpArr, INF);

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        dpArr[hackedComputer] = 0;      // 초기 위치
        pq.offer(new int[] {hackedComputer, dpArr[hackedComputer]});

        while(!pq.isEmpty()) {
            int[] cur = pq.poll();
            int from = cur[0];
            int accTime = cur[1];

            if(dpArr[from] < accTime) continue;
            for(int[] conn : connections[from]) {
                int to = conn[0];
                int time = conn[1];

                if(dpArr[to] > accTime + time) {
                    dpArr[to] = accTime + time;
                    pq.offer(new int[] {to, dpArr[to]});
                }
            }
        }
        return mapToResult(dpArr);
    }

    static Result mapToResult(int[] dpArr) {
        int max = -1;
        int count = 0;

        for(int i : dpArr) {
            if(i == INF) continue;
            max = Math.max(max, i);
            count++;
        }
        return new Result(count, max);
    }
}
