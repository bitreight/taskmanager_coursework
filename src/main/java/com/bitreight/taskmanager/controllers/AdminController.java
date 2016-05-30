package com.bitreight.taskmanager.controllers;

import com.bitreight.taskmanager.Validation;
import com.bitreight.taskmanager.exceptions.DeveloperDaoException;
import com.bitreight.taskmanager.exceptions.TaskDaoException;
import com.bitreight.taskmanager.model.Developer;
import com.bitreight.taskmanager.model.Task;
import com.bitreight.taskmanager.repository.dao.DeveloperDao;
import com.bitreight.taskmanager.repository.dao.TaskDao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;

public class AdminController extends AbstractController {

    @Autowired
    private TaskDao taskDao;
    @Autowired
    private DeveloperDao developerDao;

    @FXML
    private ToggleButton showIncompletedButton;

    @FXML
    private CheckBox isCompletedCheckbox;
    @FXML
    private TextField taskNameField;
    @FXML
    private TextField taskNumberField;
    @FXML
    private TextArea taskDescriptionArea;
    @FXML
    private DatePicker deadlinePicker;
    @FXML
    private ChoiceBox<String> priorityChoicebox;

    @FXML
    private Button taskSaveButton;

    @FXML
    private VBox assignDeveloperVBox;
    @FXML
    private ChoiceBox<Developer> taskDeveloperChoicebox;
    @FXML
    private ListView<Developer> taskDevelopersListView;

    @FXML
    private TableView<Task> taskTableView;
    @FXML
    private TableColumn<Task, Integer> numberColumn;
    @FXML
    private TableColumn<Task, String> nameColumn;
    @FXML
    private TableColumn<Task, String> deadlineColumn;
    @FXML
    private TableColumn<Task, String> stateColumn;
    @FXML
    private TableColumn<Task, String> priorityColumn;
    @FXML
    private TableColumn<Task, String> developerColumn;

    private ObservableList<Task> tasksData = FXCollections.observableArrayList();
    private ObservableList<String> prioritiesList = FXCollections.observableArrayList();
    private ObservableList<Developer> developersData = FXCollections.observableArrayList();
    private ObservableList<Developer> taskDevelopersData = FXCollections.observableArrayList();

    private Task currentTask = null;

    @FXML
    private void taskAddButtonHandler() {
        setDefaults();
        taskSaveButton.setDisable(false);
    }

    @FXML
    private void refreshButtonHandler() {
        setDefaults();
        loadTasksFromDatabase();
    }

    @FXML
    private void showIncompletedButtonAction() {
        loadTasksFromDatabase();
    }

    @FXML
    private void taskTableViewHandler() {
        currentTask = taskTableView.getSelectionModel().getSelectedItem();
        if(currentTask == null) {
            return;
        }
        //fill data
        isCompletedCheckbox.setSelected(currentTask.getIsCompleted());
        taskNameField.setText(currentTask.getName());
        taskNumberField.setText(String.valueOf(currentTask.getNumber()));
        deadlinePicker.setValue(currentTask.getDeadline());
        priorityChoicebox.getSelectionModel().select(currentTask.getPriority() - 1);
        taskDescriptionArea.setText(currentTask.getDescription());

        //prepare controls
        taskSaveButton.setDisable(false);
        taskSaveButton.setText("Изменить");
        assignDeveloperVBox.setDisable(false);

        //fill collection of task developers
        taskDevelopersData.setAll(currentTask.getTaskDevelopers());
    }

