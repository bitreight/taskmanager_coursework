package com.bitreight.taskmanager.model;

import javafx.beans.property.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Task {

    private int id;
    private int number;
    private String name = "";
    private String description = "";
    private ObjectProperty<LocalDate> deadline;
    private int priority;
    private boolean isCompleted;
    private List<Developer> taskDevelopers = new ArrayList<Developer>();

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
        return deadline != null ? deadline.get() : null;
    }

    public Date getDeadlineSql() {
        return deadline != null ? Date.valueOf(deadline.get()) : null;
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

    public void addDeveloper(Developer developer) {
        taskDevelopers.add(developer);
    }

    public List<Developer> getTaskDevelopers() {
        return taskDevelopers;
    }

    public StringProperty getTaskDevelopersString() {
        if(taskDevelopers.isEmpty()) {
            return new SimpleStringProperty("Нет");
        }

        StringBuilder developers = new StringBuilder();
        for(Developer dev : taskDevelopers) {
            if(developers.length() != 0) {
                developers.append(", ");
            }
            developers.append(dev.toString());
        }
        return new SimpleStringProperty(developers.toString());
    }
}