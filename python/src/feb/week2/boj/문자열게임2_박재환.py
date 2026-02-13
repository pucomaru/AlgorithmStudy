def solution():
    import sys
    input = sys.stdin.readline

    tc = int(input())
    for _ in range(tc):
        word = input().strip()
        k = int(input())

        alpha = [[] for _ in range(26)]

        for i in range(len(word)):
            id = ord(word[i]) - ord('a')
            alpha[id].append(i)

        max_len = -1
        min_len = len(word)+1

        for i in range(26):
            for j in range(len(alpha[i])-k+1):
                s = alpha[i][j]
                e = alpha[i][j+k-1]
                l = e-s+1
                min_len = min(min_len, l)
                max_len = max(max_len, l)
        if min_len == len(word)+1 or max_len == -1:
            print(-1)
        else:
            print(min_len,max_len)

if __name__ == '__main__':
    solution()