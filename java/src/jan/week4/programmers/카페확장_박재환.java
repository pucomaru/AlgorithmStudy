package jan.week4.programmers;

import java.util.*;

public class 카페확장_박재환 {
    public static void main(String[] args) {
        Solution problem = new Solution();
        int[] menu = {5,12,30};
        int[] order = {1,2,0,1};
        int k = 10;
        System.out.println(problem.solution(menu, order, k));
    }
}

class Solution {
    int[] exit;     // 각 손님이 나가는 시간을 기록
    public int solution(int[] menu, int[] order, int k) {
        calcExitTime(menu, order, k);

        int customerCount = order.length;
        int enterId = 0;
        int exitId = 0;

        int inCafe = 0;
        int max = 0;
        while(enterId < customerCount || exitId < customerCount) {
            int nextEnter = enterId < customerCount ?
                    enterId * k : Integer.MAX_VALUE;        // 다음 손님의 입장 시점
            int nextExit = exitId < customerCount ?
                    exit[exitId] : Integer.MAX_VALUE;        // 다음 손님의 퇴장 시점

            if(nextExit <= nextEnter) {     // 퇴장이 더 빠른 시점이라면
                inCafe--;           // 손님 퇴장
                exitId++;
            } else {
                inCafe++;           // 손님 입장
                enterId++;
                // 손님이 들어오는 시점에 최대 손님 수가 갱신된다.
                max = Math.max(max, inCafe);
            }
        }

        return max;
    }

    void calcExitTime(int[] menu, int[] order, int k) {
        exit = new int[order.length];
        for(int i=0; i<exit.length; i++) {
            exit[i] = i*k;
            if(i==0) {
                exit[i] += menu[order[i]];
                continue;
            }

            exit[i] = Math.max(exit[i-1], exit[i]) + menu[order[i]];
        }
    }
}