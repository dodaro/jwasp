input = """
1 2 2 1 3 4
1 3 2 1 2 4
1 4 0 0
1 5 2 1 6 7
1 6 2 1 5 7
1 7 0 0
2 9 4 0 2 3 2 6 5
2 10 4 0 1 3 2 6 5
1 8 2 1 9 10
0
8 okay
5 b(1)
6 b(2)
2 a(1)
3 a(2)
0
B+
0
B-
1
0
1
"""
output = """
{a(1), b(1)}
{a(1), b(2)}
{a(2), b(1)}
{a(2), b(2)}
"""
