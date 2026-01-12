import sys
input = sys.stdin.readline

INF = 16_000_001

n = int(input().strip())
FULL = (1<<n)-1
arr = [list(map(int, input().split())) for _ in range(n)]
cost_arr = [[-1]*(1<<n) for _ in range(n)]


def find_min_cost(cur, visited):
    if visited == FULL:     # 모두 방문한 경우
        return INF if arr[cur][0] == 0 else arr[cur][0]

    if cost_arr[cur][visited] != -1:
        return cost_arr[cur][visited]

    cost_arr[cur][visited] = INF

    for next in range(n):
        if visited & (1 << next) != 0:
            continue
        if arr[cur][next] == 0:
            continue

        total_cost = arr[cur][next] + find_min_cost(next, visited | (1 << next))
        cost_arr[cur][visited] = min(cost_arr[cur][visited], total_cost)

    return cost_arr[cur][visited]

def solution():
    return find_min_cost(0, 1)

if __name__ == "__main__":
    print(solution())