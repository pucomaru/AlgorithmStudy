package feb.week2.boj;

import java.io.*;
import java.util.*;

public class 색종이만들기_이다예 {

    static BufferedReader br;
    static StringTokenizer st;
    static int N;
    static int[][] paper;
    static int white,blue;


    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        // N : 한변의 길이
        N = Integer.parseInt(br.readLine());
        // 색종이 정보 배열
        paper = new int[N][N];

        // 색종이 저장
        for(int i = 0 ; i < N ; i++){
            st = new StringTokenizer(br.readLine());
            for (int j = 0 ; j < N ; j++){
                paper[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        white = 0;
        blue = 0;

        dfs(0,0,N);
        System.out.println(white);
        System.out.println(blue);
    }

    // 만약에 8이면 0,0 (2분위)/ 0, 4 (1분위_ / 4, 0(3분위) / 4,4 (4분위)
    // 매개변수명은 static 변수명이랑 같게 하지말자..
    static void dfs(int x, int y, int nowSize){
        // N이 1일 경우에는 더 쪼개질 크기가없다.
        if(nowSize==1){
            if(paper[y][x] == 1) blue++;
            else white++;
            return;
        }

        // 현재가 다 같은것도 확인...예외 ㅠ
        if(check(x,y,nowSize)){
            if(paper[y][x] == 1) blue++;
            else white++;
            return;
        }

        // 1분위 [x + (N/2) ,y] 2분위 [x,y] 3분위 [x,y +(N/2)] 4분위 [x+(N/2), y+(N/2)]
        int[] dy = {0, 0, nowSize/2, nowSize/2};
        int[] dx = {nowSize/2,0,0,nowSize/2};

//        dfs(x+(nowSize/2),y,nowSize);
//        dfs(x,y,nowSize);
//        dfs(x,y+(nowSize/2),nowSize);
//        dfs(x+(nowSize/2),y+(nowSize/2),nowSize);

        for (int i = 0 ; i < 4 ; i++){
            int ny = y + dy[i];
            int nx = x + dx[i];
            boolean can = check(nx,ny,nowSize/2);
            if(can) {
                if(paper[ny][nx] == 1) blue++;
                else white++;
            }
            else dfs(nx,ny,nowSize/2);
        }

    }

    static boolean check(int x,int y,int halfN){
        int comp = paper[y][x];

        for (int i = y; i < y + halfN ; i++){
            for(int j = x ; j < x + halfN; j++){
                if(comp != paper[i][j]) return false;
            }
        }
        return true;
    }
}
