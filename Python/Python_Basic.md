# Python Basic

`更新时间：2024-4-17`

注释解释：

- `<>`必填项，必须在当前位置填写相应数据

- `{}`必选项，必须在当前位置选择一个给出的选项

- `[]`可选项，可以选择填写或忽略

*注：此笔记中的函数和方法的参数并不完整，如有需要，请查阅Python手册*

---

# Python基础语法

## 字面量

**数字**

所有数字的类型，其下有四个子类型

- int：整型

- float：浮点型

- complex：复数类型，复数用j表示

- bool：布尔类型，只有1和0

**字符串**

字符串，又称为文本，由任意数量的中文、英文、数字、符号等组成，是描述文本的一种类型

## 注释

Python注释分为单行注释和多行注释两种

- 单行注释

Python中单行注释用 `#` 表示

```py
# 这是一行注释
# 这是第二行注释
# 这是第三行注释
```

- 多行注释

Python中多行注释用 `''''''` 表示

```py
'''
    这是第一行注释
    这是第二行注释
    这是第三行注释
'''
```

## 变量

Python和PHP一样，属于弱类型语言，变量的创建不需要指定变量类型

**基本语法**

`<var> = <value>`

```py
# 创建变量
a = 10
b = 1.1
c = "Hello"
d = 4 + 3j
# 打印变量
print(a, b, c, d)
```

> <img src="./IMG/Screenshot 2024-04-09 200258.png">

## 数据类型

在Python中，可以使用 `type()` 函数来查看某个变量的数据类型，有一个参数，返回值类型为type

```py
# 创建变量
int_num = 10
float_num = 1.234
string_ = "Hello"
# 查看相应数据类型
print(type(int_num))
print(type(float_num))
print(type(string_))
```

> <img src="./IMG/Screenshot 2024-04-09 193655.png">

## 类型转换

在Python中，类型转换使用的是相应函数，常用的有 `int()` ， `float()` ，`str()` 等，均有一个参数，返回值类型为函数名

```py
# 创建变量
int_num = 67
float_num = 1.234
# 类型转换
print(str(int_num))
print(type(str(int_num)))
print(int(float_num))
print(type(int(float_num)))
```

> <img src="./IMG/Screenshot 2024-04-09 195233.png">

## 运算符

- 取整除

Python中，使用 `//` 作为取整除运算符

```py
# 除和整除的比较
a = 9.0
b = 4.0
# 除
print(a / b)
# 整除
print(a // b)
```

> <img src="./IMG/Screenshot 2024-04-09 195829.png">

- 指数

Python中，使用 `**` 作为指数运算符

```py
# 指数运算
a = 9
b = 4
# 此处a为底数，b为指数
print(a ** b)
```

> <img src="./IMG/Screenshot 2024-04-09 200038.png">

## 字符串

Python中，字符串有多种定义方式， `'xxx'`， `"xxx"` ， `'''xxx'''` ， `"""xxx"""` 都可以定义字符串，其中三个引号的定义方式的字符串可以换行

```py
# 定义字符串
a = "hello, world"
b = """hello, 
world"""
print(a)
print(b)
```

> <img src="./IMG/Screenshot 2024-04-09 200747.png">

### 字符串拼接

在Python中，使用 `+` 拼接字符串

```py
# 拼接字符串
a = "hello"
b = ", world"
print(a + b)
```

> <img src="./IMG/Screenshot 2024-04-10 103616.png">

### 字符串格式化

- 标准格式化

Python中，print无法直接打印非str类型的数据，可以使用字符串格式化来达到这个目的，使用占位符 `%`

```py
# 字符串格式化
a = 123456
b = 0.12
c = "hello %d %.2f" % (a, b)
print(c)
```

> <img src="./IMG/Screenshot 2024-04-10 104611.png">

- 快速格式化

Python中，提供了一种快速格式化的方式，在需要打印的字符串前加 `f` 作为格式化标记，然后在字符串中插入 `{var}` 来作为占位符

> <img src="./IMG/Screenshot 2024-04-10 105352.png">

## 数据输入

Python中，使用 `input([Notice])` 函数实现数据输入，可选一个参数，返回值类型为str

```py
# 数据输入
name = input("Submit your name: ")
print("Your name:", name)
```

> <img src="./IMG/Screenshot 2024-04-10 110112.png">

## 判断

Python中，使用 `if` 进行判断，但不同于大多数编程语言的if，Python的if使用缩进进行语句块判断

```py
# if判断
a = 30
b = 20
if a > b:
    print("a > b")
    print("b < a")
else:
    print("a < b")
    print("b > a")
print("Done!")
```

> <img src="./IMG/Screenshot 2024-04-10 111517.png">

*注：Python中的else if被简写为了elif*

## 循环

Python中只有while和for两种循环，但是Python中的for类似于其他编程语言的foreach

```py
# 循环
# while
i = 0
while i <= 3:
    print("i =", i)
    i += 1
# for
name = "Kiiz"
for char in name:
    print(char)
```

> <img src="./IMG/Screenshot 2024-04-10 113311.png">

这里可以看出，for的标准语法为 `for <item> in <set>` ，set表示数据集合，如数组，字符串等，item代指集合中的项，每次循环针对集合中的一项进行操作，一次循环完成后，指针右移，指向集合中的下一项。

### Range()

`range(arg1, arg2, arg3)` 可以创建一个数字序列，有三个参数， `arg1` 是最小值， `arg2` 是最大值， `arg3` 是步长

```py
# range()
num = range(1, -20, -4)
for num in num:
    print(num)
```

> <img src="./IMG/Screenshot 2024-04-10 115413.png">

*注：range()的范围不包含最大值，如上图中，不包含-20*

特别的是，if创建的变量在循环结束后依然可以访问

```py
# range()
for num in range(1, -20, -4):
    print(num)
print(num)
```

> <img src="./IMG/Screenshot 2024-04-10 115948.png">

## 函数

Python中，函数定义关键字为 `def`

