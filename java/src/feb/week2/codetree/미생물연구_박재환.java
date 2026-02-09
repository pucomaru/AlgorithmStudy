package feb.week2.codetree;

import java.util.*;
import java.io.*;

public class 미생물연구_박재환 {
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
     *  N x N (좌측 하단 (0,0), 우측 상단(N,N))
     *  Q 번의 실험
     *
     *  [미생물 투입]
     *  좌측하단, 우측상단 좌표가 주어짐
     *  -> 투입된 영역에 다른 미생물이 있었다면, 새로 투입된 미생물만 남게 됨
     *  -> 기존에 있던 무리가 둘로 나누어지는경우 사라지게 됨
     *
     *  [배양 용기 이동]
     *  모든 미생물을 새로운 용기로 옮김
     *  가장 차지한 영역이 큰 무리부터 옮김 -> 사이즈가 같다면 투입 시간을 우선으로
     *  -> 형태는 유지하며, 다른 미생물과 겹치지 않게 최대한 x, y좌표가 작은 곳으로 옮김
     *  -> 옮길 칸이 없다면 사라져버림
     *
     *  [실험 결과 기록]
     *  상하좌우 맞닿으면 인접하다
     *  모든 인접한 무리쌍을 확인 (A넓이 * B넓이 = 성과)
     *  성과를 모두 더한 값 출력
     *
     */
    static final int[] dx = {0,1,0,-1};
    static final int[] dy = {1,0,-1,0};

