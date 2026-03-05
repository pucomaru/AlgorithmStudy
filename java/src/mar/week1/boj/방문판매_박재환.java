package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 방문판매_박재환 {
    /**
     * 두 제품을 주어진 할당량 만큼 N 명의 고객의 집을 모두 방문해 팔아야한다.
     * i번 위치 집에 방문했을 때, 판매할 수 있는 제품 양이 각각 x[i], y[i] 로 주어진다.
     *
     * 1번 고객부터, N번 고객까지 오름차순으로 한 번씩 방문해야한다.
     *
     * 최소 몇 명의 고객에게 팔아야 할당량을 채울 수 있는지
     */
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static class Customer {
        int id, cx, cy;

        Customer(int id, int cx, int cy) {
            this.id = id;
            this.cx = cx;
            this.cy = cy;
        }
    }
    static StringTokenizer st;
    static int n, x, y;
    static Customer[] customers;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        x = Integer.parseInt(st.nextToken());
        y = Integer.parseInt(st.nextToken());

        customers = new Customer[n];
        for(int i=0; i<n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int cx = Integer.parseInt(st.nextToken());
            int cy = Integer.parseInt(st.nextToken());
            Customer customer = new Customer(i+1, cx, cy);
            customers[i] = customer;
        }

        sellProducts();
    }
    static final int INF = 1_000_009;
    static void sellProducts() {
        int[][] dp = new int[x+1][y+1];
        int[][] last = new int[x+1][y+1];
        for(int i=0; i<x+1; i++) {
            Arrays.fill(dp[i], INF);
            Arrays.fill(last[i], -1);
        }
        dp[0][0] = 0;
        for(Customer c : customers) {
           for(int i=x; i>=0; i--) {
               for(int j=y; j>=0; j--) {
                   if(dp[i][j] == INF) continue;

                   int ni = Math.min(x, i + c.cx);
                   int nj = Math.min(y, j + c.cy);
                   if(dp[ni][nj] > dp[i][j] + 1) {
                       dp[ni][nj] = dp[i][j] + 1;
                       last[ni][nj] = c.id;
                   }
               }
           }
        }
        if(dp[x][y] == INF) System.out.println(-1);
        else {
            System.out.println(dp[x][y]);
            System.out.println(last[x][y]);
        }

    }
}
