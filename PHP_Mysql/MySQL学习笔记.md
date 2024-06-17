# Ki1z's MySQL学习笔记

`更新时间：2023-11-5`

注释解释：

- `<>`必填项，必须在当前位置填写相应数据

- `{}`必选项，必须在当前位置选择一个给出的选项

- `[]`可选项，可以选择填写或忽略

# 数据库介绍

## 数据库概念

数据库是按照数据结构来组织，存储和管理数据的建立在计算机存储设备上的仓库

## 数据库分类

**网络数据库**

借助于网络，将数据存储在网络上进行有效管理，并实现用户与网络中的数据库进行实时动态数据交互

**层级数据库**

层次模型实质上是一种有根节点的定向有序树

*在数学中“树”被定义为一个无回的连通图*

**关系数据库**

关系数据库是建立在关系模型上的数据库，借助于集合代数等数学概念和方法来处理数据库中的数据

**其他方式**

数据库的另外一种分类方式，通过存储介质分类，即硬盘和内存

- 关系型数据库：存储在硬盘中

- 非关系型数据库：存储在内存中

## 关系型数据库

**基本概念**

关系型数据库，建立在关系模型上的数据库，借助于集合代数等数学概念和方法来处理数据库中的数据。现实世界中的各种实体以及实体之间的各种联系均用关系模型表示。关系模型是由埃德加·科德于1970年首先提出的并配合“科德十二定律”。现如今虽然对此模型有一些批评意见，但它还是数据存储模型的传统标准，关系模型由`关系数据结构`、`关系操作集合`、`关系完整性约束`三部分组成

- 关系数据结构：指数据以什么方式来存储，是一种二维表的形式存储
  
> 本质：二维表
> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/0OD_PV~9)JE6%7D4OBEXUB)VM.png?raw=true">

- 关系操作集合：如何来关联个管理对应的存储数据，SQL指令。

> 获取张三的年纪，已知条件为姓名
> ```php
> select 年龄 from 二维表 where 姓名 = 张三
> ```

- 关系完整性约束：数据内部有对应的关联关系，以及数据与数据之间也有对应的关联关系。

  - 表内约束：对应的具体列只能放对应的数据（不能乱放）

  - 表间约束：自然界各实体间都是有着对应的关联关系（外键）

**典型的关系型数据库**

Oracle , DB2 , Microsoft SQL Server , Microsoft Access , MySQL , SQLite

- 小型关系型数据库：Microsoft Access , SQLite

- 中型关系型数据库：Microsoft SQL Server , MySQL

- 大型关系型数据库：Oracle , DB2

*MySQL当前跟Oracle是一个公司，隶属于Oraccle*

---

# SQL介绍

## SQL基本介绍

Structured Query Language，结构化查询语言，简称SQL，是一种特殊目的的编程语言，是一种数据库查询和程序设计语言，用于存取数据以及查询、更新和管理关系数据库系统，同时也是数据库脚本文件的扩展名

*SQL就是专门为关系型数据库而存在的*

## SQL分类

**数据查询语言** `Data Query Language`

其语句，也称为“数据检索语句”，用以从表中获得数据，确定数据是怎样在应用程序给出。保留字 `SELECT` 是DQL（也是所有SQL）使用最多的动词，其他DQL常用的保留字有 `WHERE` , `ORDER BY` , `GROUP BY` 和 `HAVING` 。这些DQL保留字常与其他类型的SQL一起使用。

*专门用于查询数据*

**数据操作语言** `Data Manipulation Language`

其语句包括动词 `INSERT` , `UPDATE` 和 `DELETE` 。它们分别用于修改和删除表中的行。也成为动作查询语句。

*专门用于写数据*

**事务处理语言** `Transaction Control Language`

它的语句能确保被DML所影响的表的所有行及时得以更新。TPL语句包括 `BEGIN` , `TRANSACTION` , `COMMIT` 和 `ROLLBACK` 。（不是所有的关系型数据库都提供事务安全处理）

*专门用于事务安全处理*

**数据控制语言** `Data Control Language`

它的语句通过 `GRANT` 和 `REVOKE` 获得许可，确定单个用户和用户组对数据对象的访问。它的某些RDBMS可用 `GRANT` 和 `REVOKE` 控制对表单个列的访问。

*专门用于权限管理*

**数据定义语言** `Data Definition Language`

其语句包括动词 `CREATE` 和 `DROP` 。在数据库中创建新表或删除表，为表加入索引等。DDL包括许多与人数据库目录中获得数据有关的保留字。它也是动作查询的一部分。

---

# MySQL介绍

## MySQL基本介绍

MySQL是一个关系型数据库管理系统，由瑞典MySQL AB公司研发，目前属于Oracle旗下产品。MySQL是最流行的关系型数据库管理系统之一，在WEB应用方面，MySQL是最好的RDBMS（Relational Database Management System 关系数据库管理系统）应用软件。 

- MySQL是一款开源免费的产品

- MySQL对PHP的支持是最好的

*MySQL中用到的指令就是SQL指令*

## 启动和停止MySQL服务

MySQL是一种C/S结构：客户端和服务端

服务端对应的软件：Mysqld.exe

**命令行方式**

通过Windows下打开CMD控制台，然后使用命令进行管理

`Net start 服务(mysql)` ：开启服务
`Net stop 服务(mysql)`：停止服务

**系统服务方式**

*前提：在安装MySQL的时候将其添加到了Windows的服务中*

- 方式 1：进入服务

  > 右键此计算机 > 管理 > 服务和应用程序 > 服务 > 找到MySQL > 启动

- 方式 2：通过命令行

  > `Win` + `R` > 输入 `services.msc` > 找到MySQL > 启动

## 退出和登录MySQL系统

通过客户端（mysql.exe）与服务器进行连接认证，就可以进行操作

*通常：服务端与客户端不在同一台电脑上*

**登录**

1. 找到mysql.exe

  *通过cmd控制台，如果在安装的时候指定了mysql.exe所在的路径为环境变量，就可以直接访问；如果没有，就必须进入mysql.exe所在路径*

2. 输入对应的服务器地址 `-h`

  *-h代表host，举例：-h[IP地址/域名]*

3. 输入对应服务器中MySQL监听的端口 `-P`

  *-P代表port，举例：-P:3306，<b>此处的P必须大写</b>*

4. 输入用户名 `-u`

  *-u代表user，举例：-u:root*

