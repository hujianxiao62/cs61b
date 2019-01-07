package hw2;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;
import java.lang.Math;

import java.lang.reflect.Array;
import java.util.Arrays;

public class PercolationStats {
    private double[] results;
    private int gridNumber;
    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("N and T should be bigger than 0.");
        } else {
            Percolation testPf = pf.make(N);
            results = new double[T];
            gridNumber = N * N;
            for (int i = 0; i < T; i++) {
                while (!testPf.percolates()) {
                    testPf.open(StdRandom.uniform(N) + 1, StdRandom.uniform(N) + 1);
                }
                results[i] = testPf.numberOfOpenSites()/gridNumber;
            }
        }
    }

    // sample mean of percolation threshold
    public double mean()   {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev()    {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow()     {
        return mean()-1.96*stddev()/Math.sqrt(results.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh()      {
        return mean()+1.96*stddev()/Math.sqrt(results.length);
    }
}