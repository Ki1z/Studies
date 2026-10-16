# Java八股

`更新时间：2026-10-15`

注释解释：

- `<>`必填项，必须在当前位置填写相应数据

- `{}`必选项，必须在当前位置选择一个给出的选项

- `[]`可选项，可以选择填写或忽略

*注：该笔记内的可选项和参数均不完整，如有需要，请查询相关手册*

---

## 二分查找

二分查找是一种在有序数组中高效查找目标元素的方法，其核心思想是每次将查找范围对半分，通过比较中间元素与目标值的大小，排除一半不可能的区间，从而将问题规模不断缩小。二分查找可以将查询的时间复杂度由O(n)降低为O(logn)

- 定义一个已经排序的数组 A
- 定义左边界L，有边界R，确定搜索范围，循环执行二分查找
- 对于每一次查找，获取中间索引 M = Floor ((L+R) / 2)
- 中间索引的值 A[M] 与待搜索的值 T进行比较，有如下三种情况
  - A[M] == T 表示找到，返回中间索引
  - A[M] > T，中间值右侧元素都大于T，无需比较，中间索引左移，M - 1设置为右边界，重新查找
  - A[M] < T，中间值左侧元素都小于T，无需比较，中间索引右移，M + 1设置为左边界，重新查找

- 当 L > R 时，表示没有找到，应结束循环

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

### 面试真题：二分查找

1. 有一个有序表为1,5,8,11,19,22,31,35,40,45,48,49,50，当二分查找值为48的节点时，查找成功需要比较的次数为？

使用标准的二分查找逻辑在做题时效率较低，所以我们利用一些简单规则，即当数组为偶数时，中间值为中间两个值靠左侧的一个；当数组为奇数时，直接找中间的一个元素即可

例如该题中数组长度为13，则中间值为31,31左右两侧各有6个元素，不需要考虑索引。然后在左边界移动到35，35到50之间共有6个元素，中间值有45和48两个，靠左侧的一个即是45。左边界继续右移，48到50之间有三个元素，中间值取49。右边界左移，最后剩下48，找到元素，共计4次查找

2. 使用二分法在序列 1,4,6,7,15,33,39,50,64,78,75,81,89,96中查找元素81时，需要经过多少次比较？

与上一题相同，使用简单规则快捷计算次数，数组长度为14，中间值为39和50，取39；左边界右移，从50到96长度为7，中间值为75，左边界右移；从81到96仅剩下三个元素，直接快速确定最后剩下两次查找，所以共计也是4次查找

3. 在已知的128个数组中二分查找一个数，需要比较的次数最多不超过多少次？

其实是很基本的数学题目，二分查找的本质是循环除以二，所以实际是求2的几次幂为128，按照纯数学方案也就是求以二为底，128的对数 $\log_{2}{128}$。如果数字比较小，可以直接口算，2的幂分别为2、4、8、16、32、64、128、256、512、1024等等，这样也可以方便计算出结果为7。如果数组长度非常大，则只能用对数的方式，如果只能计算以十为底的对数，则计算公式为 $\frac{\lg{128}}{\lg{2}}$，假设数组长度为40亿，求需要比较的次数最多不超过多少次

> ![](img4/70.png)

向上取整为32，如果结果是整数则直接作为结果

## 排序算法

### 冒泡排序

冒泡排序的核心思想是对比相邻两个元素的大小，如果左侧元素比右侧大，则应该交换元素位置，然后指针右移，以此类推，在重复多次后，实现全部元素排序的效果。举个例子，假设有一个数组3,2,1，现在进行冒泡排序，需要遵循以下步骤

- 进行第一轮排序，从索引0开始，取3和2两个元素
- 将两个元素进行比较，3比2大，所以3与2交换位置，得到新数组2,3,1
- 指针右移到索引1，取当前位置和右侧位置的元素，得到3和1
- 将两个元素进行比较，3比1大，所以3与1交换位置，得到新数组2,1,3
- 第一次排序完成，进行第二轮排序，指针回到索引0，取得2和1
- 比较两个元素大小，2比1大，交换元素位置，得到新数组1,2,3
- 指针右移到索引1，取得2和3
- 比较两个元素大小，2比3小，所以不需要交换位置
- 第二轮排序完成，所有元素都已经排序到了正确的位置，所以可以终止排序

从冒泡排序的步骤中不难看出，假设数组长度为n，排序的最大次数不超过n - 1，每次排序时，遍历的数组元素个数也不能超过n - 1，否则会造成指针越界

```java
// 冒泡排序
public static void bubbleSort(int[] arr) {
    int len = arr.length, tmp;
    for (int j = 0; j < len - 1; j++) {
        for (int i = 0; i < len - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                tmp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = tmp;
            }
        }
    }
}
```

**优化：减少比较次数**

冒泡排序有一个显著的特征，因为是相邻元素比较，每一次遍历一定能将一个元素移动到右侧的正确位置，所以每新一轮的比较中，可以依次减小一次比较次数。举个例子，无论数组的最大元素在哪个位置，如2,4,5,1,3、5,2,1,3,4，在第一次遍历中，5一定能移动到最右侧，如下

```sort
// 2,4,5,1,3第一次遍历
[2,4],5,1,3 --不交换-> 2,[4,5],1,3 --不交换-> 2,4,[5,1],3 --交换-> 2,4,1,[5,3] --交换-> 2,4,1,3,5

// 5,2,1,3,4第一次遍历
[5,2],1,3,4 --交换-> 2,[5,1],3,4 --交换-> 2,1,[5,3],4 --交换-> 2,1,3,[5,4] --交换-> 2,1,3,4,5
```

所以我们对代码进行一些优化，在比较大小的循环中，每次循环的次数减去当前排序的轮数减一，每一轮循环一定可以确定一个元素的位置

```c
// 冒泡排序
public static void bubbleSort(int[] arr) {
    int len = arr.length, tmp;
    for (int j = 0; j < len - 1; j++) {
        for (int i = 0; i < len - 1 - j; i++) {
            if (arr[i] > arr[i + 1]) {
                tmp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = tmp;
            }
        }
    }
}
```

**优化：减少冒泡次数**

在冒泡排序中不难发现，即使数组已经完成了排序，算法仍会继续进行遍历，直到len - 1，如下为数组1,2,3,4,5进行冒泡排序

> ![](img4/71.png)

所以当一个数组完成排序后，就不需要再进行排序了，那么如何知道一个数组完成了排序呢？在冒泡排序中，对于没有完成排序的数组，一定会发生元素位置的交换，所以，只要确定数组中没有发生元素位置的交换，也就可以认为数组完成了排序