```py
# 函数定义
# 定义一个用于字符串计数的函数
def strlen(arg):
    # 参数类型判断
    if type(arg) != str:
        return "Not a valid argument!"
    i = 0
    count = 0
    # 字符串计数
    for i in arg:
        count += 1
    return count
value1 = 1234
value2 = "abcdefg"
print(strlen(value1))
print(strlen(value2))
```

> <img src="./IMG/Screenshot 2024-04-10 143104.png">

## 综合训练1：ATM机

```py
def login():
    # 登录函数，用户使用ATM的首页
    # 无参数
    # 无返回值
    print("--------------------")
    print("您好，欢迎登录银行ATM机，请登录")
    # 初始化余额为5000000
    global balance
    balance = 5000000
    global name
    name = input("姓名：")
    print("--------------------")
    menu(name, balance)
def menu(name, balance):
    # 主菜单函数，用户登陆后，实现相关操作
    # @param1 name：数据类型为str，记录用户姓名，用于显示
    # @param2 balance：用户的余额
    # @return opt：数据类型为int，返回用户进行的操作
    print("--------------------")
    print(f"欢迎回来，{name}")
    print("1. 余额查询")
    print("2. 存款")
    print("3. 取款")
    print("4. 退出")
    # 注：Python中input返回的数据类型为str，需要转换为int
    operation = int(input("请选择您要办理的业务："))
    # 查询操作
    if operation == 1:
        print("--------------------")
        query(name, balance)
    # 存款操作
    elif operation == 2:
        print("--------------------")
        deposit(name, balance)
    # 取款操作
    elif operation == 3:
        print("--------------------")
        withdraw(name, balance)
    # 退出操作
    elif operation == 4:
        print("--------------------")
        login()
    # 无效操作
    else:
        print("--------------------")
        print("无效操作！")
        print("--------------------")
        menu(name, balance)
    print("--------------------")
def query(name, balance):
    # 余额查询函数，显示用户的余额
    # @param1 name：数据类型为str，用户姓名，用于在数据库中查询余额
    # @param2 balance：数据类型为int，用户的余额
    # 无返回值
    print("--------------------")
    print(f"亲爱的用户{name}您好，您卡上的余额为：{balance}元")
    print("--------------------")
    menu(name, balance)
def deposit(name, balance):
    # 存款函数，增加用户的余额
    # @param1 name：数据类型为str，用户姓名，用于传参
    # @param2 balance：数据类型为int，用户的余额
    # 无返回值
    print("--------------------")
    deposit = int(input("请输入您要存放的金额："))
    conf = int(input("请确认金额："))
    # 避免负数出现
    if deposit <= 0:
        print("您输入的金额有误")
        print("--------------------")
        deposit(name, balance)
    # 确认金额一致
    elif deposit != conf:
        print("您两次输入的金额不一致")
        print("--------------------")
        deposit(name, balance)
    balance += conf
    print("--------------------")
    query(name, balance)
def withdraw(name, balance):
    # 取款函数，减少用户的余额
    # @param1 name：数据类型为str，用户姓名，用于传参
    # @param2 balance：数据类型为int，用户的余额
    # 无返回值
    print("--------------------")
    deposit = int(input("请输入您要取走的金额："))
    conf = int(input("请确认金额："))
    # 避免负数出现
    if deposit <= 0:
        print("您输入的金额有误")
        print("--------------------")
        withdraw(name, balance)
    # 确认金额一致
    elif deposit != conf:
        print("您两次输入的金额不一致")
        print("--------------------")
        withdraw(name, balance)
    balance -= conf
    print("--------------------")
    query(name, balance)
# 调用登录函数，启动程序
login()
```

- 登录与主菜单

> <img src="./IMG/Screenshot 2024-04-10 154216.png">

- 存款

> <img src="./IMG/Screenshot 2024-04-10 154311.png">

- 取款

> <img src="./IMG/Screenshot 2024-04-10 154348.png">

- 退出

> <img src="./IMG/Screenshot 2024-04-10 154433.png">

## 数据容器

数据容器就是可以存储多个数据的元素，这些数据类型可以是任意的，根据数据容器的特性，一般有五类：列表、元组、字符串、集合、字典

### 列表

列表类似于数组，可以一次性存储多个数据，且不限数据类型

**基本语法**

```py
# 定义空列表
list1 = []
list2 = list()
# 定义列表
list3 = [16118, "哈哈", True, list1]
```

列表的下标基本与数组一致，从0开始，但是列表的下标可以是负数，即指针从右向左移动，从-1开始

```py
# 定义列表
list1 = [1, 2, 3, 4, 5]
# 打印列表元素
print(list1[0])
print(list1[-5])
```

> <img src="./IMG/Screenshot 2024-04-10 161133.png">

### 列表的方法

list实际上是Python中已经定义好的一个类，其下有多个方法，用于操作list相关元素

- **查找下标**

`list.index(value)`

```py
# 定义列表
list1 = ["哈哈", "呵呵", "嘿嘿", "嗨嗨", "嚯嚯"]
# 查找列表元素下标
print("呵呵在list1中的下标为：", list1.index("呵呵"))
```

> <img src="./IMG/Screenshot 2024-04-10 162649.png">

- **插入元素**

`list.insert(sub, value)`

```py
# 定义列表
list1 = ["哈哈", "呵呵", "嘿嘿", "嗨嗨", "嚯嚯"]
# 插入元素
list1.insert(1, "嘻嘻")
for i in list1:
    print(i)
```

> <img src="./IMG/Screenshot 2024-04-10 163039.png">

- **追加元素**

追加元素就是将元素插入到列表尾部

`list.append(value)`

```py
# 定义列表
list1 = ["哈哈", "呵呵", "嘿嘿", "嗨嗨", "嚯嚯"]
# 插入元素
list1.append("嘻嘻")
for i in list1:
    print(i)
```

> <img src="./IMG/Screenshot 2024-04-10 163543.png">

也可以将一个数据容器内的元素追加到另一个数据容器

`list.extend(list)`

```py
# 定义列表
list1 = ["哈哈", "呵呵", "嘿嘿"]
list2 = ["嗨嗨", "嚯嚯"]
# 追加元素
list1.extend(list2)
print(list1)
```

