package feb.week2.swea;

import java.util.*;
import java.io.*;

public class 탈주범검거_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for(int i=1; i<tc+1; i++) {
            sb.append('#').append(i).append(' ');
            init();
            sb.append('\n');
        }
        System.out.println(sb);
        br.close();
    }
    // 우 : 0, 하 : 1, 좌 : 2, 상 : 3
    static final int[] dx = {0,1,0,-1};
    static final int[] dy = {1,0,-1,0};
    /**
     * 탈주범은 시간당 1의 거리를 움직일 수 있다.
     * 탈주범이 있을 수 있는 위치의 개수를 구해야한다.
     */
    static StringTokenizer st;
    static int n, m, r, c, l;
    static Pipe[][] board;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());           // 세로
        m = Integer.parseInt(st.nextToken());           // 가로
        r = Integer.parseInt(st.nextToken());         // 시작 x
        c = Integer.parseInt(st.nextToken());         // 시작 y
        l = Integer.parseInt(st.nextToken());         // 탈출 후 소요된 시간 (탈출한 뒤 한시간 뒤 멘홀 뚜껑을 통해 들어감)

        board = new Pipe[n][m];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<m; y++) {
                int type = Integer.parseInt(st.nextToken());
                Pipe pipe = new Pipe(type);
                board[x][y] = pipe;
            }
        }
        sb.append(solution());
    }
    static int solution() {
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][m];

        /**
         * 초기 위치 설정
         */
        q.offer(new int[]{r, c, 1});
        visited[r][c] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0];
            int curY = cur[1];
            int curT = cur[2];      // 현재 시간

            if(curT == l) continue; // 더 이상 움직일 수 없는 경우

            // 현재 파이프에서 이동 가능한 출구
            for(int i=0; i<4; i++) {
                if(!board[curX][curY].open[i]) continue;
                int nx = curX + dx[i];
                int ny = curY + dy[i];
                if(nx < 0 || ny < 0 || nx >= n || ny >= m) continue;
                if(visited[nx][ny]) continue;
                // 이동하려는 파이프로 들어갈 수 있는지
                Pipe next = board[nx][ny];
                int entry = (i+2)%4;
                if(!next.open[entry]) continue;

                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny, curT+1});
            }
        }

        return countCandidateArea(visited);
    }
    static int countCandidateArea(boolean[][] visited) {
        int count = 0;
        for(int x=0; x<n; x++) {
            for(int y=0; y<m; y++) {
                if(visited[x][y]) count++;
            }
        }
        return count;
    }
    static class Pipe {
        boolean[] open;

        Pipe(int type) {
            open = new boolean[4];

            switch (type) {
                case 1:
                    type1();
                    break;
                case 2:
                    type2();
                    break;
                case 3:
                    type3();
                    break;
                case 4:
                    type4();
                    break;
                case 5:
                    type5();
                    break;
                case 6:
                    type6();
                    break;
                case 7:
                    type7();
                    break;
            }
        }

        void type1() {
            open[0] = true;     // 우
            open[1] = true;     // 하
            open[2] = true;     // 좌
            open[3] = true;     // 상
        }
        void type2() {
            open[1] = true;     // 하
            open[3] = true;     // 상
        }
        void type3() {
            open[0] = true;     // 우
            open[2] = true;     // 좌
        }
        void type4() {
            open[0] = true;     // 우
            open[3] = true;     // 상
        }
        void type5() {
            open[0] = true;     // 우
            open[1] = true;     // 하
        }
        void type6() {
            open[1] = true;     // 하
            open[2] = true;     // 좌
        }
        void type7() {
            open[2] = true;     // 좌
            open[3] = true;     // 상
        }
    }

}
