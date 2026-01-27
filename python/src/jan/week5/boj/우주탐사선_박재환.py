def solution():
    import sys
    input = sys.stdin.readline

    INF = 10 * 1000 + 5
    planet_count, launch_planet = map(int, input().split())
    planet_dist = [[INF] * planet_count for _ in range(planet_count)]
    for i in range(planet_count):
        arr = list(map(int, input().split()))
        for j in range(planet_count):
            planet_dist[i][j] = arr[j]

    def find_all_dist():
        for mid in range(planet_count):
            for start in range(planet_count):
                for end in range(planet_count):
                    planet_dist[start][end] = (
                        min(planet_dist[start][end],
                            planet_dist[start][mid] + planet_dist[mid][end])
                    )
    # 플로이드 워셜로 모든 경우의 수 구하기
    find_all_dist()
    ALL = (1 << planet_count) - 1
    dp_arr = [[-1] * (1 << planet_count) for _ in range(planet_count)]
    def find_min_cost(cur, visited):
        if visited == ALL:
            return 0
        if dp_arr[cur][visited] != -1:
            return dp_arr[cur][visited]
        min_cost = INF
        for next in range(planet_count):
            if (visited & (1 << next)) == 0:
                cost = (planet_dist[cur][next] +
                        find_min_cost(next, (visited | (1 << next))))

                min_cost = min(min_cost, cost)

        dp_arr[cur][visited] = min_cost
        return dp_arr[cur][visited]

    return find_min_cost(launch_planet, (1 << launch_planet))

if __name__ == '__main__':
    print(solution())