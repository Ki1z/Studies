Major_Arcana = ["The Fool", "The Magician", "The High Priestess","The Empress", "The Emperor", "The Hierophant","The Lovers", "The Chariot", "Strength","The Hermit", "Wheel of Fortune", "Justice","The Hanged Man", "Death", "Temperance","The Devil", "The Tower", "The Star","The Moon", "The Sun", "Judgement","The World"]
wands = ["Ace of Wands", "Two of Wands", "Three of Wands", "Four of Wands", "Five of Wands", "Six of Wands", "Seven of Wands", "Eight of Wands", "Nine of Wands", "Ten of Wands", "Page of Wands", "Knight of Wands", "Queen of Wands", "King of Wands"]
cups = ["Ace of Cups", "Two of Cups", "Three of Cups", "Four of Cups", "Five of Cups", "Six of Cups", "Seven of Cups", "Eight of Cups", "Nine of Cups", "Ten of Cups", "Page of Cups", "Knight of Cups", "Queen of Cups", "King of Cups"]
swords = ["Ace of Swords", "Two of Swords", "Three of Swords", "Four of Swords", "Five of Swords", "Six of Swords", "Seven of Swords", "Eight of Swords", "Nine of Swords", "Ten of Swords", "Page of Swords", "Knight of Swords", "Queen of Swords", "King of Swords"]
pentacles = ["Ace of Pentacles", "Two of Pentacles", "Three of Pentacles", "Four of Pentacles", "Five of Pentacles", "Six of Pentacles", "Seven of Pentacles", "Eight of Pentacles", "Nine of Pentacles", "Ten of Pentacles", "Page of Pentacles", "Knight of Pentacles", "Queen of Pentacles", "King of Pentacles"]
Minor_Arcana = wands + cups + swords + pentacles
tarot = Major_Arcana + Minor_Arcana

def reverse_fortune_wheel(FATE_prime):
    a, b, c, d, e = FATE_prime
    numerator = a + b - c + d - e
    if numerator % 2 != 0:
        raise ValueError(f"Cannot reverse: odd numerator for v1. a={a}, b={b}, c={c}, d={d}, e={e}, numerator={numerator}")
    v1 = numerator // 2
    v0 = a - v1
    v2 = b - v1
    v3 = c - v2
    v4 = d - v3
    if v4 + v0 != e:
        raise ValueError(f"Equation 5 not satisfied: {v4} + {v0} != {e}")
    return [v0, v1, v2, v3, v4]

final_values = [
    2532951952066291774890498369114195917240794704918210520571067085311474675019,
    2532951952066291774890327666074100357898023013105443178881294700381509795270,
    2532951952066291774890554459287276604903130315859258544173068376967072335730,
    2532951952066291774890865328241532885391510162611534514014409174284299139015,
    2532951952066291774890830662608134156017946376309989934175833913921142609334
]

current = final_values.copy()
for _ in range(250):
    current = reverse_fortune_wheel(current)

initial_values = current

YOUR_initial_FATE = []
for value in initial_values:
    mod_value = value % 78
    possible_reversed_index = (-value - 1) % 78
    if possible_reversed_index < 22:
        card = tarot[possible_reversed_index]
        YOUR_initial_FATE.append(f"re-{card}")
    else:
        if mod_value < 22:
            card = tarot[mod_value]
            YOUR_initial_FATE.append(card)
        else:
            card = tarot[mod_value]
            YOUR_initial_FATE.append(card)

# Ensure no duplicates (as per problem statement)
assert len(YOUR_initial_FATE) == len(set(YOUR_initial_FATE)), "Duplicate cards found"

flag = "hgame{" + "&".join([card.replace(" ", "_") for card in YOUR_initial_FATE]) + "}"
print(flag)