import zlib
import struct
filename = r"C:\Users\kiiz\Desktop\My Tools\Misc IMG\IMGmisc36.gif"
with open(filename, 'rb') as f:
    all_b = f.read()
    for i in range(920,951):
        name = r"C:\Users\kiiz\Desktop\My Tools\Misc IMG\Result\\" + str(i) + ".gif"
        f1 = open(name,"wb")
        im = all_b[:38]+struct.pack('>h',i)[::-1]+all_b[40:]
        f1.write(im)
        f1.close()
