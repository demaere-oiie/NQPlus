package nqplus;
import java.lang.Math;
import java.util.*;

/**
 * Places are value objects holding a (file,rank) tuple.
 */
public class Place {
    private int file;
    private int rank;
    public Place(int x,int y) {
        file = x;
        rank = y;
    }
    /**
     * attacks determines if this queen attacks a given square
     */
    public boolean attacks(int x,int y) {
        // assumption! we always place queens on a new file
        // so no check for vertical attack
        if(rank == y) {
            return true; // horizontal attack
        }
        if(Math.abs(x - file) == Math.abs(y - rank)) {
            return true; // diagonal attack
        }
	return false;
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
     *
     * @param n     size of the current board
     * @param other square to align with
     * @return set of Places to be avoided
     */
    public Set<Place> alignedPlaces(int n, Place other) {
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
        while(u<n && v>=0 && v<n) {
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
    /** Override equals for value semantics */
    public boolean equals(Object other) {
	if(other instanceof Place)	{
	    Place p = (Place)other;
	    return this.file == p.file && this.rank == p.rank;
	}
	return false;
    }
    /** Override hashCode for value semantics */
    public int hashCode() {
	return 31*file + rank;
    }
}
