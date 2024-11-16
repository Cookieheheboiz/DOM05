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

    private boolean checkClose = false;



    public void loginButtonOnAction(ActionEvent event) {
        if (usernameTextField.getText().isBlank() == false || passwordTextField.getText().isBlank() == false) {
            Stage stage = (Stage) LoginButton.getScene().getWindow();

            validateLogin();
            if(checkClose) {
                System.out.println("sadas");
                stage.close();
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
                    loadScene1();
                } else if ("User".equals(retrievedRole)) {
                    loginMessageLabel.setText("Welcome, User!");
                    checkClose = true;
                    loadScene2();
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


    public boolean isCheckClose() {
        return checkClose;
    }

    public void setCheckClose(boolean checkClose) {
        this.checkClose = checkClose;
    }
}
