package algorithm;

import static algorithm.Constants.DISTANCE_FROM_GOAL_LEEWAY;

public class Waypoint {
    int CoordinateX;
    int CoordinateY;
    Direction direction; //should this be more specific? like exact angle?

    public Waypoint(int coordinateX, int coordinateY, Direction direction) {
        CoordinateX = coordinateX;
        CoordinateY = coordinateY;
        this.direction = direction;
    }

    public boolean equals(Waypoint a) {
        return this.getCoordinateX() == a.getCoordinateX() &&
                this.getCoordinateY() == a.getCoordinateY() &&
                this.getDirection() == a.getDirection();
    }

//    public int getHash() {
//        return CoordinateX*27 + CoordinateY*27*27 + direction.ordinal();
//    }

    public int getCoordinateX() {
        return CoordinateX;
    }

    public void setCoordinateX(int coordinateX) {
        CoordinateX = coordinateX;
    }

    public int getCoordinateY() {
        return CoordinateY;
    }

    public void setCoordinateY(int coordinateY) {
        CoordinateY = coordinateY;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
