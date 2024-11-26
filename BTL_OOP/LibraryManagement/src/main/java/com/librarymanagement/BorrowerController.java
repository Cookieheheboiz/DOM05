package com.librarymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BorrowerController {
    @FXML
    private Button ReturnButton;
    @FXML
    private TableView<Borrower> addtoTable;
    @FXML
    private TableColumn<Borrower, String> addID;
    @FXML
    private TableColumn<Borrower, String> addFullname;
    @FXML
    private TableColumn<Borrower, String> addPhonenumber;
    @FXML
    private TableColumn<Borrower, LocalDate> addDob;
    @FXML
    private TableColumn<Borrower, Integer> addNumofBooks;
    @FXML
    private TableColumn<Borrower, String> addStatus;
    private ObservableList<Borrower> borrowerData = FXCollections.observableArrayList();


    public static class Borrower {
        private String ID;
        private String Fullname;
        private String Phonenumber;
        private LocalDate Dob;
        private int NumofBooks;
        private String Status;

        public Borrower(String ID, String Fullname, String Phonenumber, LocalDate Dob, int NumofBooks, String Status) {
            this.ID = ID;
            this.Fullname = Fullname;
            this.Phonenumber = Phonenumber;
            this.Dob = Dob;
            this.NumofBooks = NumofBooks;
            this.Status = Status;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getFullname() {
            return Fullname;
        }

        public void setFullname(String fullname) {
            this.Fullname = fullname;
        }

        public String getPhonenumber() {
            return Phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
            this.Phonenumber = phonenumber;
        }

        public LocalDate getDob() {
            return Dob;
        }

        public void setDob(LocalDate dob) {
            this.Dob = dob;
        }

        public int getNumofBooks() {
            return NumofBooks;
        }

        public void setNumofBooks(int numofBooks) {
            this.NumofBooks = numofBooks;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            this.Status = status;
        }

    }

    @FXML
    public void initialize() {
        addID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        addFullname.setCellValueFactory(new PropertyValueFactory<>("Fullname"));
        addPhonenumber.setCellValueFactory(new PropertyValueFactory<>("Phonenumber"));
        addDob.setCellValueFactory(new PropertyValueFactory<>("Dob"));
        addNumofBooks.setCellValueFactory(new PropertyValueFactory<>("NumofBooks"));
        addStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        loadBorrowerData();
    }

    public void loadBorrowerData() {
        borrowerData.clear();

        try {
            DatabaseConnection databaseConnector = new DatabaseConnection();
            Connection connection = databaseConnector.getConnection();

            String query = """
                        SELECT u.ID, u.Full_name, u.PhoneNumber, u.DateOfBirth, 
                               COALESCE(b.NumOfBook, 0) AS NumOfBook,
                               CASE WHEN b.ID IS NULL THEN 'Inactive' ELSE 'Active' END AS Status
                        FROM user_id u
                        LEFT JOIN (
                            SELECT ID, COUNT(ID) AS NumOfBook FROM borrowed_books1 GROUP BY ID
                        ) b
                        ON u.ID = b.ID;
                    """;

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while (resultSet.next()) {
                String dobString = resultSet.getString("DateOfBirth");
                LocalDate dob = null;

                try {
                    dob = LocalDate.parse(dobString, formatter); // Adjust format if needed
                } catch (DateTimeParseException e) {
                    System.err.println("Invalid date format for DateOfBirth: " + dobString);
                }

                borrowerData.add(new Borrower(
                        resultSet.getString("ID"),
                        resultSet.getString("Full_name"),
                        resultSet.getString("PhoneNumber"),
                        dob,
                        resultSet.getInt("NumOfBook"),
                        resultSet.getString("Status")
                ));
            }

            addtoTable.setItems(borrowerData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void returnMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/Menu-view.fxml"));
            Stage returnToMenu = new Stage();
            returnToMenu.initStyle(StageStyle.UNDECORATED);
            returnToMenu.setScene(new Scene(root, 900, 900));
            returnToMenu.setTitle("Hello!");
            returnToMenu.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void returnButtonAction() {
        Stage stage = (Stage) ReturnButton.getScene().getWindow();
        stage.close();
        returnMenu();
    }
}
