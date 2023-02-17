package algorithm;

import java.util.ArrayList;
import java.util.List;

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

    public List<CarCoordinate> getCarCoordinates() {
        ArrayList<CarCoordinate> list = new ArrayList<>();
        CarCoordinate cur;
        for (PathSegment e: path) {
            if (e.type == null || e.type == MovementType.FORWARD || e.type == MovementType.BACKWARD) {
                cur = new CarCoordinate(e.pos.getCoordinateX()/10, e.pos.getCoordinateY()/10, e.pos.getDirection());
                list.add(cur);
            } else {
                if (e.pos.getDirection() == Direction.UP || e.pos.getDirection() == Direction.DOWN) {
                    cur = new CarCoordinate(e.parent.pos.getCoordinateX()/10, e.pos.getCoordinateY()/10, e.pos.getDirection());
                    list.add(cur);
                } else {
                    cur = new CarCoordinate(e.pos.getCoordinateX()/10, e.parent.pos.getCoordinateY()/10, e.pos.getDirection());
                    list.add(cur);
                }
            }
        }
        return list;
    }
}
