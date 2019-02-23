package NQPlus;
import java.lang.Math;
import java.util.*;

/**
 * Places are value objects holding a (file,rank) tuple.
 */
class Place {
    public int file;
    public int rank;
    public Place(int x,int y) {
        file = x;
        rank = y;
    }
    /**
     * calculateGCD calculates the Greatest Common Denominator of its arguments
     */
    private static int calculateGCD(int m, int n) {
        // slow implementation, but this isn't a bottleneck
        if     (m>n) { return calculateGCD(m-n,n); }
        else if(m<n) { return calculateGCD(m,n-m); }
        else         { return m; }
    }
    /**
     * alignedPlaces generates the set of squares in subsequent files
     * which are aligned with this Place and the other Place.  These
     * places hence must be avoided by subsequent queens.
     */
    public Set<Place> alignedPlaces(Board b, Place other) {
        Set<Place> avoid = new HashSet<Place>();
        // find offset...
        int dx = file - other.file;
        int dy = rank - other.rank;

        // ...and reduce it to lowest terms to avoid skipping squares
        int gcd = calculateGCD(Math.abs(dx),Math.abs(dy));
        dx /= gcd;
        dy /= gcd;

        int u = file+dx;
        int v = rank+dy;
        // while we're still on the board
        while(u < b.n && v>=0 && v<b.n) {
            // avoid aligned squares
            avoid.add(new Place(u,v));
            u += dx;
            v += dy;
        }
        return avoid;
    }
    /**
     * We use toString() for output.  The algebraic notation runs out
     * of letters for larger boards, but we don't handle those in this
     * application, anyway.
     */
    public String toString() {
        // !! FIXME: notation for large boards
        return "ABCDEFGHIJKLMNOPQRSTUVWXYZ".substring(file,file+1) + (rank+1);
    }
}

/**
 * Boards are value objects holding the intermediate states of our algorithm.
 */
class Board {
    public int n;               // size of the board
    public List<Place> queens;  // queens placed so far
    public Set<Place> avoid;    // squares to avoid due to alignment
    /**
     * construct a blank Board of size n_
     */
    public Board(int n_) {
        n = n_;
        queens = new ArrayList<Place>();
        avoid  = new HashSet<Place>();
    }
    /**
     * construct a Board similar to b with the addition of latest
     */
    private Board(Board b,Place latest) {
        n = b.n;
        queens = new ArrayList<Place>();
        queens.addAll(b.queens);
        queens.add(latest);
        avoid = new HashSet<Place>();
        avoid.addAll(b.avoid);
        for( Place q : b.queens ) {
            avoid.addAll(latest.alignedPlaces(b,q));
        }
    }
    /**
     * isOK checks if a queen could be placed at (x,y)
     */
    private boolean isOK(int x, int y) {
        // check for attacks by existing queens
        for( Place q : queens ) {
            // assumption! we always place on a new file
	    // so no check for vertical attack
            if(q.rank == y) {
                return false; // horizontal attack
            }
            if(Math.abs(x - q.file) == Math.abs(y - q.rank)) {
                return false; // diagonal attack
            }
        }
        // check that we avoid alignment
        for( Place a : avoid ) {
            if(a.file == x && a.rank == y) { 
                return false;
            }
        }
        return true;
    }   
    /**
     * placeQueens recursively places queens on new files, from left to right
     */
    public List<List<Place>> placeQueens() {
        List<List<Place>> solutions = new ArrayList<List<Place>>();
        if(queens.size() == n) {
            // filled board produces a solution
            solutions.add(queens);
        } else {
            // otherwise solve for next file and recurse
            int x = queens.size();
            for(int y = 0; y < n; y++) {
                if(isOK(x,y)) {
                    Board b1 = new Board(this,new Place(x,y));
                    for(List<Place> solution : b1.placeQueens()) {
                        solutions.add(solution);
                    }
                }
            }
        }
        return solutions;
    }
}

/**
 * The App class implements an application that
 * calculates solutions to a modified N-Queens problem.
 * <PRE>
Place N queens on an NxN chess board so that none of them attack
each other (the classic n-queens problem). Additionally, please
make sure that no three queens are in a straight line at ANY angle,
so queens on A1, C2 and E3, despite not attacking each other, form
a straight line at some angle.
 * </PRE>
 * We limit N to 25 for the algebraic chess notation, but this is
 * no great loss, as we both use a cubic algorithm and don't stream
 * the solutions, hence N&gt;16 is already prohibitive to compute.
 */
public class App {
    public static void main(String[] args) {
        try {
            // default to standard chessboard
            int n = (args.length<1 ? 8 : Integer.valueOf(args[0]));
            if(n<0 || n>25) { throw new Exception(); }

            boolean solns = false;
            for(List<Place> solution : new Board(n).placeQueens()) {
                solns = true;
                System.out.println(solution);
            }
            if(!solns) {
                System.err.println("** no sol'ns **");
            }
        } catch(Exception e) {
            System.err.println("Usage: ./gradlew run --args <N>");
            System.err.println("     N must be in 0..25");
	    System.exit(1);
	}
    }
}
