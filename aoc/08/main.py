import sys
from itertools import takewhile

with open(sys.argv[1]) as fl:
    input = fl.read().split("\n")

grid = [[int(i) for i in j] for j in input if j != '']
lx = range(1, len(grid) -1)
ly = range(1, len(grid[0]) -1)
vis = []
for i in lx: 
    for j in ly: 
        if (grid[i][j] > min(
            max(grid[i][:j]),
            max(grid[i][j+1:]),
            max(v[j] for v in grid[:i]), 
            max(v[j] for v in grid[i+1:]), 

        )):
            vis.append((i,j))
print("Part 1")
print(len(vis) + 2*len(grid) + 2*len(grid[0]) - 4)
print("Part 2")

scores = {}
lx = range(0, len(grid))
ly = range(0, len(grid[0]))
for i in lx: 
    for j in ly: 
        f = lambda x: x < grid[i][j]
        s1_list = grid[i][:j:-1]
        s2_list = grid[i][:j]
        
        s1 =min(len(list(takewhile(f,grid[i][j+1:]))) + 1, len(grid[i][j+1:])) 
        s2 =min(len(list(takewhile(f,grid[i][:j][::-1]))) + 1, len(grid[i][:j][::-1]) ) 
        s3 =min(len(list(takewhile(f,(v[j] for v in grid[:i][::-1])))) + 1, len(grid[:i][::-1]) )
        s4 =min(len(list(takewhile(f,(v[j] for v in grid[i+1:])))) + 1,  len(grid[i+1:]) )
        print(f"({i},{j}) : {s1} {s2} {s3} {s4}")
        scores[(i,j)] = (s1 * s2 * s3 * s4)

print(max(scores.values()))