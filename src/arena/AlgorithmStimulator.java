package arena;

import static algorithm.Direction.*;
import java.util.List;

import algorithm.Arena;
import algorithm.Cell;
import algorithm.Direction;
import algorithm.Path;
import algorithm.PathFinder;
import algorithm.PathSequencer;
import algorithm.Waypoint;
import car.Car;
import car.CarCoordinate;
import javafx.animation.AnimationTimer;

// Java Program to create a button and add it to the stage
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import stopwatch.StopWatch;

public class AlgorithmStimulator extends Application {

	private boolean taskStarted, taskPaused;
	private Button startBtn, resetBtn, loadBtn;
	private Label controlLabel, timerHeaderLabel, loadArenaLabel, resetArenaLabel, startArenaLabel, filler;
	private GridPane controlMenu;
	private StopWatch stopWatch;
	private CarCoordinate startingPosition;
	private static Arena arena = new Arena();

	private static Canvas map;
	private myAnimationTimer animateTimer1;

	private Car car;
	private PathSequencer pathSequencer;
	int tmp = 0;
	// launch the application

	class myAnimationTimer extends AnimationTimer {
		private Canvas map;
		private List<List<CarCoordinate>> carPath;
		private int i = 0;
		private int j = 0;
//		private long startTiming = 0;
		private int iterations = 0;
		private long lastUpdate = 0 ;
		private CarCoordinate startingPosition;
		private Car car;

		public myAnimationTimer(Canvas map,  List<List<CarCoordinate>> carPath, Car car, CarCoordinate startPosition) {
			this.map = map;
			this.carPath = carPath;
			this.startingPosition = startPosition;
			this.car = car;
		}

		@Override
		public void start() {
			super.start();
		}

		public void reset() {
			this.i = 0;
			this.j = 0;
			this.car.update(this.startingPosition);

		}

		@Override
		public void handle(long timestamp) {
			if (timestamp - lastUpdate >= 500_000_000) {
				map.getGraphicsContext2D().clearRect(0, 0, map.getWidth(), map.getHeight());
				iterations++;

				drawMap(map, arena);
				drawCar(map, car);
				lastUpdate = timestamp ;
//				System.out.printf("%d %d\n", i, j);
				car.update(carPath.get(this.i).get(j++));
				if(j == carPath.get(i).size()) {
					i++;
					j = 0;
				}

				if(i == carPath.size() || iterations >= 720) {
					stop();
				}

			}

		}
	}

	public void start(Stage window) throws InterruptedException
	{
		window.setTitle("MDP Group 4 Algorithm Demo");
		GridPane g = new GridPane();

		StackPane layout = new StackPane();

		map = new Canvas(ArenaDimensions.ARENA_STIMULATOR_WIDTH,ArenaDimensions.ARENA_STIMULATOR_HEIGHT);
		arena = new Arena();

//		int[] obstacleX = new int[] {7, 8, 17, 1, 14};
//		int[] obstacleY = new int[] {18, 8, 4, 18, 14};
//		Direction[] obstacleDirection = new Direction[] {DOWN, DOWN, LEFT, DOWN,LEFT};

		int[] obstacleX = new int[] {3, 10, 17, 7, 15};
		int[] obstacleY = new int[] {14, 9, 7, 1, 15};
		Direction[] obstacleDirection = new Direction[] {DOWN, DOWN, LEFT, UP,LEFT};

		arena.setObstacles(obstacleX, obstacleY, obstacleDirection);

		startingPosition = new CarCoordinate(1,1, RIGHT);
		car = new Car(2,2, RIGHT);
		car.update(startingPosition);

		pathSequencer = new PathSequencer(arena, this.car.getCarCoordinate());
//
		List<List<CarCoordinate>> carPath = pathSequencer.getPath();

//		for(List<CarCoordinate> l : carPath) {
//			for(CarCoordinate c: l)
////				System.out.println(c.toString());
//		}
		drawMap(map, arena);
		drawCar(map, car);

		animateTimer1 = new myAnimationTimer( map,  carPath,  car,  startingPosition);

		stopWatch = new StopWatch();

		startBtn = new Button("Start ");
		startBtn.setOnAction(e -> {
			String text = startBtn.getText();
			if (text.equals("Start ") || text.equals("Resume")) {

				startBtn.setText("Pause");
				if (text.equals("Start ")) {
					stopWatch.start();
					animateTimer1.start();
				} else {
					animateTimer1.start();
					stopWatch.resume();
				}
			} else {
				startBtn.setText("Resume");
				animateTimer1.stop();
				stopWatch.pause();
			}
		});

		resetBtn = new Button("Reset");
		resetBtn.setOnAction(e -> {
			startBtn.setText("Start ");
			stopWatch.clear();
			car.update(startingPosition);
			drawMap(map, arena);
			drawCar(map, car);
			animateTimer1.reset();
		});



		loadBtn = new Button("Load");
		loadBtn.setVisible(false);
		filler = new Label();

		timerHeaderLabel = new Label("Time Lapsed");
		timerHeaderLabel.setFont(Font.font("Cambria", 20));
		timerHeaderLabel.setAlignment(Pos.CENTER);
		timerHeaderLabel.setStyle("-fx-text-fill: white; -fx-background-color:   DIMGREY");
		timerHeaderLabel.setMaxWidth(1000);


		controlLabel = new Label("Control Menu");
		controlLabel.setStyle("-fx-text-fill: white; -fx-background-color:   DIMGREY");
		controlLabel.setFont(Font.font("Cambria", 20));
		controlLabel.setTextAlignment(TextAlignment.LEFT);
		controlLabel.setAlignment(Pos.CENTER);
		controlLabel.setMaxWidth(800);


		loadArenaLabel = new Label("Load File:        ");
		resetArenaLabel = new Label("Reset Stimulation:  ");
		startArenaLabel = new Label("Start/Stop Stimulation:    ");


		controlMenu = new GridPane();
		controlMenu.setVgap(10);
		//controlMenu.add(loadArenaLabel, 0, 3);
		//controlMenu.add((loadBtn), 2, 3);
		controlMenu.add(resetArenaLabel, 0, 4);
		controlMenu.add(resetBtn, 2, 4);
		controlMenu.add(startArenaLabel, 0, 5);
		controlMenu.add(startBtn, 2, 5);
		controlMenu.setPadding(new Insets(0, 0, 15, 15));


		HBox root = new HBox(8);
		VBox vRoot = new VBox(10);

		vRoot.getChildren().addAll(filler,timerHeaderLabel, stopWatch, controlLabel, controlMenu);
		vRoot.setMargin(timerHeaderLabel, new Insets(30, 15, 0, 15));
		vRoot.setMargin(controlLabel, new Insets(30, 10, 0, 15));

		root.getChildren().addAll(map, vRoot);

		Scene scene = new Scene(root,ArenaDimensions.ARENA_STIMULATOR_WIDTH *1.55 ,ArenaDimensions.ARENA_STIMULATOR_HEIGHT);
		window.setScene(scene);
		window.show();





	}






