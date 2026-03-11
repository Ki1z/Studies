import requests

url = "http://jw.qit.edu.cn/jwglxt/xtgl/login_slogin.html"

# 设置cookie
cookies = {
    "JSESSIONID": "2C4B81A97330FE88B21B9F7DFE57C620",
    "route": "b2df44e3bdf3fa8b616086894e2666ab"
}

# 发送请求
response = requests.get(url, cookies=cookies)
print(response.text)