input = """
1 2 0 0
1 3 0 0
1 4 0 0
1 5 0 0
1 6 0 0
1 7 0 0
1 8 0 0
1 9 0 0
1 10 0 0
1 11 0 0
1 12 0 0
1 13 0 0
1 14 0 0
1 15 0 0
1 16 0 0
2 17 3 0 3 18 19 20
1 21 1 0 17
1 1 2 1 22 21
2 23 3 0 3 18 22 20
1 24 1 0 23
1 1 2 1 19 24
2 25 3 0 3 18 22 19
1 26 1 0 25
1 1 2 1 20 26
2 27 3 0 3 22 19 20
1 28 1 0 27
1 1 2 1 18 28
2 29 3 0 2 22 19 20
1 30 1 0 29
1 1 2 1 30 18
2 31 3 0 2 18 19 20
1 32 1 0 31
1 1 2 1 32 22
2 33 3 0 2 18 22 20
1 34 1 0 33
1 1 2 1 34 19
2 35 3 0 2 18 22 19
1 36 1 0 35
1 1 2 1 36 20
1 37 1 0 18
1 37 1 0 19
1 38 1 0 22
1 38 1 0 20
1 39 1 0 37
1 40 1 0 39
1 41 1 0 18
1 42 1 0 41
1 43 2 0 40 42
1 44 1 0 18
1 44 1 0 19
1 45 1 0 22
1 45 1 0 20
1 46 1 0 44
1 47 1 0 46
1 48 1 0 19
1 49 1 0 18
1 50 2 1 49 48
1 51 2 0 47 50
1 52 1 0 18
1 52 1 0 19
1 53 1 0 22
1 53 1 0 20
1 54 1 0 52
1 55 1 0 54
1 56 1 0 18
1 56 1 0 19
1 57 2 1 56 58
1 59 2 0 55 57
1 60 1 0 18
1 60 1 0 19
1 61 1 0 22
1 61 1 0 20
1 62 1 0 61
1 63 1 0 60
1 64 2 1 63 62
1 65 1 0 22
1 66 1 0 65
1 67 2 0 64 66
1 68 1 0 18
1 68 1 0 19
1 69 1 0 22
1 69 1 0 20
1 70 1 0 69
1 71 1 0 68
1 72 2 1 71 70
1 73 1 0 20
1 74 1 0 22
1 75 2 1 74 73
1 76 2 0 72 75
1 77 1 0 18
1 77 1 0 19
1 78 1 0 22
1 78 1 0 20
1 79 1 0 78
1 80 1 0 77
1 81 2 1 80 79
1 82 1 0 22
1 82 1 0 20
1 83 2 1 82 84
1 85 2 0 81 83
1 67 2 0 43 22
1 51 2 0 43 19
1 76 2 0 43 20
1 43 2 0 51 18
1 67 2 0 51 22
1 76 2 0 51 20
1 51 2 0 59 19
1 76 2 0 59 20
1 43 2 0 67 18
1 51 2 0 67 19
1 76 2 0 67 20
1 43 2 0 76 18
1 67 2 0 76 22
1 51 2 0 76 19
1 51 2 0 85 19
1 76 2 0 85 20
1 1 2 1 43 18
1 1 2 1 67 22
1 1 2 1 51 19
1 1 2 1 76 20
3 4 18 22 19 20 0 0
1 86 0 0
1 87 0 0
1 88 0 0
1 89 0 0
1 90 0 0
1 91 0 0
1 92 0 0
1 93 0 0
1 94 0 0
1 95 0 0
1 96 0 0
1 97 0 0
6 0 16 4 18 22 19 20 86 87 88 89 90 91 92 93 94 95 96 97 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1
0
2 value(0)
5 value(1)
6 value(2)
7 value(3)
4 size(2)
3 step(1)
8 step(-1)
9 diff(1,0)
10 diff(-1,0)
11 diff(0,1)
12 diff(0,-1)
13 diff(1,1)
14 diff(-1,1)
15 diff(1,-1)
16 diff(-1,-1)
18 lives(1,1)
22 lives(2,1)
19 lives(1,2)
20 lives(2,2)
43 reached(1,1)
51 reached(1,2)
59 reached(1,3)
67 reached(2,1)
76 reached(2,2)
85 reached(2,3)
0
B+
0
B-
1
0
1
"""
output = """
COST 12@1
"""