5. 输入密码 `-p`

  *-p代表password，举例：-p:root，<b>此处的p必须小写</b>*

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/@A6(R6C{X]E4@@E2O$1(%EC.png?raw=true">

**连接认证基本语法**

```
Mysql.exe/mysql -h127.0.0.1 -P3306 -uroot -proot
```

注意事项

- 通常端口都可以默认，MySQL的默认监听端口通常都是3306

- 密码的输入可以先输入-p，直接换行，然后再以密文的方式输入密码

**退出**

断开与服务器的连接，通常MySQL服务器数量有限，一旦客户端用完，建议断开

- 建议方式：使用SQL提供的指令

  - `Exit;` *Exit后分号不能省略*
  - `\q;` *Quit的缩写*
  - `Quit;`
> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/W0ZAXBR~%{HXE$0}]K{`@2N.png?raw=true">

- 其他方式：直接关闭cmd窗口

## MySQL服务端架构

MySQL服务端架构由以下几层构成：

- 数据库管理系统（最外层） `Database Management System` ，专门管理服务器端的所有内容

- 数据库（第二层） `Database` ，专门用于储存数据的仓库

- 二维数据表（第三层） `Table` ，专门用于存储具体实体的数据

- 字段（第四层） `Field` ，具体存储某种类型的数据（实际存储单元）

服务端常用关键字

- `Row` 行

- `Column` 列，对应 `Field`

---

# 数据库基本操作

数据库是数据存储的最外层（最大单元）

> DBMS（数据库管理系统） > DB（数据库） > Table（表） > Field（字段）

## 创建数据库

**基本语法**

> ```sql
> create database <databasename> [databaseoption];
> ```
> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/8K7LY6@72GW@TU1XXBM`YSO.png?raw=true">

**库选项 DatabaseOption**

数据库的相关属性

- `charset` 字符集，代表着当前数据库下的所有表存储的数据默认的指定的字符集

```sql
create database <databasename> charset {charsetname};
```

- `collate` 校对集，代表着当前数据库下的所有表存储的数据默认的指定的校对集

```sql
create database <databasename> collate {collationname};
```

## 显示数据库

每当用户通过SQL指令创建一个数据库，系统就会产生一个对应的存储数据的文件夹

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/7MQY8ISC5$X]4ELTA92FXIR.png?raw=true">

每个文件下都有一个opt文件，保存的是对应的数据库选项

**基本语法**

- 显示全部库

  ```sql
  show databases;
  ```

  > <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/QH1}ZW}P]ES~$UUUA~{]{95.png?raw=true">

- 显示部分库

  ```sql
  show databases like {'matchmode'};
  ```

- <div id="match_mode">匹配模式 MatchMode</div>

  - `_`：匹配当前位置单个字符

  - `%`：匹配指定位置零个或多个字符

  > 匹配以my开头的多个数据 `'my%';`
  > 获取以m开头，第二个字符不确定，最后以database结尾的数据 `'m_database';`
  > 获取以database结尾的数据 `'%database';`
  >
  > <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/6ANE}$QLU4JIHA8JP1Y`7%R.png?raw=true">

## 显示数据库创建语句

**基本语法**

```sql
show create database <databasename>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/@YL)Q1TS{3IKI1M$I}E@%DW.png?raw=true">

## <span id="select_database">选择数据库</span>

*为社么要选择数据库？*

因为数据是存储在数据表，数据表存储在数据库下。如果要操作数据，那么必须进入对应的数据库中。

**基本语法**

```sql
use <databasename>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/B[KAU0$%}I2N93N0WJU@B1L.png?raw=true">

## 修改数据库

修改数据库库选项

*在MySQL5.5之前可以使用rename修改数据库名，之后被移除*

**基本语法**

```sql
alter database <databasename> {charset|collate} [=] {charsetname|collationname};
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/`3IQA)H`0}(%5Y~6R78R}P5.png?raw=true">

## 删除数据库

*删库跑路？*

**基本语法**

