# 值传递
def value_propagation(num: int):
    num = 10

# 引用传递
def reference_propagation(arr: list[int]):
    arr = [1, 1, 1, 1]

if __name__ == "__main__":
    num = 1
    value_propagation(num)
    print(num)

    arr = [1, 2, 3, 4]
    reference_propagation(arr)
    print(arr)