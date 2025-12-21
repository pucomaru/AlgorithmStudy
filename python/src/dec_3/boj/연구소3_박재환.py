from collections import deque

MAX_INF = float('inf')
n, m = map(int, input().split(" "))
empty_space = 0
viruses = []
board = []
for x in range(n):
    y_arr = list(map(int, input().split(" ")))
    board.append(y_arr)
    for y in range(n):
        if board[x][y] == 0:
            empty_space+=1
        elif(board[x][y] == 2):
            viruses.append((x, y))


def solution(n, m, board, viruses, empty_space):
    min_time = MAX_INF
    # 1. 선택 가능한 바이러스 조합 찾기
    min_time = min(min_time, find_virus_combinations(0, 0, [0]*m, viruses, m, empty_space, n, board))
    print(-1 if min_time == MAX_INF else min_time)


def find_virus_combinations(idx, selected_idx, virus_combination, viruses, m, empty_space, n, board):
    if(selected_idx == m):
        # 바이러스 조합 완성
        return get_min_spread_time(virus_combination, board, n, empty_space)
    
    if(idx >= len(viruses) or (selected_idx + len(viruses)-idx) < m ):
        # 조합을 만들지 못하는 경우
        return MAX_INF

    virus_combination[selected_idx] = viruses[idx]
    t1 = find_virus_combinations(idx+1, selected_idx+1, virus_combination, viruses, m, empty_space, n, board)
    t2= find_virus_combinations(idx+1, selected_idx, virus_combination, viruses, m, empty_space, n, board)

    return min(t1, t2)

def get_min_spread_time(virus_combination, board, n, empty_space):
    q = deque()
    time_board = [list([-1]*n) for _ in range(n)]     # 배열 초기화
    for virus in virus_combination:
        x, y = virus
        q.append((x, y))
        time_board[x][y] = 0

    dx = [0,1,0,-1]
    dy = [1,0,-1,0]
    spread_cnt = 0
    max_time = -1

    while(q):
        x, y = q.popleft()
        cur_time = time_board[x][y]

        for dir in range(4):
            nx = x + dx[dir]
            ny = y + dy[dir]

            if nx < 0 or ny < 0 or nx >= n or ny >= n:
                continue
            if time_board[nx][ny] != -1 or board[nx][ny] == 1:
                continue

            time_board[nx][ny] = cur_time + 1
            q.append((nx, ny))

            # 빈 칸에 전이되는 경우 
            if board[nx][ny] == 0:
                spread_cnt += 1
                max_time = max(max_time, cur_time + 1)

                if spread_cnt == empty_space:
                    return max_time
    
    return MAX_INF

if __name__ == "__main__":
    if empty_space == 0:
        print(0)
    else:
        solution(n, m, board, viruses, empty_space)