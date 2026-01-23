def solution():
    import sys
    sys.setrecursionlimit(10**6)
    input = sys.stdin.readline

    v, e = map(int, input().split())
    graph = [[] for _ in range(v+1)]
    result = []

    for _ in range(e):
        from_, to_ = map(int, input().split())
        graph[from_].append(to_)

    id = 0
    id_arr = [0] * (v+1)
    parents = [0] * (v+1)
    on_stack = [False] * (v+1)
    stack = []
    def tarjan():
        for i in range(1, v+1):
            if id_arr[i] != 0:      # 이미 이전에 방문한 정점이라면
                continue
            dfs(i)

    def dfs(node):
        nonlocal id, id_arr, parents, on_stack, stack, result

        # 현재 정점을 초기화
        id += 1
        id_arr[node] = parents[node] = id
        on_stack[node] = True
        stack.append(node)

        for next in graph[node]:
            if id_arr[next] == 0:       # 아직 방문하기 전
                dfs(next)
                parents[node] = min(parents[node], parents[next])
            elif on_stack[next]:
                parents[node] = min(parents[node], id_arr[next])


        if id_arr[node] == parents[node]:
            scc = []

            while True:
                v = stack.pop()
                scc.append(v)
                on_stack[v] = False
                if v == node:
                    break

            scc.sort()
            scc.append(-1)
            result.append(scc)

    tarjan()
    result.sort(key=lambda x : x[0])
    print(len(result))
    for arr in result:
        print(" ".join(map(str, arr)))

if __name__ == '__main__':
    solution()