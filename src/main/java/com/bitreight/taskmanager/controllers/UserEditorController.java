package com.bitreight.taskmanager.controllers;

import com.bitreight.taskmanager.Validation;
import com.bitreight.taskmanager.exceptions.DeveloperDaoException;
import com.bitreight.taskmanager.model.Developer;
import com.bitreight.taskmanager.repository.dao.DeveloperDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;

public class UserEditorController extends AbstractController {

    @Autowired
    private DeveloperDao developerDao;

    @FXML
    private ListView<Developer> developerListView;
    @FXML
    private CheckBox isAdminCheckbox;
    @FXML
    private TextField developerPositionField;
    @FXML
    private TextField developerPatronymicField;
    @FXML
    private TextField developerSurnameField;
    @FXML
    private TextField developerNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private VBox infoVBox;
    @FXML
    private VBox credentialsVBox;
    @FXML
    private Button infoSaveButton;
    @FXML
    private Button credentialsChangeButton;
    @FXML
    private Button credentialsSaveButton;
    @FXML
    private Button cancelCredentialsChanging;

    private ObservableList<Developer> developersData = FXCollections.observableArrayList();
    private Developer currentDeveloper = null;

    @FXML
    private void selectDeveloperAction() {
        setDefaults();
        currentDeveloper = developerListView.getSelectionModel().getSelectedItem();
        if(currentDeveloper == null) {
            return;
        }
        developerNameField.setText(currentDeveloper.getName());
        developerSurnameField.setText(currentDeveloper.getSurname());
        developerPatronymicField.setText(currentDeveloper.getPatronymic());
        developerPositionField.setText(currentDeveloper.getPosition());
        isAdminCheckbox.setSelected(currentDeveloper.getIsAdmin());
        usernameField.setText(currentDeveloper.getUsername());
    }

    @FXML
    private void developerAddButtonHandler() {
        currentDeveloper = null;

        infoVBox.setDisable(false);
        credentialsVBox.setDisable(false);
        infoSaveButton.setDisable(true);
        credentialsChangeButton.setDisable(true);
        cancelCredentialsChanging.setDisable(true);

        developerNameField.clear();
        developerSurnameField.clear();
        developerPatronymicField.clear();
        developerPositionField.clear();
        isAdminCheckbox.setSelected(false);
        usernameField.clear();
        passwordField.clear();
        developerListView.getSelectionModel().clearSelection();
    }

