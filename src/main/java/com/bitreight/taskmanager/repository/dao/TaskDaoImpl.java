package com.bitreight.taskmanager.repository.dao;

import com.bitreight.taskmanager.model.Task;
import javafx.collections.ObservableList;

public class TaskDaoImpl implements TaskDao {

    @Override
    public void addTask(Task task) {

    }

    @Override
    public void updateTask(Task task) {

    }

    @Override
    public ObservableList<Task> getTasksByDeveloper(int developerId) {
        return null;
    }

    @Override
    public void deleteTask(int taskId) {

    }

    @Override
    public void assignTask(int taskId, int developerId) {

    }

    @Override
    public void deassignTask(int taskId, int developerId) {

    }

    @Override
    public void setTaskCompletionByUser(int taskId, int isCompleted) {

    }
}
