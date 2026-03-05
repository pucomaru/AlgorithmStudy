package mar.week1.codetree;

import java.util.*;
import java.io.*;

public class 고대문명유적탐사_박재환 {
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
    static int[][] board;
    static Queue<Integer> wall;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        int k = Integer.parseInt(st.nextToken());
        int m = Integer.parseInt(st.nextToken());

        board = new int[5][5];
        for(int x=0; x<5; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<5; y++) board[x][y] = Integer.parseInt(st.nextToken());
        }
        wall = new ArrayDeque<>();
        st = new StringTokenizer(br.readLine().trim());
        while(m-- > 0) wall.offer(Integer.parseInt(st.nextToken()));
        while(k-- > 0) {
            int value = 0;
            /**
             * 1. 격자 선택
             * - 유물 1차 획득 가치 최대화
             * - 같은 경우 회전 각도가 가장 작은 방법
             * - 같은 경우 회전 중심 열의 가장 작은 구간
             * - 같은 경우 회전 중심 행의 가장 작은 구간
             */
            pickGrid();
            if(board == null) break;
            /**
             * 2. 유물 획득
             * - 연속적으로 3개 이상 같은 수가 모여있으면 획득 가능
             * - 총 가치는 칸의 개수
             */
            value += getValue();
            /**
             * 3. 빈 칸 채우기
             * - 유물 획득으로 빈 칸을, 벽에 채워진 수로 채움
             */
            fillEmptySpace();
            /**
             * 유물을 획득할 수 없다면 탐사 종료
             */
            value += afterTurn();
            sb.append(value).append(' ');
        }
    }
    static int afterTurn() {
        int accValue = 0;
        while(true) {
            int value = getValue();
            if(value == 0) break;
            accValue += value;
            fillEmptySpace();
        }
        return accValue;
    }
    static void fillEmptySpace() {
        while(!found.isEmpty()) {
            int[] point = found.poll();
            board[point[0]][point[1]] = wall.poll();
        }
    }
    static PriorityQueue<int[]> found;
    static int getValue() {
        found = new PriorityQueue<>((a, b) -> {
            if(a[1] == b[1]) return Integer.compare(b[0], a[0]);
            return Integer.compare(a[1], b[1]);
        });
        boolean[][] checked = new boolean[5][5];
        int value = 0;
        for(int x=0; x<5; x++) {
            for(int y=0; y<5; y++) {
                if(checked[x][y]) continue;
                value += checkSameValue(x, y, checked);
            }
        }
        return value;
    }
    static final int[] dx = {0,1,0,-1};
    static final int[] dy = {1,0,-1,0};
    static int checkSameValue(int x, int y, boolean[][] checked) {
        int target = board[x][y];
        Queue<int[]> q = new ArrayDeque<>();
        Queue<int[]> q2 = new ArrayDeque<>();

        int size = 1;
        q.offer(new int[] {x, y});
        checked[x][y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0];
            int curY = cur[1];
            q2.offer(cur);
            for(int dir=0; dir<4; dir++) {
                int nx = curX + dx[dir];
                int ny = curY + dy[dir];
                if(nx < 0 || ny < 0 || nx > 4 || ny > 4) continue;
                if(checked[nx][ny] || board[nx][ny] != target) continue;

                q.offer(new int[] {nx, ny});
                checked[nx][ny] = true;
                size++;
            }
        }
        if(size > 2) {
            while(!q2.isEmpty()) found.offer(q2.poll());
            return size;
        }

        return 0;
    }
    static int[][] copyBoard(int[][] origin) {
        int[][] temp = new int[5][5];
        for(int x=0; x<5; x++) {
            for(int y=0; y<5; y++) temp[x][y] = origin[x][y];
        }
        return temp;
    }
    static void pickGrid() {
        /**
         * 격자의 크기가 5 x 5 로 고정되어있음
         * 나올 수 있는 중심 좌표는 항상 동일
         */
        int maxValue = 0;
        int cx = -1, cy = -1, r = -1;
        int[][] nextGrid = null;

        for(int x=1; x<4; x++) {
            for(int y=1; y<4; y++) {
                int[][] temp = copyBoard(board);
                // 회전은 [90, 180, 270], 총 3번
                for(int rotate=0; rotate<3; rotate++) {
                    temp = rotateGrid(x, y, temp);
                    int value = getMaxValue(temp);

                    if(maxValue < value) {
                        maxValue = value;
                        cx = x; cy = y;
                        r = rotate;
                        nextGrid = temp;
                    } else if(maxValue == value && rotate < r) {
                        cx = x; cy = y;
                        r = rotate;
                        nextGrid = temp;
                    } else if(maxValue == value && rotate == r && cy > y) {
                        cx = x; cy = y;
                        nextGrid = temp;
                    } else if(maxValue == value && rotate == r && cy == y && cx > x) {
                        cx = x;
                        nextGrid = temp;
                    }
                }
            }
        }
        board = nextGrid;
    }
    static int[][] rotateGrid(int cx, int cy, int[][] origin) {
        int[][] temp = copyBoard(origin);

        int[] top = {temp[cx-1][cy-1], temp[cx-1][cy], temp[cx-1][cy+1]};
        int[] bottom = {temp[cx+1][cy-1], temp[cx+1][cy], temp[cx+1][cy+1]};
        int[] left = {temp[cx-1][cy-1], temp[cx][cy-1], temp[cx+1][cy-1]};
        int[] right = {temp[cx-1][cy+1], temp[cx][cy+1], temp[cx+1][cy+1]};

        // top -> right
        temp[cx-1][cy+1] = top[0]; temp[cx][cy+1] = top[1]; temp[cx+1][cy+1] = top[2];
        // right -> bottom
        temp[cx+1][cy-1] = right[2]; temp[cx+1][cy] = right[1]; temp[cx+1][cy+1] = right[0];
        // bottom -> left
        temp[cx-1][cy-1] = bottom[0]; temp[cx][cy-1] = bottom[1]; temp[cx+1][cy-1] = bottom[2];
        // left -> top
        temp[cx-1][cy-1] = left[2]; temp[cx-1][cy] = left[1]; temp[cx-1][cy+1] = left[0];

        return temp;
    }
    static int getMaxValue(int[][] temp) {
        boolean[][] checked = new boolean[5][5];
        int value = 0;
        for(int x=0; x<5; x++) {
            for(int y=0; y<5; y++) {
                if(checked[x][y]) continue;
                value += checkSameValue(x, y, checked, temp);
            }
        }
        return value;
    }
    static int checkSameValue(int x, int y, boolean[][] checked, int[][] temp) {
        int target = temp[x][y];
        Queue<int[]> q = new ArrayDeque<>();

        int size = 1;
        q.offer(new int[] {x, y});
        checked[x][y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0];
            int curY = cur[1];

            for(int dir=0; dir<4; dir++) {
                int nx = curX + dx[dir];
                int ny = curY + dy[dir];
                if(nx < 0 || ny < 0 || nx > 4 || ny > 4) continue;
                if(checked[nx][ny] || temp[nx][ny] != target) continue;

                q.offer(new int[] {nx, ny});
                checked[nx][ny] = true;
                size++;
            }
        }
        if(size > 2) return size;

        return 0;
    }
}
