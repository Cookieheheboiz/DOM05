package com.librarymanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserController {
    @FXML
    private Button ReturnButton;
    @FXML
    private TableView<Employee> AddtoTable;
    @FXML
    private TableColumn<Employee, String> AddID;
    @FXML
    private TableColumn<Employee, String> AddName;
    @FXML
    private TableColumn<Employee, String> AddRole;
    @FXML
    private TableColumn<Employee, String> AddUsername;
    @FXML
    private TableColumn<Employee, String> AddPassword;

    private ObservableList<Employee> employeeData = FXCollections.observableArrayList();

    public static class Employee {
        private String ID;
        private String Name;
        private String Role;
        private String Username;
        private String Password;

        public Employee(String ID, String name, String Role, String username, String password) {
            this.ID = ID;
            this.Name = name;
            this.Role = Role;
            this.Username = username;
            this.Password = password;
        }

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            this.Name = name;
        }

        public String getRole() {
            return Role;
        }

        public void setRole(String Role) {
            this.Role = Role;
        }

        public String getUsername() {
            return Username;
        }

        public void setUsername(String username) {
            this.Username = username;
        }

        public String getPassword() {
            return Password;
        }

        public void setPassword(String password) {
            this.Password = password;
        }
    }

    public void loadEmployeeData() {
        // Clear the current table view
        AddtoTable.getItems().clear();

        DatabaseConnection databaseConnector = new DatabaseConnection();
        Connection connection = databaseConnector.getConnection();

        // Adjusted query to concatenate first and last name
        String query = "SELECT ID, CONCAT(First_name, ' ', Last_name) AS Full_name, MyRole, Username, Password FROM user_id";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            // Create a list of Employee objects to store the data
            ObservableList<Employee> employees = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String id = resultSet.getString("ID");
                String fullname = resultSet.getString("Full_name");
                String role = resultSet.getString("MyRole");
                String username = resultSet.getString("Username");
                String pass = resultSet.getString("Password");

                employees.add(new Employee(id, fullname, role, username, pass));
            }

            // Close the resources
            resultSet.close();
            preparedStatement.close();
            connection.close();

            // Set the items for the table view
            AddtoTable.setItems(employees);

            // Map the table columns to the Employee class properties
            AddID.setCellValueFactory(new PropertyValueFactory<>("ID"));
            AddName.setCellValueFactory(new PropertyValueFactory<>("Name"));
            AddRole.setCellValueFactory(new PropertyValueFactory<>("Role"));
            AddUsername.setCellValueFactory(new PropertyValueFactory<>("Username"));
            AddPassword.setCellValueFactory(new PropertyValueFactory<>("Password"));

        } catch (Exception e) {
            e.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Error loading employees.");
            errorAlert.showAndWait();
        }
    }


    public void initialize() {
        // Set up the columns
        AddID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        AddName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        AddRole.setCellValueFactory(new PropertyValueFactory<>("MyRole"));
        AddUsername.setCellValueFactory(new PropertyValueFactory<>("Username"));
        AddPassword.setCellValueFactory(new PropertyValueFactory<>("Password"));

        // Load data into the table
        loadEmployeeData();
    }

    public void ReturnMenu() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/Menu-view.fxml"));
            Stage re = new Stage();
            re.initStyle(StageStyle.UNDECORATED);
            re.setScene(new Scene(root, 900, 900));
            re.setTitle("Hello!");
            re.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void ReturnAction() {
        Stage stage = (Stage) ReturnButton.getScene().getWindow();
        stage.close();
        ReturnMenu();
    }

}
