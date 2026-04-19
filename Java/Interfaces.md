# 接口文档

## 1. 部门管理

### 1.1 部门列表查询

#### 1.1.1 基本信息

请求路径：`/depts`

请求方式：`GET`

描述：该接口用于部门列表数据查询

#### 1.1.2 请求参数

无

#### 1.1.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名            | 类型     | 是否必须 | 备注                         |
| ----------------- | -------- | -------- | ---------------------------- |
| code              | number   | 必须     | 响应码，1表示成功，0表示失败 |
| msg               | string   | 非必须   | 提示信息                     |
| data              | object[] | 非必须   | 返回的数据体                 |
| data - id         | number   | 非必须   | 部门id                       |
| data - name       | string   | 非必须   | 部门名称                     |
| data - createTime | string   | 非必须   | 创建时间                     |
| data - updateTime | string   | 非必须   | 修改时间                     |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": [
        {
            "id": 1,
            "name": "学工部",
            "createTime": "2026-04-08T15:00:00",
            "updateTime": "2026-04-08T15:00:00"
        },
        {
            "id": 2,
            "name": "教研部",
            "createTime": "2026-04-08T15:00:00",
            "updateTime": "2026-04-08T15:00:00"
        }
    ]
}
```

### 1.2 删除部门

#### 1.2.1 基本信息

请求路径：`/depts`

请求方式：`DELETE`

接口描述：该接口用户根据ID删除部门数据

#### 1.2.2 请求参数

参数说明
| 参数名 | 类型   | 是否必须 | 备注   |
| ------ | ------ | -------- | ------ |
| id     | number | 必须     | 部门ID |

请求参数样例：

`/depts?id=1`

`/depts?id=2`

#### 1.2.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                         |
| ------ | ------ | -------- | ---------------------------- |
| code   | number | 必须     | 响应码，1表示成功，0表示失败 |
| msg    | string | 非必须   | 提示信息                     |
| data   | object | 非必须   | 返回的数据体                 |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": null
}
```

 ### 1.3 新增部门

#### 1.3.1 基本信息

请求方式：`POST`

接口描述：该接口用于添加部门数据

#### 1.3.2 请求参数

参数格式：`application/json`

参数说明：

| 参数名 | 类型   | 是否必须 | 备注     |
| ------ | ------ | -------- | -------- |
| name   | string | 必须     | 部门名称 |

请求参数样例：

```json
{
    "name": "教研部"
}
```

#### 1.3.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                         |
| ------ | ------ | -------- | ---------------------------- |
| code   | number | 必须     | 响应码，1表示成功，0表示失败 |
| msg    | string | 非必须   | 提示信息                     |
| data   | object | 非必须   | 返回的数据体                 |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": null
}
```

### 1.4 根据ID查询部门信息

#### 1.4.1 基本信息

请求路径：`/depts/{id}`

请求方式：`GET`

接口描述：该接口用于根据ID查询部门数据

#### 1.4.2 请求参数

参数格式：路径参数

参数说明：

| 参数名 | 类型   | 是否必须 | 备注   |
| ------ | ------ | -------- | ------ |
| id     | number | 必须     | 部门ID |

请求参数样例：

`/depts/1`

`/depts/3`

#### 1.4.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名            | 类型   | 是否必须 | 备注                         |
| ----------------- | ------ | -------- | ---------------------------- |
| code              | number | 必须     | 响应码，1表示成功，0表示失败 |
| msg               | string | 非必须   | 提示信息                     |
| data              | object | 非必须   | 返回的数据体                 |
| data - id         | number | 非必须   | 部门id                       |
| data - name       | string | 非必须   | 部门名称                     |
| data - createTime | string | 非必须   | 创建时间                     |
| data - updateTime | string | 非必须   | 修改时间                     |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": {
        "id": 1,
        "name": "学工部",
        "createTime": "2026-04-08T15:00:00",
        "updateTime": "2026-04-08T15:00:00"
    }
}
```

### 1.5 修改部门

#### 1.5.1 基本信息

请求路径：`/depts`

请求方式：`PUT`

接口描述：该接口用于修改部门数据

#### 1.5.2 请求参数

参数格式：`application/json`

参数说明：

| 参数名 | 类型   | 是否必须 | 备注     |
| ------ | ------ | -------- | -------- |
| id     | number | 必须     | 部门ID   |
| name   | string | 必须     | 部门名称 |

请求参数样例：

```json
{
    "id": 1,
    "name": "学工部"
}
```

