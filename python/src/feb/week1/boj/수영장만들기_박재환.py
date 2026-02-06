def solution():
    import sys
    input = sys.stdin.readline

    n,m = map(int, input().split())
    board = [list(map(int, input().strip())) for _ in range(n)]

    dx = [1,0,-1,0]
    dy = [0,1,0,-1]
    def check_water_h(h):
        from collections import deque
        q = deque()
        visited = [[False] * m for _ in range(n)]

        # 경계부터 탐색하며, 물이 찰 수 없는 위치를 시작 포인트로 기록
        for x in range(n):
            for y in range(m):
                if x == 0 or y == 0 or x == n-1 or y == m-1:
                    if board[x][y] < h:
                        visited[x][y] = True
                        q.append((x, y))

        while q:
            x, y = q.popleft()

            for dir in range(4):
                nx = x + dx[dir]
                ny = y + dy[dir]

                if nx < 0 or ny < 0 or nx >= n or ny >=m:
                    continue
                if visited[nx][ny] or board[nx][ny] >= h:
                    continue

                visited[nx][ny] = True
                q.append((nx, ny))

        water = 0
        for x in range(n):
            for y in range(m):
                if not visited[x][y] and board[x][y] < h:
                    water+=1
        return water

    result = 0
    for h in range(2,10):
        result += check_water_h(h)

    return result

if __name__ == '__main__':
    print(solution())

