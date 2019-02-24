package nqplus;
import java.util.*;
import nqplus.Place;

/**
 * Boards are value objects holding the intermediate states of our algorithm.
 */
public class Board {
    /** size of the board */
    public int n;
    /** queens placed so far */
    public List<Place> queens; 
    /** squares to avoid due to alignment */
    public Set<Place> avoid;

    /**
     * construct a blank Board
     *
     * @param n size of Board
     */
    public Board(int n) {
        this.n = n;
        queens = new ArrayList<Place>();
        avoid  = new HashSet<Place>();
    }
    /**
     * construct a Board similar to b with the addition of latest
     *
     * @param b      previous board state
     * @param latest new queen position
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
     * isOK checks possible queen placement
     *
     * @param x file to check
     * @param y rank to check
     * @return if a queen could be placed at (x,y)
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
        return !avoid.contains(new Place(x,y));
    }   
    /**
     * placeQueens recursively places queens on new files, from left to right
     *
     * @return solutions on this branch
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
