package jan.week5.codetree;

import java.util.*;
import java.io.*;

public class 가로등설치_박재환 {
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
     * 가로등을 추가, 조정하려한다.
     *
     * 1 ~ N 까지의 직선 좌표
     * 모든 가로등은 소비 전력 r을 사용
     * 설치된 위치 x를 기준으로 [x-r, x+r] 구간을 밝힌다.
     */
    static StringTokenizer st;
    static void init() throws IOException {
        int tc = Integer.parseInt(br.readLine().trim());
        while(tc-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int type = Integer.parseInt(st.nextToken());

            switch(type) {
                case 100: {     // 마을 초기 세팅
                    setVillage();
                    break;
                }
                case 200: {     // 가로등 설치
                    installNewLamp();
                    break;
                }
                case 300: {     // 가로등 삭제
                    removeLamp();
                    break;
                }
                case 400: {
                    sb.append(getMinCost()).append('\n');
                    break;
                }
            }
        }
    }

    /**
     * [마을 상태 확인]
     * N : 거리의 크기
     * M : 초기에 존재하는 가로등의 개수
     * L[1], L[2] ... : 초기에 존재하는 가로등 위치의 정보
     *      - 오름차순으로 주어진다. (고유 번호 부여)
     */
    static int n;
    static ArrayList<Integer> lampLocations;
    static ArrayList<Integer> nextLamps;
    static ArrayList<Integer> prevLamps;

