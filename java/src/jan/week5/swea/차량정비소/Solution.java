package jan.week5.swea.차량정비소;

import java.util.*;
import java.io.*;

public class Solution {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String args[]) throws IOException
    {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        int tc = Integer.parseInt(br.readLine().trim());
        for(int i=1; i<tc+1; i++) {
            sb.append('#').append(i).append(' ');
            init();
            sb.append('\n');
        }
        br.close();
        System.out.println(sb);
    }

    /**
     * 고객이 이용했던 접수창구 번호, 정비창구 번호가 있다.
     *
     * 차량 정비소에는 N개의 접수창구, M개의 정비창구가 있다.
     * 접수창구 : 1 ~ N
     * 정비창구 : 1 ~ M
     *
     * 차량 정비 단계
     * 1. [접수창구]
     * - 고객 번호가 낮은 고객이 우선 순위가 높다.
     * - 창구 번호가 가장 낮은 곳이 우선 순위가 높다.
     * 2. [정비창구]
     * - 먼저 기다리는 순으로 처리한다.
     * - 동시에 왔다면 접수 창구 번호가 작은 고객이 우선순위가 높다.
     * - 정비 창구 번호가 작은 것이 우선 순위가 높다.
     * 각 담당자마다 처리 시간이 다르다.
     *
     * 고객은 도착하는 대로 1번부터 고객번호를 받는다.
     * 빈 창구가 없는 경우 기다린다.
     */
    static final int INF = Integer.MAX_VALUE;
    static int n, m, k, a, b;
    static Customer[] customers;
    static int[] receptionCounter;
    static int[] maintenanceCounter;
    static PriorityQueue<Customer> reception;
    static PriorityQueue<Customer> maintenance;
    static void init() throws IOException {
        StringTokenizer st;
        reception = new PriorityQueue<>((a, b) -> {
            if(a.visitTime == b.visitTime) return Integer.compare(a.id, b.id);
            return Integer.compare(a.visitTime, b.visitTime);
        });
        maintenance = new PriorityQueue<>((a, b) -> {
            if(a.receptionCounterTime == b.receptionCounterTime) return Integer.compare(a.receptionCounterId, b.receptionCounterId);
            return Integer.compare(a.receptionCounterTime, b.receptionCounterTime);
        });
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());
        a = Integer.parseInt(st.nextToken());
        b = Integer.parseInt(st.nextToken());
        // 접수창구
        receptionCounter = new int[n];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) receptionCounter[i] = Integer.parseInt(st.nextToken());
        // 정비창구
        maintenanceCounter = new int[m];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<m; i++) maintenanceCounter[i] = Integer.parseInt(st.nextToken());
        // 고객
        customers = new Customer[k+1];
        st = new StringTokenizer(br.readLine().trim());
        for(int i=1; i<k+1; i++) customers[i] = new Customer(i, Integer.parseInt(st.nextToken()));

        work();
        sb.append(findCandidateCustomer());
    }
    static class Customer {
        int id;
        int visitTime;                  // 정비소 방문시간
        int receptionCounterId;         // 접수창구번호
        int receptionCounterTime;       // 접수창구완료시간
        int maintenanceCounterId;       // 정비창구번호
        int maintenanceCounterTime;     // 정비창구완료시간

        Customer(int id, int visitedTime) {
            this.id = id;
            this.visitTime = visitedTime;
            this.receptionCounterId = -1;
            this.receptionCounterTime = INF;
            this.maintenanceCounterId = -1;
            this.maintenanceCounterTime = INF;
        }
    }
    /**
     * [시뮬레이션]
     */
    static int finishedMaintenanceCustomer;
    static int[] receptionCounterState;
    static int[] maintenanceCounterState;
    static void work() {
        finishedMaintenanceCustomer = 0;
        receptionCounterState = new int[n];
        maintenanceCounterState = new int[m];
        int cId = 1;        // 고객 id
        int time = 0;       // 시간

        while(finishedMaintenanceCustomer < k) {    // 모든 고객을 처리할 때까지
            // 현재 도착한 고객
            while(cId < k+1 && customers[cId].visitTime == time) {
                reception.offer(customers[cId]);
                cId++;
            }
            processReceptionCounter(time);
            processMaintenanceCounter(time);
            time++;
        }
    }
    /**
     * [접수창구]
     *  - 여러 고객이 기다리고 있는 경우, 고객 번호가 낮은 순서대로 우선 접수한다.
     *  - 빈 창구가 여러 곳인 경우 접수 창구 번호가 작은 곳으로 간다.
     *
     *  => time 시점을 기준으로 반복적으로 처리
     */
    static void processReceptionCounter(int time) {
        // 접수 완료 할 수 있는 고객 처리
        for(int i=0; i<n; i++) {
            if(receptionCounterState[i] == 0) continue;         // 빈 창구
            Customer curCs = customers[receptionCounterState[i]];
            if(curCs.receptionCounterTime == time) {
                receptionCounterState[i] = 0;       // 빈 칸 처리
                maintenance.offer(curCs);           // 정비창구로 이동
            }
        }

        // 빈 창구에 고객 할당
        for(int i=0; i<n; i++) {
            if(receptionCounterState[i] != 0) continue;
            if(reception.isEmpty()) break;
            Customer curCs = reception.poll();
            receptionCounterState[i] = curCs.id;
            curCs.receptionCounterTime = time + receptionCounter[i];
            curCs.receptionCounterId = i+1;
        }
    }
    /**
     * [정비창구]
     * - 기본적으로는 도착한 순서대로 처리한다.
     * - 도착한 시간이 같다면, 접수 창구 번호가 낮은 순으로 처리한다.
     */
    static void processMaintenanceCounter(int time) {
        // 완료 할 수 있는 고객 처리
        for(int i=0; i<m; i++) {
            if(maintenanceCounterState[i] == 0) continue;         // 빈 창구
            Customer curCs = customers[maintenanceCounterState[i]];
            if(curCs.maintenanceCounterTime == time) {
                maintenanceCounterState[i] = 0;       // 빈 칸 처리
                finishedMaintenanceCustomer++;        // 작업이 모두 완료된 고객 기록
            }
        }

        // 빈 창구에 고객 할당
        for(int i=0; i<m; i++) {
            if(maintenanceCounterState[i] != 0) continue;
            if(maintenance.isEmpty()) break;
            Customer curCs = maintenance.poll();
            maintenanceCounterState[i] = curCs.id;
            curCs.maintenanceCounterTime = time + maintenanceCounter[i];
            curCs.maintenanceCounterId = i+1;
        }
    }
    /**
     * 조건에 만족하는 고객 찾기
     */
    static int findCandidateCustomer() {
        int sum = 0;
        for(int i=1; i<k+1; i++) {
            if(customers[i].receptionCounterId == a && customers[i].maintenanceCounterId == b) sum += customers[i].id;
        }
        return sum == 0 ? -1 : sum;
    }
}
