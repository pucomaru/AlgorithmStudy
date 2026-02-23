package feb.week3.codetree;

import java.io.*;
import java.util.*;

public class 메두사와전사들_최적화_박재환 {
    static BufferedReader br;
    static StringTokenizer st;
    static StringBuilder sb;

    static int n, m;
    static int[][] board;

    static int medusaX, medusaY;
    static int parkX, parkY;

    static int[] wx, wy;
    static boolean[] alive, stone;

    static int[][] head;
    static int[] next;

    static boolean[][] cantGo;

    static final int[] UDLR_X = {-1, 1, 0, 0};
    static final int[] UDLR_Y = {0, 0, -1, 1};
    static final int[] LRUD_X = {0, 0, -1, 1};
    static final int[] LRUD_Y = {-1, 1, 0, 0};

    // 한 방향 시선 평가 결과:
    // - count: 이 방향에서 돌로 만드는 전사 수
    // - canSee: 이 방향 시야(전사 이동 금지 영역으로 재사용)
    // - ids/size: 돌이 될 전사 ID 목록
    static final class GazeResult {
        int count;
        boolean[][] canSee;
        int[] ids;
        int size;

        GazeResult(int count, boolean[][] canSee, int[] ids, int size) {
            this.count = count;
            this.canSee = canSee;
            this.ids = ids;
            this.size = size;
        }
    }

    static final class IntCollector {
        int[] arr = new int[16];
        int size = 0;

        void add(int v) {
            if (size == arr.length) arr = Arrays.copyOf(arr, arr.length << 1);
            arr[size++] = v;
        }
    }

    public static void main(String[] args) throws Exception {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();

        input();
        List<int[]> path = shortestPath();
        if (path == null) {
            System.out.println(-1);
            return;
        }

        simulate(path);
        System.out.print(sb);
    }

    static void input() throws Exception {
        st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine());
        medusaX = Integer.parseInt(st.nextToken());
        medusaY = Integer.parseInt(st.nextToken());
        parkX = Integer.parseInt(st.nextToken());
        parkY = Integer.parseInt(st.nextToken());