    @FXML
    private void infoSaveButtonHandler() {
        if(currentDeveloper == null) {
            return;
        }
        else if(!checkDeveloperInfo()) {
            return;
        }

        Developer developer = new Developer();
        developer.setId(currentDeveloper.getId());
        developer.setName(developerNameField.getText());
        developer.setSurname(developerSurnameField.getText());
        developer.setPatronymic(developerPatronymicField.getText());
        developer.setPosition(developerPositionField.getText());
        developer.setIsAdmin(isAdminCheckbox.isSelected());

        try {
            developerDao.updateDeveloper(developer);
            developersData.setAll(developerDao.getDevelopers());
            setDefaults();
        } catch (DeveloperDaoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
        //setDefaults();
    }

    @FXML
    private void credentialsSaveButtonHandler() {
        //create developer
        if(currentDeveloper == null) {
            if(!checkDeveloperInfo() || !checkDeveloperCredentials()) {
                return;
            }

            Developer developer = new Developer();
            developer.setName(developerNameField.getText());
            developer.setSurname(developerSurnameField.getText());
            developer.setPatronymic(developerPatronymicField.getText());
            developer.setPosition(developerPositionField.getText());
            developer.setIsAdmin(isAdminCheckbox.isSelected());

            developer.setUsername(usernameField.getText());
            developer.setPassword(passwordField.getText());

            try {
                developerDao.createDeveloper(developer);
                developersData.setAll(developerDao.getDevelopers());
                setDefaults();
            } catch (DeveloperDaoException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(e.getMessage());
                alert.show();
            }

        //change developer
        } else {
            if(!checkDeveloperCredentials()) {
                return;
            }
            try {
                developerDao.updateCredentials(currentDeveloper.getId(),
                        usernameField.getText(), passwordField.getText());
                developersData.setAll(developerDao.getDevelopers());
                setDefaults();
            } catch (DeveloperDaoException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        }
        //setDefaults();
    }

    @FXML
    private void credentialsChangeButtonHandler() {
        if(currentDeveloper == null) {
            return;
        }
        infoVBox.setDisable(true);
        credentialsVBox.setDisable(false);
    }

    @FXML
    private void cancelCredentialsChangingButtonHandler() {
        infoVBox.setDisable(false);
        credentialsVBox.setDisable(true);

        if(currentDeveloper != null) {
            usernameField.setText(currentDeveloper.getUsername());
        }
        passwordField.clear();
    }

    @FXML
    private void deleteDeveloperHandler() {
        if(currentDeveloper == null) {
            return;
        }
        try {
            developerDao.deleteDeveloper(currentDeveloper.getId());
            developersData.setAll(developerDao.getDevelopers());
            setDefaults();
        } catch (DeveloperDaoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
        //setDefaults();
    }

    @FXML
    private void initialize() {
        developerListView.setItems(developersData);
        loadDevelopersFromDatabase();
    }

    private boolean checkDeveloperInfo() {
        if(!Validation.checkDeveloperName(developerNameField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Внимание");
            alert.setHeaderText("Введите имя разработчика (1..20 символов)");
            alert.show();
            return false;
        } else if(!Validation.checkDeveloperSurname(developerSurnameField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Внимание");
            alert.setHeaderText("Введите фамилию разработчика (1..20 символов)");
            alert.show();
            return false;
        } else if(!Validation.checkDeveloperPatronymic(developerPatronymicField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Внимание");
            alert.setHeaderText("Введите отчество разработчика (1..20 символов)");
            alert.show();
            return false;
        } else if(!Validation.checkDeveloperPosition(developerPositionField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Внимание");
            alert.setHeaderText("Введите должность разработчика (1..50 символов)");
            alert.show();
            return false;
        }
        return true;
    }

    private boolean checkDeveloperCredentials() {
        if(!Validation.checkUsername(usernameField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Внимание");
            alert.setHeaderText("Введите имя пользователя (3..10 символов)");
            alert.show();
            return false;
        } else if (!Validation.checkPassword(passwordField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Внимание");
            alert.setHeaderText("Введите пароль пользователя (3..10 символов)");
            alert.show();
            return false;
        }
        return true;
    }

    private void loadDevelopersFromDatabase() {
        try {
            developersData.addAll(developerDao.getDevelopers());
        } catch (DeveloperDaoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }

//    private void setForAdding() {
//        currentDeveloper = null;
//
//        infoVBox.setDisable(false);
//        credentialsVBox.setDisable(false);
//        infoSaveButton.setDisable(true);
//        credentialsChangeButton.setDisable(true);
//        cancelCredentialsChanging.setDisable(true);
//
//        developerNameField.clear();
//        developerSurnameField.clear();
//        developerPatronymicField.clear();
//        developerPositionField.clear();
//        isAdminCheckbox.setSelected(false);
//        usernameField.clear();
//        passwordField.clear();
//        developerListView.getSelectionModel().clearSelection();
//    }

    private void setDefaults() {
        currentDeveloper = null;

        developerNameField.clear();
        developerSurnameField.clear();
        developerPatronymicField.clear();
        developerPositionField.clear();
        isAdminCheckbox.setSelected(false);

        usernameField.clear();
        passwordField.clear();

        infoVBox.setDisable(false);
        credentialsVBox.setDisable(true);
        infoSaveButton.setDisable(false);
        credentialsSaveButton.setDisable(false);
        credentialsChangeButton.setDisable(false);
        cancelCredentialsChanging.setDisable(false);
    }
}
