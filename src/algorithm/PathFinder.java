package algorithm;

import java.util.*;

import static algorithm.Constants.*;
import static algorithm.Direction.*;
import static algorithm.MovementType.*;

public class PathFinder {

    private ArrayList<PathSegment> visited = new ArrayList<PathSegment>();
    private PriorityQueue<PathSegment> queue = new PriorityQueue<>();
    private PathSegment lastMove = null;

    public PathFinder() {}

    public Path findPathBetweenTwoNodes(Waypoint a, Waypoint b, Arena g) {
        visited.clear();
        queue.clear();
        lastMove = null;

        PathSegment cur = new PathSegment(null, a, null);
        cur.calcFCost(b);
        queue.add(cur);

        while (!queue.isEmpty()) {
            cur = queue.poll();
            List<PathSegment> candidates = getCandidatePathSegments(cur, g);

            for (PathSegment candidate : candidates) {
                candidate.calcFCost(b);
                if (isAtGoal(candidate, b)) {
                    lastMove = candidate;
                    visited.add(cur);
                    visited.add(candidate);

                    ArrayList<PathSegment> pathSegments = new ArrayList<PathSegment>();
                    PathSegment curMove = lastMove;
                    PathSegment curParent;
                    do {
                        curParent = curMove.getParent();
                        pathSegments.add(0,curMove);
                        curMove = curParent;
                    } while (curParent != null);

                    Path path = new Path(pathSegments, lastMove.gcost);
                    return path;
                }

                if(queue.contains(candidate)) {
                    PathSegment dup = null;
                    // Get ArenaMove if it already exists in queue
                    Object[] queueArray = queue.toArray();
                    for(int j=0; j<queueArray.length; j++) {
                        if(candidate.equals(queueArray[j])) {
                            dup = (PathSegment) queueArray[j];
                            break;
                        }
                    }
                    // Skip successor if it does not have better value
                    if(candidate.getFcost()>=dup.getFcost()) {
                        continue;
                    }
                }

                if (visited.contains(candidate)) {
                    PathSegment dup = null;
                    // Get ArenaMove if it already exists in queue
                    Object[] visitedArray = visited.toArray();
                    for(int j=0; j<visitedArray.length; j++) {
                        if(candidate.equals(visitedArray[j])) {
                            dup = (PathSegment) visitedArray[j];
                            break;
                        }
                    }
                    // Skip successor if it does not have better value
                    if(candidate.getFcost() >= dup.getFcost()) {
                        continue;
                    } else {
                        queue.add(candidate);
                    }
                }

                queue.add(candidate);
            }
            visited.add(cur);
        }
        return null;
    }

