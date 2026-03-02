package feb.week4.codetree;

import java.util.*;
import java.io.*;

public class 색깔트리_박재환 {
    /**
     * 노드 : [color, maxDepth]
     *
     * 노드를 동적으로 처리, 색 변경 가능
     *
     * 처음에는 아무 노드도 존재하지 않는다.
     *
     * [노드 추가] - 200,000번 호출
     * - 각 노드는 고유한 번호를 갖는다.
     * - 부모 노드 번호, 색깔, 최대 깊이를 갖는다.
     * - color : 1 ~ 5 ( 1 : 빨강 ,2 : 주황, 3 : 노랑, 4 : 초록, 5 : 파랑)
     * - 부모 노드번호가 -1 이면 새로운 트리의 루트 노드가 된다.
     *
     * [색깔 변경] - 50,000번 호출
     * - 특정 노드 색을 바꾸면, 서브트리의 모든 노드 색도 바뀐다.
     *
     * [색 조회] - 20,000 번 호출
     * 특정 노드의 현재 색을 조회한다.
     *
     * [점수 조회] - 100 번 호출
     * 모든 노드의 가치를 계산해, 가치 제곱의 합을 출력한다.
     */
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static class Node {
        /**
         * 각 노드는
         * - id
         * - parentId
         * - color
         * - value
         *
         * +
         * 각 노드는 자식들을 가질 수 있다. -> depth 와 비교
         * parentId 로, parent 를 참조
         */
        int id;
        Node parent;
        int depth;
        int color;
        int updatedAt;
        List<Node> childs;

        Node(int id, Node parent, int depth, int color, int curTime) {
            this.id = id;
            this.parent = parent;
            this.depth = depth;
            this.color = color;
            this.updatedAt = curTime;
            this.childs = new ArrayList<>();
        }
    }
    static final int ADD_NODE = 100;
    static final int CHANGE_COLOR = 200;
    static final int QUERY_COLOR = 300;
    static final int QUERY_SCORE = 400;

    static StringTokenizer st;
    static List<Node> tree;
    static Map<Integer, Node> nodes;
    static int curTime;
    static void init() throws IOException {
        curTime = 0;
        tree = new ArrayList<>();
        nodes = new HashMap<>();

        int tc = Integer.parseInt(br.readLine().trim());
        while(tc-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int cmd = Integer.parseInt(st.nextToken());

            if(cmd == ADD_NODE) {
                int id = Integer.parseInt(st.nextToken());
                int parentId = Integer.parseInt(st.nextToken());
                int color = Integer.parseInt(st.nextToken());
                int depth = Integer.parseInt(st.nextToken());
                Node parent = nodes.get(parentId);
                Node newNode = new Node(id, parent, depth, color, curTime);
                addNode(newNode);
            }
            else if(cmd == CHANGE_COLOR) {
                int id = Integer.parseInt(st.nextToken());
                int color = Integer.parseInt(st.nextToken());
                Node cur = nodes.get(id);
                changeColor(cur, color);
            }
            else if(cmd == QUERY_COLOR) {
                int id = Integer.parseInt(st.nextToken());
                Node node = nodes.get(id);
                sb.append(queryColor(node)).append('\n');
            }
            else if(cmd == QUERY_SCORE) {
                sb.append(queryScore()).append('\n');
            }
            curTime++;
        }
    }
    static void addNode(Node node) {
        /**
         * parent 가 null -> root 노드임
         */
        if(node.parent == null) {
            tree.add(node);
            nodes.put(node.id, node);
            return;
        }

        // 자식 노드라면, depth 확인
        boolean isOk = canAdd(node, 1);
        if(!isOk) return;

        node.parent.childs.add(node);       // 부모노드에 자식 추가
        nodes.put(node.id, node);
    }
    static boolean canAdd(Node cur, int depth) {
        if (cur.parent == null) {
            if (cur.depth < depth) return false;
            return true;
        }
        // 부모 노드로 탐색하면서, 가능한지 순차적으로 확인
        boolean isOk = canAdd(cur.parent, depth + 1);
        if (!isOk) return false;
        if (cur.depth < depth) return false;

        return true;
    }

    static void changeColor(Node cur, int color) {
        cur.color = color;
        cur.updatedAt = curTime;
    }

    static int queryColor(Node cur) {
        int lastUpdatedAt = -1;
        int color = -1;

        while(cur != null) {
            if(cur.updatedAt > lastUpdatedAt) {
                lastUpdatedAt = cur.updatedAt;
                color = cur.color;
            }
            cur = cur.parent;
        }
        return color;
    }

    static long queryScore() {
        long score = 0;
        /**
         * 트리별로 점수 구하기
         */
        for(Node root : tree) {
            long[] result = findDiffColor(root, 0, -1);
            score += result[0];
        }
        return score;
    }
    static long[] findDiffColor(Node cur, int propagationColor, int propagationTime) {
        int curColor = propagationColor;
        int curUpdatedAt = propagationTime;
        if(cur.updatedAt > curUpdatedAt) {
            curColor = cur.color;
            curUpdatedAt = cur.updatedAt;
        }

        // 현재 색상
        int mask = curColor != 0 ? 1 << (curColor) : 0;     // 색상은 1 ~ 5
        long total = 0;

        for(Node child : cur.childs) {
            long[] temp = findDiffColor(child, curColor, curUpdatedAt);
            total += temp[0];
            mask |= temp[1];
        }

        int diffColorCnt = Integer.bitCount(mask);
        total += (diffColorCnt * diffColorCnt);

        return new long[] {total, mask};
    }
}
