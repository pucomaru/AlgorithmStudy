package jan.week4.boj;

import java.util.*;
import java.io.*;

public class 게임닉네임_박재환 {
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init(br);
        br.close();
        System.out.println(sb);
    }

    /**
     * 각 유저는 닉네임을 정한다.
     * 알파벳 소문자로만 이루어져있다. (중복 닉네임이 가능하다.)
     *
     * 닉네임을 이용해 내부에 저장할 별칭을 만든다.
     * - 유저 닉네임의 접두사(Prefix) 중 가장 짧은 것을 사용한다.
     * - 접두사가 이전에 가입한 닉네임의 접두사가 아니여야한다.
     * - 가능한 별칭이 없는 경우에는, 같은 닉네임의 수를 계산한다.
     *      - 같은 닉네임이 없다면 닉네임을 별칭으로 사용한다.
     *      - 두 개 이상 있다면 닉네임 뒤에 개수를 붙인다.
     */
    static void init(BufferedReader br) throws IOException {
        int n = Integer.parseInt(br.readLine());
        Map<String, Integer> nicknames = new HashMap<>();
        Trie trie = new Trie();
        while(n-- > 0) {
            String nickname = br.readLine().trim();

            if(nicknames.containsKey(nickname)) {
                nicknames.put(nickname, nicknames.get(nickname)+1);
                sb.append(nickname).append(nicknames.get(nickname));
            } else {
                nicknames.put(nickname, 1);
                sb.append(trie.insert(nickname));
            }
            sb.append('\n');
        }
    }

    static class Trie {
        Node rootNode;

        Trie() {
            this.rootNode = new Node();
        }

        String insert(String nickname) {
            StringBuilder alias = new StringBuilder();
            Node node = this.rootNode;
            boolean unique = false;
            for(char c : nickname.toCharArray()) {
                if(!unique) alias.append(c);
                if(!node.childs.containsKey(c)) {
                    unique = true;
                }
                node = node.childs.computeIfAbsent(c, k -> new Node());
            }
            return alias.toString();
        }
    }

    static class Node {
        Map<Character, Node> childs;

        Node() {
            this.childs = new HashMap<>();
        }
    }
}
