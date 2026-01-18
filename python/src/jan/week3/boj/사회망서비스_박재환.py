def solution():
    import sys
    sys.setrecursionlimit(10**6)
    input = sys.stdin.readline

    n = int(input().strip())
    connections = [[] for _ in range(n+1)]
    visited = [False] * (n+1)
    min_ad_count = 0
    for _ in range(n-1):
        from_, to_ = map(int, input().split())
        connections[from_].append(to_)
        connections[to_].append(from_)

    def is_ad(node : int):
        nonlocal min_ad_count

        visited[node] = True

        ad = False      # 현재 노드가 AD 인지
        # 현재 노드와 연결되어 있는 노드들을 탐색
        for connection in connections[node]:
            if visited[connection]:
                continue
            # 자식 노드 중, 하나라도 AD 가 아니라면 -> 현재 노드는 AD 가 되어야함
            if not is_ad(connection):
                ad = True

        if ad:
            min_ad_count += 1
        return ad

    is_ad(1)
    print(min_ad_count)

if __name__ == '__main__':
    solution()