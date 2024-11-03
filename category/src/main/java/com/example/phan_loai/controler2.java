package com.example.phan_loai;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class controler2 implements Initializable {

    @FXML
    private AnchorPane scenePane;

    @FXML
    private ProgressBar Progress;

    @FXML
    private Text Loading;

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Progress.setStyle("-fx-accent: #0080FF;"); // Set progress bar fill color to blue
        Loading.setStyle("-fx-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;"); // Set text properties
        startLoading();
    }

    // Start the loading process with progress bar gradually filling
    private void startLoading() {
        Progress.setProgress(0);  // Initialize progress to 0

        // Create a Timeline to gradually update the progress bar over 2 seconds (2000 milliseconds)
        Timeline timeline = new Timeline();

        timeline.setCycleCount(1);  // Run only once

        for (int i = 0; i <= 22; i++) {
            final int progressValue = i; // Need to make this final for the lambda
            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(i * 0.3), // Change this to adjust the timing
                    event -> Progress.setProgress(progressValue / 20.0) // Incrementally set the progress
            ));
        }

        timeline.setOnFinished(event -> {
            loadNextPage();  // Call method to load the next page after loading completes
        });

        // Start the timeline animation
        timeline.play();
    }
    private void loadNextPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("phan_loai.fxml")); // Update with your actual FXML file path
            AnchorPane nextPage = loader.load();


            stage = (Stage) scenePane.getScene().getWindow();


            Scene scene = new Scene(nextPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


