import tkinter, time, os
from tkinter import messagebox
from tkinter import ttk
from manager import Manager
from student import Student
from datetime import date

class GUI:

    def __init__(self, mg: Manager) -> None:
        self.mg = mg

        self.login_ui()

    # 登录界面
    def login_ui(self) -> None:
        self.login_tk = tkinter.Tk()
        self.login_tk.title('登录')
        self.login_tk.geometry('400x200')

        # 欢迎词
        login_welcome_text = tkinter.Label(
            text='学生信息管理系统',
            font=('等线', 20)
        )
        login_welcome_text.place(x=88, y=20)

        # 用户名输入框提示
        username_display = tkinter.Label(
            text='用户名：',
            font=('等线', 16)
        )
        username_display.place(x=40, y=70)

        # 用户名输入框
        self.username = tkinter.StringVar()
        username_entry = tkinter.Entry(
            show=None,
            font=('等线', 16),
            textvariable=self.username
        )
        username_entry.place(x=130, y=70)

        # 密码输入框提示
        passwd_display = tkinter.Label(
            text='密码：',
            font=('等线', 16)
        )
        passwd_display.place(x=50, y=110)

        # 密码输入框
        self.passwd = tkinter.StringVar()
        passwd_entry = tkinter.Entry(
            show='*',
            font=('等线', 16),
            textvariable=self.passwd
        )
        passwd_entry.place(x=130, y=110)

        # 登录按钮
        login_button = tkinter.Button(
            text='登录',
            font=('等线', 12),
            command=self.login
        )
        login_button.place(x=120, y=150)

        # 注册按钮
        register_button = tkinter.Button(
            text='注册',
            font=('等线', 12),
            command=self.register_ui
        )
        register_button.place(x=220, y=150)

        self.login_tk.mainloop()

    # 登录方法
    def login(self) -> None:
        username = self.username.get()
        passwd = self.passwd.get()

        if username == '' or passwd == '':
            # 用户名或密码为空
            messagebox.showwarning('登陆失败！', '用户名或密码不能为空！')
        elif self.mg.login(username, passwd):
            # 登录成功
            messagebox.showinfo('登陆成功！', f'欢迎 {username} 登录系统')
            self.login_tk.destroy()
            self.main_ui(username)
        else:
            # 登录失败界面
            messagebox.showerror('登陆失败！', '用户名或密码错误！')

    # 注册界面
    def register_ui(self) -> None:
        self.register_tk = tkinter.Toplevel(self.login_tk)
        self.register_tk.geometry('300x150')
        self.register_tk.title('注册')

        # 用户名输入框提示
        username_display = tkinter.Label(
            self.register_tk,
            text='用户名：',
            font=('等线', 12)
        )
        username_display.place(x=30, y=20)

        # 用户名输入框
        self.new_username = tkinter.StringVar()
        username_entry = tkinter.Entry(
            self.register_tk,
            show=None,
            font=('等线', 12),
            textvariable=self.new_username
        )
        username_entry.place(x=110, y=20)

        # 密码输入框提示
        passwd_display = tkinter.Label(
            self.register_tk,
            text='密码：',
            font=('等线', 12)
        )
        passwd_display.place(x=40, y=60)

        # 密码输入框
        self.new_passwd = tkinter.StringVar()
        passwd_entry = tkinter.Entry(
            self.register_tk,
            show=None,
            font=('等线', 12),
            textvariable=self.new_passwd
        )
        passwd_entry.place(x=110, y=60)

        # 注册按钮
        login_button = tkinter.Button(
            self.register_tk,
            text='注册',
            font=('等线', 12),
            command=self.register
        )
        login_button.place(x=150, y=100)

    # 注册方法
    def register(self) -> None:
        new_username = self.new_username.get()
        new_passwd = self.new_passwd.get()

        # 用户名或密码为空
        if new_username == '' or new_passwd == '':
            messagebox.showwarning('注册失败！', '用户名或密码不能为空！')
            self.register_tk.destroy()
        # 注册成功
        elif self.mg.register(new_username, new_passwd):
            self.register_tk.destroy()
            messagebox.showinfo('注册成功！', f'用户 {new_username} 成功注册学生信息管理系统')
        # 用户名冲突
        else:
            messagebox.showerror('注册失败！', '用户名已经被使用！')
            self.register_tk.destroy()

    # 主界面
    def main_ui(self, username: str) -> None:
        self.main_tk = tkinter.Tk()
        self.main_tk.geometry('1000x600')
        self.main_tk.title('学生信息管理系统V0.1')

        # 欢迎词
        welcome_text = tkinter.Label(
            text=f'欢迎，{username}',
            font=('等线', 20)
        )
        welcome_text.place(x=30, y=20)
        
        # 日期
        now = date.today()
        today_label = tkinter.Label(
            text=f'日期：{now.strftime("%Y-%m-%d")}',
            font=('等线', 16)
        )
        today_label.place(x=760, y=26)

        # 查询方式提示
        query_mode_display = tkinter.Label(
            text='查询方式：',
            font=('等线', 14)
        )
        query_mode_display.place(x=30, y=70)

        # 查询方式下拉框
        self.selected_query_mode = tkinter.StringVar()
        query_mode = ttk.Combobox(
            textvariable=self.selected_query_mode,
            state='readonly',
            width=10
        )
        query_mode['values'] = ('全部', '学号', '性别', '专业', '班级', '在校状态')
        query_mode.current(0)
        query_mode.place(x=130, y=70)

        # 比较方式提示
        comparison_mode_display = tkinter.Label(
            text='比较方式：',
            font=('等线', 14)
        )
        comparison_mode_display.place(x=230, y=70)

        # 比较方式
        self.selected_comparison_mode = tkinter.StringVar(value='=')
        comparison_mode_equal = tkinter.Radiobutton(
            text='等于',
            variable=self.selected_comparison_mode,
            value='=',
            font=('等线', 12)
        )
        comparison_mode_equal.place(x=380, y=70)
        comparison_mode_more = tkinter.Radiobutton(
            text='大于',
            variable=self.selected_comparison_mode,
            value='>',
            font=('等线', 12)
        )
        comparison_mode_more.place(x=320, y=70)
        comparison_mode_less = tkinter.Radiobutton(
            text='小于',
            variable=self.selected_comparison_mode,
            value='<',
            font=('等线', 12)
        )
        comparison_mode_less.place(x=440, y=70)

        # 查询内容提示框
        query_content_display = tkinter.Label(
            text='查询内容：',
            font=('等线', 14)
        )
        query_content_display.place(x=520, y=70)

        # 查询内容输入框
        self.query_content = tkinter.StringVar()
        query_content_entry = tkinter.Entry(
            show=None,
            font=('等线', 14),
            textvariable=self.query_content,
            width=24
        )
        query_content_entry.place(x=620, y=70)

        # 查询按钮
        query_button = tkinter.Button(
            text='查询',
            font=('等线', 14),
            command=self.query
        )
        query_button.place(x=880, y=66)

        # 添加学生按钮
        add_student_button = tkinter.Button(
            text='添加学生',
            font=('等线', 14),
            command=self.add_student_page
        )
        add_student_button.place(x=40, y=540)

        # 导出本页按钮
        output_button = tkinter.Button(
            text='导出本页',
            font=('等线', 14),
            command=self.putout
        )
        output_button.place(x=160, y=540)

        # 退出登录按钮
        output_button = tkinter.Button(
            text='退出登录',
            font=('等线', 14),
            command=self.logout
        )
        output_button.place(x=860, y=540)

        self.main_tk.mainloop()

    # 导出方法
    def putout(self) -> None:
        timestamp = time.time()
        desktop_path = os.path.join(os.path.expanduser("~"), "Desktop")  
        f_name = 'output_' + str(timestamp)[11: 17] + '.txt'
        full_path = os.path.join(desktop_path, f_name) 

        try:
            stu_list = self.student_list
        except:
            messagebox.showwarning('错误', '查询结果为空！')
            return
        
        try:
            f = open(full_path, 'w', encoding='utf-8')
        except:
            messagebox.showerror('错误', '无法创建导出文件')
            return

        for student in stu_list:
            f.write('学号：' + student.stu_id + '\n')
            f.write('姓名：' + student.stu_name + '\n')
            f.write('性别：' + student.stu_gender + '\n')
            f.write('专业：' + student.stu_major + '\n')
            f.write('班级：' + student.stu_class + '\n')
            f.write('出生日期：' + student.stu_birth + '\n')
            f.write('手机号码：' + student.stu_phone + '\n')
            f.write('在校状态：' + student.stu_status + '\n')
            f.write('成绩：\n')
            for key, value in student.stu_grades.items():
                f.write('\t' + str(key) + ': ' + str(value) + '\n')
            f.write('#############################\n')

        f.close()
        messagebox.showinfo('导出', f'文件 {f_name} 成功导出到桌面！')

    # 退出登录方法
    def logout(self) -> None:
        self.main_tk.destroy()
        messagebox.showinfo('退出登录', f'用户 {self.username.get()} 已经退出登录')
        self.login_ui()

    # 添加学生界面
    def add_student_page(self) -> None:
        self.add_student_tk = tkinter.Toplevel(self.main_tk)
        self.add_student_tk.geometry('400x480')
        self.add_student_tk.title('添加学生')
  
        # 学号提示
        id_display = tkinter.Label(
            self.add_student_tk,
            text='学号：',
            font=('等线', 14)
        )
        id_display.place(x=30, y=14)

        # 学号输入框
        self.new_id = tkinter.StringVar()
        new_id_entry = tkinter.Entry(
            self.add_student_tk,
            show=None,
            font=('等线', 14),
            textvariable=self.new_id,
            width=24
        )
        new_id_entry.place(x=100, y=14)

        # 姓名提示
        name_display = tkinter.Label(
            self.add_student_tk,
            text='姓名：',
            font=('等线', 14)
        )
        name_display.place(x=30, y=44)

        # 姓名输入框
        self.new_name = tkinter.StringVar()
        new_name_entry = tkinter.Entry(
            self.add_student_tk,
            show=None,
            font=('等线', 14),
            textvariable=self.new_name,
            width=24
        )
        new_name_entry.place(x=100, y=44)

        # 性别提示
        gender_display = tkinter.Label(
            self.add_student_tk,
            text='性别：',
            font=('等线', 14)
        )
        gender_display.place(x=30, y=74)

        # 性别选择
        self.new_gender = tkinter.StringVar(value='男')
        male_choice = tkinter.Radiobutton(
            self.add_student_tk,
            text='男',
            variable=self.new_gender,
            value='男',
            font=('等线', 14)
        )
        male_choice.place(x=100, y=72)
        female_choice = tkinter.Radiobutton(
            self.add_student_tk,
            text='女',
            variable=self.new_gender,
            value='女',
            font=('等线', 14)
        )
        female_choice.place(x=160, y=72)

        # 专业提示
        major_display = tkinter.Label(
            self.add_student_tk,
            text='专业：',
            font=('等线', 14)
        )
        major_display.place(x=30, y=104)

        # 专业输入框
        self.new_major = tkinter.StringVar()
        new_major_entry = tkinter.Entry(
            self.add_student_tk,
            show=None,
            font=('等线', 14),
            textvariable=self.new_major,
            width=24
        )
        new_major_entry.place(x=100, y=104)

        # 班级提示
        class_display = tkinter.Label(
            self.add_student_tk,
            text='班级：',
            font=('等线', 14)
        )
        class_display.place(x=30, y=134)

        # 班级输入框
        self.new_class = tkinter.StringVar()
        new_class_entry = tkinter.Entry(
            self.add_student_tk,
            show=None,
            font=('等线', 14),
            textvariable=self.new_class,
            width=24
        )
        new_class_entry.place(x=100, y=134)

        # 出生日期提示
        birth_display = tkinter.Label(
            self.add_student_tk,
            text='出生日期：',
            font=('等线', 14)
        )
        birth_display.place(x=30, y=164)

        # 年月日提示
        birth_year = tkinter.Label(
            self.add_student_tk,
            text='年',
            font=('等线', 14)
        )
        birth_year.place(x=200, y=164)
        birth_month = tkinter.Label(
            self.add_student_tk,
            text='月',
            font=('等线', 14)
        )
        birth_month.place(x=260, y=164)
        birth_date = tkinter.Label(
            self.add_student_tk,
            text='日',
            font=('等线', 14)
        )
        birth_date.place(x=320, y=164)

        # 年月日输入
        self.new_birth_year = tkinter.StringVar()
        new_birth_year_entry = tkinter.Entry(
            self.add_student_tk,
            show=None,
            font=('等线', 14),
            textvariable=self.new_birth_year,
            width=7
        )
        new_birth_year_entry.place(x=124, y=164)
        self.new_birth_month = tkinter.StringVar()
        new_birth_month_entry = tkinter.Entry(
            self.add_student_tk,
            show=None,
            font=('等线', 14),
            textvariable=self.new_birth_month,
            width=3
        )
        new_birth_month_entry.place(x=226, y=164)
        self.new_birth_date = tkinter.StringVar()
        new_birth_date_entry = tkinter.Entry(
            self.add_student_tk,
            show=None,
            font=('等线', 14),
            textvariable=self.new_birth_date,
            width=3
        )
        new_birth_date_entry.place(x=286, y=164)

        # 电话提示
        phone_display = tkinter.Label(
            self.add_student_tk,
            text='电话号码：',
            font=('等线', 14)
        )
        phone_display.place(x=30, y=194)

        # 电话输入框
        self.new_phone = tkinter.StringVar()
        new_phone_entry = tkinter.Entry(
            self.add_student_tk,
            textvariable=self.new_phone,
            show=None,
            font=('等线', 14),
            width=21
        )
        new_phone_entry.place(x=130, y=194)

        # 在校状态提示
        status_display = tkinter.Label(
            self.add_student_tk,
            text='在校状态：',
            font=('等线', 14)
        )
        status_display.place(x=30, y=224)

        # 在校状态选择
        self.new_status = tkinter.StringVar(value='在校')
        present_choice = tkinter.Radiobutton(
            self.add_student_tk,
            text='在校',
            variable=self.new_status,
            value='在校',
            font=('等线', 14)
        )
        present_choice.place(x=130, y=224)
        absent_choice = tkinter.Radiobutton(
            self.add_student_tk,
            text='离校',
            variable=self.new_status,
            value='离校',
            font=('等线', 14)
        )
        absent_choice.place(x=210, y=224)

        # 科目提示
        subject_display = tkinter.Label(
            self.add_student_tk,
            text='科目',
            font=('等线', 14)
        )
        subject_display.place(x=120, y=254)

        # 科目输入框
        self.new_subject_1 = tkinter.StringVar()
        new_subject_1_entry = tkinter.Entry(
            self.add_student_tk,
            textvariable=self.new_subject_1,
            font=('等线', 14),
            width=20
        )
        new_subject_1_entry.place(x=32, y=284)
        self.new_subject_2 = tkinter.StringVar()
        new_subject_2_entry = tkinter.Entry(
            self.add_student_tk,
            textvariable=self.new_subject_2,
            font=('等线', 14),
            width=20
        )
        new_subject_2_entry.place(x=32, y=314)
        self.new_subject_3 = tkinter.StringVar()
        new_subject_3_entry = tkinter.Entry(
            self.add_student_tk,
            textvariable=self.new_subject_3,
            font=('等线', 14),
            width=20
        )
        new_subject_3_entry.place(x=32, y=344)

        # 成绩提示
        grade_display = tkinter.Label(
            self.add_student_tk,
            text='成绩',
            font=('等线', 14)
        )
        grade_display.place(x=280, y=254)

        # 成绩输入框
        self.new_grade_1 = tkinter.StringVar()
        new_grade_1_entry = tkinter.Entry(
            self.add_student_tk,
            textvariable=self.new_grade_1,
            font=('等线', 14),
            width=8
        )
        new_grade_1_entry.place(x=260, y=284)
        self.new_grade_2 = tkinter.StringVar()
        new_grade_2_entry = tkinter.Entry(
            self.add_student_tk,
            textvariable=self.new_grade_2,
            font=('等线', 14),
            width=8
        )
        new_grade_2_entry.place(x=260, y=314)
        self.new_grade_3 = tkinter.StringVar()
        new_grade_3_entry = tkinter.Entry(
            self.add_student_tk,
            textvariable=self.new_grade_3,
            font=('等线', 14),
            width=8
        )
        new_grade_3_entry.place(x=260, y=344)

        # 提交按钮
        submit_button = tkinter.Button(
            self.add_student_tk,
            text='提交',
            font=('等线', 16),
            command=self.add_student
        )
        submit_button.place(x=160, y=400)

    # 添加学生方法
    def add_student(self) -> None:
        new_id = self.new_id.get()
        new_name = self.new_name.get()
        new_gender = self.new_gender.get()
        new_major = self.new_major.get()
        new_class = self.new_class.get()
        new_birth_year = self.new_birth_year.get()
        new_birth_month = self.new_birth_month.get()
        new_birth_date = self.new_birth_date.get()
        new_phone = self.new_phone.get()
        new_status = self.new_status.get()
        new_subject_1 = self.new_subject_1.get()
        new_subject_2 = self.new_subject_2.get()
        new_subject_3 = self.new_subject_3.get()
        new_grade_1 = self.new_grade_1.get()
        new_grade_2 = self.new_grade_2.get()
        new_grade_3 = self.new_grade_3.get()

        # 学号检错
        if new_id == '':
            messagebox.showwarning('错误', '学号不能为空')
            return
        elif len(new_id) != 10:
            messagebox.showwarning('错误', '学号必须为10位')
            return

        # 姓名检错
        if new_name == '':
            messagebox.showwarning('错误', '姓名不能为空')
            return

        # 专业检错
        if new_major == '':
            messagebox.showwarning('错误', '专业不能为空')
            return
            
        # 班级检错
        if new_class == '':
            messagebox.showwarning('错误', '班级不能为空')
            return

        # 出生日期检错
        if new_birth_year == '' or new_birth_month == '' or new_birth_date == '':
            messagebox.showwarning('错误', '出生日期不能为空')
            return
        elif not(self.mg.is_valid_date(new_birth_year, new_birth_month, new_birth_date)):
            messagebox.showwarning('错误', '出生日期格式错误')
            return

        if len(new_birth_month) == 1:
            new_birth_month = '0' + new_birth_month
        elif len(new_birth_date) == 1:
            new_birth_date = '0' + new_birth_date

        new_birth = f'{new_birth_year}-{new_birth_month}-{new_birth_date}'

        # 电话号码检错
        if new_phone == '':
            messagebox.showwarning('错误', '电话号码不能为空')
            return
        elif len(new_phone) != 11:
            messagebox.showwarning('错误', '电话号码必须为11位')
            return

        # 科目检错
        if new_subject_1 == '' or new_subject_2 == '' or new_subject_3 == '':
            messagebox.showwarning('错误', '科目不能为空')
            return
        elif new_subject_1 == new_subject_2 or new_subject_1 == new_subject_3 or new_subject_3 == new_subject_2:
            messagebox.showwarning('错误', '存在相同科目！')
            return
            
        # 成绩检错
        if new_grade_1 == '' or new_grade_2 == '' or new_grade_3 == '':
            messagebox.showwarning('错误', '成绩不能为空')
            return
        
        # 构建学生对象
        new_grades = {
            new_subject_1: new_grade_1,
            new_subject_2: new_grade_2,
            new_subject_3: new_grade_3
        }
        student = Student(new_id, new_name, new_gender, new_major, new_class, new_birth, new_phone, new_status, new_grades)

        if self.mg.insert(student):
            messagebox.showinfo('成功', f'学生 {new_name} 已被成功添加')
            self.add_student_tk.destroy()
            self.query()
        else:
            messagebox.showerror('错误', '添加学生失败!')

    # 查询方法
    def query(self) -> None:
        query_mode = self.selected_query_mode.get()
        comparison_mode = self.selected_comparison_mode.get()
        query_content = self.query_content.get()

        # '全部', '学号', '性别', '专业', '班级', '在校状态'
        if query_mode == '全部':
            query_mode = 'init'
        elif query_mode == '学号':
            query_mode = 'id'
        elif query_mode == '性别':
            query_mode = 'gender'
        elif query_mode == '专业':
            query_mode = 'major'
        elif query_mode == '班级':
            query_mode = 'class'
        elif query_mode == '在校状态':
            query_mode = 'status'

        result = self.mg.query(query_mode, comparison_mode, query_content)
        self.result_frame(result)

    # 查询结果窗口
    def result_frame(self, result: tuple[tuple]) -> None:
        # 结果边框
        border = tkinter.Canvas(
            height=400,
            width=930
        )
        border.create_rectangle(2, 2, 930, 400)
        border.place(x=30, y=120)

        # 标题
        id_title = '学号'
        name_title = '姓名'
        gender_title = '性别'
        class_title = '班级'
        title = tkinter.Label(
            border,
            text=f'{id_title:^26}{name_title:8}{gender_title:8} {class_title:^50}',
            font=('等线', 14)
        )
        title.place(x=3, y=9)

        # 标题边框
        border.create_line(2, 42, 930, 42)

        # 内容
        self.student_list = self.mg.student_info_To_object(result)
        default_y = 50
        for student in self.student_list:
            # 每条记录
            content_label = tkinter.Label(
                border,
                text=f'{student.stu_id:^16} {student.stu_name:4} {student.stu_gender:^8}   {student.stu_class:40}',
                font=('等线', 16)
            )
            content_label.place(x=6, y=default_y)

            # 编辑信息按钮
            edit_button = tkinter.Button(
                border,
                text='更新信息',
                font=('等线', 10),
                command=lambda s=student: self.edit_page(s)
            )
            edit_button.place(x=710, y=default_y)

            # 详细信息按钮
            detail_button = tkinter.Button(
                border,
                text='查看详情',
                font=('等线', 10),
                command=lambda s=student: self.show_details(s)
            )
            detail_button.place(x=780, y=default_y)

            # 删除按钮
            delete_button = tkinter.Button(
                border,
                text='删除学生',
                font=('等线', 10),
                command=lambda s=student: self.delete(s)
            )
            delete_button.place(x=850, y=default_y)
            
            default_y += 30

    # 更新学生信息界面
    def edit_page(self, student: Student) -> None:
        self.edit = tkinter.Toplevel(self.main_tk)
        self.edit.geometry('300x300')
        self.edit.title('更新信息')

        # 身份信息提示
        identification_1 = tkinter.Label(
            self.edit,
            text=f'学号: {student.stu_id}',
            font=('等线', 14)
        )
        identification_2 = tkinter.Label(
            self.edit,
            text=f'姓名: {student.stu_name}',
            font=('等线', 14)
        )
        identification_3 = tkinter.Label(
            self.edit,
            text=f'班级: {student.stu_class}',
            font=('等线', 14)
        )
        identification_1.place(x=10, y=10)
        identification_2.place(x=10, y=34)
        identification_3.place(x=10, y=58)

        # 更新项提示
        edit_opt_display = tkinter.Label(
            self.edit,
            text='更新项：',
            font=('等线', 14)
        )
        edit_opt_display.place(x=10, y=90)

        # 更新项下拉框
        self.selected_edit_opt = tkinter.StringVar()
        edit_opt = ttk.Combobox(
            self.edit,
            textvariable=self.selected_edit_opt,
            state='readonly',
            width=36
        )
        edit_opt['values'] = ('专业', '班级', '电话号码', '在校状态')
        edit_opt.current(0)
        edit_opt.place(x=10, y=120)

        # 更新内容提示
        edit_content_display = tkinter.Label(
            self.edit,
            text='更新内容：',
            font=('等线', 14)
        )
        edit_content_display.place(x=10, y=150)

        # 更新内容输入框
        self.edit_content = tkinter.StringVar()
        edit_content_entry = tkinter.Entry(
            self.edit,
            show=None,
            font=('等线', 14),
            textvariable=self.edit_content,
            width=27
        )
        edit_content_entry.place(x=10, y=180)

        # 更新按钮
        edit_button = tkinter.Button(
            self.edit,
            text='确定',
            font=('等线', 16),
            command=lambda s=student.stu_id: self.update(s)
        )
        edit_button.place(x=120, y=220)

    #更新学生信息方法
    def update(self, student: str) -> None:
        edit_opt = self.selected_edit_opt.get()
        edit_content = self.edit_content.get()

        # '专业', '班级', '电话号码', '在校状态'
        if edit_opt == '专业':
            edit_opt = 'stu_major'
        elif edit_opt == '班级':
            edit_opt = 'stu_class' 
        elif edit_opt == '电话号码':
            edit_opt = 'stu_phone'
        elif edit_opt == '在校状态':
            edit_opt = 'stu_status'   

        if (edit_opt == 'stu_phone') and not(len(edit_content) == 11):
            messagebox.showwarning('错误', '手机号码必须是11位')
            self.edit.destroy()
        elif edit_content == '':
            messagebox.showwarning('错误', '更新内容不能为空')
            self.edit.destroy()
        else:
            if self.mg.update(student, edit_opt, edit_content):
                messagebox.showinfo('更新信息', '学生信息更新成功！')
                self.edit.destroy()
                self.query()
            else:
                messagebox.showerror('更新信息', '更新失败！')

    # 显示学生详情
    def show_details(self, student: Student) -> None:
        keys = list(student.stu_grades.keys())
        values = list(student.stu_grades.values())

        info = f'''学号：{student.stu_id}
姓名: {student.stu_name}
性别: {student.stu_gender}
专业: {student.stu_major}
班级: {student.stu_class}
出生日期: {student.stu_birth}
电话号码: {student.stu_phone}
在校状态: {student.stu_status}
{keys[0]}: {values[0]}
{keys[1]}: {values[1]}
{keys[2]}: {values[2]}
'''
        
        messagebox.showinfo('学生详情', info)

    # 删除学生方法
    def delete(self, student: Student) -> None:
        if self.mg.delete(student.stu_id):
            messagebox.showinfo('删除', '删除成功！')
            self.query()
        else:
            messagebox.showwarning('删除', '删除失败！')
            self.query()