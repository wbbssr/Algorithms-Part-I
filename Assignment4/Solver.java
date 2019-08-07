import java.lang.*;
import java.util.*;
import edu.princeton.cs.algs4.*;

public class Solver {
    private int moves = 0;
    private Board initial;
    private Board initialTwin;
    private SearchNode temp;
    private LinkedStack<Board> stack = new LinkedStack<Board>();
    //private LinkedQueue<Board> que_ini = new LinkedQueue<Board>();
    //private LinkedQueue<Board> que_ini_twin = new LinkedQueue<Board>();

    public Solver(Board initial) {                    // find a solution to the initial board (using the A* algorithm)
        Comparator<SearchNode> BY_MANHATTAN = new ByManhattan();
        MinPQ<SearchNode> PQ_ini = new MinPQ<SearchNode>(BY_MANHATTAN);
        MinPQ<SearchNode> PQ_ini_twin = new MinPQ<SearchNode>(BY_MANHATTAN);

        if (initial == null)
            throw new java.lang.IllegalArgumentException();

        //Board initial;
        //Board initialTwin;

        this.initial = initial;
        initialTwin = this.initial.twin();

        SearchNode prev = new SearchNode(this.initial);
        SearchNode prev_twin = new SearchNode(initialTwin);

        PQ_ini.insert(prev);
        PQ_ini_twin.insert(prev_twin);

        while (true) {
            temp = PQ_ini.delMin();
            SearchNode temp_twin = PQ_ini_twin.delMin();

            if (temp.getBoard().isGoal()) {
                moves = temp.getMoves();
                break;
            }

            if (temp_twin.getBoard().isGoal()) {
                moves = - 1;
                break;
            }

            for (Board b : temp.getBoard().neighbors()) {
                if (temp.getPrev() != null && b.equals(temp.getPrev().getBoard()))
                    continue;
                PQ_ini.insert(new SearchNode(b, temp.getMoves() + 1, temp));
            }

            for (Board b : temp_twin.getBoard().neighbors()) {
                if (temp_twin.getPrev() != null && b.equals(temp_twin.getPrev().getBoard()))
                    continue;
                PQ_ini_twin.insert(new SearchNode(b, temp_twin.getMoves() + 1, temp_twin));
            }
        }

        while (temp != null) {
            stack.push(temp.getBoard());
            temp = temp.getPrev();
        }        

    }

    public boolean isSolvable() {                     // is the initial board solvable?
        return moves >= 0;
    }

    public int moves() {                              // min number of moves to solve initial board; -1 if unsolvable
        return moves;
    }

    public Iterable<Board> solution() {               // sequence of boards in a shortest solution; null if unsolvable
        if (isSolvable()) {
            return stack;
        }
        else
            return null;
    }

    private class SearchNode {
        private Board board;
        private int moves;
        private SearchNode prev;

        public SearchNode(Board board) {
            this.board = board;
            moves = 0;
            prev = null;
        }

        public SearchNode(Board board, int moves, SearchNode prev) {
            this.board = board;
            this.moves = moves;
            this.prev = prev;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public void setMoves(int moves) {
            this.moves = moves;
        }

        public SearchNode getPrev() {
            return prev;
        }
    }

    private static class ByManhattan implements Comparator<SearchNode> {
        public int compare(SearchNode v, SearchNode w) {
            return v.getBoard().manhattan() - w.getBoard().manhattan() + v.getMoves() - w.getMoves();
        }
    }

    public static void main(String[] args) {          // solve a slider puzzle (given below)

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        StdOut.println(solver.moves());
        StdOut.println(solver.moves());
        StdOut.println(solver.isSolvable());

        for (Board board : solver.solution())
            StdOut.println(board.toString());

        //for (Board board : solver.solution())
        //    StdOut.println(board.toString());
        //    
        StdOut.println(solver.moves());

        // print solution to standard output
        //if (!solver.isSolvable())
        //    StdOut.println("No solution possible");
        //else {
        //    StdOut.println("Minimum number of moves = " + solver.moves());

        //}
    }
}