package jan.week5.boj;

import java.util.*;
import java.io.*;

public class 청소년상어_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    /**
     * 4 x 4 격자
     * x 는 행, y 는 열
     *
     * 각 칸에는 물고기(번호, 방향)가 있다.
     * - 번호 ( 1 ~ 16 ) -> 번호는 중복될 수 없음
     * - 방향 ( 8 방향 )
     *
     * 상어는 (0, 0) 에 있는 물고기를 먹고, (0, 0) 에 들어가게 된다.
     * 상어의 방향은 물고기의 방향과 같다.
     *
     * 이후 물고기가 이동하낟.
     * 번호가 작은 물고기부터 순서대로 이동한다.
     * 한 칸씩 이동이 가능하고, 이동 가능한 칸은 빈 칸과, 다른 물고기가 있는 칸이다.
     * 격자 밖 또는 상어가 있는 칸으로는 움직일 수 없다.
     *
     * 각 물고기는 방향이 이동할 수 있는 칸을 향할 때까지 45도씩 반시계 회전한다.
     * 만약 이동할 수 있는 칸이 없다면 이동하지 않는다.
     * 다른 물고기가 있는 칸으로 이동할 때는 서로의 위치를 바꾼다.
     *
     * 물고기 이동이 모두 끝나면 상어가 이동한다.
     * 상어는 방향에 있는 칸으로 이동할 수 있는데, 한 번에 여러 개의 칸을 이동할 수 있다.
     * 상어가 물고기가 있는 칸으로 이동했다면 물고기를 먹고, 그 물고기의 방향을 가지게 된다.
     * 이동하는 중 지나가는 칸에 있는 물고기는 먹지 않는다.
     * 물고기가 없는 칸으로는 이동할 수 없다.
     * 이동할 수 있는 칸이 없으면 공간에서 벗어나 집으로 간다.
     */
    // ↑, ↖, ←, ↙, ↓, ↘, →, ↗ (1-based)
    static final int[] dx = {0,-1,-1,0,1,1,1,0,-1};
    static final int[] dy = {0,0,-1,-1,-1,0,1,1,1};
    static int[][] map;
    static TreeSet<Fish> fishes;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        // 물고기는 번호가 작은 순으로 움직인다.
        fishes = new TreeSet<>((a, b) -> Integer.compare(a.no, b.no));
        map = new int[4][4];

        for(int x=0; x<4; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<4; y++) {
                int no = Integer.parseInt(st.nextToken());
                int dir = Integer.parseInt(st.nextToken());
                Fish fish = new Fish(no, dir);
                fishes.add(fish);
                map[x][y] = no;
            }
        }
    }

    static class Fish {
        int no;
        int dir;

        Fish(int no, int dir) {
            this.no = no;
            this.dir = dir;
        }

        void turn() {
            dir = (dir%9) + (dir/9);
        }
    }
}
