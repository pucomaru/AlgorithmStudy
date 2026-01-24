package jan.week4.swea;

import java.util.*;
import java.io.*;

public class 타일매칭_박재환 {
    private static BufferedReader br;
    private static UserSolution userSolution = new UserSolution();

    private final static int WIDTH = 8;

    private final static int CMD_INIT = 100;
    private final static int CMD_TAKETURN = 200;

    private static int tiles[][] = new int[10000][WIDTH];

    private static boolean run() throws Exception {
        StringTokenizer stdin = new StringTokenizer(br.readLine(), " ");

        boolean okay = false;
        int Q = Integer.parseInt(stdin.nextToken());

        for (int q = 0; q < Q; ++q)
        {
            stdin = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(stdin.nextToken());
            switch (cmd)
            {
                case CMD_INIT:
                    int N = Integer.parseInt(stdin.nextToken());
                    for(int y = 0;y < N;y++)
                    {
                        stdin = new StringTokenizer(br.readLine(), " ");
                        for(int x = 0;x < WIDTH;x++)
                        {
                            tiles[y][x] = Integer.parseInt(stdin.nextToken());
                        }
                    }
                    userSolution.init(N, tiles);
                    okay = true;
                    break;
                case CMD_TAKETURN:
                    int[] user_ans = userSolution.takeTurn();
                    for (int i = 0; i < 5;i++)
                    {
                        int correct_ans =  Integer.parseInt(stdin.nextToken());
                        if(user_ans[i] != correct_ans)
                        {
                            okay = false;
                        }
                    }
                    break;
                default:
                    okay = false;
                    break;
            }
        }

        return okay;
    }

    public static void main(String[] args) throws Exception {
        int T, MARK;

        System.setIn(new java.io.FileInputStream("C:\\Users\\doorm\\Desktop\\Algorithm\\java\\src\\jan\\week4\\swea\\res\\sample_input_타일매칭.txt"));
        br = new BufferedReader(new InputStreamReader(System.in));

        StringTokenizer stinit = new StringTokenizer(br.readLine(), " ");
        T = Integer.parseInt(stinit.nextToken());
        MARK = Integer.parseInt(stinit.nextToken());

        for (int tc = 1; tc <= T; tc++) {
            int score = run() ? MARK : 0;
            System.out.println("#" + tc + " " + score);
        }

        br.close();
    }
}

/**
 * 코드 영역
 */
class UserSolution {
    /**
     * 8 x 8 격자
     * - 좌측 하단 : (0, 0)
     *
     * 격자 상단에는 예비 타일들이 존재하며, 이 타일들이 아래로 내려와 격자의 빈 칸을 채운다.
     *
     * 좌표 하나를 선정하여 [오른쪽, 위쪽] 인접한 좌표 타일과 바꾼다.
     * 가로나 세로 방향으로 3개 이상 같은 타일들이 존재할 경우, 그것들을 지우고 점수를 얻는다.
     *
     * 타일이 지워진 뒤 빈 공간은 타일이내려와 순차적으로 채우게 된다.
     * 타일 교환은 점수를 얻을 수 있는 경우에만 가능하다.
     *
     * 위치를 교환할 수 있는 경우가 여러 곳일 경우, 가장 높은 점수를 얻을 수 있는 곳을 선택하여 교환한다.
     *
     * 점수
     * 타일 3 개 연속 : 1
     * 타일 4 개 연속 : 4
     * 타일 5 개 연속 : 9
     *
     * 2 줄 이상이 동시에 지워질 경우 점수를 합산한다.
     */
    int n;
    int[][] board;
    int[][] candidateBlocks;        // 떨어뜨릴 수 있는 블록들 [col][block]
    int[] candidateBlockIdx;        // 떨어질 블록의 순서
    void init(int N, int mTiles[][])
    {
        n = N;
        board = new int[8][8];      // 8 x 8 격자는 고정
        candidateBlocks = new int[8][n];
        candidateBlockIdx = new int[8];
    }

