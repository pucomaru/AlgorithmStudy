package jan.week3.boj;

import java.util.*;
import java.io.*;

public class 비숍_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n;
    static int[][] board;
    static int[][] isUsed;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        board = new int[n][n];
        isUsed = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
            }
        }


        System.out.println(findMaxBishop());
    }

    static int blackBishopCount;
    static int whiteBishopCount;
    static int findMaxBishop() {
        blackBishopCount = 0;
        whiteBishopCount = 0;
        putBishop(0, 0, 0);
        putBishop(0, 0, 1);

        return blackBishopCount + whiteBishopCount;
    }

    static void putBishop(int idx, int bishopCount, int color) {
        if(color == 0) blackBishopCount = Math.max(bishopCount, blackBishopCount);
        else whiteBishopCount = Math.max(bishopCount, whiteBishopCount);

        for(int i=idx; i<n*n; i++) {
            int x = i/n;
            int y = i%n;

            if(board[x][y] == 0) continue;
            if(isUsed[x][y] > 0) continue;
            if((x+y)%2 != color) continue;

            checkUncheck(x, y, true);
            putBishop(i+1, bishopCount+1, color);
            checkUncheck(x, y, false);
        }
    }

    static int[] dx = {1,1,-1,-1};
    static int[] dy = {1,-1,1,-1};
    static void checkUncheck(int x, int y, boolean check) {
        /**
         * 현재 위치 (x, y) 에서 비숍이 움직일 수 있는 방향을 check, uncheck 한다
         */
        for(int dir=0; dir<4; dir++) {
            int curX = x;
            int curY = y;

            while(!isNotBoard(curX, curY)) {
                if(check) isUsed[curX][curY] += 1;
                else isUsed[curX][curY] = Math.max(0, isUsed[curX][curY]-1);

                curX += dx[dir];
                curY += dy[dir];
            }
        }
    }

    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