#### 1.5.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                         |
| ------ | ------ | -------- | ---------------------------- |
| code   | number | 必须     | 响应码，1表示成功，0表示失败 |
| msg    | string | 非必须   | 提示信息                     |
| data   | object | 非必须   | 返回的数据体                 |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": null
}
```

## 2. 员工管理

### 2.1 员工列表查询

#### 2.1.1 基本信息

请求路径：`/emps`

请求方式：`GET`

接口描述：该接口用于员工列表数据的条件分页查询

#### 2.1.2 请求参数

参数格式：`queryString`

参数说明：

| 参数名   | 是否必须 | 示例     | 备注                           |
| -------- | -------- | -------- | ------------------------------ |
| name     | 否       | 张       | 姓名                           |
| sex      | 否       | 男       | 性别                           |
| begin    | 否       | 2010-1-1 | 范围匹配的开始时间             |
| end      | 否       | 2010-1-1 | 范围匹配的结束时间             |
| page     | 是       | 1        | 分页查询的页码，默认为1        |
| pageSize | 是       | 10       | 分页查询的每页记录数，默认为10 |

请求数据样例：

`/emps?name=张&sex=男&begin=2010-1-1&end=2010-1-1&page=1&pageSize=10`

#### 2.1.3 响应数据

参数格式：`application/json`

参数说明：

| 名称            | 类型     | 是否必须 | 备注                         |
| --------------- | -------- | -------- | ---------------------------- |
| code            | number   | 必须     | 响应码，1表示成功，0表示失败 |
| msg             | string   | 非必须   | 提示信息                     |
| data            | object   | 必须     | 返回的数据体                 |
| data-total      | number   | 必须     | 总记录数                     |
| data-rows       | object[] | 必须     | 数据列表                     |
| rows-id         | number   | 非必须   | 员工id                       |
| rows-name       | string   | 非必须   | 员工姓名                     |
| rows-birth      | string   | 非必须   | 员工出生日期                 |
| rows-sex        | string   | 非必须   | 员工性别                     |
| rows-avatarPath | string   | 非必须   | 员工头像url                  |
| rows-deptName   | string   | 非必须   | 员工部门名称                 |
| rows-jobName    | string   | 非必须   | 员工职称                     |
| rows-boardDate  | string   | 非必须   | 员工入职日期                 |
| rows-updateTime | string   | 非必须   | 最后修改时间                 |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": {
        "total": 2,
        "rows": [
            {
                "id": 1,
                "name": "张三",
                "birth": "1990-01-01",
                "sex": "男",
                "avatarPath": "https://example.eiousee.com/zhangsan.jpg",
                "deptName": "学工部",
                "jobName": "助教",
                "boardDate": "2020-01-01",
                "updateTime": "2020-01-01T00:00:00"
            },
            {
                "id": 2,
                "name": "李四",
                "birth": "1990-01-01",
                "sex": "女",
                "avatarPath": "https://example.eiousee.com/lisi.jpg",
                "deptName": "学工部",
                "jobName": "助教",
                "boardDate": "2020-01-01",
                "updateTime": "2020-01-01T00:00:00"
            },
        ]
    }
}
```

### 2.2 添加员工

#### 2.2.1 基本信息

请求路径：`/emps`

请求方式：`POST`

接口描述：该接口用于添加员工信息

#### 2.2.2 请求参数

参数格式：`application/json`

参数说明：

| 名称                 | 类型     | 是否必须 | 备注                             |
| -------------------- | -------- | -------- | -------------------------------- |
| name                 | string   | 必须     | 员工姓名                         |
| birth                | string   | 必须     | 员工出生日期，格式为"YYYY-MM-DD" |
| sex                  | string   | 必须     | 性别                             |
| avatarPath           | string   | 非必须   | 头像url                          |
| deptName             | string   | 非必须   | 部门名称                         |
| jobName              | string   | 非必须   | 职位名称                         |
| boardDate            | string   | 非必须   | 入职日期                         |
| empExpList           | object[] | 非必须   | 工作经历                         |
| empExpList-startTime | string   | 非必须   | 开始时间                         |
| empExpList-endTime   | string   | 非必须   | 离职时间                         |
| empExpList-company   | string   | 非必须   | 所在公司名                       |
| empExpList-job       | string   | 非必须   | 担任职位                         |

请求数据样例：

```json
{
    "name": "张三",
    "birth": "2020-01-01",
    "sex": "男",
    "avatarPath": "http://example.eiousee.com/zhangsan.jpg",
    "deptName": "学工部",
    "jobName": "工程师",
    "boardDate": "2020-01-01",
    "empExps": [
        {
            "startTime": "2020-01-01",
            "endTime": "2020-01-01",
            "company": "艾欧希股份有限公司",
            "job": "Java开发工程师"
        },
        {
            "startTime": "2021-01-01",
            "endTime": "2021-01-01",
            "company": "艾欧希股份有限公司",
            "job": "C++开发工程师"
        }
    ]
}
```

