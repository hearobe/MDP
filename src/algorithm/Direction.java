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
}


