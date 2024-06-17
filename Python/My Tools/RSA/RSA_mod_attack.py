# 共模攻击
from gmpy2 import invert
import binascii
def gongmo(n, c1, c2, e1, e2):
    def egcd(a, b):
        if b == 0:
            return a, 0
        else:
            x, y = egcd(b, a % b)
            return y, x - (a // b) * y
    s = egcd(e1, e2)
    s1 = s[0]
    s2 = s[1]

    # 求模反元素
    if s1 < 0:
        s1 = - s1
        c1 = invert(c1, n)
    elif s2 < 0:
        s2 = - s2
        c2 = invert(c2, n)
    m = pow(c1, s1, n) * pow(c2, s2, n) % n
    return m
# 此处插入值c1 n e1 c2 e2
c1=1
n=1
e1=1
c2=1
e2=1
#########################
result = gongmo(n, c1, c2, e1, e2)
print(result)

print(binascii.unhexlify(hex(result)[2:].strip("L")))