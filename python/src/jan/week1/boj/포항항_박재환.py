import sys
input = sys.stdin.readline

n, m = map(int, input().split())
arr = [['X'] * (m + 2) for _ in range(n+2)]

for i in range(1, n+1):
    row = list(input().strip())
    for j in range(m):
        arr[i][j+1] = row[j]

stores = []
def map_arr():
    global arr

    store_idx = 1;
    for i in range(1, n+1):
        for j in range(1, m+1):
            if arr[i][j] == 'S':
                stores.append([i, j])
                arr[i][j] = 0
            elif arr[i][j] == 'K':
                stores.append([i, j])
                arr[i][j] = store_idx
                store_idx+=1

def get_time_table():
    time_table = [[-1_000_000]*len(stores) for _ in range(len(stores))]
    for store_idx in range(len(stores)):
        find_min_time(store_idx, time_table)
    return time_table

dx = [0,1,0,-1]
dy = [1,0,-1,0]
def find_min_time(store_idx, time_table):
    from collections import deque
    q = deque()

    visited = [[False]*(m+2) for _ in range(n+2)]
    x, y = stores[store_idx]
    q.append([x, y, 0])
    visited[x][y] = True

    while q:
        cur = q.popleft()
        cur_x = cur[0]
        cur_y = cur[1]
        cur_time = cur[2]

        if arr[cur_x][cur_y] != '.' and arr[cur_x][cur_y] != 'X':
            time_table[store_idx][arr[cur_x][cur_y]] = cur_time

        for d in range(4):
            nx = cur_x + dx[d]
            ny = cur_y + dy[d]

            if arr[nx][ny] == 'X' or visited[nx][ny]:
                continue
            visited[nx][ny] = True
            q.append([nx, ny, cur_time + 1])

min_time = 1_000_001
def solution():
    map_arr()
    time_table = get_time_table()
    get_permutation(0, [0]*len(stores), [False]*len(stores), 0, time_table)
    print(min_time)

def get_permutation(selected, seq, used, time, time_table):
    global min_time
    if selected == 6:
        min_time = min(min_time, time)
        return

    for i in range(0, len(stores)):
        if used[i]:
            continue

        time += time_table[seq[selected-1]][i]
        seq[selected] = i
        used[i] = True
        if time < min_time:
            get_permutation(selected+1, seq, used, time, time_table)
        time -= time_table[seq[selected - 1]][i]
        used[i] = False
if __name__ == '__main__':
    solution()
