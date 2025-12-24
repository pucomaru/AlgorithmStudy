package dec_4.boj;

import java.util.*;
import java.io.*;

public class 장난감섞기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * N개의 수열이 있다.
     * 각 수열의 길이는 M으로 동일하다.
     * 수열끼리의 순서만 마음대로 배치할 수 있다.
     */
    static StringTokenizer st;
    static int n, m;
    static Sequence[] seqArr;
    static void init() throws IOException {
        st = new StringTokenizer(br.readLine().trim());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());

        seqArr = new Sequence[n];
        for(int i=0; i<n ;i++) {
            st = new StringTokenizer(br.readLine().trim());
            int[] arr = new int[m];
            for(int j=0; j<m; j++) arr[j] = Integer.parseInt(st.nextToken());
            seqArr[i] = new Sequence(arr);
        }

        int result = Math.max(getMaxSubArr(), getSequenceMaxSum());
        System.out.println(result);
    }

    static int getMaxSubArr() {
        int max = 0;
        for(Sequence s : seqArr) max = Math.max(max, s.subArr);
        return max;
    }

    static int getSequenceMaxSum() {
        int max = 0;
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(i==j) continue;
                int sum = seqArr[i].suffix + seqArr[j].prefix;
                for(int z=0; z<n; z++) {
                    if(z==i || z==j) continue;
                    if(seqArr[z].total < 0) continue;
                    sum += seqArr[z].total;
                }
                max = Math.max(max, sum);
            }
        }
        return max;
    }

    static class Sequence {
        int prefix;
        int suffix;
        int total;
        int subArr;

        public Sequence(int[] arr) {
            this.setPrefix(arr);
            this.setSuffix(arr);
            this.setSubArr(arr);
        }

        private void setPrefix(int[] arr) {
            int sum = 0;
            int max = sum;
            for(int i=0; i<m; i++) {
                sum += arr[i];
                max = Math.max(sum, max);
            }
            this.prefix = max;
            this.total = sum;
        }

        private void setSuffix(int[] arr) {
            int sum = 0;
            int max = sum;
            for(int i=m-1; i>-1; i--) {
                sum += arr[i];
                max = Math.max(sum, max);
            }
            this.suffix = max;
        }

        private void setSubArr(int[] arr) {
            int localMax = 0;
            int globalMax = localMax;
            for(int i=0; i<m; i++) {
                localMax = Math.max(0, arr[i] + localMax);
                globalMax = Math.max(globalMax, localMax);
            }
            this.subArr = globalMax;
        }
    }
}
