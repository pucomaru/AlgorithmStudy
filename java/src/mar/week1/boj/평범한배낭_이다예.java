package mar.week1.boj;

import java.io.*;
import java.util.*;

public class 평범한배낭_이다예 {

    static BufferedReader br;
    static StringTokenizer st;
    static int N;
    static int K;
    static List<Product> products ;
    static int[] weight;
    static int result;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        st = new StringTokenizer(br.readLine());

        // N = 물품의 수
        N = Integer.parseInt(st.nextToken());
        // K = 준서가 버틸 수 있는 무게
        K = Integer.parseInt(st.nextToken());

        products = new ArrayList<>();

        // 물건의 수 만큼 물건 저장
        for(int i = 0 ; i < N ; i++){
            st = new StringTokenizer(br.readLine());
            // w : 물건의 무게
            int w = Integer.parseInt(st.nextToken());
            // v : 물건의 가치
            int v = Integer.parseInt(st.nextToken());

            products.add(new Product(w,v));
        }
        products.sort((a,b) -> a.weight- b.weight);
        result= 0;
        dp(0,0,0);
        System.out.println(result);
    }

    static void dp(int now,int w,int v){
        if(result < v) result =v;
        // 상품 갯수 넘기면 끝
        if(now >= products.size()) return;

        for(int i = 0 ; i < 2 ; i++){
            // i 가 0일 경우에는 now를 담음
            if(i == 0){
                // 현재 순서의 물건
                Product nowProduct = products.get(now);
                int nowWeight = nowProduct.weight + w;
                int nowValue = nowProduct.value + v;

                // 지금까지 담은 무게가 K를 넘을 경우 그만
                if(nowWeight > K) continue;


                dp(now+1,nowWeight,nowValue);
            }
            // i가 1일 경우에는 now를 담지않음
            if(i == 1){
                dp(now+1,w,v);
            }
        }
    }

    // 물건
    static class Product{
        int weight;
        int value;

        Product(int weight, int value){
            this.weight = weight;
            this.value = value;
        }
    }

    //
}
