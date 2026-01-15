def solution():
    import sys
    input = sys.stdin.readline

    n,m = map(int, input().split())
    board = [[None]*m for _ in range(n)]

    rx = 0
    ry = 0
    bx = 0
    by = 0
    for x in range(n):
        str = input().strip()
        for y in range(m):
            board[x][y] = str[y]
            if board[x][y] == 'R':
                rx = x
                ry = y
            elif board[x][y] == 'B':
                bx = x
                by = y

    dx = [0,1,0,-1]
    dy = [1,0,-1,0]
    def move_end(x, y, dir):
        move_cnt = 0
        while True:
            nx = x + dx[dir]
            ny = y + dy[dir]
            # 움직이지 못하게 막힘
            if board[nx][ny] == '#':
                break
            x = nx
            y = ny
            move_cnt += 1
            if board[x][y] == 'O':
                break
        return [x, y, move_cnt]
    def find_min_dir_change(rx, ry, bx,by):
        from collections import deque
        q = deque()
        visited = [[[[False for _ in range(m)] for _ in range(n)] for _ in range(m)] for _ in range(n)]
        min_change_dir = 10**6
        q.append((rx, ry, bx, by, 0))
        visited[rx][ry][bx][by] = True
        while q:
            cur_rx, cur_ry, cur_bx, cur_by, cur_dir_change = q.popleft()

            if cur_dir_change >= 10:
                continue

            for dir in range(4):
                # 새로운 방향으로 이동
                nrx, nry, r_move = move_end(cur_rx, cur_ry, dir)
                nbx, nby, b_move = move_end(cur_bx, cur_by, dir)
                if board[nbx][nby] == 'O':    # 파랑 구슬이 먼저 도착하는 경우
                    continue
                if board[nrx][nry] == 'O':
                    min_change_dir = min(min_change_dir, cur_dir_change+1)
                    return min_change_dir
                if nrx == nbx and nry == nby:
                    if r_move > b_move:
                        nrx -= dx[dir]
                        nry -= dy[dir]
                    else:
                        nbx -= dx[dir]
                        nby -= dy[dir]
                if visited[nrx][nry][nbx][nby]:
                    continue
                visited[nrx][nry][nbx][nby] = True
                q.append((nrx, nry, nbx, nby, cur_dir_change + 1))
        return -1

    return find_min_dir_change(rx, ry, bx, by)

if __name__ == "__main__":
    print(solution())


