# Java Web Medium

`更新时间：2026-6-10`

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

MyBatis-Plus默认提供了很多参数类型为`Wrapper`类型接口

```java
int delete(@Param("ew") Wrapper<T> queryWrapper);
int update(@Param("et") T entity, @Param("ew") Wrapper<T> updateWrapper);
default T selectOne(@Param("ew") Wrapper<T> queryWrapper) {};
default boolean exists(Wrapper<T> queryWrapper) {};
Long selectCount(@Param("ew") Wrapper<T> queryWrapper);
List<T> selectList(@Param("ew") Wrapper<T> queryWrapper);
List<Map<String, Object>> selectMaps(@Param("ew") Wrapper<T> queryWrapper);
List<Object> selectObjs(@Param("ew") Wrapper<T> queryWrapper);
<P extends IPage<T>> P selectPage(P page, @Param("ew") Wrapper<T> queryWrapper);
<P extends IPage<Map<String, Object>>> P selectMapsPage(P page, @Param("ew") Wrapper<T> queryWrapper);
```

这里的Wrapper就是条件构造器，用于构造复杂的SQL语句，Wrapper类有很多的子类，例如UpdateWrapper、QueryWrapper等等，通过方法的形式实现了SQL中所有的查询语句，例如eq()等于，like()匹配，between()范围等

**示例**

我们用几个案例来展示条件构造器如何使用

1.　查询出名字中带o，存款大于等于1000元的人的id、username、info以及balance字段

`mysql`

```mysql
# MySQL
SELECT
	id, username, info, balance
FROM
	user
WHERE
	balance >= 1000
	AND username LIKE '%o%'
```

`mybatis-plus`

```java
@Test
void testQueryByWrapper() {
    // 创建查询条件
    QueryWrapper<User> wrapper = new QueryWrapper<User>()
            .select("id", "username", "info", "balance")
            .like("username", "o")
            .ge("balance", 1000);
    // 使用查询条件查询
    List<User> users = userMapper.selectList(wrapper);
    users.forEach(System.out::println);
}
```

2. 更新用户名为jack的用户的余额为2000

`mysql`

```mysql
#MySQL
UPDATE
	user
SET 
	balance = 2000
WHERE
	username = 'jack';
```

`mybatis-plus`

```java
@Test
void testUpdateByWrapper() {
    // 创建更新条件
    UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
            .set("balance", 2000)
            .eq("username", "jack");
    // 使用更新条件更新
    userMapper.update(null, wrapper);
}
```

需要注意，在更新Wrapper中，set()方法与update()方法中的实体参数并不冲突，如果只指定了某一个，则会使用指定的值，如果两者都指定，Wrapper中的方法优先级更高，如下

```java
// 创建更新条件
UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
    .set("balance", 2000)
    .eq("username", "jack");
// 构建更新实体
User user = User.builder()
    .balance(3000)
    .build();
// 使用更新条件更新
userMapper.update(user, wrapper);
```

这种情况下，我们同时指定了更新实体user，查询Wrapper中的set方法，最后更新的结果仍为set方法中的值

**setSql()**

如果更新逻辑中要求对数据进行基于自身的变动，就必须使用UpdateWrapper，实体类无法实现

例如需要更新1、2、4号用户的余额，在其基础上减去200

`mysql`

```mysql
UPDATE
	user
SET
	balance = balance - 200
WHERE
	id IN (1, 2, 4);
```

`mybatis-plus`

```java
// 创建更新条件
UpdateWrapper<User> wrapper = new UpdateWrapper<User>()
        .setSql("balance = balance - 200")
        .in("id", 1L, 2L, 4L);
// 使用更新条件更新
userMapper.update(null, wrapper);
```

#### LambdaWrapper

在上文构造Wrapper的时候，有关字段名的编写都是基于字符串的形式，实际上这是一种非常不安全的行为，如果字符串误写，漏写，或者是存在两个相似的字段名，就及其容易导致程序报错。因此MyBatis-Plus提供了基于Lambda表达式的LambdaWrapper，使用反射获取实体类get方法中的属性对应的@TableField注解值来编写SQL语句

