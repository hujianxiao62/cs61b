package lab11.graphs;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;
    private PriorityQueue<Integer> Q;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int s) {
        marked[s] = true;
        announce();
        if (s == t) {
            targetFound = true;
            return;
        }
        Q = new PriorityQueue<>(new AstarComparator());
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
        astar(s);
    }

    // Overriding compare()method of for PQ
    class AstarComparator implements Comparator<Integer> {
        @Override
        public int compare(Integer o1, Integer o2) {
            if (distTo[o1] + h(o1) < distTo[o2] + h(o2))
                return -1;
            else if (distTo[o1] + h(o1) > distTo[o2] + h(o2))
                return 1;
            return 0;
        }
    }

}

