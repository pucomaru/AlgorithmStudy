package jan.week2.boj;

import java.io.*;
import java.util.*;

public class 벽타기_이다예_틀린풀이 {

    static BufferedReader br;
    static StringTokenizer st;
    // H : 행 // W : 열
    static int H;
    static int W;

    // 지도
    static char[][] map;
    // dp 지도
    static int[][] dp;
    // 주변 war이 있는지 확인하는 지도
    static boolean[][] mapWar;

    // 시작점
    static int startRow;
    static int startCol;

    // 도착점
    static int endRow;
    static int endCol;

    // 일반 이동  ( 동, 남, 서, 북)
    static int[] dx = {1, 0, -1, 0};
    static int[] dy = {0, -1, 0 ,1};

    // 결과값
    static int result;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());

        H = Integer.parseInt(st.nextToken());
        W = Integer.parseInt(st.nextToken());

        map = new char[H][W];
        dp = new int[H][W];
        mapWar = new boolean[H][W];

        result = Integer.MAX_VALUE;

        // 지도에 벽이랑 이제 빈칸 주입
        for(int i = 0; i < H; i++){
            String line  = br.readLine();
            for (int j =0; j < W ; j++){
                map[i][j] = line.charAt(j);
                // for문 도는김에 dp에 최댓값들 넣기
                dp[i][j] = Integer.MAX_VALUE;
            }
        }


        // 시작 점 찾기
        for (int i = 0 ; i < H; i++){
            for (int j = 0 ; j < W ; j++){
                if (map[i][j] == 'S'){
                    startRow = i;
                    startCol = j;
                }
            }
        }

        // 끝 점 찾기
        for (int i = 0 ; i < H ; i++){
            for (int j = 0; j < W ; j++){
                if (map[i][j] == 'E'){
                    endRow = i;
                    endCol = j;
                }
            }
        }

        // 벽 있는지 체크
        for ( int i = 0 ; i < H ; i++){
            for ( int j = 0 ; j < W ; j++){
                for (int k = 0 ; k < 4 ; k++){
                    int nx = i + dx[k];
                    int ny = j + dy[k];

                    if (nx < 0 || nx >= W || ny < 0 || ny >= H || (mapWar[i][j]) ) continue;
                    // #은 벽
                    if (map[ny][nx] =='#') mapWar[i][j] = true;
                }
            }
        }

        dp(startRow, startCol,0);

        System.out.println(result);
    }

    // # = 벽 , . = 빈칸
    static void dp(int nowRow, int nowCol, int move){

        // 현재 있는 위치가 이제 도착점이면 result 확인
        if (nowRow == endRow && nowCol == endCol){
            if (move < result) result = move;
            return;
        }

        // 지금 이동횟수가 dp 맵 해당 칸에서의 이동횟수보다 크면 최소시간을 출력하는것에 어긋나니까 해당 루트는 더 계산할 필요가 없음
        if (dp[nowRow][nowCol] <= move) return;

        dp[nowRow][nowCol] = move;


        // 이제 이동
        // 벽이 있는지 체크
        for (int i = 0; i < 4 ; i++){
            int nx = nowCol + dx[i];
            int ny = nowRow + dy[i];
            if ( nx < 0 || nx >= W || ny < 0 || ny >= H ) continue;

            // 이동한 칸이 빈칸 일때만 이동
            if (!(map[ny][nx] == '#')){
                if (mapWar[nowRow][nowCol] && mapWar[ny][nx]) dp(ny,nx,move);
                else dp(ny,nx,move + 1);
            }
        }
    };

}