> <img src="./IMG/Screenshot 2024-04-10 165305.png">

- **删除元素**

方式一：直接删除元素

`del(value)`

```py
# 定义列表
list1 = ["哈哈", "呵呵", "嘿嘿"]
# 删除元素
del list1[0]
print(list1)
```

> <img src="./IMG/Screenshot 2024-04-10 165453.png">

方式二：指定元素下标，取出元素，可以使用变量接收

`list.pop(sub)`

```py
# 定义列表
list1 = ["哈哈", "呵呵", "嘿嘿"]
# 删除元素
var = list1.pop(0)
print(list1, var)
```

> <img src="./IMG/Screenshot 2024-04-10 165942.png">

方式三：指定元素值，匹配列表中第一个该元素并移除

```py
# 定义列表
list1 = ["哈哈", "呵呵", "嘿嘿", "哈哈", "哈哈"]
# 删除元素
list1.remove("哈哈")
print(list1)
```

> <img src="./IMG/Screenshot 2024-04-10 170255.png">

- **清空列表**

方式一：使用方法

`list.clear()`

```py
# 定义列表
list1 = ["哈哈", "呵呵", "嘿嘿", "哈哈", "哈哈"]
# 清空列表
list1.clear()
print(list1)
```

> <img src="./IMG/Screenshot 2024-04-10 170514.png">

方式二：赋值空列表

`list = list()` 或 `list = []`

```py
# 定义列表
list1 = ["哈哈", "呵呵", "嘿嘿", "哈哈", "哈哈"]
# 清空列表
list1 = []
print(list1)
```

> <img src="./IMG/Screenshot 2024-04-10 170514.png">

- **统计指定元素数量**

`list.count(value)`

```py
# 定义列表
list1 = ["哈哈", "呵呵", "嘿嘿", "哈哈", "哈哈"]
# 统计元素
count = list1.count("哈哈")
print(count)
```

> <img src="./IMG/Screenshot 2024-04-10 170833.png">

- **统计列表中所有元素数量**

`len(list)`

```py
# 定义列表
list1 = ["哈哈", "呵呵", "嘿嘿", "哈哈", "哈哈"]
# 统计元素
count = len(list1)
print(count)
```

> <img src="./IMG/Screenshot 2024-04-10 171006.png">

### 元组

元组大致上与列表相同，但是元组一旦定义，其元素值将无法改变，且元组类下的方法只有 `tuple.index` 和 `tuple.count`

**基本语法**

```py
# 定义空元组
tuple1 = ()
tuple2 = tuple()
# 定义元组
tuple3 = (16118, "哈哈", True)
print(tuple3)
```

> <img src="./IMG/Screenshot 2024-04-10 171902.png">

*注：如果元组内只存在一个元素，需要在元素后添加一个逗号。如 `tuple1 = ("hello", )` ，否则将被识别为str类型*

### 字符串

字符串和元组一样，是不可修改的数据容器，其下的方法有 `str.index` 、 `str.replace` 、 `str.split` 、 `str.strip` 、 `str.count`

- 字符串替换

`str.replace(ori, str)`

```py
# 定义字符串
str1 = "哈哈哈，嘿哈，嘻哈"
# 替换字符串
str2 = str1.replace("哈", "嗨")
print(str2)
```

> <img src="./IMG/Screenshot 2024-04-10 173602.png">

- 字符串分割

`str.split(str)`

```py
# 定义字符串
str1 = "哈哈哈，嘿哈，嘻哈"
# 分割字符串
str2 = str1.split("，")
print(str2)
```

> <img src="./IMG/Screenshot 2024-04-10 174736.png">

*注：分割字符串得到的是一个列表对象*

- 字符串规整

字符串规整指的是在字符串的前后删减字符，数量不限，直至其他字符

`str.strip(str)`

```py
# 定义字符串
str1 = "哈哈哈，嘿哈，嘻哈"
# 字符串规整
str2 = str1.strip("哈")
print(str2)
```

> <img src="./IMG/Screenshot 2024-04-10 175606.png">

*注：若不指定参数，默认去除空格*

### 序列截取（切片）

列表、元组、字符串都属于序列，都可以通过特定方式截取，形成新序列

**基本语法**

`seq[sub_head:sub_foot:step]`

```py
# 定义序列
str1 = "123456789"
# 序列截取
str2 = str1[0:11:2]
print(str2)
```

> <img src="./IMG/Screenshot 2024-04-10 181451.png">

*注：sub_foot不包含本身，step可以为负数，同时sub_head和sub_foot也应该为负数*

### 集合

在Python中，集合是一种不允许重复元素存在的数据容器

**基本语法**

```py
# 定义空集合
set1 = set()
# 定义集合
set2 = {16118, "哈哈", True}
```

集合是一种无序容器，无法通过下标访问，但是可以修改，常用的方法有 `set.add()` 、 `set.remove()` 、 `set.pop()` 、 `set.clear()`

`set.add(value)`

```py
# 定义集合
set1 = {123, 456, 789}
# 添加数据
set1.add(111)
print(set1)
```

> <img src="./IMG/Screenshot 2024-04-11 080611.png">

此外，集合还有 `set.difference()` 、 `set_difference_update` 、 `set.union()` 等方法

- 取差集：取出set1中，不存在于set2的元素，返回值类型为set

`set1.difference(set2)`

```py
# 定义集合
set1 = {1, 2, 3, 4}
set2 = {1, 3, 5, 6}
# 取差集
set3 = set1.difference(set2)
print(set3)
```

> <img src="./IMG/Screenshot 2024-04-11 081346.png">

- 消差集：消除set1中存在于set2的元素

`set1.difference_update(set2)`

```py
# 定义集合
set1 = {1, 2, 3, 4}
set2 = {1, 3, 5, 6}
# 消差集
set1.difference_update(set2)
print(set1)
print(set2)
```

> <img src="./IMG/Screenshot 2024-04-11 081720.png">

- 合并集合，返回值类型为set

`set.union(set)`

