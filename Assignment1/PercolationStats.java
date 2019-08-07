import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
private Percolation percol;
private double[] numbesOfOpenSitesToPer;
private int numberOfTrials;
private int side;
private double mean;
private double stddev;


	public PercolationStats(int n, int trials) { // perform trials independent experiments on an n-by-n grid
		int i;

		if (n <= 0 || trials <= 0)
			throw new java.lang.IllegalArgumentException();
		else {
			numberOfTrials = trials;
			side = n;
			numbesOfOpenSitesToPer = new double[numberOfTrials];

			for (i = 0; i < numberOfTrials; i++) {
				percol = new Percolation(side);
				while (!percol.percolates()) {
					percol.open(StdRandom.uniform(side) + 1, StdRandom.uniform(side) + 1);
				}
				numbesOfOpenSitesToPer[i] = percol.numberOfOpenSites() / ((double) side * side);
			}
		}
	}

	public double mean() {                       // sample mean of percolation threshold
		mean = StdStats.mean(numbesOfOpenSitesToPer);

		return mean;
	}

	public double stddev() {                     // sample standard deviation of percolation threshold
		stddev = StdStats.stddev(numbesOfOpenSitesToPer);

		return stddev;
	}

	public double confidenceLo() {               // low endpoint of 95% confidence interval
		return mean - 1.96 * stddev / Math.sqrt(numberOfTrials);
	}

	public double confidenceHi() {               // high endpoint of 95% confidence interval
		return mean + 1.96 * stddev / Math.sqrt(numberOfTrials);
	}

	public static void main(String[] args) {     // test client (described below)
		int n = Integer.parseInt(args[0]);;
		int trials = Integer.parseInt(args[1]);;

		PercolationStats test = new PercolationStats(n, trials); 
		
		System.out.println("mean                    = " + test.mean());
		System.out.println("stddev                  = " + test.stddev());
		System.out.println("95% confidence interval = [" + test.confidenceLo() + ", " + test.confidenceHi() + "]");
	}
}






