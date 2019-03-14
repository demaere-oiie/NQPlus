package nqplus;
import java.util.*;
import nqplus.*;

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
 * no great loss, as we use an exponential algorithm and don't stream
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