```sql
drop database <databasename>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/L%{PL]K1T(XNJPBYVF}NXT7.png?raw=true">

---

# 数据表操作

## 创建数据表

### 普通创建表

**基本语法**

```sql
create table <tablename>(<fieldname> <fieldtype> [fieldattribute],...) [tableoption];
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/SBOJI%BY494628IN_IZXI{O.png?raw=true">

错误信息：表必须放到对应的数据库下，有两中方式可以将表挂入到指定的数据库下

1. 在数据表前面加上数据库的名字，用 `.` 连接

```sql
create table <databasename>.<tablename>(<fieldname> <fieldtype> [fieldattribute],...) [tableoption];
```

2. 在创建数据表之前先进入到某个指定的数据库，用 `use` ，语法[点击参考上文](#select_database)

**表选项 TableOption**

与库选项类似，优先级大于库选项

```sql
create table <databasename>.<tablename>(<fieldname> <fieldtype>[fieldattribute],...) engine [=] {enginename} charset [=] {charsetname} collate [=] {collatename};
```

- `Engine` ：存储引擎，指MySQL提供的具体存储数据的方式，默认 `innodb` ，MySQL5.5以前默认 `myisam`

- `Charset` ：字符集

- `Collate` ：校对集

### 复制已有结构

从已经存在的表复制一份（只复制结构，不复制数据）

**基本语法**

```sql
create table [databasename.]<tablename> like [databasename.]<tablename>;
```

## 显示数据表

每当一张数据表被创建，那么就会在对应的数据库下创建一些文件（与存储引擎有关）

**基本语法**

- 显示所有表

  ```sql
  show tables;
  ```

  > <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/C6IWMFEFNF7MM~1HP3ZNUUR.png?raw=true">

- 显示部分表

  ```sql
  show tables like {'matchmode'};
  ```

- 匹配模式[点击参考上文](#match_mode)

  > <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/XS9JE(0ELNVH{EXCIW8TN(2.png?raw=true">

## 显示表结构

显示表中所包含的字段信息（名字，类型，属性等）

**基本语法**

```sql
{Describe|Desc|show} columns from <tablename>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/6_FV{TAH9LBT3XF7_P~Z%AL.png?raw=true">

**解释**

- `Field` ：字段名

- `Type` ：字段类型

- `Null` ：值是否允许为空

- `Key` ：索引名

- `Default` ：默认值

- `Extra` ：其他属性

## 显示表创建语句

查看数据表创建时的语句

**基本语法**

```sql
show create table <tablename>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/R2[UQDNI]_QUMP(IK0HKIZR.png?raw=true">


## 修改表结构

**基本语法**

- 修改表名

  ```sql
  rename <tablename> to <tablename>
  ```

- 修改表选项

  ```sql
  alter table <tablename> {engine|charset|collate} [=] {enginename|charsetname|collationname};
  ```

  *注：如果数据库已经确定，表中已经存在数据，不要轻易修改表选项*

- 新增字段

  ```sql
  alter table <tablename> add [column] <fieldname> {columntype} [columnattribute] [location];
  ```

- 修改字段名

  ```sql
  alter table <tablename> change <fieldname> <fieldname> <fieldtype> [columnattribute] [location];
  ```

- 修改字段类型

  ```sql
  alter table <tablename> modify <fieldname> <fieldtype> [fieldattribute] [location];
  ```

- 删除字段

  ```sql
  alter table <tablename> drop <fieldname>;
  ```

**解释**

- `columntype` ：列类型，包含 `int` , `float` , `char(length)` 等

- `fieldattribute` ：字段属性，包含 `null` , `default` , `key` 等

- `location` ：字段位置，`first` 表示最前端， `after <fieldname>`表示在指定字段之后

## 删除表结构

**基本语法**

```sql
drop table <tablename>[,tablename,...];
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/2N{}9GOC5KX}W{99UK}L}6H.png?raw=true">

---

# 数据操作基础

## 插入操作

本质：将数据以SQL的形式存储到指定数据表的字段里

**基本语法**

```sql
insert into <tablename> [(fieldname,...)] values(value1,...)
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/}MXSOXSNFZ_3~0U6N@O@ISN.png?raw=true">

如果指定了 `fieldname` ，则每个 `value` 对应一个 `fieldname` ；若没有指定 `fieldname` ，则默认向所有字段插入一个 `value` ，且 `value` 结构必须与 `fieldname` 结构一致

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/3DJ(6V9A1@@B%KH8UK({WQY.png?raw=true">

## 查询操作

**基本语法**

- 查询全部数据

  ```sql
  select * from <tablename>;
  ```

  *通配符 `*` 表示选择表中所有数据*

- 查询表中部分字段

  ```sql
  select <fieldname>[,fieldname...] from <tablename>;
  ```

- 简单条件查询数据

  ```sql
  select <fieldname>[,fieldname...] from <tablename> where <fieldname> = <value>;
  ```

  > <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/L9@YAY{_2})USSF7{]%Q8]S.png?raw=true">

  *解释：获取表中满足条件fieldname = value数据的fieldname下的值*

## 删除操作

**基本语法**

```sql
delete from <tablename> [where condition];
```

*如果没有指定 `where condition` ，系统会默认删除表中所有数据，慎用*

> 删除年龄为20岁的人
> 
> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/$PO8`T1VF9LY)W~86OH(0%1.png?raw=true">

## 更新操作

将数据进行修改，通常是修改部分字段数据

**基本语法**

```sql
update <tablename> set <fieldname> = <value> [where condition];
```

*如果没有指定 `where condition` ，那么所有的表中的对应字段都会被修改为统一的值*

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/`9WATOY6{U%6UP)9N_C@QUP.png?raw=true">

---

# 字符集

## 字符编码概念

字符 `Character` 是一种文字和符号的总成，包括各国家文字、标点符号、图形符号、数字等。字符编码 `Character Code` 是计算机针对各种符号，在计算机中的一种二进制存储代号

## 字符集概念

字符集 `Character Set` 是多个字符的合集，字符集种类较多，每个字符集包含的字符个数不同。常见的字符集 `ASCII` , `GB2312` , `BIG5` , `GB18030` , `Unicode` 等。计算机要准确地处理各种字符集文字，需要进行字符编码，以便计算机能够识别和存储各种文字。中文文字数目较大，而且还分为简体中文和繁体中文两种不同书写规则的文字，而计算机最初是按照英语单字节字符设计的，因此，对中文字符进行编码，是中文信息交流的技术基础

## 设置客户端所有字符集

如果直接通过CMD下的mysql.exe进行中文数据插入，可能出错

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/LP@QQ9~CQ89PX~GU{Q2DD]G.png?raw=true">

出错原因：

1. 用户是通过mysql.exe来操作mysqld.exe

2. 真正的SQL执行是mysqld.exe来执行

3. mysql.exe在将数据传入mysqld.exe时，没有指定字符集，mysqld.exe使用其默认字符集

**解决方案**

mysql.exe客户端在进行数据操作之前，为mysqld.exe指定字符集

- 快捷方案：

  ```sql
  set names <charsetname>;
  ```

- 深层原理：mysql.exe与mysqld.exe之间的处理关系一共分为三层

  > 客户端数据传输数据给服务端： Client
  > 服务端返回数据给客户端： Server
  > 客户端与服务端之间的连接： Connection

`set names` 的本质：一次性打通三层关系的字符集，变得一致。在系统中有三个变量来记录这三个关系对应的字符集，查询语句 `show variables like 'character_set_%';`

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/`O6W58FCW}520AE]~E@_0(9.png?raw=true">

- `charcacter_set_client` ：客户端传入字符集

- `character_set_connnection` ：连接层字符集

- `charcacter_set_database` ：当前数据库字符集

- `charcacter_set_filesystem` ：文件系统字符集

- `charcacter_set_results` ：服务端返回结果字符集

**修改变量**

```sql
set {variablename} = value;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/C5XEXCFIBDGW16%(VOQF]BF.png?raw=true">

---

# 列类型（字段类型）

## 整数类型

**Tinyint**

迷你整型，系统采用一个字节来保存的整型。一个字节对应8位，能表示的数值范围（无符号）为0~255

**Smallint**

小整形，系统采用两个字节保存。

**Mediumint**

中整型，系统采用三个字节保存。

**Int**

标准整型，系统采用四个字节保存。

**Bigint**

大整型，系统采用八个字节保存。

**无符号标识设定**

在类型之后加上 `unsigned` ，如 `int unsigned`

**显示长度**

指数据（整型）在数据显示的时候，具体可以显示多少位

> `tinyint(3)` ：表示最长可以显示3位
> `int(11)` ：表示最长可以显示11位

*这里的位是指十进制位*

显示长度只是代表了数据是否可以达到指定长度，但不会自动填充到指定长度，若想要保持最高位输出，需要给字段添加一个 `zerofill` 属性（同 `unsigned` ）

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/6~_9Q7E8IAXG0W@XRIF3X@8.png?raw=true">

`zerofill` ：从左侧开始填充0，直到需要显示的数据。负数不能使用zerofill，使用zerofill属性的字段，自动确定为unsigned

## 小数类型

专门用来存储小数。在MySQL中小数类型又分为两类：浮点型和定点型

### 浮点型

浮点型，又称之为精度类型，是一种有可能丢失精度的数据类型，数据有可能不那么精确，尤其是在超出范围的时候

*注意：如果精度丢失，浮点型将以四舍五入的方式保留精度位，其余位补0*

**Float**

float又称为单精度类型，系统提供4个字节来存储数据，但是能表示的数据范围比整型大得多，大概是10^38，只能保证7个左右的精度

**基本语法**

- `float` ：表示不定小数位的浮点数

- `float(M , D)` ：表示一共存储M个有效数字，其中小数位占D位

*浮点数的应用：通常是用来保存一些数量特别大，大到可以不用精确的数据*

**Double**

double又称为双精度类型，系统提供8个字节来存储数据，表示的范围更大，能达到10^308，但是精度也只有15位左右

> 浮点型之所以能够存储较大的数值（不准确），是因为利用存储数据的位来存储指数<br/>
> 整型：<br/>
> 1 1 1 1 1 1<br/>
> 计算结果：1 * 2^0^ + 1 * 2^1^ + 1 * 2^2^ + 1 * 2^3^ + 1 * 2^4^ + 1 * 2^5^ = 63<br/>
> 浮点型：<br/>
> 1 1 1 1 1 1<br/>
> 前两位作10的指数<br/>
> 计算结果：10^(1 * 2^0^ + 1 * 2^1^) * (1 * 2^0^ + 1 * 2^1^ + 1 * 2^2^ + 1 * 2^3^) = 15000

### 定点型

能够保证数据精确的小数类型，小数部分可能不精确，超出长度会四舍五入，整数部分一定精确

**Decimal**

系统自动根据存储的数据来分配存储空间，每大概9个十进制位就会分配四个字节，同时小数部分和整数部分分开

**基本语法**

`decimal(M , D)` ：M表示总长度，最大值不能超过65，D代表小数部分长度，最大值能超过30

### 浮点型与定点型的比较

**创建表**

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/(H]Y}7L@@OBCZYZY$7@@SGG.png?raw=true">

**插入数据**

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/EBZGWDY1PN}KA%[GHU`GN1T.png?raw=true">

**插入最大数据**

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/8IG72GEH}1S[`NO43F0M]_H.png?raw=true">

## 日期时间类型

### Date

日期类型，系统使用三个字节来存储数据，对应格式为 `YYYY-MM-DD` ，能表示的范围为1000-01-01到9999-12-12，初始值为0000-00-00

### Time

时间类型，能够表示某个指定的时间，但是系统同样提供三个字节来存储，对应格式为 `HH:mm:ss` ，但是MySQL中的time类型能够表示的时间范围要大得多，能表示从-838:59:59到838:59:59，在MySQL中具体的用处是来描述时间段

### Datetime

日期时间类型，就是将前面的date和time结合起来，表示的时间使用八个字节存储数据，格式为 `YYYY-mm-dd HH:mm:ss` ，能表示的范围为1000-01-01 00:00:00到9999-12-12 23:59:59，初始值为0000-00-00 00:00:00

### Timestamp

时间戳类型，MySQL中的时间戳只是表示从格林威治时间开始所对应的时间，但格式为 `YYYY-mm-dd HH:mm:ss` ，时间戳类型不能为空，默认值为当前时间戳

### Year

年类型，系统使用一个字节存储，能表示的范围为1900到2155年，但是year有两种插入方式，一种是两位数0到99，另一种则是四位数插入

## 日期时间类型示例

**创建表**

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/ELFN_~VGNJ(T965EK9R7~11.png?raw=true">

**插入数据**

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/U_EUU`~8H(]XQUJ$ZY2N%X2.png?raw=true">

**Year的特殊性**

Year可以采用两位数的数据插入，也可以使用四位数的数据插入

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/B7$IOP$RPU(JDH(NNNL7}QR.png?raw=true">

Year使用两位数插入的时候，有区间划分，临界点为69和70，若输入69及以下，则显示20XX年，若输入70及以上，则显示19XX年

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/Q7]_~AL2H}5FS8V_(0_S{[6.png?raw=true">

**Timestamp的特殊性**

每当表中其他数据被更新，则会自动更新当前时间戳

*详见上图中d4列*

## 字符串类型

### Char

定长字符，指定长度后，系统一定会分配指定的空间用于存储数据

**基本语法**： `char(length)` ，length范围为0到255

### Varchar

变长字符，指定长度后，系统会根据实际存储的数据来计算长度，分配合适的长度

**基本语法**： `varchar(length)` length理论范围为0到65535

*因为要记录数据长度，varchar会额外占用一到两个字节*

### Text

文本类型，本质上MySQL提供了两种文本类型

- `Text` ：存储普通文本

- `Blob` ：存储二进制文本（图片，文件）

系统提供了四种text

- `Tinytext` ：系统使用一个字节来存储，实际能够存储的数据为 2^8^ + 1

- `Text` ：使用两个字节保存，实际能够存储的数据为 2^16^ + 2

- `Mediumtext` ：使用三个字节保存，实际能够存储的数据为 2^24^ + 3

- `Longtext` ：使用四个字节保存，实际能够存储的数据为 2^32^ + 4

*注意：在选择对应的存储文本的时候，不用刻意选择text类型，系统会自动根据存储的数据长度来选择合适的文本类型*

### Enum

枚举类型，在插入数据之前，设定几个可能出现的项，最后结果从这几个项中出现。系统提供了一到两个字节来存储枚举数据，通过计算enum列举的具体值来选择实际的存储空间

**基本语法**

```sql
enum(<value1>,<value2>,...)
```

**示例**

- 创建表

  > <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/~~(65NEE_`]7S6VQAONWLXE.png?raw=true">

- 插入合法数据

  > <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/[{@5L{WF}%7P~$05(60)J`A.png?raw=true">

- 插入非法数据

  > <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/KHPLJMING5F]EX`S48JE}2I.png?raw=true">

**储存原理**

实际上enum所存储的值并不是真正的字符串，而是字符串对应的下标，当系统设定枚举类型的时候，会给每个枚举项定义一个下标，下标从1开始

> enum(1 => 'male' , 2 => 'female' , 3 => 'secret')
> 
> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/)U0D{2P2(VBJQVU9VZ60}ZR.png?raw=true">

**枚举的意义**

1. 规范数据数据本身，限定只能插入规定的数据项

2. 节省存储空间

### Set

集合，一种将多个数据选项可以同时保存的数据类型，本质是将指定的项按照对应的二进制位来进行控制。1表示该选项被选中，0表示该选项没有被选中。系统为set提供了多个字节进行保存，但是系统会自动计算来选择具体存储的单元，每个字节会提供8个选项，最多8个字节。set和enum一样，存储的是字符串下标，而不是真实的字符串

**基本语法**

```sql
set(<value1>,<value2>,...)
```

**示例**

- 创建表

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/NX%E~]Q(}WR5XA~~@7H$]$J.png?raw=true">

- 插入数据

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/)[)}@RZJLBX%WJ}%)34BHXV.png?raw=true">

