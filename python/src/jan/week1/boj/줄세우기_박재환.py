import sys
input = sys.stdin.readline

n = int(input())
arr = []

def solution():
    global arr

    arr.append(int(input()))
    for _ in range(1, n):
        target = int(input())
        if arr[-1] <= target:
            arr.append(target)
            continue
        insert_idx = find_insert_index(target)
        arr[insert_idx] = target

    return n - len(arr)



def find_insert_index(target):
    # Lower Bound
    # : target 이상이 처음 등장하는 위치
    l = 0
    r = len(arr)-1

    while l < r:
        mid = l + (r-l) // 2
        if arr[mid] >= target:      # target 이상 조건 만족
            r = mid
        else:
            l = mid+1
    return l

if __name__ == '__main__':
    print(solution())