# Python Medium

`更新时间：2024-4-22`

*注：此笔记中的函数和方法的参数并不完整，如有需要，请查阅Python手册*

---

# Python进阶语法

## 初识类与对象

类就如同空的一个表格模板，内含多项元素，每个元素有一个对应的值，对象就是每个需要填写数据的表格，表格中的数据内容取决于是否向对象中赋值

```py
class Student:
    name = None
    gender = None
    nationality = None
    native_place = None
    age = None

stu1 = Student()
stu1.name = "张三"
stu1.age = 25
print(stu1.name)
print(stu1.age)
```

> <img src="./IMG2/Screenshot 2024-04-21 131701.png">

## 类和对象的定义

Python中，使用关键字class定义类，每个类有对应的类名称，一个类下可以有多种数据类型，类中的变量被称为属性或成员变量，函数被称为行为或成员方法

```py
class class_name:
    attribute = value

    def behavior(self):

object = class_name()
```

## self关键字

在方法的定义中，必须存在一个self参数，self用于表示类本身

```py
class Stu:
    name = None
    age = None

    def print_all(self):
        print(self.name)
        print(self.age)

stu1 = Stu()
stu1.name = "张三"
stu1.age = 40
stu1.print_all()
```

> <img src="./IMG2/Screenshot 2024-04-21 133057.png">

## 面向对象

在现实生活中，任何的事物都有属性和行为，事件如打扫卫生，属性有谁、时间、地点，行为有扫地、拖地等，物品如键盘，属性有材质、轴体、价格，行为有敲、按、砸等，这就与计算机中的类相似，计算机中的类同样地拥有属性和行为。但与之不同的是，计算机中的类并不是一个个的实体，而是一种没有任何数据的抽象框架，需要创建每一个对象，并向对象中填充数据来形成实体，这中套路就被称为面向对象

## 构造方法

Python类中有一个特殊的方法 `__init__()` ，叫做构造方法，该方法在对象创建的时候会自动执行，在创建对象语句的括号里的参数也会被构造方法接收

```py
class Student:
    id = None
    name = None
    age = None

    def __init__(self, id, name, age):
        self.id = id
        self.name = name
        self.age = age

    def print_all(self):
        print(self.id, self.name, self.age)

stu1 = Student(1, "张三", 20)
stu1.print_all()
```

> <img src="./IMG2/Screenshot 2024-04-21 140218.png">

## 常用魔术方法

### __str__()方法

`__str__()` 方法在对象被print时自动执行，返回值即为print的输出

```py
class Str:
    name = "张三"
    age = 20

    def __str__(self):
        return f"姓名：{self.name}, 年龄：{self.age}"
class Str1:
    name = "李四"
    age = 25

str1 = Str()
str2 = Str1()
print(str1)
print(str2)
```

> <img src="./IMG2/Screenshot 2024-04-21 141912.png">

### __lt__()方法

`__lt__()` 用于指定不同的对象在比较时依据的类属性，这里需要使用关键字 `other` 来代替其他对象，需要注意的是，该方法的比较符号表示的是逻辑关系，小于表示正逻辑，大于表示负逻辑

```py
class Comparison:
    def __init__(self, name, age):
        self.name = name
        self.age = age

    def __lt__(self, other):
        return self.age < other.age

stu1 = Comparison("张三", 22)
stu2 = Comparison("李四", 19)
print(stu1 > stu2)  #True
```

### __le__()方法

`__le__()` 方法用于小于等于和大于等于比较，同样地，比较符号用于表示比较逻辑的正负

```py
class Comparison:
    def __init__(self, name, age):
        self.name = name
        self.age = age

    def __le__(self, other):
        return self.age <= other.age

stu1 = Comparison("张三", 22)
stu2 = Comparison("李四", 19)
print(stu1 <= stu2) #False
```

### __eq__()方法

`__eq__()` 方法用于等于的比较

```py
class Comparison:
    def __init__(self, name, age):
        self.name = name
        self.age = age

    def __eq__(self, other):
        return self.age == other.age

stu1 = Comparison("张三", 22)
stu2 = Comparison("李四", 19)
print(stu1 == stu2) #False
```

## 面向对象的三大特性

### 封装

将现实世界中事物的属性和行为，描述为程序类中的成员变量和成员方法，这个步骤被称为封装

#### 私有成员

私有成员指的是在类中，无法被用户直接调用的成员变量或方法，Python中，私有成员的定义是在成员名前添加 `__`

```py
class Private:
    public = "public"
    __private = "private"
p = Private()
print(p.public)
print(p.__private)
```

> <img src="./IMG2/Screenshot 2024-04-21 145253.png">

```py
class Phone:
    def __init__(self, voltage):
        self.voltage = voltage
    
    def __single_core_mode(self):
        print("Enable Single Core Mode")

    def call_by_5g(self):
        if self.voltage >= 1.5:
            print("Calling By 5G")
        else:
            self.__single_core_mode()
            print("Voltage is lower than 1.5V")

phone1 = Phone(1.4)
phone1.call_by_5g()
```

> <img src="./IMG2/Screenshot 2024-04-21 150620.png">

### 继承

继承指的是将旧的类作为模板，并在此基础上增添一些新成员

#### 单继承

**基本语法**

```py
class class_name(father_name):
    new member = value
```

**示例**

```py
class Phone19:
    __SystemVersion = 1.0
    producer = "Eiousee"

    def call_by_4g(self):
        print("Calling By 4G")

class Phone20(Phone19):
    __SystemVersion = 2.0

    def call_by_5g(self):
        print("Calling By 5G")

    def check(self):
        print(self.__SystemVersion)
        self.call_by_4g()
        self.call_by_5g()

phone = Phone20()
phone.check()
```

