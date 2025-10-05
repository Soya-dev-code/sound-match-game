package src;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

public class SoundMatchApp extends Application {

    private List<Question> questions = new ArrayList<>();
    private List<Question> currentQuestions;
    private Question currentQuestion;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private BorderPane root = new BorderPane();
    private Label scoreLabel;
    private Button playSoundButton;
    private GridPane optionsGrid;
    private List<Button> optionButtons = new ArrayList<>();

    private static final int NUM_OPTIONS = 3;
    private static final int GAME_QUESTION_LIMIT = 10;

    private SoundPlayer soundPlayer = new SoundPlayer(); // Your SoundPlayer

    @Override
    public void start(Stage primaryStage) {
        loadQuestions();

        Scene scene = new Scene(root, 900, 700);
        try {
            String cssPath = "file:" + System.getProperty("user.dir") + "/game-styles.css";
            scene.getStylesheets().add(cssPath);
        } catch (Exception e) {
            System.err.println("CSS file not found: " + e.getMessage());
        }

        primaryStage.setTitle("Sound Match Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        showWelcomeScreen();
    }

    private void loadQuestions() {
        questions.add(new Question("ambulance.mp3", new String[]{"ambulance", "train", "rickshaw"}, "ambulance"));
        questions.add(new Question("bike.mp3", new String[]{"car", "bike", "jcb"}, "bike"));
        questions.add(new Question("lionroaring.mp3", new String[]{"catmeowing", "dogbarking", "lionroaring"}, "lionroaring"));
        questions.add(new Question("car.mp3", new String[]{"bike", "car", "jcb"}, "car"));
        questions.add(new Question("rainfall.mp3", new String[]{"rainfall", "oceanwaves", "thunderstorm"}, "rainfall"));
        questions.add(new Question("cellphone-ringing.mp3", new String[]{"cellphone-ringing", "jeep", "doorbell"}, "cellphone-ringing"));
        questions.add(new Question("clapping.mp3", new String[]{"violin", "clapping", "laughing"}, "clapping"));
        questions.add(new Question("coughing.mp3", new String[]{"sneezing", "laughing", "coughing"}, "coughing"));
        questions.add(new Question("cowmooing.mp3", new String[]{"dogbarking", "cowmooing", "lionroaring"}, "cowmooing"));
        questions.add(new Question("crying.mp3", new String[]{"oceanwaves", "laughing", "crying"}, "crying"));
    }

    private void showWelcomeScreen() {
        Label welcomeLabel = new Label("Welcome to Sound Match Game 🎵");
        welcomeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");
        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(e -> startGame());

        VBox layout = new VBox(50, welcomeLabel, startGameButton);
        layout.setAlignment(Pos.CENTER);
        root.setTop(null);
        root.setBottom(createExitButton());
        root.setCenter(layout);
    }

    private void startGame() {
        score = 0;
        currentQuestionIndex = 0;
        Collections.shuffle(questions);
        currentQuestions = questions.subList(0, Math.min(GAME_QUESTION_LIMIT, questions.size()));
        root.setTop(createScoreBar());
        root.setCenter(createGameContent());
        root.setBottom(createExitButton());
        loadNextQuestion();
    }

    private void showGameOverScreen() {
        Label finalScore = new Label("Game Over! Final Score: " + score + " / " + currentQuestions.size());
        finalScore.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button restartButton = new Button("Restart Game");
        restartButton.setOnAction(e -> startGame());

        Button exitButton = new Button("Exit Game");
        exitButton.setOnAction(e -> Platform.exit());

        HBox buttonBox = new HBox(20, restartButton, exitButton);
        buttonBox.setAlignment(Pos.CENTER);

        VBox layout = new VBox(30, finalScore, buttonBox);
        layout.setAlignment(Pos.CENTER);

        root.setCenter(layout);
    }

    private HBox createScoreBar() {
        scoreLabel = new Label("Score: 0 / " + GAME_QUESTION_LIMIT);
        scoreLabel.setId("score-label");

        HBox topBar = new HBox(scoreLabel);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(20));
        return topBar;
    }

