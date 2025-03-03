package org.example.pacman;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Cargamos la fuente de TÍTULO
        Font titleFont = Font.loadFont(
                getClass().getResourceAsStream("/org/example/pacman/fonts/ArcadeTitle.TTF"),
                16
        );
        System.out.println("Title font loaded: " + titleFont.getName());

        // Cargamos la fuente de CUERPO
        Font bodyFont = Font.loadFont(
                getClass().getResourceAsStream("/org/example/pacman/fonts/ArcadeBody.otf"),
                16
        );
        System.out.println("Body font loaded: " + bodyFont.getName());

        // Inicializamos SceneManager
        SceneManager.initialize(primaryStage);

        // Mostramos el menú
        SceneManager.showMenuScene();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
