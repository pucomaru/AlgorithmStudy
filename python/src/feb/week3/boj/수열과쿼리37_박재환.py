def solution():
    import sys
    input = sys.stdin.readline

    n = int(input())
    arr = list(map(int, input().strip().split()))

    tree = [0 for _ in range(4*n)]
    def build_tree(id, l, r):
        if l == r:
            if arr[l]%2 == 0:
                tree[id]+=1
            return tree[id]

        mid = (l+r)//2
        tree[id] = build_tree(2*id, l, mid) + build_tree(2*id+1, mid+1, r)
        return tree[id]

    def update(id, l, r, s, e, v):
        if r < s or l > e:
            return
        if l >= s and r <= e:
            prev = arr[s]
            tree[id] += -1 if prev%2 == 0 else 0
            tree[id] += 1 if v % 2 == 0 else 0
            arr[s] = v
            return
        mid = (l + r) // 2
        update(2*id, l, mid, s, e, v)
        update(2*id+1, mid+1, r, s, e, v)
        tree[id] = tree[2*id] + tree[2*id+1]

    def query(id, l, r, s, e):
        if r < s or l > e:
            return 0;
        if l >= s and r <= e:
            return tree[id]
        mid = (l + r) // 2
        return query(2*id, l, mid, s, e) + query(2*id+1, mid+1, r, s, e)

    build_tree(1, 0, n-1)
    cmd_count = int(input())
    for _ in range(cmd_count):
        cmd, a, b = map(int, input().strip().split())

        if cmd == 1:
            a-=1
            update(1, 0, n-1, a, a, b)
        elif cmd == 2:
            a-=1
            b-=1
            print(query(1, 0, n-1, a, b))
        else:
            a -= 1
            b -= 1
            print((b-a+1)-query(1, 0, n - 1, a, b))

if __name__ == '__main__':
    solution()