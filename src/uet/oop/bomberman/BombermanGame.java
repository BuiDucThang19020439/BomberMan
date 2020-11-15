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
                entities.forEach(g -> g.render(gc));
                //render();
                //update();
            }
        };
        timer.start();

        createMap();

        Entity bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        entities.add(bomberman);

        stillObjects.forEach(g -> g.render(gc));

        scene.setOnKeyPressed(e->{
            if(e.getCode()==KeyCode.UP) {
                if(checkEntity(stillObjects.get(entities.get(0).getX()*HEIGHT + entities.get(0).getY()-1))) {
                    stillObjects.get(entities.get(0).getX()*HEIGHT + entities.get(0).getY()).render(gc);
                    bomberman.setY(bomberman.getY()-1);
                }
            }
            else if(e.getCode()==KeyCode.DOWN) {
                if(checkEntity(stillObjects.get(entities.get(0).getX()*HEIGHT + entities.get(0).getY()+1))) {
                    stillObjects.get(entities.get(0).getX()*HEIGHT + entities.get(0).getY()).render(gc);
                    bomberman.setY(bomberman.getY()+1);
                }
            }
            else if(e.getCode()==KeyCode.RIGHT) {
                if(checkEntity(stillObjects.get(entities.get(0).getX()*HEIGHT + entities.get(0).getY()+HEIGHT))) {
                    stillObjects.get(entities.get(0).getX() * HEIGHT + entities.get(0).getY()).render(gc);
                    bomberman.setX(bomberman.getX() + 1);
                }
            }
            else if(e.getCode()==KeyCode.LEFT) {
                if(checkEntity(stillObjects.get(entities.get(0).getX()*HEIGHT + entities.get(0).getY()-HEIGHT))) {
                    stillObjects.get(entities.get(0).getX()*HEIGHT + entities.get(0).getY()).render(gc);
                    bomberman.setX(bomberman.getX()-1);
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
                }else if(map[j].charAt(i) == '1') {
                    object = new BalloomEnemy(i,j,Sprite.balloom_right1.getFxImage());
                }else {
                    object = new Grass(i, j, Sprite.grass.getFxImage());
                }
                stillObjects.add(object);
            }
        }
    }

    public void update() {
        entities.forEach(Entity::update);
    }

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