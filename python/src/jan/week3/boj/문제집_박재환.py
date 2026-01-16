def solution():
    import sys
    input = sys.stdin.readline

    n, m = map(int, input().split())    # 문제 수, 선행 관계 수
    in_edges = [0] * (n+1)
    connections = [[] for _ in range(n+1)]

    while m > 0:
        m -= 1
        prev, next = map(int, input().split())

        in_edges[next] += 1
        connections[prev].append(next)

    def solve():
        import heapq as hq

        h = []
        for problem in range(1, n+1):
            if in_edges[problem] == 0:
                hq.heappush(h, problem)

        answer = []
        while h:
            problem = hq.heappop(h)

            for connection in connections[problem]:
                in_edges[connection] -= 1
                if in_edges[connection] == 0:
                    hq.heappush(h, connection)

            answer.append(problem)
        return answer

    return " ".join(map(str,solve()))

if __name__ == '__main__':
    print(solution())