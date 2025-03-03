package org.example.pacman;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.time.LocalDateTime;

public class GameOverScene {
    private Scene scene;
    private double finalScore;

    public GameOverScene(double finalScore) {
        this.finalScore = finalScore;
        createScene();
    }

    private void createScene() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(600, 600);

        // Fondo retro
        root.setStyle(
                "-fx-background-image: url('file:src/main/resources/org/example/pacman/images/main_bg.png');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center;" +
                        "-fx-background-repeat: no-repeat;"
        );

        // Título grande
        Label gameOverLabel = new Label("game over");
        gameOverLabel.setStyle(
                "-fx-font-family: 'PacFont';" +       // Usa tu fuente de título si lo deseas
                        "-fx-font-size: 50px;" +
                        "-fx-text-fill: #FF0000;"
        );

        // Muestra la puntuación
        Label scoreLabel = new Label("Tu puntuación: " + (int) finalScore);
        scoreLabel.setStyle(
                "-fx-font-family: 'JoystixMonospace-Regular';" +  // Ajusta a tu fuente “de cuerpo”
                        "-fx-font-size: 20px;" +
                        "-fx-text-fill: #FFF;"
        );

        // Etiqueta para el nombre
        Label nameLabel = new Label("Introduce 3 letras:");
        nameLabel.setStyle(
                "-fx-font-family: 'JoystixMonospace-Regular';" +
                        "-fx-font-size: 18px;" +
                        "-fx-text-fill: #FFF;"
        );

        // TextField con límite de 3 letras (A-Z)
        TextField nameField = new TextField();
        nameField.setMaxWidth(200);

        // Estilo retro del TextField
        nameField.setStyle(
                "-fx-font-family: 'JoystixMonospace-Regular';" +
                        "-fx-font-size: 18px;" +
                        "-fx-background-color: #000;" +
                        "-fx-text-fill: #FFF;" +
                        "-fx-border-color: #FFF;" +
                        "-fx-border-width: 2;" +
                        "-fx-prompt-text-fill: #888;"
        );
        nameField.setPromptText("ABC");

        // Listener para forzar mayúsculas y solo 3 letras (A-Z)
        nameField.textProperty().addListener((obs, oldVal, newVal) -> {
            // Convertir a mayúsculas
            String upper = newVal.toUpperCase();

            // Permitir solo A-Z
            if (!upper.matches("[A-Z]*")) {
                // Si el nuevo texto contiene algo fuera de [A-Z], revertimos a oldVal
                nameField.setText(oldVal);
                return;
            }

            // Limitar a 3 caracteres
            if (upper.length() > 3) {
                upper = upper.substring(0, 3);
            }
            // Asignar el texto final
            nameField.setText(upper);
        });

        // Botón para confirmar
        Button btnOk = new Button("OK");
        btnOk.setStyle(
                "-fx-font-family: 'JoystixMonospace-Regular';" +
                        "-fx-font-size: 18px;" +
                        "-fx-background-color: #000;" +
                        "-fx-text-fill: #FFF;" +
                        "-fx-border-color: #FFF;" +
                        "-fx-border-width: 2;" +
                        "-fx-padding: 5 15 5 15;"
        );
        btnOk.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                name = "???";
            }

            // Guardar puntuación
            ScoreRecord record = new ScoreRecord(name, (int) finalScore, LocalDateTime.now());
            ScoreManager.saveScore(record);

            // Ir a la pantalla de puntuaciones (o menú)
            SceneManager.showScoreScene();
        });

        root.getChildren().addAll(gameOverLabel, scoreLabel, nameLabel, nameField, btnOk);

        scene = new Scene(root, 600, 600);
    }

    public Scene getScene() {
        return scene;
    }
}
