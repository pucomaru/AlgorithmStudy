package jan.week1.boj;

import java.util.*;
import java.io.*;

public class 가장긴증가하는부분수열5_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        sb = new StringBuilder();
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
        System.out.println(sb);
    }

    static StringTokenizer st;
    static int n;
    static int[] arr;
    static int[] arrLoc;
    static List<Integer> lisList;
    static void init() throws IOException {
        lisList = new ArrayList<>();
        n = Integer.parseInt(br.readLine().trim());
        arr = new int[n];
        arrLoc = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(st.nextToken());
        getLis();

        int lisLen = lisList.size();
        sb.append(lisLen).append('\n');
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> Integer.compare(a, b));
        for(int i=n-1; i>-1 && lisLen > 0; i--) {
            if(arrLoc[i] == lisLen) {
                lisLen--;
                pq.offer(arr[i]);
            }
        }
        while(!pq.isEmpty()) sb.append(pq.poll()).append(' ');
    }

    static void getLis() {
        for(int i=0; i<n; i++) {
            int target = arr[i];
            if(lisList.isEmpty() || lisList.get(lisList.size()-1) < target) {
                lisList.add(target);
                arrLoc[i] = lisList.size();
                continue;
            }

            int insertIdx = insertIdx(target);
            lisList.set(insertIdx, target);
            arrLoc[i] = insertIdx+1;
        }
    }

    static int insertIdx(int target) {
        int l = 0, r = lisList.size()-1;

        while(l < r) {
            int mid = l + (r-l)/2;

            if(lisList.get(mid) >= target) r = mid;
            else l = mid+1;
        }

        return l;
    }
}