> <img src="./IMG2/Screenshot 2024-04-21 152542.png">

#### 多继承

**基本语法**

```py
class class_name(father_name, father_name,...):
    new member = value
```

**示例**

```py
class Producer:
    producer = "Eiousee"

class Price:
    price = 10000

class Weight:
    weight = 300

class Phone(Producer, Price, Weight):
    def print_info(self):
        print(self.producer)
        print(self.price)
        print(self.weight)

    def __init__(self):
        self.print_info()

phone = Phone()
```

> <img src="./IMG2/Screenshot 2024-04-21 153353.png">

*注：被继承的父类优先级为从左向右*

#### 调用复写父类

在父类被复写后，依然可以通过 `super()` 来调用，但是 `super()` 只能在子类中使用

```py
class Father:
    num = 114511

class Son(Father):
    num = 112233

    def info(self):
        print(self.num)
        print(super().num)

n = Son()
n.info()
```

> <img src="./IMG2/Screenshot 2024-04-21 154735.png">

### 多态

多态指使用不同的对象完成同一个行为，就可以得到的不同的状态

```py
class Animal:
    def speak(self):
        pass

class Dog(Animal):
    def speak(self):
        print("汪汪汪")

class Cat(Animal):
    def speak(self):
        print("喵喵喵")

def make_noise(animal: Animal):
    animal.speak()

dog = Dog()
cat = Cat()
make_noise(dog)
make_noise(cat)
```

> <img src="./IMG2/Screenshot 2024-04-21 172737.png">

#### 抽象类

类似于上文中的class Animal，只提供方法，但是没有实际实现功能的类被称为抽象类或接口

```py
class Server:
    def server_ip(self):
        '''服务器地址'''
        pass

    def server_name(self):
        '''服务器名'''
        pass

    def server_location(self):
        '''服务器物理地址'''
        pass

class Eiousee(Server):
    def server_ip(self):
        print("114.132.238.79")

    def server_name(self):
        print("艾欧希服务器群组")

    def server_location(self):
        print("广州")

class Ocean(Server):
    def server_ip(self):
        print("101.43.127.58")

    def server_name(self):
        print("奥辛服务器")

    def server_location(self):
        print("上海")

def server_info(server: Server):
    server.server_ip()
    server.server_name()
    server.server_location()

eiousee = Eiousee()
ocean = Ocean()
server_info(eiousee)
server_info(ocean)
```

> <img src="./IMG2/Screenshot 2024-04-21 174102.png">

## 类型注解

当需要解释一个变量的数据类型时，就可以使用类型注解

**基本语法**

`variable: type_name = value`

也可以使用注释

`variable = value   # type: type_name`

### Union注解

在需要注解多个数据类型时，可以使用Union联合注解，使用Union时需要导包

```py
from typing import Union
my_list: list[Union[str, int]] = [1, 2, "hello", "world"]
```

## 综合案例1：每日销售额计算

主文件
```py
from File_Reading import CRV_Reading, Json_Reading
from Data_Calculate import Calculate
from pyecharts.charts import Line
from pyecharts.options import *
from pyecharts.globals import ThemeType

crv_data = CRV_Reading(r"Python/Data/File_Reading/2011年1月销售数据.txt")
json_data = Json_Reading(r"D:\VS Code Files\Python\Data\File_Reading\2011年2月销售数据JSON.txt")
crv_list = crv_data.read_data()
json_list = json_data.read_data()
all_list = crv_list + json_list
calculate = Calculate()
data = calculate.data_statistics(all_list)
line = Line(init_opts=InitOpts(theme=ThemeType.VINTAGE))
line.add_xaxis(list(data.keys()))
line.add_yaxis("销售额", list(data.values()), label_opts=LabelOpts(is_show=False))
line.set_global_opts(
    title_opts=TitleOpts(title="每日销售额", pos_bottom="1%", pos_left="center")
)
line.render()
```

File_Reading.py
```py
import json

class Data:
    def __init__(self, date, id, sales, location):
        self.date = date
        self.id = id
        self.sales = sales
        self.location = location

    def __str__(self):
        return f"日期：{self.date}，订单ID：{self.id}，销售额：{self.sales}，地区：{self.location}"

class FileReading:
    def read_data(self) -> list[Data]:
        '''数据读取'''
        pass

class CRV_Reading(FileReading):
    def __init__(self, path):
        self.path = path

    def read_data(self) -> list[Data]:
        f = open(self.path, "r", encoding="utf-8")
        data_list = []
        for line in f.readlines():
            line = line.strip()
            line_list = line.split(",")
            data = Data(line_list[0], line_list[1], line_list[2], line_list[3])
            data_list.append(data)
        f.close()
        return data_list           

class Json_Reading(FileReading):
    def __init__(self, path):
        self.path = path

    def read_data(self) -> list[Data]:
        f = open(self.path, "r", encoding="utf-8")
        data_list = []
        for line in f.readlines():
            line = json.loads(line)
            data = Data(line["date"], line["order_id"], line["money"], line["province"])
            data_list.append(data)
        f.close()
        return data_list
```

Data_Calculate.py
```py
class Calculate:
    def data_statistics(self, data_list) -> dict[str: int]:
        data_dict = {}
        for i in data_list:
            try:
                data_dict[i.date] += int(i.sales)
            except:
                data_dict[i.date] = {}
                data_dict[i.date] = int(i.sales)
        return data_dict
```

> <img src="./IMG/Screenshot 2024-04-22 145017.png">