声明一个新的变量boolean swapped，用于标记本次排序中是否发生了元素位置交换，如果有，则赋值为true，然后在每轮排序后检查swapped的值，只要为false，就可以认为数组排序已经完成了，不需要下一轮排序

```java
// 冒泡排序
public static void bubbleSort(int[] arr) {
    int len = arr.length, tmp;
    boolean swapped;
    for (int j = 0; j < len - 1; j++) {
        swapped = false;
        for (int i = 0; i < len - 1 - j; i++) {
            if (arr[i] > arr[i + 1]) {
                tmp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = tmp;
                swapped = true;
            }
        }
        if (!swapped) break;
    }
}
```

> ![](img4/72.png)

**优化：最佳实现**

假设存在一个特殊的数组2,4,1,3,5,7,8,9，按照上文的比较次数优化算法，第一轮需要比较7次，第二轮需要比较6次。但实际上，这个数组右侧的元素已经完成了排序，第二轮仅需比较2次就可以了，所以比较次数还可以进一步优化

> ![](img4/73.png)

优化比较次数的最终方案与上文减少冒泡次数相似，我们需要得知右侧的元素是否已经完成了排序，也就是右侧的元素位置是否发生了交换。如果我们记录每轮排序时最后一个交换位置的索引，那么该索引右侧的元素就可以认为已经完成了排序——因为没有发生位置交换，在下一轮排序中，就可以直接排除该索引右侧的元素

我们定义两个额外的变量lastSwappedIndex和currentSwappedIndex，lastSwappedIndex记录最后一次交换位置的索引，currentSwappedIndex记录当前排序循环中交换位置的元素索引，在每一轮排序后，将currentSwappedIndex赋值给lastSwappedIndex，循环遍历的右边界改为lastSwappedIndex，就可以缩短每次遍历的次数。而且currentSwappedIndex本身也可以判断数组是否发生了位置交换，如果我们让currentSwappedIndex默认为0，只要没有发生交换，currentSwappedIndex = 0恒为0

```java
// 冒泡排序
public static void bubbleSort(int[] arr) {
    int len = arr.length, tmp, lastSwappedIndex = len - 1;
    do {
        int currentSwappedIndex = 0;
        for (int i = 0; i < lastSwappedIndex; i++) {
            if (arr[i] > arr[i + 1]) {
                tmp = arr[i];
                arr[i] = arr[i + 1];
                arr[i + 1] = tmp;
                currentSwappedIndex = i;
            }
        }
        lastSwappedIndex = currentSwappedIndex;
    } while (lastSwappedIndex != 0);
}
```

> ![](img4/74.png)

### 选择排序

选择排序是每次遍历中选取当前最小的元素置于最左侧，每轮确定一个元素位置，然后左边界右移，以此类推，达到最终排序的目的。举个例子，存在一个数组3,1,2，进行选择排序遵循以下步骤

- 进行第一轮排序，选择左侧第一个元素3，暂定为当前最小元素
- 然后指针右移，比较当前元素与当前最小元素，1比3小，所以当前最小元素更改为1
- 指针继续右移，比较当前元素与当前最小元素，2比1大，所以当前最小元素不变
- 第一轮排序结束，将当前最小元素移动到数组中索引为0的位置，左边界右移，得到数组1,3,2
- 进行第二轮排序，选择左侧第一个元素3，暂定为当前最小元素
- 指针右移，比较当前元素与当前最小元素，2比3小，所以当前最小元素为2
- 第二轮排序结束，当当前最小元素移动到数组中索引为1的位置，得到数组1,2,3，完成排序

所以，在选择排序中，如果存在一个数组，长度为n，需要排序的最大轮数是n - 1，每轮中需要遍历的次数最多为n - 1

```java
// 选择排序
public static void selectionSort(int[] a) {
    for (int i = 0; i < a.length - 1; i++) {
        int s = i;
        for (int j = s + 1; j < a.length; j++) {
            if (a[s] > a[j]) s = j;
        }
        if (s != i) {
            int tmp = a[i];
            a[i] = a[s];
            a[s] = tmp;
        }
    }
}
```

上文编写的选择排序其实已经是优化后的版本，即在每轮遍历元素时，仅将最小元素的索引进行记录，然后在每轮遍历元素之后进行修改，而不是每次遇到最小元素，直接进行修改，这样可以减少元素交换次数

与冒泡排序相比，二者的时间复杂度都是O($n^2$)，选择排序一般比冒泡排序更快，因为交换次数更少；但如果集合的有序度高，冒泡会优于选择；冒泡排序属于稳定排序算法，选择排序不属于稳定排序算法

**双向选择排序**

选择排序的特点是每次记录最小值，然后交换最小值的位置，并右移左边界。为了提高排序效率，是否可以在每轮遍历中同时记录最大值，并左移右边界呢？这就是双向选择排序。双向选择排序同时维护最小值和最大值两个极值，每轮遍历中记录两个极值的位置，然后在遍历后同时进行修改，并同时缩小左右边界，形成一种“夹逼”的结构，当左边界大于或者等于右边界时，即认为排序完成

不过双向选择排序需要考虑一种特殊情况，当数组中最小值和最大值刚好位于两端时，如果最小值在右，最大值在左，那么不管在交换最小值还是最大值的时候，都会将另一个值的位置改变，所以此时需要先交换最小值与最大值，确保在最小值和最大值交换后，另一个值不会再次交换

```java
// 选择排序
public static void selectionSort_v2(int[] arr) {
    int len = arr.length, l = 0, tmp, r = len - 1;
    while (l < r) {
        int minIdx = l, maxIdx = r;
        for (int j = l; j <= r; j++) {
            if (arr[j] < arr[minIdx]) minIdx = j;
            if (arr[j] > arr[maxIdx]) maxIdx = j;
        }
        // 交换最大值
        tmp = arr[r];
        arr[r] = arr[maxIdx];
        arr[maxIdx] = tmp;

        if (minIdx == r) minIdx = maxIdx;

        // 交换最小值
        tmp = arr[l];
        arr[l] = arr[minIdx];
        arr[minIdx] = tmp;

        l++;
        r--;
    }
}
```

在上文的实现中，定义默认的左边界为索引0，右边界为最后一个元素索引len - 1，然后每轮遍历中，int minIdx = l, maxIdx = r暂定每轮的最小值和最大值均为左右边界，接着遍历目前边界中的所有元素，并确定两个极值的索引。然后先交换最大值，并判断最小值是否是右边界，如果是，则将最大值与最小值交换，此时再交换最小值，也就是实际的最大值原来的位置，即左边界，然后交换最小值，保证最小值与自己进行交换，位置不变

