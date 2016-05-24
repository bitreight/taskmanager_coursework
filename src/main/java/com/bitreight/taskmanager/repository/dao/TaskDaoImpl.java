package com.bitreight.taskmanager.repository.dao;

import com.bitreight.taskmanager.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TaskDaoImpl implements TaskDao {

    private SimpleJdbcCall procCreateTask;
    private SimpleJdbcCall procUpdateTask;
    private SimpleJdbcCall procDeleteTask;
    private SimpleJdbcCall procAssignTask;
    private SimpleJdbcCall procDeassignTask;
    private SimpleJdbcCall procSetTaskCompletionByUser;
    private JdbcTemplate storedFunctionCall;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.procCreateTask = new SimpleJdbcCall(dataSource)
                .withProcedureName("createTask");

        this.procUpdateTask = new SimpleJdbcCall(dataSource)
                .withProcedureName("updateTask");

        this.procDeleteTask = new SimpleJdbcCall(dataSource)
                .withProcedureName("deleteTask");

        this.procAssignTask = new SimpleJdbcCall(dataSource)
                .withProcedureName("assignTask");

        this.procDeassignTask = new SimpleJdbcCall(dataSource)
                .withProcedureName("deassignTask");

        this.procSetTaskCompletionByUser = new SimpleJdbcCall(dataSource)
                .withProcedureName("setCompletionByUser");

        this.storedFunctionCall = new JdbcTemplate(dataSource);
    }

    @Override
    public void createTask(Task task) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("number", task.getNumber())
                .addValue("task_name", task.getName())
                .addValue("description", task.getDescription())
                .addValue("deadline", task.getDeadlineSql())
                .addValue("priority", task.getPriority())
                .addValue("is_completed", task.getIsCompleted());

        try {
            procCreateTask.execute(in);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTask(Task task) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("task_id", task.getId())
                .addValue("number", task.getNumber())
                .addValue("task_name", task.getName())
                .addValue("description", task.getDescription())
                .addValue("deadline", task.getDeadlineSql())
                .addValue("priority", task.getPriority())
                .addValue("is_completed", task.getIsCompleted());

        try {
            procUpdateTask.execute(in);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Task> getTasks() {
        List<Task> tasks = null;
        final String funcQuery = "Select * From getTasks()";

        try {
            tasks = storedFunctionCall.query(funcQuery, new TaskMapper());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
    }

    @Override
    public List<Task> getTasksByDeveloper(int developerId) {
        List<Task> tasks = null;
        final String funcQuery = "Select * From getTasksByDeveloper(?)";

        try {
            tasks = storedFunctionCall
                    .query(funcQuery, new Object[] { developerId }, new TaskMapper());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tasks;
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

    private class TaskMapper implements RowMapper {

        @Override
        public Task mapRow(ResultSet rs, int i) throws SQLException {
            Task task = new Task();
            task.setId(rs.getInt("id"));
            task.setNumber(rs.getInt("number"));
            task.setName(rs.getString("task_name"));
            task.setDescription(rs.getString("description"));
            task.setDescription(rs.getString("description"));
            task.setDeadline(rs.getDate("deadline"));
            task.setPriority(rs.getInt("priority"));
            task.setIsCompleted(rs.getBoolean("is_completed"));
            task.setTaskDevelopers(rs.getString("dev_name"));

            return task;
        }
    }
}
