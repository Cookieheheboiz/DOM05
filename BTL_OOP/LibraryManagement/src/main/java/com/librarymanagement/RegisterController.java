package com.librarymanagement;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.Node;
import javafx.scene.control.DateCell;



import javafx.event.ActionEvent;
import javafx.scene.effect.Reflection;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;

public class RegisterController {
    private Stage stage;

    @FXML
    private Button CloseButton;
    @FXML
    private Label registerMessageLabel;
    @FXML
    private PasswordField passwordFillField;
    @FXML
    private PasswordField ConfirmpasswordField;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private TextField useridTextField;
    @FXML
    private TextField FirstnameField;
    @FXML
    private TextField LastnameField;
    @FXML
    private DatePicker BirthdayField;
    @FXML
    private TextField PhonenumberField;
    @FXML
    private TextField UsernameField;
    @FXML
    private TextField UpHead;


    public void closeAndopenmain() {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/hello-view.fxml"));
            Stage returnMain = new Stage();
            returnMain.initStyle(StageStyle.UNDECORATED);
            returnMain.setScene(new Scene(root, 900, 900));
            returnMain.setTitle("Hello!");
            returnMain.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }

    public void closeButtonAction() {
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
        closeAndopenmain();
    }

    public void textinHead() {
        Reflection reflection = new Reflection();
        reflection.setFraction(0.7); // Độ dài của phần phản chiếu
        reflection.setTopOffset(10.0); // Khoảng cách giữa phần chính và phần phản chiếu
        reflection.setTopOpacity(0.7); // Độ mờ của phần đầu của phản chiếu
        reflection.setBottomOpacity(0.0);
        UpHead.setEffect(reflection);
    }

    public void registerButtonAction(ActionEvent event) {
        if (passwordFillField.getText().equals(ConfirmpasswordField.getText())) {

            confirmPasswordLabel.setText("Your password has been set correctly");

            if (registerAccount()) {

                registerMessageLabel.setText("Account has been registered successfully");
            }
        } else {
            confirmPasswordLabel.setText("Your password doesn't match");
        }


    }


    public Boolean registerAccount() {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        String ID = useridTextField.getText();
        String First_name = FirstnameField.getText();
        String Last_name = LastnameField.getText();
        String Username = UsernameField.getText();
        String Password = passwordFillField.getText();
        LocalDate DateOfBirth = BirthdayField.getValue();
        String formattedDateOfBirth = DateOfBirth != null ? DateOfBirth.toString() : null;
        String PhoneNumber = PhonenumberField.getText();
        String MyRole = "User";

        try {
            // Check for duplicate username
            String checkUsernameQuery = "SELECT COUNT(*) FROM user_id WHERE Username = ?";
            String checkIDQuery = "SELECT COUNT(*) FROM user_id WHERE ID = ?";

            boolean usernameExists = false;
            boolean idExists = false;

            try (PreparedStatement usernameStmt = connectDB.prepareStatement(checkUsernameQuery);
                 PreparedStatement idStmt = connectDB.prepareStatement(checkIDQuery)) {

                // Check for existing username
                usernameStmt.setString(1, Username);
                ResultSet usernameResult = usernameStmt.executeQuery();
                if (usernameResult.next() && usernameResult.getInt(1) > 0) {
                    usernameExists = true;
                }

                // Check for existing ID
                idStmt.setString(1, ID);
                ResultSet idResult = idStmt.executeQuery();
                if (idResult.next() && idResult.getInt(1) > 0) {
                    idExists = true;
                }
            }

            // Provide user feedback
            if (usernameExists) {
                registerMessageLabel.setText("Username is already registered. Please choose a different one.");
                return false;
            }

            if (idExists) {
                registerMessageLabel.setText("ID is already registered. Please use a different ID.");
                return false;
            }

            // Insert new account
            String insertQuery = "INSERT INTO user_id(ID, First_name, Last_name, Username, Password, MyRole, DateOfBirth, PhoneNumber) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = connectDB.prepareStatement(insertQuery)) {
                insertStmt.setString(1, ID);
                insertStmt.setString(2, First_name);
                insertStmt.setString(3, Last_name);
                insertStmt.setString(4, Username);
                insertStmt.setString(5, Password);
                insertStmt.setString(6, MyRole);
                insertStmt.setString(7, formattedDateOfBirth);
                insertStmt.setString(8, PhoneNumber);

                insertStmt.executeUpdate();
            }
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            registerMessageLabel.setText("Error: Unable to register account. Please try again.");
        return false;
        }
    }

}
