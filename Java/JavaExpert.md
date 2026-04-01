# Java Expert

`更新时间：2026-4-1`

注释解释：

- `<>`必填项，必须在当前位置填写相应数据

- `{}`必选项，必须在当前位置选择一个给出的选项

- `[]`可选项，可以选择填写或忽略

*注：该笔记内的可选项和参数均不完整，如有需要，请查询相关手册*

---

## 反射

反射允许以编程方式访问有关已加载的类的字段，方法和构造器的信息，以及使用反射字段，方法和构造器在封装和安全限制内对其底层对应项进行操作。简单来说，反射可以获取类的信息，例如`JetBrain Intellij IDEA`中，实例化一个类时，会自动补全该类中的所有属性和方法，这就是使用反射实现的

### 反射的步骤

1. 获取`Class`对象

在`Java`中，万物皆对象，即使是类本身，也看作是一个类文件对象。`Java`提供了三种获取`Class`对象的方式

```java
// 直接获取
Class reflectionClass = Reflection.class;
// Class类方法获取
Class reflectionClass2 = Class.forName("com.eiousee.expert.Reflection");
// 对象方法获取
Class reflectionClass3 = new Reflection().getClass();
```

2. 获取类中的信息

`Java`提供了大量获取类有关信息的`API`

**构造器相关**

| 方法                                                         | 说明                         |
| ------------------------------------------------------------ | ---------------------------- |
| `Constructor<?>[] getConstructors()`                         | 获取全部`public`修饰的构造器 |
| `Constructor<?>[] getDeclaredConstructors()`                 | 获取全部构造器               |
| `Constructor<?>[] getConstructor(Class<?>... parameterTypes)` | 获取某个`public`修饰的构造器 |
| `Constructor<?>[] getDeclaredConstructor(Class<?>... parameterTypes)` | 获取某个构造器               |

**示例**

```java
package com.eiousee.expert;

import java.lang.reflect.Constructor;

public class Reflection {
    public static void main(String[] args) throws Exception {
        Class<A> clazz = A.class;
        // 获取所有public构造器
        Constructor[] constructors = clazz.getConstructors();
        for (Constructor constructor : constructors) {
            System.out.println(constructor.getParameterCount());
        }
        // 获取所有构造器
        System.out.println("-------");
        Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : declaredConstructors) {
            System.out.println(constructor.getParameterCount());
        }
        // 获取特定构造器
        System.out.println("-------");
        Constructor constructor = clazz.getConstructor(int.class, String.class);
        System.out.println(constructor);

    }
}

class A {
    private int a;
    public String b;

    public A() {}

    private A(int a) {
        this.a = a;
    }

    public A(int a, String b) {
        this.a = a;
        this.b = b;
    }
}
```

> ![](img4/1.png)

**属性相关**

| 方法                                         | 说明                                   |
| -------------------------------------------- | -------------------------------------- |
| `public Field[] getFields()`                 | 获取类中`public`修饰的所有属性         |
| `public Field[] getDeclaredFields()`         | 获取类中的所有属性                     |
| `public Field getField(String name)`         | 获取字段名为`name`，`public`修饰的属性 |
| `public Field getDeclaredField(String name)` | 获取字段名为`name`的属性               |

**示例**

```java
package com.eiousee.expert;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Reflection {
    public static void main(String[] args) throws Exception {
        Class<A> clazz = A.class;
        // 获取所有public属性
        Field[] fields = clazz.getFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }
        // 获取所有属性
        System.out.println("--------------");
        Field[] fields1 = clazz.getDeclaredFields();
        for (Field field : fields1) {
            System.out.println(field.getName());
        }
        // 获取字段名为a的属性
        System.out.println("--------------");
        Field field = clazz.getDeclaredField("a");
        System.out.println(field.getAnnotatedType());
    }
}

class A {
    private int a;
    public String b;

    public A() {}

    private A(int a) {
        this.a = a;
    }

    public A(int a, String b) {
        this.a = a;
        this.b = b;
    }
}
```

> ![](img4/2.png)

**方法相关**

| 方法                                                         | 说明                       |
| ------------------------------------------------------------ | -------------------------- |
| `Method[] getMethods()`                                      | 获取`public`修饰的所有方法 |
| `Method[] getDeclaredMethods()`                              | 获取所有方法               |
| `Method getMethod(String name, Class<?>... parameterTypes)`  | 获取`public`修饰的特定方法 |
| `Method getDeclaredMethod(String name, Class<?>... parameterTypes)` | 获取特定方法               |

