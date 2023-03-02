package algorithm;

public class Constants {
    static final int GRID_SIZE_IN_CELLS = 20;
    static final int DISTANCE_FROM_GOAL_LEEWAY = 5;

    static final int FORWARD_MOVEMENT = 1;
    static final int BACKWARD_MOVEMENT = 1;
    static final int FORWARD_LEFT_TURN_HORI = 2;
    static final int FORWARD_LEFT_TURN_VERT = 1;
    static final int FORWARD_RIGHT_TURN_HORI = 2;
    static final int FORWARD_RIGHT_TURN_VERT = 1;
    // backward left turn occurs when vehicle is in reverse, with front wheels turned towards the left
    static final int BACKWARD_LEFT_TURN_HORI = 3;
    static final int BACKWARD_LEFT_TURN_VERT = 5;
    static final int BACKWARD_RIGHT_TURN_HORI = 3;
    static final int BACKWARD_RIGHT_TURN_VERT = 5;

    // TODO: manhattan dist cost is lower now due to using Coordinates. Consider lowering these
    static final int STRAIGHT_COST = 1;
    static final int FORWARD_TURN_COST = 6;
    static final int BACKWARD_TURN_COST = 8;
}
