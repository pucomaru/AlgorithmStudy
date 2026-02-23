def solution():
    import sys
    input = sys.stdin.readline

    n = int(input())
    arr = list(map(int, input().strip().split()))

    l = 0
    r = n-1

    sum_best = 2 * 1_000_000_000
    l_best = 0
    r_best = 0

    while l < r:
        sum = arr[l] + arr[r]

        if sum == 0:
            sum_best = sum
            l_best = arr[l]
            r_best = arr[r]
            break

        if sum > 0:
            if abs(sum) < abs(sum_best):
                sum_best = sum
                l_best = arr[l]
                r_best = arr[r]
            r-=1
        else:
            if abs(sum) < abs(sum_best):
                sum_best = sum
                l_best = arr[l]
                r_best = arr[r]
            l+=1
    print(l_best, r_best)

if __name__ == '__main__':
    solution()