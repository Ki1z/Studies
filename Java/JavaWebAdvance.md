# Java Web Advance

`更新时间：2026-5-28`

注释解释：

- `<>`必填项，必须在当前位置填写相应数据

- `{}`必选项，必须在当前位置选择一个给出的选项

- `[]`可选项，可以选择填写或忽略

*注：该笔记内的可选项和参数均不完整，如有需要，请查询相关手册*

---

从`Java Web Advance`开始，我们将尝试构建一个真正能够实际上线的大型项目

## 项目介绍

我们将制作一个外卖点餐项目，包含用户和商家两个用户层级，普通用户层我们使用微信小程序作为前端界面，商家层面我们使用桌面浏览器作为前端。

**技术选型**

在该项目中，我们会使用以下的技术栈

用户层：`node.js`、`vue.js`、`ElementUI`、`WeChat`、`Apache echarts`

网关层：`Nginx`

应用层：`Spring Boot`、`Spring MVC`、`Spring Task` 、`Httpclient`、`Spring Cache`、`JWT`、`阿里云OSS2`、`Swagger`、`POI`、`WebSocket`

数据层：`MySQL`、`Redis`、`MyBatis`、`Pagehelper`、`Spring Data Redis`

工具类：`Git`、`Maven`、`Junit`、`Postman`

为了注重于后端开发，我们将直接跳过前端开发，使用现有的前端项目[skyTakeout](./projects/skyTakeout_frontEnd/html/sky/index.html)。

## 环境搭建

为了便于开发，我们也直接导入一个初始工程，从初始工程入手来完善项目

### 后端环境

拿到一个新的项目，我们应该对项目有一个基本的认识

> ![](javaweb2/26.png)

初始项目由四部分构成

| 模块名         | 说明                                               |
| -------------- | -------------------------------------------------- |
| `sky-take-out` | 父工程，统一管理依赖版本，聚合其他子模块           |
| `sky-common`   | 子模块，存放公共类，如工具类、常量类、异常类等等   |
| `sky-pojo`     | 子模块，存放实体类、`VO`、`DTO`等等                |
| `sky-server`   | 子模块，后端服务主模块，存放配置文件，逻辑文件等等 |

**sky-common**

> ![](javaweb2/27.png)

`sky-common`中包含了常量、上下文相关、枚举类、异常、`JSON`处理、配置、结果封装、工具类

**sky-pojo**

> ![](javaweb2/28.png)

`sky-pojo`中存放所有实体类，一般包括

| 类别     | 说明                                               |
| -------- | -------------------------------------------------- |
| `Entity` | 实体，通常和数据库中的表直接对应                   |
| `DTO`    | 数据传输对象，通常用户程序中各层之间传递数据       |
| `VO`     | 视图对象，为前端展示数据提供的对象                 |
| `POJO`   | 普通`Java`对象，只有属性和对应的`getter`和`setter` |

**sky-server**

> ![](javaweb2/29.png)

`sky-server`才是实质上的主目录，`SpringBoot`的配置文件，启动类也位于`sky-server`中

### 数据库搭建

数据库也直接使用已经设计好的`sql`文件，相关设计文档查阅[数据库文档](./skyTakeout_backEnd/database.md)

> ![](javaweb2/30.png)

### 前后端联调测试

在前后端与数据库环境准备完成后，我们进行初始的前后端联调测试，项目中已经完成了基础的登录功能，通过验证登录功能可以确保前后端和数据库项目的正常运行

> ![](javaweb2/31.png)

我们按照后端源代码，推导一下目前的登录逻辑是如何实现的

1. 首先查看`config`中有关添加拦截器的配置

```java
@Override
protected void addInterceptors(InterceptorRegistry registry) {
    log.info("开始注册自定义拦截器...");
    registry.addInterceptor(jwtTokenAdminInterceptor)
            .addPathPatterns("/admin/**")
            .excludePathPatterns("/admin/employee/login");
}
```

定义了一个`jwtTokenAdminInterceptor`拦截器，拦截的路径为`/admin/**`，但是排除了`/admin/employee/login`

2. 查看`JwtTokenAdminInterceptor`的定义

```java
package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getAdminTokenName());

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), token);
            Long empId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("当前员工id：", empId);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}
```

`JwtTokenAdminInterceptor`先从请求头中获取`token`，然后通过工具类`JwtUtil`的`parseJWT()`方法来校验令牌，令牌的密钥通过`jwtProperties`这个`Bean`的`adminSecretKey`属性获取，最后根据结果决定是否放行。

3. 查看`JwtProperties`

```java
package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.jwt")
@Data
public class JwtProperties {

    /**
     * 管理端员工生成jwt令牌相关配置
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
```

`JwtProperties`配置类通过配置文件中的`sky.jwt`来为其属性赋值，我们再去查看`application.yml`文件

```yml
sky:
  jwt:
    # 设置jwt签名加密时使用的秘钥
    admin-secret-key: eiousee
    # 设置jwt过期时间
    admin-ttl: 7200000
    # 设置前端传递过来的令牌名称
    admin-token-name: token
```

4. 查看`Controller`中有关登录的定义

```java
/**
 * 登录
 *
 * @param employeeLoginDTO
 * @return
 */
@PostMapping("/login")
public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
    log.info("员工登录：{}", employeeLoginDTO);

    Employee employee = employeeService.login(employeeLoginDTO);

    //登录成功后，生成jwt令牌
    Map<String, Object> claims = new HashMap<>();
    claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
    String token = JwtUtil.createJWT(
            jwtProperties.getAdminSecretKey(),
            jwtProperties.getAdminTtl(),
            claims);

    EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
            .id(employee.getId())
            .userName(employee.getUsername())
            .name(employee.getName())
            .token(token)
            .build();

    return Result.success(employeeLoginVO);
}
```

在`controller`通过`EmployeeLoginDTO`来接受前端数据

```java
package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "员工登录时传递的数据模型")
public class EmployeeLoginDTO implements Serializable {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

}
```

`EmployeeLoginDTO`是一个标准的`JavaBean`，仅包含属性及其`getter`、`setter`，并且实现了`Serializable`接口

接着调用`EmployeeService`的`login()`方法，在登陆成功后创建一个用户数据`Map`，将用户`id`填入到`Map`中，并调用`JwtUtil`的`createJWT()`方法，调用`jwtProperties`从配置文件中读取`jwt`加密的`key`及其生存周期，最后通过`EmployeeLoginVO`返回到前端

```java
package com.sky.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "员工登录返回的数据格式")
public class EmployeeLoginVO implements Serializable {

    @ApiModelProperty("主键值")
    private Long id;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("jwt令牌")
    private String token;

}
```

`EmployeeLoginVO`也是一个标准的`JavaBean`，并且添加了`@Builder`注解，使其可以使用`builder()`方法来创建示例，而不需要`new`

5. 查看`Service`定义

```java
package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

}
```

`EmployeeServiceImpl`中进行了简单的密码验证，根据不同的验证结果抛出了`AccountNotFoundException`、`PasswordErrorException`、`AccountLockedException`三种异常，这是为了能够让全局异常处理器`GlobalExceptionHandler`精确地捕获，并根据不同的异常类型返回不同的提示信息

6. 查看`Mapper`定义

```java
package com.sky.mapper;

import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

}
```

`EmployeeMapper`中通过`username`在`employee`表中查询用户数据并返回

### Nginx反向代理

在先前的`Vue`工程化的学习中，我们简单认识了`Nginx`服务器，现在我们来了解一下，`Nginx`服务器究竟是如何将前端请求转发到后端的

在浏览器默认的安全策略中，只允许同源通信，并不允许跨域通信。举个例子，假设前端`Nginx`服务器为`https://www.eiousee.com`，而后端`Tomcat`服务器为`https://api.eiousee.com`，那么前端的`axios`请求是无法直接访问后端的，浏览器会直接报错提示被`CROS`同源策略阻止。在浏览器中，一般把一个`URL`拆分为`协议`、`主机`、`端口`、`URI`四个部分，而同源策略要求，前三个部分必须相同，否则视为不属于同一个域。例如`https://www.eiousee.com`与`https://api.eiousee.com`的主机名不同，视为不同域；`https://www.eiousee.com`与`https://www.eiousee.com:8080`端口号不同，视为不同域；`https://www.eiousee.com`与`https://www.eiousee.com/api/`前三个部分相同，视为相同域

为此，`Nginx`提供了反向代理的功能，即将前端发送到后端的请求由`Nginx`进行中转，开发人员在`Nginx`中配置反向代理的服务器，例如设置所有`/api/`的`URI`视为向后端发送的请求，然后由`Nginx`代理转发到后端服务器，并接收后端服务器返回的数据，再返回给前端

#### 配置Nginx反向代理

`Nginx`的反向代理在配置文件`nginx.conf`中进行

```conf
server {
	location /api/ {
		proxy_pass http://localhost:8080/;
	}
}
```

`/api/`表示路由所有`URI`为`/api/`前缀的请求，然后转发请求到`http://localhost:8080/`中，这里需要注意，`/api/`这个前缀不会转发，只作为标识符出现。例如，前端请求为`http://localhost/api/stu`，转发到后端的请求为`http://localhost:8080/stu`

#### 配置负载均衡

`Nginx`的负载均衡同样在`nginx.conf`中进行

