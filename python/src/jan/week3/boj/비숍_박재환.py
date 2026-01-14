def solution():
    import sys
    sys.setrecursionlimit(10**6)
    input = sys.stdin.readline

    n = int(input())
    board = [list(map(int, input().split())) for _ in range(n)]
    diagoal_left = [0]*(2*n)        # 대각선 ( / )
    diagoal_right = [0]*(2*n)       # 대각선 ( \ )

    white_bishop = 0
    black_bishop = 0

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
            if diagoal_left[(x+y)] > 0 or diagoal_right[(n-1) + (x-y)] > 0:
                continue
            if (x+y)%2 == color:
                continue
                
            # 비숍을 놓을 수 있음
            diagoal_left[(x + y)]+=1
            diagoal_right[(n - 1) + (x - y)] += 1
            put_bishop(i+1, bishop_count+1, color)
            diagoal_left[(x + y)] -= 1
            diagoal_right[(n - 1) + (x - y)] -= 1

    put_bishop(0,0,0)
    put_bishop(0,0,1)

    return black_bishop + white_bishop

if __name__ == '__main__':
    print(solution())

