package feb.week1;

import java.io.*;
import java.util.*;

// greedy
public class 회의실배정_이다예 {

    static BufferedReader br;
    static StringTokenizer st;
    static int N;
    static PriorityQueue<Conference> pq;

    public static void main(String[] args) throws IOException {
        // 끝나는 시간 오름차순
        pq = new PriorityQueue<>((a,b) -> {
            if(a.end == b.end) return a.start-b.start; // 끝나는 시간이 같으면 시작시간 빠른걸로 먼저
            else return a.end-b.end;
        });
        br = new BufferedReader(new InputStreamReader(System.in));

        // 회의의 수 ( 1 <= N <= 100,000)
        N = Integer.parseInt(br.readLine());

        for(int i = 0 ; i < N ; i++){
            st = new StringTokenizer(br.readLine());
            int start = Integer.parseInt(st.nextToken());
            int end = Integer.parseInt(st.nextToken());
            pq.add(new Conference(start,end));
        }

        int result = 1;
        Conference nowConference = pq.poll();
        while(!pq.isEmpty()){
            Conference nextConference = pq.poll();
            if (nowConference.end <= nextConference.start){
                nowConference = nextConference;
                result++;
            }
        }
        System.out.println(result);
    }



    static class Conference{
        int start;
        int end;

        Conference(int start, int end){
            this.start = start;
            this.end = end;
        }
    }
}
