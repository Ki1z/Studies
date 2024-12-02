import gmpy2
import libnum

n= 63465293776825804886780705907118514318354492203844885746068763972603420624459
e= 23
c= 8415448673923521810157495017636043788928337310356253266903946044592128831391

def expand_fraction(num, den):
    expansion = []
    while den != 0:
        quotient = num // den
        expansion.append(quotient)
        num, den = den, num % den
    return expansion

def compute_fraction(expansion):
    numer = 0
    denom = 1
    for value in reversed(expansion):
        numer, denom = denom, value * denom + numer
    return numer, denom

def find_factors(a_val, b_val, c_val):
    root = gmpy2.isqrt(b_val * b_val - 4 * a_val * c_val)
    return (-b_val + root) // (2 * a_val), (-b_val - root) // (2 * a_val)

def generate_fractions(expansion):
    fractions = []
    for i in range(1, len(expansion) + 1):
        fractions.append(compute_fraction(expansion[:i]))
    return fractions

def execute_attack(public_exp, modulus):
    expansion = expand_fraction(public_exp, modulus)
    fractions = generate_fractions(expansion)
    for candidate_d, candidate_k in fractions:
        if candidate_k == 0: continue
        if (public_exp * candidate_d - 1) % candidate_k != 0:
            continue
        totient = (public_exp * candidate_d - 1) // candidate_k
        factor1, factor2 = find_factors(1, modulus - totient + 1, modulus)
        if factor1 * factor2 == modulus:
            return candidate_d

d=execute_attack(e, n)
m=pow(c, d, n)
print(libnum.n2s(m).decode())
