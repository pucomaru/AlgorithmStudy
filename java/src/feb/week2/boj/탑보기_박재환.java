package feb.week2.boj;

import java.util.*;
import java.io.*;

public class 탑보기_박재환 {
    /**
     * 다양한 높이의 건물 총 N개가 존재한다.
     * 건물높이를 L이라고 한다면, 높이가 L보다 큰 건물만 볼 수 있다.
     *
     * 바라보는 방향으로 높이가 L인 건물 뒤에 높이가 L이하인 건물이 있다면 가려져 보이지 않는다.
     */
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n;
    static Building[] arr;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        arr = new Building[n+1];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=1; i<n+1; i++) arr[i] = new Building(i, Integer.parseInt(st.nextToken()));
        solution();
    }
    static class Building {
        int id, height;
        Building(int id, int height) {
            this.id = id;
            this.height = height;
        }
    }
    static void solution() {
        Deque<Building> stack = new ArrayDeque<>();
        int[] canSee = new int[n+1];
        int[] nearId = new int[n+1];

        // i번째 위치에서 왼쪽으로 볼 수 있는 빌딩
        for(int i=1; i<n+1; i++) {
            while(!stack.isEmpty() && stack.peek().height <= arr[i].height) stack.pop();
            canSee[i] += stack.size();
            if(!stack.isEmpty()) nearId[i] = stack.peek().id;
            stack.push(arr[i]);
        }
        stack.clear();
        // i번째 위치에서 오른쪽으로 볼 수 있는 빌딩
        for(int i=n; i>0; i--) {
            while(!stack.isEmpty() && stack.peek().height <= arr[i].height) stack.pop();
            canSee[i] += stack.size();
            if(!stack.isEmpty()) {
                int candidateLoc = stack.peek().id;
                if(nearId[i] == 0) nearId[i] = candidateLoc;
                else {
                    int dist1 = Math.abs(nearId[i] - arr[i].id);
                    int dist2 = Math.abs(candidateLoc - arr[i].id);

                    if (dist2 < dist1) nearId[i] = candidateLoc;
                    else if(dist1 == dist2) nearId[i] = Math.min(candidateLoc, nearId[i]);
                }
            }
            stack.push(arr[i]);
        }
        //--------------------------------------------------------------------------------------
        for(int i=1; i<n+1; i++) {
            if(canSee[i] == 0) sb.append(0);
            else {
                sb.append(canSee[i]).append(' ').append(nearId[i]);
            }
            sb.append('\n');
        }
    }
}
