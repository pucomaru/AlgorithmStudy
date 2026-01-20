def solution():
    import sys
    input = sys.stdin.readline

    INF = 10**9 + 1

    n = int(input())
    arr = list(map(int, input().split()))
    tree = [[INF, INF] for _ in range(4*n)]

    def min_node(a, b):
        if a[1] > b[1]:
            return b
        if a[1] < b[1]:
            return a
        return a if a[0] < b[0] else b

    def make_tree(id, s, e):
        nonlocal tree
        if s == e:
            tree[id] = [s, arr[s]]
            return tree[id]
        mid = (s+e) // 2
        l_tree = make_tree(2*id, s, mid)
        r_tree = make_tree(2*id+1, mid+1, e)
        tree[id] = min_node(l_tree, r_tree)
        return tree[id]

    def update(id, s, e, target_id, target_value):
        if s > target_id or e < target_id:
            return

        if s == e:
            arr[s] = target_value           # 원본 값 갱신
            tree[id] = [s, target_value]    # 트리 갱신
            return

        # 탐색할 영역 남음
        mid = (s+e) // 2
        update(2*id, s, mid, target_id, target_value)
        update(2*id+1, mid+1, e, target_id, target_value)

        tree[id] = min_node(tree[id*2], tree[id*2+1])

    def query(id, s, e, l, r):
        if r < s or l > e:
            return [INF, INF]

        if s >= l and e <= r:
            return tree[id]

        mid = (s+e) // 2
        l_tree = query(2*id, s, mid, l, r)
        r_tree = query(2*id+1, mid+1, e, l ,r)

        return min_node(l_tree, r_tree)



    # 세그먼트 트리 생성
    make_tree(1, 0, n-1)
    tc = int(input())

    for _ in range(tc):
        command, a, b = map(int, input().split())

        if command == 1:
            update(1, 0, n-1, a-1, b)
        elif command == 2:
            print(query(1, 0, n-1, a-1, b-1)[0]+1)


if __name__ == '__main__':
    solution()