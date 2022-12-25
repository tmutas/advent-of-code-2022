import sys

with open(sys.argv[1], "r") as fl:
    rawinput = fl.read()

pairs = list(map(lambda x: list(map(eval, x.split("\n"))), rawinput.split("\n\n")))

def compare_ints(a, b):
    if a == b:
        return "equal"
    elif a < b:
        return "right"
    else:
        return "wrong"
    
def compare_vals(a, b):
    for aval, bval in zip(a, b):
        if isinstance(aval, int) and isinstance(bval, int):
            result = compare_ints(aval, bval)
        
        elif isinstance(aval, list):
            if not isinstance(bval, list):
                bval = [bval]
            
            result = compare_vals(aval, bval)

        else:
            aval = [aval]
            result = compare_vals(aval, bval)

        if result == "right":
            return "right"
        elif result == "wrong":
            return "wrong"
        
    if len(a) == len(b):
        return "equal"
    elif len(a) < len(b):
        return "right"
    else:
        return "wrong"
     
def is_sorted(a, b):
    return compare_vals(a,b)!= "wrong"

for a, b in pairs:
    print(a)
    print(b)
    print(compare_vals(a, b))
    print()


scores = [i+1 if is_sorted(a, b)  else 0 for i, (a, b) in enumerate(pairs)]

print(f"Part 1: {sum(scores)}")

# ===============================
# PART 2
all_pairs = []
for a,b in pairs:
    all_pairs.append(a)
    all_pairs.append(b)
all_pairs.append([[2]])
all_pairs.append([[6]])

def bubble_sort(li):
    unsorted = True
    while unsorted:
        unsorted = False
        for i in range(0, len(li) - 1):
            a = li[i]
            b = li[i + 1]
            if not is_sorted(a, b):
                li[i] = b
                li[i + 1] = a
                unsorted = True

bubble_sort(all_pairs)


filtered_pairs = [(idx + 1, i) for idx, i in enumerate(all_pairs) if len(i) == 1]
scores = [idx for idx, i in filtered_pairs if i[0] == [2] or i[0] == [6]]
print(f"Part 2: {scores}, with product {scores[0] * scores[1]}")


