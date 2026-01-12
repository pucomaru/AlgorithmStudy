import sys
sys.setrecursionlimit(10**7)        # 재귀 호출 횟수 제한
input = sys.stdin.readline

n, r, q = map(int, input().split())
graph = [[] for _ in range(n+1)]
nodes = [1] * (n+1)

for _ in range(n-1):
    a, b = map(int, input().split())
    graph[a].append(b)
    graph[b].append(a)

def get_sub_tree_size(prev, cur):
    for conn in graph[cur]:
        if prev == conn:        # 사이클 판별
            continue

        get_sub_tree_size(cur, conn)
        nodes[cur] += nodes[conn]

def solution():
    get_sub_tree_size(0, r)

    for _ in range(q):
        node = int(input())
        print(nodes[node])

if __name__ == '__main__':
    solution()