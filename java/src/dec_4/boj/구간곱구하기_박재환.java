package dec_4.boj;

import java.util.*;
import java.io.*;

public class 구간곱구하기_박재환 {
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
     * 어떤 N 개의 수가 주어져 있다.
     * 중간에 수의 변경이 빈번히 일어난다. -> 세그먼트 트리
     */
    static StringTokenizer st;
    static int n, m, k;
    static int[] numArr;
    static long[] segmentTree;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());   // 수의 개수
        numArr = new int[n];
        segmentTree = new long[4*n];
        m = Integer.parseInt(st.nextToken());   // 변경 횟수
        k = Integer.parseInt(st.nextToken());   // 구간 곱 개수
        int totalCommandCnt = m + k;
        // 수 입력 받기
        for(int idx=0; idx<n; idx++) numArr[idx] = Integer.parseInt(br.readLine().trim());
        // 세그먼트 트리 생성
        initSegmentTree(0, n-1, 1);
        // 변경 입력
        while(totalCommandCnt-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int commandType = Integer.parseInt(st.nextToken());
            switch(commandType) {
                // 1-based -> 0-based 로 통일
                case 1: {    // 교체
                    int targetIdx = Integer.parseInt(st.nextToken())-1;
                    int value = Integer.parseInt(st.nextToken());
                    updatedSegmentTree(0, n-1, 1, targetIdx, value);
                    break;
                }
                case 2: {     // 구간 곱
                    int left = Integer.parseInt(st.nextToken())-1;
                    int right = Integer.parseInt(st.nextToken())-1;
                    sb.append(query(0, n-1, 1, left, right)).append('\n');
                    break;
                }
            }
        }

    }

    static final long MOD = 1_000_000_007;
    static long initSegmentTree(int start, int end, int segmentIdx) {
        if(start == end) return segmentTree[segmentIdx] = numArr[start];   // 리프노드

        int mid = start + (end-start)/2;
        long left = initSegmentTree(start, mid, segmentIdx*2);
        long right = initSegmentTree(mid+1, end, segmentIdx*2+1);
        // 하위 노드의 곱
        return segmentTree[segmentIdx] = (left * right) % MOD;
    }

    static long updatedSegmentTree(int start, int end, int segmentIdx, int targetIdx, int value) {
        // 변경하려는 노드의 범위가 아닌 경우
        if(targetIdx < start || targetIdx > end) return segmentTree[segmentIdx];
        // 리프 노드인 경우
        if(start == end) return segmentTree[segmentIdx] = value;

        int mid = start + (end-start) / 2;
        long left = updatedSegmentTree(start, mid, segmentIdx*2, targetIdx, value);
        long right = updatedSegmentTree(mid+1, end, segmentIdx*2+1, targetIdx, value);
        return segmentTree[segmentIdx] = (left*right) % MOD;
    }

    static long query(int start, int end, int segmentIdx, int left, int right) {
        if(start > right || end < left) return 1;   // 곱을 구하는 문제이기에 0이 아닌 1 반환
        if(left <= start && end <= right) return segmentTree[segmentIdx];

        int mid = start + (end - start) / 2;
        long l = query(start, mid, segmentIdx*2, left, right);
        long r = query(mid+1, end, segmentIdx*2+1, left, right);
        return (l*r) % MOD;
    }
}
