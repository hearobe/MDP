package car;

import algorithm.Direction;

public class Car {
	public static final int carWidth = 3;
	public static  final int carHeight = 3;
	
	int xCoord;
	int yCoord;
	Direction dir;
	
	public Car(int xCoord, int yCoord, Direction dir) {
		this.xCoord = xCoord;
		this.yCoord = yCoord;
		this.dir = dir;
	}
	
	public Car() {
		// TODO Auto-generated constructor stub
	}

	public int getXCoord() {
		return xCoord;
	}

	public void setXCoord(int xCoord) {
		this.xCoord = xCoord;
	}

	public int getYCoord() {
		return yCoord;
	}

	public void setYCoord(int yCoord) {
		this.yCoord = yCoord;
	}

	public Direction getDir() {
		return dir;
	}

	public void setDir(Direction dir) {
		this.dir = dir;
	}
	
	public void setCoord(int x, int y) {
		this.xCoord = x;
		this.yCoord = y;
	}
	
	public void update(CarCoordinate k) {
		this.xCoord = k.x;
		this.yCoord = k.y;
		this.dir = k.dir;
	}
	
	public CarCoordinate getCarCoordinate() {
		return new CarCoordinate(this.xCoord, this.yCoord, this.dir);
	}
	
	public String toString() {
		return String.format("x:%d y:%d \n", this.xCoord, this.yCoord);
	}
	
	 
}
