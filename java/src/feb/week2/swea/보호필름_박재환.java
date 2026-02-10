package feb.week2.swea;

import java.util.*;
import java.io.*;

public class 보호필름_박재환 {
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
     *  각 셀들은 A / B 특성을 가지고 있다.
     *
     *  합격 기준 K
     *  세로 방향 셀들의 특성이 중요하다.
     *  => 단면의 모든 새로방향에 대해서 동일한 특성의 셀들이 K개 이상 연속적으로 있는 경우 통과
     *
     *  약품 사용
     *  막(열) 별로 투입할 수 있다.(가로방향 한 줄)
     *  약품을 투입하면 모든 셀들은 하나 이상의 특성으로 변한다.
     *
     *  => 약품 투입 욋수를 최소로 해 성능 검사를 통과할 수 있는 방법을 찾는다.
     */
    static StringTokenizer st;
    static int d, w, k;
    static int minInject;
    static void init() throws IOException {
        minInject = 15;

        st = new StringTokenizer(br.readLine().trim());
        d = Integer.parseInt(st.nextToken());       // 두께 (최대 13)
        w = Integer.parseInt(st.nextToken());       // 길이 (최대 20)
        k = Integer.parseInt(st.nextToken());       // 합격 기준
        int[][] film = new int[d][w];
        for(int x=0; x<d; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<w; y++) film[x][y] = Integer.parseInt(st.nextToken());
        }
        /**
         * 필름의 두께가 최대 13 -> 약품 투입 경우의 수 3**13 -> 완전탐색 가능
         */
        findMinInject(0, 0, film);
        sb.append(minInject);
    }
    static void findMinInject(int selectedId, int inject, int[][] film) {
        // 이전 최적해가 더 좋은 결과
        if(minInject <= inject) return;
        // 현재 상태로 검사를 통과할 수 있는지
        if(isPass(film)) {
            minInject = Math.min(minInject, inject);
            return;
        }
        // 더 이상 경우가 없는 경우
        if(selectedId == d) return;
        // 현재 막에 약품을 투입하거나, 하지 않거나
        // 약품 투입 X
        findMinInject(selectedId+1, inject, film);
        // 약품 투입 ( A or B )
        findMinInject(selectedId+1, inject+1, inject(selectedId, 0, film));
        findMinInject(selectedId+1, inject+1, inject(selectedId, 1, film));
    }
    static boolean isPass(int[][] film) {
        if(k==1) return true;       // 반드시 true

        for(int y=0; y<w; y++) {
            /**
             *  연속으로 3개 나오면 OK
             */
            boolean pass = false;
            int type = film[0][y];
            int acc = 1;
            for(int x=1; x<d; x++) {
                if(type == film[x][y]) {
                    if(++acc == k) {
                        pass = true;
                        break;
                    }
                } else {
                    type = film[x][y];
                    acc = 1;
                }
            }
            if(!pass) return false;
        }
        return true;
    }
    static int[][] inject(int row, int type, int[][] film) {
        int[][] copy = new int[d][w];
        for(int x=0; x<d; x++) {
            if(x == row) Arrays.fill(copy[x], type);
            else for(int y=0; y<w; y++) copy[x][y] = film[x][y];
        }
        return copy;
    }
}
