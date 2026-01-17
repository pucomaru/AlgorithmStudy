package jan.week3.boj;

import java.util.*;
import java.io.*;

public class 사회망서비스_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * 사람 : Node
     * 관계 : Edge
     *
     * 얼리 아답터가 아닌 사람들은 자신의 모든 친구들이 얼리어답터 일 때만 아이디어를 받아들임
     *
     * 친구 관계 그래프는 트리이며, 사이클이 존재하지 않는다
     *
     * => 내가 알고있는 모든 친구들이 얼리 아답터야한다.
     */
    static StringTokenizer st;
    static List<Integer>[] connections;
    static boolean[] visited;
    static int minAdCount;
    static void init() throws IOException {
        minAdCount = 0;

        int connectionCount = Integer.parseInt(br.readLine().trim());
        connections = new List[connectionCount+1];
        visited = new boolean[connectionCount+1];
        for(int i=0; i<connectionCount+1; i++) connections[i] = new ArrayList<>();

        while(connectionCount-- > 1) {      // n-1 개의 간선 정보가 주어짐
            st = new StringTokenizer(br.readLine().trim());
            int from = Integer.parseInt(st.nextToken());
            int to = Integer.parseInt(st.nextToken());
            
            connections[from].add(to);
            connections[to].add(from);
        }

        /**
         * 부모 노드가 AD 가 아니라면, 자식 노드는 항상 AD 여야함
         * 부모 노드가 AD 라면, 자식 노드는 AD 가 필수가 아님
         */
        isAd(1);

        System.out.println(minAdCount);
    }

    static boolean isAd(int node) {        // 현재 노드가 AD 이어야 하는지 판단
        visited[node] = true;

        boolean ad = false;
        // 현재 노드와 연결된 다른 노드들을 확인
        for(int connection : connections[node]) {
            if(visited[connection]) continue;       // 이미 확인한 노드

            // 리프노드부터 탐색할 수 있도록 바텀업으로 구성
            if(!isAd(connection)) {     // 자식노드가 AD 가 아니라면, 부모 노드는 반드시 AD
                ad = true;
            }
        }

        if(ad) minAdCount++;

        return ad;
    }
}
