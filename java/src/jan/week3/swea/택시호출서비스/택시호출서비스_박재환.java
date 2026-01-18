package jan.week3.swea.택시호출서비스;

import java.util.*;

public class 택시호출서비스_박재환 {
    private static final int CMD_INIT				= 100;
    private static final int CMD_PICKUP				= 200;
    private static final int CMD_RESET				= 300;
    private static final int CMD_GET_BEST			= 400;

    private static final int MAX_M					= 2000;

    private static UserSolution usersolution = new UserSolution();

    static class Result
    {
        int mX, mY;
        int mMoveDistance;
        int mRideDistance;

        Result()
        {
            mX = mY = mMoveDistance = mRideDistance = -1;
        }
    }

    private static long mSeed;
    private static int pseudo_rand()
    {
        mSeed = (mSeed * 1103515245 + 12345) % 2147483647;
        return (int)(mSeed >> 16);
    }

    private static int[] mXs = new int[MAX_M];
    private static int[] mYs = new int[MAX_M];

    private static boolean run(Scanner sc) throws Exception
    {
        int Q;
        int N = 0, M, L, mNo;
        int mSX, mSY, mEX, mEY;
        int ret = -1, ans;

        Result res;
        int x, y, mdist, rdist;

        int[] mNos = new int[5];
        Q = sc.nextInt();
        mSeed = sc.nextLong();

        boolean okay = false;

        for (int q = 0; q < Q; ++q)
        {
            int cmd = sc.nextInt();

            switch(cmd)
            {
                case CMD_INIT:
                    N = sc.nextInt();
                    M = sc.nextInt();
                    L = N / 10;
                    for (int i = 0; i < M; ++i)
                    {
                        mXs[i] = pseudo_rand() % N;
                        mYs[i] = pseudo_rand() % N;
                    }
                    usersolution.init(N, M, L, mXs, mYs);
                    okay = true;
                    break;
                case CMD_PICKUP:
                    do
                    {
                        mSX = pseudo_rand() % N;
                        mSY = pseudo_rand() % N;
                        mEX = pseudo_rand() % N;
                        mEY = pseudo_rand() % N;
                    } while (mSX == mEX && mSY == mEY);
                    ret = usersolution.pickup(mSX, mSY, mEX, mEY);
                    ans = sc.nextInt();
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_RESET:
                    mNo = sc.nextInt();
                    res = usersolution.reset(mNo);
                    x = sc.nextInt();
                    y = sc.nextInt();
                    mdist = sc.nextInt();
                    rdist = sc.nextInt();
                    if (res.mX != x || res.mY != y || res.mMoveDistance != mdist || res.mRideDistance != rdist)
                        okay = false;
                    break;
                case CMD_GET_BEST:
                    usersolution.getBest(mNos);
                    for (int i = 0; i < 5; ++i)
                    {
                        ans = sc.nextInt();
                        if (mNos[i] != ans)
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
        System.setIn(new java.io.FileInputStream("C:\\Users\\doorm\\OneDrive\\바탕 화면\\Algorithm\\java\\src\\jan\\week3\\swea\\택시호출서비스\\res\\sample_input.txt"));

        Scanner sc = new Scanner(System.in);

        int TC = sc.nextInt();
        int MARK = sc.nextInt();

        for (int testcase = 1; testcase <= TC; ++testcase)
        {
            int score = run(sc) ? MARK : 0;
            System.out.println("#" + testcase + " " + score);
        }

        sc.close();

    }
}


/**
 * 코드 작성 영역
 */
class UserSolution
{
    class Taxi {
        int id;
        int x, y;
        int moveDist, rideDist;

        Taxi(int id, int x, int y) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.moveDist = 0;
            this.rideDist = 0;
        }
    }
    Taxi[] taxis;
    TreeSet<Taxi> taxisSet;
    int maxDistToCall;
    public void init(int N, int M, int L, int[] mXs, int[] mYs)
    {
        maxDistToCall = L;
        taxis = new Taxi[M];
        taxisSet = new TreeSet<>((t1, t2) -> {
            if(t1.rideDist == t2.rideDist) return Integer.compare(t1.id, t2.id);
            return Integer.compare(t2.rideDist, t1.rideDist);
        });

        for(int taxiId=0; taxiId<M; taxiId++) {
            Taxi taxi = new Taxi(taxiId+1, mXs[taxiId], mYs[taxiId]);
            taxis[taxiId] = taxi;
            taxisSet.add(taxi);
        }

        return;
    }

    public int pickup(int mSX, int mSY, int mEX, int mEY)
    {
        int minDistToCall = Integer.MAX_VALUE;
        int minTaxiIdx = -1;                    // 배열의 인덱스를 저장

        for(int i=0; i<taxis.length; i++) {
            Taxi taxi = taxis[i];

            // 손님으로부터 떨어져있는 거리를 구함
            int distToCall = Math.abs(taxi.x - mSX) + Math.abs(taxi.y - mSY);
            if(distToCall > maxDistToCall) continue;        // 손님을 잡을 수 없는 거리에 있는 경우 넘어감

            // 이전의 최소 거리보다 작거나, 택시의 번호가 더 작은 경우
            if(distToCall < minDistToCall || (distToCall == minDistToCall && taxis[minTaxiIdx].id > taxi.id)) {
                minTaxiIdx = i;
                minDistToCall = distToCall;
            }
        }
        // 조건에 맞는 택시를 찾지 못함
        if(minTaxiIdx == -1) return -1;

        // 조건에 맞는 택시를 찾았고, 택시 정보를 갱신함
        Taxi taxi = taxis[minTaxiIdx];
        taxisSet.remove(taxi);

        int toGoDist = Math.abs(taxi.x - mSX) + Math.abs(taxi.y - mSY);
        int rideDist = Math.abs(mSX - mEX) + Math.abs(mSY - mEY);
        taxi.rideDist += rideDist;
        taxi.moveDist += toGoDist + rideDist;
        taxi.x = mEX;
        taxi.y = mEY;

        taxisSet.add(taxi);
        return taxi.id;
    }

    public 택시호출서비스_박재환.Result reset(int mNo)
    {
        택시호출서비스_박재환.Result res = new 택시호출서비스_박재환.Result();

        Taxi taxi = taxis[mNo-1];

        res.mX = taxi.x;
        res.mY = taxi.y;
        res.mMoveDistance = taxi.moveDist;
        res.mRideDistance = taxi.rideDist;

        taxisSet.remove(taxi);

        taxi.moveDist = 0;
        taxi.rideDist = 0;

        taxisSet.add(taxi);
        return res;
    }

    public void getBest(int[] mNos)
    {
        int idx = 0;
        for(Taxi taxi : taxisSet) {
            mNos[idx++] = taxi.id;
            if(idx == 5) break;
        }

        return;
    }
}