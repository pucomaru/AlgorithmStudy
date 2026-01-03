package jan.week1.boj;

import java.util.*;
import java.io.*;

public class 벽부수고이동하기4_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, m;
    static char[][] map;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        map = new char[n][m];
        for(int x=0; x<n; x++) {
            String input = br.readLine().trim();
            for(int y=0; y<m; y++) {
                map[x][y] = input.charAt(y);
            }
        }

        for(char[] arr : map) System.out.println(Arrays.toString(arr));
    }

    static void findEmptySpace() {
        for(int x=0; x<n; x++) {
            for(int y=0; y<m; y++) {

            }
        }
    }
}
