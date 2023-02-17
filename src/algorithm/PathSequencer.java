package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import car.CarCoordinate;

import static algorithm.Direction.*;

public class PathSequencer {
    private PathFinder pathFinder = new PathFinder();
    private Arena arena;
    private Path[][] pathMatrix;
    private int[][] costMatrix;
    private Waypoint[] goals;

    public PathSequencer(Arena arena, CarCoordinate start) {
        this.arena = arena;

        calcGoals(start);
    }

    public List<List<CarCoordinate>> getPath() {
        fillPathMatrix();
        List<List<CarCoordinate>> carCoordinates = new ArrayList<>();
        Waypoint[] obstacles = arena.getObstacles();

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

    private void fillPathMatrix() {
        int n = goals.length;
        pathMatrix = new Path[n][n];
        costMatrix = new int[n][n];
        for (int i = 0; i<n; i++) {
            for (int j = 1; j<n; j++) {
//                System.out.println("finding path between " + i + " and " + j);
                pathMatrix[i][j] = pathFinder.findPathBetweenTwoNodes(goals[i], goals[j], arena);
                if (pathMatrix[i][j] != null) {
                    costMatrix[i][j] = pathMatrix[i][j].getCost();
//                    System.out.println("path between " + i + " and " + j + " found.");
                }
            }
        }
    }


    private void calcGoals(CarCoordinate start) {
        Waypoint[] obstacles = arena.getObstacles();
        goals[0] = new Waypoint(start.getX()*10 + 5, start.getY()*10 + 5, start.getDir());
        goals = new Waypoint[obstacles.length+1];
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