package jan.week3.programmers;

import java.util.*;

public class 네트워크_박재환 {
    public static void main(String[] args) {
        int n = 3;
        int[][] computers = {{1,1,0},{1,1,0},{0,0,1}};

        Solution solution = new Solution();
        int result = solution.solution(n, computers);
        System.out.println(result);
    }
}

class Solution {
    public int solution(int n, int[][] computers) {
        int answer = 0;

        makeParents(n);
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(computers[i][j] == 1) union(i, j);
            }
        }

        Set<Integer> set = new HashSet<>();
        for(int i=0; i<n; i++) {
            set.add(find(i));
        }

        return set.size();
    }

    int[] parents;
    void makeParents(int n) {
        parents = new int[n];
        for(int i=0; i<n; i++) parents[i] = i;
    }

    int find(int a) {
        if(parents[a] == a) return a;

        return parents[a] = find(parents[a]);
    }

    void union(int a, int b) {
        int rootA = find(a);
        int rootB = find(b);

        if(rootA == rootB) return;

        parents[rootA] = rootB;
    }
}