import sys


def solution():
    import sys
    sys.setrecursionlimit(10 ** 6)
    input = sys.stdin.readline

    max_fragile_eggs = 0
    egg_count = int(input().strip())
    eggs = [list(map(int, input().split())) for _ in range(egg_count)]

    def crash_egg(hold_idx):
        nonlocal max_fragile_eggs

        if hold_idx == egg_count:
            fragile_eggs = 0

            for hard, weight in eggs:
                if hard < 1:
                    fragile_eggs += 1

            max_fragile_eggs = max(max_fragile_eggs, fragile_eggs)
            return

        if eggs[hold_idx][0] < 1:       # 현재 들고 있는 계란이 깨진 경우
            crash_egg(hold_idx+1)
            return

        # 계란으로 계란을 칠 수 있는 경우
        has_next = False

        for target_idx in range(egg_count):
            if target_idx == hold_idx:      # 자기 자신을 칠 수 없음
                continue
            if eggs[target_idx][0] < 1:     # 이미 계란이 깨져있는 경우
                continue

            # 칠 수 있음
            has_next = True
            eggs[hold_idx][0] -= eggs[target_idx][1]
            eggs[target_idx][0] -= eggs[hold_idx][1]
            crash_egg(hold_idx+1)
            eggs[hold_idx][0] += eggs[target_idx][1]
            eggs[target_idx][0] += eggs[hold_idx][1]

        if not has_next:
            crash_egg(hold_idx+1)

    crash_egg(0)

    return max_fragile_eggs

if __name__ == "__main__":
    print(solution())