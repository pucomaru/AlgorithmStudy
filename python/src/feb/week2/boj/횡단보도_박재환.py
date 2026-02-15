def solution():
    import sys
    input = sys.stdin.readline

    n, m = map(int, input().split())
    cross_walks = [[] for _ in range(n+1)]
    for time in range(m):
        a, b = map(int, input().split())
        cross_walks[a].append((b, time))
        cross_walks[b].append((a, time))

    def next_time(cur_time, open_time):
        if cur_time <= open_time:
            return open_time

        next = (cur_time - open_time + m - 1) // m
        return open_time + next * m

    def find_fastest_route():
        import heapq
        pq = []
        dist = [float('inf')] * (n+1)

        # 처음 시작은 1
        heapq.heappush(pq, (0, 1))      # (time, to)
        dist[1] = 0

        while pq:
            cur_time, cur_node = heapq.heappop(pq)

            if dist[cur_node] < cur_time:
                continue
            if cur_node == n: return cur_time

            for next, time in cross_walks[cur_node]:
                next_t = next_time(cur_time, time)
                next_t+=1

                if dist[next] > next_t:
                    dist[next] = next_t
                    heapq.heappush(pq, (next_t, next))

    return find_fastest_route()

if __name__ == '__main__':
    print(solution())