```conf
upstream servers {
	server 192.168.100.128:8080;
	server 192.168.100.129:8080
}

server {
	location /api/ {
		proxy_pass http://servers:8080/;
	}
}
```

通过`upstream`关键字声明一个服务器群组，命名为`servers`，然后在群组中定义各个服务器的`ip`地址，在反向代理时使用服务器群组的名称，`Nginx`就会根据负载均衡策略自动选择对应的服务器

| 策略         | 说明                                                  |
| ------------ | ----------------------------------------------------- |
| 轮询         | 默认方式，所有请求顺序地分配给每个服务器              |
| `weight`     | 权重分配，默认为1，权重越高，被分配地客户端请求就越多 |
| `ip_hash`    | 根据`ip`分配，每个访客可以固定访问同一个后端服务器    |
| `least_conn` | 最少连接分配，请求会优先分配给连接数最少的服务器      |
| `url_hash`   | `url`分配，相同的`url`会被分配到同一个服务器          |
| `fair`       | 响应时间分配，服务器响应时间越短，被分配的连接数越多  |

## 项目实战

### 完善登录功能

在目前的初始项目中，登录功能实际上并不够完善，员工表中的密码目前仍然通过明文存储，安全性较低，因此我们将对登录逻辑进行改造，使用`md5`加密算法来加密数据库中的密码字段

`Spring`框架中其实默认提供了`md5`的加密方法，通过`DigestUtils`工具类的`md5DigestAsHex()`，接收的数据类型为`byte[]`，返回值类型为`String`

```java
// TODO 后期需要进行md5加密，然后再进行比对
password = DigestUtils.md5DigestAsHex(password.getBytes());
if (!password.equals(employee.getPassword())) {
    //密码错误
    throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
}
```

这样，登陆加密功能就简单完成了

### Swagger

`Swagger` 是一套用于设计、构建、文档化和测试 `RESTful API` 的开源工具集。`Swagger` 提供了一种标准化的方式来描述 `API` 的结构、请求参数、响应格式等信息，使得前后端开发人员能够更高效地协作。`Swagger` 是由 `SmartBear Software` 提供的一套 `API` 开发工具集，最初是独立的 `API` 规范，现在已成为 `OpenAPI Specification` 的基础

简单来说，`Swagger`可以快捷生成接口文档，并进行在线测试

#### knife4j

`knife4j`是一个基于`Spring`的`Swagger`项目，可以通过`maven`导入到`Spring`项目中，使用`Swagger`的功能

1. 导入依赖

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-spring-boot-starter</artifactId>
</dependency>
```

2. 在配置类中加入`knife4j`的相关配置

```java
/**
 * 通过knife4j生成接口文档
 * @return
 */
@Bean
public Docket docket() {
    ApiInfo apiInfo = new ApiInfoBuilder()
            .title("苍穹外卖项目接口文档")
            .version("2.0")
            .description("苍穹外卖项目接口文档")
            .build();
    Docket docket = new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.sky.controller"))
            .paths(PathSelectors.any())
            .build();
    return docket;
}
```

3. 设置静态资源映射

静态资源映射的为了让`Tomcat`服务器能够正确解析接口文档生成的位置，让我们能够通过`/doc.html`来访问接口文档。如果不设置静态资源映射，访问`/doc.html`时就会认为我们在访问某个`Controller`，从而报错`404`

```java
/**
 * 设置静态资源映射
 * @param registry
 */
protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/doc.html").addResourceLocations("classpath:/META-INF/resources/");
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
}
```

4. 访问后端`api`文档

> ![](javaweb2/32.png)

#### 常用注解

| 注解                | 属性值        | 说明                       |
| ------------------- | ------------- | -------------------------- |
| `@Api`              | `tags`        | 业务类说明                 |
| `@ApiModel`         | `description` | 实体类说明                 |
| `@ApiModelProperty` | `value`       | 属性说明，用于描述属性信息 |
| `@ApiOperation`     | `value`       | 方法说明，用于说明方法用途 |

**示例**

```java
@RestController
@Api(tags = "员工操作接口")
public class EmployeeController {
    
    @PostMapping("/login")
    @ApiOperation("员工登录")
    Result login() {}
}

@Data
@ApiModel(description = "员工登录时传递的数据模型")
public class EmployeeLoginDTO implements Serializable {

    @ApiModelProperty("用户名")
    private String username;
}
```

### 新增员工

在`Java Web Medium`的学习中，我们已经掌握了基础的`CRUD`操作，为了提高效率，从`Java Web Advance`开始，将不会对已经学过的知识点做过多解释，如有不懂得地方，请查阅[JavaWebMedium](./JavaWebMedium.md)的相关内容

这个项目我们不再全套使用一个实体类，而是为不同的功能设计不同用处的特定实例类

`EmployeeDTO`

用于接收前端请求参数

```java
package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(description = "新增员工时传递的数据模型")
public class EmployeeDTO implements Serializable {

    @ApiModelProperty("员工id")
    private Long id;

    @ApiModelProperty("员工登录用户名")
    private String username;

    @ApiModelProperty("员工姓名")
    private String name;

    @ApiModelProperty("员工手机号")
    private String phone;

    @ApiModelProperty("员工性别")
    private String sex;

