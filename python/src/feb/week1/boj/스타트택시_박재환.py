def solution():
    import sys
    input = sys.stdin.readline

    n, m, fuel = map(int, input().split())
    board = list(list(map(int, input().split())) for _ in range(n))
    for x in range(n):
        for y in range(n):
            if board[x][y] == 1:
                board[x][y] = -1

    tx, ty = map(int, input().split())
    taxi = [tx-1, ty-1, fuel, 0]        # x, y, 남은 연료, 태운 손님

    customers = []
    for i in range(m):
        sx, sy, ex, ey = map(int, input().split())
        customers.append([sx-1, sy-1, ex-1, ey-1])
        board[sx-1][sy-1] = i+1

    dx = [-1, 0, 1, 0]
    dy = [0, 1, 0, -1]

    def find_near_customer():
        from collections import deque
        visited = [[False] * n for _ in range(n)]
        q = deque()

        # 시작 위치
        q.append((taxi[0], taxi[1]))
        visited[taxi[0]][taxi[1]] = True

        dist = 0
        while q:
            size = len(q)
            candidates = []

            for _ in range(size):
                x, y = q.popleft()

                if board[x][y] > 0:
                    candidates.append((x, y))

                for d in range(4):
                    nx = x + dx[d]
                    ny = y + dy[d]

                    if nx < 0 or ny < 0 or nx >= n or ny >= n:
                        continue
                    if visited[nx][ny] or board[nx][ny] == -1:
                        continue

                    visited[nx][ny] = True
                    q.append((nx, ny))

            if candidates:
                candidates.sort()  # 행 → 열 우선
                cx, cy = candidates[0]
                return board[cx][cy] - 1, dist

            dist += 1

        return -1, -1
    def get_dist_to_end(customer_id):
        from collections import deque
        q = deque()
        visited = [[False] * n for _ in range(n)]

        sx, sy, ex, ey = customers[customer_id]
        q.append((sx, sy, 0))
        visited[sx][sy] = True

        while q:
            x, y, dist = q.popleft()
            if x == ex and y == ey:
                return dist
            for dir in range(4):
                nx = x + dx[dir]
                ny = y + dy[dir]

                if nx < 0 or ny < 0 or nx >= n or ny >= n:
                    continue
                if visited[nx][ny] or board[nx][ny] == -1:
                    continue
                visited[nx][ny] = True
                q.append((nx, ny, dist + 1))
        return -1

    while True:
        customer_id, c_dist = find_near_customer()
        if customer_id == -1:
            break

        # 손님을 찾았다면
        # 손님의 출발지까지 이동가능한지
        if taxi[2] < c_dist:
            break
        taxi[2] -= c_dist
        taxi[0] = customers[customer_id][0]
        taxi[1] = customers[customer_id][1]
        board[customers[customer_id][0]][customers[customer_id][1]] = 0

        dist_to_end = get_dist_to_end(customer_id)
        if dist_to_end == -1 or dist_to_end > taxi[2]:
            break
        taxi[2] -= dist_to_end
        taxi[0] = customers[customer_id][2]
        taxi[1] = customers[customer_id][3]
        taxi[2] += dist_to_end * 2
        taxi[3] += 1

    return taxi[2] if taxi[3] == m else -1

if __name__ == '__main__':
    print(solution())