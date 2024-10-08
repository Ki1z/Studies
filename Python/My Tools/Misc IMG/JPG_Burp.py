import zlib
import struct
filename = r"D:\GitHubFiles\Studies\Python\My Tools\Misc IMG\IMG\test1.jpg"
with open(filename, 'rb') as f:
    all_b = f.read()
    #w = all_b[159:161]
    #h = all_b[157:159]
    for i in range(900, 1300): #界定宽度范围
        name = r"D:\GitHubFiles\Studies\Python\My Tools\Misc IMG\Result\\" + str(i) + ".jpg"
        f1 = open(name, "wb")
        im = all_b[: 159]+struct.pack('>h', i) + all_b[161:]
        f1.write(im)
        f1.close()
