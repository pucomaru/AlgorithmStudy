def solution():
    import sys
    sys.setrecursionlimit(10**6)
    input = sys.stdin.readline

    n, q1, q2 = map(int, input().split())
    arr = [0] + list(map(int, input().split()))

    tree = [0 for _ in range(4*(n+1))]
    lazy = [0 for _ in range(4*(n+1))]

    def build_tree(id, l, r):
        if l == r:
            tree[id] = arr[l]
            return tree[id]
        mid = (l + r) // 2
        tree[id] = build_tree(2 * id, l, mid) + build_tree(2 * id + 1, mid + 1, r)
        return tree[id]

    def push(id, l, r):
        if lazy[id] == 0 or l == r:
            return

        temp = lazy[id]
        mid = (l + r) // 2
        tree[2 * id] = tree[2*id] + (temp * (mid - l + 1))
        lazy[2 * id] += temp
        tree[2 * id + 1] = tree[2*id+1] + (temp * (r - mid))
        lazy[2 * id + 1] += temp

        lazy[id] = 0

    def update(id, l, r, s, e, v):
        if r < s or l > e:
            return
        if l >= s and r <= e:
            tree[id] = tree[id] + (v * (r - l + 1))
            lazy[id] += v
            return
        push(id, l, r)
        mid = (l + r) // 2
        update(2 * id, l, mid, s, e, v)
        update(2 * id + 1, mid + 1, r, s, e, v)
        tree[id] = tree[2 * id] + tree[2 * id + 1]

    def query(id, l, r, s, e):
        if r < s or l > e:
            return 0
        if l >= s and r <= e:
            return tree[id]
        push(id, l, r)
        mid = (l + r) // 2
        return query(2 * id, l, mid, s, e) + query(2 * id + 1, mid + 1, r, s, e)

    build_tree(1, 1, n)
    out = []
    while q1 + q2 > 0:
        cmd = list(map(int, input().split()))
        if cmd[0] == 1:
            s = min(cmd[1], cmd[2])
            e = max(cmd[1], cmd[2])
            out.append(str(query(1, 1, n, s, e)))
            q2-=1
        elif cmd[0] == 2:
            s = min(cmd[1], cmd[2])
            e = max(cmd[1], cmd[2])
            v = cmd[3]
            update(1, 1, n, s, e, v)
            q1 -= 1
    sys.stdout.write('\n'.join(out))

if __name__ == '__main__':
    solution()