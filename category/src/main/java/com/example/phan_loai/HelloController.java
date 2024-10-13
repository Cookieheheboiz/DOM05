package com.example.phan_loai;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Button exit;


    @FXML
    private Button entertain;

    @FXML
    private Button academic;
    // FXML reference to the ListView in the FXML file
    @FXML
    private ListView<String> categoryList;

    // FXML reference to the Label in the FXML file
    @FXML
    private ListView<String> category2;

    // List of categories
    String[] book = {"Truyện tranh", "Tiểu thuyết", "Sách phiêu lưu", "Sách giả tưởng","Sách hài hước"};
    String curr;

    String[] book2 = {"Sách giáo khoa", "Sách tham khảo", "Giáo trình", "Sách khoa học","Sách học thuật nâng cao"};


    public void initialize(URL a, ResourceBundle b) {

        categoryList.getItems().addAll(book2); //hoc thuat
        categoryList.setVisible(false);

        categoryList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                curr = categoryList.getSelectionModel().getSelectedItem();

            }
        });

        category2.getItems().addAll(book); //giai tri
        category2.setVisible(false);

        category2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                curr = category2.getSelectionModel().getSelectedItem();

            }
        });
        academic.setOnAction(event -> {
            category2.setVisible(false);
            categoryList.setVisible(true);
        });

        entertain.setOnAction(event -> {
            category2.setVisible(true);
            categoryList.setVisible(true);
        });
    }
    @FXML
    public void exitButton(javafx.event.ActionEvent event) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }
}