    @ApiModelProperty("员工身份证号")
    private String idNumber;

}
```

`Employee`

用于封装为数据库对象

```java
package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
```

`Controller`

```java
@PostMapping
@ApiOperation("新增员工")
public Result<String> add(@RequestBody EmployeeDTO employeeDTO) {
    log.info("新增员工，员工数据：{}", employeeDTO);
    employeeService.add(employeeDTO);
    return Result.success();
}
```

`Service`

在数据校验模块，我们为不同的错误类型抛出不同的自定义异常类，所有的自定义异常类都继承自`BaseException`异常类，通过全局异常处理器捕获。所有的报错信息由`MessageConstant`常量类定义。创建人`ID`和修改人`ID`使用`ThreadLocal`封装而成的`BaseContext`上下文工具类设置和获取

```java
@Override
public void add(EmployeeDTO employeeDTO) {
    String username = employeeDTO.getUsername();
    String name = employeeDTO.getName();
    String phone = employeeDTO.getPhone();
    String sex = employeeDTO.getSex();
    String idNumber = employeeDTO.getIdNumber();

    // 数据校验
    // 用户名校验
    if (username == null || username.isEmpty())
        throw new FormValueIsNullException(MessageConstant.USERNAME_IS_NULL);
    else if (username.length() < 4 || username.length() > 32)
        throw new UsernameLengthWrongException(MessageConstant.USERNAME_LENGTH_WRONG);
    Employee employee = employeeMapper.getByUsername(username);
    // 已经注册
    if (employee != null) throw new UsernameExistsException(MessageConstant.USERNAME_EXISTS);

    // 姓名校验
    if (name == null || name.isEmpty())
        throw new FormValueIsNullException(MessageConstant.NAME_IS_NULL);
    else if (name.length() < 2 || name.length() > 32)
        throw new NameLengthWrongException(MessageConstant.NAME_LENGTH_WRONG);

    // 手机号校验
    if (phone == null || phone.isEmpty())
        throw new FormValueIsNullException(MessageConstant.PHONE_NUMBER_IS_NULL);
    else if (!phone.matches("^1[3-9]\\d{9}$"))
        throw new PhoneNumberIllegalException(MessageConstant.PHONE_NUMBER_ILLEGAL);

    // 性别校验
    if (!Objects.equals(sex, "1") && !Objects.equals(sex, "2")) throw new SexNotExistException(MessageConstant.SEX_NOT_EXIST);

    // 身份证号码校验
    if (idNumber == null || idNumber.isEmpty())
        throw new FormValueIsNullException(MessageConstant.ID_NUMBER_IS_NULL);
    else if (!idNumber.matches("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"))
        throw new IdNumberIllegalException(MessageConstant.ID_NUMBER_ILLEGAL);

    employee = new Employee();
    // 复制属性
    BeanUtils.copyProperties(employeeDTO, employee);
    // 设置默认密码123456
    employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
    // 设置账号状态
    employee.setStatus(StatusConstant.ENABLE);
    // 设置创建时间和更新时间
    employee.setCreateTime(LocalDateTime.now());
    employee.setUpdateTime(LocalDateTime.now());

    // 设置创建人ID和修改人ID
    Long id = BaseContext.getCurrentId();
    employee.setCreateUser(id);
    employee.setUpdateUser(id);

    // 插入数据
    employeeMapper.insert(employee);
}
```

`Mapper.xml`

```xml
<insert id="insert">
    INSERT INTO
        employee(username,name,password,phone,sex,id_number,status,create_time,update_time,create_user,update_user)
    VALUES
        (#{username},#{name},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})
</insert>
```

> ![](javaweb2/33.png)

### 员工分页查询

分页查询的功能我们在艾欧希后台管理系统中已经实现过了，简单概述一下，就是使用`MyBatis`的`PageHelper`插件，根据`MyBatis`的拦截器拦截数据库连接请求，然后构建动态`SQL`语句，最后使用`PageHepler`的返回值`Page<>`获取数据即可

`Controller`

```java
@GetMapping("/page")
@ApiOperation("员工分页查询")
public Result<EmployeePageVO> pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
    log.info("员工分页查询：{}", employeePageQueryDTO);
    EmployeePageVO employeePageVO = employeeService.pageQuery(employeePageQueryDTO);
    return employeePageVO == null ? Result.error(MessageConstant.UNKNOWN_ERROR) : Result.success(employeePageVO);
}
```

`Service`

```java
@Override
public EmployeePageVO pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
    try (Page<Employee> page = PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize())) {
        employeeMapper.pageQuery(employeePageQueryDTO);
        return new EmployeePageVO(page.getTotal(), page.getResult());
    }
}
```

`Mapper.xml`

```xml
<!--    分页查询查询员工-->
<select id="pageQuery" resultType="com.sky.entity.Employee">
    SELECT
        id,username,name,phone,sex,id_number,status,create_time,update_time,create_user,update_user
    FROM
        employee
    <if test="name != null and name != ''">
        WHERE name LIKE CONCAT('%',#{name},'%')
    </if>
</select>
```

#### 消息转换器

在上文的代码中，`Employee`实体类被注释了两个注解

```java
//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime createTime;

//@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
private LocalDateTime updateTime;
```

这两行注解是设置`Employee`对象从后台接收数据的数据格式，在不加注解的情况下，日期数据格式实质是一个数组

```json
{
    "id": 1,
    "username": "admin",
    "name": "管理员",
    "password": null,
    "phone": "13812312312",
    "sex": "1",
    "idNumber": "110101199001010047",
    "status": 1,
    "createTime": [
      2022,
      2,
      15,
      15,
      51,
      20
    ],
    "updateTime": [
      2022,
      2,
      17,
      9,
      16,
      20
    ],
    "createUser": 10,
    "updateUser": 1
}
```

所以需要这两个注解来转换数据格式

```json
{
    "id": 1,
    "username": "admin",
    "name": "管理员",
    "password": null,
    "phone": "13812312312",
    "sex": "1",
    "idNumber": "110101199001010047",
    "status": 1,
    "createTime": "2022-02-15 15:51:20",
    "updateTime": "2022-02-17 09:16:20",
    "createUser": 10,
    "updateUser": 1
}
```

但是实际上，`Spring MVC`也可以通过扩展消息转换器来实现对日期类型数据格式的统一管理

首先创建一个配置类，继承`WebMvcConfigurationSupport`

```java
@Configuration
public class WebMvcConfiguration extends WebMvcConfigurationSupport {}
```

然后重写`extendMessageConverters`方法

```java
protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {}
```

定义一个消息转换器类`JacksonObjectMapper`，继承`ObjectMapper`。在`JacksonObjectMapper`中定义一些日期格式的常量

```java
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
//    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
```

定义`JacksonObjectMapper`的构造器，在构造器中调用父类构造器，然后调用自身的方法`configure()`忽略报错信息，再调用`getDeserializationConfig()`定义对象属性不存在时的应对策略

```java
public JacksonObjectMapper() {
    super();
    //收到未知属性时不报异常
    this.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

    //反序列化时，属性不存在的兼容处理
    this.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
}
```

接着实例化一个`SimpleModule`对象，使用`addDeserializer()`、`addSerializer()`方法设置转换映射，最后调用`registerModule()`方法注册功能模块

```java
package com.sky.json;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * 对象映射器:基于jackson将Java对象转为json，或者将json转为Java对象
 * 将JSON解析为Java对象的过程称为 [从JSON反序列化Java对象]
 * 从Java对象生成JSON的过程称为 [序列化Java对象到JSON]
 */
public class JacksonObjectMapper extends ObjectMapper {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
//    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";

    public JacksonObjectMapper() {
        super();
        //收到未知属性时不报异常
        this.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

        //反序列化时，属性不存在的兼容处理
        this.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        SimpleModule simpleModule = new SimpleModule()
                .addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)))
                .addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_TIME_FORMAT)))
                .addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT)))
                .addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern(DEFAULT_TIME_FORMAT)));

        //注册功能模块 例如，可以添加自定义序列化器和反序列化器
        this.registerModule(simpleModule);
    }
}
```

回到`extendMessageConverters()`中，我们先创建一个消息转换器对象，为消息转换器设置对象转换器，即`JacksonObjectMapper`，最后将消息转换器添加到`converters`数组中，注意需要添加到第一个

```java
@Override
protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    log.info("扩展消息转换器...");
    // 创建消息转换器对象
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    // 添加对象转换器
    converter.setObjectMapper(new JacksonObjectMapper());
    // 将消息转换器对象追加到converters中
    converters.add(0, converter);
}
```

### 员工账号状态更改

`Controller`

```java
@PostMapping("/status/{status}")
@ApiOperation("更改员工账号状态")
public Result<String> changeStatus(@PathVariable Integer status, Long id) {
    log.info("更改员工账号状态：{}", id);
    employeeService.changeStatus(status, id);
    return Result.success();
}
```

`Service`

```java
@Override
public void changeStatus(Integer status, Long id) {
    if (!Objects.equals(status, StatusConstant.ENABLE) && !Objects.equals(status, StatusConstant.DISABLE))
        throw new FormValueIsNullException(MessageConstant.STATUS_IS_NOT_DEFINED);
    if (id == null || id <= 0)
        throw new FormValueIsNullException(MessageConstant.EMPLOYEE_NOT_FOUND);
    Integer count = employeeMapper.changeStatus(status, id);
    if (count != 1)
        throw new EmployeeStatusChangeFailedException(MessageConstant.EMPLOYEE_STATUS_CHANGE_FAILED);
}
```

`Mapper`

```java
@Update("update employee set status = #{status} where id = #{id}")
Integer changeStatus(Integer status, Long id);
```

### 根据ID查询员工

`Controller`

```java
@GetMapping("/{id}")
@ApiOperation("根据id查询员工信息")
public Result<Employee> getById(@PathVariable Long id) {
    log.info("根据id查询员工信息：{}", id);
    Employee employee = employeeService.getById(id);
    return Result.success(employee);
}
```

`Service`

```java
@Override
public Employee getById(Long id) {
    if (id == null || id <= 0)
        throw new FormValueIsNullException(MessageConstant.EMPLOYEE_NOT_FOUND);
    Employee employee = employeeMapper.getById(id);
    if (employee == null)
        throw new EmployeeNotFoundException(MessageConstant.EMPLOYEE_NOT_FOUND);
    return employee;
}
```

`Mapper`

```java
@Select("select id, name, username, phone, sex, id_number, status, create_time, update_time, create_user, update_user" +
        " from employee where id = #{id}")
Employee getById(Long id);
```

### 编辑员工

`Controller`

```java
@PutMapping
@ApiOperation("编辑员工信息")
public Result<String> update(@RequestBody EmployeeDTO employeeDTO) {
    log.info("编辑员工信息：{}", employeeDTO);
    employeeService.update(employeeDTO);
    return Result.success();
}
```

`Service`

数据校验逻辑与新增员工类似，但是在检测用户名是否被占用时，不能直接检测数据库中是否存在当前用户，因为查询的结果中包含用户本身，所以我们用用户输入的新用户名来查询用户，然后判断用户`id`是否相同

```java
@Override
public void update(EmployeeDTO employeeDTO) {
    String username = employeeDTO.getUsername();
    String name = employeeDTO.getName();
    String phone = employeeDTO.getPhone();
    String sex = employeeDTO.getSex();
    String idNumber = employeeDTO.getIdNumber();

    // 数据校验
    // 用户名校验
    if (username == null || username.isEmpty())
        throw new FormValueIsNullException(MessageConstant.USERNAME_IS_NULL);
    else if (username.length() < 4 || username.length() > 32)
        throw new UsernameLengthWrongException(MessageConstant.USERNAME_LENGTH_WRONG);
    Employee employee = employeeMapper.getByUsername(username);
    // 用户名已经被占用
    if (employee != null && !Objects.equals(employee.getId(), employeeDTO.getId())) throw new UsernameExistsException(MessageConstant.USERNAME_EXISTS);

    // 姓名校验
    if (name == null || name.isEmpty())
        throw new FormValueIsNullException(MessageConstant.NAME_IS_NULL);
    else if (name.length() < 2 || name.length() > 32)
        throw new NameLengthWrongException(MessageConstant.NAME_LENGTH_WRONG);

    // 手机号校验
    if (phone == null || phone.isEmpty())
        throw new FormValueIsNullException(MessageConstant.PHONE_NUMBER_IS_NULL);
    else if (!phone.matches("^1[3-9]\\d{9}$"))
        throw new PhoneNumberIllegalException(MessageConstant.PHONE_NUMBER_ILLEGAL);

    // 性别校验
    if (!Objects.equals(sex, "0") && !Objects.equals(sex, "1")) throw new SexNotExistException(MessageConstant.SEX_NOT_EXIST);

    // 身份证号码校验
    if (idNumber == null || idNumber.isEmpty())
        throw new FormValueIsNullException(MessageConstant.ID_NUMBER_IS_NULL);
    else if (!idNumber.matches("^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$"))
        throw new IdNumberIllegalException(MessageConstant.ID_NUMBER_ILLEGAL);

    // 封装对象
    employee = new Employee();
    BeanUtils.copyProperties(employeeDTO, employee);
    // 设置更新时间
    employee.setUpdateTime(LocalDateTime.now());
    // 设置更新人ID
    employee.setUpdateUser(BaseContext.getCurrentId());
    // 更新数据
    Integer count = employeeMapper.update(employee);
    if (count != 1) throw new EmplyeeUpdateFailedException(MessageConstant.EMPLOYEE_UPDATE_FAILED);
}
```

`Mapper.xml`

```xml
<!--    更新员工信息-->
<update id="update" parameterType="com.sky.entity.Employee">
    UPDATE employee
    <set>
        <if test="username != null and username != ''">username = #{username},</if>
        <if test="name != null and name != ''">name = #{name},</if>
        <if test="password != null and password != ''">password = #{password},</if>
        <if test="phone != null and phone != ''">phone = #{phone},</if>
        <if test="sex != null">sex = #{sex},</if>
        <if test="idNumber != null and idNumber != ''">id_number = #{idNumber},</if>
        <if test="status != null">status = #{status},</if>
        <if test="createTime != null">create_time = #{createTime},</if>
        <if test="updateTime != null">update_time = #{updateTime},</if>
        <if test="createUser != null and createUser != ''">create_user = #{createUser},</if>
        <if test="updateUser != null and updateUser != ''">update_user = #{updateUser},</if>
    </set>
    WHERE id = #{id}
