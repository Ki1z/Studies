import zlib
import struct
filename = r"C:\Users\kiiz\Desktop\My Tools\Misc IMG\IMG\8c24a4f82e07cae382c5281479ce739.png"
with open(filename, 'rb') as f:
    all_b = f.read()
    #w = all_b[16:20]
    #h = all_b[20:24]
    for i in range(901,1200):  #界定宽度的范围
        name = r"C:\Users\kiiz\Desktop\My Tools\Misc IMG\Result\\" + str(i) + ".png"
        f1 = open(name,"wb")
        im = all_b[:16]+struct.pack('>i',i)+all_b[20:]
        f1.write(im)
        f1.close()
