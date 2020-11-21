package uet.oop.bomberman.entities.Bomb;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends Entity{

    private int timer = 2; // hen gio bom no

    public Bomb(int x, int y, Image img) {
        super( x, y, img);
        super.state = "live";
    }

    @Override
    public void update(int time) {
        this.img = Sprite.movingSprite(Sprite.bomb_1, Sprite.bomb, Sprite.bomb_2, time, 3).getFxImage();
    }

    public void startCount() {
        Timer temp = new Timer();
        temp.schedule(new TimerTask() {
            @Override
            public void run() {
                setState("dead");
                temp.cancel();
            }
            },timer * 1000,1);
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

}
