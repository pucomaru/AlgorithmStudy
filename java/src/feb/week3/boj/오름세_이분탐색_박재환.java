package feb.week3.boj;

import java.util.*;
import java.io.*;

public class 오름세_이분탐색_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static void init() throws IOException {
        String input;
        while((input = br.readLine()) != null) {
            if(input.isEmpty()) break;
            int n = Integer.parseInt(input.trim());
            int[] arr = new int[n];
            st = new StringTokenizer(br.readLine().trim());
            for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());
            int result = getListByBinarySearch(n, arr);
            System.out.println(result);
        }
    }
    static List<Integer> lisList;
    static int getListByBinarySearch(int n, int[] arr) {
        lisList = new ArrayList<>();
        for(int i : arr) {
            int insertIdx = lisList.isEmpty() ? 0 : findInsertIdx(i);
            if(insertIdx == lisList.size()) lisList.add(i);
            else lisList.set(insertIdx, i);
        }
        return lisList.size();
    }

    /**
     * [Lower Bound]
     * target 보다 큰 값중 가장 작은 값
     */
    static int findInsertIdx(int target) {
        int l = 0, r = lisList.size();
        while(l < r) {
            int mid = l + (r-l)/2;
            if(target <= lisList.get(mid)) r = mid;
            else l = mid+1;
        }
        return l;
    }
}