如下

```java
@Test
void testQueryByLambdaWrapper() {
    // 创建查询条件
    LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
            .select(User::getId, User::getUsername, User::getInfo, User::getBalance)
            .like(User::getUsername, "o")
            .ge(User::getBalance, 1000);
    // 使用查询条件查询
    List<User> users = userMapper.selectList(wrapper);
}
```

我们在实体类的username字段中添加@TableField注解

```java
/**
 * 用户名
 */
@TableField("`username`")
private String username;
```

指定方法，并观察构建的查询语句，证实了我们的说法是正确的

> ![](javaweb2/71.png)

### 自定义SQL

在MyBatis-Plus的使用过程中，我们不难发现其对于一些查询语句的构建存在一定的困难，例如我们提到的数据进行基于自身的变动，或者查询时需要使用聚合函数，起别名等等，这些地方难免会需要我们手动构建一些SQL语句，但由于Wrapper的定义是在Service中，一般来说Service中不允许出现SQL语句，这会违反我们的开发规范，因此我们可以利用Wrapper的优点，使用Wrapper构建WHERE字段，然后将Wrapper传递到Mapper层，再在Mapper中为Wrapper拼接剩余部分，这就是自定义SQL

1. 在Service中基于Wrapper构建WHERE字段

```java
List<Long> ids = List.of(1L, 2L, 4L);
Integer amount = 200;

// 构建查询条件
LambdaUpdateWrapper<User> wrapper = new LambdaUpdateWrapper<User>()
    .in(User::getId, ids);
// 自定义SQL方法调用
userMapper.updateBalanceByIds(wrapper, amount);
```

2. 在Mapper中定义自定义SQL方法

```java
Integer updateBalanceByIds(@Param(Constants.WRAPPER) LambdaUpdateWrapper<User> wrapper, @Param("amount") Integer amount);
```

3. 在Mapper或者Mapper.xml中定义SQL语句，使用`ew.customSqlSegment`获取mybatis-plus自动构建的sql语句

```xml
<update id="updateBalanceByIds">
    UPDATE user SET balance = balance - #{amount} ${ew.customSqlSegment}
</update>
```

### IService

如同BaseMapper，MyBatis-Plus其实也提供了现有的Service层解决方案IService，并且为了避免开发人员需要额外实现所有的接口，IService也提供了一个实现类IServiceImpl，只需要我们的Service继承IService，ServiceImpl继承IServiceImpl，即可直接在Controller中调用IService提供的所有方法

`Service`

```java
package com.itheima.mp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.mp.domain.po.User;

public interface IUserService extends IService<User> {
}
```

`ServiceImpl`

```java
package com.itheima.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;

@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
}
```

然后直接在测试类中调用查询方法，查询所有的用户信息

```java
package com.itheima.mp.mapper;

import com.itheima.mp.domain.po.User;
import com.itheima.mp.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Wrapper;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private IUserService userService;

    @Test
    void testSelect() {
        List<User> list = userService.list();
        list.forEach(System.out::println);
    }
}
```

> ![](javaweb2/72.png)

可以看到，我们根本没在Service层编写任何代码，仅使用IService就能够查询出所有的用户信息

IService提供的方法包括但不限于save、saveBatch、saveOrUpdate、removeById、updateById、update、list、listById、getById、count、page等等，覆盖可能需要的大量方法

#### 基础业务接口

基于Restful风格实现下列接口

| 编号 | 接口               | 请求方式 | 请求路径                      | 请求参数         | 返回值     |
| ---- | ------------------ | -------- | ----------------------------- | ---------------- | ---------- |
| 1    | 新增用户           | POST     | /users                        | 用户表单实体     | 无         |
| 2    | 删除用户           | DELETE   | /users/{id}                   | 用户id           | 无         |
| 3    | 根据id查询用户     | GET      | /users/{id}                   | 用户id           | 用户VO     |
| 4    | 根据id批量删除用户 | GET      | /users                        | 用户id集合       | 用户VO集合 |
| 5    | 根据id扣减余额     | PUT      | /users/{id}/deduction/{money} | 用户id，扣减金额 | 无         |