#### 2.2.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                         |
| ------ | ------ | -------- | ---------------------------- |
| code   | number | 必须     | 响应码，1表示成功，0表示失败 |
| msg    | string | 非必须   | 提示信息                     |
| data   | object | 非必须   | 返回的数据体                 |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": null
}
```

### 2.3 删除员工

#### 2.3.1 基本信息

请求路径：`/emps`

请求方式：`DELETE`

接口描述：该接口用于批量删除员工的数据信息

#### 2.3.2 请求参数

参数格式：查询参数

参数说明：

| 参数名 | 类型  | 示例    | 是否必须 | 备注     |
| ------ | ----- | ------- | -------- | -------- |
| ids    | array | 1, 2, 3 | 是       | 员工的id |

请求参数样例：`/emps?ids=1,2,3`

#### 2.3.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                         |
| ------ | ------ | -------- | ---------------------------- |
| code   | number | 是       | 响应码，1代表成功，0代表失败 |
| msg    | string | 非必须   | 提示信息                     |
| data   | object | 非必须   | 返回的数据                   |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": null
}
```

### 2.4 根据ID查询

#### 2.4.1 基本信息

请求路径：`/emps/{id}`

请求方式：`GET`

接口描述：该接口用于根据主键`ID`查询员工的信息

#### 2.4.2 请求参数

参数格式：路径参数

参数说明：

| 参数名 | 类型   | 是否必须 | 备注   |
| ------ | ------ | -------- | ------ |
| id     | number | 必须     | 员工ID |

请求参数样例：`/emps/1`

#### 2.4.3 响应数据

参数格式：`application/json`

参数说明：

| 名称                 | 类型     | 是否必须 | 备注                         |
| -------------------- | -------- | -------- | ---------------------------- |
| code                 | number   | 是       | 响应码，1代表成功，0代表失败 |
| msg                  | string   | 非必须   | 提示信息                     |
| data                 | object   | 非必须   | 返回的数据，员工信息         |
| data-id              | number   | 非必须   | 员工id                       |
| data-name            | string   | 非必须   | 员工姓名                     |
| data-birth           | string   | 非必须   | 员工出生日期                 |
| data-sex             | string   | 非必须   | 员工性别                     |
| data-avatarPath      | string   | 非必须   | 员工头像url                  |
| data-deptName        | string   | 非必须   | 部门名称                     |
| data-jobName         | string   | 非必须   | 职位名称                     |
| data-boardDate       | string   | 非必须   | 入职日期                     |
| data-empExpList      | object[] | 非必须   | 员工工作经历                 |
| empExpList-startTime | string   | 非必须   | 开始时间                     |
| empExpList-endTime   | string   | 非必须   | 离职时间                     |
| empExpList-company   | string   | 非必须   | 公司名                       |
| empExpList-job       | string   | 非必须   | 职位名称                     |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": {
        "id": 1,
        "name": "张三",
        "birth": "2010-01-01",
        "sex": "男",
        "avatarPath": "https://example.eiousee.com/zhangsan.jpg",
        "deptName": "学工部",
        "jobName": "主任",
        "boardDate": "2020-01-01",
        "empExpList": [
            {
                "startTime": "2011-01-01",
                "endTime": "2011-01-01",
                "company": "阿里巴巴",
                "job": "Java开发"
            },
            {
                "startTime": "2012-01-01",
                "endTime": "2012-01-01",
                "company": "腾讯",
                "job": "Java开发"
            }
        ]
    }
}
```

### 2.5 修改员工

#### 2.5.1 基本信息

请求路径：`/emps`

请求方式：`PUT`

接口描述：该接口用于修改员工的数据信息

#### 2.5.2 请求参数

参数格式：`application/json`

参数说明：

| 名称                 | 类型     | 是否必须 | 备注                     |
| -------------------- | -------- | -------- | ------------------------ |
| id                   | number   | 必须     | 员工id                   |
| avatarPath           | string   | 非必须   | 员工头像url              |
| boardDate            | string   | 非必须   | 入职日期，格式YYYY-MM-DD |
| deptName             | string   | 非必须   | 部门名称                 |
| jobName              | string   | 非必须   | 职位名称                 |
| empExpList           | object[] | 非必须   | 工作经历                 |
| empExpList-id        | number   | 非必须   | 员工id                   |
| empExpList-startTime | string   | 非必须   | 开始时间                 |
| empExpList-endTime   | string   | 非必须   | 离职时间                 |
| empExpList-company   | string   | 非必须   | 公司名                   |
| empExpList-job       | string   | 非必须   | 职位名称                 |

请求数据样例：

```json
{
	"id": 1,
    "avatarPath": "https://example.eiousee.com/zhangsan.jpg",
    "deptName": "学工部",
    "jobName": "主任",
    "boardDate": "2020-01-01",
    "empExpList": [
        {
            "id": 1,
            "startTime": "2011-01-01",
            "endTime": "2011-01-01",
            "company": "阿里巴巴",
            "job": "Java开发"
        },
        {
            "id": 1,
            "startTime": "2012-01-01",
            "endTime": "2012-01-01",
            "company": "腾讯",
            "job": "Java开发"
        }
    ]
}
```

#### 2.5.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                         |
| ------ | ------ | -------- | ---------------------------- |
| code   | number | 必须     | 响应码，1表示成功，0表示失败 |
| msg    | string | 非必须   | 提示信息                     |
| data   | object | 非必须   | 返回的数据体                 |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": null
}
```

