import sys
sys.setrecursionlimit(1_000_000_000)
input = sys.stdin.readline

dx = [-1,1,0,0]
dy = [0,0,-1,1]
n, m = map(int, input().split())
state = [[0]*m for _ in range(n)]       # 상태를 저장할 배열
sz_count = 0

map = [[None]*m for _ in range(n)]
for x in range(n):
    input_ = input().strip()
    for y in range(m):
        map[x][y] = input_[y]

def get_dir(c):
    if c == 'U':
        return 0
    if c == 'D':
        return 1
    if c == 'L':
        return 2
    return 3

def find_cycle(x, y):
    global state, sz_count
    state[x][y] = 1;

    dir = get_dir(map[x][y])
    nx = x + dx[dir]
    ny = y + dy[dir]

    if state[nx][ny] == 0:      # 아직 방문하지 않은 부분이라면 계속해서 탐색
        find_cycle(nx, ny)
    elif state[nx][ny] == 1:     # 동일한 탐색에서 사이클이 존재하는 경우
        sz_count+=1

    state[x][y] = 2           # 탐색이 완료된 경우에는 탐색 완료 처리


def solution():
    for x in range(n):
        for y in range(m):
            if state[x][y] != 0:
                continue

            find_cycle(x, y)

    print(sz_count)

if __name__ == '__main__':
    solution()