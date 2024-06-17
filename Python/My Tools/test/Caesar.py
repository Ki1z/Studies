str = "MTHJ{CUBCGXGUGXWREXIPOYAOEYFIGXWRXCHTKHFCOHCFDUCGTXZOHIXOEOWMEHZO}"
num = [25, 24, 25, 29]
sub = 0
str1 = ""
for i in str:
    if i == "{" or i == "}":
        str1 += i
        continue
    if sub > 3:
        sub = 0
    i = chr(ord(i) + num[sub])
    str1 += i
    sub += 1
print(str1)