package com.librarymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CategoryControler implements Initializable {



    @FXML
    private TableView<Book> AddToTable;

    @FXML
    private TableColumn<Book, String> AddTitle;

    @FXML
    private TableColumn<Book, String> AddAuthor;

    @FXML
    private TableColumn<Book, String> publisher;

    @FXML
    private TableColumn<Book, Integer> quantity;

    @FXML
    private SplitMenuButton Category;

    @FXML
    private Label Sum;



   

    /**
     * Counts the total number of books in the database.
     */
    private int countBooks() {
        int count = 0;
        String query = "SELECT sum(quantity) AS total FROM docs";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error counting books in the database.");
        }

        return count;
    }


    @FXML
    public void AddCategory(ActionEvent actionEvent) {
        Category.getItems().clear();

        String[] categories = fetchGenresFromDatabase();

        for (String genre : categories) {
            MenuItem item = new MenuItem(genre);
            item.setOnAction(this::handleGenreSelection);
            Category.getItems().add(item);
        }
    }

    /**
     * Fetches unique genres from the database.
     */
    private String[] fetchGenresFromDatabase() {
        List<String> genres = new ArrayList<>();
        String query = "SELECT DISTINCT category FROM docs";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                genres.add(rs.getString("category"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return genres.toArray(new String[0]);
    }

    @FXML
    public void handleGenreSelection(ActionEvent actionEvent) {
        MenuItem clickedItem = (MenuItem) actionEvent.getSource();
        String selectedGenre = clickedItem.getText();
        loadBooksByGenre(selectedGenre);
    }

    public void loadBooksByGenre(String category) {
        AddToTable.getItems().clear();

        String query = "SELECT title, author, publisher, sum(quantity) as total FROM docs WHERE category = ? group by title, author, publisher";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, category);
            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<Book> books = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String bookTitle = resultSet.getString("title");
                String author = resultSet.getString("author");
                String bookPublisher = resultSet.getString("publisher");
                int quantity = resultSet.getInt("total");

                books.add(new Book(bookTitle, author, bookPublisher, quantity));
            }

            AddToTable.setItems(books);
            updateBookCount();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error loading books for the selected genre.");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AddTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        AddAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        quantity.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantity.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            int newQuantity = event.getNewValue();
            updateQuantityInDatabase(book, newQuantity);
            book.setQuantity(newQuantity); // Update the local object
            updateBookCount();
        });
        updateBookCount();
        loadAllBooks();
        AddToTable.setEditable(true);


    }

    private void updateQuantityInDatabase(Book book, int newQuantity) {
        String query = "UPDATE docs SET quantity = ? WHERE title = ? AND author = ? AND publisher = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, newQuantity);
            stmt.setString(2, book.getTitle());
            stmt.setString(3, book.getAuthor());
            stmt.setString(4, book.getPublisher());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Quantity updated successfully!");
            } else {
                System.out.println("Failed to update quantity in the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error updating quantity in the database.");
        }
    }

    private void updateBookCount() {
        int totalBooks = countBooks();
        Sum.setText("Tổng số sách: " + totalBooks);
    }



    public void loadAllBooks() {
        AddToTable.getItems().clear();

        String query = "SELECT title, author, publisher, sum(quantity) as total FROM docs group by title, author, publisher";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            ObservableList<Book> books = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String bookTitle = resultSet.getString("title");
                String author = resultSet.getString("author");
                String bookPublisher = resultSet.getString("publisher");
                int quantity = resultSet.getInt("total");

                books.add(new Book(bookTitle, author, bookPublisher, quantity));
            }

            AddToTable.setItems(books);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error loading books from the database.");
        }
    }

    @FXML
    public void Back(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/fxml/Menu-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void deleteBook(ActionEvent actionEvent) {
        Book selectedBook = AddToTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING,  "Please select a book to delete.");
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Are you sure you want to delete this book?");
        confirmationAlert.setContentText("Book: " + selectedBook.getTitle());

        if (confirmationAlert.showAndWait().get() != ButtonType.OK) {
            return;
        }

        String query = "DELETE FROM docs WHERE title = ? AND author = ? AND publisher = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, selectedBook.getTitle());
            stmt.setString(2, selectedBook.getAuthor());
            stmt.setString(3, selectedBook.getPublisher());

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                // Remove the book from the TableView
                AddToTable.getItems().remove(selectedBook);

                // Update the total book count
                updateBookCount();

                showAlert(Alert.AlertType.INFORMATION,  "The book has been deleted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR,  "Failed to delete the book.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,  "Error deleting the book from the database.");
        }

    }
}
