package com.librarymanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.util.ResourceBundle;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class HelloController {
    @FXML
    private Button SignupButton;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;



    public void loginButtonOnAction(ActionEvent event) {
        if (usernameTextField.getText().isBlank() == false || passwordTextField.getText().isBlank() == false) {
            validateLogin();
        } else {
            loginMessageLabel.setText("You are tried to login, but it's failed");
        }
    }

    public void signupButtonAction(ActionEvent event) {
        Stage stage = (Stage) SignupButton.getScene().getWindow();
        stage.close();
    }

    public void validateLogin() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connectDB = databaseConnection.getConnection();

        String verifyLogin = "select count(1) from user_id where Username = '" + usernameTextField.getText() + "' and Password = '" + passwordTextField.getText() + "'";

        try {

            Statement statement = connectDB.createStatement();
            ResultSet resultSet = statement.executeQuery(verifyLogin);

            while(resultSet.next()) {
                if (resultSet.getInt(1) == 1) {
                    loginMessageLabel.setText("Congratulations, you are logged in");
                } else {
                    loginMessageLabel.setText("Please try again");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }




}