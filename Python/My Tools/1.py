from typing import Callable

def operator(num1: int, num2: int, func: Callable[[int, int], int]) -> int:
    return func(num1, num2)

def sum(num1: int, num2: int) -> int:
    return num1 + num2

def sub(num1: int, num2: int) -> int:
    return num1 - num2

if __name__ == '__main__':
    a = 10
    b = 5
    result1 = operator(a, b, sum)
    result2 = operator(a, b, sub)
    print(result1)
    print(result2)