import requests

class Response:
    def __init__(self):
        r = requests.get("http://101.200.138.180:10006/get_expression")
        self.num = r.json()

    def calculate(self) -> int:
        num = self.num["expression"]
        num_list = []
        start = 0
        end = 4
        while start < len(num):
            num_list.append(int(num[start: end]))
            start += 5
            end += 5
        sub = 1
        total = num_list[0]
        for i in num:
            if i == "+":
                total += num_list[sub]
                sub += 1
            elif i == "-":
                total -= num_list[sub]
                sub += 1
            elif i == "÷":
                total /= num_list[sub]
                sub += 1
            elif i == "×":
                total *= num_list[sub]
                sub += 1
        return total
    
    def request_submit(self, param):
        key_dict = {"answer": param}
        r = requests.get("http://101.200.138.180:10006/crawler", params=key_dict)
        print(r.text)

r = Response()
value = r.calculate()
r.request_submit(value)