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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    private Button exit;

    @FXML
    private AnchorPane scenePane;

    @FXML
    TableView<Book> AddToTable;
    @FXML
    private TableColumn<Book, String> AddTitle;
    @FXML
    private TableColumn<Book, String> AddAuthor;

    @FXML
    private TableColumn<Book, String> publisher;
    @FXML
    private SplitMenuButton Category;


    @FXML
    private Label Sum;

    private Stage stage;

    private int countBooks() {
        int count = 0;
        String query = "SELECT COUNT(*) AS total FROM docs";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            if (resultSet.next()) {
                count = resultSet.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Error counting books in the database.");
            errorAlert.showAndWait();
        }

        return count;
    }


    @FXML
    public void exitButton(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            stage = (Stage) scenePane.getScene().getWindow();
            stage.close();
        }
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

    private String[] fetchGenresFromDatabase() {
        List<String> genres = new ArrayList<>();


        String sql = "SELECT DISTINCT category FROM docs";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String category = rs.getString("category");
                genres.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return genres.toArray(new String[0]);
    }

    @FXML
    public void handleGenreSelection(ActionEvent actionEvent) {
        // Extract the source of the event, which is a MenuItem
        MenuItem clickedItem = (MenuItem) actionEvent.getSource();

        // Get the text (genre) of the selected MenuItem
        String selectedGenre = clickedItem.getText();

        // Now load books based on the selected genre
        loadBooksByGenre(selectedGenre);
    }

    public void loadBooksByGenre(String category) {
        AddToTable.getItems().clear();

        DatabaseConnection databaseConnector = new DatabaseConnection();
        Connection connection = databaseConnector.getConnection();

        String query = "SELECT title, author, publisher FROM docs WHERE category = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, category);
            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<Book> books = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String bookTitle = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                books.add(new Book(bookTitle, author, publisher));
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

            AddToTable.setItems(books);
            updateBookCount(); // Update count after loading books by genre

        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Error loading books for the selected genre.");
            errorAlert.showAndWait();
        }
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize entertainment and academic menus
        // Populating SplitMenuButton for entertainment genres
        // Populating SplitMenuButton for academic genres

        // Configure the TableView columns on initialization
        AddTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        AddAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));


        loadAllBooks();
        updateBookCount();
    }

    private void updateBookCount() {
        int totalBooks = countBooks();
        Sum.setText("Tổng số sách:  " + totalBooks);
    }

    public void loadAllBooks() {
        AddToTable.getItems().clear();

        DatabaseConnection databaseConnector = new DatabaseConnection();
        Connection connection = databaseConnector.getConnection();

        String query = "SELECT * FROM docs";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<Book> books = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String bookTitle = resultSet.getString("title");
                String author = resultSet.getString("author");
                String publisher = resultSet.getString("publisher");
                books.add(new Book(bookTitle, author, publisher));
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();

            // Set items in TableView
            AddToTable.setItems(books);

        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Error loading books from the database.");
            errorAlert.showAndWait();
        }
    }

    public void Back(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/fxml/Menu-view.fxml"));
            Parent root = loader.load();

            // Get the current stage (window)
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            // Set the new scene with the previous screen
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}