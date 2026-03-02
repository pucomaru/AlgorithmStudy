package feb.week4.boj;

import java.util.*;
import java.io.*;

public class 책정리_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n;
    static int[] arr;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());

        arr = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());

        System.out.println(n-getLis());
    }

    static List<Integer> lis;
    static int getLis() {
        /**
         * LIS 를 사용해서, 움직이지 않아도 되는 원소를 구한다.
         */
        lis = new ArrayList<>();

        for(int i : arr) {
            int insertId = lis.isEmpty() ? 0 : findInsertId(i);
            if(lis.size() == insertId) lis.add(i);
            else lis.set(insertId, i);
        }

        return lis.size();
    }
    static int findInsertId(int target) {
        int l = 0, r = lis.size();
        while(l < r) {
            int mid = l + (r-l)/2;
            if(target <= lis.get(mid)) r = mid;
            else l = mid+1;
        }
        return l;
    }
}
