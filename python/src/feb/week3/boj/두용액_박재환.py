def solution():
    import sys
    input = sys.stdin.readline

    n = int(input().strip())
    arr = list(map(int, input().split()))
    arr.sort()

    l = 0
    r = n-1

    best_min = float("inf")
    best_max = -float("inf")
    best_sum = float("inf")

    while l < r:
        a = arr[l]
        b = arr[r]
        abs_sum = abs(a + b)
        if(abs_sum < best_sum):
            best_min = a
            best_max = b
            best_sum = abs_sum

        if abs_sum == 0:
            break

        if a + b > 0:
            r-=1
        else:
            l+=1

    print(best_min, best_max)

if __name__ == '__main__':
    solution()