package feb.week1.codetree;

import java.util.*;
import java.io.*;

public class 여왕개미_박재환 {
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
     * 1차원 수직선으로 표현 (0 ~ 10**9)
     *
     * [마을건설]
     * - 여왕개미 집을 x = 0 에 건설
     * - N개의 개미집 건설 (1~N번호를 가짐)
     * - 번호가 큰 것이 더 큰 x 좌표를 가짐
     *
     * [개미집 건설]
     * - 건설할 개미집 위치는 p
     * - p는 이제까지 지어진 집보다 큰 좌표로 주어짐
     *
     * [개미집 철거]
     * - q번 개미집을 철거
     *
     * [개미집 정찰]
     * - 정찰 나갈 개미의 수 r
     * - 각 개미들은 출발할 개미집 선택 (여왕집도 가능)
     * - 1초에 1만큼 x+1 이동
     * - 개미가 지나간 집은 안전한 개미집이 된다. -> 여왕 개미가 있는 곳은 항상 안전한 개미집
     * - 더 이상 전진할 수 없거나, 안전한 개미집을 만나면 이동을 멈춤
     * - 정찰에 걸리는 시간이 최소가 되도록 개미집 선택
     */
    static StringTokenizer st;
    static void init() throws IOException {
        int tc = Integer.parseInt(br.readLine().trim());
        while(tc-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int command = Integer.parseInt(st.nextToken());
            switch(command) {
                case 100: {     // 마을 건설
                    buildVillage();
                    break;
                }
                case 200: {     // 개미집 건설
                    buildNewHome();
                    break;
                }
                case 300: {     // 개미집 철거
                    removeHome();
                    break;
                }
                case 400: {     // 개미집 정찰
                    sb.append(reconnaissanceHome()).append('\n');
                    break;
                }
            }
        }
    }
    static int hIdx;
    static TreeSet<Integer> homeLocations;      // 집 좌표 정보 저장
    static Map<Integer, Integer> idToLocation;  // id와 좌표정보 매핑
    static void buildVillage() {
        hIdx = 0;
        homeLocations = new TreeSet<>(Integer::compare);
        idToLocation = new HashMap<>();
        // 여왕 집
        hIdx++;
        int input = Integer.parseInt(st.nextToken());
        while(input-- > 0) {
            int location = Integer.parseInt(st.nextToken());
            homeLocations.add(location);
            idToLocation.put(hIdx++, location);
        }
    }
    static void buildNewHome() {
        int location = Integer.parseInt(st.nextToken());
        homeLocations.add(location);
        idToLocation.put(hIdx++, location);
    }
    static void removeHome() {
        int id = Integer.parseInt(st.nextToken());
        int location = idToLocation.get(id);
        homeLocations.remove(location);
        idToLocation.remove(id);
    }
    static int reconnaissanceHome() {
        int antCount = Integer.parseInt(st.nextToken());

        int l = 0, r = homeLocations.last();
        int minTime = r+1;
        while(l <= r) {
            int mid = l + (r-l)/2;

            if(canReconnaissance(antCount, mid)) {      // 정찰이 가능한지
                minTime = Math.min(mid, minTime);
                r = mid-1;
            } else {
                l = mid+1;
            }
        }
        return minTime;
    }
    static boolean canReconnaissance(int antCount, int target) {
        int prev = -1;
        int next = -1;
        for(int location : homeLocations) {
            if(prev == -1) {    // 아직 할당되지 않음
                prev = location;
                next = location + target;
                if(--antCount < 0) return false;
                continue;
            }

            // 이미 할당되어 있는 경우
            if(next < location) {
                prev = location;
                next = prev + target;

                if(--antCount < 0) return false;
            }
        }
        return true;
    }
}