    static StringTokenizer st;
    static int n, q;
    static int[][] board;
    static int groupId;
    static void init() throws IOException {
        groupId = 0;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        q = Integer.parseInt(st.nextToken());
        board = new int[n][n];
        for(int i=0; i<q; i++) {
            st = new StringTokenizer(br.readLine().trim());
            putGroup();
            moveNewBoard();
            sb.append(searchAdjGroups()).append('\n');
        }
    }
    static int searchAdjGroups() {
        /**
         * 서로 인접한 그룹을 확인한다.
         */
        Map<Integer, Integer> groups = new HashMap<>();
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(board[x][y] == 0) continue;
                /**
                 * 각 그룹의 크기를 구한다.
                 */
                groups.put(board[x][y], groups.getOrDefault(board[x][y], 0) + 1);
            }
        }
        int result = 0;
        boolean[][] isPair = new boolean[q+1][q+1];
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(board[x][y] == 0) continue;
                int from = board[x][y];

                for(int dir=0; dir<4; dir++) {
                    int nx = x + dx[dir];
                    int ny = y + dy[dir];
                    if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;

                    int to = board[nx][ny];
                    if(to == 0 || from == to || isPair[from][to] || isPair[to][from]) continue;

                    isPair[from][to] = true;
                    isPair[to][from] = true;
                    result += (groups.get(from) * groups.get(to));
                }
            }
        }
        return result;
    }
    static PriorityQueue<Group> pq;
    static void moveNewBoard() {
        /**
         * 새로운 배양판으로 이동시키기 위해, 미생물들의 우선순위를 결정해야함
         */
        pq = new PriorityQueue<>();
        findAllGroups();
        /**
         * 새로운 배양판으로 이동
         */
        int[][] newBoard = new int[n][n];

        while(!pq.isEmpty()) {
            Group group = pq.poll();
            List<int[]> points = group.points;
            /**
             * 기존 좌표의 상대 좌표를 구한다.
             * -> 가장 작은 위치(가장 좌측하단)을 (0,0)으로 가정
             */
            int minX = n+1, minY = n+1;
            for(int[] point : points) {
                minX = Math.min(minX, point[0]);
                minY = Math.min(minY, point[1]);
            }
            List<int[]> rel = new ArrayList<>();
            for(int[] point : points) {
                int nx = point[0] - minX;
                int ny = point[1] - minY;
                rel.add(new int[] {nx, ny});
            }
            /**
             * 이동가능한 가장 작은 위치를 찾음
             */
            boolean move = false;
            for(int x=0; x<n && !move; x++) {
                for(int y=0; y<n && !move; y++) {
                    if(!canMove(x, y, rel, newBoard)) continue;
                    // 이동 가능
                    for(int[] point : rel) {
                        int nx = point[0] + x;
                        int ny = point[1] + y;
                        newBoard[nx][ny] = group.id;
                    }
                    move = true;
                }
            }
        }
        board = newBoard;
    }
    static boolean canMove(int x, int y, List<int[]> points, int[][] board) {
        for(int[] point : points) {
            int nx = point[0] + x;
            int ny = point[1] + y;
            if(nx < 0 || ny < 0 || nx >= n || ny >= n) return false;
            if(board[nx][ny] != 0) return false;
        }
        return true;
    }
    static class Group implements Comparable<Group> {
        int id;
        List<int[]> points;

        Group(int id) {
            this.id = id;
            this.points = new ArrayList<>();
        }

        public int compareTo(Group o) {
            if(this.points.size() == o.points.size()) return Integer.compare(this.id, o.id);
            return Integer.compare(o.points.size(), this.points.size());
        }
    }
    static void findAllGroups() {
        boolean[][] visited = new boolean[n][n];
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(board[x][y] == 0 || visited[x][y]) continue;
                Group group = makeGroupObj(x, y, visited);
                pq.offer(group);
            }
        }
    }
    static Group makeGroupObj(int x, int y, boolean[][] visited) {
        Group group = new Group(board[x][y]);
        Queue<int[]> q = new ArrayDeque<>();
        List<int[]> points = new ArrayList<>();

        q.offer(new int[] {x, y});
        visited[x][y] = true;
        points.add(new int[] {x, y});

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0];
            int curY = cur[1];

            for(int dir=0; dir<4; dir++) {
                int nx = curX + dx[dir];
                int ny = curY + dy[dir];
                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(visited[nx][ny] || board[nx][ny] != board[x][y]) continue;
                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny});
                points.add(new int[] {nx, ny});
            }
        }

        group.points = points;
        return group;
    }
    static void putGroup() {
        ++groupId;
        int x1 = Integer.parseInt(st.nextToken());
        int y1 = Integer.parseInt(st.nextToken());
        int x2 = Integer.parseInt(st.nextToken());
        int y2 = Integer.parseInt(st.nextToken());
        for(int x=x1; x<x2; x++) {
            for(int y=y1; y<y2; y++) board[x][y] = groupId;
        }
        /**
         * 새로운 그룹을 추가하면서 둘 이상으로 나뉘어진 그룹이 있는지 확인
         */
        checkDivideGroup();
    }

    static void checkDivideGroup() {
        Map<Integer, Integer> group = new HashMap<>();
        boolean[][] visited = new boolean[n][n];
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(board[x][y] == 0 || visited[x][y]) continue;
                group.put(board[x][y], group.getOrDefault(board[x][y], 0) + 1);
                checkSameGroup(x, y, visited);
            }
        }

        for(Map.Entry<Integer, Integer> entry : group.entrySet()) {
            if(entry.getValue() > 1) {      // 둘 이상의 그룹으로 나뉜 경우
                for(int x=0; x<n; x++) {
                    for(int y=0; y<n; y++) {
                        if(board[x][y] == entry.getKey()) board[x][y] = 0;
                    }
                }
            }
        }
    }
    static void checkSameGroup(int x, int y, boolean[][] visited) {
        Queue<int[]> q = new ArrayDeque<>();

        q.offer(new int[] {x, y});
        visited[x][y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0];
            int curY = cur[1];

            for(int dir=0; dir<4; dir++) {
                int nx = curX + dx[dir];
                int ny = curY + dy[dir];
                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(visited[nx][ny] || board[nx][ny] != board[x][y]) continue;
                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny});
            }
        }
    }
}
