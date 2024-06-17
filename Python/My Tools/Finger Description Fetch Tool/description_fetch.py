import json
import time

class FileOperator:
    def __init__(self, opts: list[tuple]):
        self.file = opts[0][1]

    def file_reader(self) -> list[str]:
        '''
        :   文件读取方法
        :   return list[str] 返回读取到的字符串列表
        '''
        print("> Reading File...")
        f = open("./jsons/" + self.file, "r", encoding="utf-8")
        content = f.read()
        f.close()
        data = json.loads(content)["AssetName"]
        data_list = []
        for i in data:
            data_list.append(data[i]["description"])
        print("> Done")
        return data_list
    
    def file_writer(self, data) -> str:
        '''
        :   文件写入方法
        :   return str 返回文件名
        '''
        print("> Output Writing...")
        file = "./output/output_" + str(int(time.time())) + ".txt"
        f = open(file, "a", encoding="utf-8")
        for i in data:
            f.write(i + "\n")
        f.close()
        return file[9:]