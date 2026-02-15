def solution():
    import sys
    input = sys.stdin.readline

    n, m = map(int, input().split())
    board = [list(map(int, input().strip())) for _ in range(n)]

    dx = (0,1,0,-1)
    dy = (1,0,-1,0)
    def find_min_cost_route():
        from collections import deque
        q = deque()
        visited = [[[False] * 2 for _ in range(m)] for _ in range(n)]

        q.append((0,0,1,0))
        visited[0][0][0] = True

        while q:
            x, y, cost, block = q.popleft()

            if x == n-1 and y == m-1:
                return cost

            for d in range(4):
                nx = x + dx[d]
                ny = y + dy[d]

                if nx < 0 or ny < 0 or nx >= n or ny >= m:
                    continue
                if visited[nx][ny][block]:
                    continue
                if board[nx][ny] == 1:
                    if block == 0:
                        visited[nx][ny][block+1] = True
                        q.append((nx, ny, cost+1, block+1))
                    continue

                visited[nx][ny][block] = True
                q.append((nx, ny, cost + 1, block))

        return -1

    return find_min_cost_route()
if __name__ == '__main__':
    print(solution())