package feb.week3.swea;

import java.util.*;
import java.io.*;

public class 수영장_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for(int i=1; i<tc+1; i++) {
            sb.append('#').append(i).append(' ').append(init()).append('\n');
        }
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int[] prices;
    static int[] month;
    static int init() throws IOException {
        prices = new int[4];            // [1일, 1달, 3달, 1년]
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<4; i++) prices[i] = Integer.parseInt(st.nextToken());

        month = new int[13];            // [0 ~ 11]
        st = new StringTokenizer(br.readLine().trim());
        for(int i=1; i<13; i++) month[i] = Integer.parseInt(st.nextToken());

        // 모두 1일 이용권을 이용했을 때
        // 1달 이용권을 이용했을 때 (1일 이용권 요금이, 한달 이용권보다 비쌀 경우만)
        int[] oneMonthPay = new int[13];
        for(int i=1; i<13; i++) {
            oneMonthPay[i] = Math.min(prices[0] * month[i], prices[1]);
        }
        // 3달 이용권 -> 1달 이용권의 누적 비용을 기반으로 최소비용 계산
        int[] bestPrice = new int[13];          // 1-based 로
        for(int i=1; i<13; i++) {
            /**
             * 이전 달까지의 최적 값 + 이번달 1달 이용권 요금
             */
            bestPrice[i] = bestPrice[i-1] + oneMonthPay[i];
            if (i >= 3) {
                /**
                 * [이전 달까지의 최적 값 + 이번달 1달 이용권 요금] vs. [이번달을 포함한 3개월 이용권 + 이번달 - 3달 전 최적 값]
                 * 
                 * 각 id 마다, 분기처리 적용
                 */
                bestPrice[i] = Math.min(bestPrice[i], bestPrice[i - 3] + prices[2]);
            }
        }

        return Math.min(prices[3], bestPrice[12]);
    }
}
