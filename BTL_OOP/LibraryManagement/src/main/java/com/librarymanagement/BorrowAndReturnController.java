package com.librarymanagement;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BorrowAndReturnController {

    public void onBorrowBooks(MouseEvent event) {
        if (checkOverdueBooks(getCurrentUserId())){
            showAlert("Vui lòng trả sách đúng hạn trước khi mượn");
        }
else {
        loadView("/com/librarymanagement/fxml/BorrowView.fxml", event);
}
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cảnh báo");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void onReturnBooks(MouseEvent event) {
        loadView("/com/librarymanagement/fxml/ReturnView.fxml", event);
    }

    public void onBackToMenu(MouseEvent event) {
        loadView("/com/librarymanagement/fxml/UserMenu-view.fxml", event);
    }

    private void loadView(String fxmlPath, MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getCurrentUserId() {
        return HelloController.loginUserId;
    }

    private boolean checkOverdueBooks(int userId) {
        String query = "SELECT COUNT(*) FROM borrowed_books1 WHERE user_id = ? AND DATE(return_date) < CURDATE()";
        try (Connection connection = DatabaseConnection.getConnection()){
             PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int overdueCount = resultSet.getInt(1);
                return overdueCount > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}


