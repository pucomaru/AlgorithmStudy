str1, str2 = map(str, input().split())

def solution():
    if(len(str1) > len(str2)):
        lcs(str1, str2)
    else:
        lcs(str2, str1)

def lcs(maxStr, minStr):
    arr = [[0] * (len(minStr) + 1) for _ in range(maxStr + 1)]

    for i in range(1, len(maxStr)+1):
        for j in range(1, len(minStr)+1):
            if maxStr[i-1] == minStr[j-1]:
                arr[i][j] = arr[i-1][j-1]+1
            else:
                arr[i][j] = max(arr[i-1][j], arr[i][j-1])

    return None