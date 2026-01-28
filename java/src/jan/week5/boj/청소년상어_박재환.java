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
    static int maxFee;
    static void init(BufferedReader br) throws IOException {
        StringTokenizer st;
        Fish[] fishes = new Fish[17];
        int[][] map = new int[4][4];

        for(int x=0; x<4; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<4; y++) {
                int id = Integer.parseInt(st.nextToken());
                int dir = Integer.parseInt(st.nextToken());
                Fish fish = new Fish(x, y, dir);
                map[x][y] = id;
                fishes[id] = fish;
            }
        }

        // 상어의 초기 위치 설정
        int initFee = map[0][0];
        Fish fish = fishes[initFee];
        Shark shark = new Shark(fish.x, fish.y, fish.dir, 0);
        shark.sum += initFee;
        maxFee = initFee;
        map[0][0] = -1;      // 상어 : -1, 빈칸 : 0
        fish.die();

        moveShark(shark, fishes, map);
        System.out.println(maxFee);
    }

    static void moveShark(Shark shark, Fish[] fishes, int[][] map) {
        maxFee = Math.max(maxFee, shark.sum);
        // 물고기 이동
        moveFish(fishes, map);
        // 상어 이동
        for(int i=1; i<4; i++) {
            int nx = shark.x + dx[shark.dir] * i;
            int ny = shark.y + dy[shark.dir] * i;

            if(nx < 0 || ny < 0 || nx >= 4 || ny >= 4) break;
            if(map[nx][ny] < 1) continue;   // 빈칸인경우 넘어간다.

            // 상어가 이동이 가능하다.
            // 배열 복사
            int[][] copyMap = copyMap(map);
            Fish[] copyFishes = copyFishes(fishes);
            Shark copyShark = new Shark(nx, ny, shark.dir, shark.sum);

            // 상어 이동
            int fishId = copyMap[nx][ny];
            Fish fee = copyFishes[fishId];
            copyMap[shark.x][shark.y] = 0;
            copyMap[nx][ny] = -1;
            fee.live = false;
            copyShark.sum += fishId;
            copyShark.dir = fee.dir;
            moveShark(copyShark, copyFishes, copyMap);
        }
    }

    static Fish[] copyFishes(Fish[] fishes) {
        Fish[] copyFishes = new Fish[17];
        for(int i=1; i<17; i++) {
            Fish originFish = fishes[i];
            Fish tempFish = new Fish(originFish.x, originFish.y, originFish.dir, originFish.live);
            copyFishes[i] = tempFish;
        }
        return copyFishes;
    }

    static int[][] copyMap(int[][] map) {
        int[][] copyMap = new int[4][4];
        for(int x=0; x<4; x++) {
            for(int y=0; y<4; y++) {
                copyMap[x][y] = map[x][y];
            }
        }
        return copyMap;
    }

    static void moveFish(Fish[] fishes, int[][] map) {
        for(int i=1; i<17; i++) {
            if(!fishes[i].live) continue;
            findMoveSpace(i, fishes, map);
        }
    }

    static void findMoveSpace(int id, Fish[] fishes, int[][] map) {
        Fish fish = fishes[id];

        for(int i=0; i<8; i++) {
            int nDir = (fish.dir + i - 1) % 8 + 1;
            int nx = fish.x + dx[nDir];
            int ny = fish.y + dy[nDir];

            if(nx < 0 || ny < 0 || nx >= 4 || ny >= 4) continue;
            if(map[nx][ny] == -1) continue;

            // 이동할 수 있는 위치라면
            fish.dir = nDir;
            int targetId = map[nx][ny];
            map[fish.x][fish.y] = 0;
            if (targetId != 0) {    // 다른 물고기가 있는 칸으로 이동
                Fish target = fishes[targetId];
                target.x = fish.x;
                target.y = fish.y;
                map[target.x][target.y] = targetId;
            }
            // 현재 탐색 순서인 물고기 이동
            fish.x = nx;
            fish.y = ny;
            map[nx][ny] = id;
            break;

        }
    }

    static class Fish {
        int x, y;
        int dir;
        boolean live;

        Fish(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.live = true;
        }

        Fish(int x, int y, int dir, boolean live) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.live = live;
        }

        void die() { live = false; }
    }

    static class Shark {
        int x, y;
        int dir;
        int sum;
        Shark(int x, int y, int dir, int sum) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.sum = sum;
        }
    }
}
