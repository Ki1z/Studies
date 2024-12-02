def factors(num: int) -> list[int]:
    factorList = []
    for i in range(2, int(num ** 0.5) + 1):
        if num % i == 0:
            factorList.append(i)
            if i != num // i:
                factorList.append(num // i)
    factorList.append(num)

    return sorted(factorList)

if __name__ == '__main__':
    num = int(input())
    factorList = factors(num)
    print(factorList)