from database import Database
from student import Student
from gui import GUI
from manager import Manager

def main():
    # 连接数据库
    db = Database()

    # 将数据库资源传入学生信息操作对象
    mg = Manager(db)

    # 创建GUI
    gui = GUI(mg)

if __name__ == '__main__':
    main()