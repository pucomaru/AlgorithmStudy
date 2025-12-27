import sys
input = sys.stdin.readline

n, m = map(int, input().split())
arr = [list(map(int, input().split())) for _ in range(n)]

def get_sub_arr_sum(sub_arr):
    local_ = sub_arr[0]
    global_ = local_
    for i in range(1, m):
        local_ = max(sub_arr[i], local_ + sub_arr[i])
        global_ = max(global_, local_)
    return global_


def solution():
    max_sub_arr_sum = -(200*200*10000+1) 

    for top in range(n):
        zip_arr = [0]*m
        for bottom in range(top, n):
            for y in range(m):
                zip_arr[y] += arr[bottom][y]
            max_sub_arr_sum = max(max_sub_arr_sum, get_sub_arr_sum(zip_arr))

    return max_sub_arr_sum

if __name__ == '__main__':
    print(solution())
    