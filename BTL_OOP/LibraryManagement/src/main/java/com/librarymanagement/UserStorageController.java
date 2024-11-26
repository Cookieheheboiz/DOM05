package com.librarymanagement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.stage.Stage;

public class UserStorageController extends CategoryControler {

    @FXML
    private SplitMenuButton Category;

    @FXML
    private Label Sum;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        AddCategory();
    }

    @Override
    public void loadAllBooks() {
        super.loadAllBooks();
    }

    @Override
    public void loadBooksByGenre(String category) {
        super.loadBooksByGenre(category);
    }

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


    public void handleCategorySelection(ActionEvent event) {
        MenuItem selectedItem = (MenuItem) event.getSource();
        String selectedCategory = selectedItem.getText();

        loadBooksByGenre(selectedCategory);
    }

    public void AddCategory() {
        Category.getItems().clear();

        String[] categories = fetchGenresFromDatabase();

        for (String genre : categories) {
            MenuItem item = new MenuItem(genre);
            item.setOnAction(this::handleCategorySelection);
            Category.getItems().add(item);
        }
    }

    @FXML
    public void Return(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/fxml/UserMenu-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
