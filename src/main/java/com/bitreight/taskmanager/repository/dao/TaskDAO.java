package com.bitreight.taskmanager.repository.dao;

import com.bitreight.taskmanager.model.Task;

import java.util.List;

public interface TaskDao {

    public int createTask(Task task);
    public void updateTask(Task task);
    public List<Task> getAllTasks();
    public List<Task> getIncompletedTasks();
    public List<Task> getTasksByDeveloper(int developerId);
    public List<Task> getIncompletedTasksByDeveloper(int developerId);
    public void deleteTask(int taskId);
    public void assignTask(int taskId, int developerId);
    public void deassignTask(int taskId, int developerId);
    public void setTaskCompletionByUser(int taskId, boolean isCompleted);

}