package jan.week5.boj;

import java.util.*;
import java.io.*;

public class 수들의합4_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        st = new StringTokenizer(br.readLine().trim());
        int n = Integer.parseInt(st.nextToken());
        int k = Integer.parseInt(st.nextToken());
        int[] sum = new int[n+1];
        Map<Integer, Integer> map = new HashMap<>();
        st = new StringTokenizer(br.readLine().trim());
        long answer = 0;
        for(int i=1; i<n+1; i++) {
            int num = Integer.parseInt(st.nextToken());
            sum[i] = sum[i-1] + num;
            if(sum[i] == k) answer++;
            answer += map.getOrDefault(sum[i]-k, 0);
            map.put(sum[i], map.getOrDefault(sum[i], 0) + 1);
        }
        System.out.println(answer);
    }
}
