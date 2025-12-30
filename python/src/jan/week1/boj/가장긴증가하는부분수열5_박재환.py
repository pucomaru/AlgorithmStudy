import sys
input = sys.stdin.readline

n = int(input())
arr = list(map(int, input().split()))
arr_idx = [0] * n
lis_list = []

def lis():
    from collections import deque

    for i in range(n):
        if len(lis_list) == 0 or lis_list[-1] < arr[i]:
            lis_list.append(arr[i])
            arr_idx[i] = len(lis_list)
            continue
        insert_idx = get_insert_idx(arr[i])
        arr_idx[i] = insert_idx+1
        lis_list[insert_idx] = arr[i]

    print(len(lis_list))
    dq = deque()
    lis_len = len(lis_list)
    for i in range(n-1, -1, -1):
        if lis_len == arr_idx[i]:
            dq.appendleft(arr[i])
            lis_len-=1

    print(*dq)

def get_insert_idx(target):
    l = 0
    r = len(lis_list) - 1

    while(l < r):
        mid = l + (r-l)//2
        if lis_list[mid] >= target:
            r = mid
        else:
            l = mid + 1
    return l

if __name__ == '__main__':
    lis()