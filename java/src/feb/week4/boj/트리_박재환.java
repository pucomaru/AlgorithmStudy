package feb.week4.boj;

import java.util.*;
import java.io.*;

public class 트리_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static class Node {
        int id;
        Node parent;
        List<Node> childs;

        Node(int id, Node parent) {
            this.id = id;
            this.parent = parent;
            childs = new ArrayList<>();
        }
    }
    static StringTokenizer st;
    static int n;
    static Map<Integer, Node> nodes;
    static void init() throws IOException {
        nodes = new HashMap<>();

        n = Integer.parseInt(br.readLine().trim());

        // 노드를 먼저 생성
        // 문제에서 순서대로 들어온다는 보장 없음
        for (int i = 0; i < n; i++) {
            nodes.put(i, new Node(i, null));
        }
        st = new StringTokenizer(br.readLine().trim());
        for(int i=0; i<n; i++) {
            int pId = Integer.parseInt(st.nextToken());
            if(pId == -1) continue;     // 루트 노드는 변경 없음

            Node parent = nodes.get(pId);
            Node child = nodes.get(i);
            child.parent = parent;
            parent.childs.add(child);
        }

        int removeTarget = Integer.parseInt(br.readLine().trim());
        removeNode(removeTarget);

        int leafNode = 0;
        for(Node node : nodes.values()) if(node.childs.isEmpty()) leafNode++;
        System.out.println(leafNode);
    }
    static void removeNode(int removeTarget) {
        Node node = nodes.get(removeTarget);
        /**
         * 현재 노드의 자식 노드 지우기
         */
        for (Node child : new ArrayList<>(node.childs)) {
            removeNode(child.id);
        }
        /**
         * 현재 노드의 부모 노드 갱신해주기
         */
        if(node.parent != null) {
            node.parent.childs.remove(node);
        }
        nodes.remove(removeTarget);
    }
}
