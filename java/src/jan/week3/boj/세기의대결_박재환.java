package jan.week3.boj;

import java.util.*;
import java.io.*;

public class 세기의대결_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * 총알의 숫자가 점차적으로 증가해야한다.
     * -> LIS (최장증가부분수열)
     * - 1. DP (n**2)
     * - 2. 이분탐색 (nlog n)
     */
    static StringTokenizer st;
    static int n;
    static int[][] bullets;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());     // 최대 500
        bullets = new int[2][n];
        for(int i=0; i<2; i++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int j=0; j<n; j++) bullets[i][j] = Integer.parseInt(st.nextToken());
        }

        System.out.println(findWinner());
    }

    /**
     * n 의 범위가 500 이니, n**2 방식인 DP 로 풀어도 충분히 시간 내에 동작한다.
     */
    static final String SUFFIX = "Win!";
    static String findWinner() {
        int yj = getLisLength(0);
        int hg = getLisLength(1);

        if(yj == hg) return "Both "+SUFFIX;

        return (yj > hg ? "YG " : "HG ") + SUFFIX;
    }

    static int getLisLength(int idx) {
        int[] lisArr = new int[n];

        for(int i=0; i<n; i++) {
            lisArr[i] = 1;      // 자기 자신을 포함하는 길이 ( 1 )
            for(int j=0; j<i; j++) {
                if(bullets[idx][i] > bullets[idx][j]) {     // 증가하는 부분수열이라면
                    lisArr[i] = Math.max(lisArr[i], lisArr[j]+1);       // 이전 위치들의 최장 증가 부분 수열의 길이와 비교
                }
            }
        }

        return getMaxLen(lisArr);
    }

    static int getMaxLen(int[] lisArr) {
        int max = 1;
        for(int i : lisArr) max = Math.max(i, max);
        return max;
    }
}
