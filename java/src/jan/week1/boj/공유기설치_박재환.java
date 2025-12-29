package jan.week1.boj;

import java.util.*;
import java.io.*;

public class 공유기설치_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n, maxWifi;
    static int[] arr;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        maxWifi = Integer.parseInt(st.nextToken());
        arr = new int[n];
        for(int i=0; i<n; i++) arr[i] = Integer.parseInt(br.readLine().trim());
        System.out.println(getBestBetweenDist());
    }

    /**
     * 가장 인접한 두 공유기 사이의 거리를 기준으로 Binary Search 진행
     * 
     * 1. 공유기 대수를 maxWiFi 이상 설치 가능할 때 => 탐색 범위를 증가시킴
     * 2. maxWiFi 미만일 때, 탐색 범위 감소시킴
     */
    static int getBestBetweenDist() {
        Arrays.sort(arr);
        int l = 1, r = arr[arr.length-1] - arr[0];
        int result = Integer.MIN_VALUE;
        while(l <= r) {
            int mid = l + (r-l)/2;
            if(isSatisfy(mid) >= 0) {
                l = mid+1;
                result = Math.max(result, mid);
            }
            else r = mid-1;

        }

        return result;
    }

    static int isSatisfy(int dist) {
        int wifi = 1;
        int lastLoc = arr[0];
        for(int i=1; i<n; i++) {
            if(arr[i] - lastLoc >= dist) {
                lastLoc = arr[i];
                wifi++;
            }
        }
        return wifi - maxWifi;
    }
}
