def solution():
    import sys
    from collections import deque
    input = sys.stdin.readline

    n, m, k = map(int, input().split())
    a = [list(map(int, input().split())) for _ in range(n)]
    board = [[5] * n for _ in range(n)]
    trees = [[deque() for _ in range(n)] for _ in range(n)]
    temp = [[[] for _ in range(n)] for _ in range(n)]
    for _ in range(m):
        x, y, age = map(int, input().split())
        temp[x-1][y-1].append(age)

    for x in range(n):
        for y in range(n):
            if not temp[x][y]:  # 나무가 없다면 넘어감
                continue
            temp[x][y].sort()
            trees[x][y] = deque(temp[x][y])

    dead_tree = None
    def spring():
        nonlocal trees, dead_tree
        """
        양분을 흡수한다.
        if age <= map[x][y] : 양분 흡수 가능
        else : 즉사
        """
        dead_tree = [[[] for _ in range(n)] for _ in range(n)]
        for x in range(n):
            for y in range(n):
                if not trees[x][y]:     # 나무가 없다면 넘어감
                    continue
                # 나무가 있다면
                cur = trees[x][y]
                next = deque()

                while cur:
                    age = cur.popleft()
                    if age <= board[x][y]:
                        next.append(age+1)
                        board[x][y] -= age
                    else:
                        dead_tree[x][y].append(age)
                        while cur:
                            dead_tree[x][y].append(cur.popleft())
                        break

                trees[x][y] = next

    def summer():
        """
        dead_tree 를 기반으로 양분을 추가한다.
        """
        for x in range(n):
            for y in range(n):
                if not dead_tree[x][y]:
                    continue

                for age in dead_tree[x][y]:
                    board[x][y] += (age//2)

    dx = [0,1,0,-1,1,1,-1,-1]
    dy = [1,0,-1,0,1,-1,1,-1]
    def autumn():
        for x in range(n):
            for y in range(n):
                if not trees[x][y]:     # 나무가 없다면 넘어감
                    continue
                # 나무가 있다면
                for age in trees[x][y]:
                    if age%5 != 0:
                        continue
                    for dir in range(8):
                        nx = x + dx[dir]
                        ny = y + dy[dir]
                        if nx < 0 or ny < 0 or nx >= n or ny >= n:
                            continue
                        trees[nx][ny].appendleft(1)

    def winter():
        nonlocal  board
        """
        a 배열의 값을 누적
        """
        for x in range(n):
            for y in range(n):
                board[x][y] += a[x][y]

    for _ in range(k):
        spring()
        summer()
        autumn()
        winter()

    cnt = 0
    for x in range(n):
        for y in range(n):
            cnt += len(trees[x][y])

    return cnt


if __name__ == '__main__':
    print(solution())