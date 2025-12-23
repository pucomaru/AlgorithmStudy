package dec_4.boj;

import java.util.*;
import java.io.*;

public class 장난감섞기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * N개의 수열이 있다.
     * 각 수열의 길이는 M으로 동일하다.
     * 수열끼리의 순서만 마음대로 배치할 수 있다.
     */
    static StringTokenizer st;
    static int n, m;
    static PriorityQueue<Pointer> leftList;
    static PriorityQueue<Pointer> rightList;
    static List<Pointer> mustAdd;
    static int result;
    static void init() throws IOException {
        result = 0;
        leftList = new PriorityQueue<>((a, b) -> {      // 왼쪽에 놓일 배열 [a < b]]
            if(a.rightMax == b.rightMax) return Integer.compare(b.leftMax, a.leftMax);
            return Integer.compare(b.rightMax, a.rightMax);
        });
        rightList = new PriorityQueue<>((a, b) -> {     // 오른쪽에 놓일 배열 [a > b]
            if(a.leftMax == b.leftMax) return Integer.compare(b.rightMax, a.rightMax);
            return Integer.compare(b.leftMax, a.leftMax);
        });
        mustAdd = new ArrayList<>();
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        for(int i=0; i<n; i++) {
            int[] arr = new int[m];
            st = new StringTokenizer(br.readLine().trim());
            for(int j=0; j<m; j++) arr[j] = Integer.parseInt(st.nextToken());
            Pointer p = new Pointer(arr);
            if(p.leftMax == p.rightMax) mustAdd.add(p);
            else if(p.leftMax < p.rightMax) {
                leftList.offer(p);
            } else {
                rightList.offer(p);
            }
        }

//        for(Pointer p : leftList) System.out.printf("[%d, %d] ", p.leftMax, p.rightMax);
//        for(Pointer p : mustAdd) System.out.printf("%d ", p.totalSum);
//        for(Pointer p : rightList) System.out.printf("[%d, %d] ", p.leftMax, p.rightMax);

        getLeftMax();
        getRightMax();
        getTotalSum();
        System.out.println(result);
    }

    static void getLeftMax() {
        if(leftList.isEmpty()) return;
        Pointer firstPointer = leftList.poll();
        int max = Math.max(firstPointer.totalSum, firstPointer.rightMax);
        while(!leftList.isEmpty()) {
            Pointer p = leftList.poll();
            // 1. 이전 값 + 왼쪽
            int case1 = Math.max(max, max+p.leftMax);
            // 3. 이전값 + 전체
            int case2 = Math.max(max, max+p.totalSum);
            // 4. 다 버리고 max(왼쪽, 오른쪽)
            int case3 = p.rightMax;
            // 2. 이전값
            max = Math.max(max, Math.max(Math.max(case1, case2), case3));
        }
        if(max >= 0) result += max;
    }

    static void getRightMax() {
        if(rightList.isEmpty()) return;
        Pointer firstPointer = rightList.poll();
        int max = Math.max(firstPointer.leftMax, firstPointer.totalSum);
        while(!rightList.isEmpty()) {
            Pointer p = rightList.poll();
            // 1. 이전 값 + 왼쪽
            int case1 = Math.max(max, max+p.leftMax);
            // 3. 이전값 + 전체
            int case2 = Math.max(max, max+p.totalSum);
            // 4. 다 버리고 max(왼쪽, 오른쪽)
            int case3 = p.leftMax;
            // 2. 이전값
            max = Math.max(max, Math.max(Math.max(case1, case2), case3));
        }
        if(max >= 0) result += max;
    }

    static void getTotalSum() {
        for(Pointer p : mustAdd) {
            if(p.totalSum >= 0) result += p.totalSum;
        }
    }

    static class Pointer {
        int leftMax;
        int rightMax;
        int totalSum;
        int[] arr;

        public Pointer(int[] arr) {
            this.arr = arr;
            getLRPointer();
        }

        private void getLRPointer() {
            int sum = arr[0];
            int max = sum;
            for(int i=1; i<arr.length; i++) {
                sum += arr[i];
                max = Math.max(sum, max);
            }
            this.leftMax = max;

            sum = arr[arr.length-1];
            max = sum;
            for(int i=arr.length-2; i>-1; i--) {
                sum += arr[i];
                max = Math.max(sum, max);
            }
            this.rightMax = max;

            totalSum = 0;
            for(int i : arr) {
                totalSum+=i;
            }
        }
    }
}
