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

    public Button SearchBook;
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

    public void showBorrowAndReturnView(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/ManageUser-view.fxml"));
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
            e.getCause();
        }
    }

    public void LogoutAction() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            Stage stage = (Stage) LogoutField.getScene().getWindow();
            stage.close();
            Logout();
        }
    }

    public void AddStorage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/fxml/category.fxml")); // Updated to category.fxml
            AnchorPane nextPage = loader.load(); // Load the AnchorPane from category.fxml

            // Get the current stage
            Stage stage = (Stage) Storagemn.getScene().getWindow();

            // Set the new scene with the loaded AnchorPane
            Scene scene = new Scene(nextPage);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void FindBook() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/fxml/search-view.fxml")); // Updated to category.fxml
            AnchorPane nextPage = loader.load(); // Load the AnchorPane from category.fxml

            // Get the current stage
            Stage stage = (Stage) SearchBook.getScene().getWindow();

            // Set the new scene with the loaded AnchorPane
            Scene scene = new Scene(nextPage);
            stage.setScene(scene);
            stage.show();
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

    public void borrower() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/BorrowerManager-view.fxml"));
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

    public void borrowerAction() {
        Stage stage = (Stage) Borrowmn.getScene().getWindow();
        stage.close();
        borrower();
    }


}
