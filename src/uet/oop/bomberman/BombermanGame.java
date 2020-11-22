package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.base.*;
import uet.oop.bomberman.entities.Bomb.*;
import uet.oop.bomberman.entities.Mob.*;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.event.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private int speed = 2 ; // tốc độ nhân vật di chuyển
    private int enemySpeed = 1; //tốc độ địch di chuyển
    private int maxBomb = 2;// Số bomb tối đa được đặt
    private int flameLevel = 1;//do dai cua flame

    private int dx;
    private int dy;

    private GraphicsContext gc;
    private Canvas canvas;

    private List<Mob> entities = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();
    private List<Bomb> bomb = new ArrayList<>();
    private List<Flame> temp = new ArrayList<>();

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

        createMap();
        createEntity();


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {

                long elapsedNanoSeconds = now - lastUpdate;
                // 1 second = 1,000,000,000 (1 billion) nanoseconds
                double elapsedSeconds = 6 * elapsedNanoSeconds / 1_000_000_000.0;

                entities.forEach(g -> {
                    g.update((int) elapsedSeconds);
                });

                bomb.forEach(g -> {
                    g.update((int) elapsedSeconds);
                });

                for (Entity stillObject : stillObjects) {
                    stillObject.update((int) elapsedSeconds);
                }

                render();
                Movement.move(entities.get(0), dx, dy, stillObjects, HEIGHT, WIDTH, bomb);

//                if (entities.get(0).getState().equals("dead")) {
//                    Timer temp = new Timer();
//                    temp.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            stop();
//                            temp.cancel();
//                        }
//                    },300,1);
//
//                }
            }
        };
        timer.start();


        scene.setOnKeyPressed(e->{
                if(!entities.get(0).getState().equals("dead")) {
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

                    if (key == KeyCode.SPACE) {
                        createBomb();
                    }
                }
            });

        scene.setOnKeyReleased(e->{
            if(!entities.get(0).getState().equals("dead")) {
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
                    Mob object = new Bomber(i, j, Sprite.player_right.getFxImage());
                    entities.add(object);
                } else if (map[j].charAt(i) == '1') {
                    Mob object = new BalloomEnemy(i, j, Sprite.balloom_left1.getFxImage());
                    entities.add(object);
                } else if (map[j].charAt(i) == '2') {
                    Mob object = new OnealEnemy(i, j, Sprite.oneal_left1.getFxImage());
                    entities.add(object);
                }
            }
        }
    }

    public void createBomb() {
        int realX = entities.get(0).getX()/Sprite.SCALED_SIZE;
        int realY = entities.get(0).getY()/Sprite.SCALED_SIZE;
        int tempX = entities.get(0).getX()%Sprite.SCALED_SIZE;
        int tempY = entities.get(0).getY()%Sprite.SCALED_SIZE;
        if(tempX > 16) realX += 1;
        if(tempY > 16) realY += 1;
        if(bomb.size() < maxBomb) {
            Bomb newBomb = new Bomb(realX, realY, Sprite.bomb_2.getFxImage());
            newBomb.setLevel(flameLevel);
            bomb.add(newBomb);
            newBomb.startCount();
        }
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        bomb.forEach(g -> g.render(gc));
        bomb.forEach(g -> {
            if(g.getState().equals("dead")) {
                destroyAround(g);
                g.collideWithBrick(stillObjects,HEIGHT);
            }
        });
        entities.forEach(g -> {
            g.render(gc);
            if(!temp.isEmpty()) g.checkDeadFlame(bomb);
            if(g instanceof Bomber) {
                g.checkDeadEnemy(entities);
            }
        });
        entities.removeIf(g -> {
           return g.getState().equals("dead");
        });
        temp.clear();
    }

    public void destroyAround(Bomb g) {
        g.explode(stillObjects,HEIGHT,temp);
        g.collideWithAnotherBomb(bomb,HEIGHT,temp,stillObjects);
        temp.forEach(v -> v.render(gc));
        Timer count = new Timer();
        count.schedule(new TimerTask() {
            @Override
            public void run() {
                g.setState("explode");
                bomb.removeIf(g -> {
                    return g.getState().equals("explode");
                });
                count.cancel();
            }},200,1);
    }

}