```py
# 定义集合
set1 = {1, 2, 3, 4}
set2 = {1, 3, 5, 6}
# 合并集合
set3 = set1.union(set2)
print(set3)
```

### 字典

字典是一种存储键值对的数据容器，键不允许重复，只能通过键来访问，常用的方法有 `dict.pop()`  `dict.clear()`  `dict.keys()`  ``

**基本语法**

```py
# 定义空字典
dict1 = {}
dict2 = dict()
# 定义字典
dict3 = {"a": a, "b": b, "c": c,...}
```

- 获取全部key，返回值类型为dict_keys

`dict.keys()`

```py
# 定义字典
dict1 = {
    "name": "Kiiz",
    "age": 20,
    "gender": "male"
}
# 获取全部key
keys = dict1.keys()
print(keys)
```

> <img src="./IMG/Screenshot 2024-04-11 084821.png">

## 数据容器通用操作

**统计**

- `len(container)` ：统计元素数量

- `max(container)` ：统计最大值

- `min(container)` ：统计最小值

**类型转换**

- `list(container)` ：将容器转换为列表

- `tuple(container)` ：将容器转换为元组

- `str(container)` ：将容器转换为字符串

- `set(container)` ：将容器转换为集合

*字典在转换为非字符串类型时，值会丢失*

**其他操作**

- `sorted(container, [reverse = {True | False}]) ` ：将容器内元素进行排序，若reverse = True则进行倒序排序

## 函数进阶

### 多返回值

```py
# 定义函数
def func():
    return 1, "哈哈", False
a, b, c = func()
print(a, b, c)
```

> <img src="./IMG/Screenshot 2024-04-11 094154.png">

### 不定长参数

Python中，函数的参数可以不指定个数，使用 `*arg` 代替，即不定长参数，不定长参数的实质是在函数中创建一个参数元组，将接收到的参数存放在元组内

```py
# 不定长参数
def func(*arg):
    print(arg)
# 调用函数
func("hello, World")
func("Hello", "World")
```

> <img src="./IMG/Screenshot 2024-04-11 154731.png">

Python中，还存在一种不定长参数，使用 `**arg` 代替，这种不定长参数的实质是创建一个参数字典，因此接收的参数必须是键值对形式

```py
# 不定长参数
def func(**arg):
    print(arg)
# 调用函数
func(name = "Kiiz", age = 20, gender = "male")
```

### 匿名函数

#### 函数作为参数

Python中，允许函数作为另一个函数的参数

```py
# 定义函数
def my_func(func):
    result = func(1, 2)
    print(result)
def add(x, y):
    return x + y
# 调用函数
my_func(add)
```

> <img src="./IMG/Screenshot 2024-04-11 160920.png">

#### lambda（λ）匿名函数

**基本语法**

`lambda arg: statement`

lambda匿名函数没有函数名，只能调用一次，且只能存在一行语句

```py
# 定义函数
def my_func(func):
    result = func(1, 2)
    print(result)
# 调用函数，将参数设置为匿名函数
my_func(lambda x, y: x + y)
```

> <img src="./IMG/Screenshot 2024-04-11 160920.png">

---

# Python中级语法

## 文件操作

### 打开文件

在Python中，使用 `open(name, mode, encoding)` 函数打开文件，有三个参数， `name` 是要打开的文件名，数据类型为str， `mode` 是以何种模式打开文件，如只读r、写入w、追加a等，数据类型为str， `encoding` 是打开文件时使用的编码，一般使用utf-8，该参数的位置不是第三位，只能通过键值对的形式访问，数据类型为str，函数返回值类型为object

*注：如果以写入模式打开文件，文件原有内容会被删除，如果文件不存在，则会创建该文件*

### 文件读取

文件读取使用 `f.read()` 和 `f.readlines()` 方法

- `f.read(byte)` ：按指定字节数读取文件内容，不指定则默认读取文件内全部内容

```py
# 打开文件
f = open(r"D:\VS Code Files\Python\1.txt", "r", encoding = "utf-8")
# 读取文件
content = f.read()
print(content)
```

> <img src="./IMG/Screenshot 2024-04-11 165440.png">

- `f.readlines(line)` ：按行读取文件所有内容，并返回一个列表

```py
# 打开文件
f = open(r"D:\VS Code Files\Python\1.txt", "r", encoding = "utf-8")
# 读取文件
content = f.readlines()
print(content)
```

> <img src="./IMG/Screenshot 2024-04-11 165519.png">

*注：Windows系统目录分隔符 `\` 会被Python识别为转义字符的标识符，需要在字符串前添加字母 `r`*

此外，文件读取还有 `f.readline()` 方法，每次读取一行，返回值类型为str

### 文件关闭

Python中，文件关闭使用 `f.close()` 函数，也可以使用 `with open() as f:` 创建语句块，在语句结束后自动关闭文件

```py
# 打开文件
f = open(r"D:\VS Code Files\Python\1.txt", "r", encoding = "utf-8")
# 读取文件
content = f.readlines()
print(content)
# 关闭文件
f.close()
# 自动关闭文件
with open(r"D:\VS Code Files\Python\1.txt", "r", encoding = "utf-8") as f:
    content = f.readlines()
    print(content)

```

> <img src="./IMG/Screenshot 2024-04-11 171746.png">

### 综合训练2：单词统计

```py
# 打开文件
f = open(r"D:\VS Code Files\Python\1.txt", "r", encoding = "utf-8")
# 初始化计数
count = 0
# 遍历文本行
for line in f:
    # 清除开头和结尾的空格和换行符
    line = line.strip()
    # 将行列表中每一个元素使用空格分割成单词列表
    word_list = line.split(" ")
    # 统计单词出现的次数
    count += word_list.count("itheima")
