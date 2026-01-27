package jan.week5.boj;

import java.util.*;
import java.io.*;

public class 가르침_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }

    /**
     * K 개의 글자를 가르칠 시간밖에 없다.
     * 학생들은 K 개의 글자로만 이루어진 단어만을 읽을 수 있다.
     *
     * 어떤 K 개의 글자를 가르쳐야 학생들이 읽을 수 있는 단어의 개수가 최대가 되는지
     * 남극언어의 모든 단어는 'anta' 로 시작 'tica' 로 끝난다.
     */
    static int[] wordsBits;
    static int maxWord;
    static int characterCount;
    static void init(BufferedReader br) throws IOException {
        maxWord = 0;
        StringTokenizer st;
        /**
         * 모든 단어는
         * anta____tica 의 구조
         *
         * 알파벳은 총 26 개 => 소문자로만 주어짐
         */
        makeDefaultBits();
        st = new StringTokenizer(br.readLine().trim());
        int wordCount = Integer.parseInt(st.nextToken());
        characterCount = Integer.parseInt(st.nextToken());

        if(characterCount < 5) {
            System.out.println(0);
            return;
        }
        if(characterCount == 26) {
            System.out.println(wordCount);
            return;
        }
        String[] words = new String[wordCount];
        wordsBits = new int[wordCount];
        for(int i=0; i<wordCount; i++) {
            words[i] = br.readLine().trim();
            int bits = 0;
            for(char c : words[i].toCharArray()) {
                bits |= 1 << (c - 'a');
            }
            wordsBits[i] = bits;
        }
        teachCharacter(0, 0, 0);
        System.out.println(maxWord);
    }

    static int defaultBits;
    static void makeDefaultBits() {
        defaultBits = 0;
        String prefix = "anta";
        String suffix = "tica";

        for(char c : prefix.toCharArray()) {
            defaultBits |= 1 << (c - 'a');
        }

        for(char c : suffix.toCharArray()) {
            defaultBits |= 1 << (c - 'a');
        }
    }

    static void teachCharacter(int id, int selectedId, int alphabets) {
        if(selectedId == characterCount - 5) {
            int canRead = 0;
            int bits = alphabets | defaultBits;
            for(int wordBit : wordsBits) {
                if((bits & wordBit) == wordBit) canRead++;
            }
            maxWord = Math.max(maxWord, canRead);
            return;
        }
        for(int i=id; i<26; i++) {
            // 이미 기본 문자열에 포함되어 있다면
            if((defaultBits & (1 << i)) != 0) continue;
            teachCharacter(i+1, selectedId+1, (alphabets | (1 << i)));
        }
    }
}
