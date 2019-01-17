package hw4.puzzle;
import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {

    private int[][] board;
    //Constructs a board from an N-by-N array of tiles where
    //              tiles[i][j] = tile at row i, column j
    public Board(int[][] tiles){
        board = new int[tiles.length][tiles[1].length];
        for(int i=0; i<tiles.length; i++){
            for (int j=0; j<tiles[1].length; j++){
                board[i][j] = tiles[i][j];
            }
        }
    }

    //Returns value of tile at row i, column j (or 0 if blank)
    public int tileAt(int i, int j){
        if(i>=0 && i<board.length && j>=0 && j<board[1].length){
            return board[i][j];
        }else {throw new IndexOutOfBoundsException("row i and column j should be in [0, N) ");}
    }

    //Returns the board size N
    public int size(){
        return board.length;
    }

    //Returns the neighbors of the current board
    // code from http://joshh.ug/neighbors.html
    public Iterable<WorldState> neighbors(){
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }

    public int hamming(){
        int H = 0;
        for(int i=0; i<board.length; i++){
            for (int j=0; j<board[1].length; j++){
                if(board[i][j] != i*board.length + j + 1 && board[i][j] != 0){
                    H++;
                }
            }
        }
        return H;
    }

    public int manhattan(){
        int M = 0;
        for(int i=0; i<board.length; i++){
            for (int j=0; j<board[1].length; j++){
                if(board[i][j] !=0){
                    M += Math.abs((board[i][j]-1)/board.length -i) + Math.abs((board[i][j]-1)%board.length - j);
                }
            }
        }
        return M;
    }

    //Estimated distance to goal. This method should simply return the results of manhattan() when submitted to Gradescope.
    public int estimatedDistanceToGoal(){
        return manhattan();
    }

    // Returns true if this board's tile values are the same position as y's
    public boolean equals(Object y){
        if(y.getClass() != this.getClass() || y == null){
            return false;
        }
        int[][] Y = (int[][]) y;
        for(int i=0; i<board.length; i++){
            for (int j=0; j<board[1].length; j++){
                if(board[i][j] != Y[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    /** Returns the string representation of the board.*/
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}

