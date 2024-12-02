# 低加密指数攻击
import gmpy2
import os
from functools import reduce 
from Crypto.Util.number import long_to_bytes 
def CRT(items):
    N = reduce(lambda x, y: x * y, (i[1] for i in items))
    result = 0
    for a, n in items:
        m = N // n
        d, r, s = gmpy2.gcdext(n, m)
        if d != 1:
            raise Exception("Input not pairwise co-prime")
        result += a * s * m
    return result % N, N
# e, n, c
e=23
n=[63465293776825804886780705907118514318354492203844885746068763972603420624459]
c=[8415448673923521810157495017636043788928337310356253266903946044592128831391]
#########
data = list(zip(c, n))
x, n = CRT(data)
m = gmpy2.iroot(gmpy2.mpz(), e)[0].digits()
print(m)
print('m is: ' + str(long_to_bytes(m)))
