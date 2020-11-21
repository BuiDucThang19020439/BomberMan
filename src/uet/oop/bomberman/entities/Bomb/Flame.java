package uet.oop.bomberman.entities.Bomb;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;

public class Flame extends Entity{

    private int level = 1;

    public Flame(int x, int y, Image img) {
        super( x, y, img);
    }

    @Override
    public void update(int time) {

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
