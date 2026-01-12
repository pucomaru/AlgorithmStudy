import sys
input = sys.stdin.readline

INF = 500 * 500 + 1

H, W = map(int, input().split())
board = [[0] * W for _ in range(H)]

start = [0] * 2
end = [0] * 2

for x in range(H):
    str_ = input().rstrip()
    for y in range(W):
        board[x][y] = str_[y]
        if board[x][y] == 'S':
            start[0] = x
            start[1] = y
        elif board[x][y] == 'E':
            end[0] = x
            end[1] = y

dx = [0,1,0,-1]
dy = [1,0,-1,0]
def get_min_time():
    from collections import deque
    q = deque()
    time_table = [[INF] * W for _ in range(H)]

    start_x, start_y = start
    time_table[start_x][start_y] = 0
    q.append((start_x, start_y))

    while q:
        x, y = q.popleft()

        if x == end[0] and y == end[1]:
            return time_table[x][y]

        is_near_wall_flag = is_near_wall(x, y)
        for dir in range(4):
            nx = x + dx[dir]
            ny = y + dy[dir]

            if is_not_board(nx, ny):
                continue
            if board[nx][ny] == '#':
                continue

            add_time = 0 if is_near_wall_flag and is_near_wall(nx, ny) else 1

            if time_table[nx][ny] <= time_table[x][y] + add_time:
                continue

            time_table[nx][ny] = time_table[x][y] + add_time
            if add_time == 0:
                q.appendleft((nx, ny))
            else:
                q.append((nx, ny))

def is_near_wall(x, y):
    for dir in range(4):
        nx = x + dx[dir]
        ny = y + dy[dir]

        if is_not_board(nx, ny):
            continue

        if board[nx][ny] == '#':
            return True
    return False

def is_not_board(x, y):
    return x < 0 or y < 0 or x >= H or y >= W

def solution():
    print(get_min_time())

if __name__ == "__main__":
    solution()