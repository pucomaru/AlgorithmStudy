package jan.week1.boj;

import java.util.*;
import java.io.*;

public class 포항항_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static int n, m;
    static char[][] map;
    static ArrayDeque<int[]> stores;      // s : 0, 이외 1 based
    static int minTime, storeCount;
    static void init() throws IOException {
        minTime = Integer.MAX_VALUE;
        stores = new ArrayDeque<>();
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        map = new char[n+2][m+2];
        for(int x=0; x<n+2; x++) Arrays.fill(map[x], 'X');

        char storeIdx = '1';
        for(int x=1; x<n+1; x++) {
            String input = br.readLine().trim();
            for(int y=1; y<m+1; y++) {
                map[x][y] = input.charAt(y-1);
                if(map[x][y] == 'S') {
                    stores.offerFirst(new int[] {x, y});
                    map[x][y] = '0';
                }
                else if(map[x][y] == 'K') {
                    stores.offerLast(new int[] {x, y});
                    map[x][y] = storeIdx++;
                }
            }
        }
        storeCount = stores.size();
        findAllDist();
        int[] seq = new int[6];
        visitSeq(1, seq, new boolean[storeCount], 0);
        System.out.println(minTime == Integer.MAX_VALUE ? -1 : minTime);
    }

    static void visitSeq(int selected, int[] seq, boolean[] isUsed, int time) {
        if(selected == 6) {
            minTime = Math.min(minTime, time);
            return;
        }

        for(int i=1; i< storeCount; i++) {
            if(isUsed[i]) continue;
            if(allDist[seq[selected-1]][i] == -1) continue;
            time += allDist[seq[selected-1]][i];
            isUsed[i] = true;
            seq[selected] = i;
            if(time < minTime ) {
                visitSeq(selected+1, seq, isUsed, time);
            }
            isUsed[i] = false;
            time -= allDist[seq[selected-1]][i];
        }
    }

    static int[][] allDist;
    static void findAllDist() {
        allDist = new int[stores.size()][stores.size()];
        for(int i=0; i<stores.size(); i++) Arrays.fill(allDist[i], -1);
        for(int i=0; i<storeCount; i++) {    // i 번째 위치에서 모든 위치로의 거리
            allDist[i][i] = 0;
            findMinDist(i, stores.poll());
        }
    }

    static void findMinDist(int sIdx, int[] s) {
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n+2][m+2];
        q.offer(new int[] {s[0], s[1], 0});
        visited[s[0]][s[1]] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0];
            int curY = cur[1];
            int curTime = cur[2];

            if(map[curX][curY] != 'X' && map[curX][curY] != '.') {
//                System.out.printf("sIdx : %d, toIdx : %d, curTime : %d\n", sIdx, map[curX][curY]-'0', curTime);
                allDist[sIdx][map[curX][curY]-'0'] = curTime;
            }

            for(int d=0; d<4; d++) {
                int nx = curX + dx[d];
                int ny = curY + dy[d];

                if(map[nx][ny] == 'X' || visited[nx][ny]) continue;

                q.offer(new int[] {nx, ny, curTime+1});
                visited[nx][ny] = true;
            }
        }
    }
}
