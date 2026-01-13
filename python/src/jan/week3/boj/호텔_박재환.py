def solution():
    import sys
    input = sys.stdin.readline

    INF = 100 * 1000 + 1
    SEARCH_RANGE = 2001

    least_person, city_count = map(int, input().split())
    cities = [list(map(int, input().split())) for _ in range(city_count)]

    def find_min_cost():
        cost_per_person = [INF] * SEARCH_RANGE
        cost_per_person[0] = 0

        for city in cities:
            for person in range(city[1], SEARCH_RANGE):
                cost_per_person[person] = min(cost_per_person[person], cost_per_person[person-city[1]] + city[0])

        return min(cost_per_person[least_person:])

    return find_min_cost()
if __name__ == "__main__":
    print(solution())