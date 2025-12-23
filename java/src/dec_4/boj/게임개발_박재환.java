package dec_4.boj;

import java.util.*;
import java.io.*;

public class 게임개발_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }

    /**
     * 건물을 짓기 위해서, 먼저 지어져야하는 빌딩'들'이 있을 수 있다.
     * => N개의 각 건물이 완성되기까지 걸리는 최소 시간을 출력한다.
     *
     * => 건물이 지어지기 전에 만족되어야하는 선행조건이 있다.
     * => Topology Sort
     */
    static StringTokenizer st;
    static int buildingCount;
    static List<Building> buildings;
    static List<Integer>[] relevantBuilding;
    static int[] needBuilding;
    static void init() throws IOException {
        buildingCount = Integer.parseInt(br.readLine().trim());
        buildings = new ArrayList<>();
        buildings.add(new Building(0));         // 1-based
        needBuilding = new int[buildingCount+1];        // 1-based
        relevantBuilding = new List[buildingCount+1];
        for(int i=0; i<buildingCount+1; i++) relevantBuilding[i] = new ArrayList<>();

        for(int i=1; i<buildingCount+1; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int time = Integer.parseInt(st.nextToken());
            Building building = new Building(time);
            building.setPrevBuildings(st, i);
            buildings.add(building);
            needBuilding[i] = building.prevBuildings.size();
        }

        defineBuildSeq();
        int[] result = getRealTime();
        for(int i=1; i<buildingCount+1; i++) sb.append(result[i]).append('\n');
    }

    static Queue<Integer> buildSeq;
    static void defineBuildSeq() {
        Queue<Integer> topologySortQueue = new ArrayDeque<>();
        buildSeq = new ArrayDeque<>();

        for(int i=1; i<buildingCount+1; i++) {
            if(needBuilding[i] == 0) topologySortQueue.offer(i);
        }

        while(!topologySortQueue.isEmpty()) {
            int nowId = topologySortQueue.poll();
            buildSeq.offer(nowId);
            for(int buildingId : relevantBuilding[nowId]) {
                if(--needBuilding[buildingId] == 0) {
                    topologySortQueue.offer(buildingId);
                }
            }
        }
    }

    static int[] getRealTime() {
        int[] timeArr = new int[buildingCount+1];
        while(!buildSeq.isEmpty()) {
            int nowId = buildSeq.poll();
            Building building = buildings.get(nowId);
            if(building.prevBuildings.isEmpty()) {
                timeArr[nowId] = building.time;
                continue;
            }
            int maxTime = Integer.MIN_VALUE;
            for(int buildingId : building.prevBuildings) {
                maxTime = Math.max(timeArr[buildingId], maxTime);
            }
            timeArr[nowId] = building.time + maxTime;
        }
        return timeArr;
    }

    static class Building {
        int time;
        List<Integer> prevBuildings;

        public Building(int time) {
            this.time = time;
            prevBuildings = new ArrayList<>();
        }

        public void setPrevBuildings(StringTokenizer st, int id) {
            while(true) {
                int buildingId = Integer.parseInt(st.nextToken());
                if(buildingId == -1) break;
                this.prevBuildings.add(buildingId);
                relevantBuilding[buildingId].add(id);
            }
        }
    }
}
