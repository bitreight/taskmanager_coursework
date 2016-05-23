package com.bitreight.taskmanager.repository.dao;

import com.bitreight.taskmanager.model.Task;

import java.util.List;

public interface TaskDao {

    public void createTask(Task task);
    public void updateTask(Task task);
    public List<Task> getTasks();
    public List<Task> getTasksByDeveloper(int developerId);
    public void deleteTask(int taskId);
    public void assignTask(int taskId, int developerId);
    public void deassignTask(int taskId, int developerId);
    public void setTaskCompletionByUser(int taskId, int isCompleted);

}