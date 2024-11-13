import gmpy2
import libnum

n= 84759586321367337887780437584685106579085074965112521484344049646437894046082602854634633148582472968603463743438343484823980031138129314219504095698178622254320271681999860332610325223454341809051178943622128336098505461927391262429524484667385743868165220569109483345920566095908674582795138002280172054901
e= 66288875417761391735336508179031781461458397094560594352005873830730369271057472593019640848396507956317226914042734460741730619378424621998873462267141804992425548349906362051377176848446970670269625959428657063813053998204432987906270288972553991694084764963674828882833521361595114819645054140098694014033
c= 55793266382808668383390028264320313476480462639251420282032620664514843264012556336628829925417590165117761221508016231889956332919056931672687301567732943226049335108323978513326330771724845198936862017830947882511017614192627520685625693006743832632002358777752259725470367349656504917200798619000646027705

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
