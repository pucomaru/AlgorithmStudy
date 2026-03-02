package feb.week4.boj;

import java.util.*;
import java.io.*;

public class 강의실배정_박재환 {
    static BufferedReader br;
    public static void main(String[] argsa) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }
    static class Subject {
        int s, e;
        Subject(int s, int e) { this.s = s; this.e =  e; }
    }
    static StringTokenizer st;
    static int n;
    static Subject[] subjects;
    static void init() throws IOException {
        n = Integer.parseInt(br.readLine().trim());
        subjects = new Subject[n];
        for(int i=0; i<n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int s = Integer.parseInt(st.nextToken());
            int e = Integer.parseInt(st.nextToken());
            Subject subject = new Subject(s, e);
            subjects[i] = subject;
        }
        System.out.println(getMinClassRoom());
    }
    static class Room implements Comparable<Room>{
        int finish;

        Room(int finish) { this.finish = finish; }

        public int compareTo(Room o) {
            return Integer.compare(this.finish, o.finish);
        }
    }
    static int getMinClassRoom() {
        Arrays.sort(subjects, (a, b) -> {
            if(a.s == b.s) return Integer.compare(a.e, b.e);
            return Integer.compare(a.s, b.s);
        });     // 시작 시간이 빠른 강의 순으로 정렬, 종료 시간이 빠른 순으로 정렬
        PriorityQueue<Room> rooms = new PriorityQueue<>();                            // 각 강의장의 사용이 끝나는 시간 기록

        for(Subject subject : subjects) {
            if(rooms.isEmpty()) {
                rooms.offer(new Room(subject.e));
                continue;
            }
            // 사용중인 강의실과 바통터치가 가능한지
            if(rooms.peek().finish <= subject.s) {
                rooms.poll();
                rooms.offer(new Room(subject.e));
            }else {     // 터치가 불가능한 경우
                rooms.offer(new Room(subject.e));
            }
        }
        return rooms.size();
    }
}