# 关闭文件
f.close()
# 打印结果
print("在文件中，itheima出现的次数为：", count)
```

> <img src="./IMG/Screenshot 2024-04-11 175303.png">

### 文件写入（覆盖）

Python的文件写入操作分为两步，第一步使用 `f.write()` 方法将数据写入到内存缓冲区，第二步使用 `f.flush()` 将缓冲区的数据刷新到硬盘中，一般来说，为了减少硬盘的调用， `f.flush()` 只会出现在多条 `f.write()` 之后

```py
# 打开文件
f = open(r"D:\VS Code Files\Python\1.txt", "w", encoding = "utf-8")
# 写入数据
f.write("Hello, World!")
# 刷新内容
f.flush()
# 关闭文件
f.close()
```

> <img src="./IMG/Screenshot 2024-04-14 132619.png">

*注： `f.close()` 自带 `f.flush()` 的功能，可以不写 `f.flush`*

### 文件追加

文件追加操作只需要将 `f.open()` 中的第二个参数指定为 `a` 即可

```py
# 打开文件
f = open(r"D:\VS Code Files\Python\1.txt", "a", encoding = "utf-8")
# 追加数据
f.write("\n!dlroW ,olleH")
# 关闭文件
f.close()
```

> <img src="./IMG/Screenshot 2024-04-14 133906.png">

## Python异常

### 异常捕获

**基本语法**

```py
try:
except:
```

如果try下方的语句出现错误，则执行except下方的语句

```py
# 捕获异常
try:
    f = open("1.txt", "r", encoding = "utf-8")
except:
    print("文件不存在！")
```

> <img src="./IMG/Screenshot 2024-04-14 135656.png">

### 捕获特定异常

Python中，提供了捕获特定异常的方法

**基本语法**

```py
try:
except {<ErrorName>[ as alias] | (<ErrorName1>, <ErrorName2>,...)}:
```

- 捕获单个异常

```py
# 捕获特定异常
try:
    1 / 0
    f = open("1.txt", "r", encoding = "utf-8")
except FileNotFoundError as e:
    print("文件不存在！")
```

> <img src="./IMG/Screenshot 2024-04-14 141137.png">

- 捕获多个异常

```py
# 捕获特定异常
try:
    1 / 0
    f = open("1.txt", "r", encoding = "utf-8")
except (FileNotFoundError, ZeroDivisionError):
    print("除数不能为0")
    print("文件不存在！")
```

> <img src="./IMG/Screenshot 2024-04-14 141242.png">

### 异常捕获相关

- `else:` ：没有异常时执行的语句块

- `finally:` ：无论是否有异常，都强制执行的语句块

## 模块

### 引入模块

模块实际上是一种Python文件引用

**基本语法**

`[from module] import {module | [class | variable | function | *]} [as alias]`

```py
# Python休眠功能1
# 引入time模块
import time
# 调用休眠方法
time.sleep(5)
```

from可以将模块中的方法直接引入当前文件

```py
# Python休眠功能2
# 引入
from time import sleep
# 调用休眠方法
sleep(5)
```

### __name__变量

在测试Python模块时，可能会在模块文件内调用其函数，但是又不需要在引入模块时执行函数的调用，就需要用到 `__name__` 变量

数学模块：
```py
# 数学模块
# 加法
def add(x, y):
    print(x + y)
# 减法
def sub(x, y):
    print(x - y)
# 调用函数进行验证
if __name__ == "__main__":
    add(1, 2)
    sub(1, 2)
```

> <img src="./IMG/Screenshot 2024-04-14 172714.png">

引入文件：
```py
# 引入数学模块
import math_module
# 使用其下函数
math_module.add(3, 4)
math_module.sub(3, 4)
```

> <img src="./IMG/Screenshot 2024-04-14 173017.png">

### __all__变量

`__all__` 变量记录使用 `*` 的时候允许调用的函数的函数名，数据类型为list

数学模块：
```py
# 数学模块
# 限制通配符调用
__all__ = ["math_add"]
# 加法
def math_add(x, y):
    print(x + y)
# 减法
def math_sub(x, y):
    print(x - y)
```

引入文件：
```py
# 引入数学模块
from math_module import *
# 使用其下函数
math_add(3, 4)
math_sub(3, 4)
```

> <img src="./IMG/Screenshot 2024-04-14 174015.png">

## 包

Python包是Python模块的集合，物理上是一个包含了多个Python模块和一个 `__init__.py` 文件的文件夹

### 创建包

创建包的方法很简单，创建一个文件夹，并在文件夹内创建一个 `__init__.py` 文件，该文件夹就会被Python认为是Python包

## 综合案例3：自定义工具包

str_util:
```py
# 字符串反转函数
# @param1 str s 接收的字符串
# return str reverse_str 返回的字符串
def str_reverse(s):
    # 判断参数是否为字符串
    if type(s) != str:
        return "传入的不是字符串"
    # 定义反转的字符串
    reverse_str = ""
    for i in s:
        # 字符串反转
        reverse_str = i + reverse_str
    # 返回反转的字符串
    return reverse_str

# 字符串切片函数
# @param1 str s 接收的字符串
# @param2 int x 初始下标
# @param3 int y 结尾下标
# return str seq_str 返回被切片的字符串
def substr(s, x, y):
    # 判断参数是否为字符串
    if type(s) != str:
        return "传入的不是字符串"
    # 判断下标是否有效
    if x < 0:
        return "下标小于0"
    if y >= len(s):
        return "下标越界"
    # 字符串切片
    seq_str = s[x: y]
    # 返回被切片的字符串
    return seq_str

# 模块测试
if __name__ == "__main__":
    test = "abcdefg"
    test1 = str_reverse(test)
    print(test1)
    test1 = substr(test1, 1, 4)
    print(test1)
```

file_util：
```py
# 文件打印函数
# @param1 str file_name 文件的所在路径
# 无返回值
def print_file_info(file_name):
    # 检查路径数据类型是否为str
    if type(file_name) != str:
        return "路径有误"
    # 打开文件
    try:
        f = open(file_name, "r", encoding = "utf-8")
        # 遍历文件内容
        for i in f:
            # 消除换行符
            j = i.strip("\n")
            # 打印行
            print(j)
    # 异常捕获
    except:
        print("文件不存在")
    # 关闭文件
    finally:
        f.close()