### 2.6 查询所有员工姓名

#### 2.6.1 基本信息

请求路径：`/emps/names`

请求方式：`GET`

接口描述：该接口用于查询所有员工姓名

#### 2.6.2 请求参数

无

#### 2.6.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型     | 是否必须 | 备注                         |
| ------ | -------- | -------- | ---------------------------- |
| code   | number   | 是       | 响应码，1代表成功，0代表失败 |
| msg    | string   | 非必须   | 提示信息                     |
| data   | object[] | 非必须   | 返回的数据                   |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": [
        "刘洋",
        "张三",
        "张伟",
        "张强",
        "李军",
        "李四",
        "李娜",
        "杨敏",
        "浔麟",
        "王五",
        "王芳",
        "王静",
        "赵六"
    ]
}
```

## 3. 班级管理

### 3.1 条件分页查询

#### 3.1.1 基本信息

请求路径：`/class`

请求方式：`GET`

接口描述：该接口用于班级列表数据的条件分页查询

#### 3.1.2 请求参数

参数格式：`queryString`

参数说明：

| 参数名    | 是否必须 | 示例        | 备注                           |
| --------- | -------- | ----------- | ------------------------------ |
| className | 否       | 软件工程1班 | 班级名称                       |
| begin     | 否       | 2010-01-01  | 范围匹配的开始时间             |
| end       | 否       | 2010-01-01  | 范围匹配的结束时间             |
| page      | 是       | 1           | 分页查询的页码，默认为1        |
| pageSize  | 是       | 10          | 分页查询的每页记录数，默认为10 |

请求数据样例：

`/class?className=软件工程1班&begin=2010-01-01&end=2010-01-01&page=1&pageSize=10`

#### 3.1.3 响应数据

参数格式：`application/json`

参数说明：

| 名称             | 类型     | 是否必须 | 备注                         |
| ---------------- | -------- | -------- | ---------------------------- |
| code             | number   | 必须     | 响应码，1表示成功，0表示失败 |
| msg              | string   | 非必须   | 提示信息                     |
| data             | object   | 必须     | 返回的数据体                 |
| data-total       | number   | 必须     | 总记录数                     |
| data-rows        | object[] | 必须     | 数据列表                     |
| rows-id          | number   | 非必须   | 班级号                       |
| rows-className   | string   | 非必须   | 班级名称                     |
| rows-classroom   | string   | 非必须   | 班级教室                     |
| rows-teacherName | string   | 非必须   | 班主任名称                   |
| rows-startDate   | string   | 非必须   | 开课时间                     |
| rows-endDate     | string   | 非必须   | 结课时间                     |
| rows-classStatus | string   | 非必须   | 班级状态                     |
| rows-updateTime  | string   | 非必须   | 最后更新时间                 |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": {
        "total": 2,
        "rows": [
            {
                "id": 20200101,
                "className": "软件工程1班",
                "classroom": "101",
                "teacherName": "张三",
                "startDate": "2020-09-01",
                "endDate": "2021-06-30",
                "classStatus": "未开课",
                "updateTime": "2020-09-01T00:00:00"
            },
             {
                "id": 20200102,
                "className": "软件工程2班",
                "classroom": "102",
                "teacherName": "李四",
                "startDate": "2020-09-01",
                "endDate": "2021-06-30",
                "classStatus": "未开课",
                "updateTime": "2020-09-01T00:00:00"
            }
        ]
	}
}
```

### 3.2 删除班级

#### 3.2.1 基本信息

