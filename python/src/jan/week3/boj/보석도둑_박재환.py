def solution():
    import sys
    input = sys.stdin.readline

    n, k = map(int, input().split())
    diamonds = [list(map(int, input().split())) for _ in range(n)]
    bags = []
    for _ in range(k):
        bags.append(int(input()))

    def get_max_price():
        nonlocal  diamonds, bags
        import heapq as hq
        h = []

        diamonds.sort(key=lambda x: x[0])   # 무게가 가벼운 순
        bags.sort()

        total_price = 0
        idx = 0
        for bag in bags:
            while idx < n and diamonds[idx][0] <= bag:
                hq.heappush(h, -diamonds[idx][1])
                idx += 1

            if h:
                total_price += -hq.heappop(h)

        return total_price

    return get_max_price()

if __name__ == "__main__":
    print(solution())

