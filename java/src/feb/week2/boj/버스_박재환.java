package feb.week2.boj;

import java.util.*;
import java.io.*;

public class 버스_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static StringTokenizer st;
    static int k, n, c;
    static Group[] groups;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        k = Integer.parseInt(st.nextToken());           // 그룹의 수
        n = Integer.parseInt(st.nextToken());           // 정류장 개수
        c = Integer.parseInt(st.nextToken());           // 버스 최대 수용 인원 수

        groups = new Group[k];
        for(int i=0; i<k; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int s = Integer.parseInt(st.nextToken())-1;
            int e = Integer.parseInt(st.nextToken())-1;
            int size = Integer.parseInt(st.nextToken());
            Group group = new Group(s, e, size);
            groups[i] = group;
        }

        /**
         * 버스는 1, 2, ..., N 번 순서로 정류장을 방문한다.
         *
         * Greedy
         * => 빨리빨리 내려야지 최대한 많은 사람을 태울 수 있다.
         */
        System.out.println(solution());
    }
    static final Bus DUMMY = new Bus(0);
    static Bus[] busStops;
    static int solution() {
        int total = 0;
        Arrays.sort(groups);
        buildBus();
        for(Group group : groups) {
            Bus bus = query(1, 0, n-1, group.s, group.e-1);
            int remain = c - bus.cur;       // 탑승객을 태울 수 있는 남은 좌석
            if(remain < 1) continue;

            // 태울 수 있음
            int board = Math.min(group.size, remain);       // 태울 수 있는 최대 인원
            update(1, 0, n-1, group.s, group.e-1, board);
            total += board;
        }
        return total;
    }
    static class Bus {
        int cur;
        int lazy;

        Bus(int cur) {
            this.cur = cur;
            this.lazy = 0;
        }
    }
    static void buildBus() {
        busStops = new Bus[4*n];
        for(int i=0; i<4*n; i++) busStops[i] = new Bus(0);
    }
    static Bus query(int id, int l, int r, int s, int e) {
        /**
         * 찾고자 하는 구간까지의
         * - 최대 cur
         * 을 찾는다.
         */
        if(r < s || l > e) return DUMMY;
        if(l >= s && r <= e) return busStops[id];
        push(id, l, r);
        int mid = l + (r-l)/2;
        return merge(query(2*id, l, mid, s, e), query(2*id+1, mid+1, r, s, e));
    }
    static void update(int id, int l, int r, int s, int e, int target) {
        if(r < s || l > e) return;
        if(l >= s && r <= e) {
            busStops[id].cur += target;
            busStops[id].lazy += target;
            return;
        }
        push(id, l, r);
        int mid = l + (r-l)/2;
        update(2*id, l, mid, s, e, target);
        update(2*id+1, mid+1, r, s, e, target);
        busStops[id] = merge(busStops[2*id], busStops[2*id+1]);
    }
    static Bus merge(Bus a, Bus b) {
        return a.cur > b.cur ? new Bus(a.cur) : new Bus(b.cur);
    }
    static void push(int id, int l, int r) {
        if(busStops[id].lazy == 0 || l == r) return;
        /**
         * 구간의 최대합만 관리하기 때문에 lazy 단발성 합
         */
        int lazy = busStops[id].lazy;
        busStops[2*id].cur = busStops[2*id].cur + lazy;
        busStops[2*id].lazy = busStops[2*id].lazy + lazy;
        busStops[2*id+1].cur = busStops[2*id+1].cur + lazy;
        busStops[2*id+1].lazy = busStops[2*id+1].lazy + lazy;
        busStops[id].lazy = 0;
    }
    static class Group implements Comparable<Group> {
        int s, e;       // 출발지, 목적지
        int size;       // 총 인원

        Group(int s, int e, int size) {
            this.s = s;
            this.e = e;
            this.size = size;
        }

        public int compareTo(Group o) {
            if(this.e == o.e) return Integer.compare(this.s, o.s);
            return Integer.compare(this.e, o.e);
        }
    }
}
