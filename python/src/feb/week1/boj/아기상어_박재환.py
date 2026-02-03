def solution():
    import sys
    input = sys.stdin.readline

    n = int(input().strip())
    board = [list(map(int, input().split())) for _ in range(n)]
    # 상어 위치 정하기
    shark = None
    for x in range(n):
        if not shark == None:
            break
        for y in range(n):
            if board[x][y] == 9:
                shark = [x, y, 2, 0]
                break

    dx = [0,1,0,-1]
    dy = [1,0,-1,0]
    def find_fish():
        from collections import deque
        q = deque()
        visited = [[False] * n for _ in range(n)]
        fish = None

        q.append((shark[0], shark[1], 0))
        visited[shark[0]][shark[1]] = True
        board[shark[0]][shark[1]] = 0
        while q:
            x, y, dist = q.popleft()
            if not fish == None:
                if dist > fish[2]:
                    continue
            for dir in range(4):
                nx = x + dx[dir]
                ny = y + dy[dir]

                if nx < 0 or ny < 0 or nx >= n or ny >= n:
                    continue
                if visited[nx][ny] or board[nx][ny] > shark[2]:
                    continue
                visited[nx][ny] = True
                q.append((nx, ny, dist+1))
                # 먹을 수 있는 물고기인지
                if board[nx][ny] > 0 and board[nx][ny] < shark[2]:
                    if fish == None:
                        fish = [nx, ny, dist+1]
                    elif fish[2] > dist+1:
                        fish = [nx, ny, dist+1]
                    elif fish[2] == dist+1 and fish[0] > nx:
                        fish = [nx, ny, dist + 1]
                    elif fish[2] == dist + 1 and fish[0] == nx and fish[1] > ny:
                        fish = [nx, ny, dist + 1]
        return fish

    total_time = 0
    while True:
        fish = find_fish()
        if fish == None:
            break
        x, y, dist = fish
        total_time += dist

        sx, sy, ss, se = shark
        sx = x
        sy = y
        se += 1
        if se == ss:
            se = 0
            ss += 1
        shark = [sx, sy, ss, se]

    return total_time




if __name__ == '__main__':
    print(solution())