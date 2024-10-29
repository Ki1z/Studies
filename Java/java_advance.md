# Java Advance

`更新时间：2024-10-29`

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
