package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.*;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;

public class BombermanGame extends Application {

    public static String[] map = {
            "###############################",
            "#p     ** *  1 * 2 *  * * *   #",
            "# # # #*# # #*#*# # # #*#*#*# #",
            "#  x*     ***  *  1   * 2 * * #",
            "# # # # # #*# # #*#*# # # # #*#",
            "#f         x **  *  *   1     #",
            "# # # # # # # # # #*# #*# # # #",
            "#*  *      *  *      *        #",
            "# # # # #*# # # #*#*# # # # # #",
            "#*    **  *       *           #",
            "# #*# # # # # # #*# # # # # # #",
            "#           *   *  *          #",
            "###############################"
            };

    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;

    private long lastUpdate = System.nanoTime(); // Last time in which `handle()` was called
    private double speed = 1 ; // The snake moves 50 pixels per second

    private GraphicsContext gc;
    private Canvas canvas;
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();


    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        Scene scene = new Scene(root);

        // Them scene vao stage
        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                long elapsedNanoSeconds = now - lastUpdate;
                // 1 second = 1,000,000,000 (1 billion) nanoseconds
                double elapsedSeconds = 3 * elapsedNanoSeconds / 1_000_000_000.0;

                entities.forEach(g -> {
                    stillObjects.get(g.getX()*HEIGHT + g.getY()).render(gc);
                    g.render(gc);
                    g.update((int) elapsedSeconds);
                });

                scene.setOnKeyPressed(e->{
                    if(e.getCode()==KeyCode.UP) {
                        if(checkEntity(stillObjects.get(entities.get(0).getX()*HEIGHT + entities.get(0).getY()-1))) {
                            stillObjects.get(entities.get(0).getX()*HEIGHT + entities.get(0).getY()).render(gc);
                            entities.get(0).setY(entities.get(0).getY()-1);
                        }
                    }
                    else if(e.getCode()==KeyCode.DOWN) {
                        if(checkEntity(stillObjects.get(entities.get(0).getX()*HEIGHT + entities.get(0).getY()+1))) {
                            stillObjects.get(entities.get(0).getX()*HEIGHT + entities.get(0).getY()).render(gc);
                            entities.get(0).setY(entities.get(0).getY()+1);
                        }
                    }
                    else if(e.getCode()==KeyCode.RIGHT) {
                        if(checkEntity(stillObjects.get(entities.get(0).getX()*HEIGHT + entities.get(0).getY()+HEIGHT))) {
                            stillObjects.get(entities.get(0).getX() * HEIGHT + entities.get(0).getY()).render(gc);
                            entities.get(0).setX(entities.get(0).getX() + 1);
                        }
                    }
                    else if(e.getCode()==KeyCode.LEFT) {
                        if(checkEntity(stillObjects.get(entities.get(0).getX()*HEIGHT + entities.get(0).getY()-HEIGHT))) {
                            stillObjects.get(entities.get(0).getX()*HEIGHT + entities.get(0).getY()).render(gc);
                            entities.get(0).setX(entities.get(0).getX()-1);
                        }
                    }
                });
                //render();
            }
        };
        timer.start();

        createMap();
        createEntity();

        stillObjects.forEach(g -> g.render(gc));


    }

    public void createMap() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Entity object;
                if (map[j].charAt(i) == '#') {
                    object = new Wall(i, j, Sprite.wall.getFxImage());
                } else if (map[j].charAt(i) == '*') {
                    object = new Brick(i, j, Sprite.brick.getFxImage());
                } else {
                    object = new Grass(i, j, Sprite.grass.getFxImage());
                }
                stillObjects.add(object);
            }
        }
    }

    public void createEntity() {
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (map[j].charAt(i) == 'p') {
                    Entity object = new Bomber(i, j, Sprite.player_right.getFxImage());
                    entities.add(object);
                } else if (map[j].charAt(i) == '1') {
                    Entity object = new BalloomEnemy(i, j, Sprite.balloom_left1.getFxImage());
                    entities.add(object);
                } else if (map[j].charAt(i) == '2') {
                    Entity object = new OnealEnemy(i, j, Sprite.oneal_left1.getFxImage());
                    entities.add(object);
                }
            }
        }
    }

    //public void update() {
     //   entities.forEach(g->g.update(time));
    //}

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
    }

    public boolean checkEntity(Entity e) {
        if(e instanceof Wall || e instanceof Brick) return false;
        return true;
    }

}