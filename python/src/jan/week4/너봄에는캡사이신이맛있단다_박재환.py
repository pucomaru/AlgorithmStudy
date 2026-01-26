def solution():
    import sys
    input = sys.stdin.readline

    n = int(input())
    arr = list(map(int, input().split()))
    arr.sort(key= lambda x : x)

    DIV = 1_000_000_007
    def get_total_pain_score():
        make_pow_arr()

        max_ = 0
        min_ = 0

        for i in range(n):
            max_ = (max_ + get_max_pain_score(arr[i], i))%DIV
            min_ = (min_ + get_min_pain_score(arr[i], i))%DIV

        return (max_-min_+DIV)%DIV

    pow_arr = [1] * (n+1)
    def make_pow_arr():
        nonlocal pow_arr
        for i in range(1, n+1):
            pow_arr[i] = (pow_arr[i-1] * 2)%DIV

    def get_max_pain_score(max_, idx):
        return (pow_arr[idx] * max_)%DIV

    def get_min_pain_score(min_, idx):
        return (pow_arr[n-idx-1] * min_)%DIV

    return get_total_pain_score()

if __name__ == '__main__':
    print(solution())