双向选择排序可以一定程度上减少选择排序遍历的次数，例如对数组`{1, 70, 12, 24, 49, 13, 20, 86, 56, 99, 38, 81, 92, 84, 60, 66, 64, 76, 29, 65}`进行排序

> ![](img4/75.png)

但如果是有序数组`{1, 10, 11, 13, 14, 19, 22, 36, 38, 39, 43, 53, 54, 56, 59, 65, 77, 78, 81, 94}`，排序效率远不如冒泡排序

> ![](img4/76.png)

### 插入排序

插入排序的核心思想是扩大有序区域，对于一个乱序数组，我们暂确定左侧第一个元素是有序的，然后扩大有序区域，认为左侧两个元素应该是有序的，所以取出第二个元素，与第一个元素进行比较，如果第一个元素大于第二个元素，则将第一个元素右移，并在第一个元素的位置插入第二个元素。同理，有序区域继续扩大至第三个元素，然后取出第三个元素与前两个元素进行比较，再右移元素，并插入元素，以此类推，直到有序区域覆盖整个数组

插入排序效率略优于选择排序，但是是一种稳定排序算法，对于有序数组也不会产生多次排序。如果存在一个数组长度为n，插入排序的轮数不超过n - 1，因为从第二个元素开始，每轮遍历的元素从1开始累增，直到n - 1

```java
public static void insertSort(int[] arr) {
    for (int i = 1; i < arr.length; i++) {
        int insert = arr[i];
        for (int j = i - 1; j >= 0; j--) {
            if (arr[j] > insert) arr[j + 1] = arr[j];
            else {
                arr[j + 1] = insert;
                break;
            }
        }
    }
}
```

i表示左边界，同时也表示每轮排序中取出的待插入的元素，然后我们用一个新变量来临时存放这个元素，需要为其他元素移动预留空间。然后遍历边界外，也就是已经排序好的区域中的所有元素，从右向左比较，如果元素大于待插入的元素，则将元素右移，直到遇到第一个小于待插入元素的元素。因为遍历的的数组是已经排序的数组，所以当第一个小于元素出现时，一定可以确定前面的所有元素都会小于待插入元素，然后将待插入元素插入到指针所指向的元素后面一个位置

### 希尔排序

希尔排序是对插入排序进行的一种改进，如果数组中比较大的元素都在数组左侧，那么在进行普通插入排序时就会造成很多元素位置的移动。而希尔排序的核心思想是先将大的数字尽可能快地右移，这样就可以减少总体元素移动次数

希尔排序将一个数组按间隙分成了多个不同的子数组，间隙是指元素之间间隔的元素个数，假设存在数组6,5,4,3,2,1，按照间隙3划分，可以分为3个子数组，6,3、5,2，4,1，然后对每个子数组进行插入排序，得到3,2,1,6,5,4，接着缩小间隙为2，得到2个子数组，3,1,5、2,6,4，再分别对这两个子数组进行插入排序，得到1,2,3,4,5，再缩小间隙为1，再进行一次插入排序，最终完成排序

间隙的选择是希尔排序效率的核心因素，直接影响算法的时间复杂度

> ![](img4/77.png)

### 面试真题：第n轮排序的结果

1. 如果使用直接插入排序算法和直接选择排序算法对序列18,23,19,9,23,15进行排序，第三轮排序后的结果分别为？

先来看插入排序，我们知道插入排序的特点是从索引1开始，从左向右取出每一个元素，然后将元素与已经排序的元素进行比较。所以插入排序的特征是将数组分为两部分，前半部分为已经排序部分，后半部分为未排序部分，且未排序部分元素顺序永远不变。据此我们可以来推导下排序过程

```insertSort
[18],23,19,9,23,15 -> [18,23],19,9,23,15 -> [18,19,23],9,23,15 -> [9,18,19,23],23,15 -> ...
```

所以第三轮排序后结果为9,18,19,23,23,15。插入排序的快捷计算方式是依次取出数组中每一个元素加入新数组，第n轮就输出原数组前n个数字的有序数组

然后是选择排序，选择排序的特点是从0开始每轮选择第一个元素为最小元素，然后将数组中最小元素与第一个元素交换位置，并右移左边界。因此选择排序的未排序数组顺序会发生改变，只能通过推导得出结果

```selectionSort
[18,23,19,9,23,15] -> 9,[23,19,18,23,15] -> 9,15,[19,18,23,23] -> 9,15,18,[19,23,23] -> ...
```

所以第三轮排序后结果为9,15,18,19,23,23。选择排序的快捷计算方式是依次取出原数组中最小的元素加入新数组，第n轮就输出最终排序好的数组的前n个数字

### 快速排序

快速排序有些类似于二分查找，在数组中选择一个元素为基准，然后将比该元素小的放在左边，比该元素大的放在右边，这样就可以确定该元素的位置。接着继续在左右两侧各选择一个元素继续分区，以此类推，直到分区小于等于1

#### 单边循环

在单边循环中，基准点一般选择最右侧元素，定义两个指针，j指针负责找到比基准点小的元素，一旦找到则与i进行交换。i指针维护小于基准点元素的边界，也就是每次交换的目标索引。最后基准点与i交换，i即为分区位置

这样描述比较复杂，我们举一个例子，假设存在一个数组5,3,7,2,9,8,1,4

- 指针i，j起始位置均为最左侧元素5，基准点为最右侧元素4
- 然后指针j右移，一旦遇到比基准点小的元素，就与i交换，并右移i
- j右移到3，比较得知3比基准点4小，所以j与i互换，i右移，得到数组3,5,7,2,9,8,1,4
- 此时i，j均指向5，然后j继续右移
- j右移到2，2比基准点4小，所以i与j交换位置，i右移，得到数组3,2,7,5,9,8,1,4
- 以此类推，得到数组3,2,1,5,9,8,7,4
- 然后基准点与i进行交换，因为此时可以确定i右侧元素都是大于基准点的元素，得到数组3,2,1,4,9,8,7,5

以上的步骤被称为分区，快速排序实际就是在分区的基础上继续进行分区， 我们先来编写一个分区方法

```java
private static int partition(int[] arr, int left, int right) {
    // 确定基准元素
    int pivot = arr[right];
    // 确定i,j
    int i = left;
    for (int j = left; j < right; j++) {
        if (arr[j] < pivot) {
            swap(arr, i, j);
            i++;
        }
    }
    // 交换i和基准
    swap(arr, i, right);
    return i;
}
```