    /**
     * [타일이 일부만 내려올 수도 있다. -> 하나를 통째로 만든다?]
     *
     * 1. 격자를 준비 상태로
     * - 격자 내 빈공간 X
     * - 같은 타일이 3개 이상 연속되는 경우 X
     * - 타일을 교환하여 점수를 획득할 수 있어야함 -> 없다면 전체 타일 삭제
     *
     * 2. 타일 교체
     * - 얻을 수 있는 점수가 가장 높은 곳
     * - y 좌표가 가장 작은 위치
     * - x 좌표가 가장 작은 위치
     * - 오른쪽 타일 교환
     *
     * 3. 빈 공간을 채운다.
     */
    int[] takeTurn()
    {
        /**
         * 얻은 점수
         * 기준 좌표의 (y,x), 인접 좌표의 (y,x) 값을 순차적으로 저장한다.
         *
         * 한 턴에 한 번의 교환만 일어난다.
         */
        int[] ret = new int[5];

        return ret;
    }

    void step1() {
        /**
         * 보드를 채운다.
         * - 격자 내 빈 공간이 없어야한다.
         * - 3 개 이상 연속되는 타일이 없어야한다.
         * - 타일 교환을 통해 점수 획득이 가능해야한다.
         *      -> 이때 정보를 저장해서 타일 교체 시 바로 꺼내서 사용하면 될 듯
         */
        while(true) {
            // 1. 격자 내 빈 공간이 없어야한다.
            dropBlock();
            // 2. 3 개 이상 연속되는 타일이 없어야한다.
            if(!removeSeqBlocks()) continue;
            // 3. 타일 교환을 통해 점수 획득이 가능해야한다.
        }
    }

    void step2() {

    }

    void step3() {

    }

    void dropBlock() {
        /**
         * 빈 공간이 있다면, 블록을 떨어뜨려 처리한다.
         */
        for (int x=0; x<8; x++) {       // 가로 이동
            int dropPoint = 0;              // 블록을 떨어뜨릴 위치

            for (int y=0; y<8; y++) {   // 세로 이동
                if(board[y][x]==0) continue;
                board[dropPoint++][x] = board[y][x];        // 떨어뜨려야하는 위치가 있다면 갱신
            }

            while (dropPoint<8) {        // 아직 빈 칸이 남아있음 -> 대기중인 블록에서 채워야함
                board[dropPoint++][x] = candidateBlocks[x][candidateBlockIdx[x]++];
            }
        }
    }

    boolean removeSeqBlocks() {
        /**
         * 연속된 블록이 있는지 확인
         * -> 있다면 모두 삭제하고, False 반환
         * -> 없다면 True
         */
        boolean[][] isSeq = new boolean[8][8];
        boolean removed = true;

        for (int y=0; y<8; y++) {   // 가로
            int x = 0;
            while (x<8) {
                if (board[y][x]==0) {
                    x++;
                    continue;
                }
                int target = board[y][x];       // 인접한 영역을 찾을 블록
                int start = x;
                while (x<8 && board[y][x]==target) x++;
                if (x - start >= 3) {           // 3 개 이상의 블록이 연속적으로 있음
                    for (int i=start; i<x; i++) isSeq[y][i] = true;
                }
            }
        }

        for (int x=0; x<8; x++) {   // 세로
            int y = 0;
            while (y<8) {
                if (board[y][x] == 0) {
                    y++;
                    continue;
                }
                int target = board[y][x];       // 인접한 영역을 찾을 블록
                int start = y;
                while (y<8 && board[y][x]==target) y++;
                if (y - start >= 3) {           // 3 개 이상의 블록이 연속적으로 있음
                    for (int i = start; i < y; i++) isSeq[i][x] = true;
                }
            }
        }

        // 삭제
        for (int y=0; y<8; y++) {
            for (int x=0; x<8; x++) {
                if (isSeq[y][x]) {
                    board[y][x] = 0;
                    removed = false;
                }
            }
        }

        return removed;
    }

    boolean canChangeBlock() {
        /**
         * 오른쪽, 위쪽 으로만 교환이 가능
         */
        for(int y=0; y<8; y++) {        // 세로
            for(int x=0; x<8; x++) {    // 가로
                // 오른쪽 교환
                if(x+1<8) {

                }
                // 위쪽 교환
                if(y+1<8) {

                }
            }
        }
        return true;
    }

}
