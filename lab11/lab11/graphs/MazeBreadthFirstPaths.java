package lab11.graphs;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *  @author Josh Hug
 */
public class MazeBreadthFirstPaths extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private PriorityQueue<Integer> Q;

    public MazeBreadthFirstPaths(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Conducts a breadth first search of the maze starting at the source. */
    private void bfs() {
        marked[s] = true;
        announce();
        if (s == t) {
            targetFound = true;
            return;
        }
        Q = new PriorityQueue<>(new BreadthComparator());
        Q.add(s);
        while (Q.element() != t){
            int v = Q.remove();
            Iterator<Integer> E = maze.adj(v).iterator();
            while (E.hasNext()){
                int N = E.next();
                if(!marked[N]){
                    Q.add(N);
                    marked[N] = true;
                    edgeTo[N] = v;
                    announce();
                    distTo[N] = distTo[v] + 1;
                    if(N == t){
                        targetFound = true;
                        announce();
                        return;
                    }
                }
            }
        }
    }


    @Override
    public void solve() {
         bfs();
    }

    // Overriding compare()method of for PQ
    class BreadthComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            if (distTo[o1] < distTo[o2])
                return -1;
            else if (distTo[o1] > distTo[o2])
                return 1;
            return 0;
        }
    }
}

