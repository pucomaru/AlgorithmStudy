package feb.week3.boj;

import java.io.*;
import java.util.*;
public class 바이러스_이다예 {
    static BufferedReader br;
    static StringTokenizer st;

    static int computerCount,linkCount;
    static int[] parent;

    // Union-find로 접근하면 될 것 같다.
    // 연결 된 컴퓨터 쌍을 돌면서 두 컴퓨터중 번호가 작은 컴퓨터를 선택해 엮은 다음
    // 바이러스 걸린 컴퓨터와 같은 부모 번호를 가진 컴퓨터들을 count 하면 답이 나온다
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));

        computerCount = Integer.parseInt(br.readLine());
        linkCount = Integer.parseInt(br.readLine());
        parent = new int[computerCount+1];

        // 부모 배열 본인 번호로 초기화
        for (int i = 1 ; i < computerCount+1; i++){
            parent[i] = i;
        }

        // 연결된 컴퓨터들을 돌면서 작은 번호인 컴퓨터를 부모로 선정
        for(int i = 0 ; i < linkCount; i++){
            st = new StringTokenizer(br.readLine());
            int a = Integer.parseInt(st.nextToken());
            int b= Integer.parseInt(st.nextToken());

            if (find(a) != find(b)) {
                union(a,b);
            }
        }

        int result = 0 ;
        for (int i = 2; i < computerCount+1; i++){
            if (find(i) == 1) result++;
        }

        System.out.println(result);
    }

    static void union(int a, int b){
        if(find(a) > find(b)) parent[find(a)] = find(b);
        else parent[find(b)] = find(a);
    }

    static int find(int a){
        if (parent[a] != a){
            return find(parent[a]);
        }
        return a;
    }

}
