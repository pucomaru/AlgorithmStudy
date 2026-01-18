def solution():
    import sys
    from collections import deque
    input = sys.stdin.readline

    tc = int(input())

    for _ in range(tc):
        cmd = input().strip()
        n = int(input())
        arr_str = input().strip()

        dq = deque()
        if n > 0:
            dq = deque(arr_str[1:-1].split(','))

        is_front = True
        error = False

        for c in cmd:
            if c == 'R':
                is_front = not is_front
            else:  # D
                if not dq:
                    print('error')
                    error = True
                    break
                if is_front:
                    dq.popleft()
                else:
                    dq.pop()

        if error:
            continue

        if is_front:
            print('[' + ','.join(dq) + ']')
        else:
            print('[' + ','.join(reversed(dq)) + ']')




if __name__ == '__main__':
    solution()