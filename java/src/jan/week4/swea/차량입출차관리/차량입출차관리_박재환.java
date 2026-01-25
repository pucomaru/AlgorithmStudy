package jan.week4.swea.차량입출차관리;

import java.util.*;
import java.io.*;

public class 차량입출차관리_박재환 {
    private static final int CMD_INIT           = 100;
    private static final int CMD_ENTER          = 200;
    private static final int CMD_PULL_OUT       = 300;
    private static final int CMD_SEARCH         = 400;

    private static UserSolution usersolution = new UserSolution();

    public static class RESULT_E
    {
        int success;
        String locname;

        RESULT_E()
        {
            success = -1;
        }
    }

    public static class RESULT_S
    {
        int cnt;
        String[] carlist = new String[5];

        RESULT_S()
        {
            cnt = -1;
        }
    }

    private static boolean run(BufferedReader br) throws Exception
    {
        int Q, N, M, L;
        int mTime;

        String mCarNo;
        String mStr;

        int ret = -1, ans;

        RESULT_E res_e;
        RESULT_S res_s;

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
                    N = Integer.parseInt(st.nextToken());
                    M = Integer.parseInt(st.nextToken());
                    L = Integer.parseInt(st.nextToken());
                    usersolution.init(N, M, L);
                    okay = true;
                    break;
                case CMD_ENTER:
                    mTime = Integer.parseInt(st.nextToken());
                    mCarNo = st.nextToken();
                    res_e = usersolution.enter(mTime, mCarNo);
//                    System.out.printf("{%d, %s}\n", res_e.success, res_e.locname);
                    ans = Integer.parseInt(st.nextToken());
                    if (res_e.success != ans)
                        okay = false;
                    if (ans == 1)
                    {
                        mStr = st.nextToken();
                        if (!mStr.equals(res_e.locname))
                            okay = false;
                    }
                    break;
                case CMD_PULL_OUT:
                    mTime = Integer.parseInt(st.nextToken());
                    mCarNo = st.nextToken();
                    ret = usersolution.pullout(mTime, mCarNo);
//                    System.out.printf("%d\n", ret);
                    ans = Integer.parseInt(st.nextToken());
                    if (ret != ans)
                        okay = false;
                    break;
                case CMD_SEARCH:
                    mTime = Integer.parseInt(st.nextToken());
                    mStr = st.nextToken();
                    res_s = usersolution.search(mTime, mStr);
//                    System.out.printf("{%d, %s}\n", res_s.cnt, Arrays.toString(res_s.carlist));
                    ans = Integer.parseInt(st.nextToken());
                    if (res_s.cnt != ans)
                        okay = false;
                    for (int i = 0; i < ans; ++i)
                    {
                        mCarNo = st.nextToken() + mStr;
                        if (!mCarNo.equals(res_s.carlist[i]))
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
        System.setIn(new java.io.FileInputStream("C:\\Users\\doorm\\Desktop\\Algorithm\\java\\src\\jan\\week4\\swea\\res\\sample_input_차량입출차관리.txt"));

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

/**
 * 코드 영역
 */
class UserSolution
{
    /**
     * N 개의 구역 -> 각 구역에는 M 개의 슬롯이 있다.
     * -> 구역들은 영어 대문자로 구분되며 'A' 부터 순서대로 부여된다. (A -> B -> C)
     * -> 슬롯들을 3자리로 구분 -> (000 -> 001 -> 002 -> 003)
     *
     * => 주차 위치 = 구역+슬롯 (A001)
     *
     * [차량 입차]
     * - 빈 슬롯이 가장 많은 구역 중, 대문자 순서가 가장 앞선 구역이 선택
     * - 선택된 구역에서 숫자 번호가 가장 앞선 빈 슬롯에 차량 보관
     * - 빈 슬롯이 없는 경우 주차 X
     *
     * => 주차한지 L 시간이 지나면 견인
     */
    int n, m, l;
    int[] emptySpace;       // 구역별 빈 자리 수 [N]
    PriorityQueue<Integer>[] slotPointer;      // 각 구역의 슬롯 포인터 [N]
    HashMap<String, Car> cars;      // 주차되어있는 차량 정보
    HashMap<String, Towing> towings;    // 견인된 차량 정보
    PriorityQueue<TowingInfo> towingInfos;  // 견인 정보
    HashSet<String>[] carNos;
    public void init(int N, int M, int L)
    {
        /**
         * 구역 / 슬롯 초기화
         */
        cars = new HashMap<>();
        towings = new HashMap<>();
        towingInfos = new PriorityQueue<>((a, b) -> Integer.compare(a.towTime, b.towTime));
        carNos = new HashSet[10000];       // 0000 ~ 9999
        for(int i=0; i<10000; i++) carNos[i] = new HashSet<>();

        n = N;          // 구역
        m = M;          // 슬롯
        l = L;          // 최대 주차 시간
        emptySpace = new int[N];
        slotPointer = new PriorityQueue[N];
        for(int i=0; i<n; i++) {
            slotPointer[i] = new PriorityQueue<>(Integer::compare);
            for(int j=0; j<m; j++) {
                slotPointer[i].offer(j);
            }
        }

        // 초기에는 모든 슬롯에 주차 가능
        Arrays.fill(emptySpace, M);
    }

    class Car {
        int inTime;
        int area;
        int slot;

        Car(int inTime, int area, int slot) {
            this.inTime = inTime;
            this.area = area;
            this.slot = slot;
        }
    }

    class Towing {
        int inTime;
        int towTime;

        Towing(int inTime, int towTime) {
            this.inTime = inTime;
            this.towTime = towTime;
        }
    }

    class TowingInfo {
        int inTime;
        int towTime;
        String carNo;

        TowingInfo(int inTime, int towTime, String carNo) {
            this.inTime = inTime;
            this.towTime = towTime;
            this.carNo = carNo;
        }
    }

    /**
     * 차량이 입차한다.
     *
     * @param mTime
     * 입차한 시간
     * @param mCarNo
     * 입차한 차량 번호
     * @return
     * RESULT_E (success, localname) => 성공 1 / 실패 0
     */
    public 차량입출차관리_박재환.RESULT_E enter(int mTime, String mCarNo)
    {
        /**
         * 견인된 차량이 들어오는 경우, 더 이상 견인된 차량으로 생각하지 않는다.
         */
        차량입출차관리_박재환.RESULT_E res_e = new 차량입출차관리_박재환.RESULT_E();

        lazyUpdateTowedCar(mTime);
        towings.remove(mCarNo);

        int parkingArea = findParkingArea();
        if(parkingArea == -1) {     // 주차 실패
            res_e.success = 0;
            return res_e;
        }
        // 주차 가능
        res_e.success = 1;
        int slotNum = findParkingSlot(parkingArea);
        // 주차 처리
        emptySpace[parkingArea]--;

        String localname = (char)('A' + parkingArea) + String.format("%03d", slotNum);
        res_e.locname = localname;

        Car car = new Car(mTime, parkingArea, slotNum);
        int lastNo = Integer.parseInt(getLastFour(mCarNo));
        carNos[lastNo].add(mCarNo);
        cars.put(mCarNo, car);
        towingInfos.offer(new TowingInfo(car.inTime, car.inTime + l, mCarNo));
        return res_e;
    }

    String getLastFour(String carNo) {
        return carNo.substring(3);
    }

    int findParkingArea() {
        /**
         * 주차할 구역을 찾는다.
         * -> 주차할 구역이 없다면 : maxEmptyCount == 0 -> -1 반환
         */
        int minEmptyArea = -1;
        int maxEmptyCount = 0;

        for(int i=0; i<n; i++) {
            if(maxEmptyCount >= emptySpace[i]) continue;

            minEmptyArea = i;
            maxEmptyCount = emptySpace[i];
        }

        return maxEmptyCount == 0 ? -1 : minEmptyArea;
    }

    int findParkingSlot(int parkingArea) {
        /**
         * 현재 구역에 주차가 가능한 가장 작은 slot 번호를 찾는다.
         */
        return slotPointer[parkingArea].poll();
    }

    /**
     * 차량을 출차한다.
     *
     * @param mTime
     * 차량 출차 시간
     * @param mCarNo
     * 차량 번호
     * @return
     * 주차된 기간을 반환 / 견인된 경우 (주차된 기간 + 견인된 기간 * 5) * -1
     * 주차되어 있지 않다면 -1
     */
    public int pullout(int mTime, String mCarNo)
    {
        lazyUpdateTowedCar(mTime);

        Car parkedCar = cars.get(mCarNo);
        if(parkedCar != null) {
            // 주차 혹은 견인 상태
            int inTime = parkedCar.inTime;
            int parkingArea = parkedCar.area;
            int slotNum = parkedCar.slot;

            emptySpace[parkingArea]++;
            slotPointer[parkingArea].offer(slotNum);
            cars.remove(mCarNo);

            return mTime - inTime;
        }

        Towing towedCar = towings.get(mCarNo);
        if(towedCar != null) {
            // 주차 혹은 견인 상태
            int inTime = towedCar.inTime;
            int towTime = towedCar.towTime;

            towings.remove(mCarNo);

            cars.remove(mCarNo);

            return ((towTime - inTime) + (mTime - towTime) *5)*-1;
        }

        return -1;
    }

    /**
     * 주차 / 견인된 차량 중 차량 번호 뒷 4자리가 일치하는 차량을 우선순위 순으로 5대 검색한다.
     * @param mTime
     * 찾을 시간
     * @param mStr
     * 차량 뒷 4자리 번호
     * @return
     * 우선 순위 5
     * - 주차 > 견인
     * - 앞 두자리 오름차순
     * - 세 번째 자리 사전 순
     */
    public 차량입출차관리_박재환.RESULT_S search(int mTime, String mStr)
    {
        차량입출차관리_박재환.RESULT_S res_s = new 차량입출차관리_박재환.RESULT_S();

        lazyUpdateTowedCar(mTime);

        int carNo = Integer.parseInt(mStr);
        HashSet<String> candidateCars = carNos[carNo];

        String[] top5 = new String[5];
        int cnt = 0;

        for(String target : candidateCars) {
            boolean isParked = cars.containsKey(target);
            boolean isTowed = towings.containsKey(target);

            if(!isParked && !isTowed) continue;   // 존재하지 않는 차량

            int idx = cnt;
            for(int i=0; i<cnt; i++) {
                if(compare(target, top5[i]) < 0) {
                    idx = i;
                    break;
                }
            }

            if(idx < 5) {
                if(cnt < 5) cnt++;
                for(int i=cnt-1; i>idx; i--) {
                    top5[i] = top5[i-1];
                }
                top5[idx] = target;
            }
        }

        res_s.cnt = cnt;
        res_s.carlist = makeCarNosArr(cnt, top5);

        return res_s;
    }

    String[] makeCarNosArr(int cnt, String[] top5) {
        String[] result = new String[cnt];
        for(int i=0; i<cnt; i++) result[i] = top5[i];
        return result;
    }

    int compare(String a, String b) {
        boolean aParked = cars.containsKey(a);
        boolean bParked = cars.containsKey(b);

        if (aParked != bParked) return aParked ? -1 : 1;

        int aPrefixNum = (a.charAt(0) - '0') * 10 + (a.charAt(1) - '0');
        int bPrefixNum = (b.charAt(0) - '0') * 10 + (b.charAt(1) - '0');
        if(aPrefixNum != bPrefixNum) return Integer.compare(aPrefixNum, bPrefixNum);

        return a.charAt(2) - b.charAt(2);
    }



    void lazyUpdateTowedCar(int mTime) {
        /**
         * 견인 되어 있는 차량 Lazy 업데이트
         */
        while(!towingInfos.isEmpty() && towingInfos.peek().towTime <= mTime) {
            TowingInfo towingInfo = towingInfos.poll();

            // 같은 차량 조회
            Car car = cars.get(towingInfo.carNo);
            if(car == null) continue;       // 출차된 차량
            if(car.inTime != towingInfo.inTime) continue;       // 중간에 입출차 이벤트가 발생

            emptySpace[car.area]++;
            slotPointer[car.area].offer(car.slot);
            cars.remove(towingInfo.carNo);

            towings.put(towingInfo.carNo, new Towing(towingInfo.inTime, towingInfo.towTime));
        }
    }
}