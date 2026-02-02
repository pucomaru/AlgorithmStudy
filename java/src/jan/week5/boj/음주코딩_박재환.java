package jan.week5.boj;

import java.util.*;
import java.io.*;

public class 음주코딩_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(init(br));
        br.close();
    }

    /**
     * 총 N개의 정수로 이루어진 수열이 있다.
     * 게임은 총 K 번 이루어진다.
     *
     * 매 라운드마다 명령은 아래와 같다.
     * - 변경 : 수열의 한 값을 바꾼다.
     * - 곱셈 : i ~ j 까지의 곱을 구한다. (양수, 음수, 0 판별)
     *
     * 변경과 곱셈이 번갈아 나오는게 아니므로 Lazy Segment Tree 사용?
     * => 구간에 대한 변경이 아니여서, 일반적인 SegmentTree 사용
     */
    static int arrLen, roundCnt;
    static int[] arr;
    static long[] tree;
    static String init(BufferedReader br) throws IOException {
        StringTokenizer st;
        StringBuilder sb = new StringBuilder();
        String testcase;
        while((testcase = br.readLine()) != null) {
            testcase = testcase.trim();
            if(testcase.isEmpty()) continue;
            st = new StringTokenizer(testcase);
            arrLen = Integer.parseInt(st.nextToken());
            roundCnt = Integer.parseInt(st.nextToken());
            arr = new int[arrLen];
            tree = new long[4 * arrLen];
            st = new StringTokenizer(br.readLine().trim());
            for (int i = 0; i < arrLen; i++) arr[i] = Integer.parseInt(st.nextToken());
            // -------------- solution ----------------
            makeTree(1, 0, arrLen - 1);
            while (roundCnt-- > 0) {
                st = new StringTokenizer(br.readLine().trim());
                String cmd = st.nextToken();

                switch (cmd) {
                    case "C": {     // 변경
                        int targetId = Integer.parseInt(st.nextToken()) - 1;
                        int targetValue = Integer.parseInt(st.nextToken());
                        update(1, 0, arrLen - 1, targetId, targetValue);
                        break;
                    }
                    case "P": {     // 곱셈
                        int i = Integer.parseInt(st.nextToken()) - 1;
                        int j = Integer.parseInt(st.nextToken()) - 1;
                        long multi = query(1, 0, arrLen - 1, i, j);
                        sb.append(multi == 0 ? '0' : multi > 0 ? '+' : '-');
                        break;
                    }
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    static long makeTree(int id, int l, int r) {
        if(l == r) {      // 리프노드인경우, arr 값 그대로 할당
            return tree[id] = arr[l] == 0 ? 0 : arr[l] > 0 ? 1 : -1;
        }
        // 계속해서 자식 노드로 내려가야하는 경우
        int mid = l + (r-l)/2;
        return tree[id] = makeTree(2*id, l, mid) * makeTree(2*id+1, mid+1, r);
    }

    static void update(int id, int l, int r, int targetId, int targetValue) {
        if(targetId < l || targetId > r) return;        // 범위 내에 포함되지 않는 경우

        // 범위에 포함되는 경우
        if(l == r) {        // targetId 에 도달
            tree[id] = targetValue == 0 ? 0 : targetValue > 0 ? 1 : -1;
            arr[targetId] = targetValue == 0 ? 0 : targetValue > 0 ? 1 : -1;
            return;
        }
        // 탐색을 이어가야하는 경우
        int mid = l + (r-l)/2;
        update(2*id, l, mid, targetId, targetValue);
        update(2*id+1, mid+1, r, targetId, targetValue);
        tree[id] = tree[2*id] * tree[2*id+1];
    }
    static long query(int id, int l, int r, int i, int j) {
        if(r < i || l > j) return 1;        // 범위를 아예 벗어나는 경우
        if(i <= l && j >= r) return tree[id];       // 범위를 완전하게 포함하는 경우
        int mid = l + (r-l)/2;
        return query(2*id, l, mid, i, j) * query(2*id+1, mid+1, r, i, j);
    }
}
