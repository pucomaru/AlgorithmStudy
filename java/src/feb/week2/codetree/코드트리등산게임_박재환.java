package feb.week2.codetree;

import java.util.*;
import java.io.*;

public class 코드트리등산게임_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     *  지도에는 산들이 연속으로 나열되어 있다.
     *  각 산은 높이를 갖는다.
     *
     *  [이동 조건]
     *  산 이동은 현재 위치보다 오른쪽에 위치한 산으로만 이동할 수 있다.
     *  현재 산 높이보다 더 높은 산으로만 이동할 수 있다.
     *
     *  [케이블 카]
     *  특정 산에서만 탈 수 있다.
     *  현재 위치를 포함한 임의의 산으로 이동할 수 있고, 높이는 상관 없다.
     *  케이블 카를 탄 이후에도 등산을 이어간다.
     *
     *  [시뮬레이션]
     *  시작할 산을 자유롭게 선택가능하다.
     *  현재 위치보다 오른쪽에 위치한 산으로 이동에 성공할 때마다 1,000,000점을 얻는다.
     *  케이블 카를 탈 수 있는 산에 도착했다면 케이블 카를 이용한다. -> 이용에 성공하면 1,000,000점을 얻는다.
     *  최종적으로 위치한 산의 높이만큼 점수를 얻는다.
     */
    static StringTokenizer st;
    static void init() throws IOException {
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int command = Integer.parseInt(st.nextToken());

            switch(command) {
                case 100:{
                    /**
                     *  n, h[1],...h[n]
                     *  n 개의 산으로 이루어져 있으며, 왼쪽부터 높이가 주어진다.
                     */
                    makeMountain();
                    break;
                }
                case 200:{
                    /**
                     * 기존 산의 오른쪽에 h 높이를 갖는 산을 추가한다.
                     */
                    addMountain();
                    break;
                }
                case 300:{
                    /**
                     * 기존 산들 중, 가장 오른쪽에 위치한 산을 제거한다.
                     */
                    removeMountain();
                    break;
                }
                case 400:{
                    /**
                     * 케이블카를 이용할 수 있는 산의 정보 m 이 주어진다.
                     * 등산 시뮬레이션 중 얻을 수 있는 최대 점수를 출력한다.
                     */
                    break;
                }
            }
        }

    }
    static int n;
    static List<Integer> prevMountain;  // 현재 산의 이전 산 정보
    static List<Integer> nextMountain;  // 현재 산의 다음 산 정보
    static List<Integer> mountains;     // 산의 높이
    static void makeMountain() {
        n = Integer.parseInt(st.nextToken());
        mountains = new ArrayList<>();
        prevMountain = new ArrayList<>();
        nextMountain = new ArrayList<>();
        // 1-based
        mountains.add(-1);
        prevMountain.add(-1);
        nextMountain.add(-1);

        for(int mountainId=1; mountainId<n+1; mountainId++) {
            int height = Integer.parseInt(st.nextToken());
            mountains.add(height);
            prevMountain.add(mountainId-1);
            nextMountain.add(mountainId+1);
        }

        /**
         * 처음과 마지막 산 연결 끊기
         */
        prevMountain.set(1, -1);
        nextMountain.set(n, -1);
    }
    static void addMountain() {
        int height = Integer.parseInt(st.nextToken());
        int mountainId = mountains.size();

        mountains.add(height);
        prevMountain.add(mountainId-1);
        nextMountain.add(mountainId+1);

        /**
         * 기존 산에 연결 추가
         */
        nextMountain.set(mountainId-1, mountainId);
    }
    static void removeMountain() {
        int mountainId = mountains.size()-1;
        mountains.set(mountainId, -1);      // 가장 오른쪽 위치의 산 삭제
        /**
         * 현재 산과 연결된 이전, 이후 산이 있다면 둘이 연결 처리
         */
        int prev = prevMountain.get(mountainId);
        int next = nextMountain.get(mountainId);

        if(prev != -1) nextMountain.set(prev, next);
        if(next != -1) prevMountain.set(next, prev);
    }

}
