package feb.week4.codetree;

import java.util.*;
import java.io.*;

public class 마법의숲탐색_박재환 {
    /**
     * 정령들은 숲의 북쪽을 통해서만 숲에 들어올 수 있다.
     * K명의 정령은 골렘을 타고있다.
     * - 골렘은 십자 모양의 구조를 갖는다. (총 5칸)
     * - 중앙을 제외한 4칸 중 한 칸은 골렘의 출구이다. (탑승은 상관 없지만, 내릴 때는 정해진 출구로만 가능)
     * * * *
     * 0 : 북, 1 : 동, 2 : 남, 3 : 서
     * * * *
     * [골렘이동]
     * 1) 남쪽으로 한 칸 내려간다. -> 비어있을 때만 이동 가능
     * 2) (1) 방법이 안되면, 서쪽으로 회전하며 내려간다. (<, v) -> 출구 위치 변경 (반시계방향)
     * 3) (1), (2) 안되면 동쪽으로 회전하며 내려간다. (>, v) -> 출구 위치 변경 (시계방향)
     * +1 : 시계방향, -1 반시계방향
     *
     * 골렘이 더 이상 이돋할 수 없다면
     * 1) 정령은 골렘 내에서 상하좌우 인접한 칸으로 이동
     * 2) 골렘의 출구가 다른 골렘과 인접해있다면, 다른 골렘으로 이동 가능
     * 3) 가장 남쪽 칸으로 정령 이동
     *
     * 최대한 남쪽으로 이동했지만, 골렘의 몸 일부가 여전히 숲을 벗어난 상태라면
     * 1) 격자를 모두 비운다.
     * 2) 이때 정렬 위치를 출력하지 않는다.
     */
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static int[] dx = {-1,0,1,0};
    static int[] dy = {0,1,0,-1};
    static class Golem {
        // 북 동 남 서 중앙
        List<int[]> points;         // 골렘이 위치한 좌표
        int exit;

        Golem(int y, int exit) {
            points = new ArrayList<>();
            init(y);
            this.exit = exit;
        }
        void init(int y) {
            points.add(new int[] {0, y});       // 북
            points.add(new int[] {1, y+1});     // 동
            points.add(new int[] {2, y});       // 남
            points.add(new int[] {1, y-1});     // 서
            points.add(new int[] {1, y});       // 중앙
        }
        boolean moveNorth(int[][] board) {      // 남
            int dir = 2;
            List<int[]> temp = new ArrayList<>();
            for(int[] point : this.points) {
                int nx = point[0] + dx[dir];
                int ny = point[1] + dy[dir];
                if(nx < 0 || ny < 0 || nx >= board.length || ny >= board[0].length) return false;
                if(board[nx][ny] != 0) return false;
                temp.add(new int[] {nx, ny});
            }
            this.points = temp;
            return true;
        }
        boolean moveWest(int[][] board) {       // 서
            int dir = 3;
            List<int[]> temp = new ArrayList<>();
            for(int[] point : this.points) {
                int nx = point[0] + dx[dir];
                int ny = point[1] + dy[dir];
                if(nx < 0 || ny < 0 || nx >= board.length || ny >= board[0].length) return false;
                if(board[nx][ny] != 0) return false;
                temp.add(new int[] {nx, ny});
            }
            dir = 2;
            List<int[]> temp2 = new ArrayList<>();
            for(int[] point : temp) {
                int nx = point[0] + dx[dir];
                int ny = point[1] + dy[dir];
                if(nx < 0 || ny < 0 || nx >= board.length || ny >= board[0].length) return false;
                if(board[nx][ny] != 0) return false;
                temp2.add(new int[] {nx, ny});
            }
            this.points = temp2;
            this.counterClockWise();
            return true;
        }
        boolean moveEast(int[][] board) {       // 동
            int dir = 1;
            List<int[]> temp = new ArrayList<>();
            for(int[] point : this.points) {
                int nx = point[0] + dx[dir];
                int ny = point[1] + dy[dir];
                if(nx < 0 || ny < 0 || nx >= board.length || ny >= board[0].length) return false;
                if(board[nx][ny] != 0) return false;
                temp.add(new int[] {nx, ny});
            }
            dir = 2;
            List<int[]> temp2 = new ArrayList<>();
            for(int[] point : temp) {
                int nx = point[0] + dx[dir];
                int ny = point[1] + dy[dir];
                if(nx < 0 || ny < 0 || nx >= board.length || ny >= board[0].length) return false;
                if(board[nx][ny] != 0) return false;
                temp2.add(new int[] {nx, ny});
            }
            this.points = temp2;
            this.clockWise();
            return true;
        }
        void clockWise() {
            this.exit = (this.exit+1)%4;
        }
        void counterClockWise() {
            this.exit = (this.exit+-1+4)%4;
        }
    }
    static StringTokenizer st;
    static int r, c, k;
    static int[][] board;
    static Map<Integer, Golem> golems;
    static Map<Integer, Integer> maxRow;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        board = new int[r+3][c];        // 골렘 초기 위치 설정을 위한 패딩값

