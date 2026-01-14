def solution():
    import sys
    sys.setrecursionlimit(10**6)
    input = sys.stdin.readline

    n = int(input())
    board = [list(map(int, input().split())) for _ in range(n)]
    is_used = [[0]*n for _ in range(n)]

    white_bishop = 0
    black_bishop = 0

    def is_not_board(x, y):
        return x < 0 or y < 0 or x >= n or y >= n

    dx = [1,1,-1,-1]
    dy = [1,-1,1,-1]
    def check_uncheck(x, y, check):
        for dir in range(4):
            cur_x = x
            cur_y = y

            while not is_not_board(cur_x, cur_y):
                if check:
                    is_used[cur_x][cur_y] += 1
                else:
                    is_used[cur_x][cur_y] = max(0, is_used[cur_x][cur_y]-1)

                cur_x += dx[dir]
                cur_y += dy[dir]

    def put_bishop(idx, bishop_count, color):
        nonlocal white_bishop, black_bishop
        if color == 0:
            black_bishop = max(black_bishop, bishop_count)
        else:
            white_bishop = max(white_bishop, bishop_count)

        for i in range(idx, n**2):
            x = i//n
            y = i%n

            if board[x][y] == 0:
                continue
            if is_used[x][y] > 0:
                continue
            if (x+y)%2 == color:
                continue
                
            # 비숍을 놓을 수 있음
            check_uncheck(x, y, True)
            put_bishop(idx+1, bishop_count+1, color)
            check_uncheck(x, y, False)

    put_bishop(0,0,0)
    put_bishop(0,0,1)

    return black_bishop + white_bishop

if __name__ == '__main__':
    print(solution())

