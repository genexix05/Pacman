package org.example.pacman;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends ImageView {

    private double radius;
    private double vx;
    private double vy;

    // Velocidad base para moverse
    public static final double SPEED = 180; // píxeles/segundo, ajústalo a tu gusto

    public Player(String imagePath, double startX, double startY, double radius) {
        super(new Image(imagePath));
        this.setX(startX);
        this.setY(startY);
        this.radius = radius;

        // Si tu imagen es grande, ajusta:
        this.setFitWidth(40);
        this.setFitHeight(40);
        this.setPreserveRatio(true);

        vx = 0;
        vy = 0;
    }

    // Ajusta la velocidad (vx, vy)
    public void setVelocity(double vx, double vy) {
        this.vx = vx;
        this.vy = vy;
    }

    // Mover al jugador según la velocidad y el tiempo transcurrido
    public void update(double deltaTime) {
        setX(getX() + vx * deltaTime);
        setY(getY() + vy * deltaTime);
    }

    public double getRadius() {
        return radius;
    }

    // Centro X e Y, para cálculos de distancia con fantasmas
    public double getCenterX() {
        return getX() + getBoundsInLocal().getWidth() / 2;
    }

    public double getCenterY() {
        return getY() + getBoundsInLocal().getHeight() / 2;
    }
}
