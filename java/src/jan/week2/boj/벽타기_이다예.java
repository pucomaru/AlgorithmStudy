package jan.week2.boj;

import java.io.*;
import java.util.*;

// Deque 문제
// 그냥 Queue로 하면 안됨 .. Queue는 모든 간선 비용이 동일할떄
// 근데 이 문제는 간선이 0 or 1임
public class 벽타기_이다예 {

    static BufferedReader br;
    static StringTokenizer st;
    static int H,W;

    // 지도 정보 ( #, ., S,E )
    static int[][] map;
    // 최단거리만 DP
    static int[][] dist;
    // 주변에 벽이 있는지
    static boolean[][] wallMap;

    // 이동 ( 동 남 서 북 )
    static int[] dx = {1,0,-1,0};
    static int[] dy = {0,-1,0,1};

    public static void main(String[] args) throws IOException {
        final int INF =1_000_000_000;

        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        H = Integer.parseInt(st.nextToken());
        W = Integer.parseInt(st.nextToken());
        map = new int[H][W];
        dist = new int[H][W];
        wallMap = new boolean[H][W];
        // 시작점
        int startRow = 0;
        int startCol = 0;
        // 도착점
        int endRow = 0;
        int endCol = 0;

        // 지도 정보 저장 and 시작점 도착점 찾기 and dist에 최댓값저장
        for (int i = 0; i < H ; i++){
            String line = br.readLine();
            for (int j = 0; j < W ; j++){
                char now = line.charAt(j);
                map[i][j] = now;
                dist[i][j] = INF;
                if (now == 'S') {
                    startRow = i;
                    startCol = j;
                }
                if (now == 'E'){
                    endRow = i;
                    endCol = j;
                }
            }
        }

        // 주변에 벽이 있는지 정보 저장
        for (int r = 0; r < H ; r ++){
            for (int c = 0; c < W ; c++){
              for (int d = 0; d < 4 ; d++){
                  int ny = r + dy[d];
                  int nx = c + dx[d];

                  if (nx < 0 || nx >= W || ny < 0 || ny >= H) continue;
                  if (map[ny][nx] == '#') wallMap[r][c] = true;
              }
            }
        }

        int result = bfs(startRow,startCol,endRow,endCol);

        System.out.println(result);

    }


    static int bfs(int startRow,int startCol,int endRow,int endCol) {
        Deque<int[]> dq = new ArrayDeque<>();

        dq.addFirst(new int[]{startRow, startCol});
        dist[startRow][startCol] = 0;

        while (dq.size() != 0) {
            int[] now = dq.pollFirst();

            int nowR = now[0];
            int nowC = now[1];

            if (nowR == endRow && nowC == endCol) return dist[nowR][nowC];

            for (int d = 0; d < 4; d++) {
                int ny = nowR + dy[d];
                int nx = nowC + dx[d];

                // 가려고 하는 칸이 맵 인덱스를 벗어났을 경우 또는 벽일 경우 못감
                if (ny < 0 || ny >= H || nx < 0 || nx >= W || (map[ny][nx] == '#')) continue;

                // 지금 칸도 벽 인접 칸이고 다음 칸도 벽 인접 칸일경우는 이동 시간 +1 안해도됨
                // 하나라도 인접칸이 아닐 시 이동 시간 +1
                int near = (wallMap[ny][nx] && wallMap[nowR][nowC]) ? 0 : 1;
                int nd = dist[nowR][nowC] + near;

                if (nd < dist[ny][nx]) {
                    dist[ny][nx] = nd;
                    if (near == 0) dq.addFirst(new int[]{ny, nx});
                    else dq.addLast(new int[]{ny, nx});
                }
            }

        }
        return dist[endRow][endCol];
    }
}
