package feb.week3.codetree;

import java.util.*;
import java.io.*;

public class 미지의공간탈출_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * N x N 격지 내에 한 변의 길이가 M인 정육면체 형태의 시간의 벽이 세워져 있다.
     *
     * [타임머신 스캔]
     * - 미지의 공간의 평면도 : 위에서 내려다본 전체 맵
     * - 시간의 벽의 단면도 : 시간의 벽의 윗 변과 동서남북 네 면의 단면도
     *
     * 각 칸은 빈 공간(0)과 장애물(1)로 구성
     *
     * 타임머신은 시간의 벽의 윗면 어딘가 위치, 2로 표시된다.
     * 미지의 공간의 평면도에는 시간의 벽의 위치 3, 탈출구 4가 주어진다.
     *
     * 미지의 공간의 바닥에는 F개의 시간이상현상이 존재
     * - 매 v[i]의 배수 턴마다 방향 d[i]로 한 칸씩 확산된다.
     *
     * 오른쪽 0
     * 왼쪽 1
     * 아래쪽 2
     * 위쪽 3
     */
    static final int[] dx = {0, 0, 1, -1};
    static final int[] dy = {1, -1, 0, 0};

    static StringTokenizer st;
    static int n, m, f;
    static int[][] miziBoard;
    static int[][][] siganBoard;
    static int[][] strangeTime;
    static int siganTime;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        f = Integer.parseInt(st.nextToken());

        miziBoard = new int[n][n];
        for (int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for (int y = 0; y < n; y++) miziBoard[x][y] = Integer.parseInt(st.nextToken());
        }

        siganBoard = new int[5][m][m];      // 동 서 남 북 윗면
        for (int dir = 0; dir < 5; dir++) {
            for (int x = 0; x < m; x++) {
                st = new StringTokenizer(br.readLine().trim());
                for (int y = 0; y < m; y++) siganBoard[dir][x][y] = Integer.parseInt(st.nextToken());
            }
        }
        strangeTime = new int[f][4];
        for(int i=0; i <f; i++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int j=0; j<4; j++) strangeTime[i][j] = Integer.parseInt(st.nextToken());
        }
        /**
         * [1]
         * 시간의 벽면에서 미지의 평면으로의 최단거리
         *
         * [2]
         * 미지의 평면에서 시간 이상 현상을 피해 탈출구까지
         */
        siganTime = findMinTimeToMigiBoard();
        if(siganTime == -1) {
            System.out.println(-1);
            return;
        }
        int exitTime = findMintimeToExit(siganTime+1);
        if(exitTime == -1) {
            System.out.println(-1);
            return;
        }
        System.out.println(exitTime);
    }

    /**
     * [1]
     * 정육면체에서 평면과 이어지는 출구 찾기
     */
    static int[] entry;
    static int findMinTimeToMigiBoard() {
        /**
         * 윗면에서 출발위치(2) 찾기
         */
        int[] start = findSiganStartXY();
        /**
         * 정육면체와 평면의 통로 찾기
         */
        entry = entryMizi();
        /**
         * 정육면체에 연결 통로 정보 표시
         */
        int exitFace = entry[2];
        int exitX = m - 1;
        int exitY = entry[3];

        Queue<int[]> q = new ArrayDeque<>();
        boolean[][][] visited = new boolean[5][m][m];

        q.offer(new int[]{TOP, start[0], start[1], 0});
        visited[TOP][start[0]][start[1]] = true;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int face = cur[0];
            int x = cur[1];
            int y = cur[2];
            int time = cur[3];
            if(exitFace == face && exitX == x && exitY == y) return time;
            for (int dir = 0; dir < 4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];

                if (nx < 0 || ny < 0 || nx >= m || ny >= m) {
                    /**
                     * 면을 이동하는 경우
                     *
                     * 현재 면을 기준으로 하드코딩
                     */
                    if (face == TOP) {
                        if (nx < 0) {        // NORTH
                            int nFace = NORTH;
                            int nX = 0;
                            int nY = m - 1 - y;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        } else if (nx >= m) {    // SOUTH
                            int nFace = SOUTH;
                            int nX = 0;
                            int nY = ny;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        } else if (ny < 0) {     // WEST
                            int nFace = WEST;
                            int nX = 0;
                            int nY = x;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        } else if (ny >= m) {    // EAST
                            int nFace = EAST;
                            int nX = 0;
                            int nY = m - 1 - x;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        }
                    } else if (face == EAST) {
                        if (nx < 0) {        // TOP
                            int nFace = TOP;
                            int nX = m - 1 - y;
                            int nY = m - 1;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        } else if (nx >= m) {    // mizi -> 처리 X
                        } else if (ny < 0) {     // SOUTH
                            int nFace = SOUTH;
                            int nX = x;
                            int nY = m - 1;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        } else if (ny >= m) {    // NORTH
                            int nFace = NORTH;
                            int nX = nx;
                            int nY = 0;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        }
                    } else if (face == WEST) {
                        if (nx < 0) {        // TOP
                            int nFace = TOP;
                            int nX = ny;
                            int nY = 0;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        } else if (nx >= m) {    // mizi -> 처리 X
                        } else if (ny < 0) {     // NORTH
                            int nFace = NORTH;
                            int nX = nx;
                            int nY = m-1;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        } else if (ny >= m) {    // SOUTH
                            int nFace = SOUTH;
                            int nX = nx;
                            int nY = 0;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        }
                    } else if (face == SOUTH) {
                        if (nx < 0) {        // TOP
                            int nFace = TOP;
                            int nX = m - 1;
                            int nY = y;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        } else if (nx >= m) {    // mizi -> 처리 X
                        } else if (ny < 0) {     // WEST
                            int nFace = WEST;
                            int nX = nx;
                            int nY = m-1 ;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        } else if (ny >= m) {    // EAST
                            int nFace = EAST;
                            int nX = nx;
                            int nY = 0;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        }
                    } else if (face == NORTH) {
                        if (nx < 0) {        // TOP
                            int nFace = TOP;
                            int nX = 0;
                            int nY = m - 1 - y;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        } else if (nx >= m) {    // mizi -> 처리 X
                        } else if (ny < 0) {     // EAST
                            int nFace = EAST;
                            int nX = nx;
                            int nY = m - 1;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        } else if (ny >= m) {    // WEST
                            int nFace = WEST;
                            int nX = nx;
                            int nY = 0;

                            if (!visited[nFace][nX][nY] && siganBoard[nFace][nX][nY] == 0) {
                                visited[nFace][nX][nY] = true;
                                q.offer(new int[]{nFace, nX, nY, time+1});
                            }
                        }
                    }
                    continue;
                }
                if (visited[face][nx][ny] || siganBoard[face][nx][ny] == 1) continue;
                visited[face][nx][ny] = true;
                q.offer(new int[]{face, nx, ny, time+1});
            }
        }
        return -1;
    }

    static final int TOP = 4;
    static final int NORTH = 3;
    static final int SOUTH = 2;
    static final int WEST = 1;
    static final int EAST = 0;
    static int[] findSiganStartXY() {
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < m; y++) {
                if (siganBoard[4][x][y] == 2) return new int[]{x, y};
            }
        }
        return null;
    }

    static int[] entryMizi() {
        /**
         * 평면에서 정육면체가 차지하는 영역
         */
        int minX = n + 1, minY = n + 1, maxX = -1, maxY = -1;
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (miziBoard[x][y] == 3) {
                    minX = Math.min(x, minX);
                    maxX = Math.max(x, maxX);
                    minY = Math.min(y, minY);
                    maxY = Math.max(y, maxY);
                }
            }
        }

        int entryX = -1;
        int entryY = -1;
        int entryDir = -1;
        int offset = -1;
        for (int x = minX; x <= maxX; x++) {
            // WEST
            if (minY - 1 >= 0 && miziBoard[x][minY - 1] == 0) {
                entryX = x;
                entryY = minY - 1;
                entryDir = WEST;
                offset = x - minX;
                return new int[]{entryX, entryY, entryDir, offset};
            }
            // EAST
            if (maxY + 1 < n && miziBoard[x][maxY + 1] == 0) {
                entryX = x;
                entryY = maxY + 1;
                entryDir = EAST;
                offset = (m - 1) - (x - minX);
                return new int[]{entryX, entryY, entryDir, offset};
            }
        }

        for (int y = minY; y <= maxY; y++) {
            // NORTH
            if (minX - 1 >= 0 && miziBoard[minX - 1][y] == 0) {
                entryX = minX - 1;
                entryY = y;
                entryDir = NORTH;
                offset = (m - 1) - (y - minY);
                return new int[]{entryX, entryY, entryDir, offset};
            }
            // SOUTH
            if (maxX + 1 < n && miziBoard[maxX + 1][y] == 0) {
                entryX = maxX + 1;
                entryY = y;
                entryDir = SOUTH;
                offset = y - minY;
                return new int[]{entryX, entryY, entryDir, offset};
            }
        }

        return null;
    }
    static int findMintimeToExit(int time) {
        /**
         * 출구 위치 찾기
         */
        Queue<int[]> q = new ArrayDeque<>();
        int[] exit = findExit();
        int[][] timeTable = new int[n][n];
        int[][] visited = new int[n][n];
        for(int i=0; i<n; i++) {
            Arrays.fill(timeTable[i], Integer.MAX_VALUE);
            Arrays.fill(visited[i], Integer.MAX_VALUE);
        }
        timePropagation(timeTable);

        if(time >= timeTable[entry[0]][entry[1]]) return -1;        // 이동이 불가능한 경우
        q.offer(new int[] {entry[0], entry[1], time});
        visited[entry[0]][entry[1]] = time;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0];
            int curY = cur[1];
            int curTime = cur[2];

            if(curX == exit[0] && curY == exit[1]) return curTime;

            for(int dir=0; dir<4; dir++) {
                int nx = curX + dx[dir];
                int ny = curY + dy[dir];

                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(miziBoard[nx][ny] == 1 || miziBoard[nx][ny] == 3) continue;
                if(visited[nx][ny] <= curTime + 1 || timeTable[nx][ny] <= curTime + 1) continue;
                visited[nx][ny] = curTime + 1;
                q.offer(new int[] {nx, ny, curTime+1});
            }
        }

        return -1;
    }
    static int[] findExit() {
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(miziBoard[x][y] == 4) return new int[] {x, y};
            }
        }
        return null;
    }
    static void timePropagation(int[][] timeTable) {
        for(int[] strange : strangeTime) {
            int x = strange[0];
            int y = strange[1];
            int dir = strange[2];
            int time = strange[3];

            int depth = 1;
            timeTable[x][y] = 1;
            while(true) {
                x += dx[dir]; y += dy[dir];
                if(x < 0 || y < 0 || x >= n || y>= n) break;
                if(miziBoard[x][y] != 0) break;
                timeTable[x][y] = Math.min(depth * time, timeTable[x][y]);
                depth++;
            }
        }
    }
}
