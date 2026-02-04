package feb.week1.boj;

import java.util.*;
import java.io.*;

public class 스타트택시_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    /**
     * 손님을 도착지로 데려다줄 때마다 연료가 충전된다. -> 승객을 태워 이동하면서 소모한 연료양의 두 배 만큼
     * 연료가 바닥나면 업무가 끝난다.
     *
     * M명의 승객을 태우는 것이 목표
     * N x N 격자
     * - 비어있음
     * - 벽이 있음
     *
     * 택시는 상하좌우 인접한 빈 칸 하나로 이동 가능
     * -> 늘 최단경로로만 움직임
     *
     * => 목적지 도착과, 연료 바닥이 동시에 나면, 이는 실패로 간주하지 않음
     */
    static StringTokenizer st;
    static int n, m;
    static int[][] map;
    static Taxi taxi;
    static Customer[] customers;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 격자 크기
        m = Integer.parseInt(st.nextToken());       // 목표 손님 수
        int fuel = Integer.parseInt(st.nextToken());    // 초기 연료
        map = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) {
                map[x][y] = Integer.parseInt(st.nextToken());
                if(map[x][y] == 1) map[x][y] = -1;     // 벽을 -1 로 표시
            }
        }

        st = new StringTokenizer(br.readLine().trim());
        int tx = Integer.parseInt(st.nextToken())-1;
        int ty = Integer.parseInt(st.nextToken())-1;
        taxi = new Taxi(tx, ty, fuel);

        customers = new Customer[m];
        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int sx = Integer.parseInt(st.nextToken())-1;
            int sy = Integer.parseInt(st.nextToken())-1;
            int ex = Integer.parseInt(st.nextToken())-1;
            int ey = Integer.parseInt(st.nextToken())-1;
            map[sx][sy] = i+1;      // 손님 id 값 저장
            customers[i] = new Customer(sx, sy, ex, ey);
        }

        int result = solution();
        System.out.println(result);
    }
    static class Customer {
        int sx, sy;
        int ex, ey;
        int distFromTaxi;
        boolean done;

        Customer(int sx, int sy, int ex, int ey) {
            this.sx = sx;
            this.sy = sy;
            this.ex = ex;
            this.ey = ey;
            this.done = false;
        }
    }
    static class Taxi {
        int x, y;
        int fuel;
        int customerCount;

        Taxi(int x, int y, int fuel) {
            this.x = x;
            this.y = y;
            this.fuel = fuel;
            this.customerCount = 0;
        }
    }
    //--------------------------------------
    static int solution() {
        /**
         * 매번 손님을 태울 수 있는지 확인한다.
         */
        while(true) {
            int customerId = findNearCustomer();
            if(customerId == -1) break;
            // 손님을 찾은경우
            // 태울 수 있는지 확인
            Customer customer = customers[customerId];
            if(taxi.fuel < customer.distFromTaxi) break;       // 손님을 태우러 이동할 수 없음
            taxi.fuel -= customer.distFromTaxi;

            map[customer.sx][customer.sy] = 0;                  // 손님을 태웠다는 처리
            customer.done = true;
            taxi.x = customer.sx;
            taxi.y = customer.sy;
            // 목적지까지 이동할 수 있는지 확인
            int distToEnd = getDistToEnd(customer);
            if(distToEnd == -1) break;      // 목적지까지 이동할 수 있는 경로가 없음
            if(taxi.fuel < distToEnd) break;        // 연료가 부족함

            taxi.fuel -= distToEnd;
            taxi.fuel += distToEnd * 2;
            taxi.customerCount++;
            taxi.x = customer.ex;
            taxi.y = customer.ey;
        }

        return taxi.customerCount == m ? taxi.fuel : -1;
    }
    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static int findNearCustomer() {
        /**
         * 같은 거리에 여러 승객이 있을 경우, 행 번호가 가장 작은 승객을
         * 행 번호가 같다면 열 번호가 가장 작은 승객을 고른다.
         */
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];
        int[] foundCustomer = new int[3];
        foundCustomer[0] = n+1;
        foundCustomer[1] = n+1;
        foundCustomer[2] = n*n+1;
        // 만약 택시 위치에 손님이 있다면
        if(map[taxi.x][taxi.y] > 0) {
            int customerId = map[taxi.x][taxi.y]-1;
            customers[customerId].distFromTaxi = 0;
            return customerId;
        }
        // 택시위치 초기화
        q.offer(new int[] {taxi.x, taxi.y, 0});
        visited[taxi.x][taxi.y] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int dist = cur[2];
            if(dist > foundCustomer[2]) break;
            for(int dir=0; dir<4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(visited[nx][ny] || map[nx][ny] == -1) continue;
                // 현재 위치에 손님이 있는지 확인
                if(map[nx][ny] > 0) {
                    if(foundCustomer[0] == n+1 && foundCustomer[1] == n+1 && foundCustomer[2] == n*n+1) {
                        foundCustomer[0] = nx;
                        foundCustomer[1] = ny;
                        foundCustomer[2] = dist+1;
                    } else {
                        if(foundCustomer[2] > dist+1) {
                            foundCustomer[0] = nx;
                            foundCustomer[1] = ny;
                            foundCustomer[2] = dist+1;
                        } else if(foundCustomer[2] == dist+1 && foundCustomer[0] > nx) {
                            foundCustomer[0] = nx;
                            foundCustomer[1] = ny;
                        } else if(foundCustomer[2] == dist+1 && foundCustomer[0] == nx && foundCustomer[1] > ny) {
                            foundCustomer[1] = ny;
                        }
                    }
                }
                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny, dist+1});
            }
        }

        if(foundCustomer[0] == n+1 && foundCustomer[1] == n+1 && foundCustomer[2] == n*n+1) return -1;
        int customerId = map[foundCustomer[0]][foundCustomer[1]]-1;
        customers[customerId].distFromTaxi = foundCustomer[2];
        return customerId;
    }
    static int getDistToEnd(Customer customer) {
        Queue<int[]> q = new ArrayDeque<>();
        boolean[][] visited = new boolean[n][n];

        int sx = customer.sx, sy = customer.sy;
        int ex = customer.ex, ey = customer.ey;
        q.offer(new int[] {sx, sy, 0});
        visited[sx][sy] = true;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int x = cur[0];
            int y = cur[1];
            int dist = cur[2];

            if(x == ex && y == ey) return dist;

            for(int dir=0; dir<4; dir++) {
                int nx = x + dx[dir];
                int ny = y + dy[dir];
                if(nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                if(visited[nx][ny] || map[nx][ny] == -1) continue;
                visited[nx][ny] = true;
                q.offer(new int[] {nx, ny, dist+1});
            }
        }

        return -1;
    }
}
/*
6 3 15
0 0 1 0 0 0
0 0 1 0 0 0
0 0 0 0 0 0
0 0 0 0 0 0
0 0 0 0 1 0
0 0 0 1 0 0
6 5
2 2 5 6
5 4 1 6
4 2 6 5

14
 */
