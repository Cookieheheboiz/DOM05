package com.librarymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ManageUser {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @FXML
    private TableView<UserData> UserIn4;

    @FXML
    private TableColumn<UserData, Integer> UserId;

    @FXML
    private TableColumn<UserData, String> BorrowedBook;

    @FXML
    private TableColumn<UserData,String>author;
    @FXML
    private TableColumn<UserData, String> BorrowDate;

    @FXML
    private TableColumn<UserData, String> ReturnDate;

    @FXML
    private TableColumn<UserData, String> Status;


    private ObservableList<UserData> userList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configure table columns
        UserId.setCellValueFactory(new PropertyValueFactory<>("id"));
        author.setCellValueFactory(new PropertyValueFactory<>("author"));
        BorrowedBook.setCellValueFactory(new PropertyValueFactory<>("borrowedBook"));
        BorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        ReturnDate.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        Status.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Load data from the database
        loadData();
    }

    private void loadData() {
        // Clear the current list to avoid duplicates
        userList.clear();

        try {
            // Connect to the database
            Connection connection = DatabaseConnection.getConnection();

           Statement statement = connection.createStatement();
            String query = "SELECT User_id as id, author, title AS borrowedBook, borrow_date AS borrowDate, return_date AS returnDate FROM borrowed_books1";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String author = resultSet.getString("author");
                String borrowedBook = resultSet.getString("borrowedBook");
                String borrowDate = resultSet.getString("borrowDate");
                String returnDate = resultSet.getString("returnDate");
                LocalDate returnLocalDate = LocalDate.parse(returnDate, DATE_FORMATTER);
                LocalDate today = LocalDate.now();
                String status = "";

                if (today.isAfter(returnLocalDate)) {
                    status = "Quá hạn"; // Overdue
                }
                else {
                    status = "Chưa trả"; // Not yet returned
                }
                userList.add(new UserData(id, author, borrowedBook, borrowDate, returnDate, status));
            }

            resultSet.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set the data in the TableView
        UserIn4.setItems(userList);
    }




    public void DeleteUser(ActionEvent event) {

    }

    public void reTurn(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/fxml/Menu-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
