package feb.week2.boj;

import java.util.*;
import java.io.*;

public class 가장긴증가하는부분수열6_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static final long MOD = 1_000_000_007;

    static StringTokenizer st;
    static int n;
    static int[] arr;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());

        getLisBySegmentTree();
    }
    /**
     * [세그먼트 트리로 LIS 구하기]
     * - 각 위치별로 LIS 를 구할 수 있음
     * 
     * 1. 좌표 압축
     * 2. 값을 기준으로 세그먼트 트리 구성
     * 3. 각 값에 대해
     *      - 그보다 작은 값 범위의 LIS + 1 = 현재 LIS
     */
    static class Node {
        int lisLen;
        long lisCnt;

        Node(int lisLen, long lisCnt) {
            this.lisLen = lisLen;
            this.lisCnt = lisCnt;
        }
    }
    static int[] compressed;
    static int size;
    static void getLisBySegmentTree() {
        // [좌표압축]
        compress();
        // [값을 기준으로 세그먼트 트리 구성]
        buildTree();
        // [세그먼트 트리 업데이트]
        for(int i=0; i<n; i++) {
            int id = compressed[i];

            // 0 ~ id-1 구간까지 최대 LIS
            Node prevBest = query(1, 0, size-1, 0, id-1);

            // id 위치 최대 LIS 갱신
            int curLis = prevBest.lisLen+1;
            long curCnt = prevBest.lisLen == 0 ? 1 : prevBest.lisCnt;
            update(1, 0, size-1, id, id, new Node(curLis, curCnt));
        }

        sb.append(tree[1].lisLen).append(' ').append(tree[1].lisCnt);
    }

    static void compress() {
        int[] copy = new int[n];
        for(int i=0; i<n; i++) copy[i] = arr[i];
        Arrays.sort(copy);

        int id = 0;
        Map<Integer, Integer> map = new HashMap<>();

        for(int i : copy) {
            if(!map.containsKey(i)) map.put(i, id++);
        }

        compressed = new int[n];
        for(int i=0; i<n; i++) compressed[i] = map.get(arr[i]);
        size = id;
    }

    static Node[] tree;
    static void buildTree() {
        tree = new Node[4*size];
        for(int i=0; i<4*size; i++) tree[i] = new Node(0, 0);
    }

    static Node query(int id, int l, int r, int s, int e) {
        if(r < s || l > e) return new Node(0, 0);
        if(l >= s && r <= e) return tree[id];

        int mid = l + (r-l)/2;
        Node left = query(2*id, l, mid, s, e);
        Node right = query(2*id+1, mid+1, r, s, e);
        return merge(left, right);
    }
    static Node merge(Node a, Node b) {
        if(a.lisLen > b.lisLen) return a;
        if(b.lisLen > a.lisLen) return b;
        return new Node(a.lisLen, (a.lisCnt + b.lisCnt)%MOD);
    }

    static void update(int id, int l, int r, int s, int e, Node target) {
        if(r < s || l > e) return;
        if(l >= s && r <= e) {
            if(tree[id].lisLen == target.lisLen) {
                tree[id].lisCnt = (tree[id].lisCnt + target.lisCnt)%MOD;
            }else if(tree[id].lisLen < target.lisLen) {
                tree[id] = target;
            }
            return;
        }

        int mid = l + (r-l)/2;
        update(2*id, l, mid, s, e, target);
        update(2*id+1, mid+1, r, s, e, target);
        tree[id] = merge(tree[2*id], tree[2*id+1]);
    }
//-----------------------------------------------------------------------------------
    /**
     * [이분탐색으로 LIS 구하기]
     * - 각 위치별 LIS 를 구할 수 없음 -> 길이만 구할 수 있음
     */
    static List<ArrayDeque<Integer>> list;
    static void getLisByBinarySearch() {
        list = new ArrayList<>();

        for(int i=0; i<n; i++) {
            int target = arr[i];
            int insertId = list.isEmpty() ? 0 : findInsertId(target);
            if(insertId == list.size()) list.add(new ArrayDeque<>());
            list.get(insertId).offerLast(target);
        }

//        sb.append(list.size()).append(' ').append(list.get(list.size()-1).size());
    }
    static int findInsertId(int target) {
        int l = 0, r = list.size();
        while(l < r) {
            int mid = l + (r-l)/2;
            if(target <= list.get(mid).peekLast()) r = mid;
            else l = mid+1;
        }
        return l;
    }
}
