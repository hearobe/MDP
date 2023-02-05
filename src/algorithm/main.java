package algorithm;

import java.util.ArrayList;

import static algorithm.Direction.*;
import static algorithm.PathFinder.*;

public class main {
    private static PathFinder pathFinder = new PathFinder();

    public static void main(String[] args) {
        Arena g = new Arena();

        int[] obstaclesX = {2,8,13,18,4};
        int[] obstaclesY = {9,1,16,5,5};
        Direction[] obstaclesDirection = {NORTH, SOUTH, EAST, EAST, WEST};

        g.initObstacles(obstaclesX, obstaclesY, obstaclesDirection);

        Waypoint a = new Waypoint(0,0,EAST);
        Waypoint b = new Waypoint(160, 160, WEST);
        ArrayList<PathSegment> path = pathFinder.findPathBetweenTwoNodes(a, b, g);

        for (PathSegment p : path) {
            System.out.println(p.toString());
        }
    }
}


