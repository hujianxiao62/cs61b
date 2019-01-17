package hw4.puzzle;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Iterator;

public class Solver {
    private MinPQ PQ;
    private Stack<WorldState> Solu;
    private int movs;
    /*Constructor which solves the puzzle, computing everything necessary for moves() and solution() to
      not have to solve the problem again. Solves the puzzle using the A* algorithm. Assumes a solution exists.*/
    public Solver(WorldState initial){
        PQ = new MinPQ();
        Solu = new Stack<>();
        PQ.insert(new SearchNode(initial,0,null));
        SearchNode minNode = (SearchNode) PQ.delMin();
        while (!minNode.WS.isGoal()){
            Iterator<WorldState> E = minNode.WS.neighbors().iterator();
            while (E.hasNext()){
                PQ.insert(new SearchNode(E.next(),minNode.moves+1,minNode));
            }
            minNode = (SearchNode) PQ.delMin();
        }
            movs = minNode.moves;
            Solu.push(minNode.WS);
            while (minNode.prev != null){
                Solu.push(minNode.prev.WS);
                minNode = minNode.prev;
            }

    }

    private class SearchNode implements Comparable<SearchNode>{
        private WorldState WS;
        private int moves;
        private SearchNode prev;

        public SearchNode(WorldState WS_, int movs_, SearchNode prev_){
            WS = WS_;
            moves = movs_;
            prev = prev_;
        }

        @Override
        public int compareTo(SearchNode o) {
            if (this.moves + this.WS.estimatedDistanceToGoal() > o.moves + o.WS.estimatedDistanceToGoal()) return 1;
            else if (this.moves + this.WS.estimatedDistanceToGoal() == o.moves + o.WS.estimatedDistanceToGoal()) return 0;
            else return -1;
        }
    }

    /*Returns the minimum number of moves to solve the puzzle starting
                 at the initial WorldState.*/
    public int moves(){
        return movs;
    }

    /*Returns a sequence of WorldStates from the initial WorldState
                 to the solution.*/
    public Iterable<WorldState> solution(){
        return Solu;
    }
}
