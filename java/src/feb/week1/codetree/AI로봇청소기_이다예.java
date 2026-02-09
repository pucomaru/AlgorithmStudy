package feb.week1.codetree;

import java.io.*;
import java.util.*;

public class AI로봇청소기_이다예 {

    static BufferedReader br;
    static StringTokenizer st;
    static int N,K,L;
    static int[][] arr;
    static boolean[][] robotMap;
    static List<Robot> robots;

    // 오른쪽 아래 왼쪾 위
    static final int[] DR = {0,1,0,-1};
    static final int[] DC = {1,0,-1,0};

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());

        // 격자의 크기
        N = Integer.parseInt(st.nextToken());
        // 로봇 청소기의 개수
        K = Integer.parseInt(st.nextToken());
        // 테스트 횟수
        L = Integer.parseInt(st.nextToken());

        arr = new int[N+1][N+1];
        robotMap = new boolean[N+1][N+1];


        // 격자의 먼즤 양 ( -1은 물건 위치)
        for (int i = 1 ; i < N + 1 ; i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 1; j < N+ 1 ; j++){
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 로봇 위치 저장 리스트
        robots = new ArrayList<>();
        // -2는 로봇의 위치
        for(int i = 0 ; i < K ; i++){
            st = new StringTokenizer(br.readLine());
            int robotR = Integer.parseInt(st.nextToken());
            int robotC = Integer.parseInt(st.nextToken());

            robots.add(new Robot(robotR,robotC,0));
            // 로봇 위치 표시
            robotMap[robotR][robotC] = true;
        }

        for (int i = 0 ; i < L; i ++){
            move(); // 이동(BFS)
            clean(); // 청소(방향 선택 후 4칸)
            addDust(); // 먼지 +5
            spreadDust(); // 확산(동시)
            result();// 통합 출력
        }
    }

    // 로봇청소기 이동
    // 1. 각각의 로봇 청소기는 순서대로 이동 거리가 가까운 오염된 격자로 이동
    // 2. 물건이 위치한 격자나 청소가 있는 격자로는 지나갈 수 없음
    // 3  가까운 격자가 여러 개일 경우 행 번호가 작은 격자로 이동하고, 행 번호가 같을 경우에는 열 번호가 작은 격자로 이동
    static void move(){
        // 로봇 갯수만큼 이동
        for(int i = 0 ; i < robots.size(); i++) {
            Robot rb = robots.get(i);

            int nowR = rb.r;
            int nowC = rb.c;

            // BFS 탐색 중에 내 칸이 장애물이면 안되므로 잠깐 비움
            robotMap[nowR][nowC] = false;

            // Bfs로 가장가까운 칸 찾기
            Next target = bfsFind(nowR,nowC);

            rb.r = target.r;
            rb.c = target.c;

            robotMap[rb.r][rb.c] = true;

        }
    }

    // BFS 결과 : 도달 가능한 먼지칸
    // 1. 제일 가까운
    // 2. 행(R) 최소
    // 3. 열(C) 최소
    static Next bfsFind(int nowR, int nowC){
        boolean[][] visited = new boolean[N+1][N+1];
        ArrayDeque<Next> dq = new ArrayDeque<>();

        // 아직 먼지를 못찾았으면
        int findDust = -1;
        // 최단거리에서 만난 칸들중 행/열 가장 작은 좌표 표시
        int bestR = Integer.MAX_VALUE;
        int bestC = Integer.MIN_VALUE;

        dq.add(new Next(nowR,nowC,0,0));
        visited[nowR][nowC] = true;

        while(!dq.isEmpty()) {
            Next cur = dq.poll();

            // 처음 먼지를 찾은 순간이 최단거리
            if (findDust != -1 || cur.dist > findDust) break;

            // 같은 최단거리에서 행/열이 더 작은 칸으로 best 갱신
            if (cur.r < bestR || (cur.r == bestR && cur.c < bestC)) {
                bestR = cur.r;
                bestC = cur.c;
            }

            for (int d =0 ; d < 4 ; d++){
                int nr = cur.r + DR[d];
                int nc = cur.c  +DC[d];

                if(nr <= 0 || nr >= N+1 || nc <= 0 || nc >= N+1) continue;
                if (arr[nr][nc] == -1) continue; // 물건이 있을 경우
                if (robotMap[nr][nc]) continue; // 로봇이 있을꼉우
                if (visited[nr][nc]) continue;

                visited[nr][nc] = true;
                dq.add(new Next(nr,nc,d,cur.dist+1));
            }

            return new Next(bestR, bestC,0,findDust);

        }






    }

    // 먼지 청소
    static void clean(){
        for(int i = 0 ; i < robots.size(); i++){
            int robotR = robots.get(i).r;
            int robotC = robots.get(i).c;
            int direction = robots.get(i).direction;

            // 본인 오른쪽  , 아래, 왼쪽, 위
            int[][] dr = {{0,0, 1, -1},{0,0,1,0},{0,1, 0, -1},{0,0, 0, -1}};
            int[][] dc = {{0,1, 0, 0},{0,1,0,-1},{0,0, -1, 0},{0,1, -1, 0}};

            PriorityQueue<Dust> pq = new PriorityQueue<>((a,b)->{
                // 합이 같은 방향이 여러개인 경우, 오른쪽, 아래쪽, 왼쪽, 위쪽 방향의 우선순위로 방향 선택
                if (a.sum == b.sum) return a.direction - b.direction;
                else return b.sum - a.sum;
            });

            // 청소할 수 있는 최대 먼지량은 20
            // 오른쪽 방향/ 아래쪽 / 왼쪽 /위
            for(int k = 0 ;k < 4; k++){
                if(direction == k){
                    for(int d=0 ; d < 4; d++){
                        int nr = robotR + dr[k][d];
                        int nc = robotC + dc[k][d];
                        // 배열을 벗어났을 때 1 <= n <= N
                        if(nr <= 0 || nr >= N+1 || nc <=0 || nc >= N+1) continue;
                        if (arr[nr][nc] > 0){
                            pq.add(new Dust(nr,nc,arr[nr][nc],d));
                        }

                    }
                    break;
                }
            }
            while(!pq.isEmpty()){
                Dust cleanDust = pq.poll();
                if ( 20 - cleanDust.sum >= 0){
                    arr[cleanDust.r][cleanDust.c] = 0;
                }else {
                    arr[cleanDust.r][cleanDust.c] = cleanDust.sum - 20;
                }
            }
        }
    }

    // 먼지 축적
    static void addDust(){
        for(int r=1; r < N+1; r++){
            for(int c=1; c < N+1; c++){
                if(arr[r][c]>0){
                    arr[r][c] += 5;
                }
            }
        }
    }

    // 먼지 확산
        static void spreadDust(){
            // 오른쪽 , 아래, 왼쪽, 위
            int[] dr = {0,1,0,-1};
            int[] dc = {1,0,-1,0};
            int[][] temp = arr;

            for (int r = 1 ; r < N+1 ; r++){
                for (int c= 1; c < N+1; c++){
                    if(temp[r][c] == 0){
                        int sum = 0 ;
                        for (int d = 0; d < 4 ; d++){
                            int nr = r + dr[d];
                            int nc = c + dc[d];
                            if(nr < 1 || nr >= N+1 || nc < 1 || nc >= N+1) continue;
                            if(temp[nr][nc] <=0) continue;
                            sum += temp[nr][nc];
                        }
                        arr[r][c] = sum / 10;
                    }
                }
        }

    }

    // 총 먼지량 출략
    static void result(){
        int total = 0;
        for(int i = 1; i < N+1; i++){
            for (int j = 1; j < N+1 ; j++){
                if(arr[i][j] > 0) total += arr[i][j];
//                System.out.print(arr[i][j]+" ");
            }
//            System.out.println();
        }
        System.out.println(total);
    }

    // 다음 갈 자리
    static class Next{
        int r;
        int c;
        int direction ;
        int dist ;

        Next(int r, int c , int direction, int dist){
            this.r = r;
            this.c = c;
            this.direction = direction;
            this.dist = dist;
        }
    }

    // 로봇 위치 (direction 은 0: 오른쪽 / 1: 아래 / 2: 왼쪽 / 3: 위 )
    static class Robot{
        int r;
        int c;
        int direction;

        Robot(int r, int c, int direction){
            this.r = r;
            this.c = c;
            this.direction = direction;
        }
    }

    // 먼지 위치
    static class Dust{
        int r;
        int c;
        int sum;
        int direction;

        Dust(int r, int c , int sum, int direction){
            this.r = r;
            this.c = c;
            this.sum = sum;
            this.direction = direction;
        }
    }

}
