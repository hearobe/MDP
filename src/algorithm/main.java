package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static algorithm.Direction.*;

public class main {
    private static PathFinder pathFinder = new PathFinder();
    private static Arena arena = new Arena();
    private static Path[][] pathMatrix;
    private static int[][] costMatrix;
    private static Waypoint[] obstacles;
    private static Waypoint[] goals;
    public static void main(String[] args) {

        int[] obstaclesX = {6,3,8,14,16};
        int[] obstaclesY = {14,10,8,16,4};
        Direction[] obstaclesDirection = {RIGHT, DOWN, UP, LEFT, UP};

        setObstacles(obstaclesX, obstaclesY, obstaclesDirection);
        //TODO: take start point as input, pass it into calcGoals
        calcGoals(new Waypoint (20, 20, RIGHT));

        List<List<CarCoordinate>> path = getPath();

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

    private static List<List<CarCoordinate>> getPath() {
        fillPathMatrix();
        List<List<CarCoordinate>> carCoordinates = new ArrayList<>();

        boolean isPathPossible = true;
        long minCost = Long.MAX_VALUE;
        long curCost = 0;
        int[] minPermutation = new int[obstacles.length];

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
        System.out.println(Arrays.toString(minPermutation));

        carCoordinates.add(pathMatrix[0][minPermutation[0]].getCarCoordinates());
        for (int i = 1; i<minPermutation.length; i++) {
            carCoordinates.add(pathMatrix[i-1][i].getCarCoordinates());
        }

        return carCoordinates;
    }

    private static void fillPathMatrix() {
        int n = goals.length;
        pathMatrix = new Path[n][n];
        costMatrix = new int[n][n];
        for (int i = 0; i<n; i++) {
            for (int j = 1; j<n; j++) {
                System.out.println("finding path between " + i + " and " + j);
                pathMatrix[i][j] = pathFinder.findPathBetweenTwoNodes(goals[i], goals[j], arena);
                if (pathMatrix[i][j] != null) {
                    costMatrix[i][j] = pathMatrix[i][j].getCost();
                    System.out.println("path between " + i + " and " + j + " found.");
                }
            }
        }
    }

    private static void setObstacles(int[] x, int[] y, Direction[] d) {
        int n = x.length;
        obstacles = new Waypoint[n];
        for (int i = 0; i<n; i++) {
            obstacles[i] = new Waypoint(x[i]*10 + 5, y[i]*10 + 5, d[i]);
        }

        arena.initArenaObstacles(x,y,d);
    }

    private static void calcGoals(Waypoint start) {
        goals = new Waypoint[obstacles.length+1];
        goals[0] = start;
        for (int i = 1; i <= obstacles.length; i++) {
            switch (obstacles[i-1].getDirection()) {
                case UP:
                    goals[i] = new Waypoint(obstacles[i-1].getCoordinateX(), obstacles[i-1].getCoordinateY() + 25, Direction.values()[(UP.ordinal()+2) % 4]);
                    break;
                case DOWN:
                    goals[i] = new Waypoint(obstacles[i-1].getCoordinateX(), obstacles[i-1].getCoordinateY() - 25, Direction.values()[(DOWN.ordinal()+2) % 4]);
                    break;
                case RIGHT:
                    goals[i] = new Waypoint(obstacles[i-1].getCoordinateX() + 25, obstacles[i-1].getCoordinateY(), Direction.values()[(RIGHT.ordinal()+2) % 4]);
                    break;
                case LEFT:
                    goals[i] = new Waypoint(obstacles[i-1].getCoordinateX() - 25, obstacles[i-1].getCoordinateY(), Direction.values()[(LEFT.ordinal()+2) % 4]);
                    break;
                default:
                    System.out.println("Error in calcGoals");
                    break;
            }
        }
    }
}


