import tkinter
from tkinter import ttk
from datetime import date

class Main():
    def __init__(self, username: str) -> None:
        self.main(username)

    def main(self, username: str) -> None:
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
            command=self.result_frame
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
            command=''
        )
        output_button.place(x=160, y=540)

        # 退出登录按钮
        output_button = tkinter.Button(
            text='退出登录',
            font=('等线', 14),
            command=''
        )
        output_button.place(x=860, y=540)

        self.main_tk.mainloop()

    # 添加学生界面
    def add_student_page(self) -> None:
        add_student_tk = tkinter.Toplevel(self.main_tk)
        add_student_tk.geometry('400x480')
        add_student_tk.title('添加学生')

        # 学号提示
        id_display = tkinter.Label(
            add_student_tk,
            text='学号：',
            font=('等线', 14)
        )
        id_display.place(x=30, y=14)

        # 学号输入框
        self.new_id = tkinter.StringVar()
        new_id_entry = tkinter.Entry(
            add_student_tk,
            show=None,
            font=('等线', 14),
            textvariable=self.new_id,
            width=24
        )
        new_id_entry.place(x=100, y=14)

        # 姓名提示
        name_display = tkinter.Label(
            add_student_tk,
            text='姓名：',
            font=('等线', 14)
        )
        name_display.place(x=30, y=44)

        # 姓名输入框
        self.new_name = tkinter.StringVar()
        new_name_entry = tkinter.Entry(
            add_student_tk,
            show=None,
            font=('等线', 14),
            textvariable=self.new_name,
            width=24
        )
        new_name_entry.place(x=100, y=44)

        # 性别提示
        gender_display = tkinter.Label(
            add_student_tk,
            text='性别：',
            font=('等线', 14)
        )
        gender_display.place(x=30, y=74)

        # 性别选择
        self.new_gender = tkinter.StringVar(value='男')
        male_choice = tkinter.Radiobutton(
            add_student_tk,
            text='男',
            variable=self.new_gender,
            value='男',
            font=('等线', 14)
        )
        male_choice.place(x=100, y=72)
        female_choice = tkinter.Radiobutton(
            add_student_tk,
            text='女',
            variable=self.new_gender,
            value='女',
            font=('等线', 14)
        )
        female_choice.place(x=160, y=72)

        # 专业提示
        major_display = tkinter.Label(
            add_student_tk,
            text='专业：',
            font=('等线', 14)
        )
        major_display.place(x=30, y=104)

        # 专业输入框
        self.new_major = tkinter.StringVar()
        new_major_entry = tkinter.Entry(
            add_student_tk,
            show=None,
            font=('等线', 14),
            textvariable=self.new_major,
            width=24
        )
        new_major_entry.place(x=100, y=104)

        # 班级提示
        class_display = tkinter.Label(
            add_student_tk,
            text='班级：',
            font=('等线', 14)
        )
        class_display.place(x=30, y=134)

        # 班级输入框
        self.new_class = tkinter.StringVar()
        new_class_entry = tkinter.Entry(
            add_student_tk,
            show=None,
            font=('等线', 14),
            textvariable=self.new_class,
            width=24
        )
        new_class_entry.place(x=100, y=134)

        # 出生日期提示
        birth_display = tkinter.Label(
            add_student_tk,
            text='出生日期：',
            font=('等线', 14)
        )
        birth_display.place(x=30, y=164)

        # 年月日提示
        birth_year = tkinter.Label(
            add_student_tk,
            text='年',
            font=('等线', 14)
        )
        birth_year.place(x=200, y=164)
        birth_month = tkinter.Label(
            add_student_tk,
            text='月',
            font=('等线', 14)
        )
        birth_month.place(x=260, y=164)
        birth_date = tkinter.Label(
            add_student_tk,
            text='日',
            font=('等线', 14)
        )
        birth_date.place(x=320, y=164)

        # 年月日输入
        self.new_birth_year = tkinter.StringVar()
        new_birth_year_entry = tkinter.Entry(
            add_student_tk,
            show=None,
            font=('等线', 14),
            textvariable=self.new_birth_year,
            width=7
        )
        new_birth_year_entry.place(x=124, y=164)
        self.new_birth_month = tkinter.StringVar()
        new_birth_month_entry = tkinter.Entry(
            add_student_tk,
            show=None,
            font=('等线', 14),
            textvariable=self.new_birth_month,
            width=3
        )
        new_birth_month_entry.place(x=226, y=164)
        self.new_birth_date = tkinter.StringVar()
        new_birth_date_entry = tkinter.Entry(
            add_student_tk,
            show=None,
            font=('等线', 14),
            textvariable=self.new_birth_date,
            width=3
        )
        new_birth_date_entry.place(x=286, y=164)

        # 电话提示
        phone_display = tkinter.Label(
            add_student_tk,
            text='电话号码：',
            font=('等线', 14)
        )
        phone_display.place(x=30, y=194)

        # 电话输入框
        self.new_phone = tkinter.StringVar()
        new_phone_entry = tkinter.Entry(
            add_student_tk,
            textvariable=self.new_phone,
            show=None,
            font=('等线', 14),
            width=21
        )
        new_phone_entry.place(x=130, y=194)

        # 在校状态提示
        status_display = tkinter.Label(
            add_student_tk,
            text='在校状态：',
            font=('等线', 14)
        )
        status_display.place(x=30, y=224)

        # 在校状态选择
        self.new_status = tkinter.StringVar(value='在校')
        present_choice = tkinter.Radiobutton(
            add_student_tk,
            text='在校',
            variable=self.new_status,
            value='在校',
            font=('等线', 14)
        )
        present_choice.place(x=130, y=224)
        absent_choice = tkinter.Radiobutton(
            add_student_tk,
            text='离校',
            variable=self.new_status,
            value='离校',
            font=('等线', 14)
        )
        absent_choice.place(x=210, y=224)

        # 科目提示
        subject_display = tkinter.Label(
            add_student_tk,
            text='科目',
            font=('等线', 14)
        )
        subject_display.place(x=120, y=254)

        # 科目输入框
        self.new_subject_1 = tkinter.StringVar()
        new_subject_1_entry = tkinter.Entry(
            add_student_tk,
            textvariable=self.new_subject_1,
            font=('等线', 14),
            width=20
        )
        new_subject_1_entry.place(x=32, y=284)
        self.new_subject_2 = tkinter.StringVar()
        new_subject_2_entry = tkinter.Entry(
            add_student_tk,
            textvariable=self.new_subject_2,
            font=('等线', 14),
            width=20
        )
        new_subject_2_entry.place(x=32, y=314)
        self.new_subject_3 = tkinter.StringVar()
        new_subject_3_entry = tkinter.Entry(
            add_student_tk,
            textvariable=self.new_subject_3,
            font=('等线', 14),
            width=20
        )
        new_subject_3_entry.place(x=32, y=344)

        # 成绩提示
        grade_display = tkinter.Label(
            add_student_tk,
            text='成绩',
            font=('等线', 14)
        )
        grade_display.place(x=280, y=254)

        # 成绩输入框
        self.new_grade_1 = tkinter.StringVar()
        new_grade_1_entry = tkinter.Entry(
            add_student_tk,
            textvariable=self.new_grade_1,
            font=('等线', 14),
            width=8
        )
        new_grade_1_entry.place(x=260, y=284)
        self.new_grade_2 = tkinter.StringVar()
        new_grade_2_entry = tkinter.Entry(
            add_student_tk,
            textvariable=self.new_grade_2,
            font=('等线', 14),
            width=8
        )
        new_grade_2_entry.place(x=260, y=314)
        self.new_grade_3 = tkinter.StringVar()
        new_grade_3_entry = tkinter.Entry(
            add_student_tk,
            textvariable=self.new_grade_3,
            font=('等线', 14),
            width=8
        )
        new_grade_3_entry.place(x=260, y=344)

        # 提交按钮
        submit_button = tkinter.Button(
            add_student_tk,
            text='提交',
            font=('等线', 16),
            command=''
        )
        submit_button.place(x=160, y=400)

    def query(self) -> None:
        query_mode = self.selected_query_mode.get()
        comparison_mode = self.selected_comparison_mode.get()
        query_content = self.query_content.get()

    # 查询结果窗口
    def result_frame(self) -> None:
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
        major_title = '专业'
        class_title = '班级'
        title = tkinter.Label(
            border,
            text=f'{id_title:^28}{name_title:10}{gender_title:^20}{major_title:^20}{class_title:^20}',
            font=('等线', 14)
        )
        title.place(x=3, y=9)

        # 标题边框
        border.create_line(2, 42, 930, 42)

if __name__ == '__main__':
    tk = Main('admin')