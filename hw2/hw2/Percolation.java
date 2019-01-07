package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private Boolean[][] grid;
    private WeightedQuickUnionUF PUnion;
    private int N_;
    private int openSite;
    private boolean isPercolates = false;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N){
        N_ = N;
        if (N <= 0 ){
            throw new IllegalArgumentException("N should be bigger than 0.");
        }else {
            grid = new Boolean[N][N];
            PUnion = new WeightedQuickUnionUF(N*N+1);
            for (int i = 0; i<=N; i++){
                for (int j = 0; j<=N; j++){
                    grid[i][j] = false;
                }
            }
        }
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col){
        if (row < 0 || col < 0){
            throw new IndexOutOfBoundsException("row and col should not be negative.");
        }else {
            grid[row-1][col-1] = true;
            openSite++;
            if(row == 1){
                PUnion.union(N_*N_+1,(row-1)*N_+col);
            }else if(row == N_ && this.checkAround(row,col)){
                PUnion.union(N_*N_+1,(row-1)*N_+col);
                isPercolates = true;
            }else if(this.checkAround(row,col)){
                PUnion.union(N_*N_+1,(row-1)*N_+col);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        if (row < 0 || col < 0){
            throw new IndexOutOfBoundsException("row and col should not be negative.");
        }else {
            return grid[row-1][col-1];}
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        if (row < 0 || col < 0){
            throw new IndexOutOfBoundsException("row and col should not be negative.");
        }else {
            return PUnion.connected(N_*N_+1,(row-1)*N_+col);}
    }

    // number of open sites
    public int numberOfOpenSites(){
        return openSite;
    }

    // does the system percolate?
    public boolean percolates(){
        return isPercolates;
    }

    private boolean checkAround(int row, int col){
        boolean P = false;
        try{
            if( PUnion.connected(N_*N_+1,row*N_+col) ||
                    PUnion.connected(N_*N_+1,(row-2)*N_+col) ||
                    PUnion.connected(N_*N_+1,(row-1)*N_+col-1) ||
                    PUnion.connected(N_*N_+1,(row-1)*N_+col+1)
            ){
                P = true;
            }else {P = false;}
        }
        catch (Exception e){}

        return P;
    }

}