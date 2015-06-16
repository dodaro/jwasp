input = """
1 2 0 0
1 3 0 0
1 4 0 0
1 5 0 0
1 6 2 1 7 8
1 7 2 1 6 8
1 8 0 0
1 9 2 1 10 11
1 10 2 1 9 11
1 11 0 0
1 12 2 1 13 14
1 13 2 1 12 14
1 14 0 0
5 16 6 3 0 12 9 6 1 2 3
1 15 1 0 16
5 18 5 3 0 12 9 6 1 2 3
5 19 6 3 0 12 9 6 1 2 3
1 17 2 1 19 18
1 20 0 0
1 21 0 0
1 22 0 0
5 24 6 3 0 12 9 6 1 2 3
1 23 1 1 24
5 26 6 3 0 12 9 6 1 2 3
1 25 1 0 26
5 28 6 3 0 12 9 6 1 2 3
1 27 1 0 28
5 30 6 3 0 12 9 6 1 2 3
1 29 1 0 30
5 32 5 3 0 12 9 6 1 2 3
5 33 6 3 0 12 9 6 1 2 3
1 31 2 1 33 32
1 34 0 0
1 35 0 0
1 36 0 0
5 38 6 3 0 12 9 6 1 2 3
1 37 1 1 38
5 40 6 3 0 12 9 6 1 2 3
1 39 1 0 40
5 42 6 3 0 12 9 6 1 2 3
1 41 1 0 42
5 44 6 3 0 12 9 6 1 2 3
1 43 1 0 44
5 46 5 3 0 12 9 6 1 2 3
5 47 6 3 0 12 9 6 1 2 3
1 45 2 1 47 46
1 48 0 0
1 49 0 0
1 50 0 0
5 52 6 3 0 12 9 6 1 2 3
1 51 1 1 52
5 54 6 3 0 12 9 6 1 2 3
1 53 1 0 54
5 56 6 3 0 12 9 6 1 2 3
1 55 1 0 56
5 58 6 3 0 12 9 6 1 2 3
1 57 1 0 58
5 60 5 3 0 12 9 6 1 2 3
5 61 6 3 0 12 9 6 1 2 3
1 59 2 1 61 60
5 63 6 3 0 12 9 6 1 2 3
1 62 1 0 63
5 65 6 3 0 12 9 6 1 2 3
1 64 1 0 65
1 66 0 0
1 67 0 0
1 68 0 0
5 70 6 3 0 12 9 6 1 2 3
1 69 1 1 70
5 72 6 3 0 12 9 6 1 2 3
1 71 1 0 72
5 74 5 3 0 12 9 6 1 2 3
5 75 6 3 0 12 9 6 1 2 3
1 73 2 1 75 74
5 77 6 3 0 12 9 6 1 2 3
1 76 1 0 77
5 79 6 3 0 12 9 6 1 2 3
1 78 1 0 79
1 80 0 0
5 82 6 3 0 12 9 6 1 2 3
1 81 1 1 82
1 83 0 0
5 85 6 3 0 12 9 6 1 2 3
1 84 1 1 85
5 87 6 3 0 12 9 6 1 2 3
1 86 1 0 87
5 89 5 3 0 12 9 6 1 2 3
5 90 6 3 0 12 9 6 1 2 3
1 88 2 1 90 89
5 92 6 3 0 12 9 6 1 2 3
1 91 1 0 92
5 94 6 3 0 12 9 6 1 2 3
1 93 1 0 94
1 95 0 0
5 97 6 3 0 12 9 6 1 2 3
1 96 1 1 97
1 98 0 0
5 100 6 3 0 12 9 6 1 2 3
1 99 1 1 100
5 102 4 3 0 12 9 6 1 2 3
1 101 1 0 102
5 104 4 3 0 12 9 6 1 2 3
5 105 6 3 0 12 9 6 1 2 3
1 103 2 1 105 104
5 106 5 3 0 12 9 6 1 2 3
1 103 1 0 106
5 108 4 3 0 12 9 6 1 2 3
1 107 1 0 108
5 110 4 3 0 12 9 6 1 2 3
5 111 6 3 0 12 9 6 1 2 3
1 109 2 1 111 110
5 112 5 3 0 12 9 6 1 2 3
1 109 1 0 112
5 114 4 3 0 12 9 6 1 2 3
1 113 1 0 114
5 116 4 3 0 12 9 6 1 2 3
5 117 6 3 0 12 9 6 1 2 3
1 115 2 1 117 116
5 118 5 3 0 12 9 6 1 2 3
1 115 1 0 118
5 120 4 3 0 12 9 6 1 2 3
1 119 1 0 120
5 122 5 3 0 12 9 6 1 2 3
1 121 1 0 122
5 124 4 3 0 12 9 6 1 2 3
1 123 1 0 124
5 126 5 3 0 12 9 6 1 2 3
1 125 1 0 126
5 128 4 3 0 12 9 6 1 2 3
1 127 1 0 128
5 130 5 3 0 12 9 6 1 2 3
1 129 1 0 130
5 132 6 3 0 12 9 6 1 2 3
1 131 1 0 132
5 134 5 3 0 12 9 6 1 2 3
5 135 6 3 0 12 9 6 1 2 3
1 133 2 1 135 134
5 137 6 3 0 12 9 6 1 2 3
1 136 1 0 137
5 139 5 3 0 12 9 6 1 2 3
5 140 6 3 0 12 9 6 1 2 3
1 138 2 1 140 139
5 142 5 3 0 12 9 6 1 2 3
1 141 1 0 142
5 144 4 3 0 12 9 6 1 2 3
5 145 6 3 0 12 9 6 1 2 3
1 143 2 1 145 144
5 147 6 3 0 12 9 6 1 2 3
1 146 1 0 147
5 149 6 3 0 12 9 6 1 2 3
1 148 1 0 149
5 151 5 3 0 12 9 6 1 2 3
5 152 6 3 0 12 9 6 1 2 3
1 150 2 1 152 151
5 154 5 3 0 12 9 6 1 2 3
1 153 1 0 154
5 156 4 3 0 12 9 6 1 2 3
5 157 6 3 0 12 9 6 1 2 3
1 155 2 1 157 156
5 159 6 3 0 12 9 6 1 2 3
1 158 1 0 159
5 161 6 3 0 12 9 6 1 2 3
1 160 1 0 161
5 163 5 3 0 12 9 6 1 2 3
5 164 6 3 0 12 9 6 1 2 3
1 162 2 1 164 163
5 166 5 3 0 12 9 6 1 2 3
1 165 1 0 166
5 168 4 3 0 12 9 6 1 2 3
5 169 6 3 0 12 9 6 1 2 3
1 167 2 1 169 168
5 171 6 3 0 12 9 6 1 2 3
1 170 1 0 171
5 173 6 3 0 12 9 6 1 2 3
1 172 1 0 173
5 175 5 3 0 12 9 6 1 2 3
5 176 6 3 0 12 9 6 1 2 3
1 174 2 1 176 175
0
69 ouch20
73 ouch21
59 ouch16
53 okay14
67 ouch19
125 ouch36
3 c(1)
4 c(2)
5 c(3)
143 ouch40
138 ouch39
115 ouch34
109 ouch33
121 ouch35
133 ouch38
55 okay15
48 okay12
50 okay13
98 okay31
25 okay4
129 ouch37
27 okay5
29 okay6
101 okay32
93 okay29
95 okay30
80 okay24
34 okay7
86 okay26
91 okay28
83 okay25
158 okay44
36 okay8
41 okay10
153 okay43
43 okay11
2 p
39 okay9
148 okay42
146 okay41
165 okay46
20 okay2
22 okay3
160 okay45
68 okay20
71 okay21
57 okay16
76 okay22
6 d(3)
9 d(2)
12 d(1)
62 okay17
78 okay23
7 n_d(3)
10 n_d(2)
13 n_d(1)
64 okay18
66 okay19
123 okay36
141 okay40
136 okay39
119 okay35
113 okay34
49 ouch12
107 okay33
51 ouch13
131 okay38
99 ouch31
127 okay37
31 ouch6
96 ouch30
103 ouch32
81 ouch24
35 ouch7
84 ouch25
88 ouch27
172 okay48
170 okay47
37 ouch8
155 ouch43
15 okay1
174 ouch48
45 ouch11
150 ouch42
17 ouch1
167 ouch46
162 ouch45
21 ouch2
23 ouch3
0
B+
0
B-
1
0
1
"""
output = """
{p, c(1), c(2), c(3), d(3), d(2), d(1), okay1, okay2, ouch2, okay3, okay4, okay5, okay6, okay7, ouch7, okay8, okay9, okay10, okay11, okay12, ouch12, okay13, okay14, okay15, okay16, okay17, okay18, okay19, ouch19, okay20, okay21, okay22, okay23, okay24, okay25, okay26, okay28, okay29, okay30, okay31, okay32, ouch32, okay33, ouch33, okay34, ouch34, okay35, ouch35, okay36, ouch36, okay37, ouch37, okay38, okay39, okay40, okay41, okay42, okay43, okay44, okay45, okay46, okay47, okay48}
{p, c(1), c(2), c(3), d(3), d(2), n_d(1), ouch1, okay2, ouch2, okay3, ouch3, ouch6, okay7, ouch7, okay8, ouch8, ouch11, okay12, ouch12, okay13, ouch13, ouch16, okay19, ouch19, okay20, ouch20, ouch21, okay24, ouch24, okay25, ouch25, ouch27, okay30, ouch30, okay31, ouch31, okay32, ouch32, okay33, ouch33, okay34, ouch34, okay35, ouch35, okay36, ouch36, okay37, ouch37, ouch38, ouch39, okay40, ouch40, ouch42, okay43, ouch43, ouch45, okay46, ouch46, ouch48}
{p, c(1), c(2), c(3), d(3), n_d(2), d(1), okay2, ouch2, okay3, ouch3, okay7, ouch7, okay8, ouch8, okay12, ouch12, okay13, ouch13, okay19, ouch19, okay20, ouch20, okay24, ouch24, okay25, ouch25, okay30, ouch30, okay31, ouch31, okay32, ouch32, okay33, ouch33, okay34, ouch34, okay35, okay36, okay37, ouch40, ouch43, ouch46}
{p, c(1), c(2), c(3), d(3), n_d(2), n_d(1), okay2, ouch2, okay3, ouch3, okay7, ouch7, okay8, ouch8, okay12, ouch12, okay13, ouch13, okay19, ouch19, okay20, ouch20, okay24, ouch24, okay25, ouch25, okay30, ouch30, okay31, ouch31}
{p, c(1), c(2), c(3), n_d(3), d(2), d(1), okay2, ouch2, okay3, ouch3, okay7, ouch7, okay8, ouch8, okay12, ouch12, okay13, ouch13, okay19, ouch19, okay20, ouch20, okay24, ouch24, okay25, ouch25, okay30, ouch30, okay31, ouch31}
{p, c(1), c(2), c(3), n_d(3), d(2), n_d(1), okay2, ouch2, okay3, ouch3, okay7, ouch7, okay8, ouch8, okay12, ouch12, okay13, ouch13, okay19, ouch19, okay20, ouch20, okay24, ouch24, okay25, ouch25, okay30, ouch30, okay31, ouch31}
{p, c(1), c(2), c(3), n_d(3), n_d(2), d(1), okay2, ouch2, okay3, ouch3, okay7, ouch7, okay8, ouch8, okay12, ouch12, okay13, ouch13, okay19, ouch19, okay20, ouch20, okay24, ouch24, okay25, ouch25, okay30, ouch30, okay31, ouch31}
{p, c(1), c(2), c(3), n_d(3), n_d(2), n_d(1), okay2, ouch2, okay3, ouch3, okay7, ouch7, okay8, ouch8, okay12, ouch12, okay13, ouch13, okay19, ouch19, okay20, ouch20, okay24, ouch24, okay25, ouch25, okay30, ouch30, okay31, ouch31}
"""
