package cs.정렬;

import java.util.Arrays;
import java.util.Scanner;

public class 버블정렬_박재환 {
    /**
     * [버블정렬]
     * 인접한 두 원소를 비교해서 큰 값을 뒤로 보내는 방식으로 정렬한다.
     * 한 번의 순회가 끝나면 가장 큰 값이 맨 뒤에 고정된다.
     * 시간복잡도 : O(n**2)
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
        bubbleSort(arr, n);
        System.out.printf("[최종배열] : %s%n", Arrays.toString(arr));
    }

    static void bubbleSort(int[] arr, int n) {
        // 바깥 반복문 1회전이 끝날 때마다
        // 가장 큰 값 하나가 뒤쪽에 확정된다.
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            System.out.printf("%n[PASS %d] %d번째 큰 값 고정 과정%n", i + 1, i + 1);

            // 아직 정렬이 확정되지 않은 구간만 비교한다.
            for (int j = 0; j < n - i - 1; j++) {
                System.out.printf("arr[%d]=%d 와 arr[%d]=%d 비교%n",
                        j, arr[j], j + 1, arr[j + 1]);

                if (arr[j] > arr[j + 1]) {
                    System.out.printf("-> %d > %d 이므로 자리 교환%n", arr[j], arr[j + 1]);
                    swap(arr, j, j + 1);
                    swapped = true;
                    System.out.printf("교환 후 배열 : %s%n", Arrays.toString(arr));
                } else {
                    System.out.println("-> 순서가 올바르므로 교환하지 않음");
                }
            }

            System.out.printf("[PASS 결과] : %s%n", Arrays.toString(arr));

            // 한 번도 교환이 없었다면 이미 정렬이 끝난 상태다.
            if (!swapped) {
                System.out.println("교환이 한 번도 없으므로 정렬 종료");
                break;
            }
        }
    }

    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
