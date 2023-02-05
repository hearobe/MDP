package algorithm;

import java.util.*;

import static algorithm.Constants.*;
import static algorithm.Direction.*;
import static algorithm.MovementType.*;

public class PathFinder {

    private HashSet visited = new HashSet();
    private PriorityQueue<PathSegment> queue = new PriorityQueue<>();

    public PathFinder() {}

    public ArrayList<PathSegment> findPathBetweenTwoNodes(Waypoint a, Waypoint b, Arena g) {
        ArrayList<PathSegment> path = new ArrayList<PathSegment>();

        PathSegment cur = new PathSegment(null, a, null);
        queue.add(cur);

        visited.add(a);

        while (!isAtGoal(cur, b) && !queue.isEmpty()) {
            cur = queue.poll();
            List<PathSegment> candidates = getCandidatePathSegments(cur, g);

            for (PathSegment candidate : candidates) {
                if (visited.contains(candidate.pos)) {
                    continue;
                }
                candidate.calcFCost(b);
                queue.add(candidate);
                visited.add(candidate);
            }
        }

        PathSegment curParent;
        do {
            curParent = cur.getParent();
            path.add(0,cur);
            cur = curParent;
        } while (curParent != null);

        return path;
    }

    public List<PathSegment> getCandidatePathSegments(PathSegment a, Arena g) {
        LinkedList<PathSegment> queue = new LinkedList<>();

        int x = a.pos.getCoordinateX();
        int y = a.pos.getCoordinateY();
        Direction d = a.pos.getDirection();

        int xcell = x/10;
        int ycell = y/10;

        if (d == NORTH) {
            if (g.validateForwardMovement(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x, y+FORWARD_MOVEMENT, d);
                queue.add(new PathSegment(a, end, FORWARD));
            }
            if (g.validateBackwardMovement(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x, y-BACKWARD_MOVEMENT, d);
                queue.add(new PathSegment(a, end, BACKWARD));
            }
            if (g.validateForwardLeftTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x - FORWARD_LEFT_TURN_HORI, y + FORWARD_LEFT_TURN_VERT, d);
                queue.add(new PathSegment(a, end, FORWARD_LEFT_TURN));
            }
            if (g.validateForwardRightTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x + FORWARD_RIGHT_TURN_HORI, y + FORWARD_RIGHT_TURN_VERT, d);
                queue.add(new PathSegment(a, end, FORWARD_RIGHT_TURN));
            }
            if (g.validateBackwardLeftTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x - BACKWARD_LEFT_TURN_HORI, y - BACKWARD_LEFT_TURN_VERT, d);
                queue.add(new PathSegment(a, end, BACKWARD_LEFT_TURN));
            }
            if (g.validateBackwardRightTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x + BACKWARD_RIGHT_TURN_HORI, y - BACKWARD_RIGHT_TURN_VERT, d);
                queue.add(new PathSegment(a, end, BACKWARD_RIGHT_TURN));
            }
        } else if (d == SOUTH) {
            if (g.validateForwardMovement(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x, y-FORWARD_MOVEMENT, d);
                queue.add(new PathSegment(a, end, FORWARD));
            }
            if (g.validateBackwardMovement(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x, y+BACKWARD_MOVEMENT, d);
                queue.add(new PathSegment(a, end, BACKWARD));
            }
            if (g.validateForwardLeftTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x + FORWARD_LEFT_TURN_HORI, y - FORWARD_LEFT_TURN_VERT, d);
                queue.add(new PathSegment(a, end, FORWARD_LEFT_TURN));
            }
            if (g.validateForwardRightTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x - FORWARD_RIGHT_TURN_HORI, y - FORWARD_RIGHT_TURN_VERT, d);
                queue.add(new PathSegment(a, end, FORWARD_RIGHT_TURN));
            }
            if (g.validateBackwardLeftTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x + BACKWARD_LEFT_TURN_HORI, y + BACKWARD_LEFT_TURN_VERT, d);
                queue.add(new PathSegment(a, end, BACKWARD_LEFT_TURN));
            }
            if (g.validateBackwardRightTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x - BACKWARD_RIGHT_TURN_HORI, y + BACKWARD_RIGHT_TURN_VERT, d);
                queue.add(new PathSegment(a, end, BACKWARD_RIGHT_TURN));
            }
        } else if (d == EAST) {
            if (g.validateForwardMovement(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x+FORWARD_MOVEMENT, y, d);
                queue.add(new PathSegment(a, end, FORWARD));
            }
            if (g.validateBackwardMovement(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x-BACKWARD_MOVEMENT, y, d);
                queue.add(new PathSegment(a, end, BACKWARD));
            }
            if (g.validateForwardLeftTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x + FORWARD_LEFT_TURN_VERT, y + FORWARD_LEFT_TURN_HORI, d);
                queue.add(new PathSegment(a, end, FORWARD_LEFT_TURN));
            }
            if (g.validateForwardRightTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x + FORWARD_RIGHT_TURN_VERT, y - FORWARD_RIGHT_TURN_HORI, d);
                queue.add(new PathSegment(a, end, FORWARD_RIGHT_TURN));
            }
            if (g.validateBackwardLeftTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x - BACKWARD_LEFT_TURN_VERT, y + BACKWARD_LEFT_TURN_HORI, d);
                queue.add(new PathSegment(a, end, BACKWARD_LEFT_TURN));
            }
            if (g.validateBackwardRightTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x - BACKWARD_RIGHT_TURN_VERT, y - BACKWARD_RIGHT_TURN_HORI, d);
                queue.add(new PathSegment(a, end, BACKWARD_RIGHT_TURN));
            }
        } else if (d == WEST) {
            if (g.validateForwardMovement(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x-FORWARD_MOVEMENT, y, d);
                queue.add(new PathSegment(a, end, FORWARD));
            }
            if (g.validateBackwardMovement(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x+BACKWARD_MOVEMENT, y, d);
                queue.add(new PathSegment(a, end, BACKWARD));
            }
            if (g.validateForwardLeftTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x - FORWARD_LEFT_TURN_VERT, y - FORWARD_LEFT_TURN_HORI, d);
                queue.add(new PathSegment(a, end, FORWARD_LEFT_TURN));
            }
            if (g.validateForwardRightTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x - FORWARD_RIGHT_TURN_VERT, y + FORWARD_RIGHT_TURN_HORI, d);
                queue.add(new PathSegment(a, end, FORWARD_RIGHT_TURN));
            }
            if (g.validateBackwardLeftTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x + BACKWARD_LEFT_TURN_VERT, y - BACKWARD_LEFT_TURN_HORI, d);
                queue.add(new PathSegment(a, end, BACKWARD_LEFT_TURN));
            }
            if (g.validateBackwardRightTurn(xcell, ycell, d)) {
                Waypoint end = new Waypoint(x + BACKWARD_RIGHT_TURN_VERT, y + BACKWARD_RIGHT_TURN_HORI, d);
                queue.add(new PathSegment(a, end, BACKWARD_RIGHT_TURN));
            }
        }
        return queue;
    }

    public boolean isAtGoal(PathSegment a, Waypoint b) {
        boolean isWithinRangeX = Math.abs(a.pos.getCoordinateX() - b.getCoordinateX()) <= DISTANCE_FROM_GOAL_LEEWAY;
        boolean isWithinRangeY = Math.abs(a.pos.getCoordinateY() - b.getCoordinateY()) <= DISTANCE_FROM_GOAL_LEEWAY;
        boolean isFacingCorrectDirection = a.pos.getDirection() == b.getDirection();

        return isWithinRangeX && isWithinRangeY && isFacingCorrectDirection;
    }
}
