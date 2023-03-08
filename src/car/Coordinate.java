package car;

import algorithm.Direction;

public class Coordinate {
	int x;
	int y;
	Direction dir;
	
	public Coordinate(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}

	public boolean equals(Coordinate a) {
		return this.getX() == a.getX() &&
				this.getY() == a.getY() &&
				this.getDir() == a.getDir();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Direction getDir() {
		return dir;
	}

	public String toString() {
		return "("+ x + ", " + y + ")  " + dir;
	}
}