> ![](img4/78.png)

然后根据返回的位置，就可以将数组分为左右两个部分，继续进行快速排序，让区间越来越小，直到区间范围为1，就可以认为数组全部有序，这里可以递归实现

```java
public static void quickSort(int[] arr, int left, int right) {
    if (left >= right) return;
    // 获取基准索引
    int pivot = partition(arr, left, right);
    // 根据返回的索引，数组分为两个区间，对每一个区间执行一次快速排序
    quickSort(arr, left, pivot - 1);
    quickSort(arr, pivot + 1, right);
}
```

对每一次新的排序，由partition获取新的基准索引，并根据索引分区，递归进行排序。对于左半部分，其左边界为原来的左边界，右边界为基准位置的左边一个位置，也就是pivot - 1；对于右半部分，左边界为基准位置右边的一个位置，也就是pivot + 1，右边界为原来的右边界。而递归的出口则是当左边界大于等于右边界的时候，如果左边界等于右边界，则表示当前区间范围为1，已经完成了排序。如果左边界大于右边界，则是因为左边界越界或者右边界越界，例如当基准位置为0时，此时左侧就没有分区了，计算pivot - 1得到-1，数组越界；或者当基准位置为数组最后一个元素，假设数组长度为6，当基准位置为5时，计算pivot + 1得到6，数组越界，直接返回，表示该方向已经排序完成

> ![](img4/79.png)

#### 双边循环

在单边循环中，选择最左侧元素为基准点元素，j指针负责从右向左找比基准点小的元素，i指针负责从左向右找比基准点大的元素，一旦找到二者进行交换，直至i等于j，最后基准点与i交换，i积分分区位置

同样举个例子，假设存在一个数组5,3,7,2,9,8,1,4

- 选择最左侧元素5为基准点，i此时为5，j此时位于数组最右侧，位于4的位置
- 开始进行比较，j此时发现4小于基准点，因此停止等待
- 然后i右移，寻找比基准点大的元素，移动到7的位置
- 交换i，j，得到新数组5,3,4,2,9,8,1,7
- 然后j左移到1，比基准点小，停止等待
- i右移到9，与j进行交换，得到新数组5,3,4,2,1,8,9,7
- j左移到8的位置，i右移到8的位置，此时j等于i，停止循环
- 比较当前元素与基准点元素的大小，如果大于，基准点元素与当前元素索引 - 1 进行交换，否则交换当前元素
- 基准点与1进行交换，一轮排序完成，得到新数组1,3,4,2,5,8,9,7

```java
private static int partition_v2(int[] arr, int left, int right) {
    // 确定基准元素
    int pivot = arr[left];
    // 确定i, j
    int i = left + 1, j = right;
    while (i < j) {
        if (arr[i] > pivot && arr[j] < pivot) {
            swap(arr, i, j);
            i++;
            j--;
        }
        if (arr[i] <= pivot) i++;
        if (arr[j] >= pivot) j--;
    }
    if (arr[i] > pivot) {
        swap(arr, i - 1, left);
        return i - 1;
    }
    swap(arr, i, left);
    return i;
}
```

> ![](img4/80.png)

快速排序平均时间复杂度为$O(n \times \log_2n)$，最坏时间复杂度为$O(n^2)$，数据量越大，优势越明显。快速排序有多种实现方式，均属于不稳定排序。一般认为，双边循环快排比单边循环快3倍左右

## 集合

### ArrayList扩容

使用ArrayList的无参构造器默认创建的是一个长度为0的数组

```java
private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

public ArrayList() {
    this.elementData = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
}
```

对于有参构造器，如果传入的是数组长度，则返回一个指定长度数组

```java
public ArrayList(int initialCapacity) {
    if (initialCapacity > 0) {
        this.elementData = new Object[initialCapacity];
    } else if (initialCapacity == 0) {
        this.elementData = EMPTY_ELEMENTDATA;
    } else {
        throw new IllegalArgumentException("Illegal Capacity: "+
                                           initialCapacity);
    }
}
```

如果传入一个集合，则按照集合的大小来创建数组

```java
public ArrayList(Collection<? extends E> c) {
    Object[] a = c.toArray();
    if ((size = a.length) != 0) {
        if (c.getClass() == ArrayList.class) {
            elementData = a;
        } else {
            elementData = Arrays.copyOf(a, size, Object[].class);
        }
    } else {
        // replace with empty array.
        elementData = EMPTY_ELEMENTDATA;
    }
}
```

#### add扩容

向ArrayList中插入第一个数据时，就会触发其扩容机制

```java
public boolean add(E e) {
    modCount++;
    add(e, elementData, size);
    return true;
}
```

modCount是结构性修改计数器，与下文的快速失败机制有关。然后执行了add(e, elementData, size)

```java
private void add(E e, Object[] elementData, int s) {
    if (s == elementData.length)
        elementData = grow();
    elementData[s] = e;
    size = s + 1;
}
```

size用于记录ArrayList中保存的元素的个数，也就是ArrayList的实际大小。然后判断实际大小是否等于数组的最大长度，如果等于，则说明数组已经满了，需要扩容。对于新数组，elementData.length为0，size也为0，所以执行elementData = grow()

```java
private Object[] grow() {
    return grow(size + 1);
}
```

调用了重写方法，并传入参数size + 1

```java
private Object[] grow(int minCapacity) {
    int oldCapacity = elementData.length;
    if (oldCapacity > 0 || elementData != DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        int newCapacity = ArraysSupport.newLength(oldCapacity,
                minCapacity - oldCapacity, /* minimum growth */
                oldCapacity >> 1           /* preferred growth */);
        return elementData = Arrays.copyOf(elementData, newCapacity);
    } else {
        return elementData = new Object[Math.max(DEFAULT_CAPACITY, minCapacity)];
    }
}
```

通过int oldCapacity = elementData.length获取原来的数组长度，然后判断数组长度是否大于0，如果大于0，则执行ArraysSupport.newLength扩容，如果是新数组，长度为0，则初始化一个新数组，长度为DEFAULT_CAPACITY和minCapacity中的最大值。minCapacity是传入的size + 1，我们知道size为0，所以minCapacity也就为1

```java
private static final int DEFAULT_CAPACITY = 10
```

DEFAULT_CAPACITY默认为10，所以对于新创建的ArrayList，在第一次插入数据时，其容量会被扩展到10

