package algorithm;

import static algorithm.Direction.NULL;

public class Cell {
    private boolean isObstacle = false;
    Direction imageDirection = NULL;
    int positionX; // in terms of grid number. i.e. rightmost cell is 19
    int positionY; // in terms of grid number. i.e. bottom cell is 19

    public Cell(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public boolean isObstacle() {
//        if (positionX < 0 || positionY < 0 || positionX > 19 || positionY > 19) {
//            return false;
//        }
        return isObstacle;
    }

    public void setObstacle(boolean obstacle) {
        isObstacle = obstacle;
    }

    public Direction getImageDirection() {
        return imageDirection;
    }

    public void setImageDirection(Direction imageDirection) {
        this.imageDirection = imageDirection;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }
}
