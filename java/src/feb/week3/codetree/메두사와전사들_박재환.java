package feb.week3.codetree;

import java.util.*;
import java.io.*;

public class 메두사와전사들_박재환 {
    /**
     * N x N
     * - 도로 0
     * - 도로 아님 1
     *
     * 메두사의 집 [sr, sc]
     * 공원 [er, ec]
     *
     * 메두사는 도로만 따라 최단 경로로 이동
     *
     * M 명의 전사
     * - 도로, 비도로 구분 없이 모든 칸 이동 가능
     *
     * 메두사가 전사들이 움직이기 전, 그들을 바라봄으로써 돌로 만들 수 있다.
     *
     * 1. 메두사 이동
     * - 도로를 따라 한 칸 이동
     * - 공원까지 최단 경로
     * - 이동한 칸에 전사가 있는 경우 -> 메두사가 전사 공격 -> 전사 사라짐
     * - 여러 최단 경로가 존재하는 경우 [상 하 좌 우] 우선 순위를 가짐
     * - 집에서 공원으로 도달할 수 있는 경우도 있음
     *
     * 2. 메두사 시선
     * - [상 하 좌 우] 하나의 방향을 선택해 바라봄
     * - 바라보는 방향으로 90도의 시야각을 갖는다.
     * - 다른 전사에게 가려진 전사는 보이지 않는다.
     * - 돌로 변한 전사는 이번 턴에는 움직이지 못하고, 다음 턴에 움직일 수 있다.
     * - 전사를 가장 많이 볼 수 있는 방향을 바라본다. [상 하 좌 우] 우선 순위를 갖는다.
     *
     * 3. 전사들의 이동
     * - 메두사를 향해 최대 두 칸까지 이동한다.
     * - 이동 중 전사들은 같은 칸을 공유할 수 있다.
     * 3-1. 첫 번째 이동
     * - 메두사와 거리를 줄일 수 있는 방향으로 한 칸 이동한다. [상 하 좌 우] 우선순위
     * - 메두사의 시야가 들어오는 곳으로 이동 불가
     * 3-2. 두 번째 이동
     * - 메두사와 거리를 줄일 수 있는 방향으로 한 칸 이동한다. [좌 우 상 하] 우선순위
     * - 메두사의 시야가 들어오는 곳으로 이동 불가
     *
     * 4. 전사의 공격
     * - 메두사를 공격하고 사라진다.
     */
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static class Monster {
        int x, y;

        Monster(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    static class Park {
        int x, y;

        Park(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    static class Worrier {
        int x, y;
        boolean stone;
        boolean live;

        Worrier(int x, int y) {
            this.x = x;
            this.y = y;
            this.stone = false;
            this.live = true;
        }
    }
    static void allocateMonster() {
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        monster = new Monster(x, y);
    }
    static void allcatePark() {
        int x = Integer.parseInt(st.nextToken());
        int y = Integer.parseInt(st.nextToken());
        park = new Park(x, y);
    }
    static void allocateWorrier() {
        worriers = new Worrier[m+1];
        for(int i=1; i<m+1; i++) {
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            worriers[i] = new Worrier(x, y);
        }
    }
    static void allocateBoard() throws IOException {
        board = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }
    }
    static StringTokenizer st;
    static int n, m;
    static int[][] board;
    static Monster monster;
    static Park park;
    static Worrier[] worriers;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 격자 크기
        m = Integer.parseInt(st.nextToken());       // 전사 수

        st = new StringTokenizer(br.readLine().trim());
        allocateMonster();
        allcatePark();

        st = new StringTokenizer(br.readLine().trim());
        allocateWorrier();

        allocateBoard();
        /**
         * 매 턴마다
         * 매 전사가 이동한 거리의 합, 돌이 된 전사의 수, 메두사를 공격한 전사의 수
         * 를 공백을 두고 출력
         *
         * 단 메두사가 공원에 도착하는 턴에는 0을 출력하고 프로그램을 종료한다.
         */
        if(!moveMonster()) {        // 공원에 도착하지 못하는 경우
            sb.append(-1);
            return;
        }
        /**
         * 메두사의 위치를 기준으로 턴을 기록
         */
        shortestPathToPark.pollFirst();                                 // 메두사의 이동을 시작 -> 첫 위치는 상관 X
        shortestPathToPark.pollLast();                                  // 공원에 도착하는 위치 제거 
        while(!shortestPathToPark.isEmpty()) {
            int[] monsterLocation = shortestPathToPark.pollFirst();     // 현재 메두사의 위치
            // 메두사 위치 업데이트
            monster.x = monsterLocation[0];
            monster.y = monsterLocation[1];
            /**
             * [메두사 시선]
             */
            int stoneWorrier = monsterEyeSight(monsterLocation[0], monsterLocation[1]);
            /**
             * 전사들이 이동한다.
             */
            int[] worrierInfo = moveWorrier();
            // 전사들의 모든 이동거리 / 돌이 된 전사 수 / 공격에 성공한 전사 수
            sb.append(worrierInfo[0]).append(' ').append(stoneWorrier).append(' ').append(worrierInfo[1]).append('\n');
            resetWorrier();
        }
        // 공원에 도칙
        sb.append(0);
    }
    /**
     * [메두사의 이동]
     */
    static class Node {
        int x, y;
        Node prev;

        Node(int x, int y, Node prev) {
            this.x = x;
            this.y = y;
            this.prev = prev;
        }
    }
    static Deque<int[]> shortestPathToPark;
    static boolean moveMonster() {
        // 메두사의 이동 [상 하 좌 우] 우선순위
        int[] mDx = {-1,1,0,0};
        int[] mDy = {0,0,-1,1};
        /**
         * 메두사가 공원까지 이동하는 최단 경로를 구할 수 있다.
         *
         * 1회 수행으로 루트를 미리 구해둔다.
         */
        int x = monster.x, y = monster.y;
        Queue<Node> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];

        q.offer(new Node(x, y, null));
        visited[x][y] = true;

        while(!q.isEmpty()) {
            Node cur = q.poll();

            if(cur.x == park.x && cur.y == park.y) {        // 공원에 도착
                restorePathToPark(cur);
                return true;
            }

            for(int dir=0; dir<4; dir++) {
                int nx = cur.x + mDx[dir];
                int ny = cur.y + mDy[dir];

                if(isNotBoard(nx, ny) || visited[nx][ny]) continue;
                if(board[nx][ny] == 1) continue;

                Node next = new Node(nx, ny, cur);
                visited[nx][ny] = true;
                q.offer(next);
            }
        }

        return false;       // 공원까지 이동 가능한 경로가 존재하지 않음
    }
    static void restorePathToPark(Node cur) {
        /**
         * 메두사가 공원까지 이동하는 최단 경로를 복원한다.
         */
        shortestPathToPark = new ArrayDeque<>();

        while(cur != null) {
            shortestPathToPark.offerFirst(new int[] {cur.x, cur.y});
            cur = cur.prev;
        }
    }
    static boolean[][] cantGo;
    static int monsterEyeSight(int x, int y) {
        /**
         * 메두사 시선
         *
         * 각 위치별로 볼 수 있는 전사 수를 구한다.
         * - 시선의 범위
         * - 가려지는 전사
         */
        // 메두사의 시선 [상 하 좌 우] 우선순위
        int[] mDx = {-1,1,0,0};
        int[] mDy = {0,0,-1,1};
        List<Integer>[][] worrierBoard = getWorrierBoard();       // 전사들의 현 위치

        // 현 위치에 전사가 있다면, 해당 전사는 사라짐
        for(int id : worrierBoard[x][y]) worriers[id].live = false;

        // 각 방향별로 메두사가 볼 수 있는 전사의 수를 기록
        List<Integer> findWorrier = new ArrayList<>();
        for(int dir=0; dir<4; dir++) {
            boolean[][] eyeSight = new boolean[n][n];       // 메두사의 시야
            int nx = x + mDx[dir], ny = y + mDy[dir];

            int depth = 1;
            while(!isNotBoard(nx, ny)) {        // 격자를 벗어나지 않을때까지만
                // 1. 바라보는 방향으로 일직선
                eyeSight[nx][ny] = true;
                // 2. 일직선을 기준으로 좌/우 전파 [0, 1] -> [2, 3] / [2, 3] -> [0, 1]
                if(dir == 0 || dir == 1) {      // 좌 우
                    for(int i=1; i<depth+1; i++) {
                        int side = ny + i;
                        if(isNotBoard(nx, side)) break;
                        eyeSight[nx][side] = true;
                    }
                    for(int i=1; i<depth+1; i++) {
                        int side = ny - i;
                        if(isNotBoard(nx, side)) break;
                        eyeSight[nx][side] = true;
                    }
                } else {        // 상 하
                    for(int i=1; i<depth+1; i++) {
                        int side = nx + i;
                        if(isNotBoard(side, ny)) break;
                        eyeSight[side][ny] = true;
                    }
                    for(int i=1; i<depth+1; i++) {
                        int side = nx - i;
                        if(isNotBoard(side, ny)) break;
                        eyeSight[side][ny] = true;
                    }
                }

                nx += mDx[dir];
                ny += mDy[dir];
                depth++;
            }
            /**
             * 현재 메두사의 시선에서 볼 수 있는 전사들 찾기
             * - 메두사와 가까운 전사부터 탐색 
             * - 가려지는 위치는 메두사와 전사 사이 좌표 값을 상대좌표로 하여 계산
             */
            nx = x + mDx[dir]; ny = y + mDy[dir];
            List<Integer> worrierList = new ArrayList<>();
            boolean[] isFound = new boolean[m+1];
            depth = 1;
            while(!isNotBoard(nx, ny)) {        // 격자를 벗어나지 않을때까지만
                // 1. 바라보는 방향으로 일직선
                if(eyeSight[nx][ny] && !worrierBoard[nx][ny].isEmpty()) {
                    /**
                     * 일직선 뒤로는 더 이상 적을 보지 못함
                     */
                    addWorriersToSight(worrierBoard[nx][ny], worrierList, isFound);
                    for(int i=1; i<n+1; i++) {
                        if(isNotBoard(nx + mDx[dir] * i, ny + mDy[dir] * i)) break;
                        eyeSight[nx + mDx[dir] * i][ny + mDy[dir] * i] = false;
                    }
                }
                // 2. 일직선을 기준으로 좌/우 전파 [0, 1] -> [2, 3] / [2, 3] -> [0, 1]
                if(dir == 0 || dir == 1) {      // 좌 우
                    for(int i=1; i<depth+1; i++) {
                        int side = ny + i;
                        if(isNotBoard(nx, side)) break;
                        if(eyeSight[nx][side] && !worrierBoard[nx][side].isEmpty()) {    // 적이 있다면
                            addWorriersToSight(worrierBoard[nx][side], worrierList, isFound);
                            int depth1 = 1;
                            int tempX = nx + mDx[dir];
                            while(!isNotBoard(tempX, side)) {
                                for(int i1=0; i1<=depth1; i1++) {
                                    if(isNotBoard(tempX, side + i1)) break;
                                    eyeSight[tempX][side+i1] = false;
                                }
                                depth1++;
                                tempX += mDx[dir];
                            }
                        }
                    }
                    for(int i=1; i<depth+1; i++) {
                        int side = ny - i;
                        if(isNotBoard(nx, side)) break;
                        if(eyeSight[nx][side] && !worrierBoard[nx][side].isEmpty()) {    // 적이 있다면
                            addWorriersToSight(worrierBoard[nx][side], worrierList, isFound);
                            int depth1 = 1;
                            int tempX = nx + mDx[dir];
                            while(!isNotBoard(tempX, side)) {
                                for(int i1=0; i1<=depth1; i1++) {
                                    if(isNotBoard(tempX, side - i1)) break;
                                    eyeSight[tempX][side-i1] = false;
                                }
                                depth1++;
                                tempX += mDx[dir];
                            }
                        }
                    }
                } else {        // 상 하
                    for(int i=1; i<depth+1; i++) {
                        int side = nx + i;
                        if(isNotBoard(side, ny)) break;
                        if(eyeSight[side][ny] && !worrierBoard[side][ny].isEmpty()) {    // 적이 있다면
                            addWorriersToSight(worrierBoard[side][ny], worrierList, isFound);
                            int depth1 = 1;
                            int tempY = ny + mDy[dir];
                            while(!isNotBoard(side, tempY)) {
                                for(int i1=0; i1<=depth1; i1++) {
                                    if(isNotBoard(side + i1, tempY)) break;
                                    eyeSight[side+i1][tempY] = false;
                                }
                                depth1++;
                                tempY += mDy[dir];
                            }
                        }
                    }
                    for(int i=1; i<depth+1; i++) {
                        int side = nx - i;
                        if(isNotBoard(side, ny)) break;
                        if(eyeSight[side][ny] && !worrierBoard[side][ny].isEmpty()) {    // 적이 있다면
                            addWorriersToSight(worrierBoard[side][ny], worrierList, isFound);
                            int depth1 = 1;
                            int tempY = ny + mDy[dir];
                            while(!isNotBoard(side, tempY)) {
                                for(int i1=0; i1<=depth1; i1++) {
                                    if(isNotBoard(side - i1, tempY)) break;
                                    eyeSight[side-i1][tempY] = false;
                                }
                                depth1++;
                                tempY += mDy[dir];
                            }
                        }
                    }
                }

                if(dir == 0) {      // 상 방향이 가장 우선 순위를 가짐
                    findWorrier = worrierList;
                    cantGo = eyeSight;
                }
                else if(findWorrier.size() < worrierList.size()) {
                    findWorrier = worrierList;
                    cantGo = eyeSight;
                }

                nx += mDx[dir];
                ny += mDy[dir];
                depth++;
            }
        }

        /**
         * 메두사의 시선에 들어온 전사들은 돌이 된다.
         */
        for(int i : findWorrier) worriers[i].stone = true;
        return findWorrier.size();
    }
    static List<Integer>[][] getWorrierBoard() {
        List<Integer>[][] worrierBoard = new ArrayList[n][n];
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) worrierBoard[x][y] = new ArrayList<>();
        }
        for(int i=1; i<m+1; i++) {
            Worrier w = worriers[i];
            if(w.live) worrierBoard[w.x][w.y].add(i);
        }
        return worrierBoard;
    }
    static void addWorriersToSight(List<Integer> ids, List<Integer> worrierList, boolean[] isFound) {
        for(int id : ids) {
            if(isFound[id]) continue;
            worrierList.add(id);
            isFound[id] = true;
        }
    }
    static int[] moveWorrier() {
        int attackCount = 0;
        int moveCount = 0;
        for(Worrier w : worriers) {
            if(w == null || !w.live || w.stone) continue;        // 사라졌거나, 돌이 되었다면 이동할 수 없다.
            if(moveFirst(w)) {
                moveCount++;
                if (monster.x == w.x && monster.y == w.y) {      // 메두사와 만난 경우
                    attackCount++;
                    w.live = false;
                    continue;
                }
            }
            if(moveSecond(w)) {
                moveCount++;
                if (monster.x == w.x && monster.y == w.y) {      // 메두사와 만난 경우
                    attackCount++;
                    w.live = false;
                }
            }
        }
        return new int[] {moveCount, attackCount};
    }
    static boolean moveFirst(Worrier w) {
        int[] dx = {-1,1,0,0};
        int[] dy = {0,0,-1,1};
        int distFrom = Math.abs(w.x - monster.x) + Math.abs(w.y - monster.y);       // 멘헤튼 거리 기준
        for(int dir=0; dir<4; dir++) {
            int nx = w.x + dx[dir];
            int ny = w.y + dy[dir];

            if(isNotBoard(nx, ny) || cantGo[nx][ny]) continue;                          // 격자를 벗어나지 않고, 메두사의 시야에 들어가지 않는 경우
            int tempDist = Math.abs(nx - monster.x) + Math.abs(ny - monster.y);       // 멘헤튼 거리 기준
            if(tempDist < distFrom) {
                w.x = nx;
                w.y = ny;
                return true;
            }
        }
        return false;
    }
    static boolean moveSecond(Worrier w) {
        int[] dx = {0,0,-1,1};
        int[] dy = {-1,1,0,0};
        int distFrom = Math.abs(w.x - monster.x) + Math.abs(w.y - monster.y);       // 멘헤튼 거리 기준
        for(int dir=0; dir<4; dir++) {
            int nx = w.x + dx[dir];
            int ny = w.y + dy[dir];

            if(isNotBoard(nx, ny) || cantGo[nx][ny]) continue;                          // 격자를 벗어나지 않고, 메두사의 시야에 들어가지 않는 경우
            int tempDist = Math.abs(nx - monster.x) + Math.abs(ny - monster.y);       // 멘헤튼 거리 기준
            if(tempDist < distFrom) {
                w.x = nx;
                w.y = ny;
                return true;
            }
        }
        return false;
    }
    static void resetWorrier() {
        for(Worrier w : worriers) {
            if(w == null || !w.live) continue;
            w.stone = false;
        }
    }
    //----------------------------------------------------------------------
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x+1 > n || y+1 > n;
    }
}
