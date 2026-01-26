def solution():
    import sys
    input = sys.stdin.readline

    n, m = map(int, input().split())
    roads = [[] for _ in range(n+1)]

    for _ in range(m):
        from_, to_, weight_ = map(int, input().split())
        roads[from_].append((to_, weight_))
        roads[to_].append((from_, weight_))

    s, e = map(int, input().split())
    max_value = 1_000_000_005
    def find_max_route():
        import heapq as hq
        h = []
        max_weight = [0] * (n+1)

        # 초기 설정
        hq.heappush(h, (-max_value, s))
        max_weight[s] = max_value

        while h:
            weight_, from_ = hq.heappop(h)
            weight_ *= -1

            if max_weight[from_] > weight_:
                continue

            for to_, next_weight in roads[from_]:
                if max_weight[to_] < min(next_weight, weight_):
                    max_weight[to_] = min(next_weight, weight_)
                    hq.heappush(h, (-max_weight[to_], to_)) # 내림차순으로 들어가야해서 음수 처리

        return max_weight[e]

    return find_max_route()

if __name__ == '__main__':
    print(solution())