import sys
input = sys.stdin.readline

str1 = input().rstrip()
str2 = input().rstrip()

def solution():
    if len(str1) > len(str2):
        lcs(str1, str2)
    else:
        lcs(str2, str1)

def lcs(maxStr, minStr):
    arr = [[0]*(len(minStr)+1) for _ in range(len(maxStr)+1)]

    for i in range(1, len(maxStr)+1):
        for j in range(1, len(minStr)+1):
            if maxStr[i-1] == minStr[j-1]:
                arr[i][j] = arr[i-1][j-1] + 1
            else:
                arr[i][j] = max(arr[i-1][j], arr[i][j-1])

    print(arr[-1][-1])
    get_loc_str(arr, maxStr, minStr)

def get_loc_str(arr, maxStr, minStr):
    from collections import deque
    dq = deque()

    i = len(maxStr)
    j = len(minStr)

    while i > 0 and j > 0:
        if maxStr[i-1] == minStr[j-1]:
            dq.appendleft(maxStr[i-1])
            i-=1
            j-=1
        else:
            if arr[i-1][j] > arr[i][j-1]:
                i-=1
            else:
                j-=1

    print("".join(dq))

if __name__ == "__main__":
    solution()