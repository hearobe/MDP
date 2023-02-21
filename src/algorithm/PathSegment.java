package algorithm;

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
    int fcost = -1;
    MovementType type;

    public PathSegment(PathSegment parent, Waypoint pos, MovementType type) {
        this.parent = parent;
        this.pos = pos;
        this.type = type;

        calcGCost();
    }

    public int getFcost() {
        return fcost;
    }

    @Override
    public int compareTo(PathSegment o) {
        return this.fcost - o.getFcost();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PathSegment) {
            PathSegment move = (PathSegment) o;
            if(pos.equals(move.getPos())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + this.pos.getCoordinateX() + "," + this.pos.getCoordinateY()+ ") " + this.pos.getDirection() + "\n" +
                this.type + "\n" +
                this.gcost + " " + this.fcost;
    }

    public void calcGCost() {
        if (parent == null) {
            this.gcost = 0;
            return;
        }
        switch (type) {
            case FORWARD:
            case BACKWARD:
                gcost = STRAIGHT_COST + parent.getGcost();
                break;
            case FORWARD_LEFT_TURN:
            case FORWARD_RIGHT_TURN:
                gcost = FORWARD_TURN_COST + parent.getGcost();
                break;
            case BACKWARD_LEFT_TURN:
            case BACKWARD_RIGHT_TURN:
                gcost = BACKWARD_TURN_COST + parent.getGcost();
                break;
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
        if (goal == null) {
            this.fcost = 0;
            return;
        }

        int hcost = Math.abs(pos.CoordinateX - goal.getCoordinateX()) + Math.abs(pos.CoordinateY - goal.getCoordinateY());
        fcost = gcost + hcost;
        return;
    }

    public MovementType getType() {
        return type;
    }

    public void setType(MovementType type) {
        this.type = type;
    }
}
