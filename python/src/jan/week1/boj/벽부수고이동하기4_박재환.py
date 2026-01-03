import sys
input = sys.stdin.readline

n, m = map(int, input().split())
origin_map = [list(map(int, input().strip())) for _ in range(n)]

space_map = {}
def find_empty_space():
    global origin_map

    c = 'a'
    for x in range(n):
        for y in range(m):
            if origin_map[x][y] != 0:
                continue
            space_count = find_near_empty_space_size(x, y, c)
            space_map[c] = space_count
            c = chr(ord(c)+1)

dx = [0,1,0,-1]
dy = [1,0,-1,0]
def find_near_empty_space_size(x,  y, c):
    from collections import deque
    q = deque()

    global origin_map

    q.append((x, y))
    origin_map[x][y] = c
    space_count = 1
    while q:
        cur_x, cur_y = q.popleft()

        for dir in range(4):
            nx = cur_x + dx[dir]
            ny = cur_y + dy[dir]

            if is_not_map(nx, ny):
                continue
            if origin_map[nx][ny] != 0:
                continue

            origin_map[nx][ny] = c
            q.append((nx, ny))
            space_count += 1

    return space_count

def is_not_map(x, y):
    return x < 0 or x >= n or y < 0 or y >= m

def make_new_map():
    new_map = [[0]*m for _ in range(n)]
    for x in range(n):
        for y in range(m):
            if origin_map[x][y] != 1:
                continue
            new_map[x][y] = total_near_space_count(x, y)

    return new_map

def total_near_space_count(x, y):
    space_set = set()
    total_near_space = 1

    for dir in range(4):
        nx = x + dx[dir]
        ny = y + dy[dir]
        if is_not_map(nx, ny):
            continue

        if origin_map[nx][ny] == 1:
            continue

        if origin_map[nx][ny] not in space_set:
            total_near_space += space_map[origin_map[nx][ny]]
            space_set.add(origin_map[nx][ny])

    return total_near_space % 10


def solution():
    find_empty_space()
    result = make_new_map()

    for row in result:
        print(''.join(map(str, row)))

if __name__ == "__main__":
    solution()
