package cs.정렬;

import java.util.Arrays;
import java.util.Scanner;

public class 퀵정렬_박재환 {
    /**
     * [퀵정렬]
     * 데이터 집합 내 기준(Pivot)값을 정하고, Pivot을 기준으로 두 부분 집합으로 나눈다.
     * 한쪽 부분에는 Pivot보다 작은 값, 다른 쪽에는 Pivot보다 큰 값이 오도록 분할한다.
     * 이 과정을 재귀적으로 반복해 정렬한다.
     * 평균 시간복잡도 : O(n log n)
     * 최악 시간복잡도 : O(n**2)
     * 공간복잡도 : 재귀 호출에 따라 O(log n) ~ O(n)
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
        quickSort(arr, 0, n - 1);
        System.out.printf("[최종배열] : %s%n", Arrays.toString(arr));
    }

    static void quickSort(int[] arr, int left, int right) {
        // 원소가 1개 이하인 구간은 이미 정렬된 상태다.
        if (left >= right) {
            return;
        }

        int l = left;
        int r = right;
        int pivotIndex = (left + right) / 2;
        int pivot = arr[pivotIndex];

        System.out.printf("%n[PARTITION] arr[%d..%d] = %s%n",
                left, right, Arrays.toString(Arrays.copyOfRange(arr, left, right + 1)));
        System.out.printf("Pivot 선택 : arr[%d]=%d%n", pivotIndex, pivot);

        // 왼쪽 포인터는 pivot보다 작은 값이 있는 동안 오른쪽으로 이동,
        // 오른쪽 포인터는 pivot보다 큰 값이 있는 동안 왼쪽으로 이동한다.
        while (l <= r) {
            while (arr[l] < pivot) {
                System.out.printf("arr[%d]=%d < pivot(%d) 이므로 l 이동%n", l, arr[l], pivot);
                l++;
            }

            while (arr[r] > pivot) {
                System.out.printf("arr[%d]=%d > pivot(%d) 이므로 r 이동%n", r, arr[r], pivot);
                r--;
            }

            // 교차하지 않았다면 잘못된 편에 있는 두 값을 교환한다.
            if (l <= r) {
                System.out.printf("arr[%d]=%d 와 arr[%d]=%d 교환%n", l, arr[l], r, arr[r]);
                swap(arr, l, r);
                System.out.printf("교환 후 배열 : %s%n", Arrays.toString(arr));
                l++;
                r--;
            }
        }

        System.out.printf("분할 완료 -> 왼쪽 구간 arr[%d..%d], 오른쪽 구간 arr[%d..%d]%n",
                left, r, l, right);

        quickSort(arr, left, r);
        quickSort(arr, l, right);
    }

    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
