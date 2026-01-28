def solution():
    import sys
    input = sys.stdin.readline

    INF = 1_000_000_005
    n, p, k = map(int, input().split())
    cables = [[] for _ in range(n+1)]
    max_price = 1
    for _ in range(p):
        from_, to_, cost_ = map(int, input().split())
        max_price = max(max_price, cost_)
        cables[from_].append((to_, cost_))
        cables[to_].append((from_, cost_))

    def get_min_cost():
        l = 0
        r = max_price

        result = 1_000_000
        while l <= r:
            mid = (l+r) // 2
            if can_reach_n(mid):
                result = min(mid, result)
                r = mid-1
            else:
                l = mid+1
        return result

    def can_reach_n(limit_cost):
        import heapq as hq
        h = []
        cost_arr = [INF] * (n+1)

        hq.heappush(h, (0, 1))      # 첫 번째 값이 우선순위
        cost_arr[1] = 0

        while h:
            acc_cost, from_ = hq.heappop(h)

            if cost_arr[from_] < acc_cost:
                continue

            for to_, cost_ in cables[from_]:
                extra_fee = 1 if cost_ > limit_cost else 0

                if cost_arr[to_] > cost_arr[from_] + extra_fee:
                    cost_arr[to_] = cost_arr[from_] + extra_fee
                    hq.heappush(h, (cost_arr[to_], to_))

        return cost_arr[n] <= k

    return get_min_cost()

if __name__ == '__main__':
    print(solution())