    @FXML
    private void taskSaveButtonHandler() {
        if(!Validation.checkTaskName(taskNameField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Внимание");
            alert.setHeaderText("Название задачи должно содержать 1..60 символов!");
            alert.show();
            return;
        }
        else if(!Validation.checkTaskNumber(taskNumberField.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Внимание");
            alert.setHeaderText("Номер задачи должен содержать 1..8 цифр!");
            alert.show();
            return;
        }
        else if(!Validation.checkTaskDate(deadlinePicker.getValue())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Внимание");
            alert.setHeaderText("Введите корректную дату!");
            alert.show();
            return;
        }
        else if(!Validation.checkTaskDescription(taskDescriptionArea.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Внимание");
            alert.setHeaderText("Описание задачи может содержать не более 500 символов!");
            alert.show();
            return;
        }

        Task task = new Task();
        task.setIsCompleted(isCompletedCheckbox.isSelected());
        task.setName(taskNameField.getText());
        task.setNumber(Integer.valueOf(taskNumberField.getText()));
        task.setDeadline(deadlinePicker.getValue());
        task.setPriority(priorityChoicebox.getSelectionModel().getSelectedIndex() + 1);
        task.setDescription(taskDescriptionArea.getText());

        try {
            if (currentTask == null) {
                taskDao.createTask(task);
                loadTasksFromDatabase();
            } else {
                task.setId(currentTask.getId());
                taskDao.updateTask(task);
                loadTasksFromDatabase();
            }
            setDefaults();
        } catch (TaskDaoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
        //setDefaults();
    }

    @FXML
    private void taskAssignButtonHandler() {
        Developer developer = taskDeveloperChoicebox.getSelectionModel().getSelectedItem();
        if(currentTask == null || developer == null) {
            return;
        }

        try {
            taskDao.assignTask(currentTask.getId(), developer.getId());
            taskDevelopersData.add(developer);
            loadTasksFromDatabase();
        } catch (TaskDaoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }

    @FXML
    private void taskDeassignButtonHandler() {
        Developer developer = taskDevelopersListView.getSelectionModel().getSelectedItem();
        if(currentTask == null || developer == null) {
            return;
        }

        try {
            taskDao.deassignTask(currentTask.getId(), developer.getId());
            int indexToDelete = taskDevelopersListView.getSelectionModel().getSelectedIndex();
            taskDevelopersData.remove(indexToDelete);
            loadTasksFromDatabase();
        } catch (TaskDaoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }

    @FXML
    public void deleteTaskHandler() {
        if(currentTask == null) {
            return;
        }
        try {
            taskDao.deleteTask(currentTask.getId());
            loadTasksFromDatabase();
            setDefaults();
        } catch (TaskDaoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }

    @FXML
    private void taskDeveloperChoiceboxHandler() {
        try {
            developersData.setAll(developerDao.getDevelopers());
        } catch (DeveloperDaoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }

    @FXML
    private void openUserEditorButtonHandler() {
        try {
            ScreensController.loadUserEditor();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exitButtonHandler() {
        try {
            ScreensController.loadLogin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        currentTask = null;

        //controls clear
        isCompletedCheckbox.setSelected(false);
        taskNameField.clear();
        taskNumberField.clear();
        deadlinePicker.setValue(LocalDate.now());
        priorityChoicebox.getSelectionModel().select(1);
        taskDescriptionArea.clear();

        //assign vbox off
        assignDeveloperVBox.setDisable(true);
        //clear developers collection
        taskDevelopersData.clear();

        //add button setting
        taskSaveButton.setText("Добавить");
        taskSaveButton.setDisable(true);
    }

    private void loadTasksFromDatabase() {

        if(showIncompletedButton.isSelected()) {
            try {
                tasksData.setAll(taskDao.getIncompletedTasks());
            } catch (TaskDaoException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        } else {
            try {
                tasksData.setAll(taskDao.getAllTasks());
            } catch (TaskDaoException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText(e.getMessage());
                alert.show();
            }
        }
    }

    private void loadDevelopersFromDatabase() {
        try {
            developersData.setAll(developerDao.getDevelopers());

        } catch (DeveloperDaoException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(e.getMessage());
            alert.show();
        }
    }

    public void initialize() {
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().getNumberProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().getNameProperty());
        deadlineColumn.setCellValueFactory(cellData -> cellData.getValue().getDeadlineProperty().asString());
        stateColumn.setCellValueFactory(cellData -> cellData.getValue().getIsCompletedProperty());
        priorityColumn.setCellValueFactory(cellData -> cellData.getValue().getPriorityProperty());
        developerColumn.setCellValueFactory(cellData -> cellData.getValue().getTaskDevelopersString());

        taskTableView.setItems(tasksData);
        taskDeveloperChoicebox.setItems(developersData);
        taskDevelopersListView.setItems(taskDevelopersData);

        prioritiesList.setAll("Высокий", "Средний", "Низкий");
        priorityChoicebox.setItems(prioritiesList);
        priorityChoicebox.getSelectionModel().select(1);

        loadTasksFromDatabase();
        loadDevelopersFromDatabase();

        setDefaults();
    }
}
