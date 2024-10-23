package com.example.baitaplon;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit; // Thêm import để tính số ngày
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LibraryController {
    @FXML
    private TextField txtBorrowerName;

    @FXML
    private ComboBox<String> comboBookName;

    @FXML
    private DatePicker borrowDate, returnDate;

    @FXML
    private Button btnBorrow, btnReturn, btnBack; // Thêm btnBack

    // Khai báo TableView và các cột
    @FXML
    private TableView<Book> borrowedBooksTable;

    @FXML
    private TableColumn<Book, String> bookIDColumn, bookNameColumn, borrowerNameColumn;

    public void initialize() {
        // Đặt các cột tương ứng với thuộc tính của đối tượng Book
        bookIDColumn.setCellValueFactory(new PropertyValueFactory<>("bookID"));
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        borrowerNameColumn.setCellValueFactory(new PropertyValueFactory<>("borrowerName"));

        // Gọi phương thức để load danh sách sách vào ComboBox
        loadBooks();
    }

    private void loadBooks() {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT name FROM books";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String bookName = rs.getString("name");
                comboBookName.getItems().add(bookName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBorrowBook() {
        // Kiểm tra số lượng sách đang mượn
        if (borrowedBooksTable.getItems().size() >= 9) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi Số Lượng");
            alert.setHeaderText(null);
            alert.setContentText("Bạn không thể mượn quá 9 quyển sách!");
            alert.showAndWait();
            return;
        }

        // Kiểm tra nếu người dùng đã chọn sách
        String bookName = comboBookName.getValue();
        if (bookName == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi Sách");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một quyển sách để mượn!");
            alert.showAndWait();
            return;
        }

        // Lấy ngày mượn và ngày trả
        LocalDate borrow = borrowDate.getValue();
        LocalDate returnD = returnDate.getValue();

        // Kiểm tra nếu ngày mượn lớn hơn ngày trả
        if (borrow != null && returnD != null && borrow.isAfter(returnD)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi Ngày");
            alert.setHeaderText(null);
            alert.setContentText("Ngày mượn không thể lớn hơn ngày trả!");
            alert.showAndWait();
            return;
        }

        // Kiểm tra thời gian mượn không vượt quá 180 ngày
        if (borrow != null && returnD != null) {
            long daysBorrowed = ChronoUnit.DAYS.between(borrow, returnD);
            if (daysBorrowed > 180) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi Thời Gian Mượn");
                alert.setHeaderText(null);
                alert.setContentText("Bạn không được phép mượn quá 180 ngày!");
                alert.showAndWait();
                return;
            }
        }

        // Kiểm tra tên người mượn
        String borrowerName = txtBorrowerName.getText();
        if (borrowerName == null || borrowerName.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi Tên");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập tên người mượn!");
            alert.showAndWait();
            return;
        }

        // Lấy ID sách
        String bookID = getBookIDByName(bookName);

        // Tạo đối tượng Book mới
        Book newBook = new Book(bookID, bookName, borrowerName, borrow, returnD);
        borrowedBooksTable.getItems().add(newBook);

        // Xóa dữ liệu đã nhập sau khi mượn
        txtBorrowerName.clear();
        comboBookName.getSelectionModel().clearSelection();
        borrowDate.setValue(null);
        returnDate.setValue(null);
    }

    private String getBookIDByName(String bookName) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            String query = "SELECT id FROM books WHERE name = ?";
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, bookName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void handleReturnBook() {
        // Lấy đối tượng sách đang được chọn trong bảng
        Book selectedBook = borrowedBooksTable.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh Báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một quyển sách để trả!");
            alert.showAndWait();
            return;
        }

        // Lấy ngày trả từ đối tượng sách
        LocalDate borrowDateValue = selectedBook.getBorrowDate();
        LocalDate returnDateValue = selectedBook.getReturnDate();

        // Kiểm tra số ngày đã mượn
        long daysBorrowed = ChronoUnit.DAYS.between(borrowDateValue, LocalDate.now());

        // Kiểm tra thời gian mượn tối đa là 6 tháng
        if (daysBorrowed > 180) { // 6 tháng = 180 ngày
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi Thời Gian Mượn");
            alert.setHeaderText(null);
            alert.setContentText("Không thể trả sách sau 6 tháng!");
            alert.showAndWait();
            return;
        }

        // Tính số ngày còn lại
        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), returnDateValue);
        String message = daysRemaining >= 0
                ? "Còn " + daysRemaining + " ngày để trả sách."
                : "Quá hạn " + Math.abs(daysRemaining) + " ngày!";

        // Hiển thị thông báo xác nhận
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác Nhận Trả Sách");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Bạn có chắc chắn muốn trả quyển sách '" + selectedBook.getBookName() + "' không?\n" + message);
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Nếu người dùng xác nhận, xóa sách khỏi bảng
                borrowedBooksTable.getItems().remove(selectedBook);
                txtBorrowerName.clear(); // Xóa dữ liệu đã nhập
            }
        });
    }

    @FXML
    private void handleBack() {
        // Trở lại màn hình trước đó, bạn có thể điều chỉnh theo ý muốn
        System.out.println("Back button pressed!");
        // Nếu cần, thêm logic để chuyển đổi giữa các màn hình
    }
}