</update>
```

### 分类相关接口

分类相关接口的逻辑大致与员工操作相当，所以这里仅展示代码

`Controller`

```java
package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.impl.CategoryServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类管理接口")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult<Category>> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分页查询：{}", categoryPageQueryDTO);
        PageResult<Category> pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改分类")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类：{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    /**
     * 修改分类状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("更改分类状态")
    public Result<String> changeStatus(@PathVariable Integer status, Long id) {
        log.info("更改分类状态：{}", status);
        categoryService.changeStatus(status, id);
        return Result.success();
    }

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result<String> add(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类：{}", categoryDTO);
        categoryService.add(categoryDTO);
        return Result.success();
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除分类")
    public Result<String> delete(Long id) {
        log.info("删除分类：{}", id);
        categoryService.delete(id);
        return Result.success();
    }

    /**
     * 根据类型查询
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询分类")
    public Result<List<Category>> list(Integer type) {
        log.info("根据类型查询分类：{}", type);
        List<Category> list = categoryService.list(type);
        return Result.success(list);
    }
}
```

`Service`

```java
package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.*;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        Page<Category> page = PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    /**
     * 修改分类
     * @param categoryDTO
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Long id = categoryDTO.getId();
        String name = categoryDTO.getName();
        Integer sort = categoryDTO.getSort();

        // 参数校验
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.CANNOT_FOUND_CATEGORY);
        if (name == null || name.length() > 20 || name.length() < 2)
            throw new CategoryNameIllegalException(MessageConstant.CATEGORY_NAME_LENGTH_WRONG);
        Category category = categoryMapper.getByName(name);
        if (category != null && !category.getId().equals(id))
            throw new CategoryNameIllegalException(MessageConstant.CATEGORY_NAME_EXISTS);
        if (sort == null || sort < 0)
            throw new CategorySortIllegalException(MessageConstant.CATEGORY_SORT_ILLEGAL);

        category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());

        Integer result = categoryMapper.update(category);
        if (result != 1)
            throw new CategoryUpdateFailedException(MessageConstant.CATEGORY_UPDATE_FAILED);
    }

    /**
     * 修改分类状态
     * @param status
     * @param id
     */
    @Override
    public void changeStatus(Integer status, Long id) {
        if (id == null)
            throw new FormValueIsNullException(MessageConstant.CANNOT_FOUND_CATEGORY);
        if (status == null || !status.equals(0) && !status.equals(1))
            throw new CategoryStatusIllegalException(MessageConstant.CATEGORY_STATUS_ILLEGAL);
        Integer count = categoryMapper.changeStatus(status, id);
        if (count != 1)
            throw new CategoryUpdateFailedException(MessageConstant.CATEGORY_STATUS_UPDATE_FAILED);
    }

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void add(CategoryDTO categoryDTO) {
        String name = categoryDTO.getName();
        Integer sort = categoryDTO.getSort();
        Integer type = categoryDTO.getType();

        // 参数校验
        if (name == null || name.length() > 20 || name.length() < 2)
            throw new CategoryNameIllegalException(MessageConstant.CATEGORY_NAME_LENGTH_WRONG);
        Category category = categoryMapper.getByName(name);
        if (category != null)
            throw new CategoryNameIllegalException(MessageConstant.CATEGORY_NAME_EXISTS);
        if (sort == null || sort < 0)
            throw new CategorySortIllegalException(MessageConstant.CATEGORY_SORT_ILLEGAL);
        if (type == null || !type.equals(1) && !type.equals(2))
            throw new CategoryTypeIllegalException(MessageConstant.CATEGORY_TYPE_ILLEGAL);

        category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());
        category.setCreateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setStatus(StatusConstant.ENABLE);

        Integer result = categoryMapper.add(category);
        if (result != 1)
            throw new CategoryInsertFailedException(MessageConstant.CATEGORY_INSERT_FAILED);
    }

    /**
     * 删除分类
     * @param id
     */
    @Override
    public void delete(Long id) {
       if (id == null)
           throw new FormValueIsNullException(MessageConstant.CANNOT_FOUND_CATEGORY);
       Integer count = categoryMapper.delete(id);
       if (count != 1)
           throw new CategoryDeleteFailedException(MessageConstant.CATEGORY_DELETE_FAILED);
    }

    /**
     * 根据类型查询
     * @param type
     * @return
     */
    @Override
    public List<Category> list(Integer type) {
        if (type == null || !type.equals(1) && !type.equals(2))
            throw new CategoryTypeIllegalException(MessageConstant.CATEGORY_TYPE_ILLEGAL);
        return categoryMapper.list(type);
    }
}
```

`Mapper`

```java
package com.sky.mapper;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    List<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据名称查询
     * @param name
     * @return
     */
    @Select("select id, type, name, sort, status, create_time, update_time, create_user, update_user from category where name = #{name}")
    Category getByName(String name);

    /**
     * 修改分类
     * @param category
     */
    Integer update(Category category);

    /**
     * 修改分类状态
     * @param status
     * @param id
     * @return
     */
    @Update("update category set status = #{status} where id = #{id}")
    Integer changeStatus(Integer status, Long id);

    /**
     * 新增分类
     * @param category
     * @return
     */
    Integer add(Category category);

    /**
     * 删除分类
     * @param id
     * @return
     */
    @Delete("delete from category where id = #{id}")
    Integer delete(Long id);

    /**
     * 根据类型查询
     * @param type
     * @return
     */
    @Select("select id, type, name, sort, status, create_time, update_time, create_user, update_user " +
            "from category where type = #{type} order by sort")
    List<Category> list(Integer type);
}
```

`Mapper.xml`

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.sky.mapper.CategoryMapper">
<!--    新增分类-->
    <insert id="add" parameterType="com.sky.entity.Category">
        INSERT INTO
        category(type, name, sort, status, create_time, update_time, create_user, update_user)
        VALUES
        (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})
    </insert>
    <!--    修改分类-->
    <update id="update" parameterType="com.sky.entity.Category">
        UPDATE category
        <set>
            <if test="name != null">
                name = #{name},
            </if>
            <if test="sort != null">
                sort = #{sort},
            </if>
            <if test="status != null">
                status = #{status},
            </if>
            <if test="updateTime != null">
                update_time = #{updateTime},
            </if>
            <if test="updateUser != null">
                update_user = #{updateUser},
            </if>
        </set>
        WHERE id = #{id}
    </update>
    <!-- 分页查询-->
    <select id="pageQuery" resultType="com.sky.entity.Category">
        SELECT
            id, type, name, sort, status, create_time, update_time, create_user, update_user
        FROM
            category
        <where>
            <if test="type != null">
                type = #{type}
            </if>
            <if test="name != null and name != ''">
                AND name LIKE '%${name}%'
            </if>
        </where>
        ORDER BY sort
    </select>
</mapper>
```

*注：在进行数据校验时，建议使用`Objects.equals()`，因为`Objects.equals()`中进行了空值判断，直接调用属性的`equals()`方法可能导致空指针异常*

```java
public static boolean equals(Object a, Object b) {
    return (a == b) || (a != null && a.equals(b));
}
```

### 公共字段自动填充

在上文的项目实战中，不难发现在执行更新或插入操作时，我们需要为实体类的更新时间、操作员工`id`、创建人、创建时间进行单独的赋值，如下

```java
category.setUpdateTime(LocalDateTime.now());
category.setUpdateUser(BaseContext.getCurrentId());
category.setCreateTime(LocalDateTime.now());
category.setCreateUser(BaseContext.getCurrentId());
```

如果项目中有几十上百个类似的实体类及其字段，很可能导致某一个实体类的信息被漏掉，而且也不便于项目维护。因此我们可以利用`Spring AOP`来切分一个切面，在数据库事务操作之前通过切面类为相关字段赋值

1. 逻辑概述

通过`Spring AOP`定义一个切面类，切入点为所有的`Mapper`操作，但是必须是所有的插入和更新操作，这一步可以由方法名约束，但我们的方法命名比较随意，所以需要通过自定义注解来约束。在自定义注解中，我们再使用枚举类来区分更新和插入操作，因为通知中需要对更新和插入执行不同的字段填充，更新操作不能填充创建人和创建时间字段

