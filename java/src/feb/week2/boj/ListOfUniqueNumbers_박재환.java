package feb.week2.boj;

import java.util.*;
import java.io.*;

public class ListOfUniqueNumbers_박재환 {
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
        System.out.println(getAllCombi());
    }
    /**
     * 연속된 수의 길이를 1 ~ n 까지 늘려가며 순열 확인
     * => n**2 -> n 은 최대 100,000 => 시간 초과
     *
     * 투포인터 O(N) 사용
     * [left, right) -> 부분수열 길이가 1일 수 있으니, (l < n) 조건 사용
     */
    static long getAllCombi() {
        long result = 0L;
        // 해당 수가 현재 수열에 포함되어 있는지를 확인
        boolean[] visited = new boolean[1000001];       // 수열 내 원소는 100,000까지 사용 가능
        int l=0, r=0;

        while(l < n) {
            /**
             * 현재 l 의 위치에서 중복되지 않는 원소가 있는 위치까지 r 을 이동
             */
            while(r < n && !visited[arr[r]]) {
                visited[arr[r]] = true;
                r++;
            }
            /**
             * 현재 l과, r 위치에서 만들 수 있는 부분 수열 개수
             */
            result += (r - l);
            /**
             * l 이동시켜서 범위 변경
             */
            visited[arr[l++]] = false;
        }
        return result;
    }
}