        int total = 0;
        golems = new HashMap<>();
        maxRow = new HashMap<>();
        for(int i=0; i<k; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int y = Integer.parseInt(st.nextToken())-1;
            int exit = Integer.parseInt(st.nextToken());
            Golem golem = new Golem(y, exit);
            golems.put(i+1, golem);
            int row = moveGolem(i+1, golem);
            total += row;
        }
        System.out.println(total);
    }
    static int row;
    static int moveGolem(int id, Golem golem) {
        /**
         * 골렘을 최대한 남쪽으로 이동시긴다.
         * 1) 남
         * 2) 서
         * 3) 동
         */
        while(golem.points.get(2)[0] != (r+3-1)) {
            boolean move = false;

            // 남쪽
            if(golem.moveNorth(board)) {
                move = true;
            }
            // 서쪽
            else if(golem.moveWest(board)) {
                move = true;
            }
            // 동쪽
            else if(golem.moveEast(board)) {
                move = true;
            }

            if(!move) break;
        }
        if(!isVaildLocation(golem)) {
            reset();
            return 0;
        }
        /**
         * 현재 골렘이 이동 가능한 제일 낮은 행을 찾는다.
         */
        row = 0;
        Set<Integer> visited = new HashSet<>();
        visited.add(id);
        findMaxRow(id, visited);
        /**
         * 현재 골렘의 위치를 지도에 표시
         */
        for(int[] point : golem.points) {
            int x = point[0];
            int y = point[1];

            board[x][y] = id;
        }
        return row-2;
    }
    static void findMaxRow(int golemId, Set<Integer> visited) {
        row = Math.max(row, golems.get(golemId).points.get(2)[0]);

        int eId = golems.get(golemId).exit;
        int[] exit = golems.get(golemId).points.get(eId);

        for(int dir=0; dir<4; dir++) {
            int nx = exit[0] + dx[dir];
            int ny = exit[1] + dy[dir];
            if(nx < 0 || ny < 0 || nx >= board.length || ny >= board[0].length) continue;
            if(board[nx][ny] == 0) continue;
            if(board[nx][ny] == golemId) continue;
            int nextGolemId = board[nx][ny];
            if(!visited.add(nextGolemId)) continue;
            // 처음 찾는 경우
            findMaxRow(nextGolemId, visited);
        }
    }
    static boolean isVaildLocation(Golem golem) {
        /**
         * 골렘이 board 에 완전하게 포함되지 않은 경우를 확인
         */
        for(int[] point : golem.points) {
            int x = point[0];

            if(x < 3) return false;
        }
        return true;
    }
    static void reset() {
        golems.clear();
        maxRow.clear();
        for(int i=0; i<r+3; i++) Arrays.fill(board[i], 0);
    }
}
