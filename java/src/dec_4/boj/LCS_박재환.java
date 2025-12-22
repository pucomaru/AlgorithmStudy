package dec_4.boj;

import java.util.*;
import java.io.*;

public class LCS_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static String str1, str2;
    static void init() throws IOException {
        str1 = br.readLine().trim();
        str2 = br.readLine().trim();
        compareStrLen();
        int[][] arr = new int[max+1][min+1];

        for(int i=1; i<max+1; i++) {
            for(int j=1; j<min+1; j++) {
                if(maxString.charAt(i-1) == minString.charAt(j-1)) {
                    arr[i][j] = arr[i-1][j-1] + 1;
                } else {
                    arr[i][j] = Math.max(arr[i-1][j], arr[i][j-1]);
                }
            }
        }

        System.out.println(arr[max][min]);
    }

    static String maxString, minString;
    static int max, min;
    static void compareStrLen() {
        if(str1.length() >= str2.length()) {
            max = str1.length();
            maxString = str1;
            min = str2.length();
            minString = str2;
        } else {
            max = str2.length();
            maxString = str2;
            min = str1.length();
            minString = str1;
        }
    }
}
