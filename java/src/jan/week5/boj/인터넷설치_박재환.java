package jan.week5.boj;

import java.util.*;
import java.io.*;

public class 인터넷설치_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static final int INF = 1005;
    static StringTokenizer st;
    static int n, k, maxPrice;
    static List<int[]>[] lanCables;
    static void init(BufferedReader br) throws IOException {
        maxPrice = 1;

        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        int p = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        lanCables = new List[n+1];
        for(int i=0; i<n+1; i++) lanCables[i] = new ArrayList<>();
        while(p-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            int cost = Integer.parseInt(st.nextToken());
            // 인터넷 선은 서로 쌍으로 이루어져 있다.
            lanCables[from].add(new int[] {to, cost});
            lanCables[to].add(new int[] {from, cost});
            maxPrice = Math.max(maxPrice, cost);
        }

        System.out.println(findMinCostLan());
    }

    static int findMinCostLan() {
        int minPrice = 1_000_000;
        int l = 1, r = maxPrice;
        while(l <= r) {
            int mid = (l + r) / 2;
            if(canConnectNComputer(mid)) {
                minPrice = Math.min(minPrice, mid);
                r = mid-1;
            } else {
                l = mid+1;
            }
        }
        return minPrice == 1_000_000 ? -1 : minPrice;
    }

    static boolean canConnectNComputer(int limitPrice) {
        // limitPrice 를 넘는 경로는 무료로 처리
        Deque<Integer> dq = new ArrayDeque<>();
        int[] useFree = new int[n+1];
        Arrays.fill(useFree, INF);

        dq.offerLast(1);
        useFree[1] = 0;

        while(!dq.isEmpty()) {
            int from = dq.pollFirst();

            for(int[] conn : lanCables[from]) {
                int to = conn[0];
                int cost = conn[1];

                int extraFree = cost > limitPrice ? 1 : 0;

                if(useFree[from] + extraFree < useFree[to]) {
                    useFree[to] = useFree[from] + extraFree;
                    if(extraFree == 0) dq.offerFirst(to);
                    else dq.offerLast(to);
                }
            }
        }
        return useFree[n] <= k;
    }
}
