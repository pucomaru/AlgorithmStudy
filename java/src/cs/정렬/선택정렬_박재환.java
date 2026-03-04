package cs.정렬;

import java.util.*;

public class 선택정렬_박재환 {
    /**
     * [선택정렬]
     * 선택된 값과 나머지 데이터 중 비교해 알맞은 자리를 찾는 알고리즘
     * => 매번 가장 작은 값을 찾아서 앞에 배치
     * 시간 복잡도 : O(n**2)
     * 공간 복잡도 : O(1)
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
        selectSort(arr, n);
        System.out.printf("[최종배열] : %s%n", Arrays.toString(arr));
    }

    static void selectSort(int[] arr, int n) {
        // i번째 자리에 들어갈 최솟값을
        // i ~ n-1 구간에서 찾아 교환한다.
        for (int i = 0; i < n - 1; i++) {
            int min = i;

            System.out.printf("%n[STEP %d] %d번 인덱스에 들어갈 최솟값 찾기%n", i + 1, i);
            System.out.printf("현재 기준값 : arr[%d]=%d%n", min, arr[min]);

            for (int j = i + 1; j < n; j++) {
                System.out.printf("arr[%d]=%d 와 현재 최솟값 arr[%d]=%d 비교%n",
                        j, arr[j], min, arr[min]);

                if (arr[j] < arr[min]) {
                    min = j;
                    System.out.printf("-> 새로운 최솟값 갱신: arr[%d]=%d%n", min, arr[min]);
                }
            }

            if (min != i) {
                System.out.printf("arr[%d]=%d 와 arr[%d]=%d 교환%n",
                        i, arr[i], min, arr[min]);
                swap(arr, i, min);
            } else {
                System.out.println("이미 제자리이므로 교환 생략");
            }

            System.out.printf("[정렬 결과] : %s%n", Arrays.toString(arr));
        }
    }

    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