# 文件写入函数
# @param1 str file_name 文件的所在路径
# @param2 str data 需要写入的数据
# 无返回值
def append_to_file(file_name, data):
    # 检查文件路径的数据类型
    if type(file_name) != str:
        return "路径有误"
    # 检查追加的数据的数据类型
    if type(data) != str:
        return "追加数据有误"
    # 打开文件
    f = open(file_name, "a", encoding = "utf-8")
    # 写入数据
    f.write(data + "\n")
    # 关闭文件
    f.close()

# 模块测试
if __name__ == "__main__":
    path = "D:/VS Code Files/Python/1.txt"
    data = "123456aaabbbccc"
    print_file_info(path)
    append_to_file(path, data)
```

主文件：
```py
# 引入包
import package.str_util
import package.file_util
# 构建相关信息
file = "D:/VS Code Files/Python/1.txt"
data = "ABC123456"
# 反转字符串
data = package.str_util.str_reverse(data)
print(data)
# 字符串切片
data = package.str_util.substr(data, 0, 6)
print(data)
# 写入文件
package.file_util.append_to_file(file, data)
# 打印文件
package.file_util.print_file_info(file)
```

> <img src="./IMG/Screenshot 2024-04-14 201511.png">

---

# 基础综合案例

## 折线图可视化

### json数据格式

json是一种轻量级的数据交互格式，可以按照json指定的格式去组织和封装数据来让不同的语言进行数据交互

### json数据的转化

在Python中，引入json模块就可以使用其下的方法进行json相关操作

```py
# 引入json模块
import json
# 准备json格式的数据
data = [{"name": "老王", "age": "16"}, {"name": "张三", "age": "20"}]
# 将Python数据转化为json数据
data = json.dumps(data)
print(data)
# 将json数据转化为Python数据
data = json.loads(data)
print(data)
```

> <img src="./IMG/Screenshot 2024-04-15 132551.png">

### Pyecharts模块

Pyecharts模块是一个开源的可视化图形开发框架，允许开发者使用Python语言进行数据可视化开发

**安装Pyecharts**

在cmd中输入

```shell
pip install pyecharts
```

**验证安装**

在Python解释器中输入 `import pyecharts` ，若没有任何报错，则说明安装成功

### 构建基础折线图

```py
# 导包，导入Line功能构建折线图对象
from pyecharts.charts import Line
# 获取折线图对象
line = Line()
# 添加x轴数据
line.add_xaxis(["China", "America", "Russia"])
# 添加y轴数据
line.add_yaxis("2022年GDP(单位:万亿)", [17.96, 25.44, 2.24])
# 生成图表
line.render()
```

> <img src="./IMG/Screenshot 2024-04-15 140149.png">

### 全局配置

Pyecharts中，提供了一些全局配置选项，在 `set_global_opts` 方法中，同时需要导入 `pyecharts.options` 中指定的功能来为相应配置提供选项

```py
# 导入Line功能构建折线图对象
from pyecharts.charts import Line
# 导入标题选项
from pyecharts.options import TitleOpts
# 导入图例选项
from pyecharts.options import LegendOpts
# 导入工具箱选项
from pyecharts.options import ToolboxOpts

# 获取折线图对象
line = Line()
# 添加x轴数据
# 添加x轴数据
line.add_xaxis(["China", "America", "Russia"])
# 添加y轴数据
line.add_yaxis("GDP(单位:万亿)", [17.96, 25.44, 2.24])
# 设置全局配置
line.set_global_opts(
    # 设置折线图标题
    title_opts = TitleOpts(title = "2022年中美俄三国GDP总量图", pos_left = "center", pos_bottom = "1%"),
    # 设置折线图图例
    legend_opts = LegendOpts(is_show = True),
    # 设置折线图工具箱
    toolbox_opts = ToolboxOpts(is_show = True)
)
# 生成图表
line.render()
```

> <img src="./IMG/Screenshot 2024-04-15 142426.png">

### 折线图制作

```py
# 导包
import json
from pyecharts.charts import Line
from pyecharts.options import TitleOpts
from pyecharts.options import LabelOpts

def formatting(data):
    '''
    : 字符串格式化函数
    : @param1 str data 需要格式化的字符串
    : return dict 返回被格式化的数据
    '''
    # 去除文件头尾部空格和换行符
    data.strip()
    # 去除文件头部其他字符
    while(1):
        i = 0
        if data[i] != "{":
            data = data[1::]
        else:
            break
    # 去除文件尾部其他字符
    data = data[::-1]
    while(1):
        i = 0
        if data[i] != "}":
            data = data[1::]
        else:
            break
    data = data[::-1]
    # 将json格式转换为Python字典
    data = json.loads(data)
    return data

def DateCut(xaxis, yaxis):
    '''
    : 截取序列从4月7日到12月31日函数
    : @param1 list xaxis 需要截取的x轴
    : @param2 list yaxis 需要截取的y轴
    : retrun1 list 返回被截取的x轴
    : retrun2 list 返回被截取的y轴
    '''
    # 获取4月7日的下标
    head = xaxis.index("4.7")
    # 获取12月31日的下标
    tail = xaxis.index("12.31")
    # 将x轴和y轴的数据截取4月7日到12月31日
    return xaxis[head: tail + 1:], yaxis[head: tail + 1:]

