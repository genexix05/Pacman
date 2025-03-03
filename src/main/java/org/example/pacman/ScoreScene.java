package org.example.pacman;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

public class ScoreScene {

    private Scene scene;

    public ScoreScene() {
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(800, 600);

        // Fondo
        root.setStyle(
                "-fx-background-image: url('file:src/main/resources/org/example/pacman/images/main_bg.png');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center;" +
                        "-fx-background-repeat: no-repeat;"
        );

        // Título
        Text title = new Text("puntuaciones");
        title.setStyle(
                "-fx-font-family: 'PacFont';" +
                        "-fx-font-size: 50px;" +
                        "-fx-fill: #FFD700;"
        );
        root.getChildren().add(title);

        // Cargar los registros (si tienes ScoreManager)
        List<ScoreRecord> records = ScoreManager.loadScores();

        // Mostrar cada registro
        for (int i = 0; i < records.size(); i++) {
            ScoreRecord r = records.get(i);
            String line = String.format(
                    "%d) %s - %d - %s",
                    (i+1),
                    r.getName(),
                    r.getScore(),
                    r.getDateTime().toString() // o con un formato
            );
            Text scoreLine = new Text(line);
            scoreLine.setStyle(
                    "-fx-font-family: 'JoystixMonospace-Regular';" +
                            "-fx-font-size: 20px;" +
                            "-fx-fill: #FFFFFF;"
            );
            root.getChildren().add(scoreLine);
        }

        // Botón para volver al Menú
        Button btnMenu = new Button("Volver al Menú");
        btnMenu.setStyle(
                "-fx-font-family: 'JoystixMonospace-Regular';" +
                        "-fx-font-size: 20px;" +
                        "-fx-background-color: #000;" +
                        "-fx-text-fill: #FFF;" +
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-cursor: hand;"
        );
        btnMenu.setOnAction(e -> SceneManager.showMenuScene());

        root.getChildren().add(btnMenu);

        scene = new Scene(root, 800, 600);
    }

    public Scene getScene() {
        return scene;
    }
}