    private Button createExitButton() {
        Button exitButton = new Button("Exit Game");
        exitButton.setId("exit-button");
        exitButton.setOnAction(e -> Platform.exit());
        return exitButton;
    }

    private VBox createGameContent() {
        Image speakerImage = loadAssetImage("speaker", 80, 80);
        playSoundButton = new Button("", new ImageView(speakerImage));
        playSoundButton.setOnAction(e -> playCurrentSound());

        optionsGrid = new GridPane();
        optionsGrid.setAlignment(Pos.CENTER);
        optionsGrid.setHgap(50);

        optionButtons.clear();
        for (int i = 0; i < NUM_OPTIONS; i++) {
            Button btn = new Button();
            btn.getStyleClass().add("option-button");
            btn.setOnAction(e -> handleAnswer((Button) e.getSource()));
            optionButtons.add(btn);
            optionsGrid.add(btn, i, 0);
        }

        VBox centerLayout = new VBox(50, playSoundButton, optionsGrid);
        centerLayout.setAlignment(Pos.CENTER);
        return centerLayout;
    }

    private void playCurrentSound() {
        if (currentQuestion != null) {
            soundPlayer.play(System.getProperty("user.dir") + "/sounds/" + currentQuestion.getSoundFile());
        }
    }

    private void handleAnswer(Button selectedButton) {
        optionButtons.forEach(b -> b.setDisable(true));
        String selectedAnswer = (String) selectedButton.getUserData();
        boolean isCorrect = selectedAnswer.equalsIgnoreCase(currentQuestion.getCorrectAnswer());

        if (isCorrect) {
            score++;
            selectedButton.setStyle("-fx-background-color: #4CAF50;"); // Green
            soundPlayer.playSync(System.getProperty("user.dir") + "/sounds/correct.mp3");
        } else {
            selectedButton.setStyle("-fx-background-color: #F44336;"); // Red
            soundPlayer.playSync(System.getProperty("user.dir") + "/sounds/wrong.mp3");
            for (Button btn : optionButtons) {
                if (btn.getUserData().equals(currentQuestion.getCorrectAnswer())) {
                    btn.setStyle("-fx-background-color: #4CAF50;");
                }
            }
        }

        updateScoreLabel();

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> Platform.runLater(() -> {
            clearOptionStyles();
            loadNextQuestion();
        }), 2, TimeUnit.SECONDS);
        executor.shutdown();
    }

    private void clearOptionStyles() {
        for (Button btn : optionButtons) {
            btn.setStyle(null);
        }
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex >= currentQuestions.size()) {
            showGameOverScreen();
            return;
        }

        currentQuestion = currentQuestions.get(currentQuestionIndex++);
        List<String> optionsList = new ArrayList<>(Arrays.asList(currentQuestion.getOptions()));
        Collections.shuffle(optionsList);

        for (int i = 0; i < NUM_OPTIONS; i++) {
            String optionName = optionsList.get(i);
            Button btn = optionButtons.get(i);
            Image optionImage = loadAssetImage(optionName, 140, 140);
            btn.setGraphic(new ImageView(optionImage));
            btn.setUserData(optionName);
            btn.setDisable(false);
            btn.setStyle(null);
        }

        playCurrentSound();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score: " + score + " / " + currentQuestions.size());
    }

    private Image loadAssetImage(String optionName, double width, double height) {
        try {
            String imagePath = System.getProperty("user.dir") + "/assets/images/" + optionName.toLowerCase() + ".jpg";
            return new Image("file:" + imagePath, width, height, true, true);
        } catch (Exception e) {
            System.err.println("Image not found: " + optionName);
            String placeholderPath = System.getProperty("user.dir") + "/assets/images/placeholder.jpg";
            return new Image("file:" + placeholderPath, width, height, true, true);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    static class Question {
        private String soundFile;
        private String[] options;
        private String correctAnswer;

        public Question(String soundFile, String[] options, String correctAnswer) {
            this.soundFile = soundFile;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }

        public String getSoundFile() {
            return soundFile;
        }

        public String[] getOptions() {
            return options;
        }

        public String getCorrectAnswer() {
            return correctAnswer;
        }
    }
}