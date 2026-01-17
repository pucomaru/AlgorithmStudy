package jan.week3.boj;

import java.io.*;
import java.util.*;

public class AC_박재환 {
    static BufferedReader br;
    static StringBuilder sb;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        sb = new StringBuilder();
        init();
        br.close();
        System.out.println(sb);
    }

    /**
     * R 뒤집기와 D 버리기가 있다.
     * - R : 배열에 있는 수의 순서를 뒤집는 함수
     * - D : 첫 번째 수를 버리는 함수
     *      - 더 이상 버릴수 있는 수가 없다면 'ERROR' 반환
     */
    static final String ERROR = "error";
    static void init() throws IOException {
        int tc = Integer.parseInt(br.readLine().trim());

        while(tc-- > 0) {
            String command = br.readLine().trim();
            int arrLen = Integer.parseInt(br.readLine().trim());
            String arrStr = br.readLine().trim();
            boolean pollFirst = true;
            ArrayDeque<String> deque = makeArr(arrStr);

            boolean isOk = true;
            for(char c : command.toCharArray()) {
                switch(c) {
                    case 'R':
                        pollFirst = commandR(pollFirst);
                        break;
                    case 'D':
                        if(!commandD(deque, pollFirst)) {
                            sb.append(ERROR);
                            isOk = false;
                        }
                }

                if(!isOk) break;
            }
            if(isOk) sb.append(makeStr(deque, pollFirst));
            sb.append('\n');
        }
    }

    static ArrayDeque<String> makeArr(String arrStr) {
        arrStr = arrStr.substring(1, arrStr.length()-1).trim();
        ArrayDeque<String> deque = new ArrayDeque<>();

        if(arrStr.isEmpty()) return deque;

        for(String str : arrStr.split(",")) {
            deque.offer(str);
        }
        return deque;
    }

    /**
     * 배열을 거꾸로 뒤집는다.
     */
    static boolean commandR(boolean offerFirst) {
        return !offerFirst;
    }

    /**
     * 첫 번째 원소를 제거한다.
     */
    static boolean commandD(ArrayDeque<String> deque, boolean pollFirst) {
        if(deque.isEmpty()) return false;

        if(pollFirst) deque.pollFirst();
        else deque.pollLast();
        return true;
    }

    static String makeStr(ArrayDeque<String> deque, boolean pollFirst) {
        if(deque.isEmpty()) return "[]";

        StringBuilder arrStr = new StringBuilder();
        arrStr.append('[');
        while(!deque.isEmpty()) {
            arrStr.append(pollFirst ? deque.pollFirst() : deque.pollLast()).append(',');
        }
        arrStr.setCharAt(arrStr.length()-1, ']');
        return arrStr.toString();
    }
}
