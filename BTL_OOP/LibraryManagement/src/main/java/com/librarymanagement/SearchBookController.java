package com.librarymanagement;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.stage.Stage;

public class SearchBookController {

    @FXML
    private Label TrangThai;

    private static final String API_KEY = "AIzaSyD8mEOzazaLbRX0DYuvFrAincJHxitCjoU";
    @FXML
    private TextField searchField;

    @FXML
    private TableView<Book> bookTableView;
    @FXML
    private TableColumn<Book, ImageView> coverColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    @FXML
    private TableColumn<Book, String> yearColumn;
    @FXML
    private TableColumn<Book, String> categoryColumn;

    @FXML
    public void initialize() {
        coverColumn.setCellValueFactory(new PropertyValueFactory<>("coverImage"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));




    }

    @FXML
    public void onSearchButtonClick() {
        String query = searchField.getText();
        if (!query.isEmpty()) {
            searchBooks(query);
        }
    }

    private void searchBooks(String query) {
        ObservableList<Book> books = FXCollections.observableArrayList();
        try {
            String apiUrl = String.format("https://www.googleapis.com/books/v1/volumes?q=%s&key=%s",
                    URLEncoder.encode(query, "UTF-8"), API_KEY);
            HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
            connection.setRequestMethod("GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                JsonArray items = jsonResponse.getAsJsonArray("items");

                for (JsonElement item : items) {
                    JsonObject volumeInfo = item.getAsJsonObject().getAsJsonObject("volumeInfo");
                    String title = volumeInfo.has("title") ? volumeInfo.get("title").getAsString() : "N/A";
                    String author = volumeInfo.has("authors") ? volumeInfo.getAsJsonArray("authors").get(0).getAsString() : "N/A";
                    String publisher = volumeInfo.has("publisher") ? volumeInfo.get("publisher").getAsString() : "N/A";
                    String year = volumeInfo.has("publishedDate") ? volumeInfo.get("publishedDate").getAsString() : "N/A";
                    String category = volumeInfo.has("categories") ? volumeInfo.getAsJsonArray("categories").get(0).getAsString() : "N/A";



                    ImageView coverImage = null;
                    if (volumeInfo.has("imageLinks") && volumeInfo.getAsJsonObject("imageLinks").has("thumbnail")) {
                        String coverUrl = volumeInfo.getAsJsonObject("imageLinks").get("thumbnail").getAsString();
                        coverImage = new ImageView(new Image(coverUrl, 100, 150, false, false));
                    }

                    books.add(new Book(title, author, publisher, year, category, coverImage));
                }
            } else {
                System.out.println("Error: Unable to fetch data from Google Books API.");
            }
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        bookTableView.setItems(books);
    }



    public void ThemSach(ActionEvent actionEvent) {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();

        if (selectedBook == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Chưa có sách nào được chọn !");
            alert.setContentText("Hãy một cuốn !");
            alert.showAndWait();
            return;
        }

        String title = selectedBook.getTitle();
        String author = selectedBook.getAuthor();
        String publisher = selectedBook.getPublisher();
        String category = selectedBook.getCategory();

        String query = "SELECT sum(quantity) AS total_quantity FROM docs WHERE title = ? AND author = ? AND publisher = ?";
        int currentQuantity = 0;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, publisher);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                currentQuantity = rs.getInt("total_quantity"); // Get the current quantity
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        currentQuantity++;

        String sql = "INSERT INTO docs (title, author, publisher, category, quantity) VALUES (?, ?, ?, ?,?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, publisher);
            stmt.setString(4, category);
            stmt.setInt(5, currentQuantity);
            stmt.executeUpdate();


            TrangThai.setText("Book added to database successfully!");
        } catch (SQLException e) {
            e.printStackTrace();


            TrangThai.setText("Error adding book to the database.");
        }
    }

    public void Back(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/fxml/Menu-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()). getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
