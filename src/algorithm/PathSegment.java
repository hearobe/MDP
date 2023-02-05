package algorithm;

import java.util.Comparator;

import static algorithm.Constants.*;

enum MovementType {
    FORWARD,
    BACKWARD,
    FORWARD_LEFT_TURN,
    FORWARD_RIGHT_TURN,
    BACKWARD_LEFT_TURN,
    BACKWARD_RIGHT_TURN
}

public class PathSegment implements Comparable<PathSegment> {
    PathSegment parent;
    Waypoint pos;
    int gcost = -1;

    public int getFcost() {
        return fcost;
    }

    int fcost = -1;
    MovementType type;

    @Override
    public int compareTo(PathSegment o) {
        return this.fcost - o.getFcost();
    }

    public PathSegment(PathSegment parent, Waypoint pos, MovementType type) {
        this.parent = parent;
        this.pos = pos;
        this.type = type;

        calculateGCost();
    }

    @Override
    public String toString() {
        return "(" + this.pos.getCoordinateX() + "," + this.pos.getCoordinateY()+ ") " + this.pos.getDirection() + "\n" +
                this.type + "\n" +
                this.gcost + " " + this.fcost;
    }

    public void calculateGCost() {
        if (parent == null) {
            this.gcost = 0;
            return;
        }
        switch (type) {
            case FORWARD:
            case BACKWARD:
                gcost = STRAIGHT_COST + parent.getGcost();
                break;
            default:
                gcost = TURN_COST + parent.getGcost();
        }
    }

    public PathSegment getParent() {
        return parent;
    }

    public void setParent(PathSegment parent) {
        this.parent = parent;
    }

    public Waypoint getPos() {
        return pos;
    }

    public void setPos(Waypoint pos) {
        this.pos = pos;
    }

    public int getGcost() {
        return gcost;
    }

    public void setGcost(int gcost) {
        this.gcost = gcost;
    }

    public void calcFCost(Waypoint goal) {
        int hcost = Math.abs(pos.CoordinateX - goal.getCoordinateX()) + Math.abs(pos.CoordinateY - goal.getCoordinateY());
        fcost = gcost + hcost;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }
}
