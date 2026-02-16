def solution():
    """
    SWEA 에서는 sys import 불가
    그냥 input() 사용
    """
    import sys
    input = sys.stdin.readline

    tc = int(input().strip())

    def get_min_cost():
        prices = list(map(int, input().strip().split()))
        month = [0] + list(map(int, input().strip().split()))

        dp = [0 for _ in range(13)]
        for i in range(1, 13):
            """
            1일 이용권과, 1달 이용권을 비교해, 최적해 누적 
            """
            dp[i] = dp[i-1] + min(month[i] * prices[0], prices[1])
            """
            1달 이용권과, 3달 이용권 비교해, 최적해 누적
            """
            if i >= 3:          # 3달 이용권을 구매할 수 있는 시점부터
                dp[i] = min(dp[i], dp[i-3] + prices[2])
        return min(dp[12], prices[3])

    for i in range(1, tc+1):
        result = get_min_cost()
        print(f"#{i} {result}")

if __name__ == '__main__':
    solution()