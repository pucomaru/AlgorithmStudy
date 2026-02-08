package feb.week1.boj;

import java.util.*;
import java.io.*;

public class 운동_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static final int INF = 10000 * 400 + 5;
    static StringTokenizer st;
    static int v, e;
    static int[][] map;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        v = Integer.parseInt(st.nextToken());
        e = Integer.parseInt(st.nextToken());
        map = new int[v][v];
        for(int from=0; from<v; from++) {
            for(int to=0; to<v; to++) {
                if(from == to) map[from][to] = 0;
                else map[from][to] = INF;
            }
        }
        for(int i=0; i<e; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken())-1;
            int to = Integer.parseInt(st.nextToken())-1;
            int dist = Integer.parseInt(st.nextToken());
            map[from][to] = dist;
        }
        int result = solution();
        System.out.println(result);
    }
    static int solution() {
        findAllDist();
        int minDist = INF;
        for(int from=0; from<v; from++) {
            for(int to=from+1; to<v; to++) {
                if(minDist > map[from][to] + map[to][from]) {
                    minDist = map[from][to] + map[to][from];
                }
            }
        }
        return minDist == INF ? -1 : minDist;
    }
    static void findAllDist() {
        for(int mid=0; mid<v; mid++) {
            for(int from=0; from<v; from++) {
                for(int to=0; to<v; to++) {
                    if(map[from][to] > map[from][mid] + map[mid][to]) {
                        map[from][to] = map[from][mid] + map[mid][to];
                    }
                }
            }
        }
    }
}
