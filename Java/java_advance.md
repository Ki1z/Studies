# Java Advance

`更新时间：2024-10-30`

注释解释：

- `<>`必填项，必须在当前位置填写相应数据

- `{}`必选项，必须在当前位置选择一个给出的选项

- `[]`可选项，可以选择填写或忽略

*注：该笔记内的可选项和参数均不完整，如有需要，请查询相关手册*

---

## 异常

Java中的异常分为编译时异常和运行时异常两种，对于编译时异常，一般的IDE会直接使用醒目标识报错，而运行时异常是需要程序运行后才会触发的异常

### RuntimeException

RuntimeException是Java中所有运行时异常的父类，所有的运行时异常都需要继承该类

### 常见的运行时异常

- NullPointerException 空指针异常

使用引用数据类型的变量时，如果变量内没有存储任何数据，即等于null时，调用该变量的方法容易导致空指针异常

```java
public class Test {
    public static void main(String[] args) {
        // 常见的运行时异常
        // 空指针异常
        String str = null;
        System.out.println(str.length());
    }
}
```

> <img src="./img3/1.png">

- IndexOutOfBoundsException 下标越界异常

为数组赋值时，如果忘记了数组的长度，或者认为数组的首下标为1，就容易导致下表越界异常

```java
public class Test {
    public static void main(String[] args) {
        // 常见的运行时异常
        // 数组越界
        int[] arr = new int[5];
        arr[5] = 1;
    }
}
```

> <img src="./img3/2.png">

*注：这里的ArrayIndexOutOfBoundsException继承自IndexOutOfBoundsException*

- 数值异常

在计算除法时，除数可能为0，导致数值异常

```java
public class Test {
    public static void main(String[] args) {
        // 常见的运行时异常
        // 除数为0
        int a = 10;
        int b = 0;
        System.out.println(a / b);
    }
}
```

> <img src="./img3/3.png">

### 异常的作用

对于Java的异常来说，通过使用异常处理，开发者可以编写更加健壮、易于维护的代码。同时，异常可以用来定位程序出错的关键信息，也可以作为方法内部的一种特殊返回值，告知上层调用者方法的执行情况

- 普通异常处理

```java
public class Test {
    public static void main(String[] args) {
        int a = 10;
        int b = 0;
        System.out.println(div(10, 0));
    }
    
    public static double div(int dividend, int divisor) {
        if (divisor == 0) {
            System.out.println("除数不能为0");
            return -1;
        }
        return (double) dividend / divisor;
    }
}
```

在上面的程序中，考虑到除数可能为0，因此在`div()`中使用了一个`if`来判断除数是否为0，如果为0则返回-1。但是，如果遇到商本来就为-1，或者用户把-1当作正确结果时，这种处理方法就不再适用

- throw处理

```java
public class Test {
    public static void main(String[] args) {
        int a = 10;
        int b = 0;
        try {
        	System.out.println(div(10, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static double div(int dividend, int divisor) throws RuntimeException{
        if (divisor == 0) {
            throw new RuntimeException("除数不能为0");
        }
        return (double) dividend / divisor;
    }
}
```

在改进的程序中，将返回-1改为了使用`throw`关键字抛出一个`RuntimeException`异常，主函数捕获这个异常，然后输出该异常的信息

### 自定义异常

Java无法为世界上的所有问题提供异常类来代表，如果程序员遇到了某种该类问题，就可以使用自定义异常

根据继承父类的不同，自定义异常可以分为自定义运行时异常和自定义编译时异常

#### 自定义编译时异常

- CustomCompiletimeException类

```java
public class CustomCompiletimeException {
    public static void main(String[] args) {
        // 假设编写一个保存年龄的程序，首先需要判断年龄是否合法
        saveAge(10);
    }

    private static void saveAge(int age) throws AgeIllegalException{
        if (age <= 0) {
            throw new AgeIllegalException("年龄不合法");
        }
        System.out.println("保存成功！");
    }
}
```

- AgeIllegalException异常类

```java
public class AgeIllegalException extends Exception {

    public AgeIllegalException() {}

    public AgeIllegalException(String string) {
        super(string);
    }
}
```

> <img src="./img3/4.png">

*对于编译时异常，必须向上抛出或者进行异常捕获*

#### 自定义运行时异常

- CustomRuntimeException

```java
public class CustomRuntimeException {
    public static void main(String[] args) {
        // 假设编写一个保存年龄的程序，首先需要判断年龄是否合法
        saveAge(0);
    }

    private static void saveAge(int age) {
        if (age <= 0) {
            throw new AgeIllegalRuntimeException("年龄不合法");
        }
        System.out.println("保存成功！");
    }
}
```

- AgeIllegalRuntimeException

```java
public class AgeIllegalRuntimeException extends RuntimeException {

    public AgeIllegalRuntimeException() {}

    public AgeIllegalRuntimeException(String string) {
        super(string);
    }
}
```

> <img src="./img3/5.png">

*运行时异常不需要抛出，但并不代表没有抛出，所有的方法默认都会抛出一个`RuntimeException`*

### 异常处理

目前常见的异常处理方案有两种，一种是将异常层层向上抛，用户层捕获异常，响应适合用户的内容；第二种是最外层捕获异常后，尝试修复该异常

- 响应合适内容

```java
public class ResponseSuitableContent {
    public static void main(String[] args) {
        try {
            System.out.println(calc(2, 0));
        } catch (ArithmeticException e) {
            System.out.println("除数不能为0");
        }
    }

    public static double calc(double a, double b) throws ArithmeticException{
        return Math.pow(a, div(a, b));
    }

    public static double div(double a, double b) throws ArithmeticException{
        if (b == 0) {
            throw new ArithmeticException();
        }
        return a / b;
    }
}
```

> <img src="./img3/6.png">

- 尝试修复

```java
import java.util.Scanner;

public class TryToFix {
    public static void main(String[] args) {
        while (true) {
            try {
                int num = getUserInput();
                break;
            } catch (Exception e) {
                System.out.println("输入有误，请重新输入");
            }
        }
        System.out.println("输入正确");
    }

    public static int getUserInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入一个整数：");

        return scanner.nextInt();
    }
}
```

> <img src="./img3/7.png">

## 泛型

泛型是指，在定义类、接口、方法时，同时声明了一个或多个类型变量
