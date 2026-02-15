package feb.week2.codetree;

import java.util.*;
import java.io.*;

public class 코드트리등산게임_박재환 {
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
    static final int SCORE = 1_000_000;

    static StringTokenizer st;
    static void init() throws IOException {
        int q = Integer.parseInt(br.readLine().trim());

        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());

            switch(cmd) {
                case 100:{
                    initMountain();
                    break;
                }
                case 200:{
                    addMountain();
                    break;
                }
                case 300:{
                    removeMountain();
                    break;
                }
                case 400:{
                    sb.append(getScore()).append('\n');
                    break;
                }
            }
        }
    }
    static int mountainCount;
    static List<ArrayDeque<Integer>> mountains;
    static List<Integer> stack;
    static void initMountain() {
        mountains = new ArrayList<>();
        stack = new ArrayList<>();

        mountainCount = Integer.parseInt(st.nextToken());
        for(int i=0; i<mountainCount; i++) {
            /**
             * LIS 생성
             * - List<ArrayDeque<Integer>>
             *     => List.size() : LIS 길이
             *     => ArrayDeque  : 같은 LIS 길이의 원소들 (마지막 값이 가장 작은 값)
             */
            int height = Integer.parseInt(st.nextToken());
            int insertId = mountains.isEmpty() ? 0 : findInsertId(height);
            stack.add(insertId);
            if(insertId == mountains.size()) mountains.add(new ArrayDeque<>());         // 새로운 LIS 갱신
            mountains.get(insertId).offerLast(height);
        }
    }
    static void addMountain() {
        int height = Integer.parseInt(st.nextToken());
        int insertId = mountains.isEmpty() ? 0 : findInsertId(height);
        stack.add(insertId);
        if(insertId == mountains.size()) mountains.add(new ArrayDeque<>());         // 새로운 LIS 갱신
        mountains.get(insertId).offerLast(height);
    }
    static void removeMountain() {
        /**
         * lastLis : 마지막에 위치한 Height 의 LIS
         */
        int lastId = stack.size()-1;
        int lastLis = stack.get(lastId);
        mountains.get(lastLis).pollLast();
        if(mountains.get(lastLis).isEmpty()) mountains.remove(lastLis);     // 더 이상 해당 Level 에 원소가 없다면 -> 전체 LIS 가 1 감소
        stack.remove(lastId);
    }
    static long getScore() {
        int cableMountain = Integer.parseInt(st.nextToken())-1;
        long score = (stack.get(cableMountain) + mountains.size()) * SCORE;
        score += mountains.get(mountains.size()-1).peekFirst();
        return score;
    }
    static int findInsertId(int target) {
        /**
         * Lower Bound
         * (target <= VALUE)
         */
        int l=0, r=mountains.size();

        while(l < r) {
            int mid = l + (r-l)/2;
            int minValue = mountains.get(mid).peekLast();

            if(target <= minValue) r = mid;      // 정답에 포함될 수 있으므로 mid 포함
            else l = mid+1;
        }
        return l;
    }
}
