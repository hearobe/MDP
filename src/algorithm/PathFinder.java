package algorithm;

import car.Coordinate;

import java.util.*;

import static algorithm.Constants.*;
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
            newPoint = new Coordinate(col-FORWARD_MOVEMENT*(int)Math.cos(angle),
                    row+FORWARD_MOVEMENT*(int)Math.sin(angle), d);
            candidates.add(new PathSegment(a, newPoint, FORWARD));
        }

        for (int i = 0; i <= SHARP_RIGHT_VERT; i++) {
            if (i == SHARP_RIGHT_VERT) {
                for (int j = 0; j <= SHARP_RIGHT_HORI; j++) {
                    left = new Cell(col - i * (int) Math.cos(angle) - j * (int) Math.sin(angle),
                            row + i * (int) Math.sin(angle) - j * (int) Math.cos(angle));
                    if (!g.validCell(left)) {
                        isLeftPossible = false;
                        break;
                    }
                }
            }
        }
        left = new Cell(col - SHARP_OUTER_CORNER_VERT_CHECK * (int) Math.cos(angle) - SHARP_OUTER_CORNER_HORI_CHECK * (int) Math.sin(angle),
                row + SHARP_OUTER_CORNER_VERT_CHECK * (int) Math.sin(angle) - SHARP_OUTER_CORNER_HORI_CHECK * (int) Math.cos(angle));
        if (!g.validCell(left)) {
            isLeftPossible = false;
        }
        if (isLeftPossible) {
            newPoint = new Coordinate(col - SHARP_LEFT_VERT * (int) Math.cos(angle) - SHARP_LEFT_HORI * (int) Math.sin(angle),
                    row + SHARP_LEFT_VERT * (int) Math.sin(angle) - SHARP_LEFT_HORI * (int) Math.cos(angle),
                    d.turnLeft());
            candidates.add(new PathSegment(a, newPoint, FORWARD_SHARP_LEFT_TURN));
        }

        for (int i = 0; i <= SHARP_RIGHT_VERT; i++) {
            if (i == SHARP_RIGHT_VERT) {
                for (int j = 0; j <= SHARP_RIGHT_HORI; j++) {
                    right = new Cell(col - i * (int) Math.cos(angle) + j * (int) Math.sin(angle),
                            row + i * (int) Math.sin(angle) + j * (int) Math.cos(angle));
                    if (!g.validCell(right)) {
                        isRightPossible = false;
                        break;
                    }
                }
            }
        }
        right = new Cell(col - SHARP_OUTER_CORNER_VERT_CHECK * (int) Math.cos(angle) + SHARP_OUTER_CORNER_HORI_CHECK * (int) Math.sin(angle),
                row + SHARP_OUTER_CORNER_VERT_CHECK * (int) Math.sin(angle) + SHARP_OUTER_CORNER_HORI_CHECK * (int) Math.cos(angle));
        if (!g.validCell(right)) {
            isRightPossible = false;
        }
        if (isRightPossible) {
            newPoint = new Coordinate(col- SHARP_RIGHT_VERT *(int)Math.cos(angle)+ SHARP_RIGHT_HORI *(int)Math.sin(angle),
                    row+ SHARP_RIGHT_VERT *(int)Math.sin(angle)+ SHARP_RIGHT_HORI *(int)Math.cos(angle),
                    d.turnRight());
            candidates.add(new PathSegment(a, newPoint, FORWARD_SHARP_RIGHT_TURN));
        }

        // ----------------------------- FORWARD WIDE

        for (int i = 0; i <= FORWARD_LEFT_TURN_VERT; i++) {
            left = new Cell(col - i * (int) Math.cos(angle),
                    row + i * (int) Math.sin(angle));
            if (!g.validCell(left)) {
                isLeftPossible = false;
                break;
            }
            if (i == INNER_CORNER_VERT_CHECK) {
                left = new Cell(col - INNER_CORNER_VERT_CHECK * (int) Math.cos(angle) - INNER_CORNER_HORI_CHECK * (int) Math.sin(angle),
                        row + INNER_CORNER_VERT_CHECK * (int) Math.sin(angle) - INNER_CORNER_HORI_CHECK * (int) Math.cos(angle));
                if (!g.validCell(left)) {
                    isLeftPossible = false;
                    break;
                }
            }
            if (i == FORWARD_LEFT_TURN_VERT) {
                for (int j = 0; j <= FORWARD_LEFT_TURN_HORI; j++) {
                    left = new Cell(col - i * (int) Math.cos(angle) - j * (int) Math.sin(angle),
                            row + i * (int) Math.sin(angle) - j * (int) Math.cos(angle));
                    if (!g.validCell(left)) {
                        isLeftPossible = false;
                        break;
                    }
                }
            }
        }
        if (isLeftPossible) {
            newPoint = new Coordinate(col - FORWARD_LEFT_TURN_VERT * (int) Math.cos(angle) - FORWARD_LEFT_TURN_HORI * (int) Math.sin(angle),
                    row + FORWARD_LEFT_TURN_VERT * (int) Math.sin(angle) - FORWARD_LEFT_TURN_HORI * (int) Math.cos(angle),
                    d.turnLeft());
            candidates.add(new PathSegment(a, newPoint, FORWARD_LEFT_TURN));
        }

        for (int i = 0; i <= FORWARD_RIGHT_TURN_VERT; i++) {
            right = new Cell(col - i * (int) Math.cos(angle),
                    row + i * (int) Math.sin(angle));
            if (!g.validCell(right)) {
                isRightPossible = false;
                break;
            }
            if (i == INNER_CORNER_VERT_CHECK) { // if block is used to make sure this inner corner is only checked once
                right = new Cell(col - INNER_CORNER_VERT_CHECK * (int) Math.cos(angle) + INNER_CORNER_HORI_CHECK * (int) Math.sin(angle),
                        row + INNER_CORNER_VERT_CHECK * (int) Math.sin(angle) + INNER_CORNER_HORI_CHECK * (int) Math.cos(angle));
                if (!g.validCell(right)) {
                    isRightPossible = false;
                    break;
                }
            }
            if (i == FORWARD_RIGHT_TURN_VERT) {
                for (int j = 0; j <= FORWARD_RIGHT_TURN_HORI; j++) {
                    right = new Cell(col - i * (int) Math.cos(angle) + j * (int) Math.sin(angle),
                            row + i * (int) Math.sin(angle) + j * (int) Math.cos(angle));
                    if (!g.validCell(right)) {
                        isRightPossible = false;
                        break;
                    }
                }
            }
        }
        if (isRightPossible) {
            newPoint = new Coordinate(col-FORWARD_RIGHT_TURN_VERT*(int)Math.cos(angle)+FORWARD_RIGHT_TURN_HORI*(int)Math.sin(angle),
                    row+FORWARD_RIGHT_TURN_VERT*(int)Math.sin(angle)+FORWARD_RIGHT_TURN_HORI*(int)Math.cos(angle),
                    d.turnRight());
            candidates.add(new PathSegment(a, newPoint, FORWARD_RIGHT_TURN));
        }

        // -----------------------------

        // BACKWARD
        isLeftPossible = true;
        isRightPossible = true;

        straight = new Cell(col+(int)Math.cos(angle), row-(int)Math.sin(angle));
        if (g.validCell(straight)) {
            newPoint = new Coordinate(col+BACKWARD_MOVEMENT*(int)Math.cos(angle),
                    row-BACKWARD_MOVEMENT*(int)Math.sin(angle),
                    d);
            candidates.add(new PathSegment(a, newPoint, BACKWARD));
        }

        for (int i = 0; i <= BACKWARD_LEFT_TURN_VERT; i++) {
            left = new Cell(col + i * (int) Math.cos(angle),
                    row - i * (int) Math.sin(angle));
            if (!g.validCell(left)) {
                isLeftPossible = false;
                break;
            }
            if (i == INNER_CORNER_VERT_CHECK) {
                left = new Cell(col + INNER_CORNER_VERT_CHECK * (int) Math.cos(angle) - INNER_CORNER_HORI_CHECK * (int) Math.sin(angle),
                        row - INNER_CORNER_VERT_CHECK * (int) Math.sin(angle) - INNER_CORNER_HORI_CHECK * (int) Math.cos(angle));
                if (!g.validCell(left)) {
                    isLeftPossible = false;
                    break;
                }
            }
            if (i == BACKWARD_LEFT_TURN_VERT) {
                for (int j = 0; j <= BACKWARD_LEFT_TURN_HORI; j++) {
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
            newPoint = new Coordinate(col + BACKWARD_LEFT_TURN_VERT * (int) Math.cos(angle) - BACKWARD_LEFT_TURN_HORI * (int) Math.sin(angle),
                    row - BACKWARD_LEFT_TURN_VERT * (int) Math.sin(angle) - BACKWARD_LEFT_TURN_HORI * (int) Math.cos(angle),
                    d.turnRight());
            candidates.add(new PathSegment(a, newPoint, BACKWARD_LEFT_TURN));
        }

        for (int i = 0; i <= BACKWARD_RIGHT_TURN_VERT; i++) {
            right = new Cell(col + i * (int) Math.cos(angle),
                    row - i * (int) Math.sin(angle));
            if (!g.validCell(right)) {
                isRightPossible = false;
                break;
            }
            right = new Cell(col - BACKWARD_RIGHT_OUTER_CORNER_HORI_CHECK * (int) Math.sin(angle),
                    row - BACKWARD_RIGHT_OUTER_CORNER_HORI_CHECK * (int) Math.cos(angle));
            if (!g.validCell(right)) {
                isRightPossible = false;
                break;
            }
            if (i == INNER_CORNER_VERT_CHECK) { // if block is used to make sure this inner corner is only checked once
                right = new Cell(col + INNER_CORNER_VERT_CHECK * (int) Math.cos(angle) + INNER_CORNER_HORI_CHECK * (int) Math.sin(angle),
                        row - INNER_CORNER_VERT_CHECK * (int) Math.sin(angle) + INNER_CORNER_HORI_CHECK * (int) Math.cos(angle));
                if (!g.validCell(right)) {
                    isRightPossible = false;
                }
            }
            if (i == BACKWARD_RIGHT_TURN_VERT) {
//                right = new Cell(col + BACKWARD_RIGHT_OUTER_CORNER_VERT_CHECK * (int) Math.cos(angle) + BACKWARD_RIGHT_OUTER_CORNER_HORI_CHECK * (int) Math.sin(angle),
//                        row - BACKWARD_RIGHT_OUTER_CORNER_HORI_CHECK * (int) Math.sin(angle) + BACKWARD_RIGHT_OUTER_CORNER_HORI_CHECK * (int) Math.cos(angle));
//                if (!g.validCell(right)) {
//                    isRightPossible = false;
//                    break;
//                }
                for (int j = 0; j <= BACKWARD_RIGHT_TURN_HORI; j++) {
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
            newPoint = new Coordinate(col+BACKWARD_RIGHT_TURN_VERT*(int)Math.cos(angle)+BACKWARD_RIGHT_TURN_HORI*(int)Math.sin(angle),
                    row-BACKWARD_RIGHT_TURN_VERT*(int)Math.sin(angle)+BACKWARD_RIGHT_TURN_HORI*(int)Math.cos(angle),
                    d.turnLeft());
            candidates.add(new PathSegment(a, newPoint, BACKWARD_RIGHT_TURN));
        }

        return candidates;
    }

//    public PathSegment getPathSegment(PathSegment a, Coordinate newPoint) {
//        PathSegment newPath = new PathSegment(a, newPoint, getMovementType(a.getPos(), newPoint));
//
//        return newPath;
//    }

//    public MovementType getMovementType(Coordinate pos, Coordinate endPos) {
//        switch(pos.getDir()) {
//            case UP:
//                if(endPos.getY() > pos.getY()) {
//                    if(endPos.getX() > pos.getX()) {
//                        return FORWARD_SHARP_RIGHT_TURN;
//                    }
//                    else if(endPos.getX() < pos.getX()) {
//                        return FORWARD_SHARP_LEFT_TURN;
//                    }
//                    return FORWARD;
//                } else {
//                    if(endPos.getX() > pos.getX()) {
//                        return BACKWARD_RIGHT_TURN;
//                    }
//                    else if(endPos.getX() < pos.getX()) {
//                        return BACKWARD_LEFT_TURN;
//                    }
//                    return BACKWARD;
//                }
//
//            case DOWN:
//                if(endPos.getY() > pos.getY()) {
//                    if(endPos.getX() > pos.getX()) {
//                        return BACKWARD_LEFT_TURN;
//                    }
//                    else if(endPos.getX() < pos.getX()) {
//                        return BACKWARD_RIGHT_TURN;
//                    }
//                    return BACKWARD;
//
//                } else {
//                    if(endPos.getX() > pos.getX()) {
//                        return FORWARD_SHARP_LEFT_TURN;
//                    }
//                    else if(endPos.getX() < pos.getX()) {
//                        return FORWARD_SHARP_RIGHT_TURN;
//                    }
//                    return FORWARD;
//                }
//
//            case LEFT:
//                if(endPos.getX() > pos.getX()) {
//                    if (endPos.getY() > pos.getY()) {
//                        return BACKWARD_RIGHT_TURN;
//                    } else if (endPos.getY() < pos.getY()) {
//                        return BACKWARD_LEFT_TURN;
//                    }
//                    return BACKWARD;
//                } else {
//                    if (endPos.getY() > pos.getY()) {
//                        return FORWARD_SHARP_RIGHT_TURN;
//                    } else if (endPos.getY() < pos.getY()) {
//                        return FORWARD_SHARP_LEFT_TURN;
//                    }
//                    return FORWARD;
//                }
//
//            case RIGHT:
//                if(endPos.getX() > pos.getX()) {
//                    if (endPos.getY() > pos.getY()) {
//                        return FORWARD_SHARP_LEFT_TURN;
//                    } else if (endPos.getY() < pos.getY()) {
//                        return FORWARD_SHARP_RIGHT_TURN;
//                    }
//                    return FORWARD;
//                } else {
//                    if (endPos.getY() > pos.getY()) {
//                        return BACKWARD_LEFT_TURN;
//                    } else if (endPos.getY() < pos.getY()) {
//                        return BACKWARD_RIGHT_TURN;
//                    }
//                    return BACKWARD;
//                }
//            default:
//                return null;
//        }
//    }

    public boolean isAtGoal(PathSegment a, Coordinate b) {
        return a.getPos().equals(b);
    }
}
