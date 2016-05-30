package com.bitreight.taskmanager.controllers;

import com.bitreight.taskmanager.Validation;
import com.bitreight.taskmanager.exceptions.AuthorizationException;
import com.bitreight.taskmanager.repository.auth.AuthResult;
import com.bitreight.taskmanager.repository.auth.Authorization;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

public class LoginController extends AbstractController {

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
        if(!Validation.checkUsername(username)) {
            invalidLabel.setText("Логин пользователя должен содержать 3..10 символов");
        }
        else if (!Validation.checkPassword(password)) {
            invalidLabel.setText("Пароль пользователя должен содержать 3..10 символов");
        }
        else {
            try {
                AuthResult authResult = auth.login(username, password);
                if(authResult.isValid) {
                    if(authResult.isAdmin) {
                        ScreensController.loadAdmin();
                    }
                    else {
                        ScreensController.loadUser();
                    }
                }
                else {
                    invalidLabel.setText("Логин пользователя или пароль введены неверно");
                }
            } catch (AuthorizationException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(e.getMessage());
            }
        }
    }

    @FXML
    public void textFieldsClick() {
        invalidLabel.setText("");
    }

}
