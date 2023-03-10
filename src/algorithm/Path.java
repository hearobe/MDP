package algorithm;

import java.util.ArrayList;
import java.util.List;
import car.Coordinate;

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
            if (e.type == null) {
                continue;
            }
            switch(e.type) {
                case FORWARD:
                    if (s.length() > 0 && s.charAt(s.length()-1) == 'w') {
                        try {
                            if (Integer.parseInt(String.valueOf(s.charAt(s.length()-2))) == 4) {
                                s += "w";
                            } else {
                                int multiplier = Integer.parseInt(String.valueOf(s.charAt(s.length()-2))) + 1;
                                s = s.substring(0, s.length()-2) + multiplier + "w";
                            }
                        } catch (NumberFormatException | StringIndexOutOfBoundsException q) {
                            s = s.substring(0, s.length()-1) + "2w";
                        }
                    } else {
                        s += "w";
                    }
                    break;
                case BACKWARD:
                    if (s.length() > 0 && s.charAt(s.length()-1) == 's') {
                        try {
                            if (Integer.parseInt(String.valueOf(s.charAt(s.length()-2))) == 4) {
                                s += "s";
                            } else {
                                int multiplier = Integer.parseInt(String.valueOf(s.charAt(s.length()-2))) + 1;
                                s = s.substring(0, s.length()-2) + multiplier + "s";
                            }
                        } catch (NumberFormatException | StringIndexOutOfBoundsException q) {
                            s = s.substring(0, s.length()-1) + "2s";
                        }
                    } else {
                        s += "s";
                    }
                    break;
                case FORWARD_SHARP_LEFT_TURN:
                    s += "z";
                    break;
                case BACKWARD_LEFT_TURN:
                    s += "q";
                    break;
                case FORWARD_SHARP_RIGHT_TURN:
                    s += "x";
                    break;
                case BACKWARD_RIGHT_TURN:
                    s += "e";
                    break;
                case FORWARD_LEFT_TURN:
                    s += "a";
                    break;
                case FORWARD_RIGHT_TURN:
                    s += "d";
                    break;
                default:
                    continue;
            }
        }

        return s+"i";
    }

    public List<Coordinate> getCarCoordinates() {
        ArrayList<Coordinate> list = new ArrayList<>();
        Coordinate cur;
        for (PathSegment e: path) {
            if (e.type == null || e.type == MovementType.FORWARD || e.type == MovementType.BACKWARD) {
                list.add(e.getPos());
                continue;
            }

            int x = e.pos.getX(), y = e.pos.getY();
            int px = e.parent.pos.getX(), py = e.parent.pos.getY();
            Direction dir = e.pos.getDir(), pdir = e.parent.pos.getDir();

            if (pdir == Direction.UP || pdir == Direction.DOWN) {
                for (int i = py; i != (y>py ? y+1 : y-1); i += y>py ? 1 : -1) {
                    cur = new Coordinate(px, i, pdir);
                    list.add(cur);
                }
                for (int j = px; j != x; j += x>px ? 1 : -1) {
                    cur = new Coordinate(j, y, dir);
                    list.add(cur);
                }
            } else {
                for (int j = px; j != x; j += x>px ? 1 : -1) {
                    cur = new Coordinate(j, py, pdir);
                    list.add(cur);
                }
                for (int i = py; i != y; i += y>py ? 1 : -1) {
                    cur = new Coordinate(x, i, dir);
                    list.add(cur);
                }

            }
            list.add(new Coordinate(x, y, dir));
        }
        list.add(list.get(list.size()-1));
        return list;
    }
}
