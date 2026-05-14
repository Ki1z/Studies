# Java Web Advance

`更新时间：2026-5-14`

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