请求路径：`/class/{id}`

请求方式：`DELETE`

接口描述：该接口用于删除班级信息

#### 3.2.2 请求参数

参数格式：路径参数

参数说明：

| 参数名 | 类型   | 示例 | 是否必须 | 备注     |
| ------ | ------ | ---- | -------- | -------- |
| id     | number | 1    | 必须     | 班级的ID |

请求参数样例：`/class/5`

#### 3.2.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                           |
| ------ | ------ | -------- | ------------------------------ |
| code   | number | 必须     | 响应码，1 代表成功，0 代表失败 |
| msg    | string | 非必须   | 提示信息                       |
| data   | object | 非必须   | 返回的数据                     |

响应数据样例：

```JSON
{
    "code":1,
    "msg":"success",
    "data":null
}
```

### 3.3 添加班级

#### 3.3.1 基本信息

请求路径：`/class`

请求方式：`POST`

接口描述：该接口用于添加班级信息

#### 3.3.2 请求参数

参数格式：`application/json`

参数说明：

| 参数名      | 类型   | 示例        | 是否必须 | 备注       |
| ----------- | ------ | ----------- | -------- | ---------- |
| id          | number | 20200101    | 是       | 班级号     |
| className   | string | 软件工程1班 | 是       | 班级名称   |
| classroom   | string | 101         | 非必须   | 教室       |
| teacherName | string | 张三        | 是       | 班主任名字 |
| startDate   | string | 2010-01-01  | 非必须   | 开课时间   |
| endDate     | string | 2010-01-01  | 非必须   | 结课时间   |
| classStatus | string | 开课中      | 是       | 班级状态   |

请求数据样例：

```json
{
    "id": 20200101,
    "className": "软件工程1班",
    "classroom": "101",
    "teacherName": "张三",
    "startDate": "2010-01-01",
    "endDate": "2010-01-01",
    "classStatus": "开课中"
}
```

#### 3.3.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                           |
| ------ | ------ | -------- | ------------------------------ |
| code   | number | 必须     | 响应码，1 代表成功，0 代表失败 |
| msg    | string | 非必须   | 提示信息                       |
| data   | object | 非必须   | 返回的数据                     |

响应数据样例：

```JSON
{
    "code":1,
    "msg":"success",
    "data":null
}
```

### 3.4 获取班级状态

#### 3.4.1 基本信息

请求路径：`/class/status`

请求方式：`GET`

接口描述：获取所有的班级状态名称

#### 3.4.2 请求参数

无

#### 3.4.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型     | 是否必须 | 备注                         |
| ------ | -------- | -------- | ---------------------------- |
| code   | number   | 是       | 响应码，1代表成功，0代表失败 |
| msg    | string   | 非必须   | 提示信息                     |
| data   | object[] | 非必须   | 返回的数据                   |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": [
        "已结课",
        "开课中",
        "未开课"
    ]
}
```

### 3.5 根据ID查询班级

#### 3.5.1 基本信息

请求路径：`/class/{id}`

请求方式：`GET`

接口描述：该接口用于根据主键ID查询班级的信息

#### 3.5.2 请求参数

参数格式：路径参数

参数说明：

| 参数名 | 类型   | 是否必须 | 备注   |
| ------ | ------ | -------- | ------ |
| id     | number | 必须     | 班级ID |

请求参数样例：`/class/8`

#### 3.5.3 响应数据

参数格式：`application/json`

参数说明：

| 名称             | 类型   | 是否必须 | 备注                         |
| ---------------- | ------ | -------- | ---------------------------- |
| code             | number | 必须     | 响应码，1表示成功，0表示失败 |
| msg              | string | 非必须   | 提示信息                     |
| data             | object | 必须     | 返回的数据体                 |
| data-id          | number | 必须     | 班级号                       |
| data-className   | string | 必须     | 班级名称                     |
| data-classroom   | string | 必须     | 班级教室                     |
| data-teacherName | string | 必须     | 班主任名称                   |
| data-startDate   | string | 必须     | 开课时间                     |
| data-endDate     | string | 必须     | 结课时间                     |
| data-classStatus | string | 必须     | 班级状态                     |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data":{
        "id": 20200101,
        "className": "软件工程1班",
        "classroom": "101",
        "teacherName": "张三",
        "startDate": "2020-09-01",
        "endDate": "2021-06-30",
        "classStatus": "未开课"
    }
}
```

### 3.6 修改班级

#### 3.6.1 基本信息

请求路径：`/class`

请求方式：`PUT`

接口描述：该接口用于修改班级的数据信息

#### 3.6.2 请求参数

参数格式：`application/json`

