package com.librarymanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MenuController {

    @FXML
    private Button Borrowmn;

    @FXML
    private Button storagemn;

    @FXML
    private Button GameField;
    @FXML
    private Button borrowAndReturn;
    @FXML
    private Button employeeField;
    @FXML
    private Button LogoutField;

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
        Stage stage = (Stage) LogoutField.getScene().getWindow();
        stage.close();
        Logout();
    }

}
