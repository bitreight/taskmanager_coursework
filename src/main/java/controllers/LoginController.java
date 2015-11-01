package main.java.controllers;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import main.java.ViewLoader;


public class LoginController implements Initializable {
               
    @FXML
    private Label invalidLabel;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
             
    @FXML
    private void loginButtonAction(ActionEvent event) throws IOException {
        if (usernameField.getText().length() < 3 | usernameField.getText().length() > 10) {
            invalidLabel.setText("Логин пользователя должен содержать 3..10 символов");
        }
        else if (passwordField.getText().length() < 3 | passwordField.getText().length() > 10) {
            invalidLabel.setText("Пароль пользователя должен содержать 3..10 символов");
        }
        else if (usernameField.getText().equals("user") && passwordField.getText().equals("user")) {
            ViewLoader.loadUser();
        }
        else if (usernameField.getText().equals("admin") & passwordField.getText().equals("admin")) {
            ViewLoader.loadAdmin();
        }
        else {
            invalidLabel.setText("Логин пользователя или пароль введены неверно");
        }
    }
    
    @FXML
    public void textFieldsClick() {
        invalidLabel.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //
    }        
}
