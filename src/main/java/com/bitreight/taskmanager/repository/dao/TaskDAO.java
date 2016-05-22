package com.bitreight.taskmanager.repository.dao;

import com.bitreight.taskmanager.model.Task;
import javafx.collections.ObservableList;

public interface TaskDao {

    public void addTask(Task task);
    public void updateTask(Task task);
    public ObservableList<Task> getTasksByDeveloper(int developerId);
    public void deleteTask(int taskId);
    public void assignTask(int taskId, int developerId);
    public void deassignTask(int taskId, int developerId);
    public void setTaskCompletionByUser(int taskId, int isCompleted);

}