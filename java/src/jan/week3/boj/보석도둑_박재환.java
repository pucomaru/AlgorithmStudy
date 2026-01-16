package jan.week3.boj;

import java.util.*;
import java.io.*;

public class 보석도둑_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, k;        // 보석, 가방
    static int[][] diamonds;
    static int[] bags;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        diamonds = new int[n][2];
        for(int i=0; i<n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int weight = Integer.parseInt(st.nextToken());
            int price = Integer.parseInt(st.nextToken());

            diamonds[i] = new int[] {weight, price};
        }
        bags = new int[k];
        for(int i=0; i<k; i++) bags[i] = Integer.parseInt(br.readLine().trim());

        System.out.println(getMaxPrice());
    }

    static long getMaxPrice() {
        long totalPrice = 0;
        Arrays.sort(diamonds, (a, b) -> Integer.compare(a[0], b[0]));       // 보석들을 무게가 가벼운순으로 오름차순 정렬
        Arrays.sort(bags);

        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> Integer.compare(b, a));       // 가격을 PQ 에 저장 ( 가격이 높은 순으로 )
        int diamondIdx = 0;
        for(int bag : bags) {
            while(diamondIdx < n && bag >= diamonds[diamondIdx][0]) pq.offer(diamonds[diamondIdx++][1]);

            while(!pq.isEmpty()) {
                totalPrice += pq.poll();
                break;
            }
        }

        return totalPrice;
    }
}
