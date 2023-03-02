package algorithm;

import car.Coordinate;

import java.util.Arrays;

import static algorithm.Constants.*;

public class Arena {

    public Cell[][] grid;
    private Coordinate[] obstacles;


    public Arena() {
        grid = new Cell[GRID_SIZE_IN_CELLS][GRID_SIZE_IN_CELLS];

        for (int row = 0; row<GRID_SIZE_IN_CELLS; row++) {
            for (int col = 0; col<GRID_SIZE_IN_CELLS; col++) {
                grid[row][col] = new Cell(row, col);

                if (row == 0 || col == 0 || row == GRID_SIZE_IN_CELLS-1 || col == GRID_SIZE_IN_CELLS-1) {
                    grid[row][col].setBuffer(true);
                }
            }
        }
    }

    public Coordinate[] getObstacles() {
        return obstacles;
    }

    public void setObstacles(int[] x, int[] y, Direction[] d) {
        int n = x.length;
        obstacles = new Coordinate[n];

        for (int i = 0; i<n; i++) {
            int xCoordinate = x[i];
            int yCoordinate = y[i];
            obstacles[i] = new Coordinate(xCoordinate, yCoordinate, d[i]);

            grid[yCoordinate][xCoordinate].setImageDirection(d[i]);

            for (int j = xCoordinate-1; j<=xCoordinate+1; j++) {
                if (j < 0 || j > 19) {
                    continue;
                }
                for (int k = yCoordinate-1; k<=yCoordinate+1; k++) {
                    if (k < 0 || k > 19) {
                        continue;
                    }
                    if (j == xCoordinate && k == yCoordinate) {
                        grid[k][j].setObstacle(true);
                    }
                    grid[k][j].setBuffer(true);
                }
            }
        }
    }

    public boolean validCell(Cell c) {
        int x = c.getPositionX();
        int y = c.getPositionY();
        if (x < 0 || y < 0 || x > 19 || y > 19) {
            return false;
        }
        return !(grid[y][x].isObstacle() || grid[y][x].isBuffer());
    }
}