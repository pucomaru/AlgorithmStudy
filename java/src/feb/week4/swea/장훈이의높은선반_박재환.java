package feb.week4.swea;

import java.util.*;
import java.io.*;

public class 장훈이의높은선반_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for(int i=0; i<tc; i++) {
            sb.append('#').append(i+1).append(' ').append(init()).append('\n');
        }
        System.out.println(sb);
        br.close();
    }
    /**
     * 높이가 B인 선반.
     * N명의 점원, 각 점원의 키는 H[i]
     *
     * 각 점원은 탑을 쌓어서 선반 위의 물건을 사용한다.
     * 탑의 높이는 점원이 1명일 경우, 그 점원의 키와 같다
     * 2명 이상일 경우 탑을 만든 모든 점원의 키의 합과 같다
     *
     * 탑의 높이가 B 이상인 경우, 선반 위의 물건을 사용할 수 있다.
     * 높이가 B이상인 탑 중, 높이가 가장 낮은 탑을 알고싶다.
     */
    static StringTokenizer st;
    static int n, b;
    static int[] arr;
    static int bestH;
    static int init() throws IOException {
        bestH = Integer.MAX_VALUE;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        b = Integer.parseInt(st.nextToken());

        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());
        findBestH(0, 0);
        return bestH - b;
    }

    static void findBestH(int id, int sum) {
        if(id == n) {
            if(sum >= b) bestH = Math.min(bestH, sum);
            return;
        }
        if(sum >= bestH) return;

        findBestH(id+1, sum + arr[id]);
        findBestH(id+1, sum);
    }
}