参数说明：

| 参数名      | 类型   | 示例        | 是否必须 | 备注       |
| ----------- | ------ | ----------- | -------- | ---------- |
| id          | number | 20200101    | 是       | 班级号     |
| className   | string | 软件工程1班 | 是       | 班级名称   |
| classroom   | string | 101         | 非必须   | 教室       |
| teacherName | string | 李四        | 是       | 班主任名字 |
| startDate   | string | 2010-01-01  | 非必须   | 开课时间   |
| endDate     | string | 2010-01-01  | 非必须   | 结课时间   |
| classStatus | string | 开课中      | 是       | 班级状态   |

请求数据样例：

```json
{
    "id": 20200101,
    "className": "软件工程1班",
    "classroom": "101",
    "teacherName": "李四",
    "startDate": "2010-01-01",
    "endDate": "2010-01-01",
    "classStatus": "开课中"
}
```

#### 3.6.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                           |
| ------ | ------ | -------- | ------------------------------ |
| code   | number | 必须     | 响应码，1 代表成功，0 代表失败 |
| msg    | string | 非必须   | 提示信息                       |
| data   | object | 非必须   | 返回的数据                     |

响应数据样例：

```JSON
{
    "code":1,
    "msg":"success",
    "data":null
}
```

### 3.7 获取所有班级详细信息

#### 3.7.1 基本信息

请求路径：`/class/list`

请求方式：`GET`

接口描述：该接口用于查询所有班级信息，但是由前端指定查询内容

#### 3.7.2 请求参数

参数格式：`queryString`

参数说明：

| 参数名      | 类型   | 是否必须 | 备注                                                |
| ----------- | ------ | -------- | --------------------------------------------------- |
| id          | number | 非必须   | 是否查询班级号，1表示查询，其他数字或null表示不查询 |
| className   | number | 非必须   | 是否查询班级名称                                    |
| classroom   | number | 非必须   | 是否查询班级教室                                    |
| teacherName | number | 非必须   | 是否查询班主任名称                                  |
| startDate   | number | 非必须   | 是否查询开课时间                                    |
| endDate     | number | 非必须   | 是否查询结课时间                                    |
| classStatus | number | 非必须   | 是否查询班级状态                                    |
| updateTime  | number | 非必须   | 是否查询最后更新时间                                |

请求数据样例：

`/class/list?id=1&classStatus=1`

#### 3.7.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名           | 类型   | 是否必须 | 备注                         |
| ---------------- | ------ | -------- | ---------------------------- |
| code             | number | 必须     | 响应码，1表示成功，0表示失败 |
| msg              | string | 非必须   | 提示信息                     |
| data             | object | 必须     | 返回的数据体                 |
| data-id          | number | 非必须   | 班级号                       |
| data-className   | string | 非必须   | 班级名称                     |
| data-classroom   | string | 非必须   | 班级教室                     |
| data-teacherName | string | 非必须   | 班主任名称                   |
| data-startDate   | string | 非必须   | 开课时间                     |
| data-endDate     | string | 非必须   | 结课时间                     |
| data-classStatus | string | 非必须   | 班级状态                     |
| data-updateTime  | string | 非必须   | 最后更新时间                 |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": [
       {
            "id": 20200101,
            "className": null,
            "classroom": null,
            "teacherName": null,
            "startDate": null,
            "endDate": null,
            "classStatus": "未开课",
            "updateTime": null
        },
        {
            "id": 20200102,
            "className": null,
            "classroom": null,
            "teacherName": null,
            "startDate": null,
            "endDate": null,
            "classStatus": "未开课",
            "updateTime": null
        }
    ]
}
```

## 4. 学生管理

### 4.1 学生信息分页查询

#### 4.1.1 基本信息

请求路径：`/student`

请求方式：`GET`

接口描述：该接口用于学员列表数据的条件分页查询

#### 4.1.2 请求参数

参数格式：`queryString`

参数说明：

| 参数名称      | 是否必须 | 示例        | 备注                                       |
| ------------- | -------- | ----------- | ------------------------------------------ |
| name          | 否       | 张三一      | 学员姓名                                   |
| educationName | 否       | 本科        | 学历ID                                     |
| className     | 否       | 软件工程1班 | 班级ID                                     |
| page          | 是       | 1           | 分页查询的页码，如果未指定，默认为1        |
| pageSize      | 是       | 10          | 分页查询的每页记录数，如果未指定，默认为10 |

请求数据样例：`/student?name=张&educationName=&className=&page=1&pageSize=5`

#### 4.1.3 响应数据

参数格式：`application/json`

参数说明：

| 名称               | 类型     | 是否必须 | 备注                         |
| ------------------ | -------- | -------- | ---------------------------- |
| code               | number   | 必须     | 响应码，1表示成功，0表示失败 |
| msg                | string   | 非必须   | 提示信息                     |
| data               | object   | 必须     | 返回的数据体                 |
| data-total         | number   | 必须     | 总记录数                     |
| data-rows          | object[] | 必须     | 数据列表                     |
| rows-id            | number   | 非必须   | 学号                         |
| rows-name          | string   | 非必须   | 学生姓名                     |
| rows-sex           | string   | 非必须   | 性别                         |
| rows-birth         | string   | 非必须   | 出生日期                     |
| rows-className     | string   | 非必须   | 班级名称                     |
| rows-majorName     | string   | 非必须   | 专业名称                     |
| rows-educationName | string   | 非必须   | 学历                         |
| rows-updateTime    | string   | 非必须   | 最后修改时间                 |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": {
        "total": 2,
        "rows": [
            {
                "id": 2020010101,
                "name": "张三一",
                "sex": "男",
                "birth": "1990-01-01",
                "className": "软件工程1班",
                "majorName": "软件工程",
                "educationName": "初中及以下",
                "updateTime": "2020-01-01T00:00:00"
            },
            {
                "id": 2020010102,
                "name": "张三二",
                "sex": "男",
                "birth": "1990-01-01",
                "className": "软件工程1班",
                "majorName": "软件工程",
                "educationName": "初中及以下",
                "updateTime": "2020-01-01T00:00:00"
            }
        ]
    }
}
```

