package src;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class SoundMatchApp extends Application {

    private Stage window;
    private Scene welcomeScene, gameScene;
    private StackPane gameLayout;

    private Label scoreLabel;
    private int score = 0;
    private int questionIndex = 0;

    private List<String> imageFiles;
    private List<String> soundFiles;
    private String currentSound;
    private Random random = new Random();

    private Player currentPlayer;
    private Thread soundThread;

    private List<Color> bgColors = Arrays.asList(Color.LIGHTBLUE, Color.LIGHTPINK, Color.LIGHTYELLOW, Color.LIGHTGREEN, Color.LIGHTCORAL);

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        loadAssets();

        Label welcomeLabel = new Label("🎵 Sound Match Game 🎵");
        welcomeLabel.setFont(Font.font("Comic Sans MS", 36));
        welcomeLabel.setTextFill(Color.DARKGREEN);

        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-background-color: lightgreen; -fx-font-size: 24px; -fx-padding: 15px;");
        startButton.setOnAction(e -> startGame());

        VBox welcomeLayout = new VBox(30, welcomeLabel, startButton);
        welcomeLayout.setAlignment(Pos.CENTER);
        welcomeLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #e0ffe0, #c0ffc0);");

        welcomeScene = new Scene(welcomeLayout, 480, 800);

        window.setScene(welcomeScene);
        window.setTitle("Sound Match Game");
        window.show();
    }

    private void loadAssets() {
        imageFiles = new ArrayList<>();
        soundFiles = new ArrayList<>();

        File imageFolder = new File("assets/images");
        if (imageFolder.exists()) {
            for (File file : imageFolder.listFiles()) {
                if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                    imageFiles.add(file.getAbsolutePath());
                }
            }
        }

        File soundFolder = new File("sounds");
        if (soundFolder.exists()) {
            for (File file : soundFolder.listFiles()) {
                if (file.getName().endsWith(".mp3") || file.getName().endsWith(".wav")) {
                    soundFiles.add(file.getAbsolutePath());
                }
            }
        }
    }

    private void startGame() {
        gameLayout = new StackPane();
        changeBackgroundColor();

        scoreLabel = new Label("Score: 0");
        scoreLabel.setFont(Font.font("Comic Sans MS", 36));
        scoreLabel.setTextFill(Color.DARKBLUE);
        scoreLabel.setStyle("-fx-background-color: white; -fx-background-radius: 20px; -fx-padding: 15px;");
        scoreLabel.setEffect(new DropShadow(10, Color.BLACK));

        ScaleTransition pulse = new ScaleTransition(Duration.millis(800), scoreLabel);
        pulse.setFromX(1);
        pulse.setFromY(1);
        pulse.setToX(1.2);
        pulse.setToY(1.2);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        gameLayout.getChildren().add(scoreLabel);
        StackPane.setAlignment(scoreLabel, Pos.TOP_CENTER);

        nextQuestion();

        gameScene = new Scene(gameLayout, 480, 800);
        window.setScene(gameScene);
    }

    private void changeBackgroundColor() {
        Color newColor = bgColors.get(random.nextInt(bgColors.size()));
        gameLayout.setStyle("-fx-background-color: " + toRgbString(newColor) + ";");
    }

    private String toRgbString(Color c) {
        return String.format("rgb(%d, %d, %d)",
                (int)(c.getRed()*255),
                (int)(c.getGreen()*255),
                (int)(c.getBlue()*255));
    }

    private void nextQuestion() {
        if (questionIndex >= 10) {
            showFinalScore();
            return;
        }

        questionIndex++;
        changeBackgroundColor();
        stopSound();

        currentSound = soundFiles.get(random.nextInt(soundFiles.size()));
        playSound(currentSound);

        String correctImage = currentSound.replace("sounds", "assets/images")
                .replace(".mp3", ".jpg")
                .replace(".wav", ".jpg");

        if (!new File(correctImage).exists()) {
            correctImage = findMatchingImage(currentSound);
        }

        List<String> options = new ArrayList<>();
        options.add(correctImage);

        Set<String> usedImages = new HashSet<>();
        usedImages.add(correctImage);

        while (options.size() < Math.min(4, imageFiles.size())) {
            String candidate = imageFiles.get(random.nextInt(imageFiles.size()));
            if (!usedImages.contains(candidate)) {
                options.add(candidate);
                usedImages.add(candidate);
            }
        }

        Collections.shuffle(options);
        displayOptions(options, correctImage);
    }

    private String findMatchingImage(String soundPath) {
        String soundName = new File(soundPath).getName();
        String baseName = soundName.substring(0, soundName.lastIndexOf('.'));
        for (String imgPath : imageFiles) {
            String imgName = new File(imgPath).getName();
            if (imgName.contains(baseName)) {
                return imgPath;
            }
        }
        return imageFiles.get(random.nextInt(imageFiles.size()));
    }

    private void displayOptions(List<String> options, String correctImage) {
        Pane circlePane = new Pane();
        circlePane.setMinSize(480, 800);

        double centerX = 240;
        double centerY = 400;
        double radius = 200;
        int total = options.size();

        for (int i = 0; i < total; i++) {
            double angle = 2 * Math.PI * i / total;
            double x = centerX + radius * Math.cos(angle) - 70;
            double y = centerY + radius * Math.sin(angle) - 70;

            ImageView img = new ImageView(new Image(new File(options.get(i)).toURI().toString()));
            img.setFitHeight(140);
            img.setFitWidth(140);

            Button imgBtn = new Button();
            imgBtn.setShape(new Circle(70));
            imgBtn.setMinSize(140, 140);
            imgBtn.setMaxSize(140, 140);
            imgBtn.setGraphic(img);

            Color optionColor = bgColors.get(random.nextInt(bgColors.size()));
            imgBtn.setStyle("-fx-background-color: " + toRgbString(optionColor) + "; -fx-border-color: white; -fx-border-width: 3px;");

            imgBtn.setLayoutX(x);
            imgBtn.setLayoutY(y);

            imgBtn.setOnMouseEntered(e -> imgBtn.setScaleX(1.1));
            imgBtn.setOnMouseExited(e -> imgBtn.setScaleX(1));

            final String finalCorrectImage = correctImage;
            final String finalImagePath = options.get(i);

            imgBtn.setOnAction(e -> {
                stopSound();
                playClickAnimation(imgBtn);
                changeBackgroundColor();
                if (finalImagePath.equals(finalCorrectImage)) {
                    score++;
                    scoreLabel.setText("Score: " + score);
                    showFeedback("Correct ✅", Color.LIGHTGREEN);
                } else {
                    showFeedback("Wrong ❌", Color.PINK);
                }
            });

            circlePane.getChildren().add(imgBtn);
        }

        Button exitBtn = new Button("Exit");
        exitBtn.setStyle("-fx-background-color: lightcoral; -fx-font-size: 18px; -fx-padding: 10px;");
        exitBtn.setOnAction(e -> {
            stopSound();
            showFinalScore();
        });
        exitBtn.setLayoutX(centerX - 30);
        exitBtn.setLayoutY(centerY + radius + 20);
        circlePane.getChildren().add(exitBtn);

        gameLayout.getChildren().clear();
        gameLayout.getChildren().addAll(circlePane, scoreLabel);

        FadeTransition fade = new FadeTransition(Duration.millis(800), circlePane);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void showFeedback(String message, Color color) {
        Label feedback = new Label(message);
        feedback.setFont(Font.font("Comic Sans MS", 28));
        feedback.setTextFill(color);

        VBox feedbackBox = new VBox(feedback);
        feedbackBox.setAlignment(Pos.CENTER);
        feedbackBox.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10px; -fx-background-radius: 20px;");
        gameLayout.getChildren().add(feedbackBox);
        StackPane.setAlignment(feedbackBox, Pos.BOTTOM_CENTER);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                javafx.application.Platform.runLater(() -> nextQuestion());
            }
        }, 1500);
    }

    private void playSound(String soundPath) {
        stopSound();

        soundThread = new Thread(() -> {
            try {
                FileInputStream fis = new FileInputStream(soundPath);
                currentPlayer = new Player(fis);
                currentPlayer.play();
            } catch (Exception e) {
                System.out.println("Error playing sound: " + e.getMessage());
            }
        });
        soundThread.start();
    }

    private void stopSound() {
        try {
            if (currentPlayer != null) currentPlayer.close();
            if (soundThread != null && soundThread.isAlive()) soundThread.interrupt();
        } catch (Exception e) {
            System.out.println("Error stopping sound: " + e.getMessage());
        }
    }

    private void playClickAnimation(Button btn) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setAutoReverse(true);
        st.setCycleCount(2);

        RotateTransition rt = new RotateTransition(Duration.millis(400), btn);
        rt.setByAngle(360);
        rt.setCycleCount(1);

        ParallelTransition pt = new ParallelTransition(st, rt);
        pt.play();
    }

    private void showFinalScore() {
        VBox finalBox = new VBox(30);
        finalBox.setAlignment(Pos.CENTER);

        Label finalLabel = new Label("🎯 Game Over! 🎯\nYour Score: " + score);
        finalLabel.setFont(Font.font("Comic Sans MS", 36));
        finalLabel.setTextFill(Color.DARKMAGENTA);

        Button restartBtn = new Button("Restart Game");
        restartBtn.setStyle("-fx-background-color: lightgreen; -fx-font-size: 22px;");
        restartBtn.setOnAction(e -> {
            score = 0;
            questionIndex = 0;
            startGame();
        });

        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: lightcoral; -fx-font-size: 22px;");
        closeBtn.setOnAction(e -> window.close());

        finalBox.getChildren().addAll(finalLabel, restartBtn, closeBtn);
        gameLayout.getChildren().clear();
        gameLayout.getChildren().add(finalBox);

        ScaleTransition scale = new ScaleTransition(Duration.millis(800), finalBox);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1);
        scale.setToY(1);
        scale.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}