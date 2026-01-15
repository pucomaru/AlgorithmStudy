package jan.week3.programmers;

import java.util.PriorityQueue;

public class 신입사원교육_박재환 {
    /*
            2명의 신입사원이 같이 공부하면 서로의 능력을 흡수한다.
            모든 신입사원들의 능력치 합을 최소화 하고싶다.

            => 가장 능력이 낮은 두 신입 사원을 늘 뽑는다

            => PQ 사용? => offer / poll => 모두 log n
        */
    public int solution(int[] ability, int number) {
        initOnboarding(ability);

        while(number-- > 0) {
            int a = newEmps.poll();
            int b = newEmps.poll();

            int total = a+b;
            newEmps.offer(total);
            newEmps.offer(total);
        }

        int total = 0;
        while(!newEmps.isEmpty()) total += newEmps.poll();

        return total;
    }

    PriorityQueue<Integer> newEmps;
    void initOnboarding(int[] ability) {
        newEmps = new PriorityQueue<>();

        for(int i : ability) {
            newEmps.offer(i);
        }
    }
}
