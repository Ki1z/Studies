import pymysql, hashlib, json
from database import Database
from student import Student

class Manager:
    
    def __init__(self, db: Database) -> None:
        self.db = db

    # 查询方法
    def query(self, query_mode: str, comparison_mode: str, query_content: str) -> tuple[tuple]:
        # query_mode = ('id', 'name', 'gender', 'major', 'class', 'status', 'init')
        # comparison_mode = ('>', '<', '=')
        sql = ''

        if query_mode == 'init':
            sql = 'SELECT * FROM student_info'
        else:
            sql = f'SELECT * FROM student_info WHERE stu_{query_mode} {comparison_mode} "{query_content}"'

        try:
            data = self.db.query(sql)
            return data
        except pymysql.err.Error as e:
            exit(e)

    # 删除方法
    def delete(self, stu_id: str) -> bool:
        sql = f'DELETE FROM student_info WHERE stu_id = "{stu_id}"'

        try:
            self.db.query(sql)
            return True
        except pymysql.err.Error as e:
            print(e)
            return False
    
    # 插入方法
    def insert(self, student: Student) -> bool:
        sql = f"INSERT INTO student_info VALUES('{student.stu_id}','{student.stu_name}','{student.stu_gender}','{student.stu_major}','{student.stu_class}','{student.stu_birth}','{student.stu_phone}','{student.stu_status}','{json.dumps(student.stu_grades, ensure_ascii=False)}')"
        
        try:
            self.db.query(sql)
            return True
        except pymysql.err.Error as e:
            print(e)
            return False


    # 更新方法
    def update(self, stu_id: str, edit_opt: str, edit_content: str) -> bool:
        sql = f'UPDATE student_info SET {edit_opt} = "{edit_content}" WHERE stu_id = "{stu_id}"'

        try:
            self.db.query(sql)
            return True
        except pymysql.err.Error as e:
            return False

    # 登陆方法
    def login(self, user: str, passwd: str) -> bool:
        sql = f'SELECT password FROM users WHERE username = "{user}"'
        md5 = hashlib.md5()
        md5.update(passwd.encode('utf-8'))
        passwd = md5.hexdigest()

        try:
            passwd_in_dbs = self.db.query(sql)[0][0]

            if passwd_in_dbs == passwd:
                return True
            else:
                return False
        except:
            return False

    # 注册方法
    def register(self, user: str, passwd: str) -> bool:
        md5 = hashlib.md5()
        md5.update(passwd.encode('utf-8'))
        passwd = md5.hexdigest()

        try:
            self.db.query(f'INSERT INTO users VALUES(0, "{user}", "{passwd}")')
            
            return True
        except:
           return False

    # 创建学生对象方法    
    def student_info_To_object(self, student_info: tuple[tuple]) -> list[Student]:
        student_list = []

        for i in student_info:
            student = Student(str(i[0]), str(i[1]), str(i[2]), str(i[3]), str(i[4]), str(i[5]), str(i[6]), str(i[7]), json.loads(i[8]))
            student_list.append(student)
        
        return student_list
    
    # 日期检错方法
    def is_valid_date(self, new_year: str, new_month: str, new_date: str) -> bool:  
        try:   
            year = int(new_year)  
            month = int(new_month)  
            day = int(new_date)  
        except ValueError:   
            return False  
 
        if year <= 0:  
            return False  
        
        if not (1 <= month <= 12):  
            return False  
        
        if month == 2:  
            if day < 1 or (day > 28 and (year % 4 != 0 or (year % 100 == 0 and year % 400 != 0))):  
                return False  
        elif month in [4, 6, 9, 11]: 
            if day < 1 or day > 30:  
                return False  
        elif day < 1 or day > 31:
            return False  
            
        return True