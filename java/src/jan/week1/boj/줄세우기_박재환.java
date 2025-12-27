package jan.week1.boj;

import java.util.*;
import java.io.*;

public class 줄세우기_박재환 {
    static BufferedReader br;
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        init();
        br.close();
    }

    /**
     * N명의 아이들이 있다. (1~N번)
     * 번호 순으로 일렬로 서서 걸어가도록 했다.
     *
     * 아이들이 임의로 줄을 서 있을 때, 아이들을 최소한으로 옮겨서 반호순으로 세우고 싶다.
     * => 정렬 문제
     * => 고정시킬 수 있는 위치의 아이들은 현 위치에 고정시킨다.
     * => LIS 를 활용해서, 움직이지 않아도 되는 아이들을 구한다.
     */
    static int childCount;
    static List<Integer> childrenList;
    static void init() throws IOException {
        childrenList = new ArrayList<>();
        childCount = Integer.parseInt(br.readLine().trim());
        int lisLength = 1;
        // 1. 첫번째 학생을 바로 줄 세움
        childrenList.add(Integer.parseInt(br.readLine().trim()));
        for(int i=1; i<childCount; i++) {
            int newChild = Integer.parseInt(br.readLine());
            if(childrenList.get(childrenList.size()-1) <= newChild) {   // 현재 위치에 고정 시킬 수 있는 아이의 경우
                childrenList.add(newChild);
                continue;
            }

            int insertIdx = findInsertIdx(newChild);
            childrenList.set(insertIdx, newChild);
        }
//        System.out.println(childrenList);
        System.out.println(childCount-childrenList.size());
    }

    static int findInsertIdx(int target) {
        int l = 0, r = childrenList.size()-1;

        while(l < r) {
            int mid = l + (r-l)/2;
            if(childrenList.get(mid) >= target) {
                 r = mid;
            } else {
                l = mid+1;
            }
        }
        return l;
    }
}
