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
                    break;
                }
                case 300:{
                    /**
                     * 기존 산들 중, 가장 오른쪽에 위치한 산을 제거한다.
                     */
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
    static int[] mountains;     // 산의 높이
    static void makeMountain() {
        n = Integer.parseInt(st.nextToken());
        mountains = new int[n];
        for(int i=0; i<n; i++) mountains[i] = Integer.parseInt(st.nextToken());

    }
    static int[] getLis() {
        int[] compress = compress();
        int maxValue = 0;
        for(int i : compress) maxValue = Math.max(maxValue, i);

        SegmentTree segmentTree = new SegmentTree(maxValue);
        int[] dp = new int[n];

        for(int i=0; i<n; i++) {
            int height = compress[i];

            int best = 0;
            if(height > 1) {        // 나보다 작은 최대 DP 찾기
                best = segmentTree.query(1, 1, maxValue, 1, height-1);
            }

            dp[i] = best+1;
            segmentTree.update(1, 1, maxValue, height, height, dp[i]);
        }
        return dp;
    }
    static int[] getReverseLis() {
        int[] compress = compress();
        int maxValue = 0;
        for(int i : compress) maxValue = Math.max(maxValue, i);

        SegmentTree segmentTree = new SegmentTree(maxValue);
        int[] dp = new int[n];

        for(int i=n-1; i>-1; i--) {
            int height = compress[i];

            int best = 0;
            if(height < maxValue) {     // 나보다 큰 위치의 DP 찾기
                best = segmentTree.query(1, 1, maxValue, height+1, maxValue);
            }

            dp[i] = best+1;
            segmentTree.update(1, 1, maxValue, height, height, dp[i]);
        }
        return dp;
    }
    static int[] compress() {
        /**
         * 좌표 압축
         */
        int[] temp = new int[n];
        for(int i=0; i<n; i++) temp[i] = mountains[i];
        Arrays.sort(temp);

        Map<Integer, Integer> map = new HashMap<>();
        int id = 1;

        for(int i : temp) {
            if(!map.containsKey(i)) {
                map.put(i, id++);
            }
        }
        int[] result = new int[n];
        for(int i=0; i<n; i++) result[i] = map.get(mountains[i]);
        return result;
    }
    static class SegmentTree {
        /**
         * tree[height]
         * : 현재 height 에서의 LIS 최대값
         */
        int size;
        int[] tree;

        SegmentTree(int size) {
            this.size = size;
            this.tree = new int[4*size];
        }
        int query(int id, int l, int r, int s, int e) {
            if(r < s || l > e) return 0;      // 범위에 완전하게 포함되지 않음
            if(l >= s && r <= e) {          // 범위에 완전하게 포함됨
                return tree[id];
            }
            int mid = l + (r-l)/2;
            return Math.max(query(2*id, l ,mid, s, e), query(2*id+1, mid+1, r, s, e));
        }
        void update(int id, int l, int r, int s, int e, int targetValue) {
            /**
             * 현재 높이에서 최대 값(LIS) 구하기
             */
            if(r < s || l > e) return;      // 범위에 완전하게 포함되지 않음
            if(l >= s && r <= e) {          // 범위에 완전하게 포함됨
                tree[id] = Math.max(tree[id], targetValue);
                return;
            }
            int mid = l + (r-l)/2;
            update(2*id, l, mid, s, e, targetValue);
            update(2*id+1, mid+1, r, s, e, targetValue);
            tree[id] = Math.max(tree[2*id], tree[2*id+1]);
        }
    }
}
