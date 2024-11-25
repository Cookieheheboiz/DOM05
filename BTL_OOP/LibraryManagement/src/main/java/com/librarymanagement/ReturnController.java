package com.librarymanagement;

import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReturnController {

    @FXML
    private TableView<BorrowedBook> borrowedBooksTable;
    @FXML
    private TableColumn<BorrowedBook, String> titleColumn;
    @FXML
    private TableColumn<BorrowedBook, String> borrowDateColumn;
    @FXML
    private TableColumn<BorrowedBook, String> returnDateColumn;

    private ObservableList<BorrowedBook> borrowedBooks;


    public void initialize() {
        borrowedBooks = FXCollections.observableArrayList();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        loadBorrowedBooks();
    }

    private void loadBorrowedBooks() {
        borrowedBooks.clear();
        UserSession session = UserSession.getInstance();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT title, borrow_date, return_date FROM borrowed_books1 where user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,session.getUserID());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                borrowedBooks.add(new BorrowedBook(
                        resultSet.getString("title"),
                        resultSet.getDate("borrow_date").toString(),
                        resultSet.getDate("return_date").toString()
                ));
            }
            borrowedBooksTable.setItems(borrowedBooks);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void returnSelectedBooks(ActionEvent event) {
        BorrowedBook selectedBook = borrowedBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            try (Connection connection = DatabaseConnection.getConnection()) {
                String sql = "DELETE FROM borrowed_books1 WHERE title = ? AND borrow_date = ? AND return_date = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, selectedBook.getTitle());
                preparedStatement.setString(2, selectedBook.getBorrowDate());
                preparedStatement.setString(3, selectedBook.getReturnDate());
                preparedStatement.executeUpdate();

                String updateSql = "UPDATE docs SET quantity = quantity + 1 WHERE title = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSql);
                updateStatement.setString(1, selectedBook.getTitle());
                updateStatement.executeUpdate();

                borrowedBooks.remove(selectedBook);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Please select a book to return.");
        }
    }




    @FXML
    public void goBackToMainView(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/BorrowAndReturnView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
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