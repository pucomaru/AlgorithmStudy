package feb.week1.swea;

import java.util.*;
import java.io.*;

public class 홈방범서비스_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for(int i=1; i<tc+1; i++) {
            sb.append('#').append(i).append(' ');
            init();
            sb.append('\n');
        }
        br.close();
        System.out.println(sb);
    }
    /**
     *  각 집들은 M의 비용을 지불할 수 있다.
     *  손해를 보지 않는 한 최대한 많은 집에 서비스를 제공하려한다.
     */
    static StringTokenizer st;
    static int n, m;
    static int[][] map;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());           // 도시 크기 N x N (최대 20 * 20)
        m = Integer.parseInt(st.nextToken());           // 한 집이 낼 수 있는 최대 비용 (최대 10)
        map = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) map[x][y] = Integer.parseInt(st.nextToken());
        }
        int result = solution();
        sb.append(result);
    }
    /**
     * 마름모의 범위는 중심좌표(x, y)로부터 멘헤튼거리만큼
     */
    static int solution() {
        int maxInclude = 0;
        /**
         * 모든 좌표를 기준으로 방범 서비스 설치
         */
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                for(int k=1; k<2*n; k++) {
                    int cost = getCost(k);
                    int include = 0;

                    for(int x1=0; x1<n; x1++) {
                        for(int y1=0; y1<n; y1++) {
                            if(map[x1][y1] == 0) continue;
                            if(Math.abs(x-x1) + Math.abs(y-y1) < k) include++;
                        }
                    }

                    if(cost <= include*m) maxInclude = Math.max(include, maxInclude);
                }
            }
        }
        return maxInclude;
    }

    static int getCost(int k) {
        return k*k + (k-1)*(k-1);
    }
}
