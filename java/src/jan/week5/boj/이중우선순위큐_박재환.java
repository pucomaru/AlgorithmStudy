package jan.week5.boj;

import java.util.*;
import java.io.*;

public class 이중우선순위큐_박재환  {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        init(br, sb);
        br.close();
        System.out.println(sb);
    }
    static void init(BufferedReader br, StringBuilder sb) throws IOException {
        StringTokenizer st;
        int tc = Integer.parseInt(br.readLine().trim());

        while(tc-- > 0) {
            int input = Integer.parseInt(br.readLine().trim());
            Map<Integer, Integer> map = new HashMap<>();        // 동일한 정수가 삽입 될 수 있으므로
            TreeSet<Integer> set = new TreeSet<>((a, b) -> Integer.compare(b, a));
            while(input-- > 0) {
                st = new StringTokenizer(br.readLine().trim());
                String command = st.nextToken();
                int num = Integer.parseInt(st.nextToken());
                switch(command) {
                    case "I": {
                        map.merge(num, 1, Integer::sum);
                        set.add(num);
                        break;
                    }
                    case "D": {
                        if(set.isEmpty()) break;
                        if(num == 1) {
                            int target = set.first();
                            int cnt = map.get(target);
                            if(cnt > 1) map.put(target, cnt-1);
                            else {
                                map.remove(target);
                                set.remove(target);
                            }
                        }
                        else if(num == -1) {
                            int target = set.last();
                            int cnt = map.get(target);
                            if(cnt > 1) map.put(target, cnt-1);
                            else {
                                map.remove(target);
                                set.remove(target);
                            }
                        }
                        break;
                    }
                }
            }
            if(set.isEmpty()) sb.append("EMPTY");
            else {
                sb.append(set.first()).append(' ').append(set.last());
            }
            sb.append('\n');
        }
    }
}
