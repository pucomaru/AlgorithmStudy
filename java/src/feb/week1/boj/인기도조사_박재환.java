package feb.week1.boj;

import java.util.*;
import java.io.*;

public class 인기도조사_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static final int SECOND = 1;
    static final int MINUTE = 60;
    static final int HOUR = 60*60;
    /**
     *  티비를 시청한 구간
     *  HH:MM:SS - HH:MM:SS
     */
    static StringTokenizer st;
    static long[] timeStamp;
    static long[] prefix;
    static void init() throws IOException {
        timeStamp = new long[HOUR*24+1];
        int inputCount = Integer.parseInt(br.readLine().trim());
        while(inputCount-- > 0) {
            st = new StringTokenizer(br.readLine().trim(), " :-");
            int s = convertToSecond();
            int e = convertToSecond();
            if (s <= e) {
                timeStamp[s]++;
                timeStamp[e + 1]--;
            } else {
                // 자정 넘김
                timeStamp[s]++;
                timeStamp[HOUR*24]--;
                timeStamp[0]++;
                timeStamp[e + 1]--;
            }
        }

        for(int i=1; i<HOUR*24; i++) timeStamp[i] += timeStamp[i-1];
        prefix = new long[HOUR*24+1];
        for(int i=0; i<HOUR*24; i++) prefix[i+1] = prefix[i] + timeStamp[i];

        int outputCount = Integer.parseInt(br.readLine().trim());
        while(outputCount-- > 0) {
            st = new StringTokenizer(br.readLine().trim(), " :-");
            int s = convertToSecond();
            int e = convertToSecond();
            long sum;
            int len;

            if (s <= e) {
                sum = prefix[e + 1] - prefix[s];
                len = e - s + 1;
            } else {
                sum = (prefix[HOUR*24] - prefix[s]) + prefix[e + 1];
                len = (HOUR*24 - s) + (e + 1);
            }

            sb.append(String.format("%.10f", (double) sum / len)).append('\n');
        }
    }
    static int convertToSecond() {
        int h = Integer.parseInt(st.nextToken()) * HOUR;
        int m = Integer.parseInt(st.nextToken()) * MINUTE;
        int s = Integer.parseInt(st.nextToken()) * SECOND;
        return h+m+s;
    }
}
