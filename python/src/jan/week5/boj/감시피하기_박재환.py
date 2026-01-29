def solution():
    import sys
    sys.setrecursionlimit(10**6)
    input = sys.stdin.readline

    n = int(input())
    board = [input().split() for _ in range(n)]

    teachers = []
    for x in range(n):
        for y in range(n):
            if board[x][y] == 'T':
                teachers.append((x, y))

    def is_not_board(x, y):
        return x < 0 or y < 0 or x >= n or y >= n

    dx = (0,1,0,-1)
    dy = (1,0,-1,0)
    def find_students():
        for tx, ty in teachers:
            for dir in range(4):
                x = tx + dx[dir]
                y = ty + dy[dir]

                while(not is_not_board(x, y)):
                    if board[x][y] == 'S':
                        return True
                    if board[x][y] == 'O':
                        break

                    x += dx[dir]
                    y += dy[dir]
        return False

    is_hide = False
    def install_obstacle(idx, installed):
        nonlocal is_hide
        if is_hide:
            return
        if installed == 3:
            if not find_students():
                is_hide = True
            return

        for i in range(idx, n**2):
            x = i//n
            y = i%n
            if board[x][y] != 'X':
                continue
            board[x][y] = 'O'
            install_obstacle(i+1, installed + 1)
            board[x][y] = 'X'

    install_obstacle(0, 0)
    print("YES" if is_hide else "NO")

if __name__ == '__main__':
    solution()