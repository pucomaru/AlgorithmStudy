def solution():
    import sys
    input = sys.stdin.readline

    def change_cost(cost):
        return 1 if cost == 0 else 0

    n, m = map(int, input().split())
    enter, first, default_cost = map(int, input().split())
    default_cost = change_cost(default_cost)
    roads = [[] for _ in range(n + 1)]
    for _ in range(m):
        a, b, cost = map(int, input().split())
        cost = change_cost(cost)
        roads[a].append((b, cost))
        roads[b].append((a, cost))

    def get_min_uphill(building_count):
        import heapq as hq
        h = []
        visited = [False] * (building_count + 1)

        # 시작 지점
        visited[first] = True
        for b, cost in roads[first]:
            hq.heappush(h, (cost, b))

        conn_roads = 0
        uphill = 0
        while h:
            cost, a = hq.heappop(h)
            if visited[a]:
                continue
            visited[a] = True
            conn_roads += 1
            uphill += cost

            if conn_roads == building_count - 1:
                return uphill

            for b, cost in roads[a]:
                if visited[b]:
                    continue
                hq.heappush(h, (cost, b))

    def get_max_uphill(building_count):
        import heapq as hq
        h = []
        visited = [False] * (building_count + 1)

        # 시작 지점
        visited[first] = True
        for b, cost in roads[first]:
            hq.heappush(h, (-cost, b))

        conn_roads = 0
        uphill = 0
        while h:
            cost, a = hq.heappop(h)
            cost *= -1
            if visited[a]:
                continue
            visited[a] = True
            conn_roads += 1
            uphill += cost

            if conn_roads == building_count - 1:
                return uphill

            for b, cost in roads[a]:
                if visited[b]:
                    continue
                hq.heappush(h, (-cost, b))

    min_uphill = get_min_uphill(n) + default_cost
    max_uphill = get_max_uphill(n) + default_cost

    result = max_uphill**2 - min_uphill**2
    return result

if __name__ == '__main__':
    print(solution())