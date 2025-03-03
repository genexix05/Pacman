module org.example.pacman {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens org.example.pacman to javafx.fxml;
    exports org.example.pacman;
}