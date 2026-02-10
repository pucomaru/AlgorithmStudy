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

    // 오른쪽 - 아래 - 왼쪽 - 위
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

        // 먼지 배열
        arr = new int[N+1][N+1];
        // 로봇 있는지 확인 배열
        robotMap = new boolean[N+1][N+1];

        // 격자의 먼지 양 ( -1은 물건 위치)
        for (int i = 1 ; i < N + 1 ; i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 1; j < N+ 1 ; j++){
                arr[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        // 로봇 위치 저장 리스트
        robots = new ArrayList<>();

        for(int i = 0 ; i < K ; i++){
            st = new StringTokenizer(br.readLine());
            int robotR = Integer.parseInt(st.nextToken());
            int robotC = Integer.parseInt(st.nextToken());

            robots.add(new Robot(robotR,robotC));
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
    static Next bfsFind(int nowR, int nowC) {

        boolean[][] visited = new boolean[N+1][N+1];
        ArrayDeque<Next> dq = new ArrayDeque<>();

        // 먼지 있는 최단거리 저장 변수
        int findDist = -1;

        dq.add(new Next(nowR,nowC,0));
        visited[nowR][nowC] = true;
        int bestR = Integer.MAX_VALUE;
        int bestC = Integer.MAX_VALUE;

        while(!dq.isEmpty()){
            Next cur = dq.poll();
            // 먼지를 찾았을 시 더 볼 필요가 없음.
            if(findDist !=-1 && cur.dist > findDist) break;

            if(arr[cur.r][cur.c] > 0){
                if(findDist == -1) findDist = cur.dist;

                // 같은 최단거리에서 (r,c) 최소
                if(cur.r < bestR || (cur.r==bestR && cur.c < bestC)){
                    bestR = cur.r;
                    bestC = cur.c;
                }
            }

            // 4 방향 이동
            for(int d = 0 ; d < 4 ; d++){
                int nr = cur.r + DR[d];
                int nc = cur.c + DC[d];

                if(nr <=0 || nr >= N+1 || nc <=0 || nc >=N+1) continue;

                // 가려는 방향에 로봇이 있거나 물건이 있으면 못감
                if(robotMap[nr][nc]) continue;
                if(arr[nr][nc] == -1) continue;
                // 이미 간곳이면 패스
                if(visited[nr][nc]) continue;

                visited[nr][nc] = true;
                dq.add(new Next(nr,nc,cur.dist+1));
            }
        }

        if(findDist==-1) return new Next(nowR,nowC,0);

        return new Next(bestR,bestC,0);
    }


    // 먼지 청소
    static void clean(){
        for(int i = 0 ; i < robots.size(); i++) {
            int robotR = robots.get(i).r;
            int robotC = robots.get(i).c;

            // bestFace : 로봇이 최종적으로 바라볼 방향(0:오 1:아 2:왼 3:위)
            int bestFace = 0;
            int bestSum = -1;

            for(int face = 0; face < 4; face++){
                int back = (face+2)%4;
                int sum = 0;

                for(int d= 0 ; d<4; d++){
                    if(d == back) continue;;

                    int nr = robotR + DR[d];
                    int nc = robotC + DC[d];

                    if(nr <= 0 || nr >= N+1 || nc <=0 || nc>= N+1) continue;
                    // 물건이 있는 경우 안더함
                    if(arr[nr][nc] == -1) continue;

                    sum += Math.min(arr[nr][nc],20);
                }

                // 동점이면 우선순위(오른쪽,아래,왼쪽,위) 유지 => ">" 일때만 갱신
                if (sum > bestSum){
                    bestSum= sum;
                    bestFace = face;
                }
            }
            int bestBack = (bestFace + 2) % 4;
            // bestExclude 제외하고 나머지 3칸 청소
            for (int d= 0 ; d<4 ;d++){
                if (d==bestBack) continue;

                int nr = robotR + DR[d];
                int nc = robotC + DC[d];

                if (nr <= 0 || nr >= N+1 || nc<=0 || nc >= N+1) continue;
                if (arr[nr][nc]==-1) continue;

                arr[nr][nc] -=20;
                if(arr[nr][nc] <0 ) arr[nr][nc] = 0;
            }

            arr[robotR][robotC] -= 20;
            if(arr[robotR][robotC] <0 ) arr[robotR][robotC] = 0;

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
        int[][] add = new int[N+1][N+1];

        for (int r = 1; r < N+1 ; r++){
            for (int c = 1; c < N+1 ; c++){
                if(arr[r][c]!=0) continue;
                int sum = 0 ;

                for (int d = 0 ; d < 4; d++){
                    int nr = r + DR[d];
                    int nc = c + DC[d];

                    if( nr <= 0 || nr >= N+1 || nc <=0 || nc >= N+1) continue;
                    if(arr[nr][nc] == -1) continue;

                    sum += arr[nr][nc];
                }
                add[r][c] = sum/10;
            }
        }

        // 동시 확산
        for (int r = 1; r < N+1 ;r++){
            for (int c = 1; c < N+1 ;c++){
                if(arr[r][c] == -1) continue;
                arr[r][c] += add[r][c];
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
        int dist ;

        Next(int r, int c , int dist){
            this.r = r;
            this.c = c;
            this.dist = dist;
        }
    }

    // 로봇 위치 (direction 은 0: 오른쪽 / 1: 아래 / 2: 왼쪽 / 3: 위 )
    static class Robot{
        int r;
        int c;

        Robot(int r, int c){
            this.r = r;
            this.c = c;
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