2. 定义`AutoFill`注解和枚举类`OperationType`

`AutoFill`

```java
package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    OperationType value();
}
```

`OperationType`

```java
package com.sky.enumeration;

/**
 * 数据库操作类型
 */
public enum OperationType {

    /**
     * 更新操作
     */
    UPDATE,

    /**
     * 插入操作
     */
    INSERT

}
```

2. 定义切面类`FieldAutoFillAspect`

```java
package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class FieldAutoFillAspect {

}
```

3. 定义切入点，需要切入所有带有`AutoFill`注解的`Mapper`方法

```java
@Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
public void autoFill() {}
```

4. 定义通知方法，通知类型为前置通知，必须在`Mapper`方法执行之前完成字段填充，否则会数据插入数据库时会造成数据缺失

```java
@Before("autoFill()")
public void autoFill(JoinPoint joinPoint) {}
```

5. 定义自动填充逻辑

首先截取到传递给数据库的实例，然后获取当前操作用户和系统时间，从方法的注解中获取操作类型，最后根据不同的操作类型，通过`Java`反射为实例赋值。赋值的逻辑则是获取实例的`Class`对象，通过`Class`对象获取其`setter`方法，然后利用`setter`方法赋值，而不是通过`Java`直接修改其访问权限并赋值，保证逻辑的安全性

```java
@Before("autoFill()")
public void autoFill(JoinPoint joinPoint) {
    log.info("开始进行数据填充");
    // 获取更新或新增的实例
    Object[] args = joinPoint.getArgs();
    if (args == null || args.length == 0) {
        return;
    }
    Object object = args[0];
    // 获取当前时间与操作用户
    LocalDateTime now = LocalDateTime.now();
    Long currentId = BaseContext.getCurrentId();
    // 获取操作类型
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);

    if (annotation == null) {
        return;
    }
    // 根据操作类型执行不同的操作逻辑
    if (annotation.value() == OperationType.INSERT) {
        setFieldValue(object, AutoFillConstant.SET_CREATE_TIME, now);
        setFieldValue(object, AutoFillConstant.SET_UPDATE_TIME, now);
        setFieldValue(object, AutoFillConstant.SET_CREATE_USER, currentId);
        setFieldValue(object, AutoFillConstant.SET_UPDATE_USER, currentId);
    } else if (annotation.value() == OperationType.UPDATE) {
        setFieldValue(object, AutoFillConstant.SET_UPDATE_TIME, now);
        setFieldValue(object, AutoFillConstant.SET_UPDATE_USER, currentId);
    }
}

private void setFieldValue(Object object, String fieldName, Object param) {
    try {
        // 获取object的Class对象
        Class<?> clazz = object.getClass();
        // 获取object的fieldName字段的setter方法
        Method setter = clazz.getDeclaredMethod(fieldName, param.getClass());
        // 调用setter方法，为字段赋值
        setter.invoke(object, param);
    } catch (Exception e) {
        e.printStackTrace();
    }
}
```

这里的`fieldName`参数使用了常量类`AutoFillConstant`，以保证所有的相关字段名称是相同的，避免拼写错误。

```java
package com.sky.constant;

/**
 * 公共字段自动填充相关常量
 */
public class AutoFillConstant {
    /**
     * 实体类中的方法名称
     */
    public static final String SET_CREATE_TIME = "setCreateTime";
    public static final String SET_UPDATE_TIME = "setUpdateTime";
    public static final String SET_CREATE_USER = "setCreateUser";
    public static final String SET_UPDATE_USER = "setUpdateUser";
}
```

### Mybatis参数解析

在编写`Mybatis`的`XML`映射文件时，我们通常使用`#{}`来表示一个实例属性，例如

```xml
<insert id="add" parameterType="com.sky.entity.Dish">
    INSERT INTO
        dish(name, category_id, price, image, description, status, create_time, update_time, create_user, update_user)
    VALUES (
            #{name},
            #{categoryId},
            #{price},
            #{image},
            #{description},
            #{status},
            #{createTime},
            #{updateTime},
            #{createUser},
            #{updateUser}
        )
</insert>
```

在实体类`Category`中只要包含这些属性，就能够直接访问

```java
package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 菜品
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //菜品名称
    private String name;

    //菜品分类id
    private Long categoryId;

    //菜品价格
    private BigDecimal price;

    //图片
    private String image;

    //描述信息
    private String description;

    //0 停售 1 起售
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Long createUser;

    private Long updateUser;

}
```

但其实有一个问题，当`Mapper`中使用了`@Param`注解的时候，`XML`文件中必须使用`@Param`指定的参数名来访问属性，否则会报错

```java
org.apache.ibatis.binding.BindingException: Parameter 'name' not found. Available parameters are [dish, param1]
```

#### 单参数

当参数列表只有一个参数，并且没有使用`@Param`注解时，`Mybatis`会将这个参数作为根参数，`XML`中编写的如`#{name}`这样的表达式都是通过访问对象的`getter`方法获取的，相当于`dish.getName()`。也就是说，即使定义了一个没有属性的实体，但是提供了一个假的`getter`

```java
public class fakeEntity {
    public String getName() {
        return "fake";
    }
}
```

你仍然可以在`XML`文件中通过`#{name}`来得到字符串`"fake"`。

如果是基本数据类型，如`int`、`Integer`、`String`等等，`XML`中无论定义怎样的`#{aaa}`、`#{bbb}`，都可以访问到对应数据，因为这里只需要一个参数，不需要考虑参数名。

#### 多参数

而当参数列表中使用`@Param`注解，或者有多个参数时，`Mybatis`会自动生成一个参数`Map`，这个`Map`记录了参数名与参数值之间的对应关系

```java
{
    "Dish" -> Dish对象;
    "param1" -> Dish对象;
}
```

如果使用了`@Param`注解，`Map`中会自动添加一个参数名与对象的映射，同时添加一个`param1`到对象的映射。如果是多个参数，也会添加对应的映射

```java
{
    "Dish" -> Dish对象;
    "Category" -> Category对象;
    "param1" -> Dish对象;
    "param2" -> Category对象;
}
```

此时如果在`XML`文件中使用`#{name}`就会自动在`Map`中寻找对应的映射，如果没有这个映射，就会导致报错

```log
org.apache.ibatis.binding.BindingException: Parameter 'name' not found. Available parameters are [dish, param1]
```

因此如果想要访问对象属性，则必须使用对象的参数名称来访问，如`dish.name`或者`param1.name`。`param1`和`param2`是`Mybatis`为每个参数设置的默认名称，作为兜底策略，如果不指定`@Param`，开发人员就必须使用`paramX`来访问对应的对象，如果指定了`@Param`，这些默认的名称也并不会被删除。

上文提到，在单参数情况中，如果是基本数据类型，`Mybatis`会忽略参数名，因为只有一个参数可用。但如果同时存在多个基本参数类型

```java
@Update("update category set status = #{status} where id = #{id}")
Integer changeStatus(Integer status, Long id);
```

理论上来说，`status`和`id`的参数名不会被记录，默认是`param1`和`param2`，但查询语句`"update category set status = #{status} where id = #{id}"`仍然可以生效。这是因为`SpringBoot2.x+`版本中，默认使用了`-parameters`参数，让编译器在进行编译时能够保留参数名

```java
/**
 * 测试
 * @return
 */
@Select("select concat(#{c},#{b},#{a})")
String test(String a, String b, String c);
```

> ![](javaweb2/34.png)

而去除`-parameters`后，参数名不作保留，就会导致报错

```log
org.apache.ibatis.binding.BindingException: Parameter 'c' not found. Available parameters are [arg2, arg1, arg0, param3, param1, param2]
```

#### 源码追踪

你有没有考虑过，默认的`[arg2, arg1, arg0]`从何而来？

我们来查看`Mybatis`的源码，位于`org.apache.ibatis.reflection.ParamNameResolver`

```java
public static final String GENERIC_NAME_PREFIX = "param";
private final SortedMap<Integer, String> names;
```

可以看到，在`ParamNameResolver`中定义了两个关键属性，参数名前缀和参数名称列表

```java
public ParamNameResolver(Configuration config, Method method) {
  this.useActualParamName = config.isUseActualParamName();
  final Class<?>[] paramTypes = method.getParameterTypes();
  final Annotation[][] paramAnnotations = method.getParameterAnnotations();
  final SortedMap<Integer, String> map = new TreeMap<>();
  int paramCount = paramAnnotations.length;
  // get names from @Param annotations
  for (int paramIndex = 0; paramIndex < paramCount; paramIndex++) {
    if (isSpecialParameter(paramTypes[paramIndex])) {
      // skip special parameters
      continue;
    }
    String name = null;
    for (Annotation annotation : paramAnnotations[paramIndex]) {
      if (annotation instanceof Param) {
        hasParamAnnotation = true;
        name = ((Param) annotation).value();
        break;
      }
    }
    if (name == null) {
      // @Param was not specified.
      if (useActualParamName) {
        name = getActualParamName(method, paramIndex);
      }
      if (name == null) {
        // use the parameter index as the name ("0", "1", ...)
        // gcode issue #71
        name = String.valueOf(map.size());
      }
    }
    map.put(paramIndex, name);
  }
  names = Collections.unmodifiableSortedMap(map);
}
```

