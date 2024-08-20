class Student:

    def __init__(self, stu_id: str, stu_name: str, stu_gender: str, stu_major: str, stu_class: str, stu_birth: str, stu_phone: str, stu_status: str, stu_grades: dict) -> None:
        self.stu_id = stu_id
        self.stu_name = stu_name
        self.stu_gender = stu_gender
        self.stu_major = stu_major
        self.stu_class = stu_class
        self.stu_birth = stu_birth
        self.stu_phone = stu_phone
        self.stu_status = stu_status
        self.stu_grades = stu_grades

    def __str__(self) -> str:
        return f'{self.stu_id}  {self.stu_name}  {self.stu_gender}  {self.stu_major}  {self.stu_class}  {self.stu_birth}  {self.stu_phone}  {self.stu_status}  {self.stu_grades}'
