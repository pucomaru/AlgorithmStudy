package jan.week3.boj;

import java.util.*;
import java.io.*;

public class 계란으로계란치기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * 계란의 내구도가 0 이하가 되는 순간 깨진다.
     * -> 계란의 내구도는 상대 계란의 무게만큼 깎인다.
     *
     * 가장 왼쪽 계란부터 시작해서 내리친다.
     * -> 손에 들고 있는 계란으로 아직 깨지지 않은 계란을 친다.
     */
    static StringTokenizer st;
    static int eggCount;
    static Egg[] eggs;              // 계란 상태를 기록
    static int maxFragileEggs;
    static void init() throws IOException {
        maxFragileEggs = 0;

        eggCount = Integer.parseInt(br.readLine().trim());
        eggs = new Egg[eggCount];
        for(int i=0; i<eggCount; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int hard = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());
            eggs[i] = new Egg(hard, weight);
        }

        // 계란의 개수가 최대 8개 -> 백트래킹으로 모든 조합 확인
        crashEggs(0);

        System.out.println(maxFragileEggs);
    }

    static class Egg {
        int hard;
        int weight;

        public Egg(int hard, int weight) {
            this.hard = hard;
            this.weight = weight;
        }
    }

    static void crashEggs(int holdIdx) {
        if(holdIdx == eggCount) {               // 모든 계란을 내리친 경우
            int fragileEgg = 0;
            for(Egg egg : eggs) {
                if(egg.hard < 1) fragileEgg++;
            }
            maxFragileEggs = Math.max(maxFragileEggs, fragileEgg);
            return;
        }
        Egg holdingEgg = eggs[holdIdx];         // 현재 들고 있는 계란
        if(holdingEgg.hard < 1) {               // 현재 들고 있는 계란이 깨졌다면, 다음 계란을 든다.
            crashEggs(holdIdx+1);
            return;
        }

        boolean hasNext = false;                // 현재 들고 있는 계란으로 아무것도 칠 수 없는 경우를 판단
        // 현재 들고 있는 계란이 아직 깨지지 않았다면
        for(int targetIdx=0; targetIdx<eggCount; targetIdx++) {
            if(targetIdx == holdIdx) continue;  // 자기 자신을 치는 경우는 존재할 수 없다.
            Egg targetEgg = eggs[targetIdx];
            if(targetEgg.hard < 1) continue;    // 대상 계란이 깨져있다면, 다음 계란을 탐색한다.
            hasNext = true;
            // 계란으로 계란을 칠 수 있다.
            holdingEgg.hard -= targetEgg.weight;
            targetEgg.hard -= holdingEgg.weight;
            crashEggs(holdIdx+1);
            holdingEgg.hard += targetEgg.weight;
            targetEgg.hard += holdingEgg.weight;
        }
        if(!hasNext) crashEggs(holdIdx+1);
    }
}
