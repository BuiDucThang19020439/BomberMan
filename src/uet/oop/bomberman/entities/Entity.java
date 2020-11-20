package uet.oop.bomberman.entities;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Entity {
    //Tọa độ X tính từ góc trái trên trong Canvas
    protected int x;

    //Tọa độ Y tính từ góc trái trên trong Canvas
    protected int y;

    //Trạng thái của vật thể
    protected String state;


    protected Image img;

    //Khởi tạo đối tượng, chuyển từ tọa độ đơn vị sang tọa độ trong canvas
    public Entity( int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
    }

    //render hinh anh
    public void render(GraphicsContext gc) {
        gc.drawImage(img, x, y);
    }

    public boolean collidesWith(Entity other) {
        if((x%Sprite.SCALED_SIZE==0||y%Sprite.SCALED_SIZE==0)) {
            if(other instanceof Wall||other instanceof Brick) {
                Rectangle s1 = new Rectangle(x, y);
                s1.setHeight(Sprite.SCALED_SIZE);
                s1.setWidth(Sprite.SCALED_SIZE);
                Rectangle s2 = new Rectangle(other.getX(), other.getY());
                s2.setHeight(Sprite.SCALED_SIZE);
                s2.setWidth(Sprite.SCALED_SIZE);
                if (s1.getBoundsInParent().intersects(s2.getBoundsInParent())) return false;
            }
        }
        return true;
    }

    public abstract void update(int time);


    public void setX(int _x) {
        x = _x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Image getImg() {
        return img;
    }

    public void setY(int _y) {
        y = _y;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setImg(Image _img) {
        img = _img;
    }

}
