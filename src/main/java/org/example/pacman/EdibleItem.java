package org.example.pacman;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class EdibleItem extends ImageView {
    private int points;
    private double radius;

    public EdibleItem(String imagePath, double x, double y, int points, double radius) {
        super(new Image(imagePath));
        this.setX(x);
        this.setY(y);
        this.points = points;
        this.radius = radius;

        // Ajusta tamaño si quieres
        this.setFitWidth(40);
        this.setFitHeight(40);
        this.setPreserveRatio(true);
    }

    public int getPoints() {
        return points;
    }

    public double getRadius() {
        return radius;
    }
}
