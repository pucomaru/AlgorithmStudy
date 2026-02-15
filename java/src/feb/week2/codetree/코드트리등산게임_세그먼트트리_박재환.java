package feb.week2.codetree;

import java.util.*;
import java.io.*;

public class 코드트리등산게임_세그먼트트리_박재환 {
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
     * 등산을 통한 산 사이 이동은 현재 위치보다 오른쪽에 위치한 산으로만 이동 가능하다.
     * 등산을 통한 이동에는 현재 산보다 더 높은 산으로만 이동이 가능하다.
     * => LIS
     *
     * 케이블 카는 특정 산에서만 탈 수 있다.
     * 현재 위치를 포함한 임의의 산으로 이동 가능하다. (높이 상관 X)
     * 케이블 카를 탄 이후에도 등산을 이어간다.
     *
     * 오르막 이동에 성공할 때마다 1,000,000 점
     * 케이블 카 이동 성공 시 1,000,000 점
     * 최종 위치한 산의 높이만큼 점수 획득
     */
    static final int BASE = 1_000_000;
    static final int MAX_HEIGHT = 1_000_000;

    static StringTokenizer st;
    static void init() throws IOException {
        int q = Integer.parseInt(br.readLine().trim());
        while(q-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());

            switch(cmd) {
                case 100: {
                    // [빅뱅]
                    bigbang();
                    break;
                }
                case 200: {
                    // [우공이산]
                    addMountain();
                    break;
                }
                case 300: {
                    // [지진]
                    removeMountain();
                    break;
                }
                case 400: {
                    // [등산 시뮬레이션]
                    sb.append(getScore()).append('\n');
                    break;
                }
            }
        }
    }
    static List<Integer> mountains; // 산 높이 저장
    static List<Integer>[] dpList;  // 높이별 LIS 길이
    static List<Integer> dp;        // 각 인덱스의 LIS
    static int[] tree;              // 각 구간별 최대 LIS
    static int[] idTree;            // 각 구간별 최대 Height
    static void bigbang() {
        /**
         * 높이가 최대 1 ~ 1,000,000
         */
        mountains = new ArrayList<>();
        dpList = new List[MAX_HEIGHT+1];
        for(int i=1; i<MAX_HEIGHT+1; i++) {
            dpList[i] = new ArrayList<>();
            dpList[i].add(0);
        }
        dp = new ArrayList<>();
        tree = new int[4*(MAX_HEIGHT+1)];
        idTree = new int[4*(MAX_HEIGHT+1)];

        int n = Integer.parseInt(st.nextToken());
        for(int i=0; i<n; i++) mountains.add(Integer.parseInt(st.nextToken()));

        /**
         * 초기 세그먼트 트리 구성
         */
        for(int i=0; i<n; i++) {
            int curH = mountains.get(i);
            /**
             * [query] curH 이전까지 최대 LIS 구하기
             */
            int prevLis = query(1, 1, MAX_HEIGHT, 1, curH-1);
            int curLis = prevLis+1;
            dpList[curH].add(curLis);
            dp.add(curLis);
            update(1, 1, MAX_HEIGHT, curH, curH, curLis);
        }
    }
    static void addMountain() {
        int height = Integer.parseInt(st.nextToken());
        mountains.add(height);

        int prevLis = query(1, 1, MAX_HEIGHT, 1, height-1);
        int curLis = prevLis+1;
        dpList[height].add(curLis);
        dp.add(curLis);

        update(1, 1, MAX_HEIGHT, height, height, curLis);
    }
    static void removeMountain() {
        int height = mountains.remove(mountains.size()-1);      // 삭제할 높이
        dpList[height].remove(dpList[height].size()-1);
        dp.remove(dp.size()-1);
        /**
         * height 에서 최대 LIS 길이 업데이트
         */
        update(1, 1, MAX_HEIGHT, height, height, dpList[height].get(dpList[height].size()-1));
    }

    static long getScore() {
        /**
         * (케이블 카 위치까리 LIS + 전체 LIS) * BASE + 마지막 산 높이
         */
        int cableMountain = Integer.parseInt(st.nextToken())-1;
        // 케이블 카까지 이동 + 전체 lis 이동 + 케이블 카 이용 성공
        // lis 는 지나온 산의 개수 이므로 -1 을 해줘야 이동 횟수
        int score = (dp.get(cableMountain)-1 + tree[1]-1+1)*BASE;
        score += idTree[1];
        return score;
    }
    static int query(int id, int l, int r, int s, int e) {
        if(r < s || l > e) return 0;
        if(l >= s && r <= e) return tree[id];

        int mid = l + (r-l)/2;
        return Math.max(query(2*id, l, mid, s, e), query(2*id+1, mid+1, r, s, e));
    }
    static void update(int id, int l, int r, int s, int e, int v) {
        if(r < s || l > e) return;
        if(l >= s && r <= e) {
            tree[id] = v;
            idTree[id] = s;
            return;
        }
        int mid = l + (r-l)/2;
        update(2*id, l, mid, s, e, v);
        update(2*id+1, mid+1, r, s, e, v);

        if(tree[2*id] <= tree[2*id+1]) {
            /**
             * 이진트리 특성상
             * 오른쪽에 있는 노드가 늘 부모노드보다 큰 범위를 가짐
             */
            tree[id] = tree[2*id+1];
            idTree[id] = idTree[2*id+1];
        } else {
            tree[id] = tree[2*id];
            idTree[id] = idTree[2*id];
        }
    }
}
