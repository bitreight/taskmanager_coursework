package com.bitreight.taskmanager.model;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.sql.Date;
import java.time.LocalDate;

public class Task {

    private IntegerProperty id;
    private IntegerProperty number;
    private StringProperty name;
    private StringProperty description;
    private ObjectProperty<LocalDate> deadline;
    private IntegerProperty priority;
    private BooleanProperty isCompleted;
    private ObservableList<Developer> taskDevelopers;

    public Task(int id, int number, String name, String description,
                LocalDate deadline, int priority, boolean isCompleted) {
        setId(id);
        setNumber(number);
        setName(name);
        setDescription(description);
        setDeadline(deadline);
        setPriority(priority);
        setIsCompleted(isCompleted);
    }

    public Task() {}

    public int getId() {
        return id.get();
    }

    public IntegerProperty getIdProperty() {
        return id;
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public int getNumber() {
        return number.get();
    }

    public IntegerProperty getNumberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number = new SimpleIntegerProperty(number);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty getNameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty getDescriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description = new SimpleStringProperty(description);
    }

    public LocalDate getDeadline() {
        return deadline.get();
    }

    public ObjectProperty<LocalDate> getDeadlineProperty() {
        return deadline;
    }

    public void setDeadline(LocalDate date) {
        deadline = new SimpleObjectProperty<>(date);
    }

    public void setDeadline(Date date) {
        deadline = new SimpleObjectProperty<>(date.toLocalDate());
    }

    public int getPriority() {
        return priority.get();
    }

    public IntegerProperty getPriorityProperty() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = new SimpleIntegerProperty(priority);
    }

    public Boolean getIsCompleted() {
        return isCompleted.get();
    }

    public BooleanProperty getIsCompletedProperty() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = new SimpleBooleanProperty(isCompleted);
    }

    public ObservableList<Developer> getTaskDevelopers() {
        return taskDevelopers;
    }

    public void setTaskDevelopers(ObservableList<Developer> taskDevelopers) {
        this.taskDevelopers = taskDevelopers;
    }
}