    static PriorityQueue<Road> roads;
    static PriorityQueue<int[]> lampLocationMinHeap;        // 위치를 기준으로 우선순위
    static PriorityQueue<int[]> lampLocationMaxHeap;        // 위치를 기준으로 우선순위
    static void setVillage() {
        // 초기화
        lampLocations = new ArrayList<>();
        nextLamps = new ArrayList<>();
        prevLamps = new ArrayList<>();
        // 초기 설정
        lampLocations.add(-1);      // 1-based
        nextLamps.add(-1);          // 1-based
        prevLamps.add(-1);          // 1-based

        roads = new PriorityQueue<>((a, b) -> {
            if(a.dist == b.dist) return Integer.compare(a.start, b.start);
            return Integer.compare(b.dist, a.dist);
        });
        lampLocationMinHeap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        lampLocationMaxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b[0], a[0]));
        n = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());
        for(int lampId=1; lampId < m+1; lampId++) {
            int location = Integer.parseInt(st.nextToken());
            lampLocations.add(location);
            nextLamps.add(lampId+1);
            prevLamps.add(lampId-1);

            lampLocationMinHeap.offer(new int[] {location, lampId});
            lampLocationMaxHeap.offer(new int[] {location, lampId});

            if(lampId > 1) {        // 이전 램프가 존재한다 -> 두 램프 사이의 거리를 구한다.
                 int prevLocation = lampLocations.get(lampId-1);
                 int dist = location - prevLocation;
                 roads.offer(new Road(lampId-1, lampId, dist, prevLocation));
            }

        }
        // 첫번째 가로등과, 마지막 가로등의 연결 설정
        prevLamps.set(1, -1);       // 첫 번째 가로등의 이전 가로등은 존재하지 않음
        nextLamps.set(m, -1);       // 마지막 가로등의 다음 가로등은 존재하지 않음
    }
    static class Road {
        int left, right;
        int dist;
        int start;

        Road(int left, int right, int dist, int start) {
            this.left = left;
            this.right = right;
            this.dist = dist;
            this.start = start;
        }
    }

    /**
     * [가로등 추가]
     * M개의 가로등 이후에 추가로 설치하는 명령
     * 인접한 가로등 사이 거리가 가장 먼 곳 가운데 새로운 가로등을 설치한다.
     *      - 같은 거리가 여러개 있다면, 좌표 값이 가장 작은 쌍을 선택한다.
     *      - 가운데 : /2 또는 올림처리
     */
    static void installNewLamp() {
        Road targetRoad = getMaxDistRoad();
        // 사용 처리
        roads.poll();

        // 새로운 가로등 위치
        int installLocation = targetRoad.start + (targetRoad.dist+1) / 2;   // 올림 처리
        int installId = lampLocations.size();

        // 새로운 램프 추가
        lampLocations.add(installLocation);
        prevLamps.add(targetRoad.left);
        nextLamps.add(targetRoad.right);
        // 기존 램프 포인터 변경
        nextLamps.set(targetRoad.left, installId);
        prevLamps.set(targetRoad.right, installId);

        lampLocationMinHeap.add(new int[] {installLocation, installId});
        lampLocationMaxHeap.add(new int[] {installLocation, installId});

        // 새로 생긴 도로 추가
        int distLeftToNew = installLocation - lampLocations.get(targetRoad.left);
        int distNewToRight = lampLocations.get(targetRoad.right) - installLocation;
        roads.offer(new Road(targetRoad.left, installId, distLeftToNew, lampLocations.get(targetRoad.left)));
        roads.offer(new Road(installId, targetRoad.right, distNewToRight, installLocation));
    }
    /**
     * [가로등 제거]
     * D번 가로등을 제거한다.
     */
    static void removeLamp() {
        int targetLampId = Integer.parseInt(st.nextToken());
        lampLocations.set(targetLampId, -1);

        int prevLamp = prevLamps.get(targetLampId);
        int nextLamp = nextLamps.get(targetLampId);

        if(prevLamp != -1) nextLamps.set(prevLamp, nextLamp);
        if(nextLamp != -1) prevLamps.set(nextLamp, prevLamp);

        // prev, next 가 모두 존재하면 새로 생긴 도로 추가
        if(prevLamp != -1 && nextLamp != -1) {
            int left = lampLocations.get(prevLamp);
            int right = lampLocations.get(nextLamp);
            int dist = right - left;
            int start = left;
            Road road = new Road(prevLamp, nextLamp, dist, start);
            roads.offer(road);
        }
    }

    /**
     * [최적 위치 계산]
     * 마을의 거리를 전부 밝히기 위한 최소 소비 전력을 구한다.
     */
    static long getMinCost() {
        Road candidateRoad = getMaxDistRoad();
        int maxLocation = getMaxLampLocation();
        int minLocation = getMinLampLocation();

        long result1 = 2L * (minLocation-1);
        long result2 = 2L * (n-maxLocation);
        long result3 = candidateRoad == null ? 0 : candidateRoad.dist;

        return Math.max(result1, Math.max(result2, result3));
    }

    /**
     * 공통 로직
     */
    static Road getMaxDistRoad() {
        while(!roads.isEmpty()) {
            Road road = roads.peek();
            int leftLampId = road.left;
            int rightLampId = road.right;
            int dist = road.dist;
            int start = road.start;
            // 유효한 Road 라면 즉시 종료하고 반환
            if(lampLocations.get(leftLampId) == start && lampLocations.get(rightLampId) == start + dist) break;
            // 유효하지 않다면 삭제
            roads.poll();
        }
        return roads.peek();
    }

    static int getMinLampLocation() {
        while(!lampLocationMinHeap.isEmpty()) {
            int[] lamp = lampLocationMinHeap.peek();
            int location = lamp[0];
            int lampId = lamp[1];

            if(lampLocations.get(lampId) == location) break;

            lampLocationMinHeap.poll();
        }
        return lampLocationMinHeap.peek()[0];
    }
    static int getMaxLampLocation() {
        while(!lampLocationMaxHeap.isEmpty()) {
            int[] lamp = lampLocationMaxHeap.peek();
            int location = lamp[0];
            int lampId = lamp[1];

            if(lampLocations.get(lampId) == location) break;

            lampLocationMaxHeap.poll();
        }
        return lampLocationMaxHeap.peek()[0];
    }
}
