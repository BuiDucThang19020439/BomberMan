package uet.oop.bomberman.entities.base;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Brick extends Entity {

    public Brick(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update(int time) {
        if(state.equals("dead")) {
            this.img = Sprite.movingSprite(Sprite.brick_exploded,Sprite.brick_exploded1,Sprite.brick_exploded2,time,3).getFxImage();
        }
    }

    public void destroyBrick(List<Entity> stillObjects,int HEIGHT) {
        int realX = this.x/Sprite.SCALED_SIZE;
        int realY = this.y/Sprite.SCALED_SIZE;
        Timer temp = new Timer();
        temp.schedule(new TimerTask() {
            @Override
            public void run() {
                stillObjects.set(realX * HEIGHT + realY, new Grass(realX,realY,Sprite.grass.getFxImage()));
                temp.cancel();
            }
        },200,1);
    }
}
