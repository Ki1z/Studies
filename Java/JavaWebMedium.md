# Java Web Medium

`更新时间：2026-4-25`

注释解释：

- `<>`必填项，必须在当前位置填写相应数据

- `{}`必选项，必须在当前位置选择一个给出的选项

- `[]`可选项，可以选择填写或忽略

*注：该笔记内的可选项和参数均不完整，如有需要，请查询相关手册*

---

## AOP

`AOP`全称`Aspect Oriented Programming`，面向切面编程，是`OOP`面向对象编程的进阶思想。简单来说，就是面向特定方法编程，例如某些业务方法的运行速度较慢，需要定位执行耗时较长的方法，因此首先就需要统计每一个业务方法的耗时。`AOP`可以减少重复代码，对原来的代码没有任何侵入，可以提高开发效率，并且便于维护，任何更改都在单独的`AOP`中进行

### 快速入门

统计所有`Service`业务层方法的耗时

1. 引入`Spring AOP`依赖

```xml
<!--       Spring AOP-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
    <version>3.5.13</version>
</dependency>
```

2. 编写`AOP`类，添加`@Aspect`和`@Component`注解，注册为`Bean`，然后编写`AOP`方法，在方法上使用注解`@Around`来指定需要切入的方法

```java
package com.eiousee.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
public class RecordTimeAspect {

    @Around("execution(* com.eiousee.service.impl.*.*(..))")
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info("方法 {} 耗时：{}ms", joinPoint.getSignature(), end - start);

        return result;
    }
}
```

> ![](javaweb/76.png)

### 核心概念

**连接点**

`JoinPoint`，连接点，是指可以被`AOP`控制的方法，也包括方法执行时的相关信息

**通知**

`Advice`，通知，指可以重复的逻辑，即共性功能，比如在上文中编写的统计执行时间的方法

**切入点**

`PointCut`，所有满足切入点规则的连接点才能被`AOP`控制

**切面**

`Aspect`，描述通知与切入点的对应关系，简单来说，即是通过通知与切入点的约束，可以完整地描述哪些类、哪些方法需要执行哪些额外操作

**目标对象**

`Target`，通知所应用的对象

`SpringAOP`底层基于动态代理技术，实际是创建了一个切入类的代理类，然后交予`IOC`管理，在执行匹配方法时，调用代理类的对应方法，也就是通知方法

### AOP进阶

#### 通知类型

`SpringAOP`提供了多种通知类型注解，用于标注不同的通知方法

| 注解              | 备注                                                         |
| ----------------- | ------------------------------------------------------------ |
| `@Around`         | 环绕通知，通知方法在切入方法前后都被执行                     |
| `@Before`         | 前置通知，通知方法在切入方法前被执行                         |
| `@After`          | 后置通知，通知方法在切入方法后被执行                         |
| `@AfterReturning` | 后置通知，通知方法在切入方法后被执行，但切入方法不能抛出异常 |
| `@AfterThrowing`  | 异常后置通知，通知方法在切入方法抛出异常后被执行             |

*注：环绕通知方法需要手动调用`ProceedingJoinPoint`参数的`proceed()`方法来调用匹配方法，并且环绕通知方法返回值类型必须为`Object`*

#### 公共切入点表达式

在编写`Aspect`类时，如果每个方法都需要编写切入点表达式会非常麻烦，因此`SpringAOP`提供了`@PointCut`注解来记录切入点表达式

**标准语法**

```java
public class SomeAspect {
    @Pointcut("execution(* com.eiousee.service.impl.*.*(..))")
    public void pointcut() {}
    
    @Around("pointcut()")
    public Object recordTime(ProceedingJoinPoint joinPoint) throws Throwable {}
    
    @Before("pointcut()")
    public void before() {}
}
```

#### 通知顺序

默认情况下，在不同的切面类中，按照类名首字母顺序排序，如果不希望使用默认排序，`SpringAOP`也提供了`@Order()`注解来进行排序，括号内数字越小，次序越靠前

**示例**

```java
@Aspect
@Component
@Order(1)
public class FirstAspect {}

@Aspect
@Component
@Order(2)
public class SecondAspect {}
```

#### 切入点表达式

切入点表达式是描述切入点方法的表达式，用来决定项目中哪些方法需要加入通知，即用切入点表达式来匹配连接点，被匹配的连接点即为切入点

常见的切入点表达式类型有`execution()`和`@annotation`

- `execution()`：根据方法签名匹配
- `@annotation`：根据注解匹配

**execution**

**标准语法**

```java
execution([Accessbility] ReturnedValueType [PackageName.ClassName.]MethodName(ParamType) [throws ExceptionName])
```

切入点表达式中也可以使用通配符

- `*`：一级通配符，可以匹配一级任意字段，如包名、类名、方法名等

**示例**

```java
execution(* com.eiousee.*.get*(*))
```

表示匹配任意返回值，位于`com.eiousee`包下的所有类中，任何方法签名前缀为`get`，只有一个参数的方法，注意这里无法匹配`com.eiousee`子包中的类，例如`com.eiousee.controller.DeptController`，因为星号只能作为一级匹配

- `..`：多级匹配，可以匹配多个层级或数量

**示例**

```java
execution(* com.eiousee..*.get*(..))
```

表示匹配任意返回值，位于`com.eiousee`包及其所有子包类中，任何方法签名前缀为`get`，且参数数量任意的方法

`execution`也可以使用逻辑运算符，例如想要匹配特定的两个方法

```java
execution(* com.eiousee.controller.DeptController.list(..)) || execution(* com.eiousee.controller.DeptController.delete(..))
```

**@annotation**

**标准语法**

```java
@annotaion(AnnotationPackageName)
```

**示例**

匹配所有使用`@Eiousee`注解的类

```java
@annotation(com.eiousee.annotation.Eiousee)
```

#### 连接点

在`SpringAOP`中使用`JoinPoint`抽象了连接点，用它可以获得方法执行时的相关信息，比如目标类名、方法名、方法参数等等，对于环绕通知，只能使用`ProceedingJoinPoint`，其他四种通知类型只能使用`JoinPoint`

**常用API**

| 方法名           | 返回值类型 | 说明                                                         |
| ---------------- | ---------- | ------------------------------------------------------------ |
| `getTarget()`    | Object     | 获取匹配对象                                                 |
| `getClass()`     | Class      | 获取匹配类                                                   |
| `getSignature()` | Signature  | 获取匹配方法签名                                             |
| `getName()`      | String     | 获取对应的字符串名称，如`getTarget().getName()`、`getSignature().getName()` |
| `getArgs()`      | Object[]   | 获取匹配方法的参数列表                                       |

