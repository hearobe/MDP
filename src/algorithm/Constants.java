package algorithm;

public class Constants {
    static final int GRID_SIZE_IN_CELLS = 20;

    static final int DISTANCE_FROM_GOAL = 4;
    static final int OBSTACLE_BUFFER = 1;

    static final int FORWARD_MOVEMENT = 1;
    static final int BACKWARD_MOVEMENT = 1;
    static final int SHARP_LEFT_HORI = 3;
    static final int SHARP_LEFT_VERT = 1;
    static final int SHARP_RIGHT_HORI = 3;
    static final int SHARP_RIGHT_VERT = 1;
    static final int FORWARD_LEFT_TURN_HORI = 5;
    static final int FORWARD_LEFT_TURN_VERT = 3;
    static final int FORWARD_RIGHT_TURN_HORI = 5;
    static final int FORWARD_RIGHT_TURN_VERT = 3;
    // backward left turn occurs when vehicle is in reverse, with front wheels turned towards the left
    static final int BACKWARD_LEFT_TURN_HORI = 3;
    static final int BACKWARD_LEFT_TURN_VERT = 5;
    static final int BACKWARD_RIGHT_TURN_HORI = 5;
    static final int BACKWARD_RIGHT_TURN_VERT = 3;

    static final int INNER_CORNER_VERT_CHECK = 2;
    static final int INNER_CORNER_HORI_CHECK = 2;
//    static final int BACKWARD_RIGHT_OUTER_CORNER_VERT_CHECK = 4;
    static final int BACKWARD_RIGHT_OUTER_CORNER_HORI_CHECK = 1;
    static final int SHARP_OUTER_CORNER_VERT_CHECK = 2;
    static final int SHARP_OUTER_CORNER_HORI_CHECK = 2;

    static final int STRAIGHT_COST = 1;
    static final int SHARP_TURN_COST = 8;
    static final int BACKWARD_RIGHT_TURN_COST = 8;
    static final int BACKWARD_LEFT_TURN_COST = 6;
    static final int FORWARD_TURN_COST = 6;
}
