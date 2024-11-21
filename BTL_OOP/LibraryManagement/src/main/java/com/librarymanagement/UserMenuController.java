package com.librarymanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class UserMenuController {
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




    public void Logout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/hello-view.fxml"));
            Stage logout = new Stage();

            logout.setScene(new Scene(root, 900, 900));
            logout.setTitle("Hello!");
            logout.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void LogoutAction() {
        Stage stage = (Stage) LogOutbutton.getScene().getWindow();
        stage.close();
        Logout();
    }

    public void myAccount() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/MyAccount-view.fxml"));
            Stage myAcc = new Stage();
            myAcc.initStyle(StageStyle.UNDECORATED);
            myAcc.setScene(new Scene(root, 900, 900));
            myAcc.setTitle("Hello!");
            myAcc.show();
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void MyAccountAction() {
        Stage stage = (Stage) UserIn4.getScene().getWindow();
        stage.close();
        myAccount();
    }

    public void BorrowBook() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/librarymanagement/fxml/BorrowedBook-view.fxml"));
            Stage borrowBooks = new Stage();
            borrowBooks.initStyle(StageStyle.UNDECORATED);
            borrowBooks.setScene(new Scene(root, 900, 900));
            borrowBooks.setTitle("Hello!");
            borrowBooks.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void BorrowedBookAction() {
        Stage stage = (Stage) BorrowedBook.getScene().getWindow();
        stage.close();
        BorrowBook();
    }



    public void LibraryStorage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/librarymanagement/fxml/StorageBooks-view.fxml"));
            Parent root = loader.load();

            UserStorageController controller = loader.getController();


            Stage viewBook = new Stage();
            viewBook.initStyle(StageStyle.UNDECORATED);
            viewBook.setScene(new Scene(root, 900, 900));
            viewBook.setTitle("Storage Books");
            viewBook.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void LibraryStorageAction() {
        Stage stage = (Stage) StorageLib.getScene().getWindow();
        stage.close();
        LibraryStorage();
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









}
