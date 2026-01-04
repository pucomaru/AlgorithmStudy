import sys
input = sys.stdin.readline

n, q = map(int, input().split())
arr = list(map(int, input().split()))
tree = [0] * (4*n)

def make_tree(s, e, id):
    if s == e:
        tree[id] = arr[s]
        return tree[id]

    mid = (s+e)//2
    left_node = make_tree(s, mid, id*2)
    right_node = make_tree(mid+1, e, id*2+1)
    tree[id] = left_node + right_node
    return tree[id]

def query(s, e, id, l, r):
    if s > r or e < l:
        return 0

    if s >= l and e <= r:
        return tree[id]

    mid = (s+e)//2
    left_node = query(s, mid, id*2, l, r)
    right_node = query(mid+1, e, id*2+1, l, r)
    return left_node + right_node

def update(s, e, id, target_id, target_value):
    if target_id < s or target_id > e:
        return

    tree[id] = tree[id] - arr[target_id] + target_value;
    if s == e:
        return

    mid = (s+e)//2
    update(s, mid, id*2, target_id, target_value)
    update(mid+1, e, id*2+1, target_id, target_value)

def solution():
    global q, arr
    make_tree(0, n-1, 1)

    while q > 0:
        l, r, target_id, target_value = map(int, input().split())
        l-=1
        r-=1
        if r < l:
            tmp = r
            r = l
            l = tmp
        print(query(0, n-1, 1, l, r))
        target_id-=1
        update(0, n-1, 1, target_id, target_value)
        arr[target_id] = target_value
        q-=1

if __name__ == '__main__':
    solution()

