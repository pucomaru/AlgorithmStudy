package jan.week3.boj;

import java.util.*;
import java.io.*;

public class 호텔_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    static final int INF = 100*1000+1;
    static final int SEARCH_RANGE = 2001;
    static StringTokenizer st;
    static int leastPerson, cityCount;
    static City[] cities;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        leastPerson = Integer.parseInt(st.nextToken());
        cityCount = Integer.parseInt(st.nextToken());
        cities = new City[cityCount];
        for(int cityIdx=0; cityIdx<cityCount; cityIdx++) {
            st = new StringTokenizer(br.readLine().trim());
            int cost = Integer.parseInt(st.nextToken());
            int person = Integer.parseInt(st.nextToken());
            cities[cityIdx] = new City(cost, person);
        }
        System.out.println(findMinCost());
    }

    static int findMinCost() {
        int[] costPerPerson = new int[SEARCH_RANGE];       // 목표 인원을 기준으로 배낭 채우기
        Arrays.fill(costPerPerson, INF);
        costPerPerson[0] = 0;                               // 0명을 모으는 데에는 0원이 듬

        for(City city : cities) {
            for(int person=city.person; person<SEARCH_RANGE; person++) {
                costPerPerson[person] = Math.min(costPerPerson[person], costPerPerson[person-city.person] + city.cost);
            }
        }


        return findMinCostForLeastPerson(costPerPerson);
    }

    static int findMinCostForLeastPerson(int[] costPerPerson) {
        int minCost = INF;
        for(int i=leastPerson; i<SEARCH_RANGE; i++) {
            minCost = Math.min(minCost, costPerPerson[i]);
        }
        return minCost;
    }

    static class City {
        int cost;
        int person;

        public City(int cost, int person) {
            this.cost = cost;
            this.person = person;
        }
    }
}
