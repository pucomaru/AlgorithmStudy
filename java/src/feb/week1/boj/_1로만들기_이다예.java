package feb.week1.boj;

import java.io.*;

public class _1로만들기_이다예 {
    static final int MAX_NUM = Integer.MAX_VALUE;
    static BufferedReader br;
    static int N;
    static int result;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());

        result = MAX_NUM;
        dp(N,0);
        System.out.println(result);
    }

    static void dp(int now,int count) {
        if (result < count ) return;
        if (now == 1) result = count;

        if(now%3 == 0 ) dp(now/3,count+1);
        if(now%2 == 0) dp(now/2,count+1);
        dp(now-1,count+1);
    }

}
