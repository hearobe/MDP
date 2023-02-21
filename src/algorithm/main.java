package algorithm;

import static algorithm.Direction.DOWN;
import static algorithm.Direction.LEFT;
import static algorithm.Direction.UP;

import java.util.List;

import car.Car;
import car.CarCoordinate;

public class main {
    public static void main(String[] args) {
    	
        Arena arena = new Arena();
        
        
        int[] obstacleX = new int[] {1, 8, 14, 7, 17};
        int[] obstacleY = new int[] {18, 8, 14 ,18, 4};
        Direction[] obstacleDirection = new Direction[] {DOWN, DOWN, LEFT, DOWN,LEFT};
        arena.setObstacles(obstacleY, obstacleX, obstacleDirection);
       
        Car car = new Car(2, 2, UP);

        PathSequencer pathSequencer = new PathSequencer(arena, car.getCarCoordinate());

        List<List<CarCoordinate>> carPath = pathSequencer.getPath();
        
        for(List<CarCoordinate> pathSegment : carPath) {
        	for(CarCoordinate carCoord: pathSegment) {
        		System.out.println("hello");
        		carCoord.toString();
        	}
        }
    }
}


