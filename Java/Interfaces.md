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

## 5. 数据统计

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

