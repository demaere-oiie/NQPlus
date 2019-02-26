# NQPlus
## N-Queens with a twist

> Place N queens on an NxN chess board so that none of them attack
> each other (the classic n-queens problem). Additionally, please
> make sure that no three queens are in a straight line at ANY angle,
> so queens on A1, C2 and E3, despite not attacking each other, form
> a straight line at some angle.

We limit N
to between 0 and 25
for the algebraic chess [notation](https://en.wikipedia.org/wiki/Algebraic_notation_(chess)),
but this is no great loss,
as the complexity of the algorithm
and the lack of streaming for the solutions
mean N>16 is already
prohibitive to compute.

### About the algorithm

Recursive backtracking.
Several parts of the program
assume that we place queens 
one file at a time,
in order,
from left to right.
Queen attacking is handled
by simple predicates;
the alignment constraint
is handled by blacklisting.

### Usage
For example,
`./gradlew run --args 8`
to get solutions
for a normal chessboard.
