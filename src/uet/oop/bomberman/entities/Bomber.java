package uet.oop.bomberman.entities;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import uet.oop.bomberman.graphics.Sprite;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.Node;

public class Bomber extends Entity {

    public Bomber(int x, int y, Image img) {
        super( x, y, img);
    }

    @Override
    public void update(int time) {
        this.img = Sprite.movingSprite(Sprite.player_right,Sprite.player_right_1,Sprite.player_right_2,time,3).getFxImage();
        ImageView imageView = new ImageView(img);
        imageView.relocate(x,y);
    }

}
