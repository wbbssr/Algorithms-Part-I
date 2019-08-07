import java.lang.*;
import java.util.*;
import edu.princeton.cs.algs4.*;

public class Board {
    private int[][] board;
    private int dimension;
    private int hamming = 0;
    private int manhattan = 0;
    // x coordinate and y coordinate of number zero
    private int x_zero;
    private int y_zero;

    public Board(int[][] blocks) {           // construct a board from an n-by-n array of blocks
                                             // (where blocks[i][j] = block in row i, column j)
        if (blocks == null)
            throw new java.lang.IllegalArgumentException();

        dimension = blocks.length;
        board = new int[dimension][dimension];

        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++) {
                board[i][j] = blocks[i][j];

                if (0 == blocks[i][j]) {
                    x_zero = i;
                    y_zero = j;
                }
            }
        // calculate hamming and manhattan
        
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (0 == board[i][j])
                    continue;
                else {
                    // calculate hamming
                    if ((i * dimension + j + 1) != board[i][j])
                        hamming++;

                    // calculate manhattan
                    if ((board[i][j] - 1) / dimension - i >= 0)
                        manhattan += ((board[i][j] - 1) / dimension - i);
                    else
                        manhattan += (i - (board[i][j] - 1) / dimension);

                    if ((board[i][j] - 1) % dimension - j >= 0)
                        manhattan += ((board[i][j] - 1) % dimension - j);
                    else
                        manhattan += (j - (board[i][j] - 1) % dimension);   
                }
            }
        }
    }
    public int dimension() {                 // board dimension n
        return dimension;
    }
    public int hamming() {                   // number of blocks out of place
        return hamming;
    }
    public int manhattan() {                 // sum of Manhattan distances between blocks and goal
        return manhattan;
    }
    public boolean isGoal() {                // is this board the goal board?
        return 0 == manhattan;
    }
    public Board twin() {                    // a board that is obtained by exchanging any pair of blocks
        int[][] boardCopy = new int[dimension][dimension];
        int temp;

        for (int i = 0; i < dimension; i++)
            boardCopy[i] = board[i].clone();

        if (0 == boardCopy[0][0] || 0 == boardCopy[0][1]) {
            temp = boardCopy[1][0];
            boardCopy[1][0] = boardCopy[1][1];
            boardCopy[1][1] = temp;
        }
        else {
            temp = boardCopy[0][0];
            boardCopy[0][0] = boardCopy[0][1];
            boardCopy[0][1] = temp;
        }

        return new Board(boardCopy);
    }
    public boolean equals(Object y) {        // does this board equal y?
        if (y == null)
            return false;
        // why board.getClass() is not equals to this.getClass()
        if (y.getClass() != this.getClass())
            return false;

        Board that = (Board) y;

        if (dimension != that.dimension() || hamming != that.hamming() || manhattan != that.manhattan())
            return false;

        for (int i = 0; i < dimension; i++)
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] != that.board[i][j])
                    return false;
            }

        return true;
    }
    public Iterable<Board> neighbors() {     // all neighboring boards
        edu.princeton.cs.algs4.Stack<Board> neighborBoards = new edu.princeton.cs.algs4.Stack<Board>();

        if (x_zero > 0) {
            int[][] a = swap(board, x_zero, y_zero, x_zero - 1, y_zero);
            Board neighborBoard = new Board(a);
            neighborBoards.push(neighborBoard);
        }

        if (x_zero < dimension - 1) {
            int[][] a = swap(board, x_zero, y_zero, x_zero + 1, y_zero);
            Board neighborBoard = new Board(a);
            neighborBoards.push(neighborBoard);
        }

        if (y_zero > 0) {
            int[][] a = swap(board, x_zero, y_zero, x_zero, y_zero - 1);
            Board neighborBoard = new Board(a);
            neighborBoards.push(neighborBoard);
        }

        if (y_zero < dimension - 1) {
            int[][] a = swap(board, x_zero, y_zero, x_zero, y_zero + 1);
            Board neighborBoard = new Board(a);
            neighborBoards.push(neighborBoard);
        }

        return neighborBoards;
    }

    private int[][] swap(int[][] src, int x_1, int y_1, int x_2, int y_2) {
        int[][] result = new int[dimension][dimension];
        int temp;

        for (int i = 0; i < dimension; i++) {
            result[i] = src[i].clone();
        }

        temp = result[x_1][y_1];
        result[x_1][y_1] = result[x_2][y_2];
        result[x_2][y_2] = temp;

        return result;
    }
    public String toString() {               // string representation of this board (in the output format specified below)
        StringBuilder result = new StringBuilder();
        result.append(dimension);
        result.append("\n");

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                result.append(String.format("%2d", board[i][j]));
                result.append(" ");
            }

            result = result.append("\n");
        }

        return result.toString();
    }

    public static void main(String[] args) { // unit tests (not graded)
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        StdOut.println(initial.equals(initial));

    } 
}
