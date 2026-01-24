def solution():
    import sys
    input = sys.stdin.readline

    INF = 10**3 * 500 + 1

    n, m = map(int, input().split())

    if n == 0 and m == 0:
        return
    graph = [[] for _ in range(n+1)]
    s, e = map(int, input().split())
    for _ in range(m):
        from_, to_, dist_ = map(int, input().split())
        graph[from_].append((to_, dist_))

    blocked = [[False] * (n+1) for _ in range(n+1)]
    def exclude_route(prev):
        nonlocal blocked
        from collections import deque

        visited = [False] * (n+1)
        q = deque()

        q.append(e)
        visited[e] = True

        while q:
            to_ = q.popleft()

            for from_ in prev[to_]:
                blocked[from_][to_] = True

                if visited[from_]:
                    continue

                q.append(from_)
                visited[from_] = True


    def best_route():
        import heapq as hq

        pq = []
        dist_arr = [INF] * (n+1)
        prev_arr = [[] for _ in range(n+1)]

        # 시작 위치 설정
        dist_arr[s] = 0
        hq.heappush(pq, (0, s))     # 첫 번째 원소를 기준으로 오름차순 정렬 [거리, 노드]

        while pq:
            acc_dist, from_ = hq.heappop(pq)

            if dist_arr[from_] < acc_dist:
                continue

            for to_, conn_dist in graph[from_]:
                if dist_arr[to_] > acc_dist + conn_dist:
                    dist_arr[to_] = acc_dist + conn_dist
                    prev_arr[to_].clear()
                    prev_arr[to_].append(from_)
                    hq.heappush(pq, (dist_arr[to_], to_))
                elif dist_arr[to_] == acc_dist + conn_dist:
                    prev_arr[to_].append(from_)


        exclude_route(prev_arr)

    def second_best_route():
        import heapq as hq
        pq = []
        dist_arr = [INF] * (n + 1)

        # 시작 위치 설정
        dist_arr[s] = 0
        hq.heappush(pq, (0, s))  # 첫 번째 원소를 기준으로 오름차순 정렬 [거리, 노드]

        while pq:
            acc_dist, from_ = hq.heappop(pq)

            if dist_arr[from_] < acc_dist:
                continue

            for to_, conn_dist in graph[from_]:
                if blocked[from_][to_]:
                    continue

                if dist_arr[to_] > acc_dist + conn_dist:
                    dist_arr[to_] = acc_dist + conn_dist
                    hq.heappush(pq, (dist_arr[to_], to_))

        return dist_arr[e] if dist_arr[e] != INF else -1

    best_route()
    return second_best_route()

if __name__ == '__main__':
    while True:
        result = solution()
        if result == None:
            break
        print(result)