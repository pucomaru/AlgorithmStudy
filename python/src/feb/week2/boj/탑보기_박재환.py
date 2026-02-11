def solution():
    import sys
    input = sys.stdin.readline

    n = int(input().strip())
    arr = [None for _ in range(n+1)]
    can_see = [0 for _ in range(n+1)]
    near = [0 for _ in range(n+1)]
    temp = list(map(int, input().split()))
    for i in range(1,n+1):
        arr[i] = (i, temp[i-1])

    def get_result():
        from collections import deque
        q = deque()

        for i in range(1, n+1):
            while q and q[-1][1] <= arr[i][1]:
                q.pop()
            can_see[i] += len(q)
            if q:
                near[i] = q[-1][0]
            q.append(arr[i])
        q.clear()
        for i in range(n, 0, -1):
            while q and q[-1][1] <= arr[i][1]:
                q.pop()
            can_see[i] += len(q)
            if q:
                candidate = q[-1][0]
                if near[i] == 0:
                    near[i] = candidate
                else:
                    dist_1 = abs(near[i] - arr[i][0])
                    dist_2 = abs(candidate - arr[i][0])

                    if dist_1 > dist_2:
                        near[i] = candidate
                    elif dist_1 == dist_2:
                        near[i] = min(near[i], candidate)
            q.append(arr[i])

        for i in range(1, n + 1):
            if can_see[i] == 0:
                print(0)
            else:
                print(can_see[i], near[i])

    get_result()
if __name__ == '__main__':
    solution()