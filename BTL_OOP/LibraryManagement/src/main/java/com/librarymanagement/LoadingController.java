package com.librarymanagement;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {
    @FXML
    private AnchorPane scenePane;

    @FXML
    private ProgressBar Progress;

    @FXML
    private Text Loading;

    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Progress.setStyle("-fx-accent: #0080FF;");
        Loading.setStyle("-fx-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        startLoading();
    }

    private void startLoading() {
        Progress.setProgress(0);

        Timeline timeline = new Timeline();

        timeline.setCycleCount(1);

        for (int i = 0; i <= 22; i++) {
            final int progressValue = i;
            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(i * 0.3),
                    event -> Progress.setProgress(progressValue / 20.0)
            ));
        }

        timeline.setOnFinished(event -> {
            loadNextPage();
        });

        timeline.play();
    }
    private void loadNextPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/fxml/Menu-view.fxml")); // Update with your actual FXML file path
            Pane nextPage = loader.load();

            stage = (Stage) scenePane.getScene().getWindow();


            Scene scene = new Scene(nextPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
