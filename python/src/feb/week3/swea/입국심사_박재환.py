def solution():
    tc = int(input())
    def find_min_time():
        n, m = map(int, input().strip().split())
        arr = list(int(input().strip()) for _ in range(n))

        l = 0
        r = m * max(arr)
        """
        [Lower Bound]
        목표보다 크거나 같은 값 중 최소값
        """
        while l < r:
            mid = (l+r)//2

            sum = 0
            for i in arr:
                sum += (mid//i)
                if sum >= m:
                    break
            if sum >= m:
                r = mid
            else:
                l = mid+1

        return l
    for i in range(tc):
        result = find_min_time()
        print(f'#{i+1} {result}')

if __name__ == '__main__':
    solution()