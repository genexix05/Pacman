package org.example.pacman;

import javafx.animation.AnimationTimer;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameScene {

    private Scene scene;
    private Pane root;

    // Jugador y listas
    private Player player;
    private List<Particle> particles = new ArrayList<>();
    private List<EdibleItem> edibleItems = new ArrayList<>();

    // Labels de tiempo y puntuación
    private Label timeLabel;
    private Label scoreLabel;

    // Control del juego
    private double totalTime = 0;
    private double score = 0;
    private boolean gameOver = false;
    private AnimationTimer gameLoop;
    private long lastTime;

    private Random rand = new Random();

    // Ítems comestibles (con rutas e info)
    // Ajusta con tus imágenes reales
    private String[] edibleImages = {
            "file:src/main/resources/org/example/pacman/images/cereza.png",
            "file:src/main/resources/org/example/pacman/images/fresa.png",
            "file:src/main/resources/org/example/pacman/images/naranja.png",
            "file:src/main/resources/org/example/pacman/images/manzana.png",
            "file:src/main/resources/org/example/pacman/images/kiwi.png",
            "file:src/main/resources/org/example/pacman/images/pajaro.png",
            "file:src/main/resources/org/example/pacman/images/campana.png",
            "file:src/main/resources/org/example/pacman/images/llave.png"
    };
    private int[] ediblePoints = {100, 300, 500, 700, 1000, 2000, 3000, 5000};

    // Aparición progresiva de ítems
    private double spawnAccumulator = 0.0;
    private double spawnInterval = 2.0;

    // Cada 30s, fantasma nuevo
    private double ghostSpawnAccumulator = 0.0;
    private double ghostSpawnInterval = 30.0;

    // Distancia de "chase" de fantasmas
    private double chaseDistance = 100.0;

    // Subir velocidad de fantasmas cada 20s
    private double speedUpAccumulator = 0.0;
    private double speedUpInterval = 20.0;
    private double speedUpFactor = 1.1; // +10%

    // Overlay de pausa
    private Pane pauseOverlay;
    private boolean isPaused = false;

    // === SONIDOS ===
    private AudioClip startSound;
    private AudioClip chaseSound;
    private AudioClip deathSound;
    private AudioClip eatSound;

    // Para evitar reproducir chaseSound demasiadas veces
    private boolean chaseSoundPlaying = false;

    public GameScene() {
        root = new Pane();
        // Pantalla 600×600 (ajusta si quieres)
        scene = new Scene(root, 600, 600);

        // Fondo de la escena
        root.setStyle(
                "-fx-background-image: url('file:src/main/resources/org/example/pacman/images/game_bg.png');" +
                        "-fx-background-size: cover;" +
                        "-fx-background-position: center;" +
                        "-fx-background-repeat: no-repeat;"
        );

        // Barra superior
        HBox topBar = new HBox(30);
        topBar.setPrefWidth(600);
        topBar.setAlignment(Pos.CENTER);

        timeLabel = new Label("Time: 0.0");
        timeLabel.setStyle(
                "-fx-font-family: 'JoystixMonospace-Regular';" +
                        "-fx-font-size: 24px;" +
                        "-fx-text-fill: white;"
        );
        scoreLabel = new Label("Score: 0");
        scoreLabel.setStyle(
                "-fx-font-family: 'JoystixMonospace-Regular';" +
                        "-fx-font-size: 24px;" +
                        "-fx-text-fill: white;"
        );

        topBar.getChildren().addAll(timeLabel, scoreLabel);
        root.getChildren().add(topBar);

        // Crear al jugador
        player = new Player("file:src/main/resources/org/example/pacman/images/pacman.gif", 50, 50, 20);
        root.getChildren().add(player);

        // Generar fantasmas iniciales
        generateInitialGhosts(4);

        // Crear overlay de pausa
        initPauseOverlay();

        // Cargar sonidos
        loadSounds();
        // Sonido al iniciar partida
        if (startSound != null) {
            startSound.play();
        }

        initKeyListeners();
        initGameLoop();
    }

    /**
     * Carga los sonidos desde /org/example/pacman/sounds/... (o donde los tengas)
     */
    private void loadSounds() {
        try {
            // Ajusta las rutas a tus archivos .wav, .mp3...
            startSound = new AudioClip(getClass().getResource("/org/example/pacman/sounds/start.wav").toExternalForm());
            chaseSound = new AudioClip(getClass().getResource("/org/example/pacman/sounds/chase.wav").toExternalForm());
            deathSound = new AudioClip(getClass().getResource("/org/example/pacman/sounds/death.wav").toExternalForm());
            eatSound = new AudioClip(getClass().getResource("/org/example/pacman/sounds/eat.wav").toExternalForm());

            // Ajustar volúmenes si quieres
            startSound.setVolume(0.8);
            chaseSound.setVolume(0.7);
            deathSound.setVolume(1.0);
            eatSound.setVolume(1.0);

        } catch (Exception e) {
            e.printStackTrace();
            // Si no se encuentran, las variables quedarán en null
        }
    }

    /**
     * Generar n fantasmas cerca del centro
     */
    private void generateInitialGhosts(int n) {
        for (int i = 0; i < n; i++) {
            spawnGhostAtCenter();
        }
    }

    private void spawnGhostAtCenter() {
        double centerX = 300 + (rand.nextDouble() * 60 - 30);
        double centerY = 300 + (rand.nextDouble() * 60 - 30);

        double velX = (rand.nextDouble() * 4) - 2;
        double velY = (rand.nextDouble() * 4) - 2;

        Particle ghost = new Particle(
                "file:src/main/resources/org/example/pacman/images/redghost.gif",
                "file:src/main/resources/org/example/pacman/images/scaredghost.gif",
                centerX, centerY,
                velX, velY,
                20
        );
        particles.add(ghost);
        root.getChildren().add(ghost);
    }

    /**
     * Overlay de pausa
     */
    private void initPauseOverlay() {
        pauseOverlay = new Pane();
        pauseOverlay.setPrefSize(600, 600);

        // Fondo semitransparente
        pauseOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");

        // Menú de pausa
        VBox pauseMenu = new VBox(20);
        pauseMenu.setAlignment(Pos.CENTER);
        pauseMenu.setPrefSize(200, 120);
        pauseMenu.setStyle(
                "-fx-background-color: #000;" +
                        "-fx-border-color: #FFF;" +
                        "-fx-border-width: 2;"
        );

        pauseMenu.setLayoutX((600 - 200)/2);
        pauseMenu.setLayoutY((600 - 120)/2);

        Button btnContinue = new Button("Continuar");
        btnContinue.setStyle(
                "-fx-font-family: 'JoystixMonospace-Regular';" +
                        "-fx-font-size: 18px;" +
                        "-fx-background-color: #000;" +
                        "-fx-text-fill: #FFF;"
        );
        btnContinue.setOnAction(e -> {
            root.getChildren().remove(pauseOverlay);
            isPaused = false;
            gameLoop.start();
        });

        Button btnMenu = new Button("Salir");
        btnMenu.setStyle(
                "-fx-font-family: 'JoystixMonospace-Regular';" +
                        "-fx-font-size: 18px;" +
                        "-fx-background-color: #000;" +
                        "-fx-text-fill: #FFF;"
        );
        btnMenu.setOnAction(e -> {
            // Volver al menú
            SceneManager.showMenuScene();
        });

        pauseMenu.getChildren().addAll(btnContinue, btnMenu);
        pauseOverlay.getChildren().add(pauseMenu);
    }

    /**
     * Escucha de teclas
     */
    private void initKeyListeners() {
        scene.setOnKeyPressed(e -> {
            if (!isPaused) {
                switch (e.getCode()) {
                    case W, UP -> {
                        player.setVelocity(0, -Player.SPEED);
                        player.setRotate(270);
                    }
                    case S, DOWN -> {
                        player.setVelocity(0, Player.SPEED);
                        player.setRotate(90);
                    }
                    case A, LEFT -> {
                        player.setVelocity(-Player.SPEED, 0);
                        player.setRotate(180);
                    }
                    case D, RIGHT -> {
                        player.setVelocity(Player.SPEED, 0);
                        player.setRotate(0);
                    }
                    case ESCAPE -> {
                        if (!gameOver) {
                            gameLoop.stop();
                            isPaused = true;
                            if (!root.getChildren().contains(pauseOverlay)) {
                                root.getChildren().add(pauseOverlay);
                            }
                        }
                    }
                    default -> {}
                }
            } else {
                // Si está pausado y pulsamos ESC
                if (e.getCode() == KeyCode.ESCAPE) {
                    root.getChildren().remove(pauseOverlay);
                    isPaused = false;
                    gameLoop.start();
                }
            }
        });

        // Al soltar, paramos (opcional)
        scene.setOnKeyReleased(e -> {
            if (!isPaused) {
                switch (e.getCode()) {
                    case W, UP, S, DOWN, A, LEFT, D, RIGHT -> {
                        player.setVelocity(0, 0);
                    }
                    default -> {}
                }
            }
        });
    }

    /**
     * Bucle principal
     */
    private void initGameLoop() {
        lastTime = System.nanoTime();

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver && !isPaused) {
                    double elapsed = (now - lastTime) / 1_000_000_000.0;
                    lastTime = now;

                    totalTime += elapsed;
                    timeLabel.setText(String.format("Time: %.1f", totalTime));

                    // Mover jugador
                    player.update(elapsed);
                    clampPlayerPosition(player, root.getWidth(), root.getHeight());

                    // Fantasmas
                    updateGhosts(elapsed);

                    // Ítems
                    checkEdibleCollisions();
                    scoreLabel.setText(String.format("Score: %.0f", score));

                    // Spawn ítems
                    if (edibleItems.size() < 4) {
                        spawnAccumulator += elapsed;
                        if (spawnAccumulator >= spawnInterval) {
                            generateOneEdibleItem();
                            spawnAccumulator = 0.0;
                        }
                    }

                    // Cada 30s, nuevo fantasma
                    ghostSpawnAccumulator += elapsed;
                    if (ghostSpawnAccumulator >= ghostSpawnInterval) {
                        spawnGhostAtCenter();
                        ghostSpawnAccumulator = 0.0;
                    }

                    // Cada 20s, subir velocidad
                    speedUpAccumulator += elapsed;
                    if (speedUpAccumulator >= speedUpInterval) {
                        for (Particle p : particles) {
                            p.increaseSpeed(speedUpFactor);
                        }
                        speedUpAccumulator = 0.0;
                    }
                }
            }
        };
        gameLoop.start();
    }

    private void clampPlayerPosition(Player p, double width, double height) {
        double w = p.getBoundsInLocal().getWidth();
        double h = p.getBoundsInLocal().getHeight();

        if (p.getX() < 0) p.setX(0);
        else if (p.getX()+w > width) p.setX(width - w);

        if (p.getY() < 0) p.setY(0);
        else if (p.getY()+h > height) p.setY(height - h);
    }

    /**
     * Actualiza fantasmas (colisión, chase, etc.)
     * (Sonido chase implementado aquí si lo deseas.)
     */
    private void updateGhosts(double elapsed) {
        double px = player.getCenterX();
        double py = player.getCenterY();

        for (Particle ghost : particles) {
            ghost.update(root.getWidth(), root.getHeight());

            // Colisión => game over
            if (collides(player, ghost)) {
                endGame();
                return;
            }

            // Distancia => chase (aquí podrías reproducir un sonido de chase si deseas)
            double gx = ghost.getX() + ghost.getBoundsInLocal().getWidth()/2;
            double gy = ghost.getY() + ghost.getBoundsInLocal().getHeight()/2;
            double dist = Math.hypot(px - gx, py - gy);

            if (dist < chaseDistance) {
                ghost.setChasing(true);
            } else {
                ghost.setChasing(false);
            }
        }
    }

    /**
     * Colisión con ítems
     * (Sonido al comer)
     */
    private void checkEdibleCollisions() {
        List<EdibleItem> eaten = new ArrayList<>();
        for (EdibleItem item : edibleItems) {
            if (collides(player, item)) {
                score += item.getPoints();
                eaten.add(item);

                // SONIDO DE COMER
                 if (eatSound != null) {
                     eatSound.play();
                 }
            }
        }
        for (EdibleItem ed : eaten) {
            root.getChildren().remove(ed);
            edibleItems.remove(ed);
        }
    }

    private void generateOneEdibleItem() {
        int index = pickWeightedIndex(totalTime);
        String img = edibleImages[index];
        int pts = ediblePoints[index];

        double x = rand.nextDouble() * (root.getWidth() - 50);
        double y = rand.nextDouble() * (root.getHeight() - 50);

        EdibleItem ed = new EdibleItem(img, x, y, pts, 15);
        edibleItems.add(ed);
        root.getChildren().add(ed);
    }

    private int pickWeightedIndex(double time) {
        double[] weights = new double[ediblePoints.length];
        double factor = 0.05; // factor de incremento con el tiempo

        for (int i = 0; i < ediblePoints.length; i++) {
            weights[i] = ediblePoints[i] + (ediblePoints[i] * factor * time);
        }

        double total = 0;
        for (double w : weights) {
            total += w;
        }

        double r = rand.nextDouble() * total;
        double acum = 0;
        for (int i = 0; i < weights.length; i++) {
            acum += weights[i];
            if (r <= acum) {
                return i;
            }
        }
        return weights.length - 1;
    }

    /**
     * Colisión jugador - fantasma
     */
    private boolean collides(Player p, Particle ghost) {
        double px = p.getX() + p.getBoundsInLocal().getWidth()/2;
        double py = p.getY() + p.getBoundsInLocal().getHeight()/2;

        double gx = ghost.getX() + ghost.getBoundsInLocal().getWidth()/2;
        double gy = ghost.getY() + ghost.getBoundsInLocal().getHeight()/2;

        double dx = px - gx;
        double dy = py - gy;
        double dist = Math.sqrt(dx*dx + dy*dy);

        double sumRadius = p.getRadius() + ghost.getRadius();
        return dist < sumRadius;
    }

    /**
     * Colisión jugador - ítem
     */
    private boolean collides(Player p, EdibleItem item) {
        double px = p.getX() + p.getBoundsInLocal().getWidth()/2;
        double py = p.getY() + p.getBoundsInLocal().getHeight()/2;

        double ix = item.getX() + item.getBoundsInLocal().getWidth()/2;
        double iy = item.getY() + item.getBoundsInLocal().getHeight()/2;

        double dx = px - ix;
        double dy = py - iy;
        double dist = Math.sqrt(dx*dx + dy*dy);

        double sumRadius = p.getRadius() + item.getRadius();
        return dist < sumRadius;
    }

    /**
     * Fin de partida
     */
    private void endGame() {
        gameOver = true;
        gameLoop.stop();

        // SONIDO DE MUERTE
        if (deathSound != null) {
            deathSound.play();
        }

        SceneManager.showGameOverScene(score);
    }

    public Scene getScene() {
        return scene;
    }
}