在构造器中，首先获取参数中的`@Param`注解，接着调用`getActualParamName()`来获取参数的参数名称，最后使用`String.valueOf(map.size())`来作为兜底参数名。

`arg0`、`arg1`来源于`Java`反射获取的默认名称，如果在编译时不使用`-parameters`参数，虽然不会保留参数名称，但是`name = getActualParamName(method, paramIndex);`可以获得一个名称，这个名称便源自于`Java`反射中`Executable`类的`synthesizeAllParams()`方法

我们先来看`getActualParamName()`方法

```java
private String getActualParamName(Method method, int paramIndex) {
  return ParamNameUtil.getParamNames(method).get(paramIndex);
}
```

追踪到`ParamNameUtil.getParamNames()`

```java
private static List<String> getParameterNames(Executable executable) {
  return Arrays.stream(executable.getParameters()).map(Parameter::getName).collect(Collectors.toList());
}
```

这里使用了`Java`反射`API`中的`executable.getParameters()`方法

```java
public Parameter[] getParameters() {
    // TODO: This may eventually need to be guarded by security
    // mechanisms similar to those in Field, Method, etc.
    //
    // Need to copy the cached array to prevent users from messing
    // with it.  Since parameters are immutable, we can
    // shallow-copy.
    return parameterData().parameters.clone();
}
```

查看`parameterData()`

```java
private ParameterData parameterData() {
    ParameterData parameterData = this.parameterData;
    if (parameterData != null) {
        return parameterData;
    }

    Parameter[] tmp;
    // Go to the JVM to get them
    try {
        tmp = getParameters0();
    } catch (IllegalArgumentException e) {
        // Rethrow ClassFormatErrors
        throw new MalformedParametersException("Invalid constant pool index");
    }

    // If we get back nothing, then synthesize parameters
    if (tmp == null) {
        tmp = synthesizeAllParams();
        parameterData = new ParameterData(tmp, false);
    } else {
        verifyParameters(tmp);
        parameterData = new ParameterData(tmp, true);
    }
    return this.parameterData = parameterData;
}
```

首先调用`tmp = getParameters0()`从`class`文件中读取所有的参数，由于编译时没有使用`-parameters`，因此`tmp`为`null`，执行`tmp = synthesizeAllParams();`

```java
private Parameter[] synthesizeAllParams() {
    final int realparams = getParameterCount();
    final Parameter[] out = new Parameter[realparams];
    for (int i = 0; i < realparams; i++)
        // TODO: is there a way to synthetically derive the
        // modifiers?  Probably not in the general case, since
        // we'd have no way of knowing about them, but there
        // may be specific cases.
        out[i] = new Parameter("arg" + i, 0, this, i);
    return out;
}
```

在这里，参数才真正被命名为`"arg" + i`

*注：`arg0`的来源常被误解为`Parameter`实体类的`getName()`方法*

### 菜品相关接口

菜品相关接口中，主要的难点就是新增和修改菜品中涉及到的多表操作，以及文件上传操作，这里着重概述一些容易踩坑的知识点

**分页查询的每页记录数量不足**

如果在`Mapper`定义的分页查询`SQL`中使用了`JOIN`关键字连接查询`flavor`表中的内容，就会导致第一分页中查询数量少于指定数量，其根本原因在于，使用`JOIN`关键字后，如果一个菜品在`flavor`表中有多条数据，`JOIN`会为每个口味生成一条数据，如下

```mysql
SELECT
    d.id AS id,
    d.name AS name,
    df.value AS flavor
FROM dish AS d
    LEFT JOIN dish_flavor AS df ON d.id = df.dish_id
ORDER BY
    d.id
LIMIT
    0, 10;
```

> ![](javaweb2/36.png)

而`PageHelper`的分页本质就是使用`LIMIT`关键字，`PageHelper`并不知道前`10`行数据中有多行数据属于同一个实体，因此会造成实际显示的查询数量少于指定的每页数量

最直接的解决方法便是在分页查询中不使用`JOIN`关键字查询口味，而是分为两次查询，第一次使用分页查询在`dish`表中查询对应的基本信息，第二次查询不使用分页查询而是使用第一次查询获取的所有`dish.id`，在`dish_flavor`表中查询所有的口味数据，最后将两个记录组合在一起，再返回给前端

**主键返回失效**

`Mybatis`的主键返回有两种常用的方式，一种是使用`@Options`注解，另一种是在`XML`文件中使用对应的属性，但是这两种方式不能混用。假设你在`Mapper`定义了

```java
/**
 * 插入数据
 * @param test
 */
@Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "test.id")
void insertTest(@Param("test") Test test);
```

这里的`@options`是无法在`XML`文件中生效的，`@Options`只能配合注解查询`@Insert`使用，而`XML`文件中的查询语句则只能使用属性关键字来定义

```xml
<insert id="insertTest" parameterType="com.sky.entity.Test" keyProperty="id" useGeneratedKeys="true">
    insert into test(name) values(#{name})
</insert>
```

## Redis

`REmote DIctionary Server`，即`Redis` 是一个由 `Salvatore Sanfilippo` 写的 `key-value`键值对存储系统，是跨平台的非关系型数据库

`Redis`的最大优点就是通过内存存储数据，读写性能极高。由于`Redis`基于内存，从容量上也极大地限制了`Redis`的使用，`Redis`一般只用于存储访问数量极高的数据，如热点商品、咨询、新闻等等

### Redis快速入门

#### 连接

`Redis`的客户端连接命令与`MySQL`类似，使用`-h`指定主机，`-p`指定端口号

```cmd
redis-cli.exe -h localhost -p 6379
```

#### 身份认证

`Redis`默认不设置任何的身份认证，如果要设置密码，需要更改`redis.conf`文件中新增一行

```conf
requirepass <password>
```

然后使用`-a`使用密码登录

```
redis-cli.exe -h localhost -p 6379 -a <password>
```

在`Redis6`之前，并没有登录用户的概念，`Redis`的身份验证仅通过密码来实施，而在`Redis6`之后引入了多用户认证技术`ACL`，支持设置不同的用户名与密码。但是本项目中仅使用单因素认证

#### 数据类型

在`Redis`中，常用的数据类型有5种，分别是`string`、`hash`、`list`、`set`和`zset`

- `string`：字符串，最简单的数据类型
- `hash`：哈希，类似于`Java`中的`HashMap`，存储的是一个由多个键值对组成的数据结构，一般用于存储对象
- `list`：有序列表，类似于`Java`中的`LinkedList`，可以有重复元素，按照插入顺序排序
- `set`：集合，类似于`Java`中的`HashSet`，不允许重复元素，无序
- `zset`：有序集合，又称为`sorted set`，在`Redis`中有序集合每个元素对应一个分数，分数越小， 排序越靠前

### Redis常用命令

与`SQL`不同，`Redis`对不同的数据类型的操作命令也不同

#### 字符串操作

| 命令                            | 说明                                 |
| ------------------------------- | ------------------------------------ |
| `SET <key> <value>`             | 设置指定`key`的值                    |
| `GET <key>`                     | 获取指定`key`的值                    |
| `SETEX <key> <seconds> <value>` | 设置`key`的值，并设置`key`的存活时长 |
| `SETNX <key> <value>`           | 只有当`key`不存在时才设置`key`的值   |

#### 哈希操作

| 命令                         | 说明                             |
| ---------------------------- | -------------------------------- |
| `HSET <key> <field> <value>` | 设置哈希表`key`中字段`field`的值 |
| `HGET <key> <field>`         | 获取`key`中字段`field`的值       |
| `HDEL <key> <field>`         | 删除`key`中的指定字段            |
| `HKEYS <key>`                | 获取`key`中所有字段名            |
| `HVALS <key>`                | 获取`key`中所有字段值            |

#### 列表操作

| 命令                                 | 说明                         |
| ------------------------------------ | ---------------------------- |
| `LPUSH <key> <value1> [<value2>...]` | 将一个或多个值插入到列表头部 |
| `LRANGE <key> <start> <top>`         | 获取列表指定范围内的元素     |
| `RPOP <key>`                         | 移除并获取最后一个元素       |
| `LLEN <key>`                         | 获取列表长度                 |

#### 集合操作

| 命令                                  | 说明                       |
| ------------------------------------- | -------------------------- |
| `SADD <key> <member1> [<member2>...]` | 向集合中添加一个或多个成员 |
| `SEMEMBERS <key>`                     | 返回集合中所有的成员       |
| `SCARD <key>`                         | 获取集合中的成员数量       |
| `SINTER <key1> [<key2>...]`           | 返回给定所有集合的交集     |
| `SUNION <key1> [<key2>...]`           | 返回给定所有集合的并集     |
| `SREM <key> <member1> [<member2>...]` | 删除集合中一个或多个成员   |

#### 有序集合操作

| 命令                                                    | 说明                                                     |
| ------------------------------------------------------- | -------------------------------------------------------- |
| `ZADD <key> <score1> <member1> [<score2> <member2>...]` | 向有序集合中添加一个或多个成员                           |
| `ZRANGE <key> <start> <stop> [WITHSCORES]`              | 通过索引区间返回有序集合中指定区间内的成员，可选附带分数 |
| `ZINCRBY <key> <increment> <member>`                    | 对有序集合中指定成员的分数增加增量                       |
| `ZREM <key> <member1> [<member2>...]`                   | 移除有序集合中的一个或多个成员                           |

