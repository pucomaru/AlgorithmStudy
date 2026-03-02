package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 주식_BinarySearch_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for(int i=0; i<tc; i++) {
            sb.append("Case ").append('#').append(i+1).append('\n').append(init()).append('\n');
        }
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n, k;
    static List<Integer> lis;
    static int init() throws IOException {
        lis = new ArrayList<>();

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) {
            int target = Integer.parseInt(st.nextToken());
            int insertId = lis.isEmpty() ? 0 : findInsertId(target);
            if(insertId == lis.size()) lis.add(target);
            else lis.set(insertId, target);
        }

        return lis.size() >= k ? 1 : 0;
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
