package jan.week2.boj;

import java.io.*;
import java.util.*;

// 이분탐색 + greedy
public class 공유기설치_이다예 {

    static BufferedReader br;
    static StringTokenizer st;
    static int N,C ;
    static int[] location;
    static boolean calculate(int d) {
        // 첫번째 집은 일단 공유기 설치
        int count = 1;
        // 제일 최근에 공유기 설치된 집
        int idx = 0;

        for (int i = 1; i < location.length; i++){
            if (location[i] - location[idx] >= d) {
                count ++;
                idx = i;
            }
            if (count >= C) return true;

        }

        return false;
    }

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());

        // 집의 개수
        N = Integer.parseInt(st.nextToken());
        // 공유기 개수
        C = Integer.parseInt(st.nextToken());

        // 집의 위치
        location = new int[N];
        for (int i = 0; i < N ; i++){
            location[i] = Integer.parseInt(br.readLine());
        }

        // 가장 먼 거리 알기위해 집 정렬
        Arrays.sort(location);

        // 가장 최소 거리
        int low = 1;
        // 가장 최대 거리
        int high = location[N-1] - location[0];

        // 결과 (인접한 공유기 최대 거리)
        int answer = 0 ;
        while (low <= high) {
            int mid = (low + high) / 2;

            if (calculate(mid)) {
                answer = mid;
                low = mid + 1 ;
            } else {
                high = mid - 1;
            }

        }

        System.out.println(answer);

    }
}