### 4.2 专业信息查询

#### 4.2.1 基本信息

请求路径：`/student/major`

请求方式：`GET`

接口描述：获取所有的职位名称

#### 4.2.2 请求参数

无

#### 4.2.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型     | 是否必须 | 备注                         |
| ------ | -------- | -------- | ---------------------------- |
| code   | number   | 是       | 响应码，1代表成功，0代表失败 |
| msg    | string   | 非必须   | 提示信息                     |
| data   | object[] | 非必须   | 返回的数据                   |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": [
        "软件工程",
        "计算机科学",
        "网络工程"
    ]
}
```

### 4.3 学历信息查询

#### 4.3.1 基本信息

请求路径：`/student/education`

请求方式：`GET`

接口描述：获取所有的职位名称

#### 4.3.2 请求参数

无

#### 4.3.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型     | 是否必须 | 备注                         |
| ------ | -------- | -------- | ---------------------------- |
| code   | number   | 是       | 响应码，1代表成功，0代表失败 |
| msg    | string   | 非必须   | 提示信息                     |
| data   | object[] | 非必须   | 返回的数据                   |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": [
        "初中及以下",
        "高中或高职",
        "大专",
        "本科",
        "硕士",
        "博士",
        "非全日制硕士",
        "非全日制博士"
    ]
}
```

### 4.4 批量删除学生

#### 4.4.1 基本信息

请求路径：`/student/{ids}`

请求方式：`DELETE`

接口描述：该接口用于批量删除学员信息

#### 4.4.2 请求参数

参数格式：路径参数

参数说明：

| 参数名 | 类型 | 示例 | 是否必须 | 备注         |
| ------ | ---- | ---- | -------- | ------------ |
| ids    | 数组 | 1    | 必须     | 学员的ID数组 |

请求参数样例：`/student/1,2,3`

#### 4.4.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                           |
| ------ | ------ | -------- | ------------------------------ |
| code   | number | 必须     | 响应码，1 代表成功，0 代表失败 |
| msg    | string | 非必须   | 提示信息                       |
| data   | object | 非必须   | 返回的数据                     |

响应数据样例：

```JSON
{
    "code":1,
    "msg":"success",
    "data":null
}
```

### 4.5 添加学生

#### 4.5.1 基本信息

请求路径：`/student`

请求方式：`POST`

接口描述：该接口用于添加学员信息

#### 4.5.2 请求参数

参数格式：`application/json`

参数说明：

| 参数名        | 类型   | 是否必须 | 备注     |
| ------------- | ------ | -------- | -------- |
| id            | number | 是       | 学号     |
| name          | string | 是       | 姓名     |
| sex           | string | 是       | 性别     |
| birth         | string | 非必须   | 出生日期 |
| className     | string | 非必须   | 班级名称 |
| majorName     | string | 非必须   | 专业名称 |
| educationName | string | 非必须   | 学历     |

请求数据样例：

```JSON
{
    "id": 2020010101,
    "name": "张三一",
    "sex": "男",
    "birth": "2020-01-01",
    "className": "软件工程1班",
    "majorName": "软件工程",
    "educationName": "本科"
}
```

