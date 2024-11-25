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
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
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
            registerAccount();

            confirmPasswordLabel.setText("Your password has been set correctly");
            registerMessageLabel.setText("Account has been registered successfully");
        } else {
            confirmPasswordLabel.setText("Your password doesn't match");
        }


    }

    public void registerAccount() {
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


        String insertField = "INSERT INTO user_id(ID, First_name, Last_name, Username, Password, MyRole, DateOfBirth, PhoneNumber) VALUES('";
        String insertValue = ID + "','" + First_name + "','" + Last_name + "','" + Username + "','" +  Password + "','" + MyRole + "','" + formattedDateOfBirth + "','" + PhoneNumber +"')";
        String insertToRegister = insertField + insertValue;


        try{
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToRegister);

            registerMessageLabel.setText("Your account has been signed up successfully");

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }


    }
}