	private int xCoordConversion(int x) {
		return  x * ArenaDimensions.ARENA_CELL_SIZE + ArenaDimensions.ARENA_PADDING;
	}

	private int yCoordConversion(int y) {
		return   ArenaDimensions.ARENA_HEIGHT - y *ArenaDimensions.ARENA_CELL_SIZE;

	}

	private void drawCar(Canvas map, Car c) {
		int x = xCoordConversion(c.getXCoord() - 1);
		int y = yCoordConversion(c.getYCoord() + 1);

		GraphicsContext gc = map.getGraphicsContext2D();
		gc.setFill(ArenaDimensions.CAR_COLOR);
		int width = c.carWidth * ArenaDimensions.ARENA_CELL_SIZE;
		int height = c.carHeight * ArenaDimensions.ARENA_CELL_SIZE;

		gc.strokeRect(x, y, height, width);
		gc.fillRect(x, y,  height, width);
		gc.setFill(ArenaDimensions.OBSTACLE_COLOR);

		drawTriangle(gc, c.getDir(), xCoordConversion(c.getXCoord()), yCoordConversion(c.getYCoord()));


	}

	private void drawMap(Canvas map, Arena arena) {
		GraphicsContext gc = map.getGraphicsContext2D();

		gc.setLineWidth(1);
		for(int i = 0; i < ArenaDimensions.ARENA_WIDTH_UNITS; i++) {
			for(int j = 0; j < ArenaDimensions.ARENA_HEIGHT_UNITS; j++) {
				drawCell(map, arena, i, j);
			}

			gc.setFill(Color.BLACK);
			//print x-axis
			for(int x = 0; x < ArenaDimensions.ARENA_WIDTH_UNITS; x++) {
				gc.fillText(String.valueOf(x), x *  ArenaDimensions.ARENA_CELL_SIZE + ArenaDimensions.ARENA_PADDING + 5, ArenaDimensions.ARENA_PADDING / 2);
				gc.fillText(String.valueOf(x), x *  ArenaDimensions.ARENA_CELL_SIZE + ArenaDimensions.ARENA_PADDING + 5 ,
						ArenaDimensions.ARENA_HEIGHT + ArenaDimensions.ARENA_PADDING / 2+ ArenaDimensions.ARENA_CELL_SIZE);
			}
			//print y-axis
			for(int y = 0; y < ArenaDimensions.ARENA_HEIGHT_UNITS; y++) {
				gc.fillText(String.valueOf(y), ArenaDimensions.ARENA_PADDING / 2, ArenaDimensions.ARENA_PADDING / 2 + ArenaDimensions.ARENA_HEIGHT -  y * ArenaDimensions.ARENA_CELL_SIZE + 2);
				gc.fillText(String.valueOf(y), ArenaDimensions.ARENA_WIDTH + ArenaDimensions.ARENA_PADDING + 2, ArenaDimensions.ARENA_PADDING /2 + ArenaDimensions.ARENA_HEIGHT -  y * ArenaDimensions.ARENA_CELL_SIZE +2 );

			}
		}

	}

