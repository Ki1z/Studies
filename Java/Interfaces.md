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

