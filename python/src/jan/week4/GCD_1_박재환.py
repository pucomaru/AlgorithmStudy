def solution():
    import sys
    input = sys.stdin.readline

    n = int(input())
    result = n

    i = 2
    while i**2 <= n:
        if n % i == 0:
            while n%i == 0:
                n //= i
            result = result // i * (i-1)
        i += 1

    if n > 1:
        result = result // n * (n - 1)

    return result

if __name__ == '__main__':
    print(solution())