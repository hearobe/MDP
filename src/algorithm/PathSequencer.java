package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import car.Coordinate;

import static algorithm.Constants.*;
import static algorithm.Constants.BACKWARD_MOVEMENT;
import static algorithm.Direction.*;

public class PathSequencer {
    private PathFinder pathFinder = new PathFinder();
    private Arena arena;
    private Path[][] pathMatrix;
    private int[][] costMatrix;

    public Coordinate[] getGoals() {
        return goals;
    }

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
        System.out.println(s);
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
                } else if (findAlternativeGoal(i, j)) {
                    recalculatePaths(i, j);
                }
            }
        }
    }

    private boolean findAlternativeGoal(int start, int end) {
        double angle;
        int row = goals[end].getY();
        int col = goals[end].getX();
        Direction d = goals[end].getDir();
        Coordinate newgoal;
        Path path;

        switch (d){
            case UP:
                angle = Math.PI/2;
                break;
            case DOWN:
                angle = 3*Math.PI/2;
                break;
            case RIGHT:
                angle = Math.PI;
                break;
            case LEFT:
                angle = 0;
                break;
            default:
                System.out.println("Error finding candidates");
                return false;
        }

        // try one cell to the left
        newgoal = new Coordinate(col - 1 * (int) Math.sin(angle),
                row - 1 * (int) Math.cos(angle), d);
        path = pathFinder.findPathBetweenTwoNodes(goals[start], newgoal, arena);
        if (path != null) {
            goals[end] = newgoal;
            int cost = path.getCost();
            newgoal = new Coordinate(col + 1 * (int) Math.sin(angle),
                    row + 1 * (int) Math.cos(angle), d);
            Path path2 = pathFinder.findPathBetweenTwoNodes(goals[start], newgoal, arena);
            if (cost <= path2.getCost()) {
                pathMatrix[start][end] = path;
                return true;
            } else {
                goals[end] = newgoal;
                pathMatrix[start][end] = path2;
                return true;
            }
        }

        newgoal = new Coordinate(col + 1 * (int) Math.sin(angle),
                row + 1 * (int) Math.cos(angle), d);
        path = pathFinder.findPathBetweenTwoNodes(goals[start], newgoal, arena);
        if (path != null) {
            goals[end] = newgoal;
            pathMatrix[start][end] = path;
            return true;
        }

        for (int i = 1; i<=5; i++) {
            newgoal = new Coordinate(col+i*(int)Math.cos(angle),
                    row-i*(int)Math.sin(angle), d);
            path = pathFinder.findPathBetweenTwoNodes(goals[start], newgoal, arena);
            if (path != null) {
                goals[end] = newgoal;
                pathMatrix[start][end] = path;
                return true;
            }
        }

        return false;
    }

    private void recalculatePaths(int start, int end) {
        for (int i = 0; i < start; i++) {
            pathMatrix[i][end] = pathFinder.findPathBetweenTwoNodes(goals[i], goals[end], arena);
            if (i == end) {
                for (int j = 1; j < goals.length; j++) {
                    pathMatrix[i][j] = pathFinder.findPathBetweenTwoNodes(goals[i], goals[j], arena);
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
                        goals[i] = new Coordinate(x+1, y + DISTANCE_FROM_GOAL, d.turnBack());
                    } else if (x == 19) {
                        goals[i] = new Coordinate(x-1, y + DISTANCE_FROM_GOAL, d.turnBack());
                    } else {
                        goals[i] = new Coordinate(x, y + DISTANCE_FROM_GOAL, d.turnBack());
                    }
                    break;
                case DOWN:
                    if (x == 0) {
                        goals[i] = new Coordinate(x+1, y - DISTANCE_FROM_GOAL, d.turnBack());
                    } else if (x == 19) {
                        goals[i] = new Coordinate(x-1, y - DISTANCE_FROM_GOAL, d.turnBack());
                    } else {
                        goals[i] = new Coordinate(x, y - DISTANCE_FROM_GOAL, d.turnBack());
                    }
                    break;
                case RIGHT:
                    if (y == 0) {
                        goals[i] = new Coordinate(x + DISTANCE_FROM_GOAL, y+1, d.turnBack());
                    } else if (y == 19) {
                        goals[i] = new Coordinate(x + DISTANCE_FROM_GOAL, y-1, d.turnBack());
                    } else {
                        goals[i] = new Coordinate(x + DISTANCE_FROM_GOAL, y, d.turnBack());
                    }
                    break;
                case LEFT:
                    if (y == 0) {
                        goals[i] = new Coordinate(x - DISTANCE_FROM_GOAL, y+1, d.turnBack());
                    } else if (y == 19) {
                        goals[i] = new Coordinate(x - DISTANCE_FROM_GOAL, y-1, d.turnBack());
                    } else {
                        goals[i] = new Coordinate(x - DISTANCE_FROM_GOAL, y, d.turnBack());
                    }
                    break;
                default:
                    System.out.println("Error in calcGoals");
                    break;
            }
        }
    }


}
