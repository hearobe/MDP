package algorithm;

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
}
