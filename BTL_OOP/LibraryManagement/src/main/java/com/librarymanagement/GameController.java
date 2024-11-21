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

public class GameController {
    @FXML
    private Label questionLabel, correctAnswersLabel;
    @FXML
    private Button option1Button, option2Button, option3Button, startButton, returnToMenuButton;
    @FXML
    private ImageView heart1, heart2, heart3;

    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int hearts = 3;
    private int correctAnswers = 0;
    private final Image heartImage = new Image(getClass().getResource("/image/heart_icon.png").toString());
    private final Image emptyHeartImage = new Image(getClass().getResource("/image/empty_heart_icon.png").toString());

    @FXML
    public void initialize() {
        loadQuestionsFromDatabase();
        resetGameUI();
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
                        resultSet.getInt("correct_option")
                ));
            }
            Collections.shuffle(questionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void startGame() {
        hearts = 3;
        correctAnswers = 0;
        currentQuestionIndex = 0;
        correctAnswersLabel.setText("Câu trả lời đúng: " + correctAnswers);
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
        if (currentQuestionIndex < questionList.size()) {
            Question currentQuestion = questionList.get(currentQuestionIndex);

            List<String> answers = new ArrayList<>();
            answers.add(currentQuestion.getOption1());
            answers.add(currentQuestion.getOption2());
            answers.add(currentQuestion.getOption3());
            Collections.shuffle(answers);

            questionLabel.setText(currentQuestion.getQuestion());
            option1Button.setText(answers.get(0));
            option2Button.setText(answers.get(1));
            option3Button.setText(answers.get(2));

            int correctIndex = answers.indexOf(
                    switch (currentQuestion.getCorrectOption()) {
                        case 1 -> currentQuestion.getOption1();
                        case 2 -> currentQuestion.getOption2();
                        case 3 -> currentQuestion.getOption3();
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
                questionLabel.setText("Bạn đã thua! Nhấn 'Bắt đầu' để chơi lại.");
                disableAnswerButtons();
                startButton.setDisable(false);
                return;
            }
            currentQuestionIndex++;
        }

        loadNextQuestion();
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
    }

    private void enableAnswerButtons() {
        option1Button.setDisable(false);
        option2Button.setDisable(false);
        option3Button.setDisable(false);
    }

    @FXML
    private void returnToMenu(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/Menu-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Menu");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
