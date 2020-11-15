package uet.oop.bomberman.entities;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import uet.oop.bomberman.graphics.Sprite;

public class Bomber extends Entity {

    private int speed = 5; //move 5 pixel per second

    public Bomber(int x, int y, Image img) {
        super( x, y, img);
    }

    @Override
    public void update(int time) {
        this.img = Sprite.movingSprite(Sprite.player_right,Sprite.player_right_1,Sprite.player_right_2,time,3).getFxImage();
    }

    public void moveX(int _x) {
        if(x>_x) {
            for(int i = x*Sprite.SCALED_SIZE-1;i>=_x*Sprite.SCALED_SIZE;i--) {

            }
        }
    }
}
