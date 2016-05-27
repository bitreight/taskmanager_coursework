package com.bitreight.taskmanager.model;

import javafx.beans.property.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task {

    private int id;
    private int number;
    private String name;
    private String description;
    private ObjectProperty<LocalDate> deadline;
    private int priority;
    private boolean isCompleted;
    //private StringProperty taskDevelopers;
    private List<Developer> taskDevelopers = new ArrayList<Developer>();

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
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public IntegerProperty getNumberProperty() {
        return new SimpleIntegerProperty(number);
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDeadline() {
        return deadline.get();
    }

    public Date getDeadlineSql() {
        return Date.valueOf(deadline.get());
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
        return priority;
    }

    public StringProperty getPriorityProperty() {
        switch(priority) {
            case 1: return new SimpleStringProperty("Высокий");
            case 2: return new SimpleStringProperty("Средний");
            case 3: return new SimpleStringProperty("Низкий");
            default: return new SimpleStringProperty("");
        }
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public StringProperty getIsCompletedProperty() {
        return isCompleted ? new SimpleStringProperty("Выполнена") :
                new SimpleStringProperty("");
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public void setTaskDevelopers(List<Developer> taskDevelopers) {
        this.taskDevelopers = taskDevelopers;
    }

    public List<Developer> getTaskDevelopers() {
        return taskDevelopers;
    }
}
