package feb.week1.swea;

import java.util.*;
import java.io.*;

public class 미생물격리_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for(int i=1; i<tc+1; i++) {
            sb.append('#').append(i).append(' ');
            init();
            sb.append('\n');
        }
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static int n, m, k;
    static Group[] groups;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 셀의 개수
        m = Integer.parseInt(st.nextToken());       // 격리 시간
        k = Integer.parseInt(st.nextToken());       // 미생물 군집 수
        groups = new Group[k];
        for(int i=0; i<k; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken());
            int y = Integer.parseInt(st.nextToken());
            int size = Integer.parseInt(st.nextToken());
            int dir = mapDir(Integer.parseInt(st.nextToken()));
            Group group = new Group(x, y, size, dir);
            groups[i] = group;
        }

        int result = solution();
        sb.append(result);
    }
    static int[] dx = {0,1,0,-1};
    static int[] dy = {1,0,-1,0};
    static class Group {
         int x, y;
         int size;
         int dir;
         boolean isLive;

         Group(int x, int y, int size, int dir) {
             this.x = x;
             this.y = y;
             this.size = size;
             this.dir = dir;
             this.isLive = true;
         }

         void turn() {
             dir = (dir + 2) % 4;
         }

         void move() {
             x += dx[dir];
             y += dy[dir];
         }
    }
    static final int KEY = 105;
    static int solution() {
        while(m-- > 0) {
            Map<Integer, List<Group>> afterMove = new HashMap<>();
            /**
             * [군집 이동]
             */
            for(int i=0; i<k; i++) {
                Group group = groups[i];
                if(!group.isLive) continue;
                group.move();
                int hash = group.x * KEY + group.y;
                afterMove.computeIfAbsent(hash, k -> new ArrayList<>()).add(group);
            }
            /**
             * [약품이 칠해져있는 구역 확인]
             */
            for(Group group : groups) {
                if(!group.isLive) continue;
                if(isBoundary(group.x, group.y)) {
                    group.size /= 2;
                    group.turn();
                }
            }
            /**
             * [합쳐지는 군집 처리]
             */
            for(Map.Entry<Integer, List<Group>> entry : afterMove.entrySet()) {
                List<Group> list = entry.getValue();
                if(list.size() < 2) continue;       // 충돌하지 않은 군집
                // 충돌한 군집
                list.sort((a, b) -> Integer.compare(b.size, a.size));
                int sum = 0;
                for(int i=1; i<list.size(); i++) {
                    Group dead = list.get(i);
                    dead.isLive = false;
                    sum += dead.size;
                }
                Group servive = list.get(0);
                servive.size += sum;
            }
        }

        int sum = 0;
        for(Group group : groups) {
            if(!group.isLive) continue;
            sum += group.size;
        }
        return sum;
    }
    //--------------------
    /**
     * 약품이 발라진 면에 도착했는지
     */
    static boolean isBoundary(int x, int y) {
        return x == 0 || y == 0 || x == n-1 || y == n-1;
    }
    static int mapDir(int dir) {
        if(dir == 1) return 3;      // 상
        if(dir == 2) return 1;      // 하
        if(dir == 3) return 2;      // 좌
        return 0;                   // 우
    }
}