	private void drawCell(Canvas map, Arena arena, int i, int j) {
		GraphicsContext gc = map.getGraphicsContext2D();
		Cell curCell = arena.grid[j][i];
		Color cellColor;

		//choose color based on cell characteristics
		if(i <= ArenaDimensions.STARTZONE_ROW && j <= ArenaDimensions.STARTZONE_COL) {
			cellColor = ArenaDimensions.START_ZONE_COLOR;
		}else if(curCell.isObstacle()) {
			cellColor = ArenaDimensions.OBSTACLE_COLOR;
		}else {
			cellColor = ArenaDimensions.UNEXPLORED_CELL_COLOR;
		}gc.setFill(cellColor);

		//paint cell

		int cellSize =  ArenaDimensions.ARENA_CELL_SIZE;
		int xCoord = xCoordConversion(i);
		int yCoord = yCoordConversion(j);

		gc.strokeRect(xCoord , yCoord, cellSize, cellSize);
		gc.fillRect(xCoord, yCoord,cellSize,  cellSize);
		if(curCell.isObstacle() && curCell.getImageDirection() != null) {
			Direction obstacleDirection = curCell.getImageDirection();
			gc.setFill(ArenaDimensions.IMAGE_POS_COLOR);
			drawObstacleDirection(gc,obstacleDirection, xCoord, yCoord);
		}
	}

	private void drawTriangle(GraphicsContext gc, Direction d, int xCoord, int yCoord) {

		int firstX, secondX, thirdX, firstY, secondY, thirdY;

		switch(d) {
			case DOWN:
				firstX = xCoord + ArenaDimensions.ARENA_CELL_SIZE / 2;
				secondX = xCoord + ArenaDimensions.ARENA_CELL_SIZE;
				thirdX = xCoord;
				firstY = yCoord + ArenaDimensions.ARENA_CELL_SIZE;
				secondY = yCoord;
				thirdY = yCoord;
				break;

			case UP:
				firstX = xCoord;
				secondX = xCoord + ArenaDimensions.ARENA_CELL_SIZE;
				thirdX = xCoord + ArenaDimensions.ARENA_CELL_SIZE / 2;
				firstY = yCoord + ArenaDimensions.ARENA_CELL_SIZE;
				secondY = yCoord + ArenaDimensions.ARENA_CELL_SIZE;
				thirdY = yCoord;
				break;

			case LEFT:
				firstX = xCoord;
				secondX = xCoord + ArenaDimensions.ARENA_CELL_SIZE;
				thirdX = xCoord + ArenaDimensions.ARENA_CELL_SIZE;
				firstY = yCoord +  ArenaDimensions.ARENA_CELL_SIZE / 2;
				secondY= yCoord;
				thirdY = yCoord + ArenaDimensions.ARENA_CELL_SIZE;
				break;

			default:
				firstX = xCoord;
				secondX = xCoord + ArenaDimensions.ARENA_CELL_SIZE;
				thirdX = xCoord;
				firstY = yCoord;
				secondY = yCoord + ArenaDimensions.ARENA_CELL_SIZE / 2;
				thirdY = yCoord + ArenaDimensions.ARENA_CELL_SIZE;
				break;
		}

		gc.fillPolygon(new double[]{firstX, secondX,thirdX},new double[]{firstY, secondY, thirdY},3);
		gc.strokePolygon(new double[]{firstX, secondX,thirdX},new double[]{firstY, secondY, thirdY},3);
	}

	private void drawObstacleDirection(GraphicsContext gc, Direction d, int xCoord, int yCoord) {

		int width = ArenaDimensions.OBSTACLE_DIRECTION_WIDTH;
		int height = ArenaDimensions.OBSTACLE_DIRECTION_HEIGHT;
		switch(d) {
			case DOWN:
				int y = yCoord + ArenaDimensions.ARENA_CELL_SIZE - ArenaDimensions.OBSTACLE_DIRECTION_HEIGHT;
				gc.fillRect(xCoord, y, width, height );
				gc.strokeRect(xCoord, y, width, height );
				break;

			case UP:
				gc.fillRect(xCoord, yCoord, width, height );
				gc.strokeRect(xCoord, yCoord, width, height );
				break;

			case LEFT:
				gc.fillRect(xCoord, yCoord, height, width);
				gc.strokeRect(xCoord, yCoord, height, width);
				break;

			default:
				int x = xCoord + ArenaDimensions.ARENA_CELL_SIZE - ArenaDimensions.OBSTACLE_DIRECTION_HEIGHT;
				gc.fillRect(x, yCoord,  height, width);
				gc.strokeRect(x, yCoord,  height, width );

				break;
		}
	}

	private void drawCarPosition(GraphicsContext gc, Car c) {
		gc.setFill(Color.GREEN);

		drawTriangle(gc, c.getDir(), xCoordConversion(c.getXCoord()),
				yCoordConversion(c.getYCoord()));
	}




	public static void main(String args[])
	{
		launch(args);
	}
}