package cs.정렬;

import java.util.Arrays;
import java.util.Scanner;

public class 합병정렬_박재환 {
    /**
     * [합병정렬]
     * 배열을 둘 이상의 부분집합으로 나눈 뒤,
     * 각 부분집합을 정렬하고 다시 정렬된 형태로 합친다.
     * Divide And Conquer(분할 정복) 방식의 대표적인 정렬이다.
     * 시간복잡도 : O(n log n)
     * 공간복잡도 : O(n)
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
        mergeSort(arr, n);
        System.out.printf("[최종배열] : %s%n", Arrays.toString(arr));
    }

    static void mergeSort(int[] arr, int n) {
        if (n <= 1) {
            return;
        }

        int[] temp = new int[n];
        mergeSort(arr, temp, 0, n - 1);
    }

    static void mergeSort(int[] arr, int[] temp, int l, int r) {
        // 원소가 1개면 이미 정렬된 상태이므로 더 이상 나누지 않는다.
        if (l >= r) {
            return;
        }

        int mid = l + (r - l) / 2;

        System.out.printf("%n[DIVIDE] arr[%d..%d] -> arr[%d..%d], arr[%d..%d]%n",
                l, r, l, mid, mid + 1, r);

        mergeSort(arr, temp, l, mid);
        mergeSort(arr, temp, mid + 1, r);

        merge(arr, temp, l, mid, r);
    }

    static void merge(int[] arr, int[] temp, int l, int mid, int r) {
        // 병합 전 현재 구간을 임시 배열에 복사한다.
        for (int idx = l; idx <= r; idx++) {
            temp[idx] = arr[idx];
        }

        System.out.printf("[MERGE] 왼쪽 %s + 오른쪽 %s%n",
                Arrays.toString(Arrays.copyOfRange(temp, l, mid + 1)),
                Arrays.toString(Arrays.copyOfRange(temp, mid + 1, r + 1)));

        int i = l;       // 왼쪽 부분배열 시작점
        int j = mid + 1; // 오른쪽 부분배열 시작점
        int k = l;       // 원본 배열에 채워 넣을 위치

        // 두 부분배열의 앞 원소를 비교해 더 작은 값을 먼저 넣는다.
        while (i <= mid && j <= r) {
            if (temp[i] <= temp[j]) {
                System.out.printf("temp[%d]=%d <= temp[%d]=%d 이므로 왼쪽 값 선택%n",
                        i, temp[i], j, temp[j]);
                arr[k++] = temp[i++];
            } else {
                System.out.printf("temp[%d]=%d > temp[%d]=%d 이므로 오른쪽 값 선택%n",
                        i, temp[i], j, temp[j]);
                arr[k++] = temp[j++];
            }
        }

        // 왼쪽에 남은 값이 있으면 순서대로 뒤에 붙인다.
        while (i <= mid) {
            System.out.printf("왼쪽 잔여 값 temp[%d]=%d 복사%n", i, temp[i]);
            arr[k++] = temp[i++];
        }

        // 오른쪽에 남은 값도 순서대로 뒤에 붙인다.
        while (j <= r) {
            System.out.printf("오른쪽 잔여 값 temp[%d]=%d 복사%n", j, temp[j]);
            arr[k++] = temp[j++];
        }

        System.out.printf("[MERGE 결과] arr[%d..%d] = %s%n",
                l, r, Arrays.toString(Arrays.copyOfRange(arr, l, r + 1)));
    }
}
