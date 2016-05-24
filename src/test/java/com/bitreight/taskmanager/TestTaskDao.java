package com.bitreight.taskmanager;

import com.bitreight.taskmanager.model.Task;
import com.bitreight.taskmanager.repository.dao.TaskDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import java.util.List;

@ContextConfiguration("classpath:spring/spring-context.xml")
public class TestTaskDao extends AbstractJUnit4SpringContextTests {

    @Autowired
    private TaskDao taskDao;

    @Test
    public void testCreateTask() {

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
        List<Task> tasks = taskDao.getTasks();
        for(Task task : tasks) {
            System.out.println(task.getId());
            System.out.println(task.getDeadline().toString());
        }
    }
}
