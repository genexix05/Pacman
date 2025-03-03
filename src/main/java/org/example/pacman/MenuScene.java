package org.example.pacman;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MenuScene {

    private Scene scene;

    public MenuScene() {
        // Layout principal
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(600, 600);

        // Fondo de pantalla (ajusta la ruta de tu imagen)
        root.setStyle(
                "-fx-background-image: url('file:src/main/resources/org/example/pacman/images/main_bg.png');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center;" +
                        "-fx-background-repeat: no-repeat;"
        );

        // Título grande con la fuente de TÍTULO
        Label titleLabel = new Label("pac-man");
        titleLabel.setStyle(
                "-fx-font-family: 'PacFont';" +  // Usa el nombre que imprime al cargar
                        "-fx-font-size: 64px;" +
                        "-fx-text-fill: #FFD700;"
        );

        // Botón Jugar
        Button btnJugar = new Button("Jugar");
        btnJugar.setStyle(
                "-fx-font-family: 'JoystixMonospace-Regular';" +   // Fuente de cuerpo
                        "-fx-font-size: 24px;" +
                        "-fx-background-color: #000;" +
                        "-fx-text-fill: #FFF;" +
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-cursor: hand;"
        );
        btnJugar.setOnAction(e -> SceneManager.showGameScene());

        // Botón Puntuaciones
        Button btnPuntuaciones = new Button("Puntuaciones");
        btnPuntuaciones.setStyle(
                "-fx-font-family: 'JoystixMonospace-Regular';" +
                        "-fx-font-size: 24px;" +
                        "-fx-background-color: #000;" +
                        "-fx-text-fill: #FFF;" +
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-cursor: hand;"
        );
        btnPuntuaciones.setOnAction(e -> SceneManager.showScoreScene());

        // Botón Salir
        Button btnSalir = new Button("Salir");
        btnSalir.setStyle(
                "-fx-font-family: 'JoystixMonospace-Regular';" +
                        "-fx-font-size: 24px;" +
                        "-fx-background-color: #000;" +
                        "-fx-text-fill: #FFF;" +
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-cursor: hand;"
        );
        btnSalir.setOnAction(e -> SceneManager.getStage().close());

        // Añadimos los nodos al VBox
        root.getChildren().addAll(titleLabel, btnJugar, btnPuntuaciones, btnSalir);

        // Creamos la escena
        scene = new Scene(root);
    }

    public Scene getScene() {
        return scene;
    }
}