```java
public static int newLength(int oldLength, int minGrowth, int prefGrowth) {
    // preconditions not checked because of inlining
    // assert oldLength >= 0
    // assert minGrowth > 0

    int prefLength = oldLength + Math.max(minGrowth, prefGrowth); // might overflow
    if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH) {
        return prefLength;
    } else {
        // put code cold in a separate method
        return hugeLength(oldLength, minGrowth);
    }
}
```

newLength是ArraysSupport中一个用于计算合适的数组长度的方法，传入三个参数oldLength旧数组长度、minGrowth最小增量、prefGrowth推荐增量。在ArrayList的扩容中，minGrowth为minCapacity - oldCapacity，oldCapacity为elementData.length，而minCapacity 为size + 1，当size == elementData.length才触发扩容，所以minGrowth恒为1；prefGrowth默认为oldCapacity >> 1，oldCapacity是正数，所以oldCapacity >> 1等价于oldCapacity / 2，也就是原容量的一半

然后int prefLength = oldLength + Math.max(minGrowth, prefGrowth)计算合适的新容量，取minGrowth和prefGrowth的最大值，加上oldLength。我们知道minGrowth恒为1，所以prefLength一定为oldLength + oldLength / 2，也就是原容量的1.5倍

不过当prefGrowth非常大时，计算oldLength + Math.max(minGrowth, prefGrowth)可能导致溢出，所以为了安全，需要判断if (0 < prefLength && prefLength <= SOFT_MAX_ARRAY_LENGTH)，当prefLength大于0且长度不超过SOFT_MAX_ARRAY_LENGTH，也就是2147483639时，才返回prefLength，否则需要执行hugeLength(oldLength, minGrowth)

```java
private static int hugeLength(int oldLength, int minGrowth) {
    int minLength = oldLength + minGrowth;
    if (minLength < 0) { // overflow
        throw new OutOfMemoryError(
            "Required array length " + oldLength + " + " + minGrowth + " is too large");
    } else if (minLength <= SOFT_MAX_ARRAY_LENGTH) {
        return SOFT_MAX_ARRAY_LENGTH;
    } else {
        return minLength;
    }
}
```

对于大容量，无法扩展到自身1.5倍长度的数组，会计算oldLength + minGrowth得到能够使用的最小长度，然后判断是否溢出，如果溢出，则抛出OutOfMemoryError异常，如果小于SOFT_MAX_ARRAY_LENGTH，则直接扩容到SOFT_MAX_ARRAY_LENGTH，如果大于SOFT_MAX_ARRAY_LENGTH但没有溢出，则扩容到计算出的minLength。SOFT_MAX_ARRAY_LENGTH为Integer. MAX_VALUE - 8，所以理论上ArrayList的长度可以到Integer. MAX_VALUE。但是一般JVM不会允许直接创建这么大容量的数组

add扩容方式得到的数组长度可以形成一个特定列表

```java
0, 10, 15, 22, 33, 49, 73, 109, 163, 244, 366, 549, 823, 1234, 1851, 2776, 4164, 6246, 9369, 14053, 21079...
```

#### addAll扩容

addAll与add方法的扩容有些不同

```java
public boolean addAll(Collection<? extends E> c) {
    Object[] a = c.toArray();
    modCount++;
    int numNew = a.length;
    if (numNew == 0)
        return false;
    Object[] elementData;
    final int s;
    if (numNew > (elementData = this.elementData).length - (s = size))
        elementData = grow(s + numNew);
    System.arraycopy(a, 0, elementData, s, numNew);
    size = s + numNew;
    return true;
}
```

首先将传入的集合转换为数组，然后自增modCount，判断插入的集合长度是否为空，如果为空，则返回false。然后判断当前数组长度减去实际存储的容量是否大于需要插入的数组的长度，否则进行扩容，扩容大小为当前实际长度加上需要插入的数组长度

所以，对于一个空的ArrayList，如果执行addAll插入一个长度小于11的集合，那么会扩容为10；但如果插入一个长度大于11的集合，则会扩容为插入的集合长度，而不是add扩容方式对应的容量列表

但是，对于一个已经有数据的ArrayList，插入另一个集合时，其容量取决于二者的长度之和。如果长度和小于旧集合的下次扩容，则直接按add方式扩容；如果长度和大于下次扩容，则扩容到长度和

下面用示例来证明，向一个长度为10的ArrayList中插入长度为3的集合

```java
public static void main(String[] args) throws Exception {
    List<Object> list = new ArrayList<>(10);
    for (int i = 0; i < 10; i++) {
        list.add(i);
    }
    // 下次扩容时10 -> 15
    list.addAll(List.of(1, 2, 3));
    // 获取属性
    Field elementDataField = ArrayList.class.getDeclaredField("elementData");
    elementDataField.setAccessible(true);
    // 获取属性值
    Object[] elementData = (Object[]) elementDataField.get(list);
    System.out.println(elementData.length);
}
```

> ![](img4/81.png)

然后重新插入一个长度为6的集合

```java
public static void main(String[] args) throws Exception {
    List<Object> list = new ArrayList<>(10);
    for (int i = 0; i < 10; i++) {
        list.add(i);
    }
    // 下次扩容时10 -> 15
    list.addAll(List.of(1, 2, 3, 4, 5, 6));
    // 获取属性
    Field elementDataField = ArrayList.class.getDeclaredField("elementData");
    elementDataField.setAccessible(true);
    // 获取属性值
    Object[] elementData = (Object[]) elementDataField.get(list);
    System.out.println(elementData.length);
}
```

> ![](img4/82.png)

### fail_fast和fail_safe

fail_fast和fail_safe是集合迭代器Iterator的两种针对并发修改问题的应对措施。先来说说什么是并发修改，当迭代器遍历某个集合时，如果集合中元素增多或者元素减少，导致集合长度发生变化，此时迭代器指针所指向的元素也可能发生变化，如下

假设此时指针指向索引1，元素为2

```iterator
1, 2, 3, 4, 5
   ^
```

然后集合长度发生变化，在1和2之间插入了元素6

```iterator
1, 6, 2, 3, 4, 5
   ^
```

这就导致本来迭代器操作的元素是2，但是却变成了6，造成了数据不一致的情况

fail_fast快速失败，是指当并发修改发生时，立即终止遍历，并抛出并发修改异常。而fail_safe故障保护，当并发修改发生时，做出一些应对措施，允许临时的数据不一致，让遍历能够运行完成

ArrayList的迭代器属于fail_fast类型，不允许并发修改

```java
public static void main(String[] args) throws Exception {
    List<Object> list = new ArrayList<>(10);
    for (int i = 0; i < 10; i++) {
        list.add(i);
    }
    for (Object i : list) {
        list.add(2, 0);
    }
```