    public List<PathSegment> getCandidatePathSegments(PathSegment a, Arena g) {
        LinkedList<PathSegment> candidates = new LinkedList<>();
        double angle;
        int row = a.getPos().getCoordinateY()/10;
        int col = a.getPos().getCoordinateX()/10;

        switch (a.pos.getDirection()){
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
                return null;
        }

        Cell straight, left, right;
        Waypoint newPoint;
        boolean isLeftPossible = true, isRightPossible = true;

        // assuming the robot moves forward by one grid cell
        straight = new Cell(col-(int)Math.cos(angle), row+(int)Math.sin(angle));
        if (g.validCell(straight)) {
            newPoint = new Waypoint(a.getPos().getCoordinateX()-FORWARD_MOVEMENT*(int)Math.cos(angle),
                    a.getPos().getCoordinateY()+FORWARD_MOVEMENT*(int)Math.sin(angle));
            candidates.add(getPathSegment(a, newPoint));
        }

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                left = new Cell(col - i * (int) Math.cos(angle) - j * (int) Math.sin(angle),
                        row + i * (int) Math.sin(angle) - j * (int) Math.cos(angle));
                if (!g.validCell(left)) {
                    isLeftPossible = false;
                    break;
                }
            }
            if (i == 2 && isLeftPossible) {
                newPoint = new Waypoint(a.getPos().getCoordinateX() - FORWARD_LEFT_TURN_VERT * (int) Math.cos(angle) - FORWARD_LEFT_TURN_HORI * (int) Math.sin(angle),
                        a.getPos().getCoordinateY() + FORWARD_LEFT_TURN_VERT * (int) Math.sin(angle) - FORWARD_LEFT_TURN_HORI * (int) Math.cos(angle));
                candidates.add(getPathSegment(a, newPoint));
            }
        }

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                right = new Cell(col-i*(int)Math.cos(angle)+j*(int)Math.sin(angle),
                        row+i*(int)Math.sin(angle)+j*(int)Math.cos(angle));
                if (!g.validCell(right)) {
                    isRightPossible = false;
                    break;
                }
            }
            if (i == 2 && isRightPossible) {
                newPoint = new Waypoint(a.getPos().getCoordinateX()-FORWARD_RIGHT_TURN_VERT*(int)Math.cos(angle)+FORWARD_RIGHT_TURN_HORI*(int)Math.sin(angle),
                        a.getPos().getCoordinateY()+FORWARD_RIGHT_TURN_VERT*(int)Math.sin(angle)+FORWARD_RIGHT_TURN_HORI*(int)Math.cos(angle));
                candidates.add(getPathSegment(a, newPoint));
            }
        }

        // BACKWARD
        isLeftPossible = true;
        isRightPossible = true;

        straight = new Cell(col+(int)Math.cos(angle), row-(int)Math.sin(angle));
        if (g.validCell(straight)) {
            newPoint = new Waypoint(a.getPos().getCoordinateX()+BACKWARD_MOVEMENT*(int)Math.cos(angle),
                    a.getPos().getCoordinateY()-BACKWARD_MOVEMENT*(int)Math.sin(angle));
            candidates.add(getPathSegment(a, newPoint));
        }

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                left = new Cell(col + i * (int) Math.cos(angle) - j * (int) Math.sin(angle),
                        row - i * (int) Math.sin(angle) - j * (int) Math.cos(angle));
                if (!g.validCell(left)) {
                    isLeftPossible = false;
                    break;
                }
            }
            if (i == 2 && isLeftPossible) {
                newPoint = new Waypoint(a.getPos().getCoordinateX() + BACKWARD_LEFT_TURN_VERT * (int) Math.cos(angle) - BACKWARD_LEFT_TURN_HORI * (int) Math.sin(angle),
                        a.getPos().getCoordinateY() - BACKWARD_LEFT_TURN_VERT * (int) Math.sin(angle) - BACKWARD_LEFT_TURN_HORI * (int) Math.cos(angle));
                candidates.add(getPathSegment(a, newPoint));
            }
        }

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                right = new Cell(col+i*(int)Math.cos(angle)+j*(int)Math.sin(angle),
                        row-i*(int)Math.sin(angle)+j*(int)Math.cos(angle));
                if (!g.validCell(right)) {
                    isRightPossible = false;
                    break;
                }
            }
            if (i == 2 && isRightPossible) {
                newPoint = new Waypoint(a.getPos().getCoordinateX()+BACKWARD_RIGHT_TURN_VERT*(int)Math.cos(angle)+BACKWARD_RIGHT_TURN_HORI*(int)Math.sin(angle),
                        a.getPos().getCoordinateY()-BACKWARD_RIGHT_TURN_VERT*(int)Math.sin(angle)+BACKWARD_RIGHT_TURN_HORI*(int)Math.cos(angle));
                candidates.add(getPathSegment(a, newPoint));
            }
        }

        return candidates;
    }

    public PathSegment getPathSegment(PathSegment a, Waypoint newPoint) {
        PathSegment newPath = new PathSegment(a, newPoint, directionChange(a.getPos(), newPoint));

        return newPath;
    }

    public MovementType directionChange(Waypoint pos, Waypoint endPos) {
        switch(pos.getDirection()) {
            case UP:
                if(endPos.getCoordinateY() > pos.getCoordinateY()) {
                    if(endPos.getCoordinateX() > pos.getCoordinateX()) {
                        endPos.setDirection(RIGHT);
                        return FORWARD_RIGHT_TURN;
                    }
                    else if(endPos.getCoordinateX() < pos.getCoordinateX()) {
                        endPos.setDirection(LEFT);
                        return FORWARD_LEFT_TURN;
                    }
                    endPos.setDirection(UP);
                    return FORWARD;
                } else {
                    if(endPos.getCoordinateX() > pos.getCoordinateX()) {
                        endPos.setDirection(LEFT);
                        return BACKWARD_RIGHT_TURN;
                    }
                    else if(endPos.getCoordinateX() < pos.getCoordinateX()) {
                        endPos.setDirection(RIGHT);
                        return BACKWARD_LEFT_TURN;
                    }
                    endPos.setDirection(UP);
                    return BACKWARD;
                }

            case DOWN:
                if(endPos.getCoordinateY() > pos.getCoordinateY()) {
                    if(endPos.getCoordinateX() > pos.getCoordinateX()) {
                        endPos.setDirection(LEFT);
                        return BACKWARD_LEFT_TURN;
                    }
                    else if(endPos.getCoordinateX() < pos.getCoordinateX()) {
                        endPos.setDirection(RIGHT);
                        return BACKWARD_RIGHT_TURN;
                    }
                    endPos.setDirection(DOWN);
                    return BACKWARD;

                } else {
                    if(endPos.getCoordinateX() > pos.getCoordinateX()) {
                        endPos.setDirection(RIGHT);
                        return FORWARD_LEFT_TURN;
                    }
                    else if(endPos.getCoordinateX() < pos.getCoordinateX()) {
                        endPos.setDirection(LEFT);
                        return FORWARD_RIGHT_TURN;
                    }
                    endPos.setDirection(DOWN);
                    return FORWARD;
                }

            case LEFT:
                if(endPos.getCoordinateX() > pos.getCoordinateX()) {
                    if (endPos.getCoordinateY() > pos.getCoordinateY()) {
                        endPos.setDirection(DOWN);
                        return BACKWARD_RIGHT_TURN;
                    } else if (endPos.getCoordinateY() < pos.getCoordinateY()) {
                        endPos.setDirection(UP);
                        return BACKWARD_LEFT_TURN;
                    }
                    endPos.setDirection(LEFT);
                    return BACKWARD;
                } else {
                    if (endPos.getCoordinateY() > pos.getCoordinateY()) {
                        endPos.setDirection(UP);
                        return FORWARD_RIGHT_TURN;
                    } else if (endPos.getCoordinateY() < pos.getCoordinateY()) {
                        endPos.setDirection(DOWN);
                        return FORWARD_LEFT_TURN;
                    }
                    endPos.setDirection(LEFT);
                    return FORWARD;
                }

            case RIGHT:
                if(endPos.getCoordinateX() > pos.getCoordinateX()) {
                    if (endPos.getCoordinateY() > pos.getCoordinateY()) {
                        endPos.setDirection(UP);
                        return FORWARD_LEFT_TURN;
                    } else if (endPos.getCoordinateY() < pos.getCoordinateY()) {
                        endPos.setDirection(DOWN);
                        return FORWARD_RIGHT_TURN;
                    }
                    endPos.setDirection(RIGHT);
                    return FORWARD;
                } else {
                    if (endPos.getCoordinateY() > pos.getCoordinateY()) {
                        endPos.setDirection(DOWN);
                        return BACKWARD_LEFT_TURN;
                    } else if (endPos.getCoordinateY() < pos.getCoordinateY()) {
                        endPos.setDirection(UP);
                        return BACKWARD_RIGHT_TURN;
                    }
                    endPos.setDirection(RIGHT);
                    return BACKWARD;
                }
            default:
                return null;
        }
    }

    public boolean isAtGoal(PathSegment a, Waypoint b) {
        boolean isWithinRangeX = Math.abs(a.pos.getCoordinateX() - b.getCoordinateX()) <= DISTANCE_FROM_GOAL_LEEWAY;
        boolean isWithinRangeY = Math.abs(a.pos.getCoordinateY() - b.getCoordinateY()) <= DISTANCE_FROM_GOAL_LEEWAY;
        boolean isFacingCorrectDirection = a.pos.getDirection() == b.getDirection();

        return isWithinRangeX && isWithinRangeY && isFacingCorrectDirection;
    }
}
