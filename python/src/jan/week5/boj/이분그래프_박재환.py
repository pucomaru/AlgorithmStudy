def solution():
    import sys
    input = sys.stdin.readline

    def search_adj_graph(node, graph, groups):
        from collections import deque
        q = deque()

        q.append(node)
        groups[node] = 1

        while q:
            cur = q.popleft()

            for next in graph[cur]:
                if groups[next] == 0:
                    q.append(next)
                    groups[next] = -groups[cur]
                elif groups[cur] == groups[next]:
                    return False
        return True

    def is_bi_graph():
        v, e = map(int, input().split())
        graph = [[] for _ in range(v+1)]
        groups = [0] * (v+1)
        for _ in range(e):
            from_, to_ = map(int, input().split())
            graph[from_].append(to_)
            graph[to_].append(from_)

        bi_graph = True
        for node in range(1, v+1):
            if groups[node] != 0:
                continue

            if not search_adj_graph(node, graph, groups):
                bi_graph = False

        return "YES" if bi_graph else "NO"


    tc = int(input().strip())
    for _ in range(tc):
        print(is_bi_graph())

if __name__ == '__main__':
    solution()
