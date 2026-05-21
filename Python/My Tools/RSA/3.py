import struct
import os


def check_file_type(filename):
    with open(filename, 'rb') as f:
        magic = f.read(4)

    if magic[:2] == b'MZ':
        print("[+] Windows PE 文件")
    elif magic == b'\x7fELF':
        print("[+] Linux ELF 文件")
        # 检查32/64位
        with open(filename, 'rb') as f:
            f.seek(4)
            bits = f.read(1)
            if bits == b'\x01':
                print("   - 32位程序")
            elif bits == b'\x02':
                print("   - 64位程序")
    else:
        print("[-] 未知文件格式")


check_file_type(r"C:\Users\kiiz\Desktop\re1.exe")