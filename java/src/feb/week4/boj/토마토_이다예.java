package feb.week4.boj;

import java.io.*;
import java.util.*;

// bfs 문제 같음?
public class 토마토_이다예 {
    static BufferedReader br;
    static StringTokenizer st;
    static int R,C;
    static int[][] tomatoes;
    static int ripe ;
    static int days;
    static ArrayDeque<Tomato> tomato ;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());

        // 가로
        C = Integer.parseInt(st.nextToken());
        // 세로
        R = Integer.parseInt(st.nextToken());
        // 토마토 배열
        tomatoes = new int[R][C];
        // 안익은 토마토
        ripe = 0;
        // 날짜
        days = 0;
        // 이동 토마토 저장
        tomato = new ArrayDeque<>();
        for(int r = 0; r < R ; r++){
            st = new StringTokenizer(br.readLine());
            for (int c = 0 ; c < C ; c++){
                int num = Integer.parseInt(st.nextToken());
                tomatoes[r][c] = num;
                if (num == 0) ripe++;
                else if(num ==1)  tomato.add(new Tomato(r,c,0));
                
            }
        }

        if (ripe != 0) {
            bfs();
            System.out.println(days);
        }
        else System.out.println(0);
    }

    static void bfs() {
        int[] dr = {0, 1, 0, -1};
        int[] dc = {1, 0, -1, 0};

        while (!tomato.isEmpty()) {
            Tomato nowTomato = tomato.poll();

            for (int d = 0; d < 4; d++) {
                int nr = nowTomato.r + dr[d];
                int nc = nowTomato.c + dc[d];

                if ( nr < 0 || nr >= R || nc < 0 || nc >= C ) continue;
                if (tomatoes[nr][nc] != 0 ) continue;

                tomatoes[nr][nc] = 1;
                int nd = nowTomato.day +1;

                if (nd > days) days = nd;
                ripe --;

                tomato.add(new Tomato(nr,nc,nd));

            }
        }


        if(ripe != 0) days= -1;
    }

    static class Tomato{
        int r;
        int c;
        int day;

        Tomato(int r, int c, int day){
            this.r= r;
            this.c = c;
            this.day = day;
        }
    }

}
