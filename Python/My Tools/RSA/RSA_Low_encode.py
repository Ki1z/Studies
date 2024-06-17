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
e=3
n=[133024413746207623787624696996450696028790885302997888417950218110624599333002677651319135333439059708696691802077223829846594660086912881559705074934655646133379015018208216486164888406398123943796359972475427652972055533125099746441089220943904185289464863994194089394637271086436301059396682856176212902707]
c=[1402983421957507617092580232325850324755110618998641078304840725502785669308938910491971922889485661674385555242824]
#########
data = list(zip(c, n))
x, n = CRT(data)
m = gmpy2.iroot(gmpy2.mpz(x), e)[0].digits()
print(m)
print('m is: ' + str(long_to_bytes(m)))
