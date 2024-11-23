package com.librarymanagement;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BorrowController {

    @FXML
    private ComboBox<String> bookComboBox;
    @FXML
    private TableView<Book> bookTableView;
    @FXML
    private TableColumn<Book, Integer> idColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    @FXML
    private TableColumn<Book, String> categoryColumn;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button borrowButton;
    @FXML
    private Button backButton;

    public void initialize() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT title FROM docs";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bookComboBox.getItems().add(resultSet.getString("title"));
            }

            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
            authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
            publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBorrowButtonClick(ActionEvent event) {
        String selectedTitle = bookComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (selectedTitle == null) {
            showAlert("Please select a book from the dropdown.");
            return;
        }

        if (startDate == null || endDate == null) {
            showAlert("Please select both borrow and return dates.");
            return;
        }

        if (startDate.isAfter(endDate)) {
            showAlert("Borrow date cannot be after return date.");
            return;
        }

        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        if (daysBetween > 90) {
            showAlert("The borrow duration cannot exceed 90 days.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM docs WHERE title = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, selectedTitle);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("publisher"),
                        resultSet.getString("category")
                );

                boolean alreadyExists = bookTableView.getItems().stream()
                        .anyMatch(existingBook -> existingBook.getId() == book.getId());

                if (!alreadyExists) {
                    bookTableView.getItems().add(book);

                    // Lưu vào database borrowed_books
                    String insertSql = "INSERT INTO borrowed_books1 (title, author, publisher, category, borrow_date, return_date,User_id) VALUES (?, ?, ?, ?, ?, ?,?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                    insertStatement.setString(1, book.getTitle());
                    insertStatement.setString(2, book.getAuthor());
                    insertStatement.setString(3, book.getPublisher());
                    insertStatement.setString(4, book.getCategory());
                    insertStatement.setDate(5, java.sql.Date.valueOf(startDate));
                    insertStatement.setDate(6, java.sql.Date.valueOf(endDate));
                    insertStatement.setInt(7, HelloController.loginUserId);
                    insertStatement.executeUpdate();

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onBackButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/fxml/BorrowAndReturnView.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}   