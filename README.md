jwasp
====
JWASP is an ASP solver for normal logic program based on the well-known SAT/Pseudo-Boolean solver SAT4J (http://www.sat4j.org/).
JWASP extends SAT4J by implementing an algorithm based on **source pointers** for the unfounded sets computation.

At the moment, the grounder is still missing, thus JWASP takes as input logic programs in the GRINGO format.
In future the idea is to extend JWASP in order to take as input a non-ground ASP program.

Usage
===
In order to use JWASP you need the grounder GRINGO (http://potassco.sourceforge.net/).

If gringo has been downloaded just type:
```
./gringo filename | java -jar jwasp.jar
```

Tests
===
For tests, we used the test environment of WASP (https://github.com/alviano/wasp.git).
In order to test the solver just type:
```
make test
```
