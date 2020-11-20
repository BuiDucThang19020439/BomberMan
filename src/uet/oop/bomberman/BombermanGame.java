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

    private long lastUpdate; // Last time in which `handle()` was called
    private int speed = 2 ; // The snake moves 4 pixels per second

    private int dx;
    private int dy;

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

        lastUpdate = System.nanoTime();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                long elapsedNanoSeconds = now - lastUpdate;
                // 1 second = 1,000,000,000 (1 billion) nanoseconds
                double elapsedSeconds = 6 * elapsedNanoSeconds / 1_000_000_000.0;

                entities.forEach(g -> {
//                    stillObjects.get(g.getX() * HEIGHT/Sprite.SCALED_SIZE + g.getY()/Sprite.SCALED_SIZE).render(gc);
//                    stillObjects.get(g.getX() * HEIGHT/Sprite.SCALED_SIZE + g.getY()/Sprite.SCALED_SIZE + 1).render(gc);
//                    stillObjects.get(g.getX() * HEIGHT/Sprite.SCALED_SIZE + g.getY()/Sprite.SCALED_SIZE - 1).render(gc);
//                    stillObjects.get(g.getX() * HEIGHT/Sprite.SCALED_SIZE + g.getY()/Sprite.SCALED_SIZE - HEIGHT).render(gc);
//                    stillObjects.get(g.getX() * HEIGHT/Sprite.SCALED_SIZE + g.getY()/Sprite.SCALED_SIZE + HEIGHT).render(gc);
//                    g.render(gc);
                    g.update((int)elapsedSeconds);
                });

                render();
                move(entities.get(0));
                //render();
            }
        };
        timer.start();

        createMap();
        createEntity();

        stillObjects.forEach(g -> g.render(gc));

        scene.setOnKeyPressed(e->{

                KeyCode key = e.getCode();

                if (key == KeyCode.LEFT) {
                    entities.get(0).setState("left");
                    dx = -speed;
                }

                if (key == KeyCode.RIGHT) {
                    entities.get(0).setState("right");
                    dx = speed;
                }

                if (key == KeyCode.UP) {
                    entities.get(0).setState("up");
                    dy = -speed;
                }

                if (key == KeyCode.DOWN) {
                    entities.get(0).setState("down");
                    dy = speed;
                }
            });

        scene.setOnKeyReleased(e->{

                KeyCode key = e.getCode();

                if (key == KeyCode.LEFT) {
                    entities.get(0).setState("leftStop");
                    dx = 0;
                }

                if (key == KeyCode.RIGHT) {
                    entities.get(0).setState("rightStop");
                    dx = 0;
                }

                if (key == KeyCode.UP) {
                    entities.get(0).setState("upStop");
                    dy = 0;
                }

                if (key == KeyCode.DOWN) {
                    entities.get(0).setState("downStop");
                    dy = 0;
                }
            });

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



    public void move(Entity entity) {
        int realX = entity.getX()/Sprite.SCALED_SIZE;
        int realY = entity.getY()/Sprite.SCALED_SIZE;
        int tempX = entity.getX()%Sprite.SCALED_SIZE;
        int tempY = entity.getY()%Sprite.SCALED_SIZE;
        boolean check;
        if(entity.getState().equals("right")){

            //làm tròn Y để dễ di chuyển
            if(tempY<=10 && entity.collidesWith(stillObjects.get(realX * HEIGHT + realY + HEIGHT ))) entity.setY(realY * Sprite.SCALED_SIZE);
            if(tempY>=22 && entity.collidesWith(stillObjects.get(realX * HEIGHT + realY + HEIGHT + 1))) entity.setY((realY + 1) * Sprite.SCALED_SIZE);

            //kiểm tra va chạm để di chuyển
            if(tempY == 0) {
                if (entity.collidesWith(stillObjects.get(realX * HEIGHT + realY + HEIGHT))) {
                    entity.setX(entity.getX() + dx);
                    System.out.println(entity.getX() + "   " + entity.getY());
                }
            }
        }
        if(entity.getState().equals("up")){

            //làm tròn X để dễ di chuyển
            if(tempX<=10 && entity.collidesWith(stillObjects.get(realX * HEIGHT + realY - 1))) entity.setX(realX * Sprite.SCALED_SIZE);
            if(tempX>=22 && entity.collidesWith(stillObjects.get(realX * HEIGHT + realY + HEIGHT - 1))) entity.setX((realX + 1) * Sprite.SCALED_SIZE);

            //kiểm tra va chạm để di chuyển
            if(tempX == 0) {
                if(tempY == 0) {
                    if (entity.collidesWith(stillObjects.get(realX * HEIGHT + realY - 1))) {
                        entity.setY(entity.getY() + dy);
                        System.out.println(entity.getX() + "   " + entity.getY());
                    }
                }
                else {
                    if(entity.collidesWith(stillObjects.get(realX * HEIGHT + realY ))) {
                        entity.setY(entity.getY() + dy);
                        System.out.println(entity.getX() + "   " + entity.getY());
                    }
                }
            }
        }
        if(entity.getState().equals("left")){

            //làm tròn Y để dễ di chuyển
            if(tempY<=10 && entity.collidesWith(stillObjects.get(realX * HEIGHT + realY - HEIGHT ))) entity.setY(realY * Sprite.SCALED_SIZE);
            if(tempY>=22 && entity.collidesWith(stillObjects.get(realX * HEIGHT + realY - HEIGHT + 1))) entity.setY((realY + 1) * Sprite.SCALED_SIZE);

            //kiểm tra va chạm để di chuyển
            if(tempY == 0) {
                if(tempX == 0) {
                    if (entity.collidesWith(stillObjects.get(realX * HEIGHT + realY - HEIGHT))) {
                        entity.setX(entity.getX() + dx);
                        System.out.println(entity.getX() + "   " + entity.getY());
                    }
                }
                else {
                    if (entity.collidesWith(stillObjects.get(realX * HEIGHT + realY))) {
                        entity.setX(entity.getX() + dx);
                        System.out.println(entity.getX() + "   " + entity.getY());
                    }
                }
            }
        }
        if(entity.getState().equals("down")){
            //làm tròn X để dễ di chuyển
            if (tempX <= 10 && entity.collidesWith(stillObjects.get(realX * HEIGHT + realY + 1 ))) entity.setX(realX * Sprite.SCALED_SIZE);
            if (tempX >= 22 && entity.collidesWith(stillObjects.get(realX * HEIGHT + realY + HEIGHT + 1))) entity.setX((realX + 1) * Sprite.SCALED_SIZE);

            //kiểm tra va chạm để di chuyển
            if (tempX == 0) {
                if (entity.collidesWith(stillObjects.get(realX * HEIGHT + realY + 1))) {
                    entity.setY(entity.getY() + dy);
                    System.out.println(entity.getX() + "   " + entity.getY());
                }
            }
        }
    }

}