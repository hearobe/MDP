package arena;

import javafx.scene.paint.Color;

public class ArenaDimensions{
    public static final int ARENA_HEIGHT_UNITS = 20;
    public static final int ARENA_WIDTH_UNITS = 20;
    public static final int ARENA_CELL_SIZE = 20;   
    public static final int ARENA_PADDING = 25;    
    
    public static final int ARENA_WIDTH = ARENA_CELL_SIZE * ARENA_WIDTH_UNITS;
	public static final int ARENA_HEIGHT = ARENA_CELL_SIZE * ARENA_HEIGHT_UNITS;
    public static final int ARENA_STIMULATOR_WIDTH = ARENA_WIDTH + 2 * ARENA_PADDING;
    public static final int ARENA_STIMULATOR_HEIGHT = ARENA_HEIGHT + 2 * ARENA_PADDING;
    

    public static final int OBSTACLE_DIRECTION_WIDTH = ARENA_CELL_SIZE;    
    public static final int OBSTACLE_DIRECTION_HEIGHT = ARENA_CELL_SIZE / 4;

    public static final int STARTZONE_ROW = 2;    
    public static final int STARTZONE_COL = 2;

    

    //Graphic Constants
    public static final javafx.scene.paint.Color START_ZONE_COLOR = javafx.scene.paint.Color.YELLOW;	//Start Zone Color
    public static final javafx.scene.paint.Color OBSTACLE_COLOR = javafx.scene.paint.Color.BLACK;	//Obstacle Color
    public static final javafx.scene.paint.Color CELL_BORDER_COLOR = javafx.scene.paint.Color.LIGHTGRAY;	//Cell Border Color
    public static final javafx.scene.paint.Color PATH_COLOR  = Color.GREEN; //Path Color
    public static final javafx.scene.paint.Color UNEXPLORED_CELL_COLOR  = Color.WHITE; //Unexplored Color
    public static final javafx.scene.paint.Color IMAGE_POS_COLOR = Color.ORANGE; //Image Position Color
    public static final javafx.scene.paint.Color CAR_COLOR = Color.ALICEBLUE; //Image Position Color

}