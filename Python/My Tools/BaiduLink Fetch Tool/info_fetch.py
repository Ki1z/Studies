import time

class Info_Fetch:
    def __init__(self, file_list: list[str]):
        self.file_list = file_list

    def get_info(self) -> dict[str:str]:
        print("> Fetching info...")
        output_dict = {}
        for file in self.file_list:
            f = open(file, "r", encoding="utf-8")
            data = f.readlines()
            f.close()
            title = data[8][7:-18]
            output_dict[title] = ""
            for line in data:
                sub = line.find('"FuzhiCK')
                if sub < 0:
                    continue
                sub = line.find("原版的气氛与乐趣有了更大的提升")
                if not(sub == -1):
                    continue
                line = line[46:-5]
                line = line.strip("链接: ")
                output_dict[title] = line
        print(f"> {len(output_dict)} links have been found")
        return output_dict
    
class File_Writer:
    def file_write(self, link_dict: dict[str: str]):
        print("> Generating output...")
        file = "./outputs/output_" + str(int(time.time())) + ".txt"
        f = open(file, "a", encoding="utf-8")
        for i in link_dict:
            f.write(f"{i}\n{link_dict[i]}\n\n")
        f.close()
        print(f"> Done, your output is {file[10:]}")