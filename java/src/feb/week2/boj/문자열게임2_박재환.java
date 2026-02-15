package feb.week2.boj;

import java.util.*;
import java.io.*;

public class 문자열게임2_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }
    static void init() throws IOException {
        int tc = Integer.parseInt(br.readLine().trim());
        while(tc-- > 0) {
            String word = br.readLine().trim();
            int target = Integer.parseInt(br.readLine().trim());

            int minLen = word.length()+1;
            int maxLen = -1;

            List<Integer>[] alpha = new List[26];
            for(int i=0; i<26; i++) alpha[i] = new ArrayList<>();

            for(int i=0; i<word.length(); i++) {
                char c = word.charAt(i);
                alpha[c-'a'].add(i);            // 해당 문자의 인덱스 위치 기억
            }

            /**
             * 각 알파벳을 확인하며 구간 갱신
             */
            for(List<Integer> list : alpha) {
                for(int i=0; i<=list.size()-target; i++) {
                    int s = list.get(i);
                    int e = list.get(i+target-1);
                    int len = e-s+1;

                    minLen = Math.min(minLen, len);
                    maxLen = Math.max(maxLen, len);
                }
            }

            if(minLen == word.length()-1 || maxLen == -1) sb.append(-1);
            else sb.append(minLen).append(' ').append(maxLen);
            sb.append('\n');
        }
    }
}