*数据的顺序与插入时的顺序无关，实际顺序以定义时为准*

**储存原理**

每个选项都对应一个二进制位，如上图，指定所有选项被选中为11111100，hobby内第一行则为10011000。然后将二进制位逆序，再转换为十进制数进行存储

> 10011000 > 00011001 > 1 * 2^0^ + 1 * 2^3^ + 1 * 2^4^ = 25
> 
> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/A2@EV6U`12KY@93H_T`4UEK.png?raw=true">

以数值形式插入

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/HPSA5FY[SL2DUW(DD@])$W7.png?raw=true">

*数值插入的前提时每个二进制位有对应的项*

**集合的意义**

1. 规范数据

2. 节省空间

---

# 列属性

列属性又称为字段属性，在MySQL中共有6个属性：NULL、默认值、列描述、主键、唯一键、自动增长

## Null

代表字段能否为空

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/C{2H`2GF(ACL$GKKNF4{56F.png?raw=true">

值：

- `YES` ：可以为空

- `NO` ：不能为空

注意：

1. 在设计表的时候，尽量不要让数据为空

2. MySQL的最大记录长度为65535个字节，如果一个表中有字段NULL为YES，那么系统就会保留一个字节来存储NULL，最终有效存储长度为65534个字节

## 默认值

当字段被设计时，如果允许默认条件下，用户不进行数据插入，那么就使用默认值来填充，一般默认值为NULL

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/9Q0T~C2ZA422(MTESF]Z`@T.png?raw=true">

*default关键字可以在定义时使用*

## 列描述

专门用于给开发人员进行维护的一个注释说明

**基本语法**

```sql
comment `字段描述`;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/3[2W~4~9]IXKDY(P~_@_VF8.png?raw=true">

*只能使用创建查看语句查询*

## 主键

Primary Key，主要的键，在一张表中，有且只有一个字段，里面的值具有唯一性

### 创建主键

#### 随表创建

系统提供了两种增加主键的方式

1. 直接在需要当作主键的字段之后增加 `primary key` 属性来确定主键

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/YNS6BLZEBZAN2U1[%P@{@U5.png?raw=true">

2. 在所有字段之后增加 `primary key(fieldname)`

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/3M1L}08)@~TO[(7H6CUU$2U.png?raw=true">

*注：主键创建后，NULL属性会被设置为NO*

#### 表后增加

**基本语法**

```sql
alter table <tablename> add primary key(<fieldname>,...);
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/VH4GN_4JXJ%TM@~C0CX54BG.png?raw=true">

### 查看主键

1. 查看表结构 `desc <tablename>`

2. 查看表创建语句 `show create table <tablename>`

### 删除主键

**基本语法**

```sql
alter table <tablename> drop primary key;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/@$`WY2)KKQ{FW7Z7M}{9A0L.png?raw=true">

*注：主键删除后，NULL属性依旧为NO*

### 复合主键

一个表中允许有多个主键，共同组成复合主键

**示例**

有一张学生选修课表，一个学生可以选择多门选修课，一个选修课也可以由多个学生选择，但是一个学生在一个选修课中只有一个成绩

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/CFICY%$8KZT{NR(J_1FD4~E.png?raw=true">

### 主键约束

1. 当前字段对应的数据不能为空

2. 当前字段对应的数据不能有任何重复

**示例**

向上文创建的选修课表中插入数据

- 合法插入

  > <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/]WO4%C3N1WS5TU%9B7FVYB9.png?raw=true">

- 非法插入

  > <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/1.png?raw=true">

### 主键分类

主键分类采用的是主键所对应的字段的业务意义分类

- 业务主键：主键所在的字段，具有业务唯一性，如上文中的学生ID，课程ID等

- 逻辑主键：自然增长的整型

## 自动增长

Auto_increment，自动增长，当给定某个字段该属性以后，该列的数据在没有提供确定数据的时候，系统会根据之前已经存在的数据进行自动增长。通常自动增长用于逻辑主键

### 原理

1. 在系统中有维护一组的数据，用来保存当前使用了自动增长属性的字段，记住当前对应的数据值，再给定一个指定的步长

2. 当用户进行数据插入的时候，如果没有指定值，系统会在原始值上再加步长变成新的数据

*自动增长仅在未指定值时触发，且仅适用于数值*

### 使用自动增长

**基本语法**

在字段之后添加一个属性 `auto_increment`

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/XBUWLXV8PD$O8L`LJBPWMW3.png?raw=true">

插入数据

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/L$@I9]P7SQTSK@GX3Z]137S.png?raw=true">

### 修改自增长

1. 查看自增长，自增长一旦触发，会自动在表的选项中添加一个选项，一张表最多只能有一个自增长

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/GXG]YR[S{P]}SF[W54LIMJ4.png?raw=true">

2. 表选项可以通过修改表结构来实现

**基本语法**

```sql
alter table <tablename> auto_increment = value;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/X1`{}Z}]O_%MLBE$9249EYI.png?raw=true">

### 删除自增长

自增长属性无法使用drop直接删除表选项，而是在字段属性之后不再保留auto_increment。系统在用户修改自增长所在字段时，如果没有auto_increment，则会自动清除自增长

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/ME7GFFU5_8I5P%NVEYK``AN.png?raw=true">

*注：主键已经确定，不要再次添加primary key属性*

### 初始设置

在系统中，有一组变量用来维护自增长的初始值和步长

```sql
show variables like 'auto_increment%';
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/PP[]KGB`)32)%2)JULTM{97.png?raw=true">

- `auto_increment_increment` ：步长

- `auto_increment-offset` ：初始值

## 唯一键

唯一键，unique key，用来保证字段中数据的唯一，唯一键在一张表中可以有多个，且允许字段数据为NULL

### 创建唯一键

创建唯一键与创建主键非常类似

1. 直接在表字段之后增加唯一键标识符： `unique [key]`

2. 在所有表字段之后使用 `unique (<fieldname>,...)`

3. 在创建完表之后通过表属性修改

```sql
alter table <tablename> add unique key(<fieldname>,...);
```

### 查看唯一键

唯一键是属性，可以直接通过表属性查看

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/@AK}AS$}1_QRT4@X}Y77N`3.png?raw=true">

### 删除唯一键

在创建唯一键时，系统会为每个唯一键指定一个名字，可以通过查看表创建语句查看

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/~86W86%RC$C6WP`OGAGUNQL.png?raw=true">

**基本语法**

```sql
alter table <tablename> drop index <keyname>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/1I@3F[[[XLD(G]I``W30~QE.png?raw=true">

### 复合唯一键

唯一键和主键一样，可以在多个字段使用来共同保证唯一性

*一般主键作为逻辑键，其他需要唯一性的内容由唯一键处理*

---

# 表关系

表与表之间（实体）有什么样的关系，每种关系应该如何设计表结构

## 一对一

一张表中的一条记录与另一张表中最多有一条明确关系，通常此设计方案保证两张表中使用同样的主键即可

### 示例

设计学生表，包括学生相关信息

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/{Z[B[{18]U9`}APCB2(SB$J.png?raw=true">

表的使用过程中，会有常用的信息与不常用的信息之分，若将常用的与不常用的同时存储，会影响查询效率

**解决方案**

将表拆成两张，一张存储常用数据，另一张存储不常用数据

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/(ZBPXPW}C9C6RN`6W`8~)QP.png?raw=true">

## 一对多

一对多，通常也叫做多对一关系。通常一对多的设计方案是在“多关系”表中维护一个字段，这个字段是“一关系”的主键

### 示例

设计母亲和孩子表，包括母亲和孩子的相关信息

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/49$}A73KBY3N{9[L0WFSO7G.png?raw=true">

一个母亲可能对应多个孩子，单纯使用两张表无法体现其关系

**解决方案**

在孩子表中新建一个字段，用于维护母亲id

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/T]F3WW4OJXVU@45JL[8R`%J.png?raw=true">

## 多对多

一张表中的一条记录在另外一张表中可以匹配到多条记录，反之亦然。多对多的关系如果按照一对多的方案处理，就会出现一个字段中有多个其他表的主键，会阻碍查询效率。通常使用三张表解决问题

### 示例

设计老师和学生表，包括老师和学生的相关信息

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/QBH9W}SS5XXWW8(UQ~V43]C.png?raw=true">

一个老师教过多个学生，一个学生听过多个老师的课，单纯使用两张表无法体现其关系

**解决方案**

从中间设计一张表，用于维护两张表之间的关系，每一种关系都包含

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/3O0YLYE7RG5}]RT7H3UK2L7.png?raw=true">

---

# 高级数据操作

## 多数据插入

只写一次insert指令，但是可以插入多条记录

**基本语法**

```sql
insert into <tablename> [(<fieldname>,...)] values(<value1>,<value2>,...),(<value1>,<value2>,...),...;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/8WXR5NTLB)V]QNTAK1}M@3E.png?raw=true">

## 主键冲突

在有的表中，使用的是业务主键，但是往往在进行数据插入时，又不确定表中是否已经存在对用的主键

**解决方案**

1. 主键冲突更新：类似插入语法，如果插入的过程中主键冲突，那么采用更新方法

**基本语法**

```sql
insert into <tablename> [(<fieldname1>,<fieldname2>,...)] values(<value1>,<value2>,...) on duplicate key update <fieldname> = <value>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/@S{RHDIOXEZ6`A@W9LIJDZW.png?raw=true">

2. 主键冲突替换：当主键冲突之后，删除原来的数据，重新插入

**基本语法**

```sql
replace into <tablename> [(<fieldname1>,<fieldname2>,...)] values(<value1>,<value2>,...);
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/GAZOYRGF4LM5BO)4]`MO}_T.png?raw=true">

## 蠕虫复制

从已有的数据中获取数据，并且将获取到的数据插入到数据表中

**基本语法**

```sql
insert into <tablename> [(<fieldname1>,<fieldname2>,...)] select {*|fieldname} from <tablename>;
```

**示例**

创建样本表

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/J`)XBB[I)9HZI`V0W`0I3TU.png?raw=true">

蠕虫复制

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/W`($05C7GPU_$1T1~XAJ73D.png?raw=true">

注意：

1. 蠕虫复制通常都是重复数据，没有业务意义，但是可以在短期内快速增加表的数据量，从而测试表的压力，还可以通过大量数据来测试表的效率

2. 在蠕虫复制时一定要注意主键冲突

## 更新数据

1. 在数据更新的时候，通常是跟随条件更新

**基本语法**

```sql
update <tablename> set <fieldname> = <value> where <judgementcondition>;
```

2. 如果没有指定判断条件，通常是全表更新数据。但是可以使用limit来限制更新的数量

**基本语法**

```sql
update <tablename> set <fieldname> = <value> [where <condition>] limit <count>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/Q)1Y(I4TST13QC22`~FK{`1.png?raw=true">

## 删除数据

1. 删除数据的时候尽量不要全部删除，应该使用where进行判定

2. 删除数据的时候可以使用limit来限制删除的数量

3. delete删除数据的时候无法重置auto_increment

MySQL中有一个能够重置表选项中自增长的语法

**基本语法**

```sql
truncate <tablename>;
```

## 查询数据

完整的SQL查询指令

```sql
select {selectoption} <fieldname> from <origindata> where <condition> group by <groupname> having <condition> order by <orderoption> limit <conditon>;
```

**SelectOption**

系统该如何对待查询得到的结果

- `ALL` ：默认选项，保存所有记录

- `Distinct` ：去除重复的记录，只保留一条

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/GY]0OBQ%$I0D3E7RAPIT%6N.png?raw=true">

*注：所有字段都重复，才会被认定为重复*

**别名**

有时需要从多张表中获取数据，在获取数据的时候，可能存在不用表中有同名的字段，需要将字段命名成不同的字段名

**基本语法**

```sql
<fieldname> [as] <alias>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/~P$0@JSIO0EHIV7XWXUWYFI.png?raw=true">

### From 数据源

from为前面的查询提供数据，数据源只要是一个符合二维表结构的数据即可

#### 单表数据

**基本语法**

```sql
from <tablename>;
```

#### 多表数据

两张表的记录数相乘，字段数拼接

**基本语法**

```sql
from <tablename1>,<tablename2>,...;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/O0]LS~EKQYPPVMMONL83RIS.png?raw=true">

#### 动态数据

from后面的数据不是一个实体表，而是一个从表中查询出来得到的二维结果表

**基本语法**

```sql
from (select <fieldname> from <tablename>) [as] <alias>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/3){E{X~]05S%9FP3)6EXP]X.png?raw=true">

## Where 子句

用来从数据表获取数据时，进行条件筛选。指针从表对应的磁盘获取所有数据，在获取到一条数据后进行判断，若符合条件，就将数据保存到内存中，不符合则直接跳过。where通过运算符进行条件判断

## Group by 子句

根据指定的字段将数据进行分组，目的是统计

### 分组统计

**基本语法**

```sql
group by <fieldname>;
```

group by是为了分组后进行数据统计的，如果只是想看数据显示，那么group by没有意义。group by将数据分组后只会保留每组的第一条数据

**统计函数**

- `count()`： 统计每组中的数量，如果统计目标是字段，会忽略NULL字段，如果值为*，则为所有记录，包括NULL字段

- `avg()`： 求平均值

- `sum()` ：求和

- `max()` ：求最大值

- `min()` ：最最小值

- `group_concat()` ：将分组中的指定字段进行合并

**基本语法**

```sql
select <fieldname>,{founction1},{function2},... from <tablename> group by <fieldname>;
```

*注，第一列字段名应与分组的字段名一致*

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/JHNB)V$]JII2R0ADRWWI8@4.png?raw=true">

### 多分组

将数据按照某个字段进行分组之后，对已经分组的数据进行再次分组

**基本语法**

```sql
group by <fieldanme1>,<fieldname2>,...;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/[4CB[_[JVXLO_AOO%CPV7_V.png?raw=true">

### 分组排序

在MySQL中，分组默认有排序功能，按照分组字段进行排序，默认是升序

**基本语法**

```sql
group by <fieldname> [asc|desc][,<fieldname> [asc|desc],...];
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/DB0{KY5)BX4Q~TKVJAGJE@P.png?raw=true">

### 回溯统计

当前分组进行多分组之后，往上统计的过程中，需要进行上报，将这种上报统计的过程称为回溯统计。每一次分组向上统计的过程都会产生一次新的统计数据，而且当前数据对应的分组字段为NULL

**基本语法**

```sql
group by <fieldname> [asc|desc] with rollup;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/MP5GJ)9DC@_W6_4G}$EW2M3.png?raw=true">

## Having 子句

having和where一样，用来进行数据条件筛选。having在group by之后，可以针对分组数据进行统计筛选，但是where不行

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/KS4US{PM~G96ZMKI)~Y`8WW.png?raw=true">

## Order by 子句

根据校对规则对数据进行排序

**基本语法**

```sql
order by <fieldname> [asc|desc][,<fieldname> [asc|desc],...];
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/XHCT_OTDAPXA4(T8BIWZ7O3.png?raw=true">

order by和group by一样进行多字段排序，先按照第一个字段进行排序，再按照第二个字段进行排序

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/_6_FYWQ@@FM)JG5P)[U_[4L.png?raw=true">

## Limit 子句

主要用来限制记录的数据数量

### 记录数限制

纯粹地限制获取地数量，从第一条到指定的数量

**基本语法**

```sql
limit <count>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/2.png?raw=true">

### 分页

利用limit来限制获取指定区间的数据

**基本语法**

```sql
limit <offset>,<length>;
```

*注：offset从0开始计数*

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/4UFZ}}K80]K3K1IGCQLP(MV.png?raw=true">

---

# 运算符

## 算数运算符

进行算数运算，通常用于结果运算中

- `+` ：和

- `-` ：差

- `*` ：积

- `/` ：除

- `%` ：模

**基本语法**

```sql
<fieldname> {operator} <fieldname>[,<fieldname> {operator} <fieldname>,...];
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/}OVKW6GAM([QR$$H{`S}{CL.png?raw=true">

## 比较运算符

通常用来在条件中进行限定结果

- `>` ：大于

- `>=` ：大于等于

- `<` ：小于

- `<=` ：小于等于

- `=` ：等于

- `<>` ：不等于

- `<=>` ：等于

**基本语法**

```sql
<fieldname> {operator} <value>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/%P984S{UOSV0`HWHHL6YE)W.png?raw=true">

特殊应用：在字段结果中进行比较运算

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/8~1V%K`%$[LR9LP[~RENW8N.png?raw=true">

### 区间判断

查询满足某个闭区间的数据

**基本语法**

```sql
<fieldname> between <minimal> and <maximum>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/YWDL`5(}NFI`C_T~@DU~]OE.png?raw=true">

## 逻辑运算符

- `and` ：逻辑与，可写作 `&&`

- `or` ：逻辑或，可写作 `||`

- `not` ：逻辑非 可写作 `!`

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/Y$672XAC`(A{60CH20[[0`O.png?raw=true">

## In 运算符

用来替代等于，当结果不是一个值，而是一个结果集的时候

**基本语法**

```sql
<fieldname> in (value1,value2,...);
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/KGRRX%](H~4IX(46FBL6~)6.png?raw=true">

## Is 运算符

专门用来判断字段是否为NULL的运算符

**基本语法**

```sql
<fieldname> is {null|not null};
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/JD[1[MVEW6H%WXIWMBCN9KU.png?raw=true">

## Like 运算符

用来进行模糊匹配

**基本语法**

```sql
<fieldname> like {'matchmode'};
```

[占位符](#match_mode)详见上文

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/Y93RIRG`0S_~29)Z3[XE`YQ.png?raw=true">

---

# 联合查询

## 基本概念

联合查询是可合并多个相似的选择查询的结果集，等同于将一个表追加到另一个表，进行纵向合并，字段数不变，查询记录数合并，从而实现将两个表的查询组合到一起，使用谓词为 `UNION` 或 `UNION ALL`

## 应用场景

1. 将同一张表中不用的结果合并到一起展示数据

2. 在数据量大的情况下，会对表进行分表操作，需要对每张表进行部分数据统计，使用联合查询来将数据存放到一起显示

## 基本语法

```sql
select <statement> union [unionoption] select <statement>;
```

## UnionOption

与select选项基本一致

- `Distinct` ：默认值，去重

- `All` ：显示全部记录

**示例**

将男女分开显示

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/5DQ4$TXD4$2Q)N}$89(~F1N.png?raw=true">

## Order by的使用

1. 在联合查询中，使用order by的select语句必须使用括号

2. 若要使order by生效，需要使用limit

非法语句

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/_0L3$]$]11I1UKNP$1[KR4T.png?raw=true">

合法语句

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/M`]RP{1R5JLIGQ)X75~8$Q8.png?raw=true">

---

# 连接查询

## 基本概念

将多张表连接到一起进行查询，记录数和字段数列都会发生改变

**意义**

在关系型数据库中，实体与实体之间是存在很多联系的。在关系型数据库的设计过程中，遵循着一对一，一对多，多对多，通常在实际操作过程中，需要利用这层关系来保证数据的完整性

## 连接查询分类

连接查询共分为以下几类：

- 交叉连接

- 内连接

- 外连接

  - 左外连接

  - 右外连接

- 自然连接

## 交叉连接

将一张表的数据与另一张表彼此交叉

### 原理

1. 从第一张表中依次取出每一条记录

2. 取出每一条记录之后，与另一张表的全部记录匹配

3. 没有任何匹配条件，所有的结果都会进行保留

4. 记录数 = 第一张表记录数 * 第二张表记录数，字段数 = 第一张表字段数 + 第二张表字段数

### 基本语法

```sql
select {*|fieldname} fropm <tablename> cross join <tablename>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/9([%)Z60CYJ9U6(FVQ6T5XS.png?raw=true">

*注：交叉连接产生的数据是笛卡尔积，没有实际意义，应尽量避免出现*

## 内连接

从一张表中取出所有数据去另一张表中匹配，利用匹配条件进行匹配，成功则保留，失败则跳过

### 原理

1. 从第一张表中依次取出每一条记录

2. 取出每一条记录之后，与另一张表的全部记录利用匹配条件匹配

3. 匹配成功保留，失败跳过

### 基本语法

```sql
select {*|fieldname} fropm <tablename> [inner] join <tablename> on <judgementconditon>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/ZP]JB]XVUAB$9$FXH9_T7ZB.png?raw=true">

## 外连接

按照某一张表作为主表，根据条件去连接另一张表，从而得到目标数据。主表的记录一定会保留

外连接分为左连接 `left join` 和右连接 `right join` 两种，表示主表位置

### 原理

1. 确定连接主表

2. 用主表的每一条记录，去匹配从表的每一条记录

3. 满足条件保留数据，不满足跳过

4. 所有记录都没有匹配成功也会保留字段名，字段值都为NULL

### 基本语法

```sql
select {*|fieldname} fropm <tablename> {left|right} join <tablename> on <judgementconditon>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/BZI0N6S43I2T%HD{1P%6[8H.png?raw=true">

## Using 关键字

在连接查询中用来代替对应的on关键字，进行条件匹配

### 原理

1. 在来连接查询时，使用using代替on

2. 连接的两张表的字段同名

### 基本语法

```sql
select {*|fieldname} fropm <tablename> {inner|left|right} join <tablename> on using(<fieldname1>,<fieldname2>,...);
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/4~PG7$D)GCRI7Z6ZRYQVX_6.png?raw=true">

---

# 子查询

## 子查询概念

子查询是一种常用计算机语言SELECT-SQL中嵌套查询下层的程序模块，当一个查询是另一个查询的条件，称为子查询

### 主查询概念

主要的查询对象，第一条select语句，确定用户所有获取的数据源，具体到得到的字段信息

### 子查询与主查询的关系

1. 子查询是嵌入到主查询中的

2. 子查询辅助主查询，要么作为条件，要么作为数据源

3. 子查询可以独立存在，是一条完整的select语句

## 子查询分类

### 按功能分类

- 标量子查询：子查询返回的结果是一个数据

- 列子查询：返回的结果是一列

- 行子查询：返回的数据是一行

- 表子查询：返回的结果是多行多列

- Exists子查询：返回的结果是1或0

### 按位置分

- Where子查询：子查询在where条件中

- From子查询：子查询在from数据源中

## 标量子查询

**基本语法**

```sql
select <fieldname> from <tablename> where <fieldname> {=|<>} (select <fieldname> from <tablenames> where <judgementcondition>);
```

**示例**

通过一个学生的名字，查询到所在班级名

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/5@RX2V(VLSMCZ7_6D%QFB}6.png?raw=true">

## 列子查询

**基本语法**

```sql
select <fieldname> from <tablename> where <fieldname> in (select <fieldname> from <tables>);
```

**示例**

获取有学生在班的班级名

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/G1%%OW4HL`{RM}I]Y2)3894.png?raw=true">

## 行子查询

### 行元素

字段元素是指一个字段对应的值，行元素是指将多个字段合起来作为一个元素参与运算

**基本语法**

```sql
select <fieldname> from <tablename> where (<fieldname1>,<fieldname2>,...) = (select <fieldname1>,<fieldname2>,... from <tablename>);
```

**示例**

获取班上年龄最大，身高最高的学生

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/SN5PL%87IIPW4EQOR)QUNKA.png?raw=true">

## 表子查询

**基本语法**

```sql
select {*|fieldname} from (select <fieldname> from <tablename> where <judgementcondition>) as <alias> [group by <groupname> having <condition> order by <orderoption> limit <conditon>];
```

**示例**

获取每个班上身高最高的学生

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/~PMN%_N3}2EG~UQC41}9S%Y.png?raw=true">

**特别说明**

在MySQL5.7版本之后，属性 `only_full_group_by` 默认开启，直接执行上文语句会报错

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/6AK7CVU[_T(KE0@96I5CQ(A.png?raw=true">

解决方法：关闭该属性

```sql
set sql_mode = (select replace(@@sql_mode,'ONLY_FULL_GROUP_BY',''));
```

## Exists 子查询

**基本语法**

```sql
select {*|fieldname} from <tablename> where exists(select {*|fieldname} from <tablename> where <judgementcondition>);
```

**示例**

获取有学生在班的班级名

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/G3]V7%P4[CMQE2FO[QHM}K3.png?raw=true">

## 特殊关键字

### In 关键字

条件在查询结果中存在，用法： `in(列子查询)`

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/EQLHB9)HQM9L@RQB9O5KU)7.png?raw=true">

### Any 关键字

条件在查询结果中有任意一个匹配的值，用法： `{=|<>} any(列子查询)` ，等价于in

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/%V{6H9%M}7%}]_[9)E2SINA.png?raw=true">

### Some 关键字

some与any完全一致

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/(~FPCY)]D53}I%BWP~Y2M6V.png?raw=true">

### All 关键字

条件在查询结果中全部匹配，用法 `{=|<=>} all(列子查询)`

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/`(Z9WA[X$`8{13D~BA[C{7F.png?raw=true">

---

## 整库数据备份与还原

整库数据备份也叫SQL数据备份，备份的结果都是SQL指令。在MySQL中提供了一个专门用于备份SQL的客户端：mysqldump.exe

### 应用场景

SQL备份是一种mysql非常常见的备份与还原方式，SQL备份不只是备份数据，还备份对应的SQL指令。SQL备份需要备份结构，因此产生的文件较大，不适合特大型数据库备份，也不适合数据变换频繁型数据库备份

### 应用方案

#### SQL备份

SQL备份用到的是专门的客户端，因此需要与MySQL服务器进行连接

**基本语法**

```sql
mysqldump.exe -h<hostip> -P<post> -u<username> -p<password> <databasename> [<tablename1>,<tablename2>,...] > <filelocation>
```

备份可以有三种形式：

1. 整库备份，只提供数据库名

2. 单表备份，只提供一张表名

3. 多表备份，提供多张表明

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/DM1]LVL[EIR7%GYSVDG2FFV.png?raw=true">

*注：此处的filelocation可用相对路径*

#### SQL还原

MySQL提供了两种方式来还原

1. 利用mysql.exe客户端

**基本语法**

```sql
mysql.exe -h<hostip> -P<post> -u<username> -p<password> <databasename> < <filelocation>
```

*注：还原时的数据库名应存在于mysql中*

2. SQL指令中提供了一种导入SQL指令的方式

**基本语法**

```sql
source <filelocation>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/9D[R5`4GU$81J[APF%QP_(U.png?raw=true">

*注：sql指令导入时，应先进入需要还原的数据库*

---

# 用户权限管理

在不同的项目给不同的角色不同的操作权限，为了保证数据库的数据安全

## 用户管理

MySQL需要客户端进行认证才能进行服务器操作。MySQL中所有的用户信息的都保存在MySQL数据库下的user表中

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/(4$2~BKRZ)@WEZPR~5%YK4P.png?raw=true">

- `User` ：用户的用户名

- `Host` ：允许访问的主机

### 创建用户

理论上可以采用两种方式

1. 直接使用root用户在mysql.user中插入记录

2. 专门创建用户的SQL指令

**基本语法**

```sql
create user <username>[@'{hostaddress|%|*}' identified by '<password>'];
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/%$IEC5]L8TN220WQWEVQP~J.png?raw=true">

### 删除用户

**基本语法**

```sql
drop user <username>@'<hostaddress>';
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/3.png?raw=true">

### 修改用户密码

MySQL中提供了多种修改密码的方式，但基本上都必须使用系统提供的函数 `password()` 来对密码进行加密处理

1. 使用专门的修改密码的指令

**基本语法**

```sql
set password for <username>@'<hostaddress>' = password('<password>');
```

2. 使用更新语句来修改表

**基本语法**

```sql
update mysql.user set password = password('<password>') [where user = '<username>' {&&|and} host = '<hostadress>'];
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/TK8NH9BK27()Z2CC}C36V8C.png?raw=true">

*注：作者使用的MySQL版本5.7.26中，password字段名被更改为authentication_string*

## 权限管理

在MySQL中权限分为三类

- 数据权限：增删改查（ `select` | `update` | `delete` | `insert` ）

- 结构权限：结构操作（ `create` | `drop` ）

- 管理权限：权限管理（ `create user` | `grant` | `revoke` ）

### 授予权限 Grant

将权限分配给指定的用户

**基本语法**

```sql
grant <authoritylist> on {databasename|*}.{tablename|*} to <username>;
```

**AuthorityList 权限列表**

使用逗号分隔，但是可以使用all privileges代表全部权限

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/D3}8}}%S%8YW7NZR(JVAD3Q.png?raw=true">

### 取消权限 Revoke

权限回收，将权限从用户手中收回

**基本语法**

```sql
revoke {authoritylist|all privileges} on {databasename|*}.{tablename|*} from <username>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/QRC00BVC0$Q{DQ$K)U$SYAG.png?raw=true">

### 权限刷新 Flush

刷新当前对用户的权限操作，将操作的内容同步到对应表中

**基本语法**

```sql
flush privileges;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/){})CD0}KY8HYY2KO$Z)%Y5.png?raw=true">

## 密码找回

1. 停止服务

```shell
net stop mysql
```

2. 重启服务

```shell
mysqld.exe --skip-grant-tables
```

3. 当前启动的服务端不需要任何用户信息就可以直接登录，并且为root权限

4. 修改密码

5. 重启服务

---

# 外键

## 概念

如果公共关键字在一个关系中是主关键字，那么这个公共关键字被称为另一个关系的外键。外键表示了两个关系之间的相关联系，以另一个关系的外键作为主关键字的表被称为主表，具有此外键的表被称为主表的从表，外键又被称为关键字。简单来说，就是一张表中的一个字段，保存的值指向另外一张表的主键

## 外键操作

### 增加外键

MySQL中提供了两种方式增加外键

- 在创建表的时候增加外键

**基本语法**

```sql
[constraint `外键名`] foreign key(fieldname) references <tablename>(primary key);
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/7IC}KYM0E{G]4@RPX0~[X(M.png?raw=true">

- 在创建表后增加外键

**基本语法**

```sql
alter table <tablename> add [constraint `外键名`] foreign key(fieldname) references <tablename>(primary key);
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/$[S)RE$7VLDIJDN@WE1446C.png?raw=true">

### 删除外键

外键不允许修改，只能删除后重新指定

**基本语法**

```sql
alter table <tablename> drop foreign key <keyname>;
```

*注：keyname可以查看表创建语句*

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/`9D3NEYMRK8V9U{3[XUEI82.png?raw=true">

删除外键时，不会删除创建外键时同时创建的普通索引，需要单独删除

**基本语法**

```sql
alter table <tablename> drop index <indexname>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/O@@V{K(VLA$Z1PIH]$LA9KG.png?raw=true">

### 外键基本要求

- 外键字段类型需要与关联主表的字段类型完全一致

- 基本属性也要相同

- 如果是在创建表后指定外键，对从表数据与主表数据的关联关系有一定要求

- 外键只能使用innodb存储引擎，myisam不支持

### 外键约束

通过建立外键关系之后，对主表和从表都会有一定的数据约束效率

#### 约束的基本概念

- 当一个外键产生时，外键所在的表会受制于主表数据的存在从而导致数据不能进行某些不符合规范的操作

- 如果一张表被其他表外键引入，那么该表的数据操作就不能随意，必须保证从表数据的有效性

#### 外键约束的概念

可以在创建外键的时候，对外键约束进行选择性的操作

**基本语法**

```sql
add foreign key(fieldname) references <talbename>(fieldname) on [update|delete] {district|cascade|set null};
```

- `District` ：严格模式，不允许更改，默认值

- `Cascade` ：级联模式，同时更改主表与从表的数据

- `Set NULL` ：置空模式，如果外键字段允许为空，当主表删除，从表数据自动设置为NULL

通常在进行约束模式的时候，需要指定操作：update和delete。常用的操作模式：on update cascade on delete set null，更新级联，删除置空

#### 约束作用

保证数据的完整性，主表与从表数据一致。正是因为外键有非常强大的数据约束作用，而且可能导致数据在后台变化的不可控，导致程序在进行开发的逻辑的时候，没有办法很好地把握数据，外键一般比较少使用

---

# 视图基本操作

## 创建视图

视图的本质是SQL指令

**基本语法**

```sql
create view <viewname> as select...;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/BI)F(HX)SEIKA(N5}D})6YQ.png?raw=true">

## 使用视图

视图本身是虚拟表，所以一些关于表的操作也适用于视图，但是视图本身没有数据，是临时执行select语句得到对应的结果，视图主要用于查询操作

**基本语法**

```sql
select {fieldname|*} from <viewname> [clause];
```

## 修改视图

本质是修改视图对应的查询语句

**基本语法**

```sql
alter view <viewname> as select...;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/1$HWKQJ~`G1(5OS_4@_D%N7.png?raw=true">

## 删除视图

**基本语法**

```sql
drop view <viewname>;
```

> <img src="https://github.com/Ki1z/PHP-Study-Notes/blob/main/Image/EDLRS9@[04(%9PSHU(KL~O8.png?raw=true">