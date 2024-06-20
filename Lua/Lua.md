# Lua

`更新时间：2024-6-20`

---

# Lua简介

lua是一种轻量小巧的脚本语言，用标准C语言编写并以源代码形式开放， 其设计目的是为了嵌入应用程序中，从而为应用程序提供灵活的扩展和定制功能

# Lua Basic

## Lua基础语法

lua是一款弱类型轻量级脚本语言，声明变量不使用关键字，句末不使用分隔符，不使用大括号作为语句块标识符，但是需要使用end作为结束标志

```lua
-- 声明变量
a = 1
b = 0.1
c = 1e5

-- 定义函数
function hello(arg)
    print("Hello, "..arg)
end

hello(a)
hello(b)
hello(c)
```

> <img src="./IMG/Screenshot 2024-06-18 182547.png">

## 注释

lua中，使用 `--` 作为单行注释， `--[[content]]` 作为多行注释

```lua
-- 单行注释1
-- 单行注释2
--[[
    多行注释1
    多行注释2
]]
```

## 数据类型

- `nil` ：nil在lua中作为NULL使用，其布尔值为false，当变量的值为nil时，一般认为该变量不存在。当需要判断变量的值是否为nil时，需要使用双引号 `"nil"`

- `boolean` ：布尔值，只有true和false

- `number` ：数值类型，实质是双精度浮点，lua中所有的数值类型都是number

- `string` ：字符串

- `function` ：函数

- `table` ：类似于数组的类型，其索引值可以是数字，也可以是键，并且键值对不占用数值索引，lua的数值索引从1开始，可以使用负数，其键索引可以使用 `table['key']` 的方式，也可以使用 `table.key` 的方式

```lua
-- 定义各种数据类型的变量
var2 = true
var3 = 10
var4 = 'Hello'
var5 = {1, 'abc', name = 'Jack', false}

-- 不存在的变量值均为nil
print(var1)

-- table的键不占用数值索引
print(var5[3])
```

> <img src="./IMG/Screenshot 2024-06-18 184232.png">

## 变量

### 字符串

#### 定义

lua中提供了三种定义字符串的方式 `'string'` 、 `"string"` 和 `[[string]]`，其中 `[[string]]` 方式可以定义多行字符串，并且保留格式

```lua
str1 = 'str1'
str2 = "str2"
str3 = [[
    str1
        str2
    str3
]]
print(str1)
print(str2)
print(str3)
```

> <img src="./IMG/Screenshot 2024-06-18 205655.png">

#### 长度获取

lua中字符串长度的获取可以通过运算符 `#` 和内置函数 `string.len()`

```lua
str = 'hello, world'
print(#str)
print(string.len(str))
```

> <img src="./IMG/Screenshot 2024-06-18 210043.png">

### 变量作用域

lua中变量的作用域默认都是全局，只有使用了关键字 `local` 时才会被认为是局部变量

```lua
function func()
    -- 全局变量
    a = 10
    -- 局部变量
    local b = 20
end

func()
-- 访问变量
print(a)
print(b)
```

> <img src="./IMG/Screenshot 2024-06-18 185205.png">

*这里为什么有个func()呢？*

## 循环

lua提供了三种循环供开发者使用，分别是 `while` 、 `repeat...until` 和 `for` ，同时也提供了 `break` 用于循环控制

### While

lua中的while循环与C一致

**基本语法**

```lua
while <Conditional Expression> do
    <Loop Body>
end
```

**案例**

```lua
local i = 0
while i < 5 do
    print(i)
    i = i + 1
end
```

> <img src="./IMG/Screenshot 2024-06-18 185720.png">

### Repeat...Until

repeat..until循环类似于do..while，但是lua中的until是循环终止条件

**基本语法**

```lua
repeat
    <Loop Body>
until <Loop Exit Conditional Expression>
```

**案例**

```lua
local i = 0
repeat
    print(i)
    i = i + 1
until i >= 5
```

> <img src="./IMG/Screenshot 2024-06-18 190012.png">

### For

lua中，for循环提供了两种使用方式，一种是数值for循环，即循环控制依靠变量的值，另一种是泛型for循环，循环通过迭代器遍历表

