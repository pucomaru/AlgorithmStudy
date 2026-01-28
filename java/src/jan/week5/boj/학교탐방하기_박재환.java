package jan.week5.boj;

import java.util.*;
import java.io.*;

public class 학교탐방하기_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    /**
     * 도로의 종류
     * - 오르막
     * - 내리막
     *
     * 입구 : 숫자 0
     * 각 건물들에는 번호가 있다.
     *
     * 오르막을 k 번 오를 때, 피로도는 K**2 가 된다.
     *
     * [피로도의 계산은 최초 조사된 길을 기준으로 한다.]
     * => 내리막길로 내려갔다가 다시 올라올 때 오르막길이 되는 경우는 고려하지 않는다.
     * => MST
     */
    static PriorityQueue<int[]> minHeap;
    static PriorityQueue<int[]> maxHeap;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        int buildingCount = Integer.parseInt(st.nextToken());
        int roadCount = Integer.parseInt(st.nextToken());
        minHeap = new PriorityQueue<>((a, b) -> Integer.compare(b[2], a[2]));
        maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));
        st = new StringTokenizer(br.readLine().trim());
        int enter = Integer.parseInt(st.nextToken());
        int enterBuilding = Integer.parseInt(st.nextToken());
        int enterCost = Integer.parseInt(st.nextToken());
        while(roadCount-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());
            int[] road = new int[] {from, to, cost};
            minHeap.offer(road);
            maxHeap.offer(road);
        }
        make(buildingCount);
        union(enter, enterBuilding);
        int minUpHills = findMinUphills(buildingCount) + (enterCost == 0 ? 1 : 0);
        make(buildingCount);
        union(enter, enterBuilding);
        int maxUpHills = findMaxUpHills(buildingCount) + (enterCost == 0 ? 1 : 0);
        System.out.println((maxUpHills * maxUpHills) - (minUpHills * minUpHills));
    }

    static int findMinUphills(int buildingCount) {
        int confirmRoads = 0;
        int uphills = 0;
        while(!minHeap.isEmpty()) {
            int[] road = minHeap.poll();
            int from = road[0];
            int to = road[1];
            int cost = road[2];

            if(union(from, to)) {
                uphills += (cost == 0 ? 1 : 0);
                if(++confirmRoads == buildingCount-1) break;
            }
        }
        return uphills;
    }

    static int findMaxUpHills(int buildingCount) {
        int confirmRoads = 0;
        int uphills = 0;
        while(!maxHeap.isEmpty()) {
            int[] road = maxHeap.poll();
            int from = road[0];
            int to = road[1];
            int cost = road[2];

            if(union(from, to)) {
                uphills += (cost == 0 ? 1 : 0);
                if(++confirmRoads == buildingCount-1) break;
            }
        }
        return uphills;
    }

    static int[] parents;
    static void make(int buildingCount) {
       parents = new int[buildingCount+1];
       for(int i=0; i<buildingCount+1; i++) parents[i] = i;
    }
    static int find(int node) {
        if(node == parents[node]) return parents[node];
        return parents[node] = find(parents[node]);
    }
    static boolean union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);
        if(rootA == rootB) return false;
        parents[rootA] = rootB;
        return true;
    }
}
