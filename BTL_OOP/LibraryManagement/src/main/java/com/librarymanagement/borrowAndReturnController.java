package com.librarymanagement;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class borrowAndReturnController {

    public void onBorrowBooks(ActionEvent event) {
        loadView("/com/librarymanagement/fxml/BorrowView.fxml", event);
    }

    public void onReturnBooks(ActionEvent event) {
        loadView("/com/librarymanagement/fxml/ReturnView.fxml", event);
    }

    public void onBackToMenu(ActionEvent event) {
        loadView("/com/librarymanagement/fxml/Menu-view.fxml", event);
    }

    private void loadView(String fxmlPath, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
