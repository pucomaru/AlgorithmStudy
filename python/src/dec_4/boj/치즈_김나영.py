from sys import stdin as s
from collections import deque

input = s.readline

N, M = map(int, input().split())
grid = [list(map(int, input().split())) for _ in range(N)]
cheese_num = 0
for r in grid:
    cheese_num += sum(r)

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
                    touched_cheese[(ni, nj)] = touched_cheese.get((ni, nj), 0) + 1
                    continue
                queue.append([ni, nj])
                visited[ni][nj] = 1

# def check_cheese():
#     for i in range(N):
#         for j in range(M):
#             if grid[i][j] == 1:
#                 return True
#
#     return False

def remove_cheese():
    # for i in range(N):
    #     for j in range(M):
    #         if touched_cheese[i][j] >= 2:
    #             grid[i][j] = 0
    #             global cheese_num
    #             cheese_num -= 1
    for i, j in touched_cheese:
        if touched_cheese[(i, j)] >= 2:
            grid[i][j] = 0
            global cheese_num
            cheese_num -= 1

num = 0
while cheese_num:
    # touched_cheese = [[0] * M for _ in range(N)]
    touched_cheese = {}
    bfs()
    remove_cheese()
    num += 1

print(num)
