package feb.week1.codetree;

import java.io.*;
import java.util.*;

public class AI로봇청소기_이다예 {

    static BufferedReader br;
    static StringTokenizer st;
    static int N,K,L;
    static int[][] arr;
    static List<Robot> robots;

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
            arr[robotR][robotC] = -2;
            robots.add(new Robot(robotR,robotC,0));
        }

        for (int i = 0 ; i < L; i ++){
            move();
            clean();
            addDust();
            spreadDust();
            result();
        }
    }

    // 로봇청소기 이동
    // 1. 각각의 로봇 청소기는 순서대로 이동 거리가 가까운 오염된 격자로 이동
    // 2. 물건이 위치한 격자나 청소가 있는 격자로는 지나갈 수 없음
    // 3  가까운 격자가 여러 개일 경우 행 번호가 작은 격자로 이동하고, 행 번호가 같을 경우에는 열 번호가 작은 격자로 이동
    static void move(){
        // 로봇 갯수만큼 이동
        for(int i = 0 ; i < robots.size(); i++) {
            int nowR = robots.get(i).r;
            int nowC = robots.get(i).c;
            // 가까운 격자가 여러개일 경우 행 번호가 작은 격자로 이동 , 행번호가 같으면 열 번호가 작은 격자로 이동
            PriorityQueue<Next> pq = new PriorityQueue<>((a,b )->{
                if(a.r == b.r) return a.c - b.c;
                else return a.r - b.r;
            });

            boolean find = false;

            // 오른쪽, 아래, 왼쪽, 위
            int[] dr = {0,1,0,-1};
            int[] dc = {1,0,-1,0};

            // 오염된 격자 찾을 때까지
            while(!find){
                for(int j = 0 ; j < 4 ; j++){
                    int nr = nowR + dr[j];
                    int nc = nowC + dc[j];
                    if (nr <= 0 || nr >= N+1 || nc <= 0 || nc >= N+1) continue;
                    // 이동 칸이 0 이거나 물건이 있거나 로봇이있으면 땡
                    if(arr[nr][nc] == 0 || arr[nr][nc] == -1 || arr[nr][nc] == -2) continue;
                    pq.add(new Next(nr,nc,j));
                }
                // pq에 들어간 값이 있다는건 오염된 격자가 있다는것
                if(!pq.isEmpty()) find = true;
                else {
                    // 한칸 더 가보기
                    dr[1] +=1;
                    dr[3] -= 1;
                    dc[0] +=1;
                    dc[2] -= 1;
                }
            }
            // 제일 앞에있는 칸 가져오기
            Next next = pq.poll();
            robots.get(i).r = next.r;
            robots.get(i).c = next.c;
            robots.get(i).direction = next.direction;
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

        for (int r = 1 ; r < N+1 ; r++){
            for (int c= 1; c < N+1; c++){
                if(arr[r][c] == 0){
                    int sum = 0 ;
                    for (int d = 0; d<4 ; d++){
                        int nr = r + dr[d];
                        int nc = c + dc[d];
                        if(nr < 1 || nr >= N+1 || nc < 1 || nc >= N+1) continue;
                        sum += arr[nr][nc];
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
            }
        }
        System.out.println(total);
    }

    // 다음 갈 자리
    static class Next{
        int r;
        int c;
        int direction ;

        Next(int r, int c, int direction){
            this.r = r;
            this.c = c;
            this.direction = direction;
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
