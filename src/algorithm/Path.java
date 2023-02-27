package algorithm;

import java.util.ArrayList;
import java.util.List;
import car.CarCoordinate;

public class Path {
    List<PathSegment> path;
    int cost;

    public Path(List<PathSegment> path, int cost) {
        this.path = path;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public List<PathSegment> getPath() {
        return path;
    }

    public String getSTMPath() {
        String s = "";
        for (PathSegment e: path) {
//            System.out.println(e.toString());
            if (e.type == null) {
                continue;
            }
            switch(e.type) {
                case FORWARD:
                    s += "w";
                    break;
                case BACKWARD:
                    s += "s";
                    break;
                case FORWARD_LEFT_TURN:
                    s += "z";
                    break;
                case BACKWARD_LEFT_TURN:
                    s += "q";
                    break;
                case FORWARD_RIGHT_TURN:
                    s += "x";
                    break;
                case BACKWARD_RIGHT_TURN:
                    s += "e";
                    break;
                default:
                    continue;
            }
        }

        return s+"i";
    }

    public List<CarCoordinate> getCarCoordinates() {
        ArrayList<CarCoordinate> list = new ArrayList<>();
        CarCoordinate cur;
        double angle;
        for (PathSegment e: path) {
            System.out.println(e.toString());
            if (e.type == null || e.type == MovementType.FORWARD || e.type == MovementType.BACKWARD) {
                cur = new CarCoordinate(e.pos.getCoordinateX()/10, e.pos.getCoordinateY()/10, e.pos.getDirection());
                list.add(cur);
                continue;
            }

            int x = e.pos.getCoordinateX()/10, y = e.pos.getCoordinateY()/10;
            int px = e.parent.pos.getCoordinateX()/10, py = e.parent.pos.getCoordinateY()/10;
            Direction dir = e.pos.getDirection(), pdir = e.parent.pos.getDirection();
            MovementType type = e.type;

            switch (pdir){
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
                    System.out.println("Error");
                    return null;
            }

            if (pdir == Direction.UP || pdir == Direction.DOWN) {
                for (int i = py; i != (y>py ? y+1 : y-1); i += y>py ? 1 : -1) {
                    cur = new CarCoordinate(px, i, pdir);
                    list.add(cur);
                }
                for (int j = px; j != x; j += x>px ? 1 : -1) {
                    cur = new CarCoordinate(j, y, dir);
                    list.add(cur);
                }
            } else {
                for (int j = px; j != x; j += x>px ? 1 : -1) {
                    cur = new CarCoordinate(j, py, pdir);
                    list.add(cur);
                }
                for (int i = py; i != y; i += y>py ? 1 : -1) {
                    cur = new CarCoordinate(x, i, dir);
                    list.add(cur);
                }

            }
            list.add(new CarCoordinate(x, y, dir));
//            else {
//
//                if (e.pos.getDirection() == Direction.RIGHT || e.pos.getDirection() == Direction.LEFT) {
//                    cur = new CarCoordinate(e.parent.pos.getCoordinateX()/10, e.pos.getCoordinateY()/10, e.pos.getDirection());
//                    list.add(cur);
//                    list.add(new CarCoordinate(e.pos.getCoordinateX()/10, e.pos.getCoordinateY()/10, e.pos.getDirection()));
//                } else {
//                    cur = new CarCoordinate(e.pos.getCoordinateX()/10, e.parent.pos.getCoordinateY()/10, e.pos.getDirection());
//                    list.add(cur);
//                    list.add(new CarCoordinate(e.pos.getCoordinateX()/10, e.pos.getCoordinateY()/10, e.pos.getDirection()));
//                }
//            }
        }
        list.add(list.get(list.size()-1));
        return list;
    }
}
