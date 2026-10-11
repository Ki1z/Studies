# Java八股

`更新时间：2026-10-11`

注释解释：

- `<>`必填项，必须在当前位置填写相应数据

- `{}`必选项，必须在当前位置选择一个给出的选项

- `[]`可选项，可以选择填写或忽略

*注：该笔记内的可选项和参数均不完整，如有需要，请查询相关手册*

---

## 二分查找

二分查找是一种在有序数组中高效查找目标元素的方法，其核心思想是每次将查找范围对半分，通过比较中间元素与目标值的大小，排除一半不可能的区间，从而将问题规模不断缩小。二分查找可以将查询的时间复杂度由O(n)降低为O(logn)

**基本逻辑**

- 定义一个已经排序的数组 A
- 定义左边界L，有边界R，确定搜索范围，循环执行二分查找
- 对于每一次查找，获取中间索引 M = Floor ((L+R) / 2)
- 中间索引的值 A[M] 与待搜索的值 T进行比较，有如下三种情况
  - A[M] == T 表示找到，返回中间索引
  - A[M] > T，中间值右侧元素都大于T，无需比较，中间索引左移，M - 1设置为右边界，重新查找
  - A[M] < T，中间值左侧元素都小于T，无需比较，中间索引右移，M + 1设置为左边界，重新查找

- 当 L > R 时，表示没有找到，应结束循环

**代码实现**

```java
public static int binarySearch(int[] arr, int target) {
    // 判断数组长度
    int length = arr.length;
    if (length < 1)
        return -1;
    else if (length == 1) {
        return target == arr[0] ? 0 : -1;
    }
    // 声明左右边界
    int l = 0, r = length - 1;
    // 声明中间索引
    int m;
    // 进入查找循环
    while (l <= r) {
        // 设置中间索引
        m = (l + r) / 2;
        // 比较arr[m]与target的值
        if (arr[m] == target)
            // 如果等于，直接返回索引
            return m;
        else if (arr[m] > target)
            // 如果大于，则右侧元素均大于，无需比较
            // 设置新的右边界
            r = m - 1;
        else
            // 如果小于，则左侧元素均大于，无需比较
            // 设置新的左边界
            l = m + 1;
    }
    return -1;
}
```

### 整数溢出

当左右边界都非常大，合计超过int的最大值时，现有方法在计算中间索引m时就会发生整数溢出问题。举个例子，假设现在有一个数组，长度为Integer.MAX_VALUE，那么r此时为Integer.MAX_VALUE - 1，需要查询的元素在数组右半侧，所以左边界右移，移动到 Integer.MAX_VALUE / 2 + 1的位置，此时再计算中间索引 m = Integer.MAX_VALUE / 2 + 1 + Integer.MAX_VALUE - 1，结果一定会大于Integer.MAX_VALUE，造成整数溢出

```java
public static void main(String[] args) {
    int l = Integer.MAX_VALUE / 2 + 1, r = Integer.MAX_VALUE - 1;
    int m = (l + r) / 2;
    System.out.println("new m is " + m);
}
```

> ![](img4/69.png)

**方案一**

解决方案有多种，例如从计算方式入手，造成整数溢出的原因是左边界和右边界之和超过了int最大值，那么我们在计算m时可以不直接使用加号
$$
\frac{l+r}{2} \Rightarrow \frac{l}{2} + \frac{r}{2} \Rightarrow l - \frac{l}{2} + \frac{r}{2} \Rightarrow l + (\frac{r}{2} - \frac{l}{2}) \Rightarrow l + \frac{r-l}{2}
$$
由于r通常比较大，我们让r先减去l，然后再除以2，最后再加上l，l又比较小，所以就可以避免整数溢出的问题

**方案二**

如果我们仅从int8字节的角度来看，l + r大于8字节时就会认为是整数溢出，但如果从更高位角度来看，假设他没有发生溢出，我们正常进行除以2的运算。不过这里并不是直接将其更换为long类型，而是换用按位右移运算符，在位运算中，只考虑整数除法的情况下，按位右移与除以2完全相等，即使发生了整数溢出，在按位右移后，符号位一定会补0，也就是回到整数范围内，这样就可以巧妙地解决整数溢出的问题，而且cpu对于按位运算的性能更好，java底层的二分查找就使用了按位右移

```java
// Like public version, but without range checks.
private static int binarySearch0(int[] a, int fromIndex, int toIndex,
                                 int key) {
    int low = fromIndex;
    int high = toIndex - 1;

    while (low <= high) {
        int mid = (low + high) >>> 1;
        int midVal = a[mid];

        if (midVal < key)
            low = mid + 1;
        else if (midVal > key)
            high = mid - 1;
        else
            return mid; // key found
    }
    return -(low + 1);  // key not found.
}
```

在Java中，按位右移有两种，有符号按位右移`>>`和无符号按位右移`>>>`，这里应使用无符号按位右移，因为我们需要右移后数字回到整数范围，高位补0，在溢出的情况下，高位始终为1，如果使用有符号按位右移会导致右移后高位补1，溢出持续存在

