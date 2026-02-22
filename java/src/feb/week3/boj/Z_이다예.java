package feb.week3.boj;

import java.io.*;
import java.util.*;
public class Z_이다예 {
    static BufferedReader br;
    static StringTokenizer st;
    static int N, r, c;
    static int total;
    static int result;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());
        N = Integer.parseInt(st.nextToken());
        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());

        total = 1;

        // 제곱 나타내기
        for (int i = 0; i < N; i++) {
            total *= 2;
        }

        result = 0;
        z(0, 0, total);
        System.out.println(result);
    }

    static void z(int row, int col, int t) {
        if(t==1) return;

        int half = t/2;

        // 1사분면
        if(r < row +half && c < col+half){
            z(row,col,half);
        } // 2사분면
        else if (r < row +half && c >= col+half){
            result += half*half;
            z(row,col+half,half);
        } // 3사분면
        else if(r >= row +half && c < col+half){
            result += (half*half) * 2;
            z(row+half,col,half);
        } else{ // 4사분면
            result += (half*half) * 3;
            z(row+half,col+half,half);
        }
    }
}