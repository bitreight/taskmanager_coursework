package com.bitreight.taskmanager.controllers;

import com.bitreight.taskmanager.Authorization;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class LoginController {

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
            //auth.login(username, password);
        }
    }

    @FXML
    public void textFieldsClick() {
        invalidLabel.setText("");
    }

    private void loadAdmin() throws IOException {
        final String pathToAdminScreen = "/fxml/Admin.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(pathToAdminScreen));
        Scene scene = new Scene(root);
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private void loadUser() throws IOException {
        final String pathToUserScreen = "/fxml/User.fxml";
        Parent root = FXMLLoader.load(getClass().getResource(pathToUserScreen));
        Scene scene = new Scene(root);
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
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
