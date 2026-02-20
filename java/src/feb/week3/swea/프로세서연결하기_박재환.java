package feb.week3.swea;

import java.util.*;
import java.io.*;

public class 프로세서연결하기_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for(int i=0; i<tc; i++) {
            sb.append('#').append(i+1).append(' ').append(init()).append('\n');
        }
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n;
    static int[][] board;
    static List<int[]> processors;
    static int minCableL;
    static int maxProcessorCnt;
    static int init() throws IOException {
        processors = new ArrayList<>();
        minCableL = Integer.MAX_VALUE;
        maxProcessorCnt = 0;

        n = Integer.parseInt(br.readLine().trim());
        board = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) {
                board[x][y] = Integer.parseInt(st.nextToken());
                if(board[x][y] == 1 &&
                        !(x == 0 || x == n-1 || y == 0 || y == n-1)) processors.add(new int[] {x, y});
            }
        }
        findMinCableL(0, 0, 0, new boolean[n][n]);
        return minCableL == Integer.MAX_VALUE ? 0 : minCableL;
    }
    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static void findMinCableL(int processorId, int cableL, int processorCnt, boolean[][] checked) {
        if(processorId == processors.size()) {
            if(maxProcessorCnt < processorCnt) {
                maxProcessorCnt = processorCnt;
                minCableL = cableL;
            } else if(maxProcessorCnt == processorCnt && minCableL > cableL) {
                minCableL = cableL;
            }
            return;
        }
        /**
         * [가지치기]
         * 이전 최대 프로세서 수보다, 지금부터 모든 프로세스가 연결되도 경우가 되지 않는 경우
         */
        if(processorCnt + (processors.size() - processorId) < maxProcessorCnt)
            return;

        int[] processor = processors.get(processorId);
        // 현재 프로세서를 4방향으로 연결시켜본다.
        for(int dir=0; dir<4; dir++) {
            /**
             * 현재 방향에서 연결 가능한지, 가능하지 않은지
             */
            int x = processor[0];
            int y = processor[1];

            List<int[]> cable = new ArrayList<>();
            boolean connect = false;
            while(!connect) {
                x += dx[dir];
                y += dy[dir];
                if(x < 0 || y < 0 || x >= n || y >= n) break;
                if(board[x][y] != 0) break;
                if(checked[x][y]) break;
                checked[x][y] = true;
                cable.add(new int[] {x, y});
                if(x == 0 || x == n-1 || y == 0 || y == n-1) {
                    connect = true;
                }
            }
            if(connect) {
                findMinCableL(processorId+1, cableL + cable.size(), processorCnt + 1, checked);
            }
            for(int[] point : cable) checked[point[0]][point[1]] = false;
        }
        findMinCableL(processorId + 1, cableL, processorCnt, checked);
    }
}
