package com.librarymanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ResourceBundle;


public class HelloController {
    @FXML
    private Button SignupButton;

    @FXML
    private Button LoginButton;
    @FXML
    private Label loginMessageLabel;

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Button forgetPass;

    public String nextScenePath;

    private boolean checkClose = false;



    public void loginButtonOnAction(ActionEvent event) {
        if (usernameTextField.getText().isBlank() == false || passwordTextField.getText().isBlank() == false) {
            Stage stage = (Stage) LoginButton.getScene().getWindow();

            validateLogin();
            if(checkClose) {
                System.out.println("sadas");

            }
            else {
                System.out.println("You are tried to login, but it's failed");
            }
        } else if (usernameTextField.getText().isBlank() == true || passwordTextField.getText().isBlank() == true) {
            loginMessageLabel.setText("You are tried to login, but it's failed");
        }
    }

    public void signupButtonAction(ActionEvent event) {
        Stage stage = (Stage) SignupButton.getScene().getWindow();
        stage.close();
        createAccount();

    }


    public void validateLogin() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connectDB = databaseConnection.getConnection();

        String verifyLogin = "SELECT Username, Password, MyRole FROM user_id WHERE Username = ? AND Password = ?";
        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(verifyLogin);
            preparedStatement.setString(1, usernameTextField.getText());
            preparedStatement.setString(2, passwordTextField.getText());

            ResultSet resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) { // Check if a matching record exists
                System.out.println("Username: " + resultSet.getString("Username"));
                System.out.println("Password: " + resultSet.getString("Password"));
                System.out.println("Role: " + resultSet.getString("MyRole"));
                String retrievedRole = resultSet.getString("MyRole");

                if ("Admin".equals(retrievedRole)) {
                    loginMessageLabel.setText("Welcome, Admin!");
                    checkClose = true;
                    showLoadingScene("/com/librarymanagement/fxml/Menu-view.fxml");

                } else if ("User".equals(retrievedRole)) {
                    loginMessageLabel.setText("Welcome, User!");
                    checkClose = true;
                    showLoadingScene("/com/librarymanagement/fxml/UserMenu-view.fxml");
                } else {
                    loginMessageLabel.setText("Invalid role. Please contact support.");
                }
            } else {
                // If no record is found
                loginMessageLabel.setText("Incorrect username or password. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            loginMessageLabel.setText("An error occurred during login. Please try again.");
        }
    }



    public void createAccount() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/Register-view.fxml"));
            Stage registerStage = new Stage();
            registerStage.initStyle(StageStyle.UNDECORATED);
            registerStage.setScene(new Scene(root, 900, 900));
            registerStage.setTitle("Signup");
            registerStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void loadScene1() {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/Menu-view.fxml"));
            Stage menuStage = new Stage();
            menuStage.close();
            menuStage.initStyle(StageStyle.UNDECORATED);
            menuStage.setScene(new Scene(root, 900, 900));
            menuStage.setTitle("Signup");
            menuStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void loadScene2() {
        try {

            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/UserMenu-view.fxml"));
            Stage menuStage = new Stage();
            menuStage.initStyle(StageStyle.UNDECORATED);
            menuStage.setScene(new Scene(root, 900, 900));
            menuStage.setTitle("Signup");
            menuStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void showLoadingScene(String nextScenePath) {
        try {
            // Load the loading screen FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/fxml/loading.fxml"));
            Parent loadingRoot = loader.load();

            // Get the LoadingController instance
            LoadingController loadingController = loader.getController();

            // Set the next scene path in the LoadingController
            loadingController.setNextScenePath(nextScenePath);

            // Show the loading scene
            Stage stage = (Stage) LoginButton.getScene().getWindow();
            Scene loadingScene = new Scene(loadingRoot);
            stage.setScene(loadingScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setNextScenePath(String nextScenePath) {
        this.nextScenePath = nextScenePath;
    }

    @FXML
    private void handleForgetPass(ActionEvent event) {
        try {
            Stage currentStage = (Stage) forgetPass.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/fxml/reset_password.fxml"));
            Parent root = loader.load();

            ResetPasswordController resetPasswordController = loader.getController();

            Stage resetStage = new Stage();
            resetStage.initStyle(StageStyle.UNDECORATED);
            resetStage.setScene(new Scene(root, 619, 313));
            resetStage.setTitle("Reset Password");

            resetPasswordController.setOnSuccess(() -> {
                resetStage.close();
                showLoginScreen();
            });

            resetStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoginScreen() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/hello-view.fxml"));
            Stage loginStage = new Stage();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.setScene(new Scene(root, 900, 900));
            loginStage.setTitle("Login");
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isCheckClose() {
        return checkClose;
    }

    public void setCheckClose(boolean checkClose) {
        this.checkClose = checkClose;
    }
}
