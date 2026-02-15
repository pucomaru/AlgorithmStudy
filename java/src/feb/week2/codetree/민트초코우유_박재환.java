package feb.week2.codetree;

import java.util.*;
import java.io.*;

public class 민트초코우유_박재환 {
    /**
     * N x N
     * 각 칸에는 학생들이 있다.
     *
     * 초기 각 학생은 [민트, 초코, 우유] 중 하나만 신봉한다.
     * - T : 민트
     * - C : 초코
     * - M : 우유
     *
     * 다른 사람의 영향을 받으면
     * - 초코우유
     * - 민트우유
     * - 민트초코
     * - 민트초코우유
     *
     * 초기 신앙심이 주어진다.
     *
     * [아침]
     * 모든 학생이 신앙심을 1씩 얻는다.
     *
     * [점심]
     * 신봉음식이 완전하게 같은 경우 그룹을 형성한다. (상하좌우)
     * 대표자를 선정한다.
     * - 신앙심이 가장큰
     * - x, y가 가장 작은
     * 대표자를 제외한 그룹원의 신앙심을 1씩 감소 -> 대표자한테 줌
     *
     * [저녁]
     * 대표가 신앙을 전파한다.
     * - 단일 : 민트, 초코, 우유
     * - 이중 : 초코우유, 민트우유, 민트초코
     * - 삼중 : 민트초코우유
     *
     * 전파 순서
     * - 그룹 대표자 신앙심 높은 순
     * - x, y 작은 순
     *
     * 대표자는 본인 신앙심 1만 남기고 나머지를 간절함으로 사용
     * 전파 방향은 (간절함 % 4) [위, 아래, 왼쪽, 오른쪽]
     *
     * 격자 밖으로 나가거나, 간절함이 0 되면 종료
     * 신봉음식이 완전하게 같다면 패스
     * 간절 > 신앙심 -> 강한 전파 성공 -> 전파자 간절함 - (신앙심+1), 신앙심+1
     * x <= y -> 약한 전파 성공 -> 전파자 간절함 0, 신앙심 + 간절함
     *
     * 전파 당하면 그 턴에는 더 이상 전파하지 않음
     * -> 당할수는 있음
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
    static int[] dx = {-1,1,0,0};
    static int[] dy = {0,0,-1,1};

    static int n;
    static int[][] foods;
    static int[][] trusts;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        int t = Integer.parseInt(st.nextToken());

        foods = new int[n][n];
        trusts = new int[n][n];
        /**
         * [민트(T), 초코(C), 우유(M)]
         * -> 비트 마스킹 이용
         * T : 001
         * C : 010
         * M : 100
         */
        for(int x=0; x<n; x++) {
            String line = br.readLine().trim();
            for(int y=0; y<n; y++) {
                char c = line.charAt(y);
                switch (c) {
                    case 'T':
                        foods[x][y] = 1;
                        break;
                    case 'C':
                        foods[x][y] = 1<<1;
                        break;
                    case 'M':
                        foods[x][y] = 2<<1;
                        break;
                }
            }
        }
        for(int x=0; x<n; x++) {
            st = new StringTokenizer(br.readLine().trim());
            for(int y=0; y<n; y++) trusts[x][y] = Integer.parseInt(st.nextToken());
        }
        solution(t);
    }

    static void solution(int day) {
        while(day-- > 0) {      // 하루 단위 시뮬레이션
            morningAndLunch();
            dinner();
            print();
        }
    }

    /**
     * ⚠️ 우선순위 큐를 사용하면 틀림!
     * offer, poll 시점에 재정렬(log n)이 이루어지기 때문에,
     * 전파하는 과정에서 계속해서 우선순위가 바뀜
     */
    static List<Ref> refList;
    static void morningAndLunch() {
        /**
         * 아침 + 점심
         * => 그룹의 대표자에게 신앙심을 1씩 전달
         * => 아침에 +1 로직 제거
         * => 대표자 신앙심 + 그룹 크기
         */
        refList = new ArrayList<>();
        boolean[][] checked = new boolean[n][n];
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                if(checked[x][y]) continue;     // 이미 체크된 인원
                Ref ref = findMembers(x, y, checked);
                refList.add(ref);
            }
        }
        Collections.sort(refList);
    }
    static class Ref implements Comparable<Ref> {
        /**
         * 대표자
         * - x, y
         * - 신앙심
         */
        int x, y;
        int t;

        Ref(int x, int y, int t) {
            this.x = x;
            this.y = y;
            this.t = t;
        }

        public int compareTo(Ref o) {
            int cntA = Integer.bitCount(foods[this.x][this.y]);
            int cntB = Integer.bitCount(foods[o.x][o.y]);
            if(cntA != cntB) return Integer.compare(cntA, cntB);
            if(this.t != o.t) return Integer.compare(o.t, this.t);
            if(this.x != o.x) return Integer.compare(this.x, o.x);
            return Integer.compare(this.y, o.y);
        }
    }
    static Ref findMembers(int x, int y, boolean[][] checked) {
        /**
         * 현재 그룹에 포함될 인원을 찾는다.
         */
        Queue<int[]> q = new ArrayDeque<>();
        int food = foods[x][y];
        int refX = x, refY = y, refT = trusts[x][y];
        int groupSize = 0;

        q.offer(new int[] {x, y});
        checked[x][y] = true;
        groupSize++;

        while(!q.isEmpty()) {
            int[] cur = q.poll();
            int curX = cur[0];
            int curY = cur[1];

            for(int dir=0; dir<4; dir++) {
                int nx = curX + dx[dir];
                int ny = curY + dy[dir];
                /**
                 * 격자 벗어나는 경우
                 * 신봉 음식이 다른 경우
                 * 이미 어느 그룹에 속한 경우
                 */
                if(isNotBoard(nx, ny)) continue;
                if(food != foods[nx][ny]) continue;
                if(checked[nx][ny]) continue;

                checked[nx][ny] = true;
                groupSize++;
                q.offer(new int[] {nx, ny});
                /**
                 * 대표자 확인
                 * - 신앙심 크기
                 * - x 크기
                 * - y 크기
                 */
                if(refT < trusts[nx][ny]) {
                    refX = nx;
                    refY = ny;
                    refT = trusts[nx][ny];
                } else if(refT == trusts[nx][ny]) {
                    if(refX > nx || (refX == nx && refY > ny)) {
                        refX = nx;
                        refY = ny;
                    }
                }
            }
        }

        /**
         * 대표자에게 1씩 신앙심 전달
         */
        trusts[refX][refY] += groupSize;
        return new Ref(refX, refY, trusts[refX][refY]);
    }

    static void dinner() {
        /**
         * refList(대표자) 순으로 전파를 이어간다.
         */
        boolean[][] defense = new boolean[n][n];
        for(Ref ref : refList) {
            if(defense[ref.x][ref.y]) continue;     // 방어상태라면 전파하지 않는다.
            propagation(ref, defense);
            trusts[ref.x][ref.y] = 1;               // 신앙심은 1만 남게된다.
        }
    }
    static void propagation(Ref ref, boolean[][] defense) {
        int x = ref.x;
        int y = ref.y;
        int beg = ref.t-1;      // 간절함
        int dir = ref.t%4;      // 전파 방향 (신앙심%4)

        while(true) {
            x += dx[dir];
            y += dy[dir];
            if(isNotBoard(x, y)) break;                             // 격자를 벗어남
            if(foods[x][y] == foods[ref.x][ref.y]) continue;        // 전파할 필요 없음
            /**
             * [전파 가능]
             */
            if(trusts[x][y] < beg) {                                // 강한 전파
                /**
                 * - 사상 동기회
                 * - 대표자의 간절함이 y + 1 만큼 감소
                 * - 대상은 신앙심 1 증가
                 */
                foods[x][y] = foods[ref.x][ref.y];
                beg -= (trusts[x][y]+1);
                trusts[x][y]++;
            } else if(trusts[x][y] >= beg) {                        // 약한 전파
                /**
                 * - 기존 음식 + 전파 음식
                 * - 대표자 간절함 0
                 * - 대상 신앙심 beg 만큼 증가
                 */
                foods[x][y] |= foods[ref.x][ref.y];
                trusts[x][y]+=beg;
                beg = 0;
            }
            defense[x][y] = true;
            if(beg == 0) break;
        }
    }
    static final int[] printSeq = {(1|1<<1|2<<1), (1|1<<1), (1|2<<1), (2<<1|1<<1), (2<<1), (1<<1), (1)};
    static void print() {
        Map<Integer, Integer> map = new HashMap<>();
        for(int i : printSeq) map.put(i, 0);
        for(int x=0; x<n; x++) {
            for(int y=0; y<n; y++) {
                int i = map.get(foods[x][y]);
                map.put(foods[x][y], trusts[x][y] + i);
            }
        }
        for(int i : printSeq) sb.append(map.get(i)).append(' ');
        sb.append('\n');
    }
    //********************** 공통 *************************
    static boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}
