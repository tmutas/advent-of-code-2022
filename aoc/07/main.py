import sys

with open(sys.argv[1]) as fl:
    input = fl.read().split("\n")

dirs = {}
curdirnamestack = []

for line in input:
    e = line.split(" ")
    if e[0] == "dir":
        dirs[curdirname]["dirs"].append("/".join(curdirnamestack + [e[1]]))
    elif (e[1] == "ls") and (curdirname not in dirs):
        dirs[curdirname] = {'files' : [], "dirs" : []}
    elif e[1] == "cd" :
        if e[2] == "..":
            curdirnamestack.pop()
            curdirname = "/".join(curdirnamestack)
        else:
            curdirnamestack.append(e[2])
            curdirname = "/".join(curdirnamestack)
    elif e[0].isnumeric():
        dirs[curdirname]["files"].append(int(e[0]))
    else:
        pass

sizes = {}

def calc_size(i):
    s = sum(j for j in dirs[i]['files'])
    s += sum(sizes.get(j, calc_size(j)) for j in dirs[i]["dirs"])
    sizes[i] = s
    return s

print("Part 1:")
print(sum(map(lambda x: x if x <= 100000 else 0, (calc_size(i) for i in dirs))))
print("")
print("Part 2:")

min_size = sizes["/"] - 40000000 

sorted_sizes = sorted(sizes.values())


sorted_sizes = [i for i in sorted_sizes if i > min_size]
print(sorted_sizes)
print(sorted_sizes[0])
