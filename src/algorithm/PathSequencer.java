package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import car.CarCoordinate;

import static algorithm.Direction.*;

public class PathSequencer {
    private PathFinder pathFinder = new PathFinder();
    private Arena arena;
    private Path[][] pathMatrix;
    private int[][] costMatrix;
    private Waypoint[] goals;
    private int[] pathSequence = null;

    public PathSequencer(Arena arena, CarCoordinate start) {
        this.arena = arena;

        calcGoals(start);
    }

    public void getPath() {
        fillPathMatrix();
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
                if (costMatrix[a[i-1]][a[i]] == 0) {
                    isPathPossible = false;
                    break;
                }
                curCost += costMatrix[a[i-1]][a[i]];
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

        pathSequence = Arrays.copyOf(minPermutation, minPermutation.length);
//        System.out.println(pathSequence);
    }

    public String getSTMPath() {
        if (pathSequence == null) {
            getPath();
        }
        String s = "STM";

        s += ",";
        s += pathMatrix[0][pathSequence[0]].getSTMPath();
        for (int i = 1; i<pathSequence.length; i++) {
            s += ",";
            s += pathMatrix[pathSequence[i-1]][pathSequence[i]].getSTMPath();
        }
        s += "!";

        return s;
    }

    public String getAndroidOrder() {
        if (pathSequence == null) {
            getPath();
        }
        String s = "AN,ObstacleOrder,";

        for (int i = 0; i<pathSequence.length; i++) {
            s += pathSequence[i];
        }

        System.out.println("Android order: " + s);

        return s;
    }

    public List<List<CarCoordinate>> getCarCoordinates() {
        if (pathSequence == null) {
            getPath();
        }

        List<List<CarCoordinate>> carCoordinates = new ArrayList<>();
        carCoordinates.add(pathMatrix[0][pathSequence[0]].getCarCoordinates());
        for (int i = 1; i<pathSequence.length; i++) {
            carCoordinates.add(pathMatrix[pathSequence[i-1]][pathSequence[i]].getCarCoordinates());
        }

        return carCoordinates;
    }

    private void fillPathMatrix() {
        int n = goals.length;
        pathMatrix = new Path[n][n];
        costMatrix = new int[n][n];
        for (int i = 0; i<n; i++) {
            for (int j = 1; j<n; j++) {
                if (i == j) {
                    continue;
                }
//                System.out.println("finding path between " + i + " and " + j);
                pathMatrix[i][j] = pathFinder.findPathBetweenTwoNodes(goals[i], goals[j], arena);
                if (pathMatrix[i][j] != null) {
                    costMatrix[i][j] = pathMatrix[i][j].getCost();
//                    System.out.println("path between " + i + " and " + j + " found.");
                }
            }
        }

        for (int i = 0; i<n; i++) {
            System.out.println(Arrays.toString(costMatrix[i]));
        }
    }


    private void calcGoals(CarCoordinate start) {
        Waypoint[] obstacles = arena.getObstacles();
//        for (Waypoint e: obstacles) {
//            System.out.println(e.toString());
//        }

        goals = new Waypoint[obstacles.length+1];
        goals[0] = new Waypoint(start.getX()*10 + 5, start.getY()*10 + 5, start.getDir());
        for (int i = 1; i <= obstacles.length; i++) {
            switch (obstacles[i-1].getDirection()) {
                case UP:
                    if (obstacles[i-1].getCoordinateX()/10 == 0) {
                        goals[i] = new Waypoint(obstacles[i-1].getCoordinateX()+10, obstacles[i-1].getCoordinateY() + 30, Direction.values()[(UP.ordinal()+2) % 4]);
                    } else if (obstacles[i-1].getCoordinateX()/10 >= 19) {
                        goals[i] = new Waypoint(obstacles[i-1].getCoordinateX()-10, obstacles[i-1].getCoordinateY() + 30, Direction.values()[(UP.ordinal()+2) % 4]);
                    } else {
                        goals[i] = new Waypoint(obstacles[i-1].getCoordinateX(), obstacles[i-1].getCoordinateY() + 30, Direction.values()[(UP.ordinal()+2) % 4]);
                    }
                    break;
                case DOWN:
                    if (obstacles[i-1].getCoordinateX()/10 == 0) {
                        goals[i] = new Waypoint(obstacles[i-1].getCoordinateX()+10, obstacles[i-1].getCoordinateY() - 30, Direction.values()[(DOWN.ordinal()+2) % 4]);
                    } else if (obstacles[i-1].getCoordinateX()/10 >= 19) {
                        goals[i] = new Waypoint(obstacles[i-1].getCoordinateX()-10, obstacles[i-1].getCoordinateY() - 30, Direction.values()[(DOWN.ordinal()+2) % 4]);
                    } else {
                        goals[i] = new Waypoint(obstacles[i-1].getCoordinateX(), obstacles[i-1].getCoordinateY() - 30, Direction.values()[(DOWN.ordinal()+2) % 4]);
                    }
                    break;
                case RIGHT:
                    if (obstacles[i-1].getCoordinateY()/10 == 0) {
                        goals[i] = new Waypoint(obstacles[i-1].getCoordinateX() + 30, obstacles[i-1].getCoordinateY()+10, Direction.values()[(RIGHT.ordinal()+2) % 4]);
                    } else if (obstacles[i-1].getCoordinateY()/10 >= 19) {
                        goals[i] = new Waypoint(obstacles[i-1].getCoordinateX() + 30, obstacles[i-1].getCoordinateY()-10, Direction.values()[(RIGHT.ordinal()+2) % 4]);
                    } else {
                        goals[i] = new Waypoint(obstacles[i-1].getCoordinateX() + 30, obstacles[i-1].getCoordinateY(), Direction.values()[(RIGHT.ordinal()+2) % 4]);
                    }
                    break;
                case LEFT:
                    if (obstacles[i-1].getCoordinateY()/10 == 0) {
                        goals[i] = new Waypoint(obstacles[i-1].getCoordinateX() - 30, obstacles[i-1].getCoordinateY()+10, Direction.values()[(LEFT.ordinal()+2) % 4]);
                    } else if (obstacles[i-1].getCoordinateY()/10 >= 19) {
                        System.out.println("reached");
                        goals[i] = new Waypoint(obstacles[i-1].getCoordinateX() - 30, obstacles[i-1].getCoordinateY()-10, Direction.values()[(LEFT.ordinal()+2) % 4]);
                    } else {
                        goals[i] = new Waypoint(obstacles[i-1].getCoordinateX() - 30, obstacles[i-1].getCoordinateY(), Direction.values()[(LEFT.ordinal()+2) % 4]);
                    }
                    break;
                default:
                    System.out.println("Error in calcGoals");
                    break;
            }
        }

//        for (Waypoint e: goals) {
//            System.out.println("goal");
//            System.out.println(e.toString());
//        }
    }


}
