package uet.oop.bomberman.entities.Mob;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import uet.oop.bomberman.entities.Bomb.Flame;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.base.Brick;
import uet.oop.bomberman.entities.base.Wall;
import uet.oop.bomberman.entities.Bomb.Bomb;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Mob extends Entity {

    public Mob(int x, int y, Image img) {
        super( x, y, img);
    }

    @Override
    public void update(int time) {

    }

    public boolean collidesWith(Entity other) {
        if((x% Sprite.SCALED_SIZE==0||y%Sprite.SCALED_SIZE==0)) {
            if(other instanceof Wall ||other instanceof Brick ||other instanceof Bomb) {
                Rectangle s1 = new Rectangle(x, y);
                s1.setHeight(Sprite.SCALED_SIZE);
                s1.setWidth(Sprite.SCALED_SIZE);
                Rectangle s2 = new Rectangle(other.getX(), other.getY());
                s2.setHeight(Sprite.SCALED_SIZE);
                s2.setWidth(Sprite.SCALED_SIZE);
                return !s1.getBoundsInParent().intersects(s2.getBoundsInParent());
            }
        }
        return true;
    }

    public boolean collidesWithBomb(List<Bomb> bomb) {
        for(Entity other : bomb) {
            if((x-(other.getX()-32)==0 && state.equals("right") && y == other.getY())
                || (x-(other.getX()+32)==0 && state.equals("left") && y == other.getY())
                || (y-(other.getY()-32)==0 && state.equals("down") && x == other.getX())
                || (y-(other.getY()+32)==0 && state.equals("up") && x == other.getX())) return false;
        }
        return true;
    }

    public void checkDeadFlame(List<Bomb> bomb) {

        for (Bomb other :bomb) {
            if(this.getX() < other.getX() + (other.getMaxSizeRight() + 1) * Sprite.SCALED_SIZE && this.getX() > other.getX() - (other.getMaxSizeLeft() + 1) * Sprite.SCALED_SIZE && this.getY() == other.getY()) {
                this.state = "dead";
                break;
            }
            if(this.getY() < other.getY() + (other.getMaxSizeDown() + 1) * Sprite.SCALED_SIZE && this.getY() > other.getY() - (other.getMaxSizeTop() + 1) * Sprite.SCALED_SIZE && this.getX() == other.getX()) {
                this.state = "dead";
                break;
            }
        }
    }

    public void checkDeadEnemy(List<Mob> entities) {
        for(Mob other :entities) {
            if(!(other instanceof Bomber)) {
                if(this.getX() <= other.getX() + Sprite.SCALED_SIZE && this.getX() >= other.getX() - Sprite.SCALED_SIZE && this.getY() == other.getY()) {
                    this.state = "dead";
                    break;
                }
                if(this.getY() <= other.getY() + Sprite.SCALED_SIZE && this.getY() >= other.getY() - Sprite.SCALED_SIZE && this.getX() == other.getX()) {
                    this.state = "dead";
                    break;
                }
            }
        }
    }


}