#### 通用命令

| 命令             | 说明                        |
| ---------------- | --------------------------- |
| `KEYS <pattern>` | 查找所有符合给定模式的`key` |
| `EXISTS <key>`   | 检查`key`是否存在           |
| `TYPE <key>`     | 返回`key`存储的数据类型     |
| `DEL <key>`      | 删除`key`                   |

### Spring Data Redis

在`Java`中，有三种常用的`Redis`客户端，`Jedis`、`Lettuce`和`Spring Data Redis`

`Spring Data Redis`是`Spring`提供的，封装了`Jedis`以及`Lettuce`的客户端，相比前两者开发效率更高，与`SpringBoot`框架融合度也更高，因此我们使用`Spring Data Redis`

#### 快速入门

1. 导入依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

2. 在`application.yml`配置文件中配置`Redis`数据源

```
spring:
  redis:
    host: localhost
    port: 6379
    password: root
```

3. 编写配置类，创建`RedisTemplate`对象

```java
package com.sky.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        log.info("开始创建RedisTemplate对象...");
        RedisTemplate redisTemplate = new RedisTemplate();
        // 设置RedisConnectionFactory
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 设置key序列化器
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        return redisTemplate;
    }
}
```

4. 通过`RedisTemplate`对象来操作`Redis`

```java
package com.sky.test;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
public class SpringDataRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate() {
        redisTemplate.opsForValue().set("name", "张三");
        System.out.println(redisTemplate.opsForValue().get("name"));
    }
}
```

> ![](javaweb2/37.png)

由于我们使用的是`Java`客户端来存储数据，因此`Redis`数据库中存储的实际数据并不一定是可见字符

> ![](javaweb2/38.png)

其本质原因是`Java`客户端在插入数据时会进行序列化来保留一定的数据信息，这些不可见字符就是数据的序列化属性，如果缺少这些信息，`Java`客户端就不能正常解析`Redis`数据库中的数据

> ![](javaweb2/39.png)

如上，我们在`Redis`中存储了一个`Hash`，键名为`username`，值为`kiiz`，然后在`Java`中尝试获取这个数据

```java
@Test
public void testRedisTemplate() {
    System.out.println("尝试获取kiiz -> username");
    System.out.println(redisTemplate.opsForHash().get("kiiz", "username"));
}
```

> ![](javaweb2/41.png)

实际上`Java`获取的并不是`key`为`username`的值，而是`��username`的值，因此结果为`null`

### 店铺营业状态设置

依据`Redis`，我们就可以来实现店铺营业状态的设置逻辑，如果使用传统关系型数据库，用一张表存储一个营业状态字段会非常浪费资源，因此我们可以仅使用`Redis`中的一个字符串字段来存储

在`SpringBoot`项目中，`Redis`操作使用`Repository`类来操作，`Repository`类与`Service`相似，需要设计单独的接口类与实现类方便维护

`Controller`

```java
package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "店铺相关接口")
public class ShopController {

    @Autowired
    private ShopService shopService;

    /**
     * 修改店铺状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("修改店铺状态")
    public Result<String> changeStatus(@PathVariable Integer status) {
        log.info("修改店铺状态：{}", status);
        shopService.changeStatus(status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获取店铺状态")
    public Result<Integer> getStatus() {
        Integer status = shopService.getStatus();
        log.info("获取店铺状态：{}", status);
        return Result.success(status);
    }
}
```

`Service`

```java
package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.exception.BaseException;
import com.sky.exception.FormValueIsNullException;
import com.sky.exception.ShopStatusChangeFailedException;
import com.sky.exception.ShopStatusGetFailedException;
import com.sky.repository.ShopRepository;
import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sky.constant.MessageConstant;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopRepository shopRepository;

    /**
     * 改变店铺状态
     *
     * @param status
     */
    @Override
    public void changeStatus(Integer status) {
        if (status == null || !Objects.equals(status, StatusConstant.ENABLE) && !Objects.equals(status, StatusConstant.DISABLE))
            throw new FormValueIsNullException(MessageConstant.STATUS_IS_NOT_DEFINED);
        Integer count = shopRepository.changeStatus(status);
        if (count == 0)
            throw new ShopStatusChangeFailedException(MessageConstant.SHOP_STATUS_CHANGE_FAILED);
    }

    /**
     * 获取店铺状态
     *
     * @return
     */
    @Override
    public Integer getStatus() {
        Integer status = shopRepository.getStatus();
        if (status == null || !Objects.equals(status, StatusConstant.ENABLE) && !Objects.equals(status, StatusConstant.DISABLE))
            throw new ShopStatusGetFailedException(MessageConstant.STATUS_IS_NOT_DEFINED);

        return status;
    }
}
```

`Repository`

```java
package com.sky.repository.impl;

import com.sky.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class ShopRepositoryImpl implements ShopRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String KEY_SHOP_STATUS = "SHOP:STATUS";

    /**
     * 改变店铺状态
     * @param status
     * @return
     */
    @Override
    public Integer changeStatus(Integer status) {
        ValueOperations ops = redisTemplate.opsForValue();
        ops.set(KEY_SHOP_STATUS, status);
        return Objects.equals(ops.get(KEY_SHOP_STATUS), status) ? 1 : 0;
    }

    /**
     * 获取店铺状态
     * @return
     */
    @Override
    public Integer getStatus() {
        ValueOperations ops = redisTemplate.opsForValue();
        return (Integer) ops.get(KEY_SHOP_STATUS);
    }
}
```

## HttpClient

`HttpClient`是`Apache Jakarta Common`下的子项目，可以用来提供高效的，最新的，功能丰富的支持`HTTP`协议的客户端编程工具包，并且支持`HTTP`协议最新版本和建议

### 快速入门

1. 引入依赖

```xml
<dependency>
    <groupId>org.apache.httpcomponents.client5</groupId>
    <artifactId>httpclient5</artifactId>
    <version>5.3</version>
</dependency>
```

2. 创建`HttpClient`实例

```java
package com.sky.test;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HttpClientTest {

    @Test
    public void test1() {
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
    }
}
```

3. 在`HttpClient`中，每个请求类型对应不同的请求对象类型，这里我们创建一个`GET`请求

```java
package com.sky.test;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HttpClientTest {

    @Test
    public void test1() {
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建GET请求对象
        HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");
    }
}
```

4. 调用`execute`方法发送请求，并接收响应

```java
package com.sky.test;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class HttpClientTest {

    @Test
    public void test1() throws Exception {
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建GET请求对象
        HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");
        // 发送GET请求，并获取响应
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));
    }
}
```

5. 最后释放所有资源

```java
package com.sky.test;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class HttpClientTest {

    @Test
    public void test1() throws Exception {
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建GET请求对象
        HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");
        // 发送GET请求，并获取响应
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        System.out.println(EntityUtils.toString(entity));

        // 释放资源
        response.close();
        httpClient.close();
    }
}
```

> ![](javaweb2/42.png)

### 微信登录功能

微信官方提供了小程序登录的时序图

> ![](javaweb2/43.png)

开发者服务器即是我们自己的后端服务器，从图中可以看出，后端需要实现的逻辑是接收小程序发来的`code`，然后转发到微信接口服务，后端从接口服务得到`session_key`和`openid`，再返回到小程序中，最后小程序携带自定义的登录状态请求后端服务器，后端根据自定义登录态进行确认即可

接着我们来测试微信接口服务，在`Postman`中输入官方接口`https://api.weixin.qq.com/sns/jscode2session`，然后填入必要的参数

| 参数名       | 类型     | 必填 | 说明                                            |
| :----------- | :------- | :--- | :---------------------------------------------- |
| `appid`      | `string` | 是   | 小程序 `appId`                                  |
| `secret`     | `string` | 是   | 小程序 `appSecret`                              |
| `js_code`    | `string` | 是   | 登录时获取的 `code`，可通过`wx.login()`方法获取 |
| `grant_type` | `string` | 是   | 授权类型，此处只需填写 `authorization_code`     |

然后我们使用自己的信息发送请求

> ![](javaweb2/45.png)

可以看到，成功返回了`session_key`和`openid`

接着我们就可以根据开发文档来编写小程序登录的逻辑，首先定义`Controller`，请求路径为`/user/user`

```java
package com.sky.controller.user;

import com.sky.dto.UserLoginDTO;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/user")
@Slf4j
@Api(tags = "C端用户接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登录
     *
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("C端用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录，微信授权码:{}", userLoginDTO);
        UserLoginVO userLoginVO = userService.login(userLoginDTO);
        return Result.success(userLoginVO);
    }
}
```

然后在`Service`中，首先进行参数校验，接着通过`WeChatLoginUtil`向微信开放平台的登录接口发送请求，在接收到数据后进行校验，如果为空则表示登录失败。然后在数据库中查询`openid`所属的用户是否存在，不存在则创建一个新账户。最后根据用户`id`以及`openid`生成`jwt`令牌，返回给微信小程序

