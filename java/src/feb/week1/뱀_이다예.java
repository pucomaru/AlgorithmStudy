package feb.week1;

import java.io.*;
import java.util.*;

public class 뱀_이다예 {

    static BufferedReader br;
    static StringTokenizer st;
    static int N,K,L;
    static int[][] game;
    static Deque<Rotate> rotates;
    // 뱀 이동 규칙
    // 1. 뱀은 몸길이를 늘려 머리를 다음칸에 위치시킴
    // 2. 벽이나 자기자신의 몸과 부딪히면 게임 끝
    // 3. 만약 이동한 칸에 사과가 있다면, 그 칸에 있던 사과가 없어지고 꼬리는 움직이지 않음
    // 4. 만약 이동한 칸에 사과가 없다면, 몸길이를 줄여서 꼬리가 위치한 칸을 비워줌. 즉 , 몸길이 변하지 않음

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));

        // 보드의 크기
        N = Integer.parseInt(br.readLine());
        // 사과의 개수
        K = Integer.parseInt(br.readLine());
        // 사과 위치 정보 저장
        for(int i = 0 ; i < K ; i++){
            st = new StringTokenizer(br.readLine());
            int appleR = Integer.parseInt(st.nextToken());
            int appleC = Integer.parseInt(st.nextToken());
            game[appleR][appleC] = 1;
        }
        // 뱀의 방향 변환 휫수
        L = Integer.parseInt(br.readLine());
        rotates = new ArrayDeque<>();

        for(int i = 0; i < L; i++){
            st = new StringTokenizer(br.readLine());
            int seconds = Integer.parseInt(st.nextToken());
            String direction = st.nextToken();
            rotates.add(new Rotate(seconds,direction));
        }


    }

    // 몇초가 끝난 뒤
    static class Rotate{
        int seconds;
        String direction;

        Rotate(int seconds, String direction){
            this.seconds = seconds;
            this.direction = direction;
        }
    }
}