```java
package com.itheima.mp.controller;

import com.itheima.mp.domain.dto.UserFormDTO;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.domain.vo.UserVO;
import com.itheima.mp.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@Api(tags = "用户管理接口")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    /**
     * 新增用户接口
     * @param userFormDTO 用户信息
     */
    @PostMapping
    @ApiOperation("新增用户接口")
    public void save(@RequestBody UserFormDTO userFormDTO) {
        log.info("保存用户信息：{}", userFormDTO);
        User user = new User();
        BeanUtils.copyProperties(userFormDTO, user);
        userService.save(user);
    }

    /**
     * 删除用户接口
     * @param id 用户id
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除用户接口")
    public void delete(@PathVariable Long id) {
        log.info("删除用户信息：{}", id);
        userService.removeById(id);
    }

    /**
     * 根据id查询用户接口
     * @param id 用户id
     * @return 用户信息
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询用户接口")
    public UserVO getById(@PathVariable Long id) {
        log.info("查询用户信息：{}", id);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userService.getById(id), userVO);
        return userVO;
    }

    /**
     * id批量查询用户接口
     * @param ids 用户id列表
     * @return 用户信息列表
     */
    @GetMapping
    @ApiOperation("查询id批量查询用户接口")
    public List<UserVO> listByIds(@RequestParam List<Long> ids) {
        log.info("查询id批量查询用户信息：{}", ids);
        return userService.listByIds(ids).stream().map(user -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            return userVO;
        }).toList();
    }
}
```

对于已经熟练掌握CRUD的我们来说，这些内容简直易如反掌

#### 复杂业务接口

上文中第五个接口不能直接调用现有的IService方法实现，因此仍需我们自己定义方法

```java
package com.itheima.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 根据用户id扣减余额
     * @param id   用户id
     * @param money 扣减金额
     */
    @Override
    public void deductById(Long id, Integer money) {
        // 获取用户信息
        User user = this.getById(id);
        // 判断账户是否存在
        if (user == null)
            throw new RuntimeException("用户不存在");
        // 判断账户余额是否充足
        if (user.getBalance() >= money)
            user.setBalance(user.getBalance() - money);
        else
            throw new RuntimeException("账户余额不足");
        // 更新用户信息
        this.updateById(user);
    }
}
```

当然，也可以选择使用自定义Mapper

Service

```java
package com.itheima.mp.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.mp.domain.po.User;
import com.itheima.mp.mapper.UserMapper;
import com.itheima.mp.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class IUserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    /**
     * 根据用户id扣减余额
     * @param id   用户id
     * @param money 扣减金额
     */
    @Override
    public void deductById(Long id, Integer money) {
        // 获取用户信息
        User user = this.getById(id);
        // 判断账户是否存在
        if (user == null)
            throw new RuntimeException("用户不存在");
        // 判断账户余额是否充足
        if (user.getBalance() >= money)
            user.setBalance(user.getBalance() - money);
        else
            throw new RuntimeException("账户余额不足");
        // 更新用户信息
        baseMapper.deductById(id, money);
    }
}
```

Mapper

```java
package com.itheima.mp.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.itheima.mp.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据id扣减余额
     * @param id 用户id
     * @param money 扣减金额
     */
    @Update("update user set balance = balance - #{money} where id = #{id}")
    void deductById(Long id, Integer money);
}
```

### IService Lambda

IService除了提供基本的查询方法外，也提供了基于Lambda表达式的查询方法，如LambdaQueryChainWrapper和LambdaUpdateChainWrapper

