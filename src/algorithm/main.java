package algorithm;

import car.Coordinate;

import static algorithm.Direction.*;

public class main {
    public static void main(String[] args) {
        Arena arena = new Arena();

        int[] obstacleX = new int[] {1, 8, 14, 7, 17};
        int[] obstacleY = new int[] {18, 8, 14 ,18, 4};
        Direction[] obstacleDirection = new Direction[] {DOWN, DOWN, LEFT, DOWN,LEFT};
        arena.setObstacles(obstacleX, obstacleY, obstacleDirection);

        PathSequencer p = new PathSequencer(arena, new Coordinate(2,2, Direction.UP));
//        List<List<CarCoordinate>> carPath = p.getPath();

//        for(List<CarCoordinate> pathSegment : carPath) {
//            for(CarCoordinate carCoord: pathSegment) {
//                System.out.println(carCoord.toString());
//            }
//        }
    }
}



