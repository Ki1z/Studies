from Crypto.Cipher import AES
from Crypto.Util import Counter
from Crypto.Util.Padding import unpad
import hashlib

# 输入
ciphertext_hex = '288f4ebe615a0c67d83d41e2ba69a928a0e4fd1a558416d4b241be78e75d0800d7523ad626d579118fb7ce8443f06788a22fcf1a02807ea92b3cb951fb581abe'
nonce_hex = '3c43a4c91063869d'
iv_hex = '11a73ba0fcb137fbdb3de89f7db0afc5'

ct = bytes.fromhex(ciphertext_hex)
nonce = bytes.fromhex(nonce_hex)
iv = bytes.fromhex(iv_hex)

# === 第三层：CTR 解密（最外层）===
key3 = bytes([(i + 42) % 256 for i in range(16)])
# 关键：使用 Counter，initial_value=0
ctr = Counter.new(64, prefix=nonce, initial_value=0)
cipher_ctr = AES.new(key3, AES.MODE_CTR, counter=ctr)
layer2_ct = cipher_ctr.decrypt(ct)

# === 第二层：CBC 解密 ===
key2 = hashlib.sha256(b"SecondLayerKey").digest()[:16]
cipher_cbc = AES.new(key2, AES.MODE_CBC, iv)
layer1_ct = cipher_cbc.decrypt(layer2_ct)

# === 第一层：ECB 解密 ===
key1 = hashlib.md5(b"FirstLayerKey").digest()
cipher_ecb = AES.new(key1, AES.MODE_ECB)
padded_plaintext = cipher_ecb.decrypt(layer1_ct)

print(padded_plaintext)