package com.librarymanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
public class ResetPasswordInUserController {

    private Runnable onSuccess;
    public void setOnSuccess(Runnable onSuccess) {
        this.onSuccess = onSuccess;
        System.out.println("Callback set successfully.");

    }

    @FXML
    private Button CloseButton;
    @FXML
    private Label Status;

    @FXML
    private TextField EnterUser;

    @FXML
    private PasswordField EnterPass;

    @FXML
    private PasswordField ConfirmPass;

    @FXML
    public void initialize() {
        // Get user data from the session
        UserSession session = UserSession.getInstance();

        // Populate the labels

        EnterUser.setText(session.getUsername());
    }

    @FXML
    private void UpdatePass() {
        String username = EnterUser.getText();
        String newPassword = EnterPass.getText();
        String confirmPassword = ConfirmPass.getText();

        if (username.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Status.setText("Vui lòng điền đầy đủ thông tin.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Status.setText("Mật khẩu xác minh không khớp.");
            return;
        }

        boolean updated = updatePasswordInDatabase(username, newPassword);
        if (updated) {
            Status.setText("Cập nhật mật khẩu thành công!");
            if (onSuccess != null) {
                onSuccess.run();
            }
        } else {
            Status.setText("Không cập nhật mật khẩu thành công. Thử lại!");
        }



    }

    private boolean updatePasswordInDatabase(String username, String newPassword) {
        String url = "jdbc:mysql://localhost:3306/librarymanagement"; // Replace with your DB URL
        String user = "root"; // Replace with your DB username
        String password = "Dangquochuy24982765@"; // Replace with your DB password

        String sql = "UPDATE user_id SET Password = ? WHERE Username = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newPassword); // Replace with hashed password for security
            stmt.setString(2, username);

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void Close() {
        Stage stage = (Stage) CloseButton.getScene().getWindow();
        stage.close();
    }

    public void CloseAction() {
        Close();
    }

}
