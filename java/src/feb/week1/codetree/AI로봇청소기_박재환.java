package feb.week1.codetree;

import java.util.*;
import java.io.*;

public class AI로봇청소기_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    /**
     *  N x N
     *  - 먼지 O
     *  - 먼지 X
     *  - 물건
     *
     *  먼지가 있는 공간은 1~100사이의 먼지 양을 갖는다.
     *
     *  [청소기 이동]
     *  이동거리가 가장 가까운 오염된 격자로 이동한다. (청소기 순서대로, 동시 X)
     *  물건 혹은 청소기가 있는 격자로 지나갈 수 없다.
     *  가까운 격자가 여러 개, (행 번호 작은 것 -> 열 번호 작은 것)
     *
     *  [청소]
     *  바라보고 있는 방향 기준, 본인이 위치한 격자, 왼쪽, 오른쪽, 위쪽 을 청소할 수 있다.
     *  4가지 방향 중 가장 큰 방향에서 청소를 시작한다.
     *  격자별 최대 청소 가능 20
     *  합이 여러개 (오른쪽 > 아래 > 왼쪽 > 위쪽)
     *
     *  [먼지축적]
     *  먼지가 있는 모든 격자에 동시에 5씩 추가된다
     *
     *  [먼지확산]
     *  깨끗한 격자에 주변 4방향 격자의 먼지량 합을 10으로 나눈 값만큼 먼지가 확산
     *  편의상 나눗셈 과정에 생기는 소숫점 아래 수는 버린다.
     *  모든 깨끗한 격자에 대해 동시에 확산이 이루어진다.
     *
     *  [출력]
     *  전체 공간의 총 먼지량을 출력한다.
     *  먼지가 있는 곳이 없으면 0을 출력한다.
     */
    static StringTokenizer st;
    static int n, k, l;
    static int[][] board;
    static Robot[] robots;
    static boolean[][] isAllocated;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());           // 격자 크기
        k = Integer.parseInt(st.nextToken());           // 로봇 청소기 개수
        l = Integer.parseInt(st.nextToken());           // 테스트 횟수
        board = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }
        robots = new Robot[k];
        isAllocated = new boolean[n][n];
        for(int i=0; i<k; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken())-1;
            int y = Integer.parseInt(st.nextToken())-1;
            isAllocated[x][y] = true;
            robots[i] = new Robot(x, y);
        }

        solution();
    }
    static class Robot {
        int x, y;

        Robot(int x, int y) {
            this.x = x;
            this.y = y;
        }

        void updateLocation(int[] newLocation) {
            this.x = newLocation[0];
            this.y = newLocation[1];
        }
    }
    static int[] dx = {0,-1,0,1};
    static int[] dy = {-1,0,1,0};
    static void solution() {
        while(l-- > 0) {
            /**
             * [청소기이동]
             */
            moveRobots();
            /**
             * [청소]
             */
            clean();
            /**
             * [먼지축적]
             */
            accDust();
            /**
             * [먼지확산]
             */
            spreadDust();
            /**
             * [출력]
             */
            printDust();
        }
    }
    static void printDust() {
        int sum = 0;
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(board[x][y] < 1) continue;
                sum += board[x][y];
            }
        }
        sb.append(sum).append('\n');
    }
    static void spreadDust() {
        int[][] tempDust = new int[n][n];
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(board[x][y] != 0) continue;
                int sum = 0;
                for(int dir=0; dir<4; dir++) {
                    int nx = x + dx[dir];
                    int ny = y + dy[dir];
                    if(isNotBoard(nx, ny) || board[nx][ny] < 1) continue;
                    sum += board[nx][ny];
                }
                tempDust[x][y] += sum/10;
            }
        }
        for(int x=0; x<n; x++) {
            for (int y = 0; y < n; y++) {
                board[x][y] += tempDust[x][y];
            }
        }
    }
    static void accDust() {
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(board[x][y] < 1) continue;
                board[x][y] += 5;
            }
        }
    }
    static int[] prioritySearchDir = {2,3,0,1};
    static void clean() {
        /**
         * 바라보고 있는 방향으로 [현위치, 왼쪽, 오른쪽, 위쪽] 을 청소 가능
         * -> 먼지량의 합이 가장 큰 방향에서 청소를 시작한다.
         * 오른쪽 -> 아래쪽 -> 왼쪽 -> 위쪽
         */
        for(Robot robot : robots) {
            // 현재 로봇이 청소할 방향을 결정
            int dir = decisionCleanDir(robot);
            if(dir == -1) continue;
            doClean(robot.x, robot.y, dir);
        }
    }
    static void doClean(int x, int y, int dir) {
        board[x][y] = Math.max(board[x][y]-20, 0);
        // 위
        int nx = x + dx[dir];
        int ny = y + dy[dir];
        if(!isNotBoard(nx, ny) && board[nx][ny] > 0) board[nx][ny] = Math.max(board[nx][ny]-20, 0);
        // 왼쪽
        nx = x + dx[(dir-1+4)%4];
        ny = y + dy[(dir-1+4)%4];
        if(!isNotBoard(nx, ny) && board[nx][ny] > 0) board[nx][ny] = Math.max(board[nx][ny]-20, 0);
        // 오른쪽
        nx = x + dx[(dir+1)%4];
        ny = y + dy[(dir+1)%4];
        if(!isNotBoard(nx, ny) && board[nx][ny] > 0) board[nx][ny] = Math.max(board[nx][ny]-20, 0);
    }
    static int decisionCleanDir(Robot robot) {
        int maxSum, maxDir;
        maxSum = 0;
        maxDir = -1;
        for(int id=0; id<4; id++) {
            int dir = prioritySearchDir[id];
            int sum = getSum(robot.x, robot.y, dir);

            if(maxSum < sum) {
                maxSum = sum;
                maxDir = dir;
            }
        }
        return maxDir;
    }
    static int getSum(int x, int y, int dir) {
        int sum = Math.min(board[x][y], 20);
        // 위 (앞)
        int nx = x + dx[dir];
        int ny = y + dy[dir];
        if(!isNotBoard(nx, ny) && board[nx][ny] > 0) sum += Math.min(board[nx][ny], 20);
        // 왼쪽
        nx = x + dx[(dir-1+4)%4];
        ny = y + dy[(dir-1+4)%4];
        if(!isNotBoard(nx, ny) && board[nx][ny] > 0) sum += Math.min(board[nx][ny], 20);
        // 오른쪽
        nx = x + dx[(dir+1)%4];
        ny = y + dy[(dir+1)%4];
        if(!isNotBoard(nx, ny) && board[nx][ny] > 0) sum += Math.min(board[nx][ny], 20);
        return sum;
    }
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
    static void moveRobots() {
        /**
         * 순서대로 청소기를 움직인다.
         */
        for(int robotId=0; robotId<k; robotId++) {
            Robot robot = robots[robotId];
            if(board[robot.x][robot.y] > 0) continue;
            int[] newLocation = findNearLocation(robotId);
            if(newLocation == null) continue;
            isAllocated[robot.x][robot.y] = false;
            robot.updateLocation(newLocation);
            isAllocated[robot.x][robot.y] = true;
        }
    }
    static int[] findNearLocation(int robotId) {
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];

        Robot robot = robots[robotId];
        q.offer(new int[] {robot.x, robot.y, 0});
        visited[robot.x][robot.y] = true;

        int bestX, bestY, bestDist;
        bestX = bestY = n;
        bestDist = n*n+5;
        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int dist = cur[2];
            if(bestDist < dist) break;
            for(int dir=0; dir<4; dir++) {
                int nx = x+dx[dir];
                int ny = y+dy[dir];
                if(isNotBoard(nx, ny)) continue;
                if(visited[nx][ny] || isAllocated[nx][ny] || board[nx][ny] == -1) continue;
                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny, dist+1});
                if(board[nx][ny] > 0) {     // 먼지가 있는 칸이라면
                    if(bestDist > dist + 1) {
                        bestX = nx;
                        bestY = ny;
                        bestDist = dist + 1;
                    } else if(bestDist == dist + 1) {
                        if(bestX > nx || (bestX == nx && bestY > ny)) {
                            bestX = nx;
                            bestY = ny;
                        }
                    }
                }
            }
        }
        if(bestX == n && bestY == n) return null;
        return new int[] {bestX, bestY};
    }
}
