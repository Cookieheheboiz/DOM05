package com.librarymanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class MenuController {

    @FXML
    private Button SearchBook;
    @FXML
    private Button Borrowmn;
    @FXML
    private Button Storagemn;
    @FXML
    private Button GameField;
    @FXML
    private Button borrowAndReturn;
    @FXML
    private Button employeeField;
    @FXML
    private Button LogoutField;

    @FXML
    private void initialize() {
        // Không cần thiết lập onAction ở đây, đã thiết lập trong FXML
    }

    public void showBorrowAndReturnView(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/BorrowAndReturnView.fxml"));
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Borrow and Return Books");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openGameView(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/Game.fxml"));
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Game");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Logout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/hello-view.fxml"));
            Stage logout = new Stage();
            logout.initStyle(StageStyle.UNDECORATED);
            logout.setScene(new Scene(root, 900, 900));
            logout.setTitle("Hello!");
            logout.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void LogoutAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            Stage stage = (Stage) LogoutField.getScene().getWindow();
            stage.close();
            Logout();
        }
    }

    public void AddStorage(ActionEvent event) {
        try {
            AnchorPane nextPage = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/category.fxml"));
            Stage stage = (Stage) Storagemn.getScene().getWindow();
            stage.setScene(new Scene(nextPage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void FindBook(ActionEvent event) {
        try {
            AnchorPane nextPage = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/search-view.fxml"));
            Stage stage = (Stage) SearchBook.getScene().getWindow();
            stage.setScene(new Scene(nextPage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void employee() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/User-view.fxml"));
            Stage employ = new Stage();
            employ.initStyle(StageStyle.UNDECORATED);
            employ.setScene(new Scene(root, 900, 900));
            employ.setTitle("Hello!");
            employ.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void employeeAction() {
        Stage stage = (Stage) employeeField.getScene().getWindow();
        stage.close();
        employee();
    }


    public void borrowerAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/User-view.fxml"));
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(root, 900, 900));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
