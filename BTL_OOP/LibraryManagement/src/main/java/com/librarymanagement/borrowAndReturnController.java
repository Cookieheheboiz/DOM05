package com.librarymanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class borrowAndReturnController {
    @FXML
    private TextField txtBorrowerName;
    @FXML
    private ComboBox<String> comboBookName;
    @FXML
    private DatePicker borrowDate, returnDate;
    @FXML
    private Button btnBorrow, btnReturn, btnBack;
    @FXML
    private TableView<Book> borrowedBooksTable;
    @FXML
    private TableColumn<Book, String> bookIDColumn, bookTitleColumn, bookAuthorColumn, bookPublisherColumn, bookCategoryColumn;

    private final DatabaseConnection dbConnection = new DatabaseConnection();

    public void initialize() {
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookPublisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        bookCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        loadBooks();
    }

    private void loadBooks() {
        try (Connection connection = dbConnection.getConnection()) {
            String query = "SELECT title FROM docs"; // Sử dụng bảng 'docs' và cột 'title'
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                comboBookName.getItems().add(rs.getString("title"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBorrowBook() {
        if (borrowedBooksTable.getItems().size() >= 9) {
            showAlert(Alert.AlertType.ERROR, "Lỗi Số Lượng", "Bạn không thể mượn quá 9 quyển sách!");
            return;
        }

        String bookName = comboBookName.getValue();
        if (bookName == null) {
            showAlert(Alert.AlertType.ERROR, "Lỗi Sách", "Vui lòng chọn một quyển sách để mượn!");
            return;
        }

        LocalDate borrow = borrowDate.getValue();
        LocalDate returnD = returnDate.getValue();

        if (borrow == null || returnD == null || borrow.isAfter(returnD)) {
            showAlert(Alert.AlertType.ERROR, "Lỗi Ngày", "Ngày mượn không hợp lệ hoặc lớn hơn ngày trả!");
            return;
        }

        long daysBorrowed = ChronoUnit.DAYS.between(borrow, returnD);
        if (daysBorrowed > 180) {
            showAlert(Alert.AlertType.ERROR, "Lỗi Thời Gian Mượn", "Bạn không được phép mượn quá 180 ngày!");
            return;
        }

        String borrowerName = txtBorrowerName.getText();
        if (borrowerName == null || borrowerName.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Lỗi Tên", "Vui lòng nhập tên người mượn!");
            return;
        }

        // Lấy thông tin đầy đủ của sách theo tên
        Book bookDetails = getBookDetailsByName(bookName);
        if (bookDetails != null) {
            // Thiết lập thông tin người mượn và ngày mượn/trả
            bookDetails.setBorrowerName(borrowerName);
            bookDetails.setBorrowDate(borrow);
            bookDetails.setReturnDate(returnD);

            // Thêm sách vào bảng
            borrowedBooksTable.getItems().add(bookDetails);
            clearInputFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không tìm thấy thông tin của sách.");
        }
    }

    private Book getBookDetailsByName(String bookName) {
        try (Connection connection = dbConnection.getConnection()) {
            String query = "SELECT id, title, author, publisher, category FROM docs WHERE title = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, bookName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Tạo và trả về đối tượng Book với đầy đủ thông tin
                return new Book(
                        rs.getString("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("publisher"),
                        rs.getString("category")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void handleReturnBook() {
        Book selectedBook = borrowedBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showAlert(Alert.AlertType.WARNING, "Cảnh Báo", "Vui lòng chọn một quyển sách để trả!");
            return;
        }

        LocalDate borrowDateValue = selectedBook.getBorrowDate();
        LocalDate returnDateValue = selectedBook.getReturnDate();
        long daysBorrowed = ChronoUnit.DAYS.between(borrowDateValue, LocalDate.now());

        if (daysBorrowed > 180) {
            showAlert(Alert.AlertType.ERROR, "Lỗi Thời Gian Mượn", "Không thể trả sách sau 6 tháng!");
            return;
        }

        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), returnDateValue);
        String message = daysRemaining >= 0
                ? "Còn " + daysRemaining + " ngày để trả sách."
                : "Quá hạn " + Math.abs(daysRemaining) + " ngày!";

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác Nhận Trả Sách");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Bạn có chắc chắn muốn trả sách '" + selectedBook.getBookName() + "' không?\n" + message);
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                borrowedBooksTable.getItems().remove(selectedBook);
                clearInputFields();
            }
        });
    }

    @FXML
    private void handleBack() {
        try {
            Parent menuRoot = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/Menu-view.fxml"));
            Stage stage = (Stage) btnBack.getScene().getWindow();
            stage.setScene(new Scene(menuRoot, 900, 900));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearInputFields() {
        txtBorrowerName.clear();
        comboBookName.getSelectionModel().clearSelection();
        borrowDate.setValue(null);
        returnDate.setValue(null);
    }
}
