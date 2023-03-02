package algorithm;

import car.Coordinate;

import java.util.*;

import static algorithm.Constants.*;
import static algorithm.Direction.*;
import static algorithm.MovementType.*;

public class PathFinder {

    private ArrayList<PathSegment> visited = new ArrayList<PathSegment>();
    private PriorityQueue<PathSegment> queue = new PriorityQueue<>();
    private PathSegment lastMove = null;

    public PathFinder() {}

    // TODO: change starting and ending points to cells instead of waypoints
    // turns should round up to the cell (i.e. if its 5 cm just move 5 cm more before turning)
    public Path findPathBetweenTwoNodes(Coordinate a, Coordinate b, Arena g) {
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

                    ArrayList<PathSegment> pathSegments = new ArrayList<>();
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
        int row = a.getPos().getY();
        int col = a.getPos().getX();
        Direction d = a.getPos().getDir();

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
                return null;
        }

        Cell straight, left, right;
        Coordinate newPoint;
        boolean isLeftPossible = true, isRightPossible = true;

        straight = new Cell(col-(int)Math.cos(angle), row+(int)Math.sin(angle));
        if (g.validCell(straight)) {
            newPoint = new Coordinate(a.getPos().getX()-FORWARD_MOVEMENT*(int)Math.cos(angle),
                    a.getPos().getY()+FORWARD_MOVEMENT*(int)Math.sin(angle), d);
            candidates.add(getPathSegment(a, newPoint));
        }

        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 2; j++) {
                left = new Cell(col - i * (int) Math.cos(angle) - j * (int) Math.sin(angle),
                        row + i * (int) Math.sin(angle) - j * (int) Math.cos(angle));
                if (!g.validCell(left)) {
                    isLeftPossible = false;
                    break;
                }
            }
        }
        if (isLeftPossible) {
            newPoint = new Coordinate(a.getPos().getX() - FORWARD_LEFT_TURN_VERT * (int) Math.cos(angle) - FORWARD_LEFT_TURN_HORI * (int) Math.sin(angle),
                    a.getPos().getY() + FORWARD_LEFT_TURN_VERT * (int) Math.sin(angle) - FORWARD_LEFT_TURN_HORI * (int) Math.cos(angle),
                    d.turnLeft());
            candidates.add(getPathSegment(a, newPoint));
        }

        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 2; j++) {
                right = new Cell(col - i * (int) Math.cos(angle) + j * (int) Math.sin(angle),
                        row + i * (int) Math.sin(angle) + j * (int) Math.cos(angle));
                if (!g.validCell(right)) {
                    isRightPossible = false;
                    break;
                }
            }
        }
        if (isRightPossible) {
            newPoint = new Coordinate(a.getPos().getX()-FORWARD_RIGHT_TURN_VERT*(int)Math.cos(angle)+FORWARD_RIGHT_TURN_HORI*(int)Math.sin(angle),
                    a.getPos().getY()+FORWARD_RIGHT_TURN_VERT*(int)Math.sin(angle)+FORWARD_RIGHT_TURN_HORI*(int)Math.cos(angle),
                    d.turnRight());
            candidates.add(getPathSegment(a, newPoint));
        }

        // BACKWARD
        isLeftPossible = true;
        isRightPossible = true;

        straight = new Cell(col+(int)Math.cos(angle), row-(int)Math.sin(angle));
        if (g.validCell(straight)) {
            newPoint = new Coordinate(a.getPos().getX()+BACKWARD_MOVEMENT*(int)Math.cos(angle),
                    a.getPos().getY()-BACKWARD_MOVEMENT*(int)Math.sin(angle),
                    d);
            candidates.add(getPathSegment(a, newPoint));
        }

        // TODO: check with STM if the y-displacement is 5 or 4
        for (int i = 0; i <= 4; i++) {
            left = new Cell(col + i * (int) Math.cos(angle),
                    row - i * (int) Math.sin(angle));
            if (!g.validCell(left)) {
                isLeftPossible = false;
                break;
            }
            // TODO: check if this is necessary with STM
            left = new Cell(col + i * (int) Math.cos(angle) + 1 * (int) Math.sin(angle),
                    row - i * (int) Math.sin(angle) + 1 * (int) Math.cos(angle));
            if (!g.validCell(left)) {
                isLeftPossible = false;
                break;
            }
            if (i == 4) {
                for (int j = 0; j <= 3; j++) {
                    left = new Cell(col + i * (int) Math.cos(angle) - j * (int) Math.sin(angle),
                            row - i * (int) Math.sin(angle) - j * (int) Math.cos(angle));
                    if (!g.validCell(left)) {
                        isLeftPossible = false;
                        break;
                    }
                }
            }
        }
        if (isLeftPossible) {
            newPoint = new Coordinate(a.getPos().getX() + BACKWARD_LEFT_TURN_VERT * (int) Math.cos(angle) - BACKWARD_LEFT_TURN_HORI * (int) Math.sin(angle),
                    a.getPos().getY() - BACKWARD_LEFT_TURN_VERT * (int) Math.sin(angle) - BACKWARD_LEFT_TURN_HORI * (int) Math.cos(angle),
                    d.turnRight());
            candidates.add(getPathSegment(a, newPoint));
        }

        for (int i = 0; i <= 4; i++) {
            right = new Cell(col + i * (int) Math.cos(angle),
                    row - i * (int) Math.sin(angle));
            if (!g.validCell(right)) {
                isRightPossible = false;
                break;
            }
            right = new Cell(col + i * (int) Math.cos(angle) - 1 * (int) Math.sin(angle),
                    row - i * (int) Math.sin(angle) - 1 * (int) Math.cos(angle));
            if (!g.validCell(right)) {
                isRightPossible = false;
            }
            if (i == 4) {
                for (int j = 0; j <= 3; j++) {
                    right = new Cell(col + i * (int) Math.cos(angle) + j * (int) Math.sin(angle),
                            row - i * (int) Math.sin(angle) + j * (int) Math.cos(angle));
                    if (!g.validCell(right)) {
                        isRightPossible = false;
                        break;
                    }
                }
            }
        }
        if (isRightPossible) {
            newPoint = new Coordinate(a.getPos().getX()+BACKWARD_RIGHT_TURN_VERT*(int)Math.cos(angle)+BACKWARD_RIGHT_TURN_HORI*(int)Math.sin(angle),
                    a.getPos().getY()-BACKWARD_RIGHT_TURN_VERT*(int)Math.sin(angle)+BACKWARD_RIGHT_TURN_HORI*(int)Math.cos(angle),
                    d.turnLeft());
            candidates.add(getPathSegment(a, newPoint));
        }

        return candidates;
    }

    public PathSegment getPathSegment(PathSegment a, Coordinate newPoint) {
        PathSegment newPath = new PathSegment(a, newPoint, getMovementType(a.getPos(), newPoint));

        return newPath;
    }

    public MovementType getMovementType(Coordinate pos, Coordinate endPos) {
        switch(pos.getDir()) {
            case UP:
                if(endPos.getY() > pos.getY()) {
                    if(endPos.getX() > pos.getX()) {
                        return FORWARD_RIGHT_TURN;
                    }
                    else if(endPos.getX() < pos.getX()) {
                        return FORWARD_LEFT_TURN;
                    }
                    return FORWARD;
                } else {
                    if(endPos.getX() > pos.getX()) {
                        return BACKWARD_RIGHT_TURN;
                    }
                    else if(endPos.getX() < pos.getX()) {
                        return BACKWARD_LEFT_TURN;
                    }
                    return BACKWARD;
                }

            case DOWN:
                if(endPos.getY() > pos.getY()) {
                    if(endPos.getX() > pos.getX()) {
                        return BACKWARD_LEFT_TURN;
                    }
                    else if(endPos.getX() < pos.getX()) {
                        return BACKWARD_RIGHT_TURN;
                    }
                    return BACKWARD;

                } else {
                    if(endPos.getX() > pos.getX()) {
                        return FORWARD_LEFT_TURN;
                    }
                    else if(endPos.getX() < pos.getX()) {
                        return FORWARD_RIGHT_TURN;
                    }
                    return FORWARD;
                }

            case LEFT:
                if(endPos.getX() > pos.getX()) {
                    if (endPos.getY() > pos.getY()) {
                        return BACKWARD_RIGHT_TURN;
                    } else if (endPos.getY() < pos.getY()) {
                        return BACKWARD_LEFT_TURN;
                    }
                    return BACKWARD;
                } else {
                    if (endPos.getY() > pos.getY()) {
                        return FORWARD_RIGHT_TURN;
                    } else if (endPos.getY() < pos.getY()) {
                        return FORWARD_LEFT_TURN;
                    }
                    return FORWARD;
                }

            case RIGHT:
                if(endPos.getX() > pos.getX()) {
                    if (endPos.getY() > pos.getY()) {
                        return FORWARD_LEFT_TURN;
                    } else if (endPos.getY() < pos.getY()) {
                        return FORWARD_RIGHT_TURN;
                    }
                    return FORWARD;
                } else {
                    if (endPos.getY() > pos.getY()) {
                        return BACKWARD_LEFT_TURN;
                    } else if (endPos.getY() < pos.getY()) {
                        return BACKWARD_RIGHT_TURN;
                    }
                    return BACKWARD;
                }
            default:
                return null;
        }
    }

    public boolean isAtGoal(PathSegment a, Coordinate b) {
        return a.getPos().equals(b);
    }
}
