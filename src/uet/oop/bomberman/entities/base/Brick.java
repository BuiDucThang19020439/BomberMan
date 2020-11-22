package uet.oop.bomberman.entities.base;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

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
}
