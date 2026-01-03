from operator import truediv
from sys import stdin as s
from collections import deque

input = s.readline

N, M = map(int, input().split())
grid = [list(map(int, input().split())) for _ in range(N)]



def bfs():
    visited = [[0]*M for _ in range(N)]
    queue = deque([[0,0]])
    visited[0][0] = 1
    while queue:
        ti, tj = queue.popleft()
        for di, dj in [[1,0],[0,1],[-1,0],[0,-1]]:
            ni, nj = ti+di, tj+dj
            if 0 <= ni < N and 0 <= nj < M and visited[ni][nj] == 0:
                if grid[ni][nj] ==1:
                    touched_cheese[ni][nj] += 1
                    continue
                queue.append([ni, nj])
                visited[ni][nj] = 1

def check_cheese():
    for i in range(N):
        for j in range(M):
            if grid[i][j] == 1:
                return True

    return False

def remove_cheese():
    for i in range(N):
        for j in range(M):
            if touched_cheese[i][j] >= 2:
                grid[i][j] = 0

num = 0
while check_cheese():
    touched_cheese = [[0] * M for _ in range(N)]
    bfs()
    remove_cheese()
    num += 1

print(num)
