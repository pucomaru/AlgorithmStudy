def solution():
    import sys
    input = sys.stdin.readline

    card_count, pick_count, turn_count = map(int, input().split())
    cards = list(map(int, input().split()))

    # Union-Find
    parents = [None]*(pick_count+1)
    def make():
        nonlocal parents
        for i in range(pick_count+1):
            parents[i] = i

    def find(a):
        if parents[a] == a:
            return a;

        parents[a] = find(parents[a])
        return parents[a]

    def union(a, b):
        root_a = find(a)
        root_b = find(b)

        if root_a == root_b:
            return

        parents[root_a] = root_b

    # Binary Search
    def find_upper_bound(target):
        l = 0
        r = pick_count

        while(l < r):
            mid = (l+r) // 2
            if cards[mid] > target:
                r = mid
            else:
                l = mid+1

        return l

    # ----------------------------------

    cards.sort()
    # 1. Union Find 만들기
    make()
    turn = list(map(int, input().split()))
    for card in turn:
        upper_idx = find_upper_bound(card)
        result = find(upper_idx)
        print(cards[result])
        union(result, result+1)

if __name__ == '__main__':
    solution()