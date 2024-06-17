from info_fetch import Info_Fetch, File_Writer

class FileDefine:
    def file_name_creator(self):
        print("> Starting...")
        num = 0
        file_list = []
        print("> Constructing files...")
        while num < 40960:
            hex_str = str(hex(num))[2:]
            if num < 16:
                hex_str = "000" + hex_str
            elif num < 256:
                hex_str = "00" + hex_str
            elif num < 4096:
                hex_str = "0" + hex_str
            file = "./webpages/www/docdetacil" + hex_str + ".html"
            file_2 = "./webpages/www/docdetacil" + hex_str + "-2.html"
            try:
                open(file)
                file_list.append(file)
                open(file_2)
                file_list.append(file_2)
            except:
                pass
            finally:
                num += 1
        print(f"> {len(file_list)} files are ready")
        return file_list
        
file = FileDefine()
file_list = file.file_name_creator()
info_fetch = Info_Fetch(file_list)
link_dict = info_fetch.get_info()
file_write = File_Writer()
file_write.file_write(link_dict)