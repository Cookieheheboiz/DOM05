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
        UserSession session = UserSession.getInstance();
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
            String sql = "SELECT id, title, author, publisher, category FROM docs WHERE title = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, selectedTitle);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                int quantity = resultSet.getInt("quantity");

                if (quantity <= 0) {
                    showAlert("The books are fully borrowed!");
                    return;
                }

                Book book = new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("publisher"),
                        resultSet.getString("category")
                );

                String checkExistingSql = "SELECT count(*) FROM borrowed_books1 WHERE id = ? AND user_id = ?";
                PreparedStatement checkExistingStmt = connection.prepareStatement(checkExistingSql);
                checkExistingStmt.setInt(1, book.getId());
                checkExistingStmt.setInt(2, session.getUserID()); // Assuming HelloController has loginUserId
                ResultSet checkExistingResult = checkExistingStmt.executeQuery();

                if (checkExistingResult.next() && checkExistingResult.getInt(1) > 0 ) {
                    showAlert("You have already borrowed this book. You cannot borrow it again.");
                    return;
                }

                    bookTableView.getItems().add(book);

                    String insertSql = "INSERT INTO borrowed_books1 (id, title, author, publisher, category, borrow_date, return_date,User_id) VALUES (?, ?, ?, ?, ?, ?,?,?)";
                    PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                    System.out.println(book.getId());

                    insertStatement.setInt(1,book.getId());
                    insertStatement.setString(2, book.getTitle());
                    insertStatement.setString(3, book.getAuthor());
                    insertStatement.setString(4, book.getPublisher());
                    insertStatement.setString(5, book.getCategory());
                    insertStatement.setDate(6, java.sql.Date.valueOf(startDate));
                    insertStatement.setDate(7, java.sql.Date.valueOf(endDate));
                    insertStatement.setInt(8, HelloController.loginUserId);
                    insertStatement.executeUpdate();

                    String updateSql = "UPDATE docs SET quantity = quantity - 1 WHERE id = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                    updateStatement.setInt(1, book.getId());

                    updateStatement.executeUpdate();
                    showAlert("The book has been successfully borrowed.");


            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("An error occurred while borrowing the book. Please try again.");
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