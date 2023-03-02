package algorithm;

public enum Direction {
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public static Direction parseDir(String s) {
        switch (s) {
            case "n":
                return UP;
            case "s":
                return DOWN;
            case "e":
                return RIGHT;
            case "w":
                return LEFT;
            default:
                System.out.println("Invalid Direction");
                return null;
        }
    }

    public Direction turnBack() {
        return Direction.values()[(this.ordinal()+2) % 4];
    }

    public Direction turnRight() {
        return Direction.values()[(this.ordinal()+1) % 4];
    }

    public Direction turnLeft() {
        return Direction.values()[(this.ordinal()+3) % 4];
    }
}