# 导入数据
f_us = open("D:/VS Code Files/Python/Data/line_chart/America.txt", "r", encoding = "utf-8")
f_in = open("D:/VS Code Files/Python/Data/line_chart/India.txt", "r", encoding = "utf-8")
f_jp = open("D:/VS Code Files/Python/Data/line_chart/Japan.txt", "r", encoding = "utf-8")
data_us = f_us.read()
data_in = f_in.read()
data_jp = f_jp.read()
# 关闭文件
f_us.close()
f_in.close()
f_jp.close()
# 格式化数据
data_us = formatting(data_us)
data_in = formatting(data_in)
data_jp = formatting(data_jp)
# 获取目标字典
data_us = data_us["data"][0]["trend"]
data_in = data_in["data"][0]["trend"]
data_jp = data_jp["data"][0]["trend"]
# 获取日期数据，作为x轴
xaxis_us = data_us["updateDate"]
xaxis_in = data_in["updateDate"]
xaxis_jp = data_jp["updateDate"]
# 获取确诊人数，作为y轴
yaxis_us = data_us["list"][0]["data"]
yaxis_in = data_in["list"][0]["data"]
yaxis_jp = data_jp["list"][0]["data"]
# 数据截取
(xaxis_us, yaxis_us) = DateCut(xaxis_us, yaxis_us)
(xaxis_in, yaxis_in) = DateCut(xaxis_in, yaxis_in)
(xaxis_jp, yaxis_jp) = DateCut(xaxis_jp, yaxis_jp)
# 生成图表
line = Line()
line.add_xaxis(xaxis_us)
line.add_yaxis("美国确诊人数", yaxis_us, label_opts = LabelOpts(is_show = False))
line.add_yaxis("印度确诊人数", yaxis_in, label_opts = LabelOpts(is_show = False))
line.add_yaxis("日本确诊人数", yaxis_jp, label_opts = LabelOpts(is_show = False))
line.set_global_opts(
    title_opts = TitleOpts(title = "美印日三国疫情确诊人数", pos_left = "center", pos_bottom = "1%"),
)
line.render()
```

> <img src="./IMG/Screenshot 2024-04-16 102423.png">

## 地图可视化

### 创建基础地图

```py
# 导包
from pyecharts.charts import Map
from pyecharts.options import TitleOpts
from pyecharts.options import VisualMapOpts

# 获取map对象
map = Map()
# 构建数据
data = [
    ("北京市", 7),
    ("天津市", 54),
    ("上海市", 304),
    ("广东省", 88),
    ("台湾省", 1),
    ("四川省", 122),
    ("重庆市", 729)   
]
# 创建地图
map.add("统计地区", data, "china")
# 设置全局配置
map.set_global_opts(
    # 设置标题
    title_opts=TitleOpts(title="我国各省疫情确诊人数", pos_left="center", pos_bottom="1%"),
    # 设置图例
    visualmap_opts=VisualMapOpts(
        # 开启图例
        is_show=True,
        # 图例分段
        is_piecewise=True,
        # 设置图例参数
        pieces=[
            {"min": 1, "max": 9, "label": "1-9人", "color": "#CCFFFF"},
            {"min": 10, "max": 99, "label": "10-99人", "color": "#FFFF99"},
            {"min": 100, "max": 299, "label": "100-299人", "color": "#FF6666"},
            {"min": 300, "max": 599, "label": "300-599人", "color": "#CC3333"},
            {"min": 600, "max": 899, "label": "600-899人", "color": "#990033"}
        ]
    )
)
# 生成地图
map.render()
```

> <img src="./IMG/Screenshot 2024-04-16 110042.png">

### 制作全国疫情地图

```py
# 全国各省份疫情确诊人数可视化地图
# 导包
import json
from pyecharts.charts import Map
from pyecharts.options import TitleOpts
from pyecharts.options import VisualMapOpts

# 打开文件
f = open("D:/VS Code Files/Python/Data/map/epidemic.txt", "r", encoding="utf-8")
data = f.read()
# 关闭文件
f.close()
# 将json数据转换为Python字典
data = json.loads(data)
# 确定各省份信息位置
data = data["areaTree"][0]["children"]
# 获取地图对象
map = Map()
# 构建数据
map_data = []
for p in data:
    province_data = []
    province_data.insert(0, p["name"])
    province_data.insert(1, p["total"]["confirm"])
    map_data.append(province_data)
# 创建地图
map.add("确诊人数(单位：人)", map_data, "china")
# 设置全局配置
map.set_global_opts(
    # 设置标题
    title_opts=TitleOpts(title="全国各省份疫情确诊人数可视化地图", pos_left="center", pos_bottom="1%"),
    # 设置图例
    visualmap_opts=VisualMapOpts(
        # 开启图例
        is_show=True,
        # 图例分段
        is_piecewise=True,
        # 设置图例参数
        pieces=[
            {"min": 1, "max": 9, "label": "1-9人", "color": "#CCFFFF"},
            {"min": 10, "max": 99, "label": "10-99人", "color": "#FFFF99"},
            {"min": 100, "max": 999, "label": "100-999人", "color": "#FF6666"},
            {"min": 1000, "max": 9999, "label": "1000-9999人", "color": "#CC3333"},
            {"min": 10000, "max": 99999, "label": "10000-99999人", "color": "#990033"}
        ]
    )
)
# 生成地图
map.render()
```

> <img src="./IMG/Screenshot 2024-04-16 114930.png">

### 制作省级疫情地图

```py
# 四川省疫情确诊人数可视化地图
# 导包
import json
from pyecharts.charts import Map
from pyecharts.options import *

# 打开文件，读取数据
f = open("D:/VS Code Files/Python/Data/map/epidemic.txt", "r", encoding="utf-8")
data = f.read()
# 关闭文件
f.close()
# 将数据转化为字典
data = json.loads(data)
# 确定四川省数据位置
data = data["areaTree"][0]["children"][12]["children"]
# 获取地图对象
map = Map()
# 构建数据
map_data = []
for i in data:
    if i["name"] == "境外输入" or i["name"] == "地区待确认":
        continue
    list_unit = []
    list_unit.append(i["name"] + "市")
    list_unit.append(i["total"]["confirm"])
    map_data.append(list_unit)
# 创建地图
map.add("确诊人数(单位：人)", map_data, "四川")
# 设置全局配置
map.set_global_opts(
    # 设置标题
    title_opts=TitleOpts(title="四川省疫情确诊人数可视化地图", pos_left="center", pos_bottom="1%"),
    # 设置图例
    visualmap_opts=VisualMapOpts(
        # 开启图例
        is_show=True,
        # 图例分段
        is_piecewise=True,
        # 设置图例参数
        pieces=[
            {"min": 1, "max": 19, "label": "1-19人", "color": "#CCFFFF"},
            {"min": 20, "max": 39, "label": "20-39人", "color": "#FFFF99"},
            {"min": 40, "max": 59, "label": "40-59人", "color": "#FF6666"},
            {"min": 60, "max": 79, "label": "60-79人", "color": "#CC3333"},
            {"min": 80,  "label": "80人及以上", "color": "#990033"}
        ]
    )
)
# 生成地图
map.render()
```

> <img src="./IMG/Screenshot 2024-04-17 133156.png">

## 柱状图可视化

### 基础柱状图

```py
# 导包
from pyecharts.charts import Bar

