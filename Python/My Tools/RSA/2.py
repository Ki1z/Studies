def bytes_to_long(b):
    # 将字节转为整数
    return int.from_bytes(b, byteorder='big')

def long_to_bytes(n, length=None):
    # 将整数转回字节，确保长度正确
    if length is None:
        length = (n.bit_length() + 7) // 8
    return n.to_bytes(length, byteorder='big')

# 假设 m1 已知
m1 = 98070044311371556054042332561937893752308958397904918802563297726292227020432617954869045913212860026910098537038380048521688973606036605846779856823126712600452511179357789946005406008647373168812879294641383207114439538201704420125195493064766081392841317137251853058381858922383533597111104373163076665926     # 这里是示例值，你需要替换为实际的 m1 值

# 将 m1 转换为字节形式
m1_bytes = long_to_bytes(m1)

# 假设 flag 长度的一半（例如 32 字节）
half_length = 32  # 这个值需要根据实际情况调整

# 分离出实际的 real_flag 和 random_bytes
real_flag_bytes = m1_bytes[:half_length]
random_bytes = m1_bytes[half_length:]

print("Real flag bytes:", real_flag_bytes)

# 从字节形式转换回字符串（假设 flag 是以 ASCII 编码的形式存储）
flag_str = real_flag_bytes.decode('ascii')
print("Recovered flag:", flag_str)