例如遍历list时，向索引为2的位置插入一个元素0

> ![](img4/83.png)

而java.util.concurrent.CopyOnWriteArrayList的迭代器则属于fail_safe类型，支持并发修改

```java
public static void main(String[] args) throws Exception {
    List<Object> list = new CopyOnWriteArrayList<>();
    for (int i = 0; i < 10; i++) {
        list.add(i);
    }
    for (Object i : list) {
        list.add(2, 0);
    }
    System.out.println(list);
}
```

> ![](img4/84.png)

#### fail_fast

在执行增强for的时候，会先创建一个迭代器对象

```java
public Iterator<E> iterator() {
    return new Itr();
}
```

在创建迭代器时，会维护一个属性int expectedModCount = modCount，modCount在ArrayList中用于记录结构性修改操作时，modCount都会自增，上文也有所提及。结构性修改是指增加、删除、清空、替换等操作，而迭代器获取一份modCount，也就是为了确保在遍历过程中，能根据结构性修改次数来判断是否发生并发修改

```java
public E next() {
    checkForComodification();
    int i = cursor;
    if (i >= size)
        throw new NoSuchElementException();
    Object[] elementData = ArrayList.this.elementData;
    if (i >= elementData.length)
        throw new ConcurrentModificationException();
    cursor = i + 1;
    return (E) elementData[lastRet = i];
}
```

在执行迭代器的next方法时，就会先调用checkForComodification()检查modCount的值是否被修改，而且还会if (i >= elementData.length)判断指针是否越界，如果指针越界，也说明在遍历时有元素插入，抛出ConcurrentModificationException并发修改异常

```java
final void checkForComodification() {
    if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
}
```

 #### fail_safe

在CopyOnWriteArrayList的迭代器初始化时，会保留一份原数组的备份es

```java
COWIterator(Object[] es, int initialCursor) {
    cursor = initialCursor;
    snapshot = es;
}
```

在迭代器进行遍历时，实际操作的元素始终是备份es，而不是原数组，所以即使在遍历时进行结构性修改，也不会影响正在遍历的数组

```java
public E next() {
    if (! hasNext())
        throw new NoSuchElementException();
    return (E) snapshot[cursor++];
}
```

我们来观察COW的add方法

```java
public boolean add(E e) {
    synchronized (lock) {
        Object[] es = getArray();
        int len = es.length;
        es = Arrays.copyOf(es, len + 1);
        es[len] = e;
        setArray(es);
        return true;
    }
}
```

上来就添加了一个互斥锁，锁的来源是final transient Object lock = new Object()，也就是每个实例都有一把不同的锁，保证一个实例在同一时间只能被一个线程访问。然后获取当前的数组，并获取长度，将当前数组扩容，并将元素插入，然后将扩容后的数组返回给COW实例

因此，在对COW进行迭代器遍历时，获取到的数据都是原数组的备份，但是结构性操作又是在真正的数组中进行的。所以当我们遍历整个列表并打印，并不会是输出我们新增的元素

```java
public static void main(String[] args) throws Exception {
    List<Object> list = new CopyOnWriteArrayList<>();
    for (int i = 0; i < 5; i++) {
        list.add(i);
    }
    for (Object i : list) {
        list.add(2, 5);
        System.out.println(i);
    }
    System.out.println(list);
}
```

> ![](img4/85.png)

### LinkedList vs ArrayList

- ArrayList

1. 基于数组，需要连续内存
2. 随机访问快，可以根据下标访问
3. 尾部插入、删除性能更好，其他部分插入、删除都需要移动数据，性能相对较低
4. 可以利用cpu缓存，局部性原理

- LinkedList

1. 基于双向链表，无需连续内存
2. 随机访问慢，需要沿着链表遍历
3. 头尾插入与删除性能更好，因为仅需要改变节点头尾指针
4. 每个节点需要额外存储两个指针，占用内存相对更多

#### 随机访问性能对比

ArrayList基于数组结构，数组中存储的是指向各个元素的指针，而指针大小是确定的，所以只需要知道元素的索引，就可以快捷计算出元素距离数组头部的偏移量，然后得到数组的内存地址。ArrayList的查询时间复杂度为$O(1)$

LinkedList基于双向链表，由多个节点组成，每个节点有两个指针，分别指向上一个和下一个链表节点。如果需要查询链表的某个元素，则需要从头节点或者尾节点遍历，LinkedList的查询时间复杂度为$O(n)$

按照传统意义的查询来说，例如按照元素内容查询元素的下标，ArrayList和LinkedList的时间复杂度都是$O(n)$，如果需要快速查询，则应该选择如HashMap、TreeMap这样的结构

#### 增删性能对比

当需要向ArrayList中插入一个元素时，其时间复杂度会根据插入位置的不同而发生变化。当插入位置为尾部时，不需要移动元素，时间复杂度为$O(1)$；当插入位置为其他时，ArrayList需要将该位置后面的元素复制一份，然后右移一位覆盖原来的数组，再插入新数据，此时时间复杂度为$O(n)$。ArrayList的删除操作类似，删除尾部的时间复杂度为$O(1)$，其他位置为$O(n)$

而当需要向LinkedList插入数据时，其时间复杂度也根据插入位置的不同而不同。当插入位置为头部或者尾部时，仅需更改头部或者尾部元素的前指针和后指针，时间复杂度为$O(1)$；如果插入中间元素，则需要先遍历寻找位置，时间复杂度为$O(n)$。删除操作类似

ArrayList的中间增删操作性能仍优于LinkedList，因为ArrayList查询中间元素的性能更高

#### 局部性原理

在计算机中，CPU计算的速度非常快，CPU时钟周期是CPU中最小的时间单位，完成一个CPU命令一般需要一个或者更多的时钟周期。时钟周期是CPU主频率的倒数，对于现代主流桌面端CPU，主频一般在4G甚至5G赫兹以上，时钟周期也就是0.2纳秒左右。而CPU与内存间的读写性能一般在100纳秒左右，所以实际上CPU大量时间都是在等内存提供数据，而不是进行计算

而为了提高CPU的利用率，1985年起，英特尔首次为CPU添加独立的高速缓存，发展至今，形成了现在的CPU三级缓存L1、L2、L3。L1读写性能一般在1纳秒，L2在3到5纳秒，L3在10到20纳秒。如果三级缓存中仍无法获取数据，才从内存中获取数据

> ![](img4/86.png)

