package com.librarymanagement;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController extends HelloController implements Initializable {
    @FXML
    private AnchorPane scenePane;

    @FXML
    private ProgressBar Progress;

    @FXML
    private Text Loading;

    private Stage stage;

    private String userID; // User information
    private String fullName;
    private String phoneNumber;
    private String birthday;
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
            final int progressValue = i; // Need to make this final for the lambda
            timeline.getKeyFrames().add(new KeyFrame(
                    Duration.seconds(i * 0.3), // Change this to adjust the timing
                    event -> Progress.setProgress(progressValue / 20.0) // Incrementally set the progress
            ));
        }
        timeline.setOnFinished(event -> loadNextPage());
        timeline.play();
    }
    private void loadNextPage() {
        try {
            // Load the next scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource(nextScenePath));

            if (nextScenePath.equals("/com/librarymanagement/fxml/UserMenu-view.fxml")) {
                UserMenuController controller = new UserMenuController();

            }
            Parent nextPage = loader.load();

            Stage stage = (Stage) scenePane.getScene().getWindow();
            stage.setScene(new Scene(nextPage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Setter for nextScenePath
    public void setNextScenePath(String nextScenePath) {
        this.nextScenePath = nextScenePath;
    }
}
