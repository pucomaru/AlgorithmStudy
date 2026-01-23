package jan.week4.boj;

import java.util.*;
import java.io.*;

public class 문제추천시스템Version1_박재환 {
    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        init(br, sb);
        br.close();
        System.out.println(sb);
    }
    static StringTokenizer st;
    static TreeSet<Problem> problems;
    static Map<Integer, Problem> problemMap;
    static void init(BufferedReader br, StringBuilder sb) throws IOException {
        problemMap = new HashMap<>();
        problems = new TreeSet<>((a, b) -> {
            if(a.hard == b.hard) return Integer.compare(b.id, a.id);
            return Integer.compare(b.hard, a.hard);
        });

        int originCount = Integer.parseInt(br.readLine().trim());
        while(originCount-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            int id = Integer.parseInt(st.nextToken());
            int hard = Integer.parseInt(st.nextToken());
            Problem problem = new Problem(id, hard);
            problems.add(problem);
            problemMap.put(id, problem);
        }

        int commandCount = Integer.parseInt(br.readLine().trim());
        while(commandCount-- > 0) {
            st = new StringTokenizer(br.readLine().trim());
            String command = st.nextToken();
            switch(command) {
                case "add": {
                    int id = Integer.parseInt(st.nextToken());
                    int hard = Integer.parseInt(st.nextToken());
                    Problem problem = new Problem(id, hard);
                    problems.add(problem);
                    problemMap.put(id, problem);
                    break;
                }
                case "solved": {
                    int id = Integer.parseInt(st.nextToken());
                    Problem problem = findProblem(id);
                    problems.remove(problem);
                    break;
                }
                case "recommend": {
                    int x = Integer.parseInt(st.nextToken());
                    sb.append((x == 1 ? problems.first() : problems.last()).id).append('\n');
                    break;
                }
            }
        }
    }

    static Problem findProblem(int id) {
        return problemMap.get(id);
    }

    static class Problem {
        int id;
        int hard;

        Problem(int id, int hard) {
            this.id = id;
            this.hard = hard;
        }
    }
}
