import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	// 储存每个格子的开关信息，false为关，true为开
	private boolean[][] nByNGrid;
	// 储存每个格子的union, connected等操作
	private WeightedQuickUnionUF uf;
	// 方块每边的格子数量
	private int side;

	public Percolation(int n) {               // create n-by-n grid, with all sites blocked
		int i;

		if (n <= 0) {
			throw new java.lang.IllegalArgumentException();
		} else {
			side = n;
			nByNGrid = new boolean[side][side];
			// uf[n * n] is virtual top site
			// uf[n * n + 1] is virtual bottom site
			uf = new WeightedQuickUnionUF(side * side + 1);
		}
	}

	public void open(int row, int col) {      // open site (row, col) if it is not open already
		if (row <= 0 || row > side || col <= 0 || col > side)
			throw new java.lang.IndexOutOfBoundsException();
		// 若格子为关，则打开它
		if (!isOpen(row, col)) {
			nByNGrid[row - 1][col - 1] = true;

			if (1 == row)  // 判断格子是否在第一行，若成立，则与vitrual top site connected
				uf.union(row * side + col - side - 1, side * side);
			
			//if (side == row) // 判断格子是否在最后一行，若成立，则与virtual bottom site connected
				//uf.union(row * side + col - side - 1, side * side + 1);


			// 判断格子上方是否有格子；若有，判断上方格子是否打开；若打开，两格子connected
			if (row > 1)
				if (isOpen(row - 1, col))
					uf.union(row * side + col - side - 1, row * side + col - 2 * side - 1);

			// 判断格子下方是否有格子；若有，判断下方格子是否打开；若打开，两格子connected
			if (row < side)
				if (isOpen(row + 1, col))
					uf.union(row * side + col - side - 1, row * side + col - 1);

			// 判断格子左边是否有格子；若有，判断左边格子是否打开；若打开，两格子connected
			if (col > 1)
				if (isOpen(row, col - 1))
					uf.union(row * side + col - side - 1, row * side + col - side - 2);

			// 判断格子右边是否有格子；若有，判断右边格子是否打开；若打开，两格子connected
			if (col < side)
				if (isOpen(row, col + 1))
					uf.union(row * side + col - side - 1, row * side + col - side);
		}
	}

	public boolean isOpen(int row, int col) { // is site (row, col) open?
		if (row <= 0 || row > side || col <= 0 || col > side) {
			throw new java.lang.IndexOutOfBoundsException();
		} else {
			return nByNGrid[row - 1][col - 1];
		}
	}

	public boolean isFull(int row, int col) { // is site (row, col) full?
		if (row <= 0 || row > side || col <= 0 || col > side) {
			throw new java.lang.IndexOutOfBoundsException();
		} else 
			return uf.connected(row * side + col - side - 1, side * side);
	}

	public int numberOfOpenSites() {          // number of open sites
		int i, j;
		int count = 0;

		for (i = 1; i <= side; i++)
			for (j = 1; j <= side; j++)
				if (isOpen(i, j))
					count++;

		return count;
	}

	public boolean percolates() {             // does the system percolate?
		int i;

		for (i = 0; i <= side; i++) {
			if (uf.connected(side * side + i - side - 1, side * side))
				return true;
		}

		return false;
	}

	public static void main(String[] args) {  // test client (Optional)		
		/* Percolation test = new Percolation(1000);

		while (!test.percolates()) {
			test.open(StdRandom.uniform(1000) + 1, StdRandom.uniform(1000) + 1);
		}

		System.out.println(test.numberOfOpenSites() / (1000.0 * 1000.0)); */
	}
} 