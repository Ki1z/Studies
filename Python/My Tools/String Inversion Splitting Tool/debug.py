data = "php.a>d- 46esab|=sTKdFzWUV0RfRCKjVGel9FbsVGazByboNWZgAHaw9DP ohce"
mark = "\\"
f = open("./special_characters.txt", "r", encoding="utf-8")
special_character = f.read().split(",")
f.close()
print(f"> All the special characters you want to mark are [{special_character}]")
data = list(data)
sub = 0
while sub < len(data):
    need_mark = False
    for i in special_character:
        if data[sub] == i:
            need_mark = True
    if need_mark:
        data.insert(sub, mark)
        sub += 2
    else:
        sub += 1
data = "".join(data)
print(f"> Done, the marked string is [{data}]")