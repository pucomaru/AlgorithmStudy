package mar.week1.boj;

import java.util.*;
import java.io.*;

public class 나이순정렬_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static class Member {
        int age;
        String name;

        Member(int age, String name) { this.age = age; this.name = name; }
    }
    static StringTokenizer st;
    static int n;
    static Member[] members;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        members = new Member[n];
        for(int i=0; i<n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int age = Integer.parseInt(st.nextToken());
            String name = st.nextToken();
            Member member = new Member(age, name);
            members[i] = member;
        }
        insertSort();
        for(Member m : members) {
            System.out.printf("%d %s\n", m.age, m.name);
        }
    }
    static void insertSort() {
        for(int i=1; i<n; i++) {
            Member key = members[i];
            int j = i - 1;
            while(j >= 0 && key.age < members[j].age) {
                members[j+1] = members[j];
                j--;
            }
            members[j+1] = key;
        }
    }
}
