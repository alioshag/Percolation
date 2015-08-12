/*
 * name: Aliosha Gonzalez
 * date: 07/03/2015
 * Purpose: A program that calculates the mean, standard deviation and the 
 *          confidence interval of the percolation threshold in a grid N*N. 
 * Compilation: javac-algs4 PercolationStats.java
 * Execution: java-algs4 PercolationStats N T (both arguments are integers)
 *          arg0: N*N size of the grid
 *          arg1: T amount of times to run the experiment 
 */
public class PercolationStats {
	private double[] thrHArray; //array to store every threshold value for each calculation(T times)
	private int T;             //amount of times to repeat the calculation
	
	public PercolationStats(int N, int T) {     // perform T independent experiments on an N-by-N grid
		if (N <= 0 || T <= 0) throw new IllegalArgumentException("The values of N or T are equal or less than zero");
		
		double gridSize = N*N;  //size of the grid
		this.T = T;
		thrHArray = new double[T];  //T = sampling times
		
		for (int i = 0; i < T; i++) {   //fill the thrHArray with threshold values
			Percolation per = new Percolation(N);  //percolation obj
			int openSiteCounter = 0;                   //openSite Counter
			
			while (!per.percolates())  {
				int x = StdRandom.uniform(N) + 1;
				int y = StdRandom.uniform(N) + 1;
				if (!per.isOpen(x, y)) {  //if site is blocked, open it
					per.open(x, y);
					openSiteCounter++;
				}
			}

			thrHArray[i] = openSiteCounter / gridSize;
		}
   }
   
   public double mean() {                      // sample mean of percolation threshold
	    return StdStats.mean(thrHArray); 
   }
   
   public double stddev() {                    // sample standard deviation of percolation threshold
       return StdStats.stddev(thrHArray);
   }
   
   public double confidenceLo() {      // low  endpoint of 95% confidence interval
	   return mean() - (1.96*stddev() / Math.sqrt(T));
   }

   public double confidenceHi() {              // high endpoint of 95% confidence interval
	   return mean() + (1.96*stddev() / Math.sqrt(T));
   }
   
   //the client takes 2 args (N and T)
   public static void main(String[] args) {   // test client (described below)
	     
	   if (args.length != 2) throw new IllegalArgumentException("Invalid number of arguments");
	   
	   int N = Integer.valueOf(args[0]);
	   int T = Integer.valueOf(args[1]);
	   PercolationStats perStats = new PercolationStats(N, T);
	   
	   System.out.println("mean                    = "+ perStats.mean());
	   System.out.println("stddev                  = "+ perStats.stddev());
	   System.out.println("95% confidence interval = "+perStats.confidenceLo()+ ", " +perStats.confidenceHi());
   }
}
