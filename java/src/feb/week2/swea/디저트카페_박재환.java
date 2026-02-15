package feb.week2.swea;

import java.util.*;
import java.io.*;

public class 디저트카페_박재환 {
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
    static StringTokenizer st;
    static int n;
    static int[][] map;
    static boolean[] desserts;
    static int maxDesserts;
    static void init() throws IOException {
        desserts = new boolean[101];        // 1 ~ 100 이 디저트
        maxDesserts = -1;
        n = Integer.parseInt(br.readLine().trim());
        map = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) map[x][y] = Integer.parseInt(st.nextToken());
        }
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                int dessert = map[x][y];
                desserts[dessert] = true;
                findMaxRoute(x, y, x, y, 0, 1);
                desserts[dessert] = false;
            }
        }
        sb.append(maxDesserts);
    }
    /**
     * 이동은 대각선으로만 가능하다.
     * 사각형으로 이동해야한다. -> 가로1 == 가로2, 세로1 == 세로2
     * 같은 종류의 디저트가 있으면 안된다.
     *
     * 이동 가능한 경로는 1개
     */
    static int[] dx = {-1,-1, 1, 1};
    static int[] dy = {-1, 1, 1, -1};
    static void findMaxRoute(int sx, int sy, int x, int y, int seqId, int count) {
        /**
         * seqId : 탐색 순서
         * seq : 탐색 길이 [0] == [2], [1] == [3]
         */
        if(seqId == 4) return;

        /**
         * 현재 위치 x, y에서 현재 seqId 순서의 dx, dy로 탐색
         */
        List<Integer> backup = new ArrayList<>();
        while(true) {     // 격자 내에서만 유효
            /**
             * x, y를 이동시켜가며 각 길이마다 다음 seqId로 재귀호출
             */
            x += dx[seqId];
            y += dy[seqId];
            if(x < 0 || y < 0 || x >= n || y >= n) break;
            if(seqId == 3 && sx == x && sy == y) {      // 마지막에 순회하고 돌아오는 경우
                maxDesserts = Math.max(maxDesserts, count);
                break;
            }
            int dessert = map[x][y];
            if(desserts[dessert]) break;        // 같은 디저트를 먹은 경우
            desserts[dessert] = true;
            backup.add(dessert);
            findMaxRoute(sx, sy, x, y, seqId+1, ++count);
        }
        for(int i : backup) desserts[i] = false;
    }
}
