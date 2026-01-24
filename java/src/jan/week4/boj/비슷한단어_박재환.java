package jan.week4.boj;

import java.util.*;
import java.io.*;

public class 비슷한단어_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    /**
     * N 개의 영단어 중 가장 비슷한 두 단어를 구한다.
     * 두 단어의 비슷한 정도는 두 단어의 접두사의 길이로 측정한다.
     *
     * 접두사의 길이가 최대인 경우가 여러 개일 때에는 입력되는 순서가 빠른 단어를 정답으로한다.
     */
    static int maxPrefixLen, ans1, ans2;
    static void init(BufferedReader br) throws IOException {
        maxPrefixLen = 0;
        ans1 = ans2 = -1;
        Trie trie = new Trie();

        int tc = Integer.parseInt(br.readLine().trim());
        String[] words = new String[tc];
        for(int i=0; i<tc; i++) {
            String word = br.readLine().trim();
            words[i] = word;
            trie.insert(word, i);
        }

        findMaxPrefixWordCombo(trie.rootNode, 0);
        System.out.println(words[ans1]);
        System.out.println(words[ans2]);
    }

    static void findMaxPrefixWordCombo(Node node, int depth) {
        if(node.pass > 1) {     // 두 단어 이상 해당 노드를 지나갔다면

            if(maxPrefixLen < depth) {
                maxPrefixLen = depth;
                ans1 = node.first;
                ans2 = node.second;
            } else if(maxPrefixLen == depth &&
                    (node.first < ans1 ||
                            (node.first == ans1 && node.second < ans2))) {
                ans1 = node.first;
                ans2 = node.second;
            }
        }

        for(Node child : node.childs.values()) {
            findMaxPrefixWordCombo(child, depth+1);
        }
    }

    static class Trie {
        Node rootNode;

        Trie() {
            this.rootNode = new Node();
        }

        void insert(String word, int idx) {
            Node node = this.rootNode;

            for(char c : word.toCharArray()) {
                node = node.childs.computeIfAbsent(c, k -> new Node());
                node.pass++;

                if(node.first == -1) node.first = idx;
                else if(node.second == -1) node.second = idx;
            }
        }
    }
    static class Node {
        Map<Character, Node> childs;
        int pass;       // 몇 번 지나갔는지
        int first;      // 첫 번째로 지나간 단어 인덱스
        int second;     // 두 번째로 지나간 단어 인덱스
        Node() {
            this.childs = new HashMap<>();
            this.pass = 0;
            this.first = -1;
            this.second = -1;
        }
    }
}
