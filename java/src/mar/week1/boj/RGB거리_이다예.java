package mar.week1.boj;

import java.io.*;
import java.util.*;

public class RGB거리_이다예 {
    static BufferedReader br;
    static StringTokenizer st;
    static int N;
    static int result ;
    static List<Home> homes;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        N = Integer.parseInt(br.readLine());
        result = Integer.MAX_VALUE;
        homes = new ArrayList<>();

        for (int i =0 ; i < N ; i++){
            st= new StringTokenizer(br.readLine());
            int redCost= Integer.parseInt(st.nextToken());
            int greenCost= Integer.parseInt(st.nextToken());
            int blueCost= Integer.parseInt(st.nextToken());

            homes.add(new Home(redCost,greenCost,blueCost));
        }

        dfs(0,0,-1);
        System.out.println(result);
    }

    static void dfs(int now,int cost,int choice){
        if(cost > result) return;
        if(now >= homes.size()) {
            if(cost < result) result = cost;
            return;
        }

        Home nowHome = homes.get(now);

        for(int i = 0 ; i < 3 ; i++){
            if(i==0 && choice != 0){
                cost += nowHome.red;
                dfs(now+1,cost,0);
                cost -= nowHome.red;
            }
            if(i==1 && choice != 1){
                cost += nowHome.green;
                dfs(now+1,cost,1);
                cost -= nowHome.green;
            }
            if(i==2 && choice != 2){
                cost += nowHome.blue;
                dfs(now+1,cost,2);
                cost -= nowHome.blue;
            }
        }


    }

    static class Home{
        int red;
        int green;
        int blue;

        Home(int red, int green, int blue){
           this.red = red;
           this.green = green;
           this.blue = blue;
        }
    }
}
