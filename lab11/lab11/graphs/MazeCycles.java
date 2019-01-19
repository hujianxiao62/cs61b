package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private Maze maze;
    private int[] edgeTo2; // used as edgeTo, whereas the original edgeTo is used only for draw the line of cycle
    Boolean cycleFound = false;
    private int cycleLastNode = -1;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
    }

    @Override
    public void solve() {
        edgeTo2 = new int[maze.V()];
        for(int i =0; i<edgeTo2.length; i++){
            edgeTo2[i] = edgeTo[i];
        }
        solveHelper(0);
    }

    public void solveHelper(int v) {
        marked[v] = true;
        announce();

        for (int w : maze.adj(v)) {
            if (marked[w]) {
                if (edgeTo2[v] != w) {
                    cycleFound = true;
                    cycleLastNode = v;
                    drawLine(cycleLastNode);
                    return;
                }
            } else {
                edgeTo2[w] = v;
                solveHelper(w);
                if (cycleFound) {
                    return;
                }
            }
        }
    }

    private void drawLine(int w){
        boolean neibor = false;
        while (!neibor || edgeTo2[cycleLastNode] == w){
            edgeTo[w] = edgeTo2[w];
            w = edgeTo[w];

            neibor = false;
            for(int ww:maze.adj(w)){
                if(ww == cycleLastNode){
                    neibor = true;
                    break;
                }
            }
        }
        edgeTo[w] = cycleLastNode;
        announce();
    }
}

