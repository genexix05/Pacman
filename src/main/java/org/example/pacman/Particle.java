package org.example.pacman;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Particle extends ImageView {

    private double velocityX;
    private double velocityY;
    private double radius;

    private Image normalImage;
    private Image chaseImage;
    private boolean isChasing = false;

    public Particle(String normalImgPath,
                    String chaseImgPath,
                    double x,
                    double y,
                    double velocityX,
                    double velocityY,
                    double radius) {

        this.normalImage = new Image(normalImgPath);
        this.chaseImage = new Image(chaseImgPath);
        setImage(normalImage);

        setX(x);
        setY(y);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.radius = radius;

        setFitWidth(40);
        setFitHeight(40);
        setPreserveRatio(true);
    }

    public void update(double width, double height) {
        // Mover
        setX(getX() + velocityX);
        setY(getY() + velocityY);

        // Rebotar en bordes horizontales
        if (getX() <= 0 || getX() + getBoundsInLocal().getWidth() >= width) {
            velocityX = -velocityX;
        }
        // Rebotar en bordes verticales
        if (getY() <= 0 || getY() + getBoundsInLocal().getHeight() >= height) {
            velocityY = -velocityY;
        }
    }

    // Cambiar sprite a chase o normal
    public void setChasing(boolean chasing) {
        if (this.isChasing != chasing) {
            this.isChasing = chasing;
            if (isChasing) {
                setImage(chaseImage);
            } else {
                setImage(normalImage);
            }
        }
    }

    public boolean isChasing() {
        return isChasing;
    }

    public double getRadius() {
        return radius;
    }

    // NUEVO: aumentar la velocidad (factor > 1.0 para acelerar)
    public void increaseSpeed(double factor) {
        velocityX *= factor;
        velocityY *= factor;
    }
}
