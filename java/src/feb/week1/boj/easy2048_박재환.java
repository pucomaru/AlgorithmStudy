package feb.week1.boj;

import java.util.*;
import java.io.*;

public class easy2048_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int n;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        Block[][] map = new Block[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) {
                int num = Integer.parseInt(st.nextToken());
                if(num == 0) map[x][y] = null;
                else map[x][y] = new Block(num);
            }
        }
        int result = solution(map);
        System.out.println(result);
    }
    static class Block {
        int num;
        boolean increase;

        Block(int num) {
            this.num = num;
        }
    }
    /**
     * 이동방향은 총 4가지 방향
     * 상, 하, 좌, 우
     * 5번 이동했을 때 최대
     * 4 ** 5
     */
    static int topValue;
    static int solution(Block[][] map) {
        topValue = 2;
        moveBlock(0, map);
        return topValue;
    }
    static int getTopValue(Block[][] map) {
        int top = 0;
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(map[x][y] == null) continue;
                top = Math.max(map[x][y].num, top);
            }
        }
        return top;
    }
    static void moveBlock(int seq, Block[][] map) {
        if(seq == 5) {
            int top = getTopValue(map);
            topValue = Math.max(top, topValue);
            return;
        }
        /**
         * 계속해서 map을 Copy 해서 새로운 map으로 생성
         */
        for(int dir=0; dir<4; dir++) {
            Block[][] copy = copyMap(map);
            switch(dir) {
                case 0:
                    left(copy);
                    break;
                case 1:
                    right(copy);
                    break;
                case 2:
                    top(copy);
                    break;
                case 3:
                    bottom(copy);
                    break;
            }
            moveBlock(seq+1, copy);
        }

    }
    static void left(Block[][] map) {
        Deque<Block> q = new ArrayDeque<>();
        for(int x=0; x<n; x++) {
            q.clear();
            for(int y=0; y<n; y++) {
                if(map[x][y] == null) continue;
                // 빈칸이 아닐 때
                if(!q.isEmpty() && !q.getLast().increase && q.getLast().num == map[x][y].num) {
                    q.getLast().increase = true;
                } else {
                    q.offer(map[x][y]);
                }
            }
            for(int y=0; y<n; y++) {
                if(!q.isEmpty()) {
                    Block block = q.poll();
                    if(block.increase) block.num *= 2;
                    map[x][y] = block;
                } else {
                    map[x][y] = null;
                }
            }
        }
    }
    static void right(Block[][] map) {
        Deque<Block> q = new ArrayDeque<>();
        for(int x=0; x<n; x++) {
            q.clear();
            for(int y=n-1; y>-1; y--) {
                if(map[x][y] == null) continue;
                // 빈칸이 아닐 때
                if(!q.isEmpty() && !q.getLast().increase && q.getLast().num == map[x][y].num) {
                    q.getLast().increase = true;
                } else {
                    q.offer(map[x][y]);
                }
            }
            for(int y=n-1; y>-1; y--) {
                if(!q.isEmpty()) {
                    Block block = q.poll();
                    if(block.increase) block.num *= 2;
                    map[x][y] = block;
                } else {
                    map[x][y] = null;
                }
            }
        }
    }
    static void top(Block[][] map) {
        Deque<Block> q = new ArrayDeque<>();
        for(int y=0; y<n; y++) {
            q.clear();
            for(int x=0; x<n; x++) {
                if(map[x][y] == null) continue;
                // 빈칸이 아닐 때
                if(!q.isEmpty() && !q.getLast().increase && q.getLast().num == map[x][y].num) {
                    q.getLast().increase = true;
                } else {
                    q.offer(map[x][y]);
                }
            }
            for(int x=0; x<n; x++) {
                if(!q.isEmpty()) {
                    Block block = q.poll();
                    if(block.increase) block.num *= 2;
                    map[x][y] = block;
                } else {
                    map[x][y] = null;
                }
            }
        }
    }
    static void bottom(Block[][] map) {
        Deque<Block> q = new ArrayDeque<>();
        for(int y=0; y<n; y++) {
            q.clear();
            for(int x=n-1; x>-1; x--) {
                if(map[x][y] == null) continue;
                // 빈칸이 아닐 때
                if(!q.isEmpty() && !q.getLast().increase && q.getLast().num == map[x][y].num) {
                    q.getLast().increase = true;
                } else {
                    q.offer(map[x][y]);
                }
            }
            for(int x=n-1; x>-1; x--) {
                if(!q.isEmpty()) {
                    Block block = q.poll();
                    if(block.increase) block.num *= 2;
                    map[x][y] = block;
                } else {
                    map[x][y] = null;
                }
            }
        }
    }
    static Block[][] copyMap(Block[][] map) {
        Block[][] copy = new Block[n][n];
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(map[x][y] == null) copy[x][y] = null;
                else copy[x][y] = new Block(map[x][y].num);
            }
        }
        return copy;
    }
}
