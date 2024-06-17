# Linux Basic

`更新时间：2024-4-9`

注释解释：

- `<>`必填项，必须在当前位置填写相应数据

- `{}`必选项，必须在当前位置选择一个给出的选项

- `[]`可选项，可以选择填写或忽略

*注：该笔记内的可选项和参数均不完整，如有需要，请查询相关手册*

# Linux目录结构

Linux的文件结构为树形，但是没有盘符这个概念，所有的文件都储存在根目录 `/` 下。在Windows系统中，路径之间的层级关系用 `\` 来表示，而Linux中则用 `/` 来表示

- Windows： `C:\user\kiiz\desktop\word.txt`

- Linux： `/usr/local/games/word.txt`

# Linux命令基础

Linux中的命令都有通用格式

`<command> [-options] [parameter]`

- `<command>` ：需要执行的命令

- `[-options]` ：命令的一些选项，可以控制命令的行为细节

- `[parameter]` ：命令的参数，多数用于指向命令的目标

**举例**

`ls -l /usr` ：查看根目录下usr目录下的所有文件信息， `ls` 是命令， `-l` 是可选项， `/usr` 是参数

> <img src="./IMG/Screenshot 2024-04-01 190339.png">

## ls命令

`ls [-a -l -h] [path]`

ls命令是列出目录下所有内容，有三个常用可选项，可以指定一个参数

> <img src="./IMG/Screenshot 2024-04-01 191506.png">

- `[-a]` ：列出指定目录的所有内容，包括隐藏文件

- `[-l]` ：以列表形式列出内容

- `[-h]` ：让文件大小以易于阅读的方式列出，必须与 `-l` 搭配使用

以上三个选项中， `-a` 可以与 `-l` 和 `-h` 混合使用，如 `-a -l -h` 、 `-alh`等，
> <img src="./IMG/Screenshot 2024-04-01 192050.png">

- `[path]` ：指定需要列出内容的目录，默认是当前目录

## mkdir命令

`mkdir [-p] <path1> <path2>...`

mkdir命令是创建文件夹，有一个常用可选项，必须指定一个到多个参数

- `<path>` ：创建文件夹的路径

> <img src="./IMG/Screenshot 2024-04-01 194009.png">

- `[-p]` ：允许用户创建多层级目录

> <img src="./IMG/Screenshot 2024-04-01 194046.png">

## touch命令

`touch <filename1> <filename2>...`

touch命令可以创建文件，必须指定一个到多个参数

- `filename` ：创建文件的文件名，可以包含路径

> <img src="./IMG/Screenshot 2024-04-01 194731.png">

## cat/more命令

`cat/more <filename>`

cat/more命令可以查看文本文件的内容，cat一次性展示全部内容，more分次展示所有内容，必须指定一个参数

- `<filename>`：需要查阅文件的文件名，可以包含路径

- cat：

> <img src="./IMG/Screenshot 2024-04-01 195418.png">

- more：

> <img src="./IMG/Screenshot 2024-04-01 195547.png">

*注：在more状态下，使用 `Space` 翻页， `Enter` 下拉一行， `B` 返回上一页， `Q` 退出*

## cp命令

`cp [-r] <target> <destination>`

cp命令可以复制文件或者文件夹，有一个常用可选项，两个必须指定的参数

- `<target>` ：需要复制的文件或文件夹

- `<destination>` ：复制到的路径

> <img src="./IMG/Screenshot 2024-04-01 200321.png">

- `[-r]` ：声明被复制的目标是文件夹

> <img src="./IMG/Screenshot 2024-04-01 200414.png">

## mv命令

`mv <target> <destination>`

mv命令可以移动文件或者文件夹，有一个常用可选项，两个必须指定的参数

- `<target>` ：需要移动的文件或文件夹

- `<destination>` ：移动到的路径

> <img src="./IMG/Screenshot 2024-04-01 205055.png">

如果移动到的路径不存在，则会将被移动的文件改名

> <img src="./IMG/Screenshot 2024-04-01 205331.png">

## rm命令

`rm [-r -f] <file1> <file2>...`

rm命令用于删除文件和文件夹，有两个常用可选项，必须指定一个到多个参数

- `-r` ：与cp命令相同，用于声明被删除的目标是文件夹

- `-f` ：在root用户删除文件时，会弹出相关提示，使用该选项可以省略相关提示，普通用户不显示任何提示

- `<file>` ：指定需要删除的文件或文件夹

> <img src="./IMG/Screenshot 2024-04-01 210002.png">

rm命令的参数支持使用通配符

- `abc*` ：以abc开头的文件或文件夹

- `*abc` ：以abc结尾的文件或文件夹

- `*abc*` ：包含abc的文件或文件夹

> <img src="./IMG/Screenshot 2024-04-01 214454.png">

*注：此处仅展示了部分通配符的用法，详细用法请查阅相关手册*

## which命令

`which <command1> <command2>...`

which命令可以查询一个命令对应的可执行程序在Linux系统中所在的位置，必须指定一个或多个参数

- `<command>` ：需要查询的命令

> <img src="./IMG/Screenshot 2024-04-02 110934.png">

## find命令

`find <init_path> {-name / -size} {filename / {+ / -}n{K / M / G}}`

find命令可以查询文件所在位置，有一个常用可选项，必须指定两个或多个参数

- `<init_path>` ：查询的起始路径，查询范围仅包含该路径及其子目录

- `{-name}` ：将匹配模式设置为按文件名匹配

    - `{filename}` ：需要查询的文件名，可以包含路径，可以使用通配符

> <img src="./IMG/Screenshot 2024-04-02 112557.png">

- `{-size}` ：将匹配模式设置为按文件大小匹配

    - `{+ / -}` ：加号代表大于，减号代表小于

    - `{n}` ：指定的文件大小的数字

    - `{k / M / G}` ：指定的文件大小的单位，k表示KiB，M表示MiB，G表示GiB，k必须小写，M个G必须大写

> <img src="./IMG/Screenshot 2024-04-02 113537.png">

## grep命令

`grep [-n] <keyword> <filename>`

grep命令可以通过关键字来查询文本文件中相应的行，有一个常用可选项，必须指定两个参数

- `[-n]` ：显示查询结果的行号

- `<keyword>` ：需要查询的关键字，若带有空格或特殊字符，需要使用双引号

- `<filename>` ：需要查询的文件，可以包含路径

> <img src="./IMG/Screenshot 2024-04-02 115646.png">

## wc命令

`wc [-c -m -l -w] <filename>`

wc命令可以统计文本文件中的相关信息，有四个常用可选项，必须指定一个参数。wc的查询结果是有排序的，默认为 `-l -w -m -c`

- `[-c]` ：统计字节数

- `[-m]` ：统计字符数

- `[-l]` ：统计行数

- `[-w]` ：统计空格数

- `<filename>` ：需要统计的文件，可以包含路径

> <img src="./IMG/Screenshot 2024-04-02 120436.png">

## 管道符|

- `|` ： 管道符，作用是将管道符左边命令的结果作为内容输入给右边的命令

**示例**

统计1.txt中带有逗号的行数

> <img src="./IMG/Screenshot 2024-04-02 121406.png">

统计根目录下最后修改时间为五月的文件夹的数量

> <img src="./IMG/Screenshot 2024-04-02 121741.png">

## echo命令

`echo <content>`

echo命令可以在命令行内输出内容，必须指定一个参数

- `<content>` ：需要输出的内容

> <img src="./IMG/Screenshot 2024-04-02 175733.png">

- `反引号` ： `<content>` 中被反引号`括起来的内容会作为命令执行

> <img src="./IMG/Screenshot 2024-04-02 180452.png">

## 重定向符> >>

- `>` ：将左边命令的内容覆盖到右边的文件中

- `>>` ：将左边命令的内容追加到右边的文件中

**示例**

用根目录下的所有文件夹名覆盖1.txt文件

> <img src="./IMG/Screenshot 2024-04-02 181735.png">

将根目录内所有最后修改日期为May的文件数量追加到1.txt中

> <img src="./IMG/Screenshot 2024-04-02 182258.png">

## tail命令

`tail [-f -n] <filename>`

tail命令可以查看文件尾部内容，有两个可选项，必须指定一个参数

- `[-f]` ：启用持续跟踪，立刻返回文件更改内容，即监听

- `[-n]` ：指定需要查看的行数，默认10行

- `<filename>` ：需要查看的文件

> <img src="./IMG/Screenshot 2024-04-02 183422.png">

## vi/vim命令

`vi/vim <filename>`

vi/vim可用于对文本的编辑，有四种模式 `命令模式` 、 `输入模式` 、 `底线命令模式` 和 `搜索模式` ，必须指定一个参数

- `命令模式` ：键盘的所有输入均视为命令，按 `i` 进入输入模式， `:` 进入底线命令模式， `/` 进入搜索模式，任何情况按 `esc` 都可以回到命令模式

> <img src="./IMG/Screenshot 2024-04-02 192706.png">

- `输入模式` ：启用对文本内容的更改

> <img src="./IMG/Screenshot 2024-04-02 192758.png">

- `搜索模式` ：搜索相应内容，按 `n` 向下搜索， `shift + n` 向上搜索

> <img src="./IMG/Screenshot 2024-04-02 192910.png">

- `底线命令模式` ：执行文件的保存和退出，按 `w` 保存， `q` 退出

> <img src="./IMG/Screenshot 2024-04-02 192958.png">

- `<filename>` ：需要编辑的文件，若不存在，则新建该文件

---

# 权限

## su命令

`su [-] <user>`

su命令可以切换用户，必须指定一个参数，可选一个参数

- `[-]` ：是否加载环境变量

- `<user>` ：指定需要切换的用户

## 添加sudo权限

在命令前面加上 `sudo` 可以使命令以root权限执行，但是只有拥有sudo权限的用户才可以使用sudo权限，因此需要给指定的用户添加sudo权限

首先切换到root用户，使用 `visudo` 命令打开sudoers文件，也可以通过 `vim /etc/sudoers` 打开

> <img src="./IMG/Screenshot 2024-04-06 132304.png">

在最后一行添加 `kiiz  ALL=(ALL)   NOPASSWD: ALL` 就可以让kiiz用户有权限使用sudo，并且使用时不需要输入密码

> <img src="./IMG/Screenshot 2024-04-06 132530.png">

使用sudo命令在根目录下创建文件夹

> <img src="./IMG/Screenshot 2024-04-06 132755.png">

## 用户组管理

### 创建用户组

`groupadd <group>`

groupadd命令可以创建用户组，必须指定一个参数，且必须拥有root权限

- `<group>` ：需要创建的用户组名

> <img src="./IMG/Screenshot 2024-04-06 133929.png">

### 删除用户组

`groupdel <group>`

groupdel命令可以删除用户组，必须指定一个参数，且必须拥有root权限

- `<group>` ：需要删除的用户组名

> <img src="./IMG/Screenshot 2024-04-06 134419.png">

## 用户管理

### 创建用户

`useradd [-g -m] <username>`

useradd命令可以创建用户，有两个可选项，必须指定一个参数，且必须拥有root权限

- `[-g]` ：指定用户加入的用户组，如果没有指定，则创建当前用户名相同的用户组

- `[-m]` ：创建用户home目录

- `<username>` ：需要创建的用户

> <img src="./IMG/Screenshot 2024-04-06 141213.png">

### 删除用户

`userdel [-r] <username>`

userdel命令可以删除用户，有一个可选项，必须指定一个参数，且必须拥有root权限

- `[-r]` ：是否删除home目录

- `<username>` ：需要删除的用户

> <img src="./IMG/Screenshot 2024-04-06 141448.png">

### 查询用户信息

`id [user]`

id命令可以查询用户信息，可选一个参数

- `[user]` ：指定需要查询的用户名，默认当前用户

> <img src="./IMG/Screenshot 2024-04-06 141901.png">

### 修改用户组

`usermod [-g -G] <username>`

usermod命令可以设置用户的相关信息，有两个常用选项，必须指定一个参数，且必须拥有root权限

- `[-g]` ：指定用户的基本组，即gid对应的组

- `[-G]` ：指定用户的扩展组，可以指定多个

- `<username>` ：需要修改用户组的用户

> <img src="./IMG/Screenshot 2024-04-06 143208.png">

## 权限管理

在命令 `ls -l` 中，显示了文件对应的权限信息

> <img src="./IMG/Screenshot 2024-04-06 144035.png">

第一列展示文件或文件夹的权限控制信息，第三列展示所属用户，第四列展示所属用户组

**第一列详解**

第一列有十个字母，第一个字母表示文件类型， `l` 为软链接，也就是文件夹的快捷方式， `d` 为文件夹， `-` 为文件；第二个到第四个字母表示所属用户拥有的权限， `r` 为读取权限， `w` 为写入权限， `x` 为执行权限， `-` 为没有该权限；第五个到第七个字母表示所属用户组拥有的权限，第八个到第十个表示其他用户拥有的权限

**示例**

> <img src="./IMG/Screenshot 2024-04-06 145528.png">

boot文件的第一列为 `drwxr-xr-x` ，从左往右依次，d表示该文件是一个文件夹，rwx表示所属的用户root拥有读取、写入、执行权限，r-x表示所属权限组root拥有读取和执行权限，没有写入权限，第二个r-x表示其他用户拥有读取和执行权限，没有写入权限

### 修改权限

`chmod [-R] <u=,g=,o=> <filename>`

chmod命令可以修改文件或文件夹的权限，有一个常用可选项，必须指定四个参数，且必须拥有root权限或是文件的所属用户

- `[-R]` ：对文件夹内所有内容应用相关权限的修改

- `<u=,g=,o=>` ：需要修改的权限

- `<filename>` ：需要修改的文件

> <img src="./IMG/Screenshot 2024-04-06 151705.png">

Linux的权限信息实际上是一个二进制数，修改权限也可以通过二进制数的方式

> <img src="./IMG/Screenshot 2024-04-06 152239.png">

7的二进制为111，表示u=rwx，5的二进制为101，表示g=r-x，1的二进制为001，表示--x

### 修改所属用户/组

`chown [-R] [user]:[group] <filename>`

chown命令可以修改文件或文件夹的所属用户/用户组，有一个常用参数，必须指定一个参数，可选两个参数，且必须拥有root权限

- `[-R]` ：对文件夹内所有内容应用相关权限的修改

- `[user]` ：指定所属用户

- `[group]` ：指定所属用户组

- `<filename>` ：需要修改的文件

> <img src="./IMG/Screenshot 2024-04-07 170916.png">

---

# Linux基础操作

## 系统操作

### history命令

`history [num]`

history命令可以查看所有命令执行历史，可选一个参数

- `[num]` ：指定需要查看的命令行数

> <img src="./IMG/Screenshot 2024-04-07 172050.png">

### apt命令

`apt [-y] {install | remove | search} <software>`

apt命令可以安装一些常用软件，有一个常用可选项，必须指定两个参数，且必须拥有root权限

- `[-y]` ：跳过确认环节

- `{install}` ：执行安装程序

- `{remove}` ：执行卸载程序

- `{search}` ：执行搜索程序

- `<software>` ：需要安装的软件

> <img src="./IMG/Screenshot 2024-04-07 174018.png">

### systemctl命令

`systemctl {start | stop | status | enable | disable} <service>`

systemctl命令可以控制系统服务的启动、停止、自启动等功能，必须指定两个参数，且必须拥有root权限

- `start` ：启动服务

- `stop` ：停止服务

- `status` ：查询服务状态

- `enable` ：启用自启动

- `disable` ：禁用自启动

- `service` ：需要设置的服务

> <img src="./IMG/Screenshot 2024-04-07 174817.png">

### ln命令

`ln [-s] <path> <file>`

ln命令可以创建链接，有一个常用可选项，必须指定两个参数

- `[-s]` ：创建软链接

- `<path>` ：指定被链接到的文件

- `<file>` ：指定链接名

*注：Linux中有两种链接：硬链接和软链接，类似引用和指针的区别，硬链接本质上和源文件是同一个文件，在Linux的索引节点号是相同的，而软链接中储存的是源文件的路径，软链接本身的索引节点号与源文件不同*

> <img src="./IMG/Screenshot 2024-04-08 131108.png">

## 文件传输

### wget命令

`wget [-b] <url>`

wget命令可以下载网络资源，有一个常用可选项，必须指定一个参数

- `[-b]` ：启用后台下载

- `<url>` ：指定下载文件的url

> <img src="./IMG/Screenshot 2024-04-08 142858.png">

### curl命令

`curl [-O] <url>`

curl命令可以对指定url发起请求

- `[-O]` ：下载url文件

- `<url>` ：指定请求的url

> <img src="./IMG/Screenshot 2024-04-08 143415.png">

## 网络端口

### nmap命令

`nmap <ip>`

nmap命令可以查看指定ip中端口的占用情况，必须指定一个参数

- `<ip>` ：需要查看的ip

> <img src="./IMG/Screenshot 2024-04-08 144510.png">

### netstat命令

`netstat [-a -n -p]`

netstat命令可以查看本机端口占用情况，有三个常用可选项

- `[-a]` ：显示所有端口信息

- `[-n]` ：将所有域名转换为ip

- `[-p]` ：显示正在使用端口的程序识别码和名称

> <img src="./IMG/Screenshot 2024-04-08 145544.png">

## 进程管理

### ps命令

`ps [-e -f]`

ps命令可以列出Linux进程，有两个常用可选项

- `[-e]` ：列出全部进程

- `[-f]` ：格式化输出

> <img src="./IMG/Screenshot 2024-04-08 150426.png">

### kill命令

`kill [-9] <id>`

kill命令用于终止进程，有一个常用可选项，必须指定一个参数

- `[-9]` ：强制结束进程

- `<id>` ：需要结束的进程id

> <img src="./IMG/Screenshot 2024-04-08 151342.png">

## 磁盘管理

### df命令

`df [-h]`

df命令可以查看硬盘占用信息，有一个常用可选项

- `[-h]` ：使用易于阅读的方式显示

> <img src="./IMG/Screenshot 2024-04-09 105905.png">

### iostat命令

`iostat [-x] [num1] [num2]`

iostat命令可以查看cpu和硬盘信息，有一个常用可选项，可以指定两个参数

- `[-x]` ：显示更多信息

- `[num1]` ：指定刷新时间

- `[num2]` ：指定刷新次数

> <img src="./IMG/Screenshot 2024-04-09 110313.png">

## 环境变量

### 查看环境变量

`env`

env命令可以查看目前系统中所有的环境变量

> <img src="./IMG/Screenshot 2024-04-09 111517.png">

### 创建环境变量

**临时生效**

`export <var>=<value>`

export命令可以临时创建环境变量，必须指定两个参数

- `<var>` ：环境变量名

- `<value>` ：环境变量值

> <img src="./IMG/Screenshot 2024-04-09 111930.png">

*注：调用临时变量时，需要在变量前添加$*

**长期生效**

- 当前用户

在 `~/.bashrc` 文件中添加临时创建的命令

> <img src="./IMG/Screenshot 2024-04-09 112454.png">

- 所有用户

## 解压缩文件

### tar命令

`tar [-c -x -v -z -f] {<zip> <file1> <file2>... | <zip> [-C] [path]}`

tar命令用于解压缩文件，有多个常用可选项，必须指定一个到多个参数，可用于.tar和.gz文件

- `[-c]` ：压缩模式

- `[-x]` ：解压模式

- `[-v]` ：显示解压缩进度条

- `[-z]` ：启用gzip模式，默认tar模式，一般置于首位

- `[-C]` ：指定解压文件目的地

- `[-f]` ：指定要压缩的文件，该选项必须置于末尾

- `<zip>` ：压缩包名

- `<file>` ：需要压缩的源文件

- `<path>` ：解压到的路径

压缩模式

> <img src="./IMG/Screenshot 2024-04-09 120039.png">

解压模式

> <img src="./IMG/Screenshot 2024-04-09 120500.png">

### zip命令

`zip [-r] <zip> <file1> <file2>...`

zip命令用于压缩文件，有一个常用可选项，必须指定两个到多个参数，可用于.zip

- `[-r]` ：指定被压缩文件为文件夹

- `<zip>` ：压缩包名

- `<file>` ：需要压缩的源文件

> <img src="./IMG/Screenshot 2024-04-09 183739.png">

### unzip命令

`unzip <zip> [-d] <path>`

unzip命令用于解压文件，有一个常用可选项，必须指定两个参数，可用于.zip

- `[-d]` ：指定解压路径

- `<zip>` ：压缩包名

- `<path>` ：解压到的路径

> <img src="./IMG/Screenshot 2024-04-09 184148.png">

