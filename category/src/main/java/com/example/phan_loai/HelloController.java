package com.example.phan_loai;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Button exit;

    @FXML
    private AnchorPane scenePane;

    @FXML
    private TableView<Book> AddToTable;
    @FXML
    private TableColumn<Book, String> AddTitle;
    @FXML
    private TableColumn<Book, String> AddAuthor;

    @FXML
    private SplitMenuButton entertain;

    @FXML
    private SplitMenuButton academic;

    private Stage stage;

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
    public void AddEntertain(ActionEvent actionEvent) {
        entertain.getItems().clear();

        String[] entertainGenres = {"Truyện tranh", "Tiểu thuyết", "Sách phiêu lưu", "Sách giả tưởng", "Sách hài hước"};

        for (String genre : entertainGenres) {
            MenuItem item = new MenuItem(genre);
            item.setOnAction(this::handleGenreSelection);  // Add action on menu item click
            entertain.getItems().add(item);
        }
    }

    @FXML
    public void AddAcademic(ActionEvent actionEvent) {
        academic.getItems().clear();

        String[] academicGenres = {"Sách giáo khoa", "Sách tham khảo", "Giáo trình", "Sách học thuật nâng cao"};

        for (String genre : academicGenres) {
            MenuItem item = new MenuItem(genre);
            item.setOnAction(this::handleGenreSelection);
            academic.getItems().add(item);
        }
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

    public void loadBooksByGenre(String genre) {
        // Clear the current table view
        AddToTable.getItems().clear();

        DatabaseConnector databaseConnector = new DatabaseConnector();
        Connection connection = databaseConnector.getConnection();

        String query = "SELECT title, author FROM books WHERE genre = ?";  // Assuming your table is named 'books' and has columns 'title' and 'author'

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, genre);  // Set the genre parameter

            ResultSet resultSet = preparedStatement.executeQuery();

            // Create a list of Book objects to store the data
            ObservableList<Book> books = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String bookTitle = resultSet.getString("title");
                String author = resultSet.getString("author");
                books.add(new Book(bookTitle, author));
            }

            // Close the resources
            resultSet.close();
            preparedStatement.close();
            connection.close();

            // Set the items for the table view
            AddToTable.setItems(books);

            // Map the table columns to the Book class properties
            AddTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            AddAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));

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
        AddEntertain(null);  // Populating SplitMenuButton for entertainment genres
        AddAcademic(null);   // Populating SplitMenuButton for academic genres

        // Configure the TableView columns on initialization
        AddTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        AddAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
    }

    public class Book {
        private final String title;
        private final String author;

        public Book(String title, String author) {
            this.title = title;
            this.author = author;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }
    }

}
