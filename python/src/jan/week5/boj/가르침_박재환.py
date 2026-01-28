def solution():
    import sys
    sys.setrecursionlimit(10**6)
    input = sys.stdin.readline

    DEFAULT_BITS = 0
    prefix = 'anta'
    suffix = 'tica'
    # 기본 Bit 세팅
    for c in prefix:
        diff = ord(c) - ord('a')
        DEFAULT_BITS |= (1 << diff)

    for c in suffix:
        diff = ord(c) - ord('a')
        DEFAULT_BITS |= (1 << diff)

    n, k = map(int, input().split())
    # 조기 종료
    if k < 5:
        return 0
    if k == 26:
        return n

    word_bits = [None] * n
    for i in range(n):
        word = input().strip()
        word_bit = 0
        # 비트로 변환
        for c in word:
            diff = ord(c) - ord('a')
            if DEFAULT_BITS & (1 << diff) != 0:
                continue
            word_bit |= (1 << diff)
        word_bits[i] = word_bit

    max_word_count = 0
    def teach_alphabet(alpha_id, selected_id, alphabet):
        nonlocal max_word_count
        if selected_id == k-5:
            full_bit = DEFAULT_BITS | alphabet
            word_count = 0
            for word_bit in word_bits:
                if (full_bit & word_bit) == word_bit:
                    word_count += 1
            max_word_count = max(max_word_count, word_count)
            return

        for id in range(alpha_id, 26):
            if DEFAULT_BITS & (1 << id) != 0:
                continue

            teach_alphabet(id+1, selected_id+1, (alphabet | (1 << id)))

    teach_alphabet(0, 0, 0)
    return max_word_count

if __name__ == '__main__':
    print(solution())