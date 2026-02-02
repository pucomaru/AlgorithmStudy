def solution():
    import sys
    input = sys.stdin.readline
    tc = int(input())

    INF = 10000 * 100000 + 7
    def spread_infection(n, start, computers):
        import heapq as hq
        h = []
        dp = [INF] * (n+1)

        hq.heappush(h, (0, start))      # (비용, 노드)
        dp[start] = 0

        while h:
            acc, cur = hq.heappop(h)

            if dp[cur] < acc:
                continue

            for next, cost in computers[cur]:
                if dp[next] > acc + cost:
                    dp[next] = acc + cost
                    hq.heappush(h, (dp[next], next))

        total_time = -1
        count = 0
        for i in dp:
            if i == INF:
                continue
            total_time = max(total_time, i)
            count += 1

        return (count, total_time)

    def init():
        n, d, c = map(int, input().split())
        computers = [[] for _ in range(n+1)]
        for _ in range(d):
            a, b, s = map(int, input().split())
            computers[b].append((a, s))

        result = spread_infection(n, c, computers)
        return f"{result[0]} {result[1]}"

    for _ in range(tc):
        print(init())

if __name__ == '__main__':
    solution()