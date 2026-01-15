package jan.week3.boj;

import java.util.*;
import java.io.*;

public class 카드게임_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }

    static StringTokenizer st;
    static int cardCount, pickCount, turnCount;
    static int[] cards;
    static boolean[] isUsed;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        cardCount = Integer.parseInt(st.nextToken());
        pickCount = Integer.parseInt(st.nextToken());
        turnCount = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine().trim());
        cards = new int[pickCount];
        isUsed = new boolean[pickCount];
        for(int i=0; i<pickCount; i++) {
            int number = Integer.parseInt(st.nextToken());
            cards[i] = number;
        }
        Arrays.sort(cards);     // 오름차순으로 정렬

        st = new StringTokenizer(br.readLine().trim());
        while(turnCount-- > 0) {
            int cardNumber = Integer.parseInt(st.nextToken());
            int result = findMinCardNumber(cardNumber);

            for(; result<pickCount && isUsed[result]; result++);

            isUsed[result] = true;
            sb.append(cards[result]).append('\n');
        }
    }

    /**
     * target 보다 큰 가장 작은 수
     * => upper bound
     */
    static int findMinCardNumber(int target) {
        int l=0, r=pickCount;

        while(l < r) {
            int mid = l + (r-l)/2;

            if(cards[mid] > target) {
                r = mid;
            } else l = mid+1;
        }

        return l;
    }
}
