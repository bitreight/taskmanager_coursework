package com.bitreight.taskmanager;

import com.bitreight.taskmanager.model.Developer;
import com.bitreight.taskmanager.model.Task;
import com.bitreight.taskmanager.repository.dao.TaskDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.time.LocalDate;
import java.util.List;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@ContextConfiguration("classpath:spring/spring-context.xml")
public class TestTaskDao extends AbstractJUnit4SpringContextTests {

    @Autowired
    private TaskDao taskDao;

    @Test
    public void testCreateTask() {
        Task task = new Task();
        task.setNumber(110);
        task.setName("testtask");
        task.setDescription("test task");
        task.setDeadline(LocalDate.of(2016, 5, 30));
        task.setPriority(1);
        task.setIsCompleted(false);
        int id = taskDao.createTask(task);
        System.out.println(id);
        assertNotEquals(id, 0);
    }

    @Test
    public void testUpdateTask() {
        Task task = new Task();
        task.setId(3);
        task.setNumber(110);
        task.setName("testtask");
        task.setDescription("test task updated");
        task.setDeadline(LocalDate.of(2016, 5, 30));
        task.setPriority(3);
        task.setIsCompleted(true);
        taskDao.updateTask(task);
    }

    @Test
    public void testDeleteTask() {
        taskDao.deleteTask(3);
    }

    @Test
    public void testAssignTask() {
        taskDao.assignTask(1, 1);
    }

    @Test
    public void testDeassignTask() {
        taskDao.deassignTask(1, 1);
    }

    @Test
    public void testGetAllTasks() {
        List<Task> tasks = taskDao.getAllTasks();
        for(Task task : tasks) {
            System.out.println(task.getId() + " " + task.getName());
            System.out.println("Developers:");
            System.out.println(task.getTaskDevelopersString().get());
        }
    }

    @Test
    public void testSetTaskCompletionByUser() {
        taskDao.setTaskCompletionByUser(1, true);
    }

    @Test
    public void testGetTasksByDeveloper() {

        List<Task> tasks = taskDao.getTasksByDeveloper(2);
        for(Task task : tasks) {
            System.out.println(task.getId());
            System.out.println(task.getDeadline().toString());
        }
    }

    @Test
    public void testGetTasks() {
        List<Task> tasks = taskDao.getAllTasks();
        for(Task task : tasks) {
            System.out.println(task.getId());
            System.out.println(task.getDeadline().toString());
        }
    }
}
