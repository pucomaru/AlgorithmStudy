package cs.정렬;

import java.util.*;

public class 삽입정렬_박재환 {
    /**
     * [삽입정렬]
     * 데이터 집합을 순회하며, 정렬이 필요한 요소를 뽑아 이를 다시 적당한 곳으로 삽입하는 알고리즘
     * => 이미 정렬된 영역에 원소 삽입
     * 시간복잡도 : 평균 O(n**2), 최고 O(n)
     * 공간복잡도 : O(1)
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("배열의 크기 : ");
        int n = sc.nextInt();
        int[] arr = new int[n];

        System.out.print("공백을 기준으로 배열에 들어갈 값을 입력 : ");
        for (int i = 0; i < n; i++) {
            arr[i] = sc.nextInt();
        }

        System.out.printf("[초기배열] : %s%n", Arrays.toString(arr));
        insertSort(arr, n);
        System.out.printf("[최종배열] : %s%n", Arrays.toString(arr));
    }

    static void insertSort(int[] arr, int n) {
        // arr[0]은 이미 정렬된 구간이라고 보고,
        // 두 번째 원소부터 앞쪽 정렬 구간에 끼워 넣는다.
        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;

            System.out.printf("%n[STEP %d] 삽입할 값 : %d%n", i, key);
            System.out.printf("정렬된 구간 : %s | 비교 시작 인덱스 : %d%n",
                    Arrays.toString(Arrays.copyOfRange(arr, 0, i)), j);

            // key보다 큰 값은 한 칸씩 뒤로 밀어서
            // key가 들어갈 자리를 만든다.
            while (j >= 0 && arr[j] > key) {
                System.out.printf("arr[%d]=%d > %d 이므로 오른쪽으로 이동%n", j, arr[j], key);
                arr[j + 1] = arr[j];
                j--;
                System.out.printf("이동 후 배열 : %s%n", Arrays.toString(arr));
            }

            // 비워진 자리에 key를 삽입한다.
            arr[j + 1] = key;
            System.out.printf("arr[%d] 위치에 %d 삽입%n", j + 1, key);
            System.out.printf("[정렬 결과] : %s%n", Arrays.toString(arr));
        }
    }
}
