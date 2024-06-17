import sys, getopt
from description_fetch import FileOperator

class Menu:
    def start(self):
        '''主菜单'''
        print("###############################################")
        print("#                                             #")
        print("#  Finger Description Fetch Tool Version 0.2  #")
        print("#                                             #")
        print("#                            Made By Ki1z     #")
        print("#                                             #")
        print("###############################################")
        print(">")
        print("> It is a tool that can fetch the descriptions of fingers in a json file, and write them into a txt file")
        print(">")
        print("> Opts:")
        print(">")
        print("> -o: the name of Json file")
        print(">")

    def put_in(self) -> list[tuple]:
        '''
        :   接收输入方法
        :   return list[tuple] 返回接收到的选项与参数列表
        '''
        try:
            opts, args = getopt.getopt(sys.argv[1:], "o:")
        except getopt.GetoptError as e:
            print(f"ERROR: {e}")
            sys.exit()
        if opts == [] and args == []:
            self.start()
            sys.exit()
        elif opts == [] and not(args == []):
            print("ERROR: Not specified Opts")
            sys.exit()
        return opts

    def __init__(self):
        self.put_in()

class Path_Check:
    def __init__(self, opts):
        self.opts = opts

    def check(self) -> bool:
        '''
        :   文件校验方法
        :   return bool 返回校验结果
        '''
        file = self.opts[0][1]
        try:
            open("./jsons/" + file)
            return True
        except:
            print("ERROR: Not a valid Json file")
            sys.exit()

menu = Menu()
opts = menu.put_in()
check = Path_Check(opts)
if check.check():
    operator = FileOperator(opts)
    data = operator.file_reader()
    file = operator.file_writer(data)
    print("> Done")
    print(f"> Output has been generated in {file}")