所以，如果想要提高计算机性能，就应该尽量提高CPU三级缓存的命中率，而局部性原理就是一种缓存优化措施。CPU在读取数组中元素到缓存时，并不会只读取需要计算的那一个特定元素，而是将数组中与之相邻的一些元素也读取到缓存中，因为相邻元素有很大可能也会被访问，这就是空间局部性

局部性原理将局部性分为三类

- 时间局部性：近期访问过的数据或指令很可能在不久后再次被访问，如循环中的变量
- 空间局部性：被访问位置附近的存储单元也可能很快被访问，如数组顺序遍历
- 顺序局部性：指令通常按顺序执行，顺序执行与非顺序执行比例约为 5:1

LinkedList因本身是非连续内存，在运算时不满足局部性原理，因此在缓存层面性能也比ArrayList更低

### HashMap

HashMap的底层数据结构在JDK1.7和1.8版本有所不同，1.7使用数组 + 链表结构，而1.8引入红黑树，使用数组 + 链表 + 红黑树结构。HashMap类似于Redis中的dict，在插入元素时先计算元素的哈希值，然后使用哈希值映射到数组上的一个位置，数组中存储的并不是一个一个的元素，而是一个单链表，如果哈希数组的长度足够小，就容易出现哈希碰撞，此时将新元素挂载在旧元素后方，就可以避免元素被覆盖或者无法插入

如下，我们定义一个长度为16的HashMap，向其中插入10个元素

> ![](img4/87.png)

以下是有关HashMap的重要常量

```java
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
static final int MAXIMUM_CAPACITY = 1 << 30;
static final float DEFAULT_LOAD_FACTOR = 0.75f;
static final int TREEIFY_THRESHOLD = 8;
static final int MIN_TREEIFY_CAPACITY = 64;
static final int UNTREEIFY_THRESHOLD = 6;
```

- DEFAULT_INITIAL_CAPACITY：使用无参构造器时创建的桶数组的默认长度，每个桶就是一个节点链表
- MAXIMUM_CAPACITY：HashMap允许的最大容量
- DEFAULT_LOAD_FACTOR：默认负载因子，当负载率超过这个值时会触发扩容，负载率等于负载量除以总容量
- TREEIFY_THRESHOLD：长度树化阈值，当桶中的链表长度大于等于8时，会树化形成红黑树
- MIN_TREEIFY_CAPACITY：容量树化阈值，当桶数组的长度大于等于64时，才会触发树化，与TREEIFY_THRESHOLD必须同时满足
- UNTREEIFY_THRESHOLD：退化阈值，当红黑树节点树小于等于6时，会从红黑树退化为链表

```java
static class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    V value;
    Node<K,V> next;
}
```

这是HashMap中有关节点的定义，一个节点中存储了四个属性，分别是哈希值、键、值和指向下一个Node的指针

#### 链表过长问题

哈希表最大的优点是查询速度非常快，如果使用合适的哈希函数，并且数据量较小，HashMap的查询时间复杂度为$O(1)$，等同于数组；但是如果哈希函数选择不恰当，或者在小容量的哈希表中存储大量结构，导致一个桶存储了大长度的链表，HashMap的最坏时间复杂度会降低到$O(n)$，因为需要遍历链表中每一个元素

对于HashMap，提升查询性能实际就是缩短链表长度，或者提升链表的查询性能。目前有扩容和树化两种方案

##### 扩容

HashMap在插入时，先计算键的哈希值，然后以哈希值为基准，映射出桶数组上的一个索引位置，再在对应位置插入Entry。而如果对HashMap进行扩容，就可以使映射的范围更大，原来索引相同的元素在扩容后的索引就可能不同，从而减短链表长度，提升性能

HashMap的扩容永远只会发生在size大于threshold的时候，每次调用put()、putAll()等方法时，会判断当前的size与threshold的关系

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}
```

包装方法，实际调用了putVal()

```java
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else {
        Node<K,V> e; K k;
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            for (int binCount = 0; ; ++binCount) {
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
    ++modCount;
    if (++size > threshold)
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

首先定义了Node<K,V>[] tab桶数组、Node<K,V> p待插入的节点和int n, i两个标识符。然后if ((tab = table) == null || (n = tab.length) == 0)判断当前桶数组是否为空，或者桶数组的长度是否为0，如果桶数组是空的，则执行resize()，并将新的长度赋值给n，即n代表当前桶数组长度。而resize()其实就是扩容方法

接着if ((p = tab[i = (n - 1) & hash]) == null)判断索引位置是否存在其他元素，如果p为null，则表示该桶暂未插入任何元素，直接tab[i] = newNode(hash, key, value, null)插入一个新Node

如果桶中存在元素，则if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k))))判断Key是否是相同的，如果是相同Key，则e = p表示需要进行替换；再else if (p instanceof TreeNode)判断是否是一个树节点，如果是树节点，执行e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value)插入一个分支；如果不是树节点，且Key不相同，则说明是链表结构，需要遍历寻找Key。这里循环先执行了if ((e = p.next) == null)判断是否存在下一个Node，是因为如果走到这一个分支，就可以确保链表中之前的节点都没有这个Key，如果不存在下一个节点，则说明该HashMap中Key不存在，p.next = newNode(hash, key, value, null)直接插入即可。而在插入之后，还需要if (binCount >= TREEIFY_THRESHOLD - 1)判断是否满足了树化条件，当旧链表长度大于等于TREEIFY_THRESHOLD - 1的时候，插入新的节点，链表长度就一定大于等于TREEIFY_THRESHOLD，需要进行树化

然后再判断当前节点的Key是否与插入Key相等，相等则需要进行替换，替换不触发树化。接着判断e是否为null，如果e不为null，说明上方找到了Key相同的节点，所以覆盖原来的value，V oldValue = e.value记录旧value，e.value = value替换value，最后return oldValue返回旧value

如果是插入新值，需要自增modCount，因为存在modCount，所以HashMap也是线程不安全的，并且并发问题的应对策略是fail_fast。接着if (++size > threshold)判断是否需要扩容，最后返回null

*注：这里的afterNodeAccess()和afterNodeInsertion()是LinkedHashMap的方法，在HashMap中均为空实现*

```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {               // zero initial threshold signifies using defaults
        newCap = DEFAULT_INITIAL_CAPACITY;
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr;
    @SuppressWarnings({"rawtypes","unchecked"})
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    if (oldTab != null) {
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    return newTab;
}
```

