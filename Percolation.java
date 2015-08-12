/*
 * name: Aliosha Gonzalez
 * date: 07/03/2015
 * Purpose: A class to calculate whether a system percolates. 
 * Compilation: javac-algs4 Percolation.java
 * 
 */
public class Percolation {
	private int N;    //one side of the grid, the grid will be N*N
	private int gridSize;
	private WeightedQuickUnionUF wUF;
	private boolean[] openClose;   //false blocked, true open 
	private boolean[] emptyFull;   //false empty, true full
	private double openSites;
	
    public Percolation(int N) {
    	if (N <= 0) throw new IllegalArgumentException("N should not be zero or less than zero");
    	
    	this.N = N;
    	gridSize = N*N;
    	wUF = new WeightedQuickUnionUF(gridSize + 2); //2 extra sites top/bottom off the grid(0,gridSize+1)
    	//+1 to begin at 1 instead of 0
    	openClose = new boolean[gridSize + 1];  //store whether each site is open(true) / blocked(false-default)
    	emptyFull = new boolean[gridSize + 1];  //the virtual sites does not need to be empty or full
    	openSites = 0;                          //openSites counter
    }
    
    private double getopenSites() {  //return openSites counter
        return openSites;
    }
    
    //converts from (x,y) coordinates to 1D number
    private int xyTo1D(int i, int j) {
         return ((i-1) * N) + j; 
    }
    
    //validate i and j as a valid index
    private void validate(int i, int j) {
    	if (i < 1 || i > N || j < 1 || j > N) 
    	    throw new IndexOutOfBoundsException("index i or j is not between 1 and " + N);
    }
    
    // if any adjacent site is open, create a union operation with current site
    // if adjacent site is full, make current site full
    private void makeUnion(int p, int q) {
		wUF.union(p, q); //do a union operation
		if (emptyFull[q]) emptyFull[p] = true; //if adjacent site is full, the current site becomes full
    }
    
    //opens the site on coordinates (i,j)
    public void open(int i, int j) {   
    	validate(i, j);
    	int p = xyTo1D(i, j);  //current site index in the 1D array
    	
    	if (openClose[p]) return; //if site is open return, nothing to open 
    	
    	openClose[p] = true;   //open the site
    	openSites++;           //increase openSites counter
    	
    	if (i == 1) { //if site is on the very top, create an union ops with virtual site (0), make it full too
    		wUF.union(p,  0);
    		emptyFull[p] = true; //make the site full too
    	} else if (i == N) wUF.union(p, gridSize+1);   //if site is on the very bottomn, virtual(gridSize+1) site on the bottom
    	 
    	//if any adjacent site is legal and open try to create a union operation with current site
    	if (i-1 >= 1 && isOpen(i-1, j)) makeUnion(p, xyTo1D(i-1, j)); //TOP
    	if (i+1 <= N && isOpen(i+1, j)) makeUnion(p, xyTo1D(i+1, j)); //DOWN
    	if (j-1 >= 1 && isOpen(i  , j-1)) makeUnion(p, xyTo1D(i  , j-1)); //LEFT
    	if (j+1 <= N && isOpen(i  , j+1)) makeUnion(p, xyTo1D(i  , j+1)); //RIGHT

        if (isFull(i, j)) makeFull(i, j);
    }
    
  //After all the unions, update fullness recursively 
    private void makeFull(int i, int j) {
    	emptyFull[ xyTo1D(i, j) ] = true;
    	if (i-1 >= 1 && isOpen(i-1, j) && !isFull(i-1, j)) makeFull(i-1, j  ); 
    	if (i+1 <= N && isOpen(i+1, j) && !isFull(i+1, j)) makeFull(i+1, j  );
    	if (j-1 >= 1 && isOpen(i  , j-1) && !isFull(i  , j-1)) makeFull(i  , j-1);
    	if (j+1 <= N && isOpen(i  , j+1) && !isFull(i  , j+1)) makeFull(i  , j+1);
    }
    
  //return true is site is open, false otherwise
    public boolean isOpen(int i, int j) {  
    	validate(i, j);
    	return openClose[ xyTo1D(i, j) ];
    }
  //return true is site is full, false otherwise
    public boolean isFull(int i, int j) {  
    	validate(i, j);
    	return emptyFull[ xyTo1D(i, j) ];
    }
    
  //return true if top and bottom site percolates, false otherwise
    public boolean percolates() {     
    	 return wUF.connected(0, gridSize+1);
    }
    
	
	public static void main(String[] args) {
		Percolation per = new Percolation(1);
		int x = 1;
		int y = 1;
		System.out.println("(" +x+ ", " +y+ ") = " + per.xyTo1D(x, y));
		System.out.println("open sites: "  +per.getopenSites());

		per.open(1, 1);
//		per.open(2, 1);
//		per.open(2, 2);
//		per.open(3, 2);
//		per.open(3, 3);
		System.out.println("Are they connected(2,3)? " + per.wUF.connected(0, 3+1));
		System.out.println("Percolates? " + per.percolates());
		System.out.println("isFull(1, 1): " +per.xyTo1D(1, 1)+ " "+per.isFull(1, 1));
		System.out.println("isFull(2, 1): " +per.xyTo1D(2, 1)+ " " + per.isFull(2, 1));
		System.out.println("isFull(2, 2): " +per.xyTo1D(2, 2)+ " " + per.isFull(2, 2));
		System.out.println("isFull(3, 2): "  +per.xyTo1D(3, 2)+ " "+ per.isFull(3, 2));
		System.out.println("isFull(3, 3): "  +per.xyTo1D(3, 3)+ " "+ per.isFull(3, 3));
		System.out.println("open sites: "  +per.getopenSites());
	}
}
