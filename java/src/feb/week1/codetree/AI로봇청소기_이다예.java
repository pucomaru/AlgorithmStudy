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
        // 로봇 청쇡의 개수
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
            robots.add(new Robot(robotR,robotC));
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
    // 3  가까운 격자가 여러 개일 경우 행 번호가 작은 격자로 이동하고, 행 번호가 같을 경우에는 열 번호가 가작은 격자로 이동
    static void move(){
        // 로봇 갯수만큼 이동
        for(int i = 0 ; i < robots.size(); i++) {
            // 가까운 격자가 여러개일 경우 행 번호가 작은 격자로 이동 , 행번호가 같으면 열 번호가 작은 격자로 이동
            PriorityQueue<Next> pq = new PriorityQueue<>((a,b )->{
                if(a.r == b.r) return a.c - b.c;
                else return a.c - b.c;
            });

            boolean find = false;

            // 오른쪽, 아래, 왼쪽, 위
            int[] dr = {0,1,0,-1};
            int[] dc = {1,0,-1,0};

            // 오염된 격자 찾을 때까지
            while(!find){
                for(int j = 0 ; j < 4 ; j++){
                    int nr = dr[j];
                    int nc = dc[j];
                    // 이동 칸이 0 이거나 물건이 있거나 로봇이있으면 땡
                    if(arr[nr][nc] == 0 || arr[nr][nc] == -1 || arr[nr][nc] == -2) continue;
                    pq.add(new Next(nr,nc));
                }
                // pq에 들어간 값이 있다는건 오염된 격자가 있다는것
                if(!pq.isEmpty()) find = true;
                else {
                    // 한칸 더 가보기
                    dr[1] +=1;
                    dr[3] -= -1;
                    dc[0] +=1;
                    dc[2] -= 1;
                }
            }
            // 제일 앞에있는 칸 가져오기
            Next next = pq.poll();
            robots.get(i).r = next.r;
            robots.get(i).c = next.c;
        }
    }

    // 먼지 청소
    static void clean(){

    }

    // 먼지 축적
    static void addDust(){

    }

    // 먼지 확산
    static void spreadDust(){

    }

    // 총 먼지량 출략
    static void result(){
        int total = 0;
    }

    static class Next{
        int r;
        int c;

        Next(int r, int c){
            this.r = r;
            this.c = c;
        }
    }
    static class Robot{
        int r;
        int c;

        Robot(int r, int c){
            this.r = r;
            this.c = c;
        }
    }
}
