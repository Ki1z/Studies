import zlib
import struct
filename = r"C:\Users\kiiz\Desktop\yuanshenmaomi.png"
with open(filename, 'rb') as f:
    all_b = f.read()
    #w = all_b[16:20]
    #h = all_b[20:24]
    for i in range(700,1000):  #界定宽度的范围
        name = r"D:\GitHubFiles\Studies\Python\My Tools\Misc IMG\Result\\" + str(i) + ".png"
        f1 = open(name,"wb")
        im = all_b[:16]+struct.pack('>i',i)+all_b[20:]
        f1.write(im)
        f1.close()
