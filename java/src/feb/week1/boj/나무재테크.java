package feb.week1.boj;

import java.util.*;
import java.io.*;

public class 나무재테크 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * N x N
     * 초기 모든 땅의 양분은 5만큼 있다.
     *
     * M 개의 나무를 구매해 땅에 심는다.
     * - 같은 칸에 여러 개의 나무가 심어져 있을 수 있다.
     *
     * [봄]
     * 자신의 나이만큼 양분을 먹고, 나이가 1 증가
     * - 한 칸에 여러 나무가 있다면, 어린 나무부터 양분을 먹는다. -> 양분을 먹지 못하면 즉사한다.
     *
     * [여름]
     * 봄에 죽은 나무가 양분으로 변한다.
     * - 죽은 나무의 나이 / 2 가 양분으로 추가된다. (소수점 X)
     *
     * [가을]
     * 나무가 번식한다. (나이가 5배수 인 나무만)
     * - 인접한 8개의 칸에 나이가 1인 나무가 생긴다. -> 격자를 벗어나면 나무가 생기지 않는다.
     *
     * [겨울]
     * 각 칸에 A[r][c] 만큼 양분이 추가된다.
     *
     * => K년이 지난 이후 상도의 땅에 살아있는 나무의 개수를 구해라
     *
     */
    static StringTokenizer st;
    static int treeId;
    static int n, m, k;
    static int[][] map;
    static int[][] a;
    static Deque<Integer>[][] trees;
    static void init() throws IOException {
        treeId = 0;
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());       // 격자의 크기
        m = Integer.parseInt(st.nextToken());       // 초기 나무의 수
        k = Integer.parseInt(st.nextToken());       // 시뮬레이션 횟수

        map = new int[n][n];
        trees = new Deque[n][n];
        List<Integer>[][] temp = new List[n][n];    // 정렬을 위해 임시로 사용하는 변수
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                map[x][y] = 5;                      // 초기 양분 5
                trees[x][y] = new ArrayDeque<>();   // Deque 생성
                temp[x][y] = new ArrayList<>();
            }
        }

        a = new int[n][n];
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) a[x][y] = Integer.parseInt(st.nextToken());
        }

        for(int i=0; i<m; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int x = Integer.parseInt(st.nextToken())-1;
            int y = Integer.parseInt(st.nextToken())-1;
            int age = Integer.parseInt(st.nextToken());
            temp[x][y].add(age);
        }

        for (int x=0; x<n; x++) {
            for (int y=0; y<n; y++) {
                if (temp[x][y].isEmpty()) continue;

                temp[x][y].sort(Integer::compare);
                for (int age : temp[x][y]) {
                    trees[x][y].addLast(age);
                }
            }
        }

        System.out.println(solution());
    }
    /**
     * k 년 이후 남아있는 나무의 개수
     */
    static int solution() {
        while(k-- > 0) {        // 한 해씩 처리
            spring();
            summer();
            autumn();
            winter();
        }
        int treeCount = 0;
        for(int x=0; x<n; x++) {
            for (int y = 0; y < n; y++) {
                treeCount += trees[x][y].size();
            }
        }
        return treeCount;
    }

    static List<Integer>[][] dead;
    static void spring() {
        dead = new List[n][n];
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(trees[x][y].isEmpty()) continue;
                Deque<Integer> cur = trees[x][y];           // 현재 처리할 나무
                Deque<Integer> next = new ArrayDeque<>();   // 양분 흡수 가능한 나무
                dead[x][y] = new ArrayList<>();             // 죽는 나무
                // 나무가 있다면
                while(!cur.isEmpty()) {
                    int age = cur.pollFirst();      // 가장 어린 나무
                    if(map[x][y] >= age) {
                        map[x][y] -= age;
                        next.offerLast(age+1);
                    } else {
                        dead[x][y].add(age);
                    }
                }
                trees[x][y] = next;
            }
        }
    }

    static void summer() {
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                if(dead[x][y] == null) continue;
                for (int age : dead[x][y]) {
                    map[x][y] += age / 2;
                }
            }
        }
    }
    static int[] dx = {0,1,0,-1,1,1,-1,-1};
    static int[] dy = {1,0,-1,0,1,-1,1,-1};
    static void autumn() {
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(trees[x][y].isEmpty()) continue;

                // 나무가 있다면
                for(int age : trees[x][y]) {
                    if(age % 5 != 0) continue;

                    for(int dir=0; dir<8; dir++) {
                        int nx = x + dx[dir];
                        int ny = y + dy[dir];
                        if (nx < 0 || ny < 0 || nx >= n || ny >= n) continue;
                        trees[nx][ny].offerFirst(1);
                    }
                }
            }
        }
    }

    static void winter() {
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                map[x][y] += a[x][y];
            }
        }
    }
}
