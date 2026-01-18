package jan.week3.boj;

import java.util.*;
import java.io.*;

public class 개미굴_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }

    static StringTokenizer st;
    static void init() throws IOException {
        int inputCount = Integer.parseInt(br.readLine().trim());

        Trie trie = new Trie();
        while(inputCount-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int foodCount = Integer.parseInt(st.nextToken());
            String[] foods = new String[foodCount];
            for(int i=0; i<foodCount; i++) foods[i] = st.nextToken();
            trie.insert(foods);
        }
        trie.restorationAntNest(trie.rootNode, 0, sb);
    }

    static class Node {
        Map<String, Node> childs;
        boolean end;

        Node() {
            childs = new TreeMap<>(String::compareTo);      // 사전순 정렬
            end = false;
        }
    }

    static class Trie {
        // 루트 노드 생성
        Node rootNode = new Node();

        void insert(String[] foods) {
            Node node = this.rootNode;      // 루트 노드로부터, 순차적으로 탐색
            // 한 탐사 로봇이 탐색한 순서를 바탕으로 트리를 생성한다.
            for(String food : foods) {
                node = node.childs.computeIfAbsent(food, k -> new Node());
            }

            node.end = true;        // 마지막 노드 표시
        }

        /**
         * 현재 노드 (부모) => 자식 노드로 탐색
         * @param node
         * 현재 노드
         * @param depth
         * 현재 깊이
         */
        void restorationAntNest(Node node, int depth, StringBuilder sb) {
            for(Map.Entry<String, Node> childNodes : node.childs.entrySet()) {
                for(int i=0; i<depth; i++) sb.append("--");
                sb.append(childNodes.getKey()).append('\n');
                restorationAntNest(childNodes.getValue(), depth+1, sb);
            }
        }
    }
}
