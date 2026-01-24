package jan.week4.swea.애벌레키우기;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class 애벌레키우기_박재환 {
    private static final int CMD_INIT           = 100;
    private static final int CMD_JOIN           = 200;
    private static final int CMD_TOP5           = 300;

    private static UserSolution usersolution = new UserSolution();

    public static class RESULT
    {
        int cnt;
        int[] IDs = new int[5];

        RESULT()
        {
            cnt = -1;
        }
    }

    private static boolean run(BufferedReader br) throws Exception
    {
        int Q;
        int N, mTime, mID, mX, mY, mLength;
        int cnt, ans;


        StringTokenizer st = new StringTokenizer(br.readLine(), " ");
        Q = Integer.parseInt(st.nextToken());

        boolean okay = false;

        for (int q = 0; q < Q; ++q)
        {
            st = new StringTokenizer(br.readLine(), " ");
            int cmd = Integer.parseInt(st.nextToken());

            switch(cmd)
            {
                case CMD_INIT:
                    N =  Integer.parseInt(st.nextToken());
                    usersolution.init(N);
                    okay = true;
                    break;
                case CMD_JOIN:
                    mTime =  Integer.parseInt(st.nextToken());
                    mID =  Integer.parseInt(st.nextToken());
                    mX =  Integer.parseInt(st.nextToken());
                    mY =  Integer.parseInt(st.nextToken());
                    mLength =  Integer.parseInt(st.nextToken());
                    usersolution.join(mTime, mID, mX, mY, mLength);
                    break;
                case CMD_TOP5:
                    mTime =  Integer.parseInt(st.nextToken());
                    RESULT ret = usersolution.top5(mTime);
                    System.out.printf("{%d, %s}\n", ret.cnt, Arrays.toString(ret.IDs));
                    cnt = Integer.parseInt(st.nextToken());
                    if (cnt != ret.cnt)
                        okay = false;
                    for (int i = 0; i < cnt; ++i){
                        ans = Integer.parseInt(st.nextToken());
                        if (ans != ret.IDs[i])
                            okay = false;
                    }
                    break;
                default:
                    okay = false;
                    break;
            }
        }

        return okay;
    }

    public static void main(String[] args) throws Exception
    {
        System.setIn(new java.io.FileInputStream("C:\\Users\\doorm\\Desktop\\Algorithm\\java\\src\\jan\\week4\\swea\\res\\sample_input_애벌레키우기.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine(), " ");

        int TC = Integer.parseInt(st.nextToken());
        int MARK = Integer.parseInt(st.nextToken());

        for (int testcase = 1; testcase <= TC; ++testcase)
        {
            int score = run(br) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }

        br.close();
    }
}

class UserSolution
{
    /**
     * N x N 격자
     * 유저들은 고유 ID 를 갖고, mTime 에 (x, y) 위치에서 게임을 시작한다.
     * 초기 애벌레 길이 mLength 가 주어진다. -> 초기에 애벌레는 항상 상단을 바라본 채 추가된다.
     *
     * 모든 애벌레는 매초 머리가 바라보는 방향으로 한칸씩 전진한다.
     * -> 몸이 일직선으로 펴져 있는 경우, 바라보는 방향을 시계방향으로 90도 회전한다.
     *
     * 격자판을 벗어난 애벌레는 소멸한다.
     * A 애벌레가 B 애벌레에 충돌한 경우, A 애벌레는 소멸하고, B 의 성장 잠재력이 A 길이 만큼 증가한다.
     * 서로의 머리끼지 충돌한 경우 둘 다 소멸한다.
     *
     * 성장 잠재력이 1 이상인 경우, 매 초 이동 시 길이가 1씩 성장하고, 잠재력은 1씩 감소한다.
     *
     */
    class Worm {
        int id;
        int headX, headY;
        int conerX, conerY;
        int dir;
        int prevDir;
        int straightCount;
        int length;
        int potential;
        boolean isLive;

        Worm(int id, int headX, int headY, int length) {
            this.id = id;
            this.headX = headX;
            this.headY = headY;
            this.conerX = -1;
            this.conerY = -1;
            this.dir = 0;
            this.prevDir = -1;
            this.length = length;
            this.straightCount = length-1;
            this.potential = 0;
            this.isLive = true;
        }

        void grow() {       // 잠재력이 1 이상이라면, 크기 증가
            if(potential < 1) return;
            potential--;
            length++;

//            if (straightCount == length - 2) {
//                straightCount++;
//            }
        }

        boolean isStraight() {
            return straightCount == length-1;
        }

        void move() {
//            System.out.printf("[Move - Detail] id : %d, straightCount : %d, length %d\n", this.id, this.straightCount, this.length);
            /**
             * 몸이 일직선 상태인 경우
             * -> 시계방향으로 90도 회전 후 1칸 이동
             */
            if(isStraight()) {
//                System.out.printf("[Turn]\n");
                prevDir = dir;
                dir = (dir+1)%4;
                straightCount = 0;
            }

            straightCount++;
            headX += dx[dir];
            headY += dy[dir];
        }
    }
    final int KEY = 2001;
    // 상 우 하 좌
    final int[] dx = {-1,0,1,0};
    final int[] dy = {0,1,0,-1};
    int n, curTime;
    ArrayList<Worm> worms;
    public void init(int N)
    {
        n = N;
        curTime = 0;
        worms = new ArrayList<>();
    }

    public void join(int mTime, int mID, int mX, int mY, int mLength)
    {
        System.out.printf("[Join] id : %d\n", mID);
        jumpTime(mTime);
        Worm worm = new Worm(mID, mY, mX, mLength);
        worms.add(worm);
    }
    void jumpTime(int mTime) {
        while(curTime < mTime) {
            System.out.printf("curTime : %d -> %d\n", curTime, curTime+1);
            moveWorms();
            growAllWorms();
            curTime++;
        }
    }
    void moveWorms() {
        /**
         * 1. 잠재력이 있으면 길이 증가
         * 2. 모든 애벌레 이동
         * 3. 격자 밖인 애벌레 소멸
         * 4. 머리끼지 충돌한 애벌레 소멸
         * 5. 그 외 흡수되는 충돌 애벌레 처리
         */
//        // 1. 성장
//        growAllWorms();
        // 2. 이동
        moveAllWorms();
        // 3. 격자 밖 제거
        deadByOutOfBoard();
        // 4. 머리끼지 부딪히는 경우 제거
        deadByHeadCollisions();
        // 5
        mergeWorms();
    }

    void growAllWorms() {
        for(Worm worm : worms) {
            if(!worm.isLive) continue;
            worm.grow();
        }
    }

    void moveAllWorms() {
        for(Worm worm : worms) {
            if(!worm.isLive) continue;
            worm.move();
            System.out.printf("[Move] id : %d (%d, %d) / straightCount : %d / length : %d\n", worm.id, worm.headX, worm.headY, worm.straightCount, worm.length);
        }
    }
    Set<Worm> deadByOutOfBoardWorms;
    void deadByOutOfBoard() {
        deadByOutOfBoardWorms = new HashSet<>();
        for(Worm worm : worms) {
            if(!worm.isLive) continue;
            if(isNotBoard(worm.headX, worm.headY)) deadByOutOfBoardWorms.add(worm);
        }
    }
    Set<Worm> deadByHeadCollisionsWorms;
    void deadByHeadCollisions() {
        deadByHeadCollisionsWorms = new HashSet<>();
        Map<Integer, List<Worm>> collisions = new HashMap<>();

        for(Worm worm : worms) {
            if(!worm.isLive) continue;
            int hash = worm.headX * KEY + worm.headY;
            collisions.computeIfAbsent(hash, k -> new ArrayList<>()).add(worm);
        }

        for(List<Worm> list : collisions.values()) {
            if(list.size() < 2) continue;       // 머리끼리 부딪히지 않음
            for(Worm worm : list) deadByHeadCollisionsWorms.add(worm);
        }
    }

    void mergeWorms() {
        Map<Worm, Worm> collisions = new HashMap<>();
        Map<Worm, List<Worm>> hitTo = new HashMap<>();
        Map<Worm, Integer> indeg = new HashMap<>();
        Map<Worm, Integer> outdeg = new HashMap<>();
        Set<Worm> willDie = new HashSet<>();
        willDie.addAll(deadByOutOfBoardWorms);
        willDie.addAll(deadByHeadCollisionsWorms);

        for(Worm a : worms) {       // 머리
            if(!a.isLive) continue;
            if (willDie.contains(a)) continue;
            for(Worm b : worms) {   // 몸통
                if(!b.isLive) continue;
                if(a==b) continue;

                /**
                 * a 가 b 에 부딪히면, a 의 길이 만큼 b 의 잠재력이 증가한다.
                 */
                if(isCollision(a, b)) {
                    System.out.printf("[Collision] %d -> %d\n", a.id, b.id);
                    hitTo.computeIfAbsent(a, k -> new ArrayList<>()).add(b);
                    outdeg.put(a, outdeg.getOrDefault(a, 0) + 1);
                    indeg.put(b, indeg.getOrDefault(b, 0) + 1);
                    break;
                }
            }
        }
        checkAllCollision(hitTo, indeg, outdeg, willDie);
    }

    void checkAllCollision(Map<Worm, List<Worm>> hitTo, Map<Worm, Integer> indeg, Map<Worm, Integer> outdeg, Set<Worm> willDie) {
        Set<Worm> dead = new HashSet<>(willDie);

        for (Worm w : outdeg.keySet()) {
            dead.add(w);
        }
        for (Map.Entry<Worm, Integer> e : indeg.entrySet()) {
            Worm w = e.getKey();

            if (dead.contains(w)) continue;
            if (outdeg.getOrDefault(w, 0) > 0) continue;

            int gain = 0;
            for (Map.Entry<Worm, List<Worm>> x : hitTo.entrySet()) {
                if (x.getValue().contains(w)) {
                    gain += x.getKey().length;
                }
            }
            w.potential += gain;
        }
        for (Worm w : dead) {
            w.isLive = false;
        }
    }

    boolean isCollision(Worm a, Worm b) {
        System.out.printf("[isCollision] %d -> %d\n", a.id, b.id);
        // 해당 머리의 충돌 여부를 확인
        int headX = a.headX;
        int headY = a.headY;

        if(b.isStraight()) {        // 꼬리 부분 신경 쓰지 않음
            return checkCollision(headX, headY, b.headX, b.headY, b.dir, b.length-1);
        }

        // 머리와 꼬리를 신경 씀
        int headLen = b.straightCount;
        if(checkCollision(headX, headY, b.headX, b.headY, b.dir, headLen-1)) return true;
        int bodyLen = b.length - headLen;
        int bodyHeadX = b.headX - (b.straightCount * dx[b.dir]);
        int bodyHeadY = b.headY - (b.straightCount * dy[b.dir]);
        return checkCollision(headX, headY, bodyHeadX, bodyHeadY, b.prevDir, bodyLen-1);
    }

    boolean checkCollision(int headX, int headY, int x, int y, int dir, int len) {
        if(len == 0) return false;      // 머리와 머리 충돌은 이전에 처리

        if(dir == 0 || dir == 2) {      // 상 / 하
            if(headY != y) return false;
            int endX = x - (dx[dir] * len);
            return isContained(x, endX, headX);
        } else {    // 좌 우
            if(headX != x) return false;
            int endY = y - (dy[dir] * len);
            return isContained(y, endY, headY);
        }
    }

    boolean isContained(int s, int e, int target) {
        return Math.min(s, e) <= target && Math.max(e, s) >= target;
    }

    public 애벌레키우기_박재환.RESULT top5(int mTime)
    {
        System.out.printf("[TOP] curTime : %d\n", mTime);
        애벌레키우기_박재환.RESULT res = new 애벌레키우기_박재환.RESULT();
        jumpTime(mTime);
        List<Worm> alive = new ArrayList<>();
        for (Worm w : worms) {
            if (w.isLive) alive.add(w);
        }
        alive.sort((a, b) -> {
            if (a.length != b.length)
                return b.length - a.length;
            return b.id - a.id;
        });
        res.cnt = Math.min(5, alive.size());
        for (int i = 0; i < res.cnt; i++) {
            res.IDs[i] = alive.get(i).id;
        }
        return res;
    }

    boolean isNotBoard(int x, int y) {
        return x < 0 || y < 0 || x >= n || y >= n;
    }
}