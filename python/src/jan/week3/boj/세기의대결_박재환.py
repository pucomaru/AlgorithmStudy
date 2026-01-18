def solution():
    import sys
    input = sys.stdin.readline

    n = int(input())
    bullets = [list(map(int, input().split())) for _ in range(2)]

    def find_insert_idx(target, arr):
        l = 0
        r = len(arr)-1

        while l < r:
            mid = (l+r)//2
            if arr[mid] >= target:
                r = mid
            else:
                l = mid+1
        return l

    def get_lis_len(arr):
        lis_arr = []

        for num in arr:
            if len(lis_arr) == 0 or lis_arr[-1] < num:
                lis_arr.append(num)
                continue

            insert_idx = find_insert_idx(num, lis_arr)
            lis_arr[insert_idx] = num

        return len(lis_arr)

    def rotate_bullet(idx):
        max_lis_len = 1
        for _ in range(n):
            max_lis_len = max(max_lis_len, get_lis_len(bullets[idx]))
            temp_bullet = [None] * n
            for i in range(n):
                temp_bullet[i] = bullets[idx][(i+1)%n]
            bullets[idx] = temp_bullet

        return max_lis_len

    def winner(yj, hg):
        suffix = " Win!"

        return ("YJ" if yj > hg else "HG" if hg > yj else "Both") + suffix

    yj = rotate_bullet(0)
    hg = rotate_bullet(1)

    return winner(yj, hg)


if __name__ == '__main__':
    print(solution())