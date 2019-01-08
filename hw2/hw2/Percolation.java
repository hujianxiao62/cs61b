package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private Boolean[][] grid;
    private WeightedQuickUnionUF PUnion;
    private WeightedQuickUnionUF FUnion;
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
            PUnion = new WeightedQuickUnionUF(N*N+2);
            FUnion = new WeightedQuickUnionUF(N*N+2);
            for (int i = 0; i<N; i++){
                for (int j = 0; j<N; j++){
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
            grid[row][col] = true;
            openSite++;
            if(row == 0){
                this.checkAround(row,col);
                PUnion.union(N_*N_,row*N_+col);
                FUnion.union(N_*N_,row*N_+col);
            }else if(row == N_-1){
                this.checkAround(row,col);
                PUnion.union(N_*N_+1,row*N_+col);
            }
            else{
                 this.checkAround(row,col);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        if (row < 0 || col < 0){
            throw new IndexOutOfBoundsException("row and col should not be negative.");
        }else {
            return grid[row][col];}
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        if (row < 0 || col < 0){
            throw new IndexOutOfBoundsException("row and col should not be negative.");
        }else {
            return FUnion.connected(N_*N_,row*N_+col);}
    }

    // number of open sites
    public int numberOfOpenSites(){
        return openSite;
    }

    // does the system percolate?
    public boolean percolates(){
        return PUnion.connected(N_*N_,N_*N_+1);
    }

    private void checkAround(int row, int col){
        try{
            if( this.isOpen(row+1,col)){
                PUnion.union((row+1)*N_+col,row*N_+col);
                FUnion.union((row+1)*N_+col,row*N_+col);
            }}catch (Exception e){}
        try{
            if(this.isOpen(row-1,col)){
                PUnion.union((row-1)*N_+col,row*N_+col);
                FUnion.union((row-1)*N_+col,row*N_+col);
            }}catch (Exception e){}
        try{
            if(this.isOpen(row,col+1)){
                PUnion.union(row*N_+col+1,row*N_+col);
                FUnion.union(row*N_+col+1,row*N_+col);
            }}catch (Exception e){}
        try{
            if(this.isOpen(row,col-1)){
                PUnion.union(row*N_+col-1,row*N_+col);
                FUnion.union(row*N_+col-1,row*N_+col);
            }}catch (Exception e){}
    }

}