import sys
input = sys.stdin.readline

n, c = map(int, input().split())
arr = list(int(input()) for _ in range(n))
arr.sort()      # 오름차순 정렬

def solution():
    l = 1
    r = arr[-1]-arr[0]

    result = -1
    while l <= r:
        mid = l + (r-l)//2

        if get_router_count(mid) >= 0:
            result = max(result, mid)
            l = mid+1
        else:
            r = mid-1
    return result

def get_router_count(mid):
    router = 1
    prev_router_location = arr[0]
    for i in range(1, n):
        if arr[i] - prev_router_location >= mid:
            router+=1
            prev_router_location = arr[i]
    return router - c

if __name__ == "__main__":
    print(solution())