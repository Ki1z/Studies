import gmpy2
import libnum
from sympy import symbols, Eq, solve

# 假设你已经有了 n、e、c 和 p^q
n = 124121742433386095705311225695801760362852852566217717331768507632130669748480134091236138576336684757533864152825068590228777378517261386860515005283892370100986166713704470275586380471252125943853415677035145762096542879779198642539963272946667834252038809677113959263289484086409931495568491754725301103953
e = 65537
c = 43735162879827286229041206732231027010433261805209070920241032330698105490405671088538157815203632904738206904380082811045471515979643030627163910951967080132493759826955510429816541085240501175763879277080363166599603845608309063371602415631819736080068450237442573200534146133073417442448913282020534751072
r = 1965221422190883011303162401417101213884905181846738618707577668311011858223021486645058050128444366251132733357715120119284320467696315527008827310087312

# 定义符号
p = symbols('p')

# 构建方程
# n = p * (p ^ r)
# 我们需要将 p ^ r 转换为一个多项式形式
# 可以通过枚举 p 的可能值来找到满足条件的 p 和 q

# 从 sqrt(n) 开始向下搜索
sqrt_n = int(gmpy2.isqrt(n))

# 搜索 p 和 q
for p_val in range(sqrt_n, 1, -1):
    if gmpy2.is_prime(p_val):
        q_val = p_val ^ r
        if gmpy2.is_prime(q_val) and p_val * q_val == n:
            p = p_val
            q = q_val
            break
else:
    raise ValueError("Failed to recover p and q.")

# 计算 φ(n)
phi_n = (p - 1) * (q - 1)

# 计算私钥 d
d = gmpy2.invert(e, phi_n)

# 解密过程
decrypted_m = pow(c, d, n)  # 使用私钥 d 和模数 n 解密密文 c
decrypted_message = libnum.n2s(decrypted_m)  # 将解密后的数字转换回字符串

# 打印解密后的消息
print("Decrypted message:", decrypted_message)