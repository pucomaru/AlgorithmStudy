package feb.week1.codetree;

import java.util.*;
import java.io.*;

public class 해적선장코디_박재환 {
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
    static TreeSet<BattleShip> readyBattleShips;
    static PriorityQueue<BattleShip> waitBattleShips;
    static Map<Integer, BattleShip> bindBattleShips;
    static void init() throws IOException {
        int commandCount = Integer.parseInt(br.readLine().trim());
        int time = 0;
        while(commandCount-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int command = Integer.parseInt(st.nextToken());
            switch(command) {
                case 100: {
                    /**
                     * [공격 준비]
                     * N척의 선박에 사격 준비 지시
                     * 선발 (id, 공격력, 재장전 시간, 상태 - 초기 상태는 사격 대기)
                     */
                    readyAttack();
                    break;
                }
                case 200: {
                    /**
                     * [지원 요청]
                     * 새로운 선박 합류
                     */
                    addNewBattleShip();
                    break;
                }
                case 300: {
                    /**
                     * [함포 교체]
                     * id번 선박의 함포를 교체한다. (공격력 변경)
                     */
                    changeCanon();
                    break;
                }
                case 400: {
                    /**
                     * [공격 명령]
                     * 사격 대기 상태인 선박 중 공격력이 가장 높은 5척이 공격한다.
                     * - 공격력이 같다면 id가 낮은 것이 우선순위를 갖는다.
                     * - 사격 후 즉시 재장전에 들어간다. -> 사격 시점을 포함해 r 시간이 경과하면 사격 대기 상태로 변한다.
                     *
                     * => 총 피해량, 사격에 참여한 선박 수, 우선순위 id
                     */
                    sb.append(attack(time)).append('\n');
                    break;
                }
            }
            /**
             * 재장전 시간 감소
             */
            time++;
            while(!waitBattleShips.isEmpty() && waitBattleShips.peek().remainShoot <= time) {
                BattleShip battleShip = waitBattleShips.poll();
                readyBattleShips.add(battleShip);
            }
        }
    }
    static class BattleShip {
        int id;
        int pw;
        int reload;
        int remainShoot;

        BattleShip(int id, int pw, int reload) {
            this.id = id;
            this.pw = pw;
            this.reload = reload;
            
            this.remainShoot = 0;      // 초기에는 무조건 쏠 수 있도록 최소 값
        }

        int shoot(int time) {
            this.remainShoot = this.reload + time;
            return this.pw;
        }
    }
    /**
     * [공격 준비]
     */
    static void readyAttack() {
        bindBattleShips = new HashMap<>();
        waitBattleShips = new PriorityQueue<>((a, b) -> Integer.compare(a.remainShoot, b.remainShoot));
        readyBattleShips = new TreeSet<>((a, b) -> {
            if(a.pw != b.pw) return Integer.compare(b.pw, a.pw);        // 2. 공격력이 쎈 함선이 우선
            return Integer.compare(a.id, b.id);                         // id 가 작은 함선이 우선
        });

        int battleShipCount = Integer.parseInt(st.nextToken());
        while(battleShipCount-- > 0) {
            int id = Integer.parseInt(st.nextToken());
            int pw = Integer.parseInt(st.nextToken());
            int reload = Integer.parseInt(st.nextToken());
            BattleShip battleShip = new BattleShip(id, pw, reload);
            readyBattleShips.add(battleShip);
            bindBattleShips.put(id, battleShip);
        }
    }
    /**
     * [지원 요청]
     */
    static void addNewBattleShip() {
        int id = Integer.parseInt(st.nextToken());
        int pw = Integer.parseInt(st.nextToken());
        int reload = Integer.parseInt(st.nextToken());
        BattleShip battleShip = new BattleShip(id, pw, reload);
        readyBattleShips.add(battleShip);
        bindBattleShips.put(id, battleShip);
    }
    /**
     * [함포 교체]
     */
    static void changeCanon() {
        int id = Integer.parseInt(st.nextToken());
        int pw = Integer.parseInt(st.nextToken());

        BattleShip battleShip = bindBattleShips.get(id);
        if(readyBattleShips.remove(battleShip)) {
            battleShip.pw = pw;
            readyBattleShips.add(battleShip);
        } else {
            battleShip.pw = pw;
        }
    }
    /**
     * [공격 명령]
     */
    static String attack(int time) {
        List<BattleShip> shootList = new ArrayList<>();

        for(BattleShip battleShip : readyBattleShips) {
            shootList.add(battleShip);
            if(shootList.size() == 5) break;    // 최대 5개의 함선만 사격
        }

        // 업데이트
        StringBuilder shootSb = new StringBuilder();
        shootSb.append(' ').append(shootList.size()).append(' ');
        int totalPw = 0;
        for(BattleShip battleShip : shootList) {
            readyBattleShips.remove(battleShip);
            totalPw += battleShip.shoot(time);
            shootSb.append(battleShip.id).append(' ');
            waitBattleShips.add(battleShip);
        }
        shootSb.insert(0, totalPw);
        return shootSb.toString();
    }
}