resize()是HashMap的核心与难点。Node<K,V>[] oldTab = table存储旧hashtable，也就是桶数组；int oldCap = (oldTab == null) ? 0 : oldTab.length记录桶数组长度，如果oldTab == null，则是新创建的HashMap，还未进行初始化，赋值为0；int oldThr = threshold记录旧阈值，阈值通过(int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY)计算，DEFAULT_LOAD_FACTOR为0.75，DEFAULT_INITIAL_CAPACITY为16，所以初始阈值为12；int newCap, newThr = 0初始化新hashtable的长度和阈值

if (oldCap > 0)判断是否需要初始化，如果不需要，则进一步判断if (oldCap >= MAXIMUM_CAPACITY)旧的长度是否大于允许的最大长度，超过则设置threshold = Integer.MAX_VALUE阈值为int最大值，然后直接返回旧的hashtable，不进行扩容。((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap >= DEFAULT_INITIAL_CAPACITY)将旧容量左移一位，即翻倍得到新容量，再判断是否小于最大值，并且旧容量需要大于等于初始容量，满足则将新阈值也翻倍

else if (oldThr > 0)如果需要初始化，但是已经设定了阈值，则newCap = oldThr将容量设置为阈值；最后就是完全初始化，newCap = DEFAULT_INITIAL_CAPACITY设置默认容量，newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY)设置默认阈值

接着如果新阈值仍为0，即当oldCap大于0，但是不满足(newCap = oldCap << 1) < MAXIMUM_CAPACITY或者oldCap >= DEFAULT_INITIAL_CAPACITY，仍需计算一个新阈值。先计算float ft = (float)newCap * loadFactor得到ft，然后判断新容量小于最大值且ft小于最大值，如果二者均满足，则新阈值为ft；任一不满足，新阈值为int最大值

新hashtable参数计算完成，threshold = newThr赋值新阈值，Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap]根据新容量创建桶数组，table = newTab将HashMap的桶数组更换为新数组，下面就开始转移数据

for循环遍历oldCap，if ((e = oldTab[j]) != null)获取每一个桶，并判断桶是否为空，如果桶不为空，oldTab[j] = null释放原来的桶。if (e.next == null)判断是否只有一个元素，如果只有一个元素，则newTab[e.hash & (newCap - 1)] = e重新计算索引，并插入该节点；else if (e instanceof TreeNode)如果节点类型是树节点，((TreeNode<K,V>)e).split(this, newTab, j, oldCap)拆分红黑树，这一步可能导致树退化为链表；最后的情况就是桶中存储了一个链表

在上文中我们知道，HashMap的扩容，一直会将容量扩展至当前容量的2倍，而在重新计算索引时，这也会导致新索引仅有两种可能。第一，索引位于原位置；第二，索引位于i + oldCap位置。举个例子，假设存在一个HashMap，默认长度16，元素A的哈希值为97，元素B的哈希值为49，与16取模结果都为1。但是如果将HashMap进行一次扩容，长度达到32，重新计算元素A和元素B的索引，就会发现元素A新索引仍为1，但是元素B新索引为17，刚好是1 + 16

所以Java定义了四个指针Node<K,V> loHead = null, loTail = null和Node<K,V> hiHead = null, hiTail = null。lo表示low低位，hi表示high高位，Java在判断是否需要移动位置时的公式是e.hash & oldCap而不是重新计算hash & (newCap - 1)。我们从可以从数学上来理解这个计算方式
$$
假设 \ oldCap=16，newCap=32，则 \\
oldCap - 1 = 00001111_{(2)} \\
newCap - 1 = 00011111_{(2)} \\
对任意数字，计算hash \ \& \ (newCap - 1)都有 \\
hash \ \& \ (newCap - 1) = hash \ \& \ (oldCap - 1) + hash \ \& \ oldCap \\
例如hash(a)=97，hash(b)=49则 \\
hash(a) \ \& \ 16 = 0 \\
hash(b) \ \& \ 16 = 16
$$
所以只需要计算e.hash & oldCap，判断结果是否为0，如果为0，表示元素仍在原位置；而结果不为0，元素需要移动到i + oldCap的位置。因此，if ((e.hash & oldCap) == 0)判断节点位置，如果为0，if (loTail == null)判断是尾节点是否为null，也就是是否刚开始遍历这个链表，如果是，设置loHead = e，放在原位置，并且是头节点；如果loTail == null尾节点不为空，则loTail.next = e挂载在尾节点之后，也就是尾插入法，最后loTail = e设置e为尾节点。而如果e.hash & oldCap不为0，表示需要移动到新位置，同样if (hiTail == null)判断是否是头节点，然后设置相应头尾节点

遍历完成后，if (loTail != null)判断原位置是否还有元素，如果有，则loTail.next = null释放尾节点原来的next，因为可能指向另外一个位置的节点，然后newTab[j] = loHead交给新数组。if (hiTail != null)判断高位是否有元素，有则与低位一样，释放尾节点next，并newTab[j + oldCap] = hiHead插入到新数组的j + oldCap位置

##### 树化

当链表元素个数大于等于8，且桶数组容量大于等于64时，才会触发树化。树化会将链表转换为一个红黑树，红黑树的查询性能优于链表，时间复杂度为$O(\log_2{n})$，但是应当尽量避免树化。对于合格的哈希函数，应当服从泊松分布，在负载因子0.75的情况下，链表长度超过8的概率是0.00000006，树化阈值设置为8就是为了尽量避免树化

红黑树的内存占用相比普通链表更大，在Java中，TreeNode继承了Node

```java
static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
    TreeNode<K,V> parent;  // red-black tree links
    TreeNode<K,V> left;
    TreeNode<K,V> right;
    TreeNode<K,V> prev;    // needed to unlink next upon deletion
    boolean red;
}
```

parent父节点，left左子节点，right右子节点，prev前指针，red颜色标识符，如果为true表示该节点为红色节点。Java在构建红黑树节点时还保留了双向链表结构，也是为了能够更方便地退化为链表。相比链表，红黑树节点多了四个指针和一个布尔值，而且大多数情况下HashMap每个桶的长度都不会超过2，使用链表的效率和内存占用要远远优于红黑树

**退化**

上文提到，当桶长度小于等于6时，红黑树会退化为链表，而退化还有一个规则。删除一个红黑树节点前，当红黑树的根节点，其子节点、左子节点的左子节点任意一个为null时，也会导致树退化

> ![](img4/88.png)

例如上图所示的红黑树，如果删除74、85，他们是右子节点的子节点，树不会退化；而如果删除9和2（不考虑平衡），左子节点的左子节点为null，再次删除元素时，就会导致该树退化为链表

