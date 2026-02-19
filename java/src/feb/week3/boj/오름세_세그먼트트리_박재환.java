package feb.week3.boj;

import java.util.*;
import java.io.*;

public class 오름세_세그먼트트리_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st;
        String input;
        while((input = br.readLine()) != null) {
            int n = Integer.parseInt(input.trim());
            int[] arr = new int[n];
            st = new StringTokenizer(br.readLine().trim());
            for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());
            solution(n, arr);
        }
        br.close();
    }
    static int[] tree;
    static void solution(int n, int[] arr) {
        /**
         * 좌표 압축
         */
        compress(arr);
        tree = new int[4*size];
        /**
         * arr 원소를 순차적으로 처리한다.
         * 1. a 의 이전위치까지 최장 증가 부분 수열을 구한다.
         * 2. 1에서 구한 값에 + 1을 한 값이 현재 수의 최장증가 부분 수열이다.
         */
        for(int i=0; i<n; i++) {
            int num = compressArr[i];
            /**
             * num 이전까지 최장 증가 수열을 가져온다.
             */
            int prevLis = query(1, 1, size, 1, num-1);
            int curLis = prevLis + 1;
            update(1, 1, size, num, num, curLis);
        }
        System.out.println(tree[1]);
    }
    static int[] compressArr;
    static int size;
    static void compress(int[] arr) {
        int id = 0;
        int[] temp = arr.clone();

        Arrays.sort(temp);
        Map<Integer, Integer> map = new HashMap<>();
        for(int i : temp) {
            if(!map.containsKey(i)) {
                map.put(i, ++id);
            }
        }

        compressArr = new int[arr.length];
        size = id;
        for(int i=0; i<arr.length; i++) {
            compressArr[i] = map.get(arr[i]);
        }
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
            return;
        }
        int mid = l + (r-l)/2;
        update(2*id, l, mid, s, e, v);
        update(2*id+1, mid+1, r, s, e, v);
        tree[id] = Math.max(tree[2*id], tree[2*id+1]);
    }
}
