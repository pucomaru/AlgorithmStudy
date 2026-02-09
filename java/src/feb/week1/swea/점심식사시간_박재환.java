package feb.week1.swea;

import java.util.*;
import java.io.*;

public class 점심식사시간_박재환 {
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
    }
    /**
     *  N x N
     *  - P : 사람
     *  - S : 계단 입구
     *
     *  모든 사람이 계단을 내려가 아래층으로 이동했을 때 = 이동 완료 시간
     *
     *  이동 시간 : |PR - SR| + |PC - SC|
     *  계단 내려가는 시간
     *  - 1분 후 아래칸으로 내려간다. (K분이 걸린다)
     *  - 동시에 최대 3명까지만 올라갈 수 있다.
     *
     *  계단은 반드시 2개이다.
     *  => 각 사람이 계단을 선택하는 경우 2**10 => 1024
     *
     */
    static StringTokenizer st;
    static int n;
    static int[][] map;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        map = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) {
                map[x][y] = Integer.parseInt(st.nextToken());
            }
        }


    }
}
