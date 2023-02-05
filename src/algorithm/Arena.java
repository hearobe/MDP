package algorithm;

import static algorithm.Constants.*;
import static algorithm.Constants.BACKWARD_MOVEMENT;
import static algorithm.Direction.*;
import static algorithm.Direction.WEST;

public class Arena {

    Cell[][] grid;

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

    // add obstacle positions
    // add node waypoints for start position and obstacle viewing points
    // robot start position
    public void initObstacles(int[] x, int[] y, Direction[] d) {
        int length = x.length;

        for (int i = 0; i<length; i++) {
            int xCoordinate = x[i];
            int yCoordinate = y[i];

            grid[yCoordinate][xCoordinate].setImageDirection(d[i]);

            for (int j = xCoordinate-1; j<=xCoordinate+1; j++) {
                for (int k = yCoordinate-1; k<=yCoordinate+1; k++) {
                    grid[k][j].setObstacle(true);
                }
            }
        }
    }

    public boolean validCell(int x, int y) {
        if (x < 0 || y < 0 || x > 19 || y > 19) {
            return false;
        }
        return grid[y][x].isObstacle();
    }

    public boolean validateForwardMovement(int x, int y, Direction d) {
        if (d == NORTH) {
            for (int i = 0; i<FORWARD_MOVEMENT; i++) {
                if (!validCell(x, y+i)) {
                    return false;
                }
            }
        } else if (d == SOUTH) {
            for (int i = 0; i<FORWARD_MOVEMENT; i++) {
                if (!validCell(x, y-i)) {
                    return false;
                }
            }
        } else if (d == EAST) {
            for (int i = 0; i<FORWARD_MOVEMENT; i++) {
                if (!validCell(x+i, y)) {
                    return false;
                }
            }
        } else if (d == WEST) {
            for (int i = 0; i<FORWARD_MOVEMENT; i++) {
                if (!validCell(x-i, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validateBackwardMovement(int x, int y, Direction d) {
        if (d == NORTH) {
            for (int i = 0; i<BACKWARD_MOVEMENT; i++) {
                if (!validCell(x, y-i)) {
                    return false;
                }
            }
        } else if (d == SOUTH) {
            for (int i = 0; i<BACKWARD_MOVEMENT; i++) {
                if (!validCell(x, y+i)) {
                    return false;
                }
            }
        } else if (d == EAST) {
            for (int i = 0; i<BACKWARD_MOVEMENT; i++) {
                if (!validCell(x-i, y)) {
                    return false;
                }
            }
        } else if (d == WEST) {
            for (int i = 0; i<BACKWARD_MOVEMENT; i++) {
                if (!validCell(x+i, y)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean validateForwardLeftTurn(int x, int y, Direction d) {
        if (d == NORTH) {
            for (int i = 0; i >= 1; i--) {
                for (int j = 1; j <= 2; j++) {
                    if (!validCell(x-i, y+j)) {
                        return false;
                    }
                }
            }
        } else if (d == SOUTH) {
            for (int i = 0; i >= 1; i--) {
                for (int j = 1; j <= 2; j++) {
                    if (!validCell(x+i, y-j)) {
                        return false;
                    }
                }
            }
        } else if (d == EAST) {
            for (int i = 0; i >= 1; i--) {
                for (int j = 1; j <= 2; j++) {
                    if (!validCell(x+j, y+i)) {
                        return false;
                    }
                }
            }
        } else if (d == WEST) {
            for (int i = 0; i >= 1; i--) {
                for (int j = 1; j <= 2; j++) {
                    if (!validCell(x-j, y-i)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean validateForwardRightTurn(int x, int y, Direction d) {
        if (d == NORTH) {
            for (int i = 0; i >= 1; i--) {
                for (int j = 1; j <= 2; j++) {
                    if (!validCell(x+i, y+j)) {
                        return false;
                    }
                }
            }
        } else if (d == SOUTH) {
            for (int i = 0; i >= 1; i--) {
                for (int j = 1; j <= 2; j++) {
                    if (!validCell(x-i, y-j)) {
                        return false;
                    }
                }
            }
        } else if (d == EAST) {
            for (int i = 0; i >= 1; i--) {
                for (int j = 1; j <= 2; j++) {
                    if (!validCell(x+j, y-i)) {
                        return false;
                    }
                }
            }
        } else if (d == WEST) {
            for (int i = 0; i >= 1; i--) {
                for (int j = 1; j <= 2; j++) {
                    if (!validCell(x+j, y+i)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean validateBackwardLeftTurn(int x, int y, Direction d) {
        if (d == NORTH) {
            if (grid[y-1][x].isObstacle() ||
                    grid[y-2][x].isObstacle() ||
                    grid[y-1][x-1].isObstacle() ||
                    grid[y-2][x-1].isObstacle()) {
                return false;
            }
        } else if (d == SOUTH) {
            if (grid[y+1][x].isObstacle() ||
                    grid[y+2][x].isObstacle() ||
                    grid[y+1][x+1].isObstacle() ||
                    grid[y+2][x+1].isObstacle()) {
                return false;
            }
        } else if (d == EAST) {
            if (grid[y+1][x].isObstacle() ||
                    grid[y+2][x].isObstacle() ||
                    grid[y+1][x-1].isObstacle() ||
                    grid[y+2][x-1].isObstacle()) {
                return false;
            }
        } else if (d == WEST) {
            if (grid[y-1][x].isObstacle() ||
                    grid[y-2][x].isObstacle() ||
                    grid[y-1][x+1].isObstacle() ||
                    grid[y-2][x+1].isObstacle()) {
                return false;
            }
        }
        return true;
    }

    public boolean validateBackwardRightTurn(int x, int y, Direction d) {
        if (d == NORTH) {
            if (grid[y-1][x].isObstacle() ||
                    grid[y-2][x].isObstacle() ||
                    grid[y-1][x+1].isObstacle() ||
                    grid[y-2][x+1].isObstacle()) {
                return false;
            }
        } else if (d == SOUTH) {
            if (grid[y+1][x].isObstacle() ||
                    grid[y+2][x].isObstacle() ||
                    grid[y+1][x-1].isObstacle() ||
                    grid[y+2][x-1].isObstacle()) {
                return false;
            }
        } else if (d == EAST) {
            if (grid[y-1][x].isObstacle() ||
                    grid[y-2][x].isObstacle() ||
                    grid[y-1][x-1].isObstacle() ||
                    grid[y-2][x-1].isObstacle()) {
                return false;
            }
        } else if (d == WEST) {
            if (grid[y+1][x].isObstacle() ||
                    grid[y+2][x].isObstacle() ||
                    grid[y+1][x+1].isObstacle() ||
                    grid[y+2][x+1].isObstacle()) {
                return false;
            }
        }
        return true;
    }
}
