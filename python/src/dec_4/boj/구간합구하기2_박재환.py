tree = []
lazy = []

def main():
    import sys
    input = sys.stdin.readline

    global tree, lazy

    n, m, k = map(int, input().split())
    arr = list(int(input()) for _ in range(n))

    tree = [0] * (4*n)
    lazy = [0] * (4*n)
    init_tree(0, n-1, 1, arr)
    total_command_count = m + k
    for _ in range(total_command_count):
        data = list(map(int, input().split()))

        match data[0]:
            case 1:
                l, r, add_value = data[1]-1, data[2]-1, data[3]
                update_tree(0, n-1, 1, l, r, add_value)
            case 2:
                l, r = data[1]-1, data[2]-1
                print(query(0, n-1, 1, l, r))

def init_tree(s, e, idx, arr):
    global tree

    if s==e:
        tree[idx] = arr[s]
        return tree[idx]
    mid = s + (e-s)//2
    l = init_tree(s, mid, idx*2, arr)
    r = init_tree(mid+1, e, idx*2+1, arr)
    tree[idx] = l + r
    return tree[idx]

def update_tree(s, e, idx, l, r, add_value):
    global tree, lazy

    if s > r or e < l:
        return
    if s >= l and e <= r:
        lazy[idx] += add_value
        return

    # 채워주기
    l_max = max(s, l)
    r_min= min(r, e)
    tree[idx] += (add_value * (r_min-l_max+1))

    mid = s + (e-s)//2
    update_tree(s, mid, idx*2, l, r, add_value)
    update_tree(mid+1, e, idx*2+1, l, r, add_value)

def query(s, e, idx, l, r):
    global tree, lazy
    if s > r or e < l:
        return 0

    # lazy 전파 및 반영
    tree[idx] += ((e-s+1) * lazy[idx])
    if s != e:
        lazy[idx*2] += lazy[idx]
        lazy[idx*2+1] += lazy[idx]
    lazy[idx] = 0

    if s >= l and e <= r:
        return tree[idx]

    mid = s + (e-s)//2
    l_range = query(s, mid, idx*2, l, r)
    r_range = query(mid+1, e, idx*2+1, l, r)
    return l_range + r_range

if __name__ == '__main__':
    main()