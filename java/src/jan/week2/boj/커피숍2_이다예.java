package java.src.jan.week2.boj;

import java.io.*;
import java.util.*;


public class 커피숍2_이다예 {

    static BufferedReader br;
    static StringTokenizer st;

    // N : 수의 개수 / Q : 턴의 개수
    static int N , Q;
    // 게임 숫자 판
    static int[] numbers ;

    public static void main(String[] args) throws IOException {

        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());

        N = Integer.parseInt(st.nextToken());
        Q = Integer.parseInt(st.nextToken());

        numbers = new int[N];

        st = new StringTokenizer(br.readLine());

        // 처음 게임 세팅
        for (int i = 0; i < N ; i ++){
            numbers[i] = Integer.parseInt(st.nextToken());
        }

        // x ~ y 까지의 합을 구하고 a 번째 수를 b를 바꾼다 .
        // x > y인 경우 y번째 부터 x번째
        for (int q = 0 ; q < Q ; q++){
            st = new StringTokenizer(br.readLine());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
        }

    }
}
