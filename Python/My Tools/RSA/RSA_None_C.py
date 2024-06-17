# 素数分解
import rsa
import gmpy2
# 插入值 p q e n
p = 285960468890451637935629440372639283459
q = 304008741604601924494328155975272418463
e = 65537
n = 86934482296048119190666062003494800588905656017203025617216654058378322103517
fn = (p-1)*(q-1)

d = int(gmpy2.invert(e,fn))
key = rsa.PrivateKey(n,e,d,q,p)
with open(r'C:\Users\kiiz\Desktop\My Tools\test\flag.enc','rb') as f:    #文件路径
    f = f.read()
    print(rsa.decrypt(f,key))