```java
package com.sky.service.impl;

import com.sky.constant.JwtClaimsConstant;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.BaseException;
import com.sky.exception.CannotGetOpenIdException;
import com.sky.exception.FormValueIsNullException;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.service.UserService;
import com.sky.utils.JwtUtil;
import com.sky.utils.WeChatLoginUtil;
import com.sky.vo.UserLoginVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatLoginUtil weChatLoginUtil;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = BaseException.class)
    public UserLoginVO login(UserLoginDTO userLoginDTO) {
        // 参数校验
        String code = userLoginDTO.getCode();
        if (code == null)
            throw new FormValueIsNullException(MessageConstant.LOGIN_CODE_IS_NULL);
        if (code.length() != 32 && !code.startsWith("0"))
            throw new FormValueIsNullException(MessageConstant.LOGIN_CODE_FORMATE_ERROR);

        // 获取openid
        String openid = weChatLoginUtil.getOpenId(code);
        if (openid == null)
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);

        // 判断是否为新用户
        User user = userMapper.getByOpenid(openid);
        // 新用户完成自动注册
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            Integer count = userMapper.add(user);
            if (count <= 0)
                throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 创建jwt令牌
        Map<String, Object> userClaim = new HashMap<>();
        userClaim.put(JwtClaimsConstant.USER_ID, user.getId());
        userClaim.put(jwtProperties.getUserTokenName(), openid);
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                userClaim);

        return UserLoginVO.builder()
                .id(user.getId())
                .openid(openid)
                .token(token)
                .build();
    }
}
```

## 缓存

现在我们已经完成了用户端和管理端的基础功能实现，但是目前仍然存在一定的问题，当请求人数过多时，`MySQL`数据库的每次请求都会造成大量的性能开销，可能会导致用户在点击页面后，后台执行两三秒才返回数据，对于用户而言，这样的体验是灾难级的，因此我们需要利用缓存来改善用户体验

根据我们目前的程序设计，用户通过小程序登陆后，首先需要加载所有的分类信息

我们来改造`Service`，在数据库查询之前首先查询缓存，然后判断缓存中是否存在数据，如果不存在，再查询数据库，并将数据添加到缓存中

`Service`

```java
@Override
public List<Category> list(Integer type) {
    // 查询缓存
    List<Category> list = categoryRepository.getCategoryCache(type);
    // 缓存中存在数据，直接返回
    if (list != null && !list.isEmpty()) {
        log.info("查询缓存成功：{}", list);
        return list;
    }
    // 缓存中没有数据，查询数据库
    list =  categoryMapper.list(type);
    // 缓存数据
    categoryRepository.addCategoryCache(list);
    return list;
}
```

新建一个`CategoryRepository`，用户操作`Redis`

```java
package com.sky.repository.impl;

import com.sky.entity.Category;
import com.sky.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String KEY_CATEGORY_LIST = "CATEGORY:LIST";

    /**
     * 查询分类缓存
     * @param type
     * @return
     */
    @Override
    public List<Category> getCategoryCache(Integer type) {
        ValueOperations ops = redisTemplate.opsForValue();
        // 查询所有缓存
        List<Category> list = (List<Category>) ops.get(KEY_CATEGORY_LIST);
        // 筛选指定类型的缓存
        if (type != null) {
            list.removeIf(category -> !Objects.equals(category.getType(), type));
        }
        return list;
    }


    /**
     * 添加分类缓存
     * @param list
     */
    @Override
    public void addCategoryCache(List<Category> list) {
        ValueOperations ops = redisTemplate.opsForValue();
        // 添加缓存
        ops.set(KEY_CATEGORY_LIST, list);
    }
}
```

现在来进行实际测试，第二次查询中可以看到已经从缓存中获取了数据

> ![](javaweb2/47.png)

但是现在会存在一个问题，假设我们在后台对分类进行更改，例如禁用`蜀味烤鱼`分类

> ![](javaweb2/48.png)

但是用户端仍然能够访问，并且获取其中的内容

> ![](javaweb2/49.png)

本质原因在于缓存中的数据与数据库中的数据不同步，我们更新了数据库中的数据，但是缓存数据并没有更新。最直接的解决方法就是在每次数据库操作后，同步删除对应的缓存数据，只有当用户再进行一次查询时，才生成新的缓存数据

这里我们定义了一个切面类，专门用于缓存操作，同时定义了缓存操作方法枚举类，用于指定缓存操作类型

`CacheOperationType`

```java
package com.sky.enumeration;

public enum CacheOperationType {
    /**
     * 新增缓存
     */
    INSERT,
    /**
     * 删除缓存
     */
    DELETE
}
```

`CacheOperationAspect`

```java
package com.sky.aspect;

import com.sky.annotation.Cache;
import com.sky.enumeration.CacheOperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Slf4j
@Component
public class CacheOperationAspect {

    @Autowired
    private RedisTemplate redisTemplate;

    @Pointcut("execution(* com.sky.service.impl.*.*(..)) && @annotation(com.sky.annotation.Cache)")
    public void cacheOperation() {}

    @AfterReturning(pointcut = "cacheOperation()", returning = "result")
    public void cacheOperation(JoinPoint joinPoint, Object result) {
        log.info("开始进行缓存操作");
        // 获取缓存操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Cache annotation = signature.getMethod().getAnnotation(Cache.class);
        if (annotation == null) {
            return;
        }
        CacheOperationType cacheOperationType = annotation.cacheOperationType();
        // 获取缓存的key
        String cacheKey = annotation.cacheKey();
        log.info("缓存操作类型：{}，缓存的key：{}", cacheOperationType, cacheKey);

        if (Objects.equals(cacheOperationType, CacheOperationType.INSERT)) {
            log.info("缓存新增数据：{}", result.toString());
            // 缓存数据
            redisTemplate.opsForValue().set(cacheKey, result);
        } else if (Objects.equals(cacheOperationType, CacheOperationType.DELETE)) {
            log.info("缓存删除数据");
            // 删除缓存
            redisTemplate.delete(cacheKey);
        }
    }
}
```

简单解释一下切面类，切入点设置为所有`Service`中带有`@Cache`注解的方法，这里不能切入`Mapper`，因为`Mapper`返回的数据可能在`Service`中还会进行一些处理。通知类型为后置通知，而且是增强的`@AfterReturning`，在成功方法返回后才更新缓存，保证`Service`中抛出异常时，缓存不会继续更新

接着就是通知逻辑，首先获取缓存的操作类型，第一次查询操作时应当增加缓存，数据库变动时应当删除缓存。从`@Cache`注解中获取缓存`key`，然后调用`RedisTemplate`进行缓存操作

### Spring Cache

`Spring Cache`是`Spring`框架中提供的一套缓存框架，可以让开发者通过注解的方式来简化缓存操作，提高开发效率

`Spring Cache`提供了一层抽象，底层可以切换不同的缓存实现，兼容主流缓存方案，如`EHCache`、`Caffeine`、`Redis`等

### Spring Cache 快速入门

1. 引入依赖

`Spring Cache`会自动识别当前项目中使用的缓存方案，无需手动配置

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

2. 常用注解

| 注解             | 说明                                                         |
| ---------------- | ------------------------------------------------------------ |
| `@EnableCaching` | 开启缓存注解功能，通常注解在启动类上                         |
| `@Cacheable`     | 在方法执行前自动查询缓存中是否有数据，如果有，则直接返回缓存数据；如果没有，则调用方法，并将方法返回值添加到缓存中 |
| `@CachePut`      | 将方法返回值增加到缓存中                                     |
| `@CacheEvict`    | 将一条或多条数据从缓存中删除                                 |

**@CachePut**

`@CachePut`注解可以将方法的返回值添加到缓存中，通过`cacheNames`来设置缓存名称，`key`来设置键名。注意，存储在`Redis`中的键名并不是`key`，而是`cacheNames::key`，例如

```java
@CachePut(cacheNames = "userCache", key = "1")
```

`Redis`中保存的实际键名为`userCache::1`。`key`不仅可以是字符串，还可以是`SpEL`表达式，`Spring Expression Language`，简称`SpEL`，是`Spring`框架提供的一种表达式，支持属性访问、方法调用、运算符、条件判断等等，在这里可以用于获取方法返回值对象中的某些属性，用于构建`key`

```java
@CachePut(cacheNames = "userCache", key = "#user.id")
```

`SpEL`表达式以井号`#`开头，可以拼接一个参数名，如上，使用`.`来访问对象属性，同时支持`?`安全导航操作符来确保不会抛出空指针异常。`SpEL`还支持使用使用关键字来访问特定元素，如`#result`可以直接访问方法返回值，`#p0`、`#a0`访问参数列表，`#root`访问注解操作对象，例如在方法上则是指方法本身，使用`#root.args[0]`来访问第一个参数

**@Cacheable**

`@Cacheable`与`@CachePut`类似，但是在表达式中无法使用`#result`来获取返回值，因为`@Cacheable`会在缓存查询失败后自动将方法返回值存入缓存中

```java
@Override
@Cacheable(cacheNames = "CATEGORY", key = "'LIST'")
public List<Category> list(Integer type) {
    return categoryMapper.list(type);
}
```

上文的代码中，我们为`CategoryService`的`list`方法添加了`@Cacheable`注解，当用户请求分类列表时，就会优先在`Redis`中查找，然后再调用数据库。需要注意，如果缓存中存在对应的数据，`list`方法根本不会执行，因为`Spring Cache`底层使用了动态代理技术，创建一个代理类先执行`Redis`缓存查询操作，只有当结果为空时，才会调用`list`方法，否则代理类就会直接返回数据

