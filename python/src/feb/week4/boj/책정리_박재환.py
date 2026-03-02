def solution():
    import sys
    input = sys.stdin.readline

    n = int(input())
    arr = list(map(int, input().split()))

    def find_insert_id(num, lis):
        l = 0
        r = len(lis)

        while l < r:
            mid = (l + r) // 2
            if num <= lis[mid]:
                r = mid
            else:
                l = mid + 1

        return l

    def find_min_cost():
        lis = []

        for num in arr:
            insert_id = 0 if len(lis) == 0 else find_insert_id(num, lis)
            if len(lis) == insert_id:
                lis.append(num)
            else:
                lis[insert_id] = num

        return len(lis)

    return n-find_min_cost()

if __name__ == '__main__':
    print(solution())
