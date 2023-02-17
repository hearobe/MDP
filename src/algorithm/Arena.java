package algorithm;

import java.util.ArrayList;

import static algorithm.Constants.*;
import static algorithm.Direction.*;
import static algorithm.Direction.LEFT;

public class Arena {

    private Cell[][] grid;
    private Waypoint[] obstacles;


    public Arena() {
        grid = new Cell[GRID_SIZE_IN_CELLS][GRID_SIZE_IN_CELLS];

        for (int row = 0; row<GRID_SIZE_IN_CELLS; row++) {
            for (int col = 0; col<GRID_SIZE_IN_CELLS; col++) {
                grid[row][col] = new Cell(row, col);

                if (row == 0 || col == 0 || row == GRID_SIZE_IN_CELLS-1 || col == GRID_SIZE_IN_CELLS-1) {
                    grid[row][col].setObstacle(true);
                }
            }
        }
    }

    public Waypoint[] getObstacles() {
        return obstacles;
    }

    public void setObstacles(int[] x, int[] y, Direction[] d) {
        int n = x.length;
        obstacles = new Waypoint[n];
        for (int i = 0; i<n; i++) {
            obstacles[i] = new Waypoint(x[i]*10 + 5, y[i]*10 + 5, d[i]);
        }

        for (int i = 0; i<n; i++) {
            int xCoordinate = x[i];
            int yCoordinate = y[i];

            grid[yCoordinate][xCoordinate].setImageDirection(d[i]);

            for (int j = xCoordinate-1; j<=xCoordinate+1; j++) {
                if (j < 0 || j > 19) {
                    continue;
                }
                for (int k = yCoordinate-1; k<=yCoordinate+1; k++) {
                    if (k < 0 || k > 19) {
                        continue;
                    }
                    grid[k][j].setObstacle(true);
                }
            }
        }

        for (int i = 0; i<20; i++) {
            grid[0][i].setObstacle(true);
            grid[19][i].setObstacle(true);
            grid[i][0].setObstacle(true);
            grid[i][19].setObstacle(true);
        }
    }


    // currently unused, may not be necessary
    public boolean validCell(Waypoint p) {
        int x = p.getCoordinateX();
        int y = p.getCoordinateY();
        if (x < 0 || y < 0 || x > 199 || y > 199) {
            return false;
        }
        return !grid[p.getCoordinateY()/10][p.getCoordinateX()/10].isObstacle();
    }

    public boolean validCell(Cell c) {
        int x = c.getPositionX();
        int y = c.getPositionY();
        if (x < 0 || y < 0 || x > 19 || y > 19) {
            return false;
        }
        return !grid[y][x].isObstacle();
    }
}
