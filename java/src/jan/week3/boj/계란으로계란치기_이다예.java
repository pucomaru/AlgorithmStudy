package jan.week3.boj;

import java.io.*;
import java.util.*;

public class 계란으로계란치기_이다예 {
    static BufferedReader br;
    static StringTokenizer st;

    static int N;

    static Egg[] eggs;

    static int result;
    static boolean[] isBroken;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));

        // N = 계란의 수 ( 1 <= N <= 8)
        N = Integer.parseInt(br.readLine());
        // 계란 정보
        eggs = new Egg[N];

        for (int i = 0; i < N ; i++){
            st = new StringTokenizer(br.readLine());
            // 계란의 내구도와 무게
            int durability = Integer.parseInt(st.nextToken());
            int weight = Integer.parseInt(st.nextToken());

            Egg egg = new Egg(durability, weight);
            eggs[i] = egg;
        }

        // 규칙
        // 1. 가장 왼쪽의 계란을 듬
        // 2. 손에 들고 있는 계란으로 깨지지 않은 다른 계란 중에서 하나를 침 .
        // 2-1. 손에 든 계란이 깨지거나, 깨진 게란이 있으면 치지않고 넘어감 -> 손에 든 계란을 원래 자리에내려놓고 3번 진행
        // 2-2. 손에 든 계란이 깨지지않거나 걔진 게란이 없으면 안넘어가겠죠
        // 3. 가장 최근에 든 계란의 오른쪽 계란을 들고 2번 과정 진행
        // (종료 조건) 3.1 가장 최근에 든 계란이 가장 오른쪽에 위치할 계란일 경우 계란을 치는 과정 종료
        // 정답은 최대한 많은 계란을 깨는것이 목표
        result = 0;

        // 깨진 계란 체크
        isBroken = new boolean[N];
        dfs(0,0);
        System.out.println(result);
    }

    static void dfs(int now, int brokenEgg){
        // 다 깨졌거나 현재 계란 위치가 제일 오른쪽이면 종료
        if (now == N  || brokenEgg == N){
            if (result < brokenEgg) result = brokenEgg;
            return;
        }

        if (isBroken[now]) {
            dfs(now+1,brokenEgg);
            return;
        }

        // 현재 계란
        Egg nowEgg = eggs[now];
//        System.out.print(nowEgg.durability);
//        System.out.print(nowEgg.weight);
//        System.out.println(now);
        // 현재 계란의 가중치 저장
        int nowEggDurability = nowEgg.durability;

        boolean hit = false;
        for (int i = 0; i < N; i++) {
            // i가 현재 계란일 경우 pass
            if (i == now) continue;
            // 때릴 계란이 깨졌을 경우
            if (isBroken[i]) continue;
            hit = true;
            Egg fightEgg = eggs[i];
            // 상대 계란의 가중치 저장
            int fightEggDurability = fightEgg.durability;

            // 3번 과정으로 넘어갈 경우 ( 들고 있는 계란이 깨지거나 깨진 계란이 있을경우 )
            // 현재 계란도 깨지고 상대 계란도 깨졌을경우
            if ((nowEgg.durability - fightEgg.weight <= 0)
                    && fightEgg.durability - nowEgg.weight <= 0) {
                isBroken[now] = true;
                isBroken[i] = true;
                dfs(now+1, brokenEgg + 2);
                // 다음 for문을 위해 egg리스트 바꾼거 다시 원래대로
                isBroken[now] = false;
                isBroken[i] = false;
            }
            else if (nowEgg.durability - fightEgg.weight <= 0) { // 현재 계란만 꺠졌을 경우
                isBroken[now] = true;
                fightEgg.durability = fightEgg.durability - nowEgg.weight;
                dfs(now+1, brokenEgg+1);
                isBroken[now] = false;
                fightEgg.durability = fightEggDurability;
            }
            else if (fightEgg.durability - nowEgg.weight <= 0){ // 상대 계란만 깨졌을 경우
                isBroken[i] = true;
                nowEgg.durability = nowEgg.durability - fightEgg.weight;
                dfs(now+1, brokenEgg+1);
                isBroken[i] = false;
                nowEgg.durability = nowEggDurability;

            }// 들고있는게 안깨졌거나 다른 계란 쳤는데 아직 깨진게 없을 경우
            else {
                nowEgg.durability = nowEggDurability - fightEgg.weight;
                fightEgg.durability = fightEggDurability - nowEgg.weight;
                dfs(now+1,brokenEgg);
                nowEgg.durability = nowEggDurability;
                fightEgg.durability = fightEggDurability;
            }
        }
        if(!hit) dfs(now+1,brokenEgg);

    }

    static class Egg{
        int durability;
        int weight;

        Egg(int durability, int weight){
            this.durability = durability;
            this.weight = weight;
        }
    }

}
