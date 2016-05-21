package com.bitreight.taskmanager.controllers;

import com.bitreight.taskmanager.Authorization;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class LoginController {

    @Autowired
    private Authorization auth;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label invalidLabel;

    @FXML
    private void loginButtonAction() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();
        if(!checkUsername(username)) {
            invalidLabel.setText("Логин пользователя должен содержать 3..10 символов");
        }
        else if (!checkPassword(password)) {
            invalidLabel.setText("Пароль пользователя должен содержать 3..10 символов");
        }
        else {
            //Authorization auth = new Authorization();
            if(auth.login(username, password).isValid) {
                ScreensController.loadAdmin();
            }
        }
    }

    @FXML
    public void textFieldsClick() {
        invalidLabel.setText("");
    }

    //testable
    private boolean checkUsername(String username) {
        return !(username.length() < 3 | username.length() > 10);
    }

    //testable
    private boolean checkPassword(String password) {
        return !(password.length() < 3 | password.length() > 10);
    }
}
