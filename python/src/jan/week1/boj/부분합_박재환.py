n, s = map(int, input().split())
arr = list(map(int, input().split()))

def solution():
    l = 0
    r = 0
    sub_sum = 0
    min_len = 100_001

    while r < n:
        sub_sum += arr[r]

        while sub_sum >= s:
            sub_sum -= arr[l]
            min_len = min(min_len, (r-l)+1)
            l+=1
        r+=1

    return min_len if min_len != 100_001 else 0

if __name__ == "__main__":
    print(solution())

