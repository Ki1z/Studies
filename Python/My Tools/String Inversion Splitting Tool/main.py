class Main:
    def isset(self, file: str) -> bool:
        try:
            open("./string/" + file)
            return True
        except:
            return False
    
    def start(self) -> str:
        print("> String Inversion Splitting Tool Ver 0.1")
        while 1:
            file = input("> please input file name:\n")
            if self.isset(file):
                return file
            else:
                print(f"> The file {file} is not exist")

class File_handling:
    def __init__(self, file: str):
        self.file = file

    def str_get(self) -> str:
        f = open("./string/" + self.file, "r", encoding="utf-8")
        data = f.read()
        f.close()
        print(f"> the contents of {self.file} are [{data}]")
        return data

    def str_mark(self, data: str) -> str:
        next_step = input("> Do you need to mark the special characters?(y/n)\n")
        if next_step == 'n' or next_step == 'N':
            return data
        while 1:
            mark = input("> Please input the character that you want to add in front of the others:\n")
            if len(mark) > 1:
                print("> You cannot use more than 1 character")
            else:
                break
        f = open("./special_characters.txt", "r", encoding="utf-8")
        special_character = f.read().split(",")
        f.close()
        print(f"> All the special characters you want to mark are [{special_character}]")
        data = list(data)
        sub = 0
        while sub < len(data):
            need_mark = False
            for i in special_character:
                if data[sub] == i:
                    need_mark = True
            if need_mark:
                data.insert(sub, mark)
                sub += 2
            else:
                sub += 1
        data = "".join(data)
        print(f"> Done, the marked string is [{data}]")
        return data
        
    def str_split(self, data: str) -> list:
        num = int(input("> How many characters do you need to split into groups:\n")) - 3
        data_list = []
        start = 0
        while start < len(data):
            end = start + num
            unit = data[start: end:]
            if unit[-1] == "\\":
                unit = data[start: end - 1:]
                start += num - 1
            else:
                start += num
            data_list.insert(0, unit)
        print(f"> The splitted string are {data_list}")
        return data_list
        
    def str_write(self, data: list):
        f = open("./payload.txt", "w", encoding="utf-8")
        first = 1
        for i in data:
            if first == 1:
                f.write(">" + i + "\n")
                first = 0
            else:
                f.write(">" + i + "\\\\\n")
        f.close()
        print("> Done, your payload has generated")

    def start(self):
        data = self.str_get()
        data = self.str_mark(data)
        data = self.str_split(data)
        self.str_write(data)

if __name__ == '__main__':
    main = Main()
    file = main.start()
    file_handling = File_handling(file)
    file_handling.start()