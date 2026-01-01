package jan.week1.codetree;

import java.util.*;
import java.io.*;

public class 택배하차_박재환 {
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
     * N x N 격자
     * 1. 택배 투입
     *      - 직사각형 모양으로, 왼쪽 열의 위치 c, 가로 크기 w, 세로 크기 h
     *      - 각 택배는 번호를 갖는다
     * 2. 택배 하차
     *      - (죄측) : 택배를 잡고 왼쪽으로 이동했을 때, 막히지 않는 것부터 하차
     *      - (우측) : 택배를 잡고 오른쪽으로 이동했을 때, 막히지 않는 것부터 하차
     *    만약 이 조건을 만족하는 것들이 여러 개 있다면, 번호가 작은 것부터 확인
     *
     * 공간에 있는 택배를 모두 하차할 때까지 2번 과정을 반복
     * ======================================================================
     * 제한시간 : 1 초
     */
    static StringTokenizer st;
    static int n, m;
    static int[][] board;
    static Queue<PostCommand> commands;
    static void init() throws IOException {
        commands = new ArrayDeque<>();

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());   // 격자 크기
        m = Integer.parseInt(st.nextToken());   // 택배 개수 (최대 100개)

        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int id = Integer.parseInt(st.nextToken());
            int height = Integer.parseInt(st.nextToken());
            int width = Integer.parseInt(st.nextToken());
            int col = Integer.parseInt(st.nextToken())-1;

            commands.offer(new PostCommand(id, height, width, col));
        }

        solution();
    }

    static class PostCommand {
        int id;
        int height;
        int width;
        int col;

        public PostCommand(int id, int height, int width, int col) {
            this.id = id;
            this.height = height;
            this.width = width;
            this.col = col;
        }
    }

    /**
     * 모든 택배를 하차한다 -> commands.isEmpty() == true
     *
     * while() {
     *     1. 박스 떨어뜨리기 => 매 턴마다 초기 상태 세팅
     *     2. 왼쪽에서 박스를 하나 뽑습니다.
     *     3. 박스를 떨어뜨립니다.
     *     4. 오른쪽에서 박스를 하나 뽑습니다.
     *     5. 박스를 떨어뜨립니다.
     * }
     */
    static Set<Integer> processedPost;
    static void solution() {
        processedPost = new HashSet<>();        // 처리가 완료된 택배 리스트를 저장

        while(!commands.isEmpty()) {
            dropPosts();        // 매 턴마다 새로운 택배 상태 갱신

            pickPostLeft();     // 왼쪽에서 택배를 뽑는다.
            dropPosts();

            pickPostRight();    // 오른쪽에서 택배를 뽑는다.
            dropPosts();
        }
    }

    /**
     * commands 내부에 있는 택배를 떨어뜨리는 명령어를 실행합니다.
     */
    static void dropPosts() {
       board = new int[n][n];
       Queue<PostCommand> tempCommands = new ArrayDeque<>();

       while(!commands.isEmpty()) {
           PostCommand command = commands.poll();
           if(processedPost.contains(command.id)) continue;     // 이미 처리된 값들은 다시 처리하지 않음

           int id = command.id;
           int height = command.height;
           int width = command.width;
           int col = command.col;

           boolean isOk = true;
           int bottom = 0;
           for(int x=0; x<n; x++) {                  // 0 ~ n 까지 순차적으로 탐색하며
               for(int y=col; y<col+width; y++) {   // 택배가 차지할 공간을 확인
                   if(board[x][y] == 0) continue;  // 놓을 수 있음
                   isOk = false;                  // 놓을 수 없음
                   break;
               }
               if(isOk) bottom = x;
               else break;
           }

           for(int x=bottom; x>bottom-height; x--) {
               for(int y=col; y<col+width; y++) {
                   board[x][y] = id;
               }
           }

           tempCommands.offer(command);
       }

       commands = tempCommands;
    }

    static void pickPostLeft() {
        int minId = 101;
        Set<Integer> tempPostIdx = new HashSet<>();
        for(int x=0; x<n; x++) {
            int detectedPost = 0;
            int col = -1;
            for(int y=0; y<n; y++) {    // 왼쪽에서 뽑을 수 있는 후보 찾기
                if(board[x][y] != 0 && !tempPostIdx.contains(board[x][y])) {
                    tempPostIdx.add(board[x][y]);
                    detectedPost = board[x][y];
                    col = y;
                    break;
                }
            }
            if(detectedPost == 0) continue;

            int row = x;
            for(; row<n; row++) {
                if(board[row][col] != detectedPost) {
                    break;
                }
            }

            // 밀어내기
            boolean isOk = true;
            for(int y=0; y<col; y++) {
                for(int nx=x; nx<row; nx++) {
                    if(board[nx][y] != 0) {
                        isOk = false;
                        break;
                    }
                    if(!isOk) break;
                }
            }

            if(isOk) minId = Math.min(minId, detectedPost);
        }

        if(minId == 101) return;

        processedPost.add(minId);
        sb.append(minId).append('\n');
    }

    static void pickPostRight() {
        int minId = 101;
        Set<Integer> tempPostIdx = new HashSet<>();
        for(int x=0; x<n; x++) {
            int detectedPost = 0;
            int col = -1;
            for(int y=n-1; y>-1; y--) {    // 오른쪽에서 뽑을 수 있는 후보 찾기
                if(board[x][y] != 0 && !tempPostIdx.contains(board[x][y])) {
                    tempPostIdx.add(board[x][y]);
                    detectedPost = board[x][y];
                    col = y;
                    break;
                }
            }
            if(detectedPost == 0) continue;

            int row = x;
            for(; row<n; row++) {
                if(board[row][col] != detectedPost) {
                    break;
                }
            }
            
            // 밀어내기
            boolean isOk = true;
            for(int y=n-1; y>col; y--) {
                for(int nx=x; nx<row; nx++) {
                    if(board[nx][y] != 0) {
                        isOk = false;
                        break;
                    }
                    if(!isOk) break;
                }
            }

            if(isOk) minId = Math.min(minId, detectedPost);
        }

        if(minId == 101) return;

        processedPost.add(minId);
        sb.append(minId).append('\n');
    }
}
