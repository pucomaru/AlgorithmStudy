from unittest import result


def solution():
    import sys
    input = sys.stdin.readline

    n = int(input())
    arr = list(map(int, input().split()))

    def get_all_combi():
        result = 0
        numbers = [False for _ in range(100001)]

        l = r = 0
        while l < n:
            """
            r 확장
            현재 l 위치에서 만들 수 있는 부분수열의 최대 위치까지 r 이동
            [l, r)
            """
            while r < n and not numbers[arr[r]]:
                numbers[arr[r]] = True
                r += 1

            """
            현재 만들 수 있는 부분수열의 개수
            (r-l)
            """
            result += (r-l)
            """
            l 을 다음 위치로 이동
            """
            numbers[arr[l]] = False
            l+=1

        return result

    return get_all_combi()

if __name__ == '__main__':
    print(solution())