现在我们需要完成一个接口，根据复杂条件查询用户，条件为name、status、minBalance以及maxBalance，使用LambdaQueryChainWrapper完成

```java
/**
 * 复杂查询
 * @param userQuery 查询条件
 * @return 用户信息列表
 */
@Override
public List<UserVO> complexQuery(UserQuery userQuery) {
    return lambdaQuery()
            .like(userQuery.getName() != null, User::getUsername, userQuery.getName())
            .eq(userQuery.getStatus() != null, User::getStatus, userQuery.getStatus())
            .ge(userQuery.getMinBalance() != null, User::getBalance, userQuery.getMinBalance())
            .le(userQuery.getMaxBalance() != null, User::getBalance, userQuery.getMaxBalance())
            .list()
            .stream()
            .map(user -> {
                UserVO userVO = new UserVO();
                BeanUtils.copyProperties(user, userVO);
                return userVO;
            })
            .toList();
}
```

简单解释一下lambdaQuery()的使用，lambdaQuery()支持链式编程，使用sql关键字作为方法名，如like()、eq()等，当需要构造查询条件时，使用对应实体类的getter获取对应字段名，然后传入需要比较的值。每个关键字方法都存在一个重写方法，重写方法新增了一个参数condition用于在构造动态sql，避免因为参数值为null而导致查询时报错。在条件构造完成后，需要使用one()、list()、count()等执行方法才能真正进行执行，执行完成后通过StreamAPI转换为List\<UserVO\>返回前端

> ![](javaweb2/73.png)

lambdaUpdate()用法与lambdaQuery()类似，set()方法也有一个重写方法的第一个参数为condition

```java
// 更新用户信息
lambdaUpdate()
        .eq(User::getId, id)
        .set(User::getBalance, user.getBalance() - money)
        .set(Objects.equals(user.getBalance(), 0), User::getStatus, 2)
        .update();
```

如上，只有当用户余额为0的时候，才会将用户状态进行变更，相当于XML中的\<if test="balance == 0"\>status = #{status}\</if\>

### IService批处理

IService拥有自己的批处理逻辑，比传统for循环直接插入的性能要更好。我们使用一个例子来展示

将五万条数据插入表中

1. 直接插入

```java
@Test
void testSave() {
    Long begin = System.currentTimeMillis();
    for (int i = 0; i < 50000; i++) {
        save(i);
    }
    Long end = System.currentTimeMillis();
    System.out.println("耗时：" + (end - begin));
}
```

> ![](javaweb2/74.png)



2. 批处理插入

因为saveBatch()的容量有限，因此需要将五万条数据拆分为多次插入，这里我们规定每5000条插入一次，共计插入10次

```java
@Test
void testSaveBatch() {
    Long begin = System.currentTimeMillis();
    List<User> userList = new ArrayList<>(5000);
    for (int i = 1; i <= 50000; i++) {
        userList.add(userBuilder(i));
        if (i % 5000 == 0) {
            userService.saveBatch(userList);
            userList.clear();
        }
    }
    Long end = System.currentTimeMillis();
    System.out.println("耗时：" + (end - begin));
}
```

> ![](javaweb2/75.png)

可以看到，仅用了9577毫秒就完成了所有数据的插入，相较于直接插入的49265毫秒，速度提升了5.1倍

同时，如果在jdbc的url中使用了rewriteBatchedStatements=true，性能可以进一步提升

```yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/mp?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&serverTimezone=Asia/Shanghai&rewriteBatchedStatements=true
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: root
```

> ![](javaweb2/76.png)









## 微服务

微服务是一种软件架构风格，它是以专注于单一职责的很多小项目为基础，组合出复杂的大型应用

微服务架构同样会遇到很多问题，包括但不限于服务拆分、远程调用、服务治理、请求路由、身份认证、配置管理、服务保护、分布式事务、异步通信、消息可靠性、延迟消息、分布式搜索、倒排索引、数据聚合等等

学习微服务，就是要解决微服务架构所面临的各种问题
