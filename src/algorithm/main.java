package algorithm;

import java.util.ArrayList;

import static algorithm.Direction.*;
import static algorithm.PathFinder.*;

public class main {
    private static PathFinder pathFinder = new PathFinder();

    public static void main(String[] args) {
        Arena g = new Arena();

        int[] obstaclesX = {1,5,12,13,16};
        int[] obstaclesY = {10,16,6,17,2};
        Direction[] obstaclesDirection = {NORTH, SOUTH, EAST, EAST, WEST};

        g.initObstacles(obstaclesX, obstaclesY, obstaclesDirection);

        // testing PathFinder, to be removed
        Waypoint a = new Waypoint(10,10,EAST);
        Waypoint b = new Waypoint(150, 65, WEST);
        boolean hasPath = pathFinder.findPathBetweenTwoNodes(a, b, g);
        System.out.println("hasPath = "+ hasPath);
        if (hasPath) {
            ArrayList<PathSegment> path = pathFinder.getPath(b);

            for (PathSegment p : path) {
                System.out.println(p.toString());
            }
        }
    }
}


