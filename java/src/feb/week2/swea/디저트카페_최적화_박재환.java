package feb.week2.swea;

import java.util.*;
import java.io.*;

public class 디저트카페_최적화_박재환 {
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
                findMaxRoute(x, y, 0, new int[2], 0);
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
    static void findMaxRoute(int x, int y, int seqId, int[] seq, int count) {
        /**
         * seqId : 탐색 순서
         * seq : 탐색 길이 [0] == [2], [1] == [3]
         */
        List<Integer> backup = new ArrayList<>();
        int len = 0;
        if(seqId == 2) {    // [0] 과 [1] 설정 완료
            /**
             *  더 이상 재귀 호출하지 않고 이번턴에 끝내기
             */
            // [2]
            while(true) {     // 격자 내에서만 유효
                /**
                 * x, y를 이동시켜가며 각 길이마다 다음 seqId로 재귀호출
                 */
                x += dx[seqId];
                y += dy[seqId];
                if(x < 0 || y < 0 || x >= n || y >= n) break;
                int dessert = map[x][y];
                if(desserts[dessert]) break;

                desserts[dessert] = true;
                count++;
                backup.add(dessert);
                if(++len == seq[0]) break;
            }
            if(len == seq[0]) {
                len = 0;
                seqId++;
                // [3]
                while(true) {     // 격자 내에서만 유효
                    /**
                     * x, y를 이동시켜가며 각 길이마다 다음 seqId로 재귀호출
                     */
                    x += dx[seqId];
                    y += dy[seqId];
                    if(x < 0 || y < 0 || x >= n || y >= n) break;
                    int dessert = map[x][y];
                    if(desserts[dessert]) break;

                    desserts[dessert] = true;
                    count++;
                    backup.add(dessert);
                    if(++len == seq[1]) {
                        maxDesserts = Math.max(maxDesserts, count);
                        break;
                    }
                }
            }
            for(int i : backup) desserts[i] = false;
            return;
        }
        /**
         * 현재 위치 x, y에서 현재 seqId 순서의 dx, dy로 탐색
         */
        while(true) {     // 격자 내에서만 유효
            /**
             * x, y를 이동시켜가며 각 길이마다 다음 seqId로 재귀호출
             */
            x += dx[seqId];
            y += dy[seqId];
            if(x < 0 || y < 0 || x >= n || y >= n) break;
            int dessert = map[x][y];
            if(desserts[dessert]) break;

            desserts[dessert] = true;
            count++;
            backup.add(dessert);
            seq[seqId] = (++len);

            findMaxRoute(x, y, seqId+1, seq, count);
        }
        for(int i : backup) desserts[i] = false;
    }
}
