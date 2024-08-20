import pymysql
from tkinter import messagebox

class Database:
    
    def __init__(self) -> None:
        # 连接数据库
        try:
            self.db = pymysql.connect(
                host = 'localhost',
                user = 'root',
                password = 'root',
                database = 'student_info'
            )
        except pymysql.err.Error as e:
            # 无法连接到数据库
            messagebox.showerror('错误！', '数据库连接失败，请检查数据库启动情况')
            exit(e)
        
    # 查询方法
    def query(self, sql: str) -> tuple[tuple]:
        cursor = self.db.cursor()
        cursor.execute(sql)
        data = cursor.fetchall()
        print(data)

        return data