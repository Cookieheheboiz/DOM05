package com.librarymanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ResetPasswordController {

    @FXML
    private TextField EnterUser;

    @FXML
    private PasswordField EnterPass;

    @FXML
    private PasswordField ConfirmPass;

    @FXML
    private void UpdatePass() {
        String username = EnterUser.getText();
        String newPassword = EnterPass.getText();
        String confirmPassword = ConfirmPass.getText();

        if (username.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Lỗi", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match.");
            return;
        }

        boolean updated = updatePasswordInDatabase(username, newPassword);
        if (updated) {
            showAlert("Success", "Password updated successfully!");
        } else {
            showAlert("Error", "Failed to update the password. Please try again.");
        }
    }

    private boolean updatePasswordInDatabase(String username, String newPassword) {
        String url = "jdbc:mysql://localhost:3306/your_database"; // Replace with your DB URL
        String user = "your_db_user"; // Replace with your DB username
        String password = "your_db_password"; // Replace with your DB password

        String sql = "UPDATE users SET password = ? WHERE username = ?";

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
