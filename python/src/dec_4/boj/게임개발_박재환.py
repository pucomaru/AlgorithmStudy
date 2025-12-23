
def main():
    import sys
    input = sys.stdin.readline

    n = int(input())
    buildings = [None] * (n+1)
    for id in range(1, n+1):
        data = list(map(int, input().split()))
        time = data[0]
        prev = data[1:-1]
        buildings[id] = {
            "time": time,
            "prev": prev,
            "later": [],
            "in_edge": len(prev)
        }

    build_sequence(buildings, n)


def build_sequence(buildings, n):
    from collections import deque
    q = deque()

    # 연결관계 표시
    for id in range(1, n + 1):
        prev = buildings[id]["prev"]
        for i in prev:
            buildings[i]["later"].append(id)
        if len(prev) == 0:
            q.append(id)

    seq = [0]
    while q:
        building_id = q.popleft()
        for id in buildings[building_id]["later"]:
            buildings[id]["in_edge"] -= 1
            if buildings[id]["in_edge"] == 0:
                q.append(id)
        seq.append(building_id)

    get_real_time(buildings, seq)

def get_real_time(buildings, seq):
    result = [0] * len(seq)
    for id in range(1, len(seq)):
        building_id = seq[id]
        if id == 0:
            result[id] = buildings[building_id]["time"]
            continue
        max_time = 0
        for i in buildings[building_id]["prev"]:
            max_time = max(max_time, result[i])
        result[building_id] = max_time + buildings[building_id]["time"]

    for time in result[1:]:
        print(time)

if __name__ == "__main__":
    main()