#### 数值型For

**基本语法**

```lua
for <Initial Value Assignment Expression>, <End>(Closed Interval)[, <Step>] do
    <Loop Body>
end
```

**案例**

```lua
for i = 0, 10, 2 do
    print(i)
end
```

> <img src="./IMG/Screenshot 2024-06-18 191337.png">

#### 泛型For

**基本语法**

```lua
for <key>, <value> in pairs(<Iterable Variable>) do
    <Loop Body>
end
```

**案例**

```lua
local name_list = {'Jack', 'Tom', 'Sara', 'Pudding'}
for key, value in pairs(name_list) do
    print(key .. ':' .. value)
end
```

> <img src="./IMG/Screenshot 2024-06-18 192128.png">

*注：这里的 `pairs()` 是一个迭代器函数，用来迭代可迭代变量*

## 流程控制

lua中使用 `if` 、 `else` 、 `elseif` 来进行流程控制

**基本语法**

```lua
if <Conditional Expression> then
    <Statement Block>
elseif <Conditional Expression> then
    <Statement Block>
else
    <Statement Block>
end
```

**案例**

```lua
print('Give me a num: ')
while 1 do
    local num = io.read('*n')
    if num == 1 then
        print('yes')
        break
    elseif num < 1 and num > 0 then
        print('almost, once again: ')
    else
        print('not yet, again: ')
    end
end
```

> <img src="./IMG/Screenshot 2024-06-18 194414.png">

## 函数

lua的函数定义需要使用关键字 `function` ，可以指定函数作用域，可选是否存在返回值和参数

**基本语法**

```lua
[local] function [Function Name] ([Parameters...])
    <Function Body>
    [return <Returned Values...>]
end
```

**案例**

```lua
local function sum(num1, num2)
    return num1 + num2
end

local function sub(num1, num2)
    return num1 - num2
end

local function operator(num1, num2, algorithm)
    local result = algorithm(num1, num2)
    return result
end

local a = 10
local b = 20
print(operator(a, b, sum))
print(operator(a, b, sub))
```

> <img src="./IMG/Screenshot 2024-06-18 195659.png">

### 匿名函数

在lua中，没有函数名的函数被称为匿名函数，匿名函数可以作为其他函数的参数，也可以被变量接收

```lua
-- 定义匿名函数
local sum = function (num1, num2) 
    return num1 + num2
end

-- 调用匿名函数
local a = 10
local b = 5
print(sum(a, b))

-- 匿名函数作为参数
local function echo(func)
    print('Hello' .. func())
end

echo(
    function ()
        return ', World'
    end
)
```

> <img src="./IMG/Screenshot 2024-06-18 200608.png">

### 可变参数

lua中，若不确定参数的数量，可以使用可变参数 `...` 来代替，同时 `{...}` 可以用来表示该参数表

```lua
local function average(...)
    local result = 0
    local arg = {...}
    for key, value in ipairs(arg) do
       result = result + value
    end
    print("总共传入 " .. #arg .. " 个数")
    return result / #arg
end

print("平均值为", average(10,5,3,4,5,6))
```

> <img src="./IMG/Screenshot 2024-06-18 204623.png">

## 运算符

lua中的运算符与C基本相同，以下列举lua中独特的运算符

- `..` ：字符串连接符，用于连接两个字符串

- `#` ：长度运算符，可以获取表或字符串的长度

---

# Lua Medium

## 表

### 表指针

在lua中，表变量保存的只是表的内存地址，如果存在多个变量储存同一表地址的情况下，对任一变量赋值为nil，不影响其他变量，即表变量实质为表指针

```lua
local table1 = {name = 'kiiz'}
local table2 = table1
print(table1, table2)
table1 = nil
print(table1, table2)
```

> <img src="./IMG/Screenshot 2024-06-19 203610.png">

### 常见方法

- `table.concat(table[, sep, start, end])` ：对表中的字符串进行拼接操作， `sep` 为分隔符

- `table.insert(table[, pos], value)` ：插入元素到表中

- `table.remove(table[, pos])` ：移除表元素

- `table.sort(table[, comp])` ：对表进行排序，可以指定排序方式

```lua
local table1 = {'henrin', 'kiiz'}
table.insert(table1, 2, 'oceuk')
table.remove(table1, 1)
table.sort(table1)
```

> <img src="./IMG/Screenshot 2024-06-19 205455.png">

### table.sort()高级用法

`table.sort()` 方法中可以自定义排序方式，但是排序方式需要使用一个函数来完成，如果要使排序表达式生效，返回值只能为 `true`

- 逆序排序实现

```lua
local table1 = {'c', 'd', 'a', 'b'}
table.sort(table1)
for k, v in pairs(table1) do
    print(v)
end
print('\n')
table.sort(table1, function (a, b)
    if a > b then
        return true
    end
    return false
end)
for k, v in pairs(table1) do
    print(v)
end
```

> <img src="./IMG/Screenshot 2024-06-19 210316.png">

- 二维表排序实现

```lua
local function sort(a, b)
    if a.level > b.level then
        return true
    elseif a.level == b.level then
        if a.salary > b.salary then
            return true
        end
    end
    return false
end

local staff = {
    {id = 1, name = 'Kiiz', salary = 15000, level = 1},
    {id = 2, name = 'Oceuk', salary = 14000, level = 1},
    {id = 3, name = 'Henrin', salary = 23000, level = 2},
    {id = 4, name = 'Ocean', salary = 17000, level = 1},
}

table.sort(staff, sort)

for k, v in pairs(staff) do
    print('id=' .. v.id .. ' name=' .. v.name .. ' salary=' .. v.salary .. ' level=' .. v.level)
end
```

> <img src="./IMG/Screenshot 2024-06-19 212009.png">

## 错误处理

lua中提供了 `assert()` 、 `error()` 、 `pcall()` 、 `xpcall()` 、 `debug()` 来进行错误处理

### assert()

**基本语法**

`assert(<Expression>[, <Massage>])`

当 `<Expression>` 的值为false或nil时，函数报错，若指定了 `[Massage]` ，则先输出 `[Massage]` ，再输出错误信息；若没有指定 `[Massage]` ，先输出 `assertion failed!` ，再输出错误信息。当函数 `assert()` 抛出错误后，程序会被终止

**案例**

```lua
local function get_input()

    local num1
    local num2

    print('input num1: ')
    num1 = io.read('n')
    assert(type(num1) == 'number', 'num1 isnt a number')
    print('input num2: ')
    num2 = io.read('n')
    assert(type(num2) == 'number')

    return num1, num2
end

get_input()
```

> <img src="./IMG/Screenshot 2024-06-20 204134.png">

### error()

**基本语法**

`error(<Massage>[, <Error Level>])`

当 `error()` 函数被调用时，程序会立即终止，并抛出错误信息 `<Massage>` ，如果指定了错误等级 `[Error Level]` ，会按照相应等级来抛出信息，函数没有返回值

**错误等级**

- 0：不添加任何位置信息

- 1：添加 `error()` 函数的所在文件和行号

- 2：添加 `error()` 函数所在函数名

**案例**

```lua
local function get_input()

    local num1
    local num2

    print('input num1: ')
    num1 = io.read('n')
    if type(num1) ~= 'number' then
        error('num1 must be a number', 0)
    end
    print('input num2: ')
    num2 = io.read('n')
    if type(num2) ~= 'number' then
        error('num2 must be a number', 2)
    end

    return num1, num2
end

get_input()
```

> <img src="./IMG/Screenshot 2024-06-20 205227.png">

### pcall()

**基本语法**

`pcall(<Debug Function>[, <parameters...>])`

`pcall()` 函数会执行传入的函数，如果传入的函数有参数，应在 `[parameters]` 处指定。如果传入的函数执行成功，会返回true和函数的返回值，执行失败则会返回false和错误信息

**案例**

```lua
local function sum(a, b)

    return a + b
end

local function input()

    local a = io.read()
    local b = io.read()

    return a, b
end

local a, b = input()
local code, reason = pcall(sum, a, b)
print(code, reason)
```

> <img src="./IMG/Screenshot 2024-06-20 211851.png">