package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 양팔저울_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static int[] chu;
    static int[] gu;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        chu = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) chu[i] = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(br.readLine().trim());
        gu = new int[m];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<m; i++) gu[i] = Integer.parseInt(st.nextToken());

        canMakeSameWeight();
    }
    static final int INF = 987654321;
    static void canMakeSameWeight() {
        int max = 0;
        for(int i : chu) max += i;

        boolean[] dp = new boolean[max+1];
        dp[0] = true;
        for(int w : chu) {
            boolean[] next = new boolean[max+1];
            for(int i=max; i>=0; i--) {
                if(!dp[i]) continue;
                next[i] = true;
                if(w + i <= max) next[w+i] = true;      // 현재 i에서 w를 추가했을 때
                next[Math.abs(i-w)] = true;             // 현재 i에서 w를 뺏을 때 (i-w or w-i)
            }
            dp = next;
        }
        StringBuilder sb = new StringBuilder();
        for(int i : gu) {
            if(i > max || !dp[i]) sb.append('N');
            else if(dp[i]) sb.append('Y');
            sb.append(' ');
        }
        System.out.println(sb);
    }
}
