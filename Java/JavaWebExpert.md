# Java Web Medium

`更新时间：2026-6-9`

注释解释：

- `<>`必填项，必须在当前位置填写相应数据

- `{}`必选项，必须在当前位置选择一个给出的选项

- `[]`可选项，可以选择填写或忽略

*注：该笔记内的可选项和参数均不完整，如有需要，请查询相关手册*

---

## MyBatis-Plus

MyBatis-Plus是一个 MyBatis的增强工具，在 MyBatis 的基础上只做增强不做改变，为简化开发、提高效率而生。MyBatis-Plus只做增强不做改变，引入它不会对现有工程产生影响，如丝般顺滑。启动即会自动注入基本 CURD，性能基本无损耗，直接面向对象操作。内置通用 Mapper、通用 Service，仅仅通过少量配置即可实现单表大部分 CRUD 操作，更有强大的条件构造器，满足各类使用需求。支持 MySQL、MariaDB、Oracle、DB2、H2、HSQL、SQLite、Postgre、SQLServer 等多种数据库

### 快速入门

我们准备了一个传统MyBatis项目，项目中已经编写好了基础CRUD查询逻辑

```java
package com.itheima.mp.mapper;

import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper{

    void saveUser(User user);

    void deleteUser(Long id);

    void updateUser(User user);

    User queryUserById(@Param("id") Long id);

    List<User> queryUserByIds(@Param("ids") List<Long> ids);
}
```

1. 引入依赖

```xml
<!--        mybatis-plus-->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>3.5.3.1</version>
        </dependency>
```

2. 将Mapper继承BaseMapper\<T>

```java
package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    void saveUser(User user);

    void deleteUser(Long id);

    void updateUser(User user);

    User queryUserById(@Param("id") Long id);

    List<User> queryUserByIds(@Param("ids") List<Long> ids);
}
```

3. 删除原Mapper中的所有方法，让其全部继承自父类

```java
package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
}
```

4. 使用测试类进行测试

```java
@Test
void testInsert() {
    User user = new User();
    user.setId(5L);
    user.setUsername("Lucy");
    user.setPassword("123");
    user.setPhone("18688990011");
    user.setBalance(200);
    user.setInfo("{\"age\": 24, \"intro\": \"英文老师\", \"gender\": \"female\"}");
    user.setCreateTime(LocalDateTime.now());
    user.setUpdateTime(LocalDateTime.now());
    userMapper.insert(user);
}
```

> ![](javaweb2/70.png)

### 常见注解

MyBatis-Plus通过扫描实体类，并基于反射获取实体类信息作为数据库表信息，然后在`BaseMapper`中实现针对某个实体类的CRUD操作。但是MyBatis-Plus的解析是存在一定规范的

- 类名驼峰转换为下划线作为表名
- 实体类中属性名为id的属性作为主键
- 其他属性名驼峰转下划线作为字段名
- 不符合规范的类或者属性需要使用注解来显式声明

| 注解        | 说明             |
| ----------- | ---------------- |
| @TableName  | 指定表名         |
| @TableId    | 指定主键         |
| @TableField | 指定其他普通字段 |

**示例**

```java
@TableName("stu_users")
public class User {
    
    @TableId("stu_id")
    private Long id;
    
    @TableField("stu_age")
    private Integer age;
}
```

@TableId字段相对比较特殊，可以通过`type`属性来设置主键类型，使用IdType枚举类的值来表示

| 枚举值           | 说明                                                         |
| ---------------- | ------------------------------------------------------------ |
| IdType.AUTO      | 数据库自增长                                                 |
| IdType.INPUT     | 通过setter自行填入                                           |
| IdType.ASSIGN_ID | 通过接口IdentifierGenerator的nextId方法来生成id，默认实现类为雪花算法。未设置@TableId(IdType.AUTO)，且未传递id值的时候，默认会通过IdentifierGenerator生成一个id |

当属性名为is开头的字符串，MyBatis-Plus在转换时会去掉is前缀，例如属性名isMarried，转换后的字段名为married，导致字段名与数据库不一致，因此也需要使用@TableField注解；当属性名为SQL关键字时，也需要使用@TableField注解添加转义符，如@TableField("\`order\`")避免SQL识别为ORDER关键字；当数据库不存在对应字段时，需要使用@TableField(exist = false)来声明，否则MyBatis-Plus会尝试在数据库中寻找该字段，但找不到而导致报错

### 常见配置

MyBatis-Plus的配置项继承了MyBatis原生配置和一些自己特有的配置

```yml
mybatis-plus:
  # 配置实体类包
  type-aliases-package: com.itheima.mp.domain.po
  # XML文件地址
  mapper-locations: classpath*:mapper/**/*.xml
  configuration:
    # 是否开启驼峰命名
    map-underscore-to-camel-case: true
    # 是否启用二级缓存
    cache-enabled: false
  global-config:
    db-config:
      # 默认主键策略
      id-type: assign_id
      # 默认更新策略
      update-strategy: not_null
```

### 条件构造器





## 微服务

微服务是一种软件架构风格，它是以专注于单一职责的很多小项目为基础，组合出复杂的大型应用

微服务架构同样会遇到很多问题，包括但不限于服务拆分、远程调用、服务治理、请求路由、身份认证、配置管理、服务保护、分布式事务、异步通信、消息可靠性、延迟消息、分布式搜索、倒排索引、数据聚合等等

学习微服务，就是要解决微服务架构所面临的各种问题
