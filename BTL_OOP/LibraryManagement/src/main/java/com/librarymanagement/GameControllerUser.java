package com.librarymanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

import javafx.scene.control.ToggleButton;


public class GameControllerUser {
    @FXML
    private Label questionLabel, correctAnswersLabel;
    @FXML
    private Button option1Button, option2Button, option3Button, replayButton, option4Button, startButton, returnToMenuButton, fiftyFiftyButton;
    @FXML
    private ImageView heart1, heart2, heart3;
    @FXML
    private ToggleButton musicToggleButton;

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int hearts = 3;
    private int correctAnswers = 0;
    private boolean fiftyFiftyUsedThisGame = false;
    private boolean fiftyFiftyUsedCurrentQuestion = false;
    private final Image heartImage = new Image(getClass().getResource("/image/heart_icon.png").toString());
    private final Image emptyHeartImage = new Image(getClass().getResource("/image/empty_heart_icon.png").toString());
    private MediaPlayer mediaPlayer;

    @FXML
    public void initialize() {
        loadQuestionsFromDatabase();
        resetGameUI();

        String musicFile = "D:/DOM05-namdz - Copy1/BTL_OOP/LibraryManagement/src/main/resources/music_game/ailatrieuphu.mp3";
        Media media = new Media(new File(musicFile).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
    }

    @FXML
    private void toggleMusic() {
        if (musicToggleButton.isSelected()) {
            mediaPlayer.play();
        } else {
            mediaPlayer.pause();
        }
    }


    private void loadQuestionsFromDatabase() {
        questionList = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM questions")) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                questionList.add(new Question(
                        resultSet.getString("question"),
                        resultSet.getString("option1"),
                        resultSet.getString("option2"),
                        resultSet.getString("option3"),
                        resultSet.getString("option4"),
                        resultSet.getInt("correct_option")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void replayGame() {
        resetGame();
    }

    private void resetGame() {
        hearts = 3;
        correctAnswers = 0;
        currentQuestionIndex = 0;
        fiftyFiftyUsedThisGame = false;
        fiftyFiftyButton.setDisable(false);
        correctAnswersLabel.setText("Câu trả lời đúng: " + correctAnswers);

        Collections.shuffle(questionList);

        updateHeartDisplay();
        startButton.setDisable(true);
        replayButton.setDisable(false);
        enableAnswerButtons();
        loadNextQuestion();
    }

    @FXML
    private void startGame() {
        hearts = 3;
        correctAnswers = 0;
        currentQuestionIndex = 0;
        fiftyFiftyUsedThisGame = false;
        fiftyFiftyButton.setDisable(false);
        correctAnswersLabel.setText("Câu trả lời đúng: " + correctAnswers);

        Collections.shuffle(questionList);

        updateHeartDisplay();
        startButton.setDisable(true);
        enableAnswerButtons();
        loadNextQuestion();
    }

    private void resetGameUI() {
        questionLabel.setText("Nhấn 'Bắt đầu' để chơi.");
        correctAnswersLabel.setText("Câu trả lời đúng: " + correctAnswers);
        updateHeartDisplay();
        disableAnswerButtons();
    }

    private void loadNextQuestion() {
        fiftyFiftyUsedCurrentQuestion = false;
        enableAnswerButtons();

        if (currentQuestionIndex < questionList.size()) {
            Question currentQuestion = questionList.get(currentQuestionIndex);

            List<String> answers = new ArrayList<>();
            answers.add(currentQuestion.getOption1());
            answers.add(currentQuestion.getOption2());
            answers.add(currentQuestion.getOption3());
            answers.add(currentQuestion.getOption4());
            Collections.shuffle(answers);

            questionLabel.setText(currentQuestion.getQuestion());
            option1Button.setText(answers.get(0));
            option2Button.setText(answers.get(1));
            option3Button.setText(answers.get(2));
            option4Button.setText(answers.get(3));

            int correctIndex = answers.indexOf(
                    switch (currentQuestion.getCorrectOption()) {
                        case 1 -> currentQuestion.getOption1();
                        case 2 -> currentQuestion.getOption2();
                        case 3 -> currentQuestion.getOption3();
                        case 4 -> currentQuestion.getOption4();
                        default -> throw new IllegalStateException("Invalid correct option index");
                    });
            currentQuestion.setCorrectOption(correctIndex + 1);
        } else {
            questionLabel.setText("Chúc mừng bạn đã chiến thắng!");
            disableAnswerButtons();
            startButton.setDisable(false);
        }
    }

    @FXML
    private void handleAnswer1() {
        processAnswer(1);
    }

    @FXML
    private void handleAnswer2() {
        processAnswer(2);
    }

    @FXML
    private void handleAnswer3() {
        processAnswer(3);
    }

    @FXML
    private void handleAnswer4() {
        processAnswer(4);
    }

    private void processAnswer(int selectedOption) {
        Question currentQuestion = questionList.get(currentQuestionIndex);

        if (selectedOption == currentQuestion.getCorrectOption()) {
            correctAnswers++;
            correctAnswersLabel.setText("Câu trả lời đúng: " + correctAnswers);
            currentQuestionIndex++;
        } else {
            hearts--;
            updateHeartDisplay();
            if (hearts == 0) {
                showGameOverDialog();
                return;
            }
            currentQuestionIndex++;
        }

        loadNextQuestion();
    }

    private void showGameOverDialog() {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Bạn đã thua!");
        alert.setContentText("Bạn có muốn chơi lại không?");

        javafx.scene.control.ButtonType yesButton = new javafx.scene.control.ButtonType("Có");
        javafx.scene.control.ButtonType noButton = new javafx.scene.control.ButtonType("Không");
        alert.getButtonTypes().setAll(yesButton, noButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == yesButton) {
                resetGame();
            } else {
                resetGameUI();
            }
        });
    }

    private void updateHeartDisplay() {
        heart1.setImage(hearts >= 1 ? heartImage : emptyHeartImage);
        heart2.setImage(hearts >= 2 ? heartImage : emptyHeartImage);
        heart3.setImage(hearts >= 3 ? heartImage : emptyHeartImage);
    }

    private void disableAnswerButtons() {
        option1Button.setDisable(true);
        option2Button.setDisable(true);
        option3Button.setDisable(true);
        option4Button.setDisable(true);
    }

    private void enableAnswerButtons() {
        option1Button.setDisable(false);
        option2Button.setDisable(false);
        option3Button.setDisable(false);
        option4Button.setDisable(false);
    }

    @FXML
    private void returnToMenu(ActionEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/UserMenu-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void useFiftyFifty() {
        if (fiftyFiftyUsedThisGame || fiftyFiftyUsedCurrentQuestion) return;

        fiftyFiftyUsedThisGame = true;
        fiftyFiftyUsedCurrentQuestion = true;
        fiftyFiftyButton.setDisable(true);

        Question currentQuestion = questionList.get(currentQuestionIndex);
        List<Button> buttons = List.of(option1Button, option2Button, option3Button, option4Button);
        List<Button> incorrectButtons = new ArrayList<>();

        for (int i = 0; i < buttons.size(); i++) {
            if (i + 1 != currentQuestion.getCorrectOption()) {
                incorrectButtons.add(buttons.get(i));
            }
        }

        Collections.shuffle(incorrectButtons);
        incorrectButtons.get(0).setDisable(true);
        incorrectButtons.get(1).setDisable(true);
    }
}
