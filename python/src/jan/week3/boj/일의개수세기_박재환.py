import sys
input = sys.stdin.readline

a, b = map(int, input().split())

def get_one_bits(num, k):
    one_bit_len = 1 << k
    full_len = 1 << (k+1)

    total_num_count = num+1
    repeat_group = total_num_count // full_len
    remain = total_num_count % full_len
    one_bit_count = one_bit_len * repeat_group
    one_bit_count += max(0, remain - one_bit_len)

    return one_bit_count

def get_one_bit_count(num):
    sum = 0
    for k in range(55):
        sum += get_one_bits(num, k)
    return sum

def solution():
    return get_one_bit_count(b) - get_one_bit_count(a-1)

if __name__ == '__main__':
    print(solution())