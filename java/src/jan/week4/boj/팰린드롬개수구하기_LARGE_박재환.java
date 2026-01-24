package jan.week4.boj;

import java.util.*;
import java.io.*;

class 팰린드롬개수구하기_LARGE_박재환 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br);
        br.close();
    }
    static final int MOD = 10_007;
    static void init(BufferedReader br) throws IOException {
        String str = br.readLine().trim();      // 길이가 1000 이하
        System.out.println(subPalindrome(str));
    }

    static long subPalindrome(String str) {
        int strLen = str.length();
        long[][] palindrome = new long[strLen][strLen];
        for(int i=0; i<strLen; i++) palindrome[i][i] = 1;       // 자기 자신을 부분수열로 갖는 경우

        for(int len=2; len<=strLen; len++) {
            for(int i=0; i+len-1 < strLen; i++) {
                int j = i + len - 1;

                if (str.charAt(i) == str.charAt(j)) {
                    palindrome[i][j] = palindrome[i+1][j] + palindrome[i][j-1] + 1;
                } else {
                    palindrome[i][j] = palindrome[i+1][j] + palindrome[i][j-1] - palindrome[i+1][j-1];
                }

                palindrome[i][j] %= MOD;
                if (palindrome[i][j] < 0) palindrome[i][j] += MOD;
            }
        }
        return palindrome[0][strLen-1];
    }
}