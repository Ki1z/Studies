import requests, datetime, argparse

def get_input() -> object:
    parser = argparse.ArgumentParser()
    parser.add_argument('-U', type=str, required=True, help='target URL')
    parser.add_argument('-D', type=str, default='default.txt', help='the dictionary to be used, default=default.txt')
    parser.add_argument('-M', type=str, default='Default' , choices=['Default', 'Differ'], help='the mode of dirscan, Default is to collect all the responses with http code 200, Differ is to collect the responses with a different content-length from index page')
    try:
        args = parser.parse_args()
    except:
        exit()
    return args                                                                                                         

def confirm_request(url: str, file: str) -> tuple[str, list[str]]:
    try:
        dict_file = open('./dirscan_dict/' + file, 'r', encoding='utf-8')
        dict = dict_file.readlines()
        dict_file.close()
    except:
        print(f'[{time.time()}]> Error: The dictionary is not exist')
        exit()
    if not(url[-1] == '/'):
        url += '/'
    print(f'[{time.time()}]> URL: {url}')
    print(f'[{time.time()}]> Dictionary: {file}')
    return url, dict

def request(url: str, dict: list[str], mode: str):
    file_name = url[7: -1].replace('.', '-').replace(':', '-') + '_' + str(time.time()).replace(':', '-')
    file = open(f'./dirscan_dict/output/{file_name}.txt', 'w', encoding='utf-8')
    if mode == 'Differ':
        length = len(requests.get(url + 'index.php').content)
        print(f'[{time.time()}]> The length of index.php is {length}')
        for i in dict:
            i = i.strip()
            response = requests.get(url + i)
            if not(str(response) == '<Response [200]>'):
                continue
            if not(len(response.content) == length):
                print(f'[{time.time()}]> {i}    {len(response.content)} OK')
                file.write(url + i + '\n')
    else:
        for i in dict:
            i = i.strip()
            response = str(requests.get(url + i))
            if response == '<Response [200]>':
                print(f'[{time.time()}]> {i}    200 OK')
                file.write(url + i + '\n')
    print(f'[{time.time()}]> Output has generated in file "./dirscan_dict/output/{file_name}.txt"')
    file.close()

if __name__ == '__main__':
    global time
    time = datetime.datetime.now().replace(microsecond=0)
    target = get_input()
    url = target.U
    file = target.D
    mode = target.M
    url, dict = confirm_request(url, file)
    request(url, dict, mode)
