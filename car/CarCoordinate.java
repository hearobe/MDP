package car;

import algorithm.Direction;

public class CarCoordinate {
	int x;
	int y;
	Direction dir;
	
	public CarCoordinate(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
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
	
    @Override
    public String toString() {
        return "x: " + this.x + "  " + "y: " + this.y + "dir: "  + this.dir + " \n";
    }
}
