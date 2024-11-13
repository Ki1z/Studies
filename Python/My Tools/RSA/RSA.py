# 此处插入值 p q c e
p= 142746377986265588203209230608614141486992129456713308782651620565639716483042704148029354162076590370818112090453266601765342503099931507118348740371992875352802970518126456858906932187200583596528119833923802821087594481687681655766838418022679403912877047458442485930708989264802383271676438430051477419073
q= 101556540759993341132401841934321855672207223737490583713852133044065857859294513627531508680505435020802615808427112439278953868339029525177790315321339940570430502294620060278709533980502929100142925694557791453738196989957889876386914879754138712981207794515117907563426835202358740651616786664770511197491
e= 65537
c= 2611937276563784099571420314571389591567448750803793958762014602973047518746085506592099244737625830903336692357104933828317944187060779272934599968616752518554261802011669038583836429797400505009978723437313062289959873127204553824981485673453311158945977895389921853001526741382428068045040739837015887345762607146159386674676190190751753826672466106089054696312109426929622505950864639198171842997066483989838750844330068119086429713235512330019438990633015670552379970353941911418304624746490992764510781206851621852353964095594842028856883435745655968386202086207268861651484600402486261505175288889546106455212
##################
import gmpy2
n = p * q
d = gmpy2.invert(e, (p - 1) * (q - 1))
print("d:\n" + str(d))
m = pow(c, d, n)
print("m:\n" + str(m))
m = hex(m)[2:]
flag = ""
for i in range(len(m) // 2):
    flag += chr(int(m[i * 2: (i + 1) * 2], 16))
print(flag)