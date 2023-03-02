package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import car.Coordinate;

import static algorithm.Direction.*;

public class PathSequencer {
    private PathFinder pathFinder = new PathFinder();
    private Arena arena;
    private Path[][] pathMatrix;
    private int[][] costMatrix;
    private Coordinate[] goals;
    private int[] pathSequence = null;

    public PathSequencer(Arena arena, Coordinate start) {
        this.arena = arena;

        calcGoals(start);
    }

    public void getPath() {
        fillPathMatrix();
        Coordinate[] obstacles = arena.getObstacles();

        boolean isPathPossible = true;
        long minCost = Long.MAX_VALUE, curCost;
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

        pathSequence = Arrays.copyOf(minPermutation, minPermutation.length);
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

        return s;
    }

    public List<List<Coordinate>> getCarCoordinates() {
        if (pathSequence == null) {
            getPath();
        }

        List<List<Coordinate>> carCoordinates = new ArrayList<>();
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

        // find all possible paths between any 2 goals
        for (int i = 0; i<n; i++) {
            for (int j = 1; j<n; j++) {
                if (i == j) {
                    continue;
                }
                pathMatrix[i][j] = pathFinder.findPathBetweenTwoNodes(goals[i], goals[j], arena);
                if (pathMatrix[i][j] != null) {
                    costMatrix[i][j] = pathMatrix[i][j].getCost();
                }
            }
        }
    }


    private void calcGoals(Coordinate start) {
        Coordinate[] obstacles = arena.getObstacles();

        goals = new Coordinate[obstacles.length+1];
        goals[0] = start;
        for (int i = 1; i <= obstacles.length; i++) {
            int x = obstacles[i-1].getX(), y = obstacles[i-1].getY();
            Direction d = obstacles[i-1].getDir();

            switch (d) {
                case UP:
                    if (x == 0) {
                        goals[i] = new Coordinate(x+1, y + 3, d.turnBack());
                    } else if (x == 19) {
                        goals[i] = new Coordinate(x-1, y + 3, d.turnBack());
                    } else {
                        goals[i] = new Coordinate(x, y + 3, d.turnBack());
                    }
                    break;
                case DOWN:
                    if (x == 0) {
                        goals[i] = new Coordinate(x+1, y - 3, d.turnBack());
                    } else if (x == 19) {
                        goals[i] = new Coordinate(x-1, y - 3, d.turnBack());
                    } else {
                        goals[i] = new Coordinate(x, y - 3, d.turnBack());
                    }
                    break;
                case RIGHT:
                    if (y == 0) {
                        goals[i] = new Coordinate(x + 3, y+1, d.turnBack());
                    } else if (y == 19) {
                        goals[i] = new Coordinate(x + 3, y-1, d.turnBack());
                    } else {
                        goals[i] = new Coordinate(x + 3, y, d.turnBack());
                    }
                    break;
                case LEFT:
                    if (y == 0) {
                        goals[i] = new Coordinate(x - 3, y+1, d.turnBack());
                    } else if (y == 19) {
                        System.out.println("reached");
                        goals[i] = new Coordinate(x - 3, y-1, d.turnBack());
                    } else {
                        goals[i] = new Coordinate(x - 3, y, d.turnBack());
                    }
                    break;
                default:
                    System.out.println("Error in calcGoals");
                    break;
            }
        }
    }


}
