import base64

f = open("./1.txt", "r", encoding="utf-8")
content = f.readlines()
f.close()
f = open("./output.txt", "w", encoding="utf-8")
for line in content:
    line = line.strip()
    while 1:
        try:
            base64.b64decode(line)
            break
        except:
            line += "="
    f.write(line + "\n")
f.close()