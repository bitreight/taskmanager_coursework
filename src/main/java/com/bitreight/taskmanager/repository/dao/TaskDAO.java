package com.bitreight.taskmanager.repository.dao;

import com.bitreight.taskmanager.exceptions.TaskDaoException;
import com.bitreight.taskmanager.model.Task;

import java.util.List;

public interface TaskDao {

    public int createTask(Task task) throws TaskDaoException;
    public void updateTask(Task task) throws TaskDaoException;
    public List<Task> getAllTasks() throws TaskDaoException;
    public List<Task> getIncompletedTasks() throws TaskDaoException;
    public List<Task> getTasksByDeveloper(int developerId) throws TaskDaoException;
    public List<Task> getIncompletedTasksByDeveloper(int developerId) throws TaskDaoException;
    public void deleteTask(int taskId) throws TaskDaoException;
    public void assignTask(int taskId, int developerId) throws TaskDaoException;
    public void deassignTask(int taskId, int developerId) throws TaskDaoException;
    public void setTaskCompletionByUser(int taskId, boolean isCompleted) throws TaskDaoException;

}