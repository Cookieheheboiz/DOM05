package com.librarymanagement;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.Media;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class UserMenuController extends CategoryControler {
    @FXML
    private Button ReloadButton;
    @FXML
    private Button UserIn4;
    @FXML
    private Button BorrowedBook;
    @FXML
    private Button StorageLib;
    @FXML
    private Button LibCard;
    @FXML
    private Button LogOutbutton;
    private String nextPath;
    @FXML
    private Button ChangepwButton;

    @FXML
    private Label IDField;
    @FXML
    private Label FullnameField;
    @FXML
    private Label PhonenumberField;
    @FXML
    private Label BirthdayField;

    @FXML
    private Label Cardi4name;
    @FXML
    private Label Cardi4ID;
    @FXML
    private Label Cardi4birthday;
    @FXML
    private Label Cardi4phone;

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

    @FXML
    private TableView<BorrowedBook> borrowedBooksTable;
    @FXML
    private TableColumn<BorrowedBook, String> titleColumn1;
    @FXML
    private TableColumn<BorrowedBook, String> borrowDateColumn;
    @FXML
    private TableColumn<BorrowedBook, String> returnDateColumn;
    @FXML
    private TableColumn<BorrowedBook, String> userColumn;

    private ObservableList<BorrowedBook> borrowedBooks;
    private Timeline refreshTimeline; // Timeline để làm mới dữ liệu

    @FXML
    private TableView<BorrowedBook> SuggestTable;
    @FXML
    private TableColumn<BorrowedBook, Integer> TierSuggest;
    @FXML
    private TableColumn<BorrowedBook, String> TitleSuggest;
    @FXML
    private TableColumn<BorrowedBook, String> AuthorSuggest;
    @FXML
    private TableColumn<BorrowedBook, String> CategorySuggest;
    @FXML
    private TableColumn<BorrowedBook, Integer> AmountSuggest;


    @FXML
    private Button BRTborrowButton;
    @FXML
    private Button BRTreturnButton;
    @FXML
    private Button BRTbackTomainButton;


    @FXML
    private SplitMenuButton Category;

    @FXML
    private Label Sum;



    @FXML
    private AnchorPane userIn4anchorPane;


    @FXML
    private AnchorPane BorrowBookanchorpane;
    @FXML
    private AnchorPane ReturnBookAnchorPane;
    @FXML
    private AnchorPane ReturnAndBorrowAnchorpane;

    @FXML
    private AnchorPane FirstanchorPane;

    @FXML
    private AnchorPane StorageBookAnchorPane;
    @FXML
    private AnchorPane UserCardAnchorPane;
    private List<Media> backgroundMusic = new ArrayList<>();
    private MediaPlayer currentMediaPlayer;
    private int currentTrackIndex = 0;
    private boolean isMuted = false;
    @FXML
    private Button nextButton;

    @FXML
    private Button muteButton;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        super.initialize(location, resources);
        AddCategory();

        // Get user data from the session
        UserSession session = UserSession.getInstance();

        // Populate the labels in userI4anchorPane
        IDField.setText(String.valueOf(session.getUserID()));
        FullnameField.setText(session.getFullName());

        PhonenumberField.setText(session.getPhoneNumber());
        BirthdayField.setText(session.getBirthday());

        // Populate the labels in LibraryCard
        Cardi4name.setText(session.getFullName());
        Cardi4ID.setText(String.valueOf(session.getUserID()));
        Cardi4birthday.setText(session.getBirthday());
        Cardi4phone.setText(session.getPhoneNumber());

        // return Book part
        borrowedBooks = FXCollections.observableArrayList();
        titleColumn1.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));


        loadBorrowedBooks();

        try {
            // Đọc danh sách file nhạc từ thư mục
            File musicDirectory = new File("D:/DOM05/BTL_OOP/LibraryManagement/src/main/resources/music");
            File[] musicFiles = musicDirectory.listFiles((dir, name) -> name.endsWith(".mp3"));

            if (musicFiles != null) {
                for (File file : musicFiles) {
                    backgroundMusic.add(new Media(file.toURI().toString()));
                }

                // Bắt đầu phát bài đầu tiên
                playMusic();
            } else {
                System.out.println("No music files found in the directory.");
            }
            } catch (Exception e) {
            throw new RuntimeException(e);
        }

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

    private void playMusic() {
        if (!backgroundMusic.isEmpty()) {
            if (currentMediaPlayer != null) {
                currentMediaPlayer.stop();
            }

            currentMediaPlayer = new MediaPlayer(backgroundMusic.get(currentTrackIndex));
            currentMediaPlayer.play();
        }
    }

    @FXML
    private void onNextButtonClick() {
        if (!backgroundMusic.isEmpty()) {
            currentTrackIndex = (currentTrackIndex + 1) % backgroundMusic.size();
            playMusic();
        }
    }

    @FXML
    private void onMuteButtonClick() {
        if (currentMediaPlayer != null) {
            isMuted = !isMuted;
            currentMediaPlayer.setMute(isMuted);
            muteButton.setText(isMuted ? "Unmute" : "Mute");
        }
    }

    @Override
    public void loadAllBooks() {
        super.loadAllBooks();
    }

    @Override
    public void loadBooksByGenre(String category) {
        super.loadBooksByGenre(category);
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

    public void openGameView(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/GameUser.fxml"));
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(root));
            currentStage.setTitle("Game");
            currentMediaPlayer.setMute(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /// Borrow book anchorpane action
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
            String sql = "SELECT id, title, author, publisher, category, quantity FROM docs WHERE title = ?";
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
                checkExistingStmt.setInt(2, HelloController.loginUserId); // Assuming HelloController has loginUserId
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



    /// show alert when user does not select a book to borrow
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void hideAllAnchorPanes() {
        AnchorPane[] panes = {userIn4anchorPane, FirstanchorPane, BorrowBookanchorpane, ReturnBookAnchorPane, ReturnAndBorrowAnchorpane,
                                StorageBookAnchorPane, UserCardAnchorPane}; // Include all anchor panes here
        for (AnchorPane pane : panes) {
            if (pane != null) {
                pane.setVisible(false);
            }
        }
    }

    public void switchToUserInfoPane() {
        hideAllAnchorPanes();
        if (userIn4anchorPane != null) {
            userIn4anchorPane.setVisible(true);
        }
    }

    public void  switchToBorrowBookAnchorPane() {
        hideAllAnchorPanes();
        if (BorrowBookanchorpane != null) {
            BorrowBookanchorpane.setVisible(true);
        }
    }

    public void  switchToReturnBookAnchorPane() {
        hideAllAnchorPanes();
        if (ReturnBookAnchorPane != null) {
            ReturnBookAnchorPane.setVisible(true);
        }
    }

    public void switchToFirstAnchorPane() {
        hideAllAnchorPanes();
        if (FirstanchorPane != null) {
            FirstanchorPane.setVisible(true);
        }
    }

    public void switchToBorrowAndReturnBookAnchorPane() {
        hideAllAnchorPanes();
        if (ReturnAndBorrowAnchorpane != null) {
            ReturnAndBorrowAnchorpane.setVisible(true);
        }
    }

    public void switchToStorageBookAnchorPane() {
        hideAllAnchorPanes();
        if (StorageBookAnchorPane != null) {
            StorageBookAnchorPane.setVisible(true);
        }
    }

    public void switchToLibraryCardAnchorPane() {
        hideAllAnchorPanes();
        if (UserCardAnchorPane != null) {
            UserCardAnchorPane.setVisible(true);
        }
    }

    public void Logout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/hello-view.fxml"));
            Stage logout = new Stage();

            logout.setScene(new Scene(root, 900, 900));
            logout.setTitle("Hello!");
            logout.show();
            currentMediaPlayer.setMute(true);

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void LogoutAction() {
        UserSession session = UserSession.getInstance();
        session.setUserID(0);
        session.setFullName(null);
        session.setPhoneNumber(null);
        session.setBirthday(null);
        Stage stage = (Stage) LogOutbutton.getScene().getWindow();
        stage.close();
        Logout();
    }

    public void LibraryCard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/LibraryCard-view.fxml"));
            Stage card = new Stage();
            card.initStyle(StageStyle.UNDECORATED);
            card.setScene(new Scene(root, 900, 900));
            card.setTitle("Hello!");
            card.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

        public void LibraryCardAction() {
        Stage stage = (Stage) LibCard.getScene().getWindow();
        stage.close();
        LibraryCard();
    }






    public void ChangePassword() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/reset_passwordInUser-view.fxml"));
            Stage changePassword = new Stage();
            changePassword.initStyle(StageStyle.UNDECORATED);
            changePassword.setScene(new Scene(root, 600, 310));
            changePassword.setTitle("Hello!");
            changePassword.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void ChangePasswordAction() {
        ChangePassword();
    }


    // Show the book which user borrowed
    private void loadBorrowedBooks() {
        borrowedBooks.clear();
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT title, borrow_date, return_date FROM borrowed_books1 where user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,HelloController.loginUserId);
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


    // Return the selected book
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
    private void reloadScene(ActionEvent event) {
        try {
            currentMediaPlayer.setMute(true);
            // Save the current visible pane for restoration
            String currentView = getCurrentVisiblePane(); // Implement this method to get the current view's ID or name

            // Reload the FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/fxml/UserMenu-view.fxml"));
            Parent root = loader.load();

            // Pass session and current view data to the new controller
            UserMenuController newController = loader.getController();
            newController.restoreState(currentView); // Implement restoreState method in your controller

            // Get the current stage and set the new scene
            Stage stage = (Stage) ReloadButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error occurred while reloading the scene.");
        }
    }

    private String getCurrentVisiblePane() {
        if (userIn4anchorPane.isVisible()) return "userIn4anchorPane";
        if (FirstanchorPane.isVisible()) return "FirstanchorPane";
        if (BorrowBookanchorpane.isVisible()) return "BorrowBookanchorpane";
        if (ReturnBookAnchorPane.isVisible()) return "ReturnBookAnchorPane";
        if (ReturnAndBorrowAnchorpane.isVisible()) return "ReturnAndBorrowAnchorpane";
        if (StorageBookAnchorPane.isVisible()) return "StorageBookAnchorPane";
        return "FirstanchorPane"; // Default fallback
    }

    public void restoreState(String paneName) {
        hideAllAnchorPanes();
        switch (paneName) {
            case "userIn4anchorPane":
                switchToUserInfoPane();
                break;
            case "FirstanchorPane":
                switchToFirstAnchorPane();
                break;
            case "BorrowBookanchorpane":
                switchToBorrowBookAnchorPane();
                break;
            case "ReturnBookAnchorPane":
                switchToReturnBookAnchorPane();
                break;
            case "ReturnAndBorrowAnchorpane":
                switchToBorrowAndReturnBookAnchorPane();
                break;
            case "StorageBookAnchorPane":
                switchToStorageBookAnchorPane();
                break;
        }
    }

    /// Top 5 book suggest for user

}