# 获取对象
bar = Bar()
# 创建柱状图
bar.add_xaxis(["中国", "美国", "俄罗斯"])
bar.add_yaxis("GDP(单位:万亿)", [17.96, 25.44, 2.24])
# 生成地图
bar.render()
```

> <img src="./IMG/Screenshot 2024-04-17 133913.png">

*注：可以使用 `bar.reversal_axis()` 方法来反转xy轴*

### 创建时间线

```py
# 导包
from pyecharts.charts import Bar, Timeline
from pyecharts.options import *
from pyecharts.globals import ThemeType

# 定义柱状图生成函数
def bar_creator(xaxis, yaxis):
    '''
    : 柱状图生成函数
    : @param1 sequence xaxis x轴数据
    : @param2 sequence yaxis y轴数据
    : return object bar 返回已经构建好的柱状图对象
    '''
    # 获取对象
    bar = Bar()
    # 创建柱状图
    bar.add_xaxis(xaxis)
    bar.add_yaxis("GDP(单位:万亿)", yaxis, label_opts=LabelOpts(position="right"))
    # 反转xy轴
    bar.reversal_axis()
    # 返回柱状图
    return bar

# 获取时间轴对象
timeline = Timeline({"theme": ThemeType.LIGHT})
# 构建数据
# x轴数据
country = ["中国", "美国", "俄罗斯"]
# y轴数据列表
gdp = [
    [30, 20, 10],
    [37, 21, 16],
    [44, 26, 29]
]
# 遍历y轴列表，有几个元素就创建几个柱状图
i = 0
bar_list = []
while(i < len(gdp)):
    bar_list.append(bar_creator(country, gdp[i]))
    i += 1
# 为时间轴添加柱状图
year = 2020
for i in bar_list:
    timeline.add(i, str(year) + "年")
    year += 1
# 播放设置
timeline.add_schema(
    # 播放间隔
    play_interval=1200,
    # 是否自动播放
    is_auto_play=False,
    # 是否显示时间轴
    is_timeline_show=True,
    # 是否循环播放
    is_loop_play=False
)
# 通过时间轴生成柱状图
timeline.render()
```

### 制作动态柱状图

#### sort方法补充

在Python中， `sequence.sort(key, reverse)` 方法可以用来给序列排序，同时，该方法也可以输入两个参数， `key` 数据类型为None，指定排序时参考的键， `reverse` 数据类型为bool，指定排序的顺序，False表示升序，True表示降序

```py
# sort补充
# 构建数据
a = [['a',1], ['b',2], ['c',3]]
# 定义一个匿名函数，返回参数的第二个元素，指定该元素作为排序依据
a.sort(key=lambda list:list[1], reverse=True)
print(a)
```

> <img src="./IMG/Screenshot 2024-04-17 144457.png">

#### 制作1960-2019年世界各国GDP总量动态柱状图

```py
# 制作1960-2019年世界各国GDP总量动态柱状图
# 导包
from pyecharts.charts import Bar, Timeline
from pyecharts.globals import ThemeType
from pyecharts.options import *

# 构建柱状图生成函数
def bar_creator(list, year):
    '''
    : 柱状图生成函数
    : @param1 list list 数据列表
    : @parma2 str year 当前年份
    : return object bar 返回已经构建好的柱状图对象
    '''
    # 获取gdp前8的数据
    country = []
    gdp = []
    count = 0
    for i in list:
        if count > 8:
            break
        country.append(i[0])
        gdp.append(int(i[1] / 100000000))
        count += 1
    # 获取柱状图对象
    bar = Bar()
    # 反转数据
    country.reverse()
    gdp.reverse()
    # 创建柱状图
    bar.add_xaxis(country)
    bar.add_yaxis("GDP总量(单位：亿元)", gdp, label_opts=LabelOpts(position="right"))
    # 反转xy轴
    bar.reversal_axis()
    # 设置标题
    bar.set_global_opts(
        title_opts=TitleOpts(title=f"{year}年全球GDP前八名")
    )
    # 返回柱状图
    return bar

# 数据处理
# 打开文件
f = open("D:/VS Code Files/Python/Data/bar_chart/1960-2019全球GDP数据.csv", "r", encoding="ANSI")
# 按行读取文件内容，并将文件首行删去
data = f.readlines()[1::]
# 关闭文件
f.close()
# 定义数据字典
data_dict = {}
# 遍历数据行
for i in data:
    # 去除换行符
    i.strip()
    # 取出年份、国家、gdp数据
    year = int(i.split(",")[0])
    country = i.split(",")[1]
    gdp = float(i.split(",")[2])
    try:
        # 尝试将国家和gdp数据插入到相应年份中
        data_dict[year].append([country, gdp])
    except:
        # 尝试失败，在字典中创建对应年份的键值对
        data_dict[year] = []
        # 插入数据
        data_dict[year].append([country, gdp])
    finally:
        # 将数据进行降序排序
        data_dict[year].sort(key=lambda list:list[1], reverse=True)
# 获取时间轴对象
timeline = Timeline({"theme": ThemeType.LIGHT})
# 为时间轴添加柱状图
# 获取所有年份
year = data_dict.keys()
# 遍历年份
for i in year:
    # 为所有年份创建柱状图
    bar = bar_creator(data_dict[i], str(i))
    # 将所有年份的柱状图添加到时间轴中
    timeline.add(bar, str(i) + "年")
# 时间轴播放设置
timeline.add_schema(
    # 播放间隔
    play_interval=1200,
    # 是否自动播放
    is_auto_play=False,
    # 是否显示时间轴
    is_timeline_show=True,
    # 是否循环播放
    is_loop_play=False
)
# 通过时间轴生成柱状图
timeline.render()
```

> <img src="./IMG/Screenshot 2024-04-17 165334.png">
