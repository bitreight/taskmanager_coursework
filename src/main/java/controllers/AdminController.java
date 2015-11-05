package main.java.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import main.java.Task;


public class AdminController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();
    
    @FXML
    private TableView<Task> taskTable;
    
    @FXML
    private TableColumn<Task, Integer> idColumn;
    
    @FXML
    private TableColumn<Task, String> nameColumn;
    
    @FXML
    private TableColumn<Task, String> descriptionColumn;
    
    @FXML
    private TableColumn<Task, String> termColumn;
    
    @FXML
    private TableColumn<Task, Boolean> stateColumn;
     
    @FXML
    private TextArea descriptionArea;

    @FXML
    private Button taskSaveButton;

    @FXML
    private TextField taskNameField;

    @FXML
    private TextField taskNumberField;

    @FXML
    private DatePicker taskTermPicker;

    @FXML
    private TextArea taskDescriptionArea;

    @FXML
    private void taskAddButtonAction() {
        taskSaveButton.setDisable(false);
        taskNameField.clear();
        taskNameField.clear();
        taskNumberField.clear();
        taskTermPicker.setValue(null);
        taskDescriptionArea.clear();
    }

    @FXML
    private void taskSaveButtonAction() {
        //System.out.println(taskTermPicker.getValue().toString());
        //while(true) {
            if (taskNameField.getText().equals("") | taskNumberField.getText().equals("") | taskTermPicker.getValue() == null) {
                System.out.println("Поля «Название задачи», «Номер» и «Срок» являются обязательными для заполнения");
            }
            else if (taskNameField.getText().length() > 60) {
                System.out.println("Название задачи должно содержать 1..60 символов");
            }
            else if (taskNumberField.getText().length() > 8) {
                System.out.println("Номер задачи должен содержать 1..8 цифр");
            }
            else if (taskDescriptionArea.getText().length() > 500) {
                System.out.println("Описание задачи может содержать не более 500 символов");
            }
            else {
                taskSaveButton.setDisable(true);
            }
    }

    @FXML
    private void tasksTableAction() {
        
        Task task = taskTable.getSelectionModel().getSelectedItem();
        if (task == null)
            return;
        
        String description = "Задача №" + task.getId() + ". " + task.getName() +
                "\nОписание: " + task.getDescription();
        descriptionArea.setText(description);        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        taskSaveButton.setDisable(true);

        idColumn.setCellValueFactory(new PropertyValueFactory<Task, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("description"));
        termColumn.setCellValueFactory(new PropertyValueFactory<Task, String>("term"));        
        stateColumn.setCellValueFactory(new PropertyValueFactory("state"));      
       
        initTasks();
        
        taskTable.setItems(tasks);         
        
        taskTable.setRowFactory(new Callback<TableView<Task>, TableRow<Task>>() {
            @Override
            public TableRow<Task> call(TableView<Task> tableView) {
            final TableRow<Task> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();            
            MenuItem editItem = new MenuItem("Изменить статус");
            editItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {   
                    if (row.getItem().getState() == "") {                        
                        row.getItem().setState(true);
                    }
                    else {                        
                        row.getItem().setState(false);
                    }
                    taskTable.getItems().set(row.getIndex(), row.getItem());
              }
            });
            //String taskState = row.getItem().getState();
            rowMenu.getItems().addAll(editItem);

            // only display context menu for non-null items:
            row.contextMenuProperty().bind(
              Bindings.when(Bindings.isNotNull(row.itemProperty()))
              .then(rowMenu)
              .otherwise((ContextMenu)null));
                        
            return row;
          }           
        });     
              
    }     
        
    private void initTasks() {  //Test
        tasks.add(new Task(1, "Задача 1", "Описание задачи 1", "20.10.15", false));
        tasks.add(new Task(2, "Еще задача", "Отличное описание", "10.09.15", true));
        tasks.add(new Task(3, "Сделать курсач", "УЖАС", "10.12.15", false));
    }
    
}
