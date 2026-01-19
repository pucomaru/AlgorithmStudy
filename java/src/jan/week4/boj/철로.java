package jan.week4;

import java.util.*;
import java.io.*;

public class 철로 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    static StringTokenizer st;
    static int personCount;
    static int railLength;
    static void init() throws IOException {
        personCount = Integer.parseInt(br.readLine().trim());
        Person[] persons = new Person[personCount];
        for(int i=0; i<personCount; i++) {
            st = new StringTokenizer(br.readLine());
            // 시작, 끝 위치를 정해야함
            int a = Integer.parseInt(st.nextToken());
            int b = Integer.parseInt(st.nextToken());
            persons[i] = new Person(a, b);
        }
        railLength = Integer.parseInt(br.readLine().trim());

        System.out.println(getMaxIncludePersons(persons));
    }

    static class Person {

        int start;
        int end;

        Person(int a, int b) {
            this.start = Math.min(a, b);
            this.end = Math.max(a, b);
        }
    }

    static List<Person> personList;
    static int getMaxIncludePersons(Person[] persons) {
        int maxIncludePersons = 0;
        personList = new ArrayList<>();
        // 가장 먼저 철로의 길이에 절대 포함될 수 없는 사람들을 제거
        for(Person person : persons) {
            if(person.end - person.start > railLength) continue;
            personList.add(person);
        }

        // 철도의 끝을 기점으로, 슬라이딩 윈도우 형식으로 탐색
        personList.sort((a, b) -> Integer.compare(a.end, b.end));

        // 철도 내에 포함되는 사람의 최대 수를 저장
        PriorityQueue<Person> pq = new PriorityQueue<>((a, b) -> Integer.compare(a.start, b.start));

        // 철도의 끝을 end 기준으로 계산
        for (Person person : personList) {          // 가장 큰 end 를 가지고 있는 사람부터

            int end = person.end;
            int start = end - railLength;

            // 현재 기준이 되는 사람은 무조건 포함
            pq.offer(person);

            while (!pq.isEmpty() && pq.peek().start < start) {
                pq.poll();
            }

            maxIncludePersons = Math.max(maxIncludePersons, pq.size());
        }

        return maxIncludePersons;
    }
}
