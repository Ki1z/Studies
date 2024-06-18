import requests, datetime

class Scanner:
    def __init__(self, url: str, dict: str, output: str) -> None:
        self.url = url if url[-1] == '/' else url + '/'
        self.dict_file = dict
        self.output = output if output[-1] == '/' else output + '/'
        global time
        time = datetime.datetime.now().replace(microsecond=0)

    def scan(self) -> list[str]:
        pass

    def out(self) -> None:
        result = self.result
        if 'http://' in self.url:
            self.url = self.url[7: -1]
        elif 'https://' in self.url:
            self.url = self.url[8: -1]
        filename = self.url.replace('.', '-').replace(':', '-') + '_' + str(time.time()).replace(':', '-') + '.txt'
        f = open(self.output + filename, 'w', encoding='utf-8')
        for line in result:
            f.write(line + '\n')
        f.close()
        print(f'[{time.time()}]> The output has generated in file "{filename}"')

class Default(Scanner):
    def scan(self) -> list[str]:
        result = []
        try:
            dict_file = open('./dirscan_dict/' + self.dict_file, 'r', encoding='utf-8')
            dict = dict_file.readlines()
            dict_file.close()
        except:
            print(f'[{time.time()}]> Error: The dictionary is not exist')
            exit()
        for i in dict:
            i = i.strip()
            response = str(requests.get(self.url + i))
            if response == '<Response [200]>':
                result.append(self.url + i + '    200 OK')
                print(f'[{time.time()}]> {i}    200 OK')
        self.result = result
    
class Length(Scanner):
    def scan(self) -> list[str]:
        result = []
        try:
            dict_file = open('./dirscan_dict/' + self.dict_file, 'r', encoding='utf-8')
            dict = dict_file.readlines()
            dict_file.close()
        except:
            print(f'[{time.time()}]> Error: The dictionary is not exist')
            exit()
        base = len(requests.get(self.url + 'index.php').content)
        print(f'[{time.time()}]> The basic length of "index.php" is {base}')
        for i in dict:
            i = i.strip()
            code = requests.get(self.url + i)
            response = len(code.content)
            if not(response == base) and str(code) == '<Response [200]>':
                result.append(self.url + i + f'    {response}')
                print(f'[{time.time()}]> {i}    {response}')
        self.result = result
    
class Cookie(Scanner):
    def scan(self) -> list[str]:
        result = []
        try:
            dict_file = open('./dirscan_dict/' + self.dict_file, 'r', encoding='utf-8')
            dict = dict_file.readlines()
            dict_file.close()
        except:
            print(f'[{time.time()}]> Error: The dictionary is not exist')
            exit()
        for i in dict:
            i = i.strip()
            code = requests.get(self.url + i)
            response = str(code.cookies)
            if response == '<RequestsCookieJar[]>' and str(code) == '<Response [200]>':
                result.append(self.url + i)
                print(f'[{time.time()}]> {i}')
        self.result = result