def solution():
    import sys
    input = sys.stdin.readline

    n = int(input())
    persons = [list(map(int, input().split())) for _ in range(n)]
    d = int(input())

    def get_max_include_person():
        import heapq

        candidate_person = []
        for a, b in persons:
            start = min(a, b)
            end = max(a, b)

            if end - start > d:
                continue

            candidate_person.append((start, end))

        # end 순으로 오름차순 정렬
        candidate_person.sort(key = lambda p: p[1])

        hq = []

        max_person = 0
        for start, end in candidate_person:
            rail_start = end - d
            heapq.heappush(hq, start)

            while hq and hq[0] < rail_start:
                heapq.heappop(hq)

            max_person = max(max_person, len(hq))

        return max_person

    return get_max_include_person()


if __name__ == "__main__":
    print(solution())