#### 4.5.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                           |
| ------ | ------ | -------- | ------------------------------ |
| code   | number | 必须     | 响应码，1 代表成功，0 代表失败 |
| msg    | string | 非必须   | 提示信息                       |
| data   | object | 非必须   | 返回的数据                     |

响应数据样例：

```JSON
{
    "code":1,
    "msg":"success",
    "data":null
}
```





## 5. 数据统计

### 5.1 员工性别统计

#### 5.1.1 基本信息

请求路径：`/report/empSexData`

请求方式：`GET`

接口描述：统计各个职位的员工人数

#### 5.1.2 请求参数

无

#### 5.1.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名         | 类型     | 是否必须 | 备注                         |
| -------------- | -------- | -------- | ---------------------------- |
| code           | number   | 是       | 响应码，1表示成功，0表示失败 |
| msg            | string   | 非必须   | 提示信息                     |
| data           | object   | 非必须   | 返回的数据体                 |
| data-titleList | string[] | 是       | 职位列表                     |
| data-dataList  | number[] | 是       | 人数列表                     |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": {
        "titleList": ["男", "女"],
        "dataList": [20, 12]
    }
}
```

### 5.2 员工职位人数统计

#### 5.2.1 基本信息

请求路径：`/report/empJobData`

请求方式：`GET`

接口描述：统计各个职位的员工人数

#### 5.2.2 请求参数

无

#### 5.2.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名         | 类型     | 是否必须 | 备注                         |
| -------------- | -------- | -------- | ---------------------------- |
| code           | number   | 是       | 响应码，1表示成功，0表示失败 |
| msg            | string   | 非必须   | 提示信息                     |
| data           | object   | 非必须   | 返回的数据体                 |
| data-titleList | string[] | 是       | 职位列表                     |
| data-dataList  | number[] | 是       | 人数列表                     |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": {
        "titleList": ["助教", "讲师", "副教授"],
        "dataList": [1, 1, 2]
    }
}
```

### 5.3 部门员工人数统计

#### 5.3.1 基本信息

请求路径：`/report/empDeptData`

请求方式：`GET`

接口描述：统计各个职位的员工人数

#### 5.3.2 请求参数

无

#### 5.3.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名         | 类型     | 是否必须 | 备注                         |
| -------------- | -------- | -------- | ---------------------------- |
| code           | number   | 是       | 响应码，1表示成功，0表示失败 |
| msg            | string   | 非必须   | 提示信息                     |
| data           | object   | 非必须   | 返回的数据体                 |
| data-titleList | string[] | 是       | 部门列表                     |
| data-dataList  | number[] | 是       | 人数列表                     |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": {
        "titleList": ["学工部", "金融部", "人事部"],
        "dataList": [1, 1, 2]
    }
}
```



## 6. 其他接口

### 6.1 登录

### 6.2 文件上传

#### 6.2.1 基本信息

请求路径：`/upload`

请求方式：`POST`

接口描述：上传图片接口

#### 6.2.2 请求参数

参数格式：`multipart/form-data`

参数说明：

| 参数名称 | 参数类型 | 是否必须 | 示例 | 备注 |
| -------- | -------- | -------- | ---- | ---- |
| file     | file     | 是       |      |      |

#### 6.2.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型   | 是否必须 | 备注                           |
| ------ | ------ | -------- | ------------------------------ |
| code   | number | 是       | 响应码，1代表成功，0代表失败   |
| msg    | string | 非必须   | 提示信息                       |
| data   | object | 非必须   | 返回的数据，上传文件的访问路径 |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": "https://web-framework.oss-cn-hangzhou.aliyuncs.com/2020-09-02-00-27-0400.jpg"
}
```

### 6.3 获取所有职位

#### 6.3.1 基本信息

请求路径：`/jobs`

请求方式：`GET`

接口描述：获取所有的职位名称

#### 6.3.2 请求参数

无

#### 6.3.3 响应数据

参数格式：`application/json`

参数说明：

| 参数名 | 类型     | 是否必须 | 备注                         |
| ------ | -------- | -------- | ---------------------------- |
| code   | number   | 是       | 响应码，1代表成功，0代表失败 |
| msg    | string   | 非必须   | 提示信息                     |
| data   | object[] | 非必须   | 返回的数据                   |

响应数据样例：

```json
{
    "code": 1,
    "msg": "success",
    "data": [
        "助教",
        "讲师",
        "副教授",
        "教授",
        "副主任",
        "主任",
        "工程师"
    ]
}
```
