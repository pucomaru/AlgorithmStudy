def solution():
    import sys
    from collections import deque
    input = sys.stdin.read().split()
    iterator = iter(input)

    q = deque()
    n, m = int(next(iterator)), int(next(iterator))
    map_ = [[None] * m for _ in range(n)]
    for x in range(n):
        for y in range(m):
            map_[x][y] = int(next(iterator))
            if map_[x][y]:
                q.append((x, y))

    def is_not_board(x, y):
        return x < 0 or y < 0 or x >= n or y >= m

    dx = [0, 1, 0, -1]
    dy = [1, 0, -1, 0]

    def after_one_year():
        nonlocal map_, q

        temp_q = deque()
        while q:
            x, y = q.popleft()
            near_sea = 0
            for dir in range(4):
                nx = x + dx[dir]
                ny = y + dy[dir]

                if is_not_board(nx, ny):
                    continue
                if map_[nx][ny] == 0:
                    near_sea += 1

            temp_q.append((x, y, near_sea))

        while temp_q:
            x, y, near_sea = temp_q.popleft()

            map_[x][y] = max(0, map_[x][y] - near_sea)
            if map_[x][y] > 0:
                q.append((x, y))

    def get_glacier_group():
        if len(q) == 0:
            return False

        temp_q = deque()
        visited = [[False] * m for _ in range(n)]

        first_x, first_y = q[0]
        temp_q.append((first_x, first_y))
        visited[first_x][first_y] = True
        glacier_size = 1
        while temp_q:
            x, y = temp_q.popleft()

            for dir in range(4):
                nx = x + dx[dir]
                ny = y + dy[dir]
                if is_not_board(nx, ny):
                    continue
                if not map_[nx][ny]:
                    continue
                if visited[nx][ny]:
                    continue

                visited[nx][ny] = True
                temp_q.append((nx, ny))
                glacier_size += 1

        return glacier_size < len(q)

    year = 0
    while q:
        after_one_year()
        year+=1
        if get_glacier_group():
            return year
    return 0


if __name__ == '__main__':
    print(solution())