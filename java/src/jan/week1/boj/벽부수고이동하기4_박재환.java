package jan.week1.boj;

import java.util.*;
import java.io.*;

public class 벽부수고이동하기4_박재환 {
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

        findEmptySpace();
        int[][] result = createNewMap();
        for(int x=0; x<n; x++) {
            for(int y=0; y<m; y++) {
                sb.append(result[x][y]);
            }
            sb.append('\n');
        }
    }

    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static Map<Character, Integer> spaceCounts;
    static void findEmptySpace() {
        char c = 'a';
        spaceCounts = new HashMap<>();
        for(int x=0; x<n; x++) {
            for(int y=0; y<m; y++) {
                if(map[x][y] != '0') continue;
                int spaceCount = searchNearArea(x, y, c);
                spaceCounts.put(c++, spaceCount);
            }
        }
    }

    static int searchNearArea(int x, int y, char c) {
        Queue<int[]> q = new ArrayDeque<>();
        q.offer(new int[] {x, y});
        map[x][y] = c;
        int areaCount = 1;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0];
            int curY = cur[1];

            for(int dir=0; dir<4; dir++) {
                int nx = curX + dx[dir];
                int ny = curY + dy[dir];

                if(isNotMap(nx, ny)) continue;
                if(map[nx][ny] != '0') continue;

                map[nx][ny] = c;
                areaCount++;
                q.offer(new int[] {nx, ny});
            }
        }
        return areaCount;
    }

    static boolean isNotMap(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= m;
    }

    static int[][] createNewMap() {
        int[][] newMap = new int[n][m];

        for(int x=0; x<n; x++) {
            for(int y=0; y<m; y++) {
                if(map[x][y] != '1') newMap[x][y] = 0;
                else {
                    newMap[x][y] = getTotalNearSpaceCount(x, y);
                }
            }
        }

        return newMap;
    }

    static int getTotalNearSpaceCount(int x, int y) {
        Set<Character> visitedSpace = new HashSet<>();
        int totalNearSpaceCount = 1;

        for(int dir=0; dir<4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];

            if(isNotMap(nx, ny) || map[nx][ny] == '1') continue;
            if(!visitedSpace.add(map[nx][ny])) continue;
            totalNearSpaceCount += spaceCounts.get(map[nx][ny]);
        }

        return totalNearSpaceCount%10;
    }
}
