package feb.week3.swea;

import java.util.*;
import java.io.*;

public class 등산로조성_박재환 {
    static BufferedReader br;
    static StringBuilder sb;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for (int i = 1; i < tc + 1; i++) {
            sb.append('#').append(i).append(' ').append(init()).append('\n');
        }
        br.close();
        System.out.println(sb);
    }

    static StringTokenizer st;

    static void input() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        int maxHeight = 0;
        board = new int[n][n];
        for (int x = 0; x < n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for (int y = 0; y < n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
                maxHeight = Math.max(maxHeight, board[x][y]);
            }
        }

        candidateLocation = new ArrayList<>();
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if (board[x][y] == maxHeight) candidateLocation.add(new int[]{x, y});
            }
        }
    }

    static int n, k;
    static int[][] board;
    static List<int[]> candidateLocation;
    static int maxLength;
    static int init() throws IOException {
        input();

        maxLength = 0;
        for (int[] point : candidateLocation) {
            /**
             * 출발지 후보가 될 수 있는 위치들
             */
            int x = point[0];
            int y = point[1];
            /**
             * 시작 위치를 포함해서 길이를 계산
             */
            int curHeight = board[x][y];
            boolean[][] visited = new boolean[n][n];
            visited[x][y] = true;
            getLongestPath(x, y, 1, 0, curHeight, visited);
        }
        return maxLength;
    }

    static int[] dx = {0, 1, 0, -1};
    static int[] dy = {1, 0, -1, 0};
    static void getLongestPath(int x, int y, int len, int dig, int curHeight, boolean[][] visited) {
        for(int dir=0; dir<4; dir++) {
            int nx = x + dx[dir];
            int ny = y + dy[dir];
            if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;      // 격자를 벗어나는 경우

            /**
             * 두 가지 경우가 있음
             * - 현 위치보다 높이가 같거나 높은 경우
             *      - 깎을 수 있는 경우 깎아봄
             * - 높이가 낮은 경우
             *      - 깎지 않아도 이동 가능
             */
            int nextHeight = board[nx][ny];
            if(curHeight < nextHeight+1 && dig == 0) {      // 현 위치보다 높고, 아직 다른 산을 깎지 않은 경우
                // k 만큼 땅을 깎을 수 있음
                int need = nextHeight - (curHeight - 1);    // 현재보다 1 낮은 높이로 만들기 위해 필요한 높이
                if(need > k) continue;
                if (visited[nx][ny]) continue;
                visited[nx][ny] = true;
                getLongestPath(nx, ny, len + 1, 1, curHeight-1, visited);
                visited[nx][ny] = false;
            } else if(curHeight > nextHeight) {         // 깎지 않고 이동 가능
                if(visited[nx][ny]) continue;
                visited[nx][ny] = true;
                getLongestPath(nx, ny, len+1, dig, nextHeight, visited);
                visited[nx][ny] = false;
            }
        }
        maxLength = Math.max(len, maxLength);
    }
}
