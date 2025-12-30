package jan.week1.boj;

import java.util.*;
import java.io.*;

public class LCS2_박재환 {
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
        String str1 = br.readLine().trim();
        String str2 = br.readLine().trim();

        if(str1.length() > str2.length()) lcs(str1, str2);
        else lcs(str2, str1);
    }

    static void lcs(String maxStr, String minStr) {
        int maxLen = maxStr.length();
        int minLen = minStr.length();

        int[][] arr = new int[maxLen+1][minLen+1];

        for(int i=1; i<maxLen+1; i++) {
            for(int j=1; j<minLen+1; j++) {
                if(maxStr.charAt(i-1) == minStr.charAt(j-1)) arr[i][j] = arr[i-1][j-1] + 1;
                else arr[i][j] = Math.max(arr[i-1][j], arr[i][j-1]);
            }
        }
        sb.append(arr[maxLen][minLen]).append('\n');
        findLcsStr(maxStr, minStr, arr);
    }

    /**
     * LCS 역추적
     * str[i] == str[j] => i-1, j-1 로 이동
     * 아니라면,왔던 위치로 이동
     */
    static void findLcsStr(String str1, String str2, int[][] arr) {
        StringBuilder lcs = new StringBuilder();

        int i = str1.length(), j = str2.length();

        while(i > 0 && j > 0) {
            if(str1.charAt(i-1) == str2.charAt(j-1)) {
                lcs.append(str1.charAt(i-1));
                i--; j--;
            } else {
                if(arr[i-1][j] > arr[i][j-1]) i--;
                else j--;
            }
        }

        sb.append(lcs.reverse()).append('\n');
    }
}