        wx = new int[m];
        wy = new int[m];
        alive = new boolean[m];
        stone = new boolean[m];
        Arrays.fill(alive, true);

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < m; i++) {
            wx[i] = Integer.parseInt(st.nextToken());
            wy[i] = Integer.parseInt(st.nextToken());
        }

        board = new int[n][n];
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            for (int j = 0; j < n; j++) {
                board[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        head = new int[n][n];
        next = new int[m];
        cantGo = new boolean[n][n];
    }

    // 메두사 최단 경로를 한 번만 계산한다.
    // BFS 확장 순서를 상/하/좌/우로 고정해 동일 거리에서 우선순위를 보장한다.
    static List<int[]> shortestPath() {
        boolean[][] vis = new boolean[n][n];
        int[][] px = new int[n][n];
        int[][] py = new int[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(px[i], -1);
            Arrays.fill(py[i], -1);
        }

        ArrayDeque<int[]> q = new ArrayDeque<>();
        q.offer(new int[]{medusaX, medusaY});
        vis[medusaX][medusaY] = true;

        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0], y = cur[1];
            if (x == parkX && y == parkY) break;

            for (int d = 0; d < 4; d++) {
                int nx = x + UDLR_X[d];
                int ny = y + UDLR_Y[d];
                if (!inRange(nx, ny) || vis[nx][ny] || board[nx][ny] == 1) continue;
                vis[nx][ny] = true;
                px[nx][ny] = x;
                py[nx][ny] = y;
                q.offer(new int[]{nx, ny});
            }
        }

        if (!vis[parkX][parkY]) return null;

        ArrayDeque<int[]> rev = new ArrayDeque<>();
        int cx = parkX, cy = parkY;
        while (cx != -1) {
            rev.addLast(new int[]{cx, cy});
            int tx = px[cx][cy];
            int ty = py[cx][cy];
            cx = tx;
            cy = ty;
        }

        List<int[]> path = new ArrayList<>(rev.size());
        while (!rev.isEmpty()) path.add(rev.pollLast());
        return path;
    }

    static void simulate(List<int[]> path) {
        // 시작점(path[0]) 제외, 공원(path[last]) 도착 턴은 출력 0 후 종료
        // 매 턴 순서:
        // 1) 메두사 이동 2) 같은 칸 전사 제거 3) 최적 시선 선택/석화
        // 4) 전사 1차+2차 이동 5) 턴 결과 기록
        for (int i = 1; i < path.size() - 1; i++) {
            medusaX = path.get(i)[0];
            medusaY = path.get(i)[1];

            killWarriorsOnMedusa();
            buildOccupancy();

            GazeResult best = chooseBestGaze();
            for (int k = 0; k < best.size; k++) stone[best.ids[k]] = true;
            cantGo = best.canSee;

            int[] moveInfo = moveWarriors();
            sb.append(moveInfo[0]).append(' ')
              .append(best.count).append(' ')
              .append(moveInfo[1]).append('\n');

            resetStone();
        }
        sb.append(0);
    }

    static void killWarriorsOnMedusa() {
        for (int i = 0; i < m; i++) {
            if (alive[i] && wx[i] == medusaX && wy[i] == medusaY) alive[i] = false;
        }
    }

    static void buildOccupancy() {
        // 칸당 다수 전사를 O(1) 삽입 가능한 단일 연결리스트로 관리:
        // head[x][y] -> 전사 인덱스 체인(next)
        for (int i = 0; i < n; i++) Arrays.fill(head[i], -1);
        Arrays.fill(next, -1);
        for (int i = 0; i < m; i++) {
            if (!alive[i]) continue;
            int x = wx[i], y = wy[i];
            next[i] = head[x][y];
            head[x][y] = i;
        }
    }

    static GazeResult chooseBestGaze() {
        // 동률이면 먼저 평가한 방향 유지: 상(0), 하(1), 좌(2), 우(3)
        GazeResult best = null;
        for (int dir = 0; dir < 4; dir++) {
            GazeResult cur = evalDirection(dir);
            if (best == null || cur.count > best.count) best = cur;
        }
        return best;
    }

    static GazeResult evalDirection(int dir) {
        boolean[][] canSee = new boolean[n][n];
        // 먼저 90도 시야(삼각형)를 채우고, 이후 전사를 만나며 가림(그림자) 처리
        markTriangle(canSee, dir);

        IntCollector ids = new IntCollector();
        int cnt = 0;

        if (dir == 0 || dir == 1) {
            int step = (dir == 0) ? -1 : 1;
            for (int depth = 1; depth < n; depth++) {
                int x = medusaX + step * depth;
                if (!inRange(x, medusaY)) break;

                // 메두사와 가까운 depth부터 탐색해야 가림 규칙이 정확해진다.
                cnt += processCellAndShadow(canSee, ids, dir, x, medusaY);

                for (int off = 1; off <= depth; off++) {
                    int yR = medusaY + off;
                    if (inRange(x, yR)) cnt += processCellAndShadow(canSee, ids, dir, x, yR);
                    int yL = medusaY - off;
                    if (inRange(x, yL)) cnt += processCellAndShadow(canSee, ids, dir, x, yL);
                }
            }
        } else {
            int step = (dir == 2) ? -1 : 1;
            for (int depth = 1; depth < n; depth++) {
                int y = medusaY + step * depth;
                if (!inRange(medusaX, y)) break;

                cnt += processCellAndShadow(canSee, ids, dir, medusaX, y);

                for (int off = 1; off <= depth; off++) {
                    int xD = medusaX + off;
                    if (inRange(xD, y)) cnt += processCellAndShadow(canSee, ids, dir, xD, y);
                    int xU = medusaX - off;
                    if (inRange(xU, y)) cnt += processCellAndShadow(canSee, ids, dir, xU, y);
                }
            }
        }

        return new GazeResult(cnt, canSee, ids.arr, ids.size);
    }

    static int processCellAndShadow(boolean[][] canSee, IntCollector ids, int dir, int x, int y) {
        if (!canSee[x][y] || head[x][y] == -1) return 0;

        int add = 0;
        // 같은 칸의 전사는 전부 함께 석화 대상으로 들어간다.
        for (int p = head[x][y]; p != -1; p = next[p]) {
            ids.add(p);
            add++;
        }

        // 이 칸 전사로 인해 뒤쪽 시야를 차단한다.
        applyShadow(canSee, dir, x, y);
        return add;
    }

    static void applyShadow(boolean[][] canSee, int dir, int x, int y) {
        // 가림 처리 핵심:
        // - 중심선(메두사와 같은 행/열) 전사는 뒤 직선 시야를 모두 가린다.
        // - 좌/우(또는 상/하)로 치우친 전사는 뒤쪽으로 부채꼴 그림자를 만든다.
        if (dir == 0 || dir == 1) {
            int stepX = (dir == 0) ? -1 : 1;
            int side = y - medusaY;

            if (side == 0) {
                for (int nx = x + stepX; inRange(nx, y); nx += stepX) {
                    canSee[nx][y] = false;
                }
                return;
            }

            int nx = x + stepX;
            int depth = 1;
            if (side > 0) {
                while (inRange(nx, y)) {
                    for (int k = 0; k <= depth; k++) {
                        int ny = y + k;
                        if (!inRange(nx, ny)) break;
                        canSee[nx][ny] = false;
                    }
                    nx += stepX;
                    depth++;
                }
            } else {
                while (inRange(nx, y)) {
                    for (int k = 0; k <= depth; k++) {
                        int ny = y - k;
                        if (!inRange(nx, ny)) break;
                        canSee[nx][ny] = false;
                    }
                    nx += stepX;
                    depth++;
                }
            }
        } else {
            int stepY = (dir == 2) ? -1 : 1;
            int side = x - medusaX;

            if (side == 0) {
                for (int ny = y + stepY; inRange(x, ny); ny += stepY) {
                    canSee[x][ny] = false;
                }
                return;
            }

            int ny = y + stepY;
            int depth = 1;
            if (side > 0) {
                while (inRange(x, ny)) {
                    for (int k = 0; k <= depth; k++) {
                        int nx = x + k;
                        if (!inRange(nx, ny)) break;
                        canSee[nx][ny] = false;
                    }
                    ny += stepY;
                    depth++;
                }
            } else {
                while (inRange(x, ny)) {
                    for (int k = 0; k <= depth; k++) {
                        int nx = x - k;
                        if (!inRange(nx, ny)) break;
                        canSee[nx][ny] = false;
                    }
                    ny += stepY;
                    depth++;
                }
            }
        }
    }

    static void markTriangle(boolean[][] canSee, int dir) {
        // 방향별 90도 시야(맨해튼 깊이 d에서 폭 2d+1)를 미리 마킹
        if (dir == 0) {
            for (int d = 1; d < n; d++) {
                int x = medusaX - d;
                if (x < 0) break;
                int l = Math.max(0, medusaY - d);
                int r = Math.min(n - 1, medusaY + d);
                for (int y = l; y <= r; y++) canSee[x][y] = true;
            }
        } else if (dir == 1) {
            for (int d = 1; d < n; d++) {
                int x = medusaX + d;
                if (x >= n) break;
                int l = Math.max(0, medusaY - d);
                int r = Math.min(n - 1, medusaY + d);
                for (int y = l; y <= r; y++) canSee[x][y] = true;
            }
        } else if (dir == 2) {
            for (int d = 1; d < n; d++) {
                int y = medusaY - d;
                if (y < 0) break;
                int u = Math.max(0, medusaX - d);
                int b = Math.min(n - 1, medusaX + d);
                for (int x = u; x <= b; x++) canSee[x][y] = true;
            }
        } else {
            for (int d = 1; d < n; d++) {
                int y = medusaY + d;
                if (y >= n) break;
                int u = Math.max(0, medusaX - d);
                int b = Math.min(n - 1, medusaX + d);
                for (int x = u; x <= b; x++) canSee[x][y] = true;
            }
        }
    }

    static int[] moveWarriors() {
        int moveCount = 0;
        int attackCount = 0;

        for (int i = 0; i < m; i++) {
            if (!alive[i] || stone[i]) continue;

            // 1차 이동 우선순위: 상/하/좌/우
            if (moveOneStep(i, UDLR_X, UDLR_Y)) {
                moveCount++;
                if (wx[i] == medusaX && wy[i] == medusaY) {
                    alive[i] = false;
                    attackCount++;
                    continue;
                }
            }

            // 2차 이동 우선순위: 좌/우/상/하
            if (moveOneStep(i, LRUD_X, LRUD_Y)) {
                moveCount++;
                if (wx[i] == medusaX && wy[i] == medusaY) {
                    alive[i] = false;
                    attackCount++;
                }
            }
        }

        return new int[]{moveCount, attackCount};
    }

    static boolean moveOneStep(int id, int[] dx, int[] dy) {
        int curDist = dist(wx[id], wy[id], medusaX, medusaY);

        for (int d = 0; d < 4; d++) {
            int nx = wx[id] + dx[d];
            int ny = wy[id] + dy[d];
            // 시야(cantGo)로는 이동할 수 없고, 메두사와 거리가 엄격히 감소해야만 이동
            if (!inRange(nx, ny) || cantGo[nx][ny]) continue;

            int nd = dist(nx, ny, medusaX, medusaY);
            if (nd < curDist) {
                wx[id] = nx;
                wy[id] = ny;
                return true;
            }
        }
        return false;
    }

    static void resetStone() {
        for (int i = 0; i < m; i++) {
            if (alive[i]) stone[i] = false;
        }
    }

    static int dist(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    static boolean inRange(int x, int y) {
        return x >= 0 && y >= 0 && x < n && y < n;
    }
}
