package algorithm;

import java.util.ArrayList;
import java.util.Arrays;

import static algorithm.Direction.*;

public class main {
    private static PathFinder pathFinder = new PathFinder();
    private static Arena arena = new Arena();
    private static Path[][] pathMatrix;
    private static int[][] costMatrix;
    private static Waypoint[] obstacles;
    private static Waypoint[] goals;
    public static void main(String[] args) {

        int[] obstaclesX = {1,5,12,13,16};
        int[] obstaclesY = {10,16,6,17,2};
        Direction[] obstaclesDirection = {NORTH, SOUTH, EAST, EAST, WEST};

        setObstacles(obstaclesX, obstaclesY, obstaclesDirection);
        calcGoals(new Waypoint (1, 1, EAST));

        int n = goals.length;
        pathMatrix = new Path[n][n];
        for (int i = 0; i<n; i++) {
            for (int j = 1; j<n; j++) {
                pathMatrix[i][j] = pathFinder.findPathBetweenTwoNodes(goals[i], goals[j], arena);
                if (pathMatrix[i][j] != null) {
                    costMatrix[i][j] = pathMatrix[i][j].getCost();
                }
            }
        }

        boolean isPathPossible = true;
        long minCost = Long.MAX_VALUE;
        long curCost = 0;
        int[] minPermutation;

        Permute p = new Permute(obstacles.length);
        ArrayList<int[]> permutations = p.getPermutations();

        for(int[] a : permutations) {
            if (costMatrix[0][a[0]] == 0) {
                continue;
            }
            curCost = costMatrix[0][a[0]];
            for(int i = 1; i<a.length; i++) {
                if (costMatrix[i-1][i] == 0) {
                    isPathPossible = false;
                    break;
                }
                curCost += costMatrix[i-1][i];
            }
            if (!isPathPossible) {
                continue;
            }
            if (curCost < minCost) {
                minCost = curCost;
                minPermutation = Arrays.copyOf(a, a.length);
            }
        }

        // testing PathFinder, to be removed
//        Waypoint a = new Waypoint(10,10,EAST);
//        Waypoint b = new Waypoint(150, 65, WEST);
//        boolean hasPath = pathFinder.findPathBetweenTwoNodes(a, b, g);
//        System.out.println("hasPath = "+ hasPath);
//        if (hasPath) {
//            ArrayList<PathSegment> path = pathFinder.getPath(b);
//
//            for (PathSegment p : path) {
//                System.out.println(p.toString());
//            }
//        }

        // permute test
//        Permute p = new Permute(5);
//        ArrayList<int[]> permutations = p.getPermutations();
//        for(int[] a : permutations) {
//            System.out.println(Arrays.toString(a));
//        }
//        System.out.println(permutations.size());
    }

    private static void setObstacles(int[] x, int[] y, Direction[] d) {
        int n = x.length;
        obstacles = new Waypoint[n];
        for (int i = 0; i<n; i++) {
            obstacles[i] = new Waypoint(x[i], y[i], d[i]);
        }

        arena.initArenaObstacles(x,y,d);
    }

    private static void calcGoals(Waypoint start) {
        goals = new Waypoint[obstacles.length+1];
        goals[0] = start;
        for (int i = 1; i <= obstacles.length; i++) {
            switch (obstacles[i].getDirection()) {
                case NORTH:
                    goals[i] = new Waypoint(obstacles[i].getCoordinateX(), obstacles[i].getCoordinateY() + 25, Direction.values()[(NORTH.ordinal()+2) % 4]);
                    break;
                case SOUTH:
                    goals[i] = new Waypoint(obstacles[i].getCoordinateX(), obstacles[i].getCoordinateY() - 25, Direction.values()[(SOUTH.ordinal()+2) % 4]);
                    break;
                case EAST:
                    goals[i] = new Waypoint(obstacles[i].getCoordinateX() + 25, obstacles[i].getCoordinateY(), Direction.values()[(EAST.ordinal()+2) % 4]);
                    break;
                case WEST:
                    goals[i] = new Waypoint(obstacles[i].getCoordinateX() - 25, obstacles[i].getCoordinateY(), Direction.values()[(WEST.ordinal()+2) % 4]);
                    break;
                default:
                    System.out.println("Error in calcGoals");
                    break;
            }
        }
    }
}


