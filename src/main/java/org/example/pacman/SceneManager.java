package org.example.pacman;

import javafx.stage.Stage;

public class SceneManager {

    private static Stage mainStage;

    public static void initialize(Stage stage) {
        mainStage = stage;
        mainStage.setTitle("Pac-Man");

        // Tamaño fijo
        mainStage.setWidth(600);
        mainStage.setHeight(600);

        // Evitar redimensionar
        mainStage.setResizable(false);
        mainStage.setMaximized(false);
    }

    public static void showMenuScene() {
        MenuScene menuScene = new MenuScene();
        mainStage.setScene(menuScene.getScene());
        mainStage.show();
    }

    public static void showGameScene() {
        GameScene gameScene = new GameScene();
        mainStage.setScene(gameScene.getScene());
        mainStage.show();
    }

    // Si tienes ScoreScene
    public static void showScoreScene() {
        ScoreScene scoreScene = new ScoreScene();
        mainStage.setScene(scoreScene.getScene());
        mainStage.show();
    }

    // Si tienes GameOverScene
    public static void showGameOverScene(double finalScore) {
        GameOverScene gameOverScene = new GameOverScene(finalScore);
        mainStage.setScene(gameOverScene.getScene());
        mainStage.show();
    }

    public static Stage getStage() {
        return mainStage;
    }
}
