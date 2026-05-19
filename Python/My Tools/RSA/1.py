import gmpy2
from Crypto.Util.number import long_to_bytes


def low_exponent_attack():
    # 在这里填入题目给出的 16 进制 n 和 c
    n_hex = "0x52d483c27cd806550fbe0e37a61af2e7cf5e0efb723dfc81174c918a27627779b21fa3c851e9e94188eaee3d5cd6f752406a43fbecb53e80836ff1e185d3ccd7782ea846c2e91a7b0808986666e0bdadbfb7bdd65670a589a4d2478e9adcafe97c6ee23614bcb2ecc23580f4d2e3cc1ecfec25c50da4bc754dde6c8bfd8d1fc16956c74d8e9196046a01dc9f3024e11461c294f29d7421140732fedacac97b8fe50999117d27943c953f18c4ff4f8c258d839764078d4b6ef6e8591e0ff5563b31a39e6374d0d41c8c46921c25e5904a817ef8e39e5c9b71225a83269693e0b7e3218fc5e5a1e8412ba16e588b3d6ac536dce39fcdfce81eec79979ea6872793"
    c_hex = "0x10652cdfaa6b63f6d7bd1109da08181e500e5643f5b240a9024bfa84d5f2cac9310562978347bb232d63e7289283871efab83d84ff5a7b64a94a79d34cfbd4ef121723ba1f663e514f83f6f01492b4e13e1bb4296d96ea5a353d3bf2edd2f449c03c4a3e995237985a596908adc741f32365"

    # 将 16 进制字符串转换为整数
    n = int(n_hex, 16)
    c = int(c_hex, 16)
    e = 3

    print(f"[*] 开始爆破 k，n = {hex(n)}")

    k = 0
    while True:
        # 构造 m^e = c + k*n
        value = c + k * n

        # 使用 gmpy2.iroot 开 e 次方根，返回 (根, 是否为完全方根)
        m, is_perfect = gmpy2.iroot(value, e)

        if is_perfect:
            print(f"[+] 成功找到！k = {k}")
            # 将整数 m 转换为字节流（可见字符）
            plaintext = long_to_bytes(int(m))
            print(f"[+] 解密结果: {plaintext.decode('utf-8', errors='ignore')}")
            break

        k += 1

if __name__ == '__main__':
    low_exponent_attack()