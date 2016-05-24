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

    private JdbcTemplate storedFunctionCall;

    private SimpleJdbcCall procCreateTask;
    private SimpleJdbcCall procUpdateTask;
    private SimpleJdbcCall procDeleteTask;
    private SimpleJdbcCall procAssignTask;
    private SimpleJdbcCall procDeassignTask;
    private SimpleJdbcCall procSetTaskCompletionByUser;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.procCreateTask = new SimpleJdbcCall(dataSource)
                .withProcedureName("addTask");

        this.procUpdateTask = new SimpleJdbcCall(dataSource)
                .withProcedureName("updateTask");

        this.storedFunctionCall = new JdbcTemplate(dataSource);

        this.procDeleteTask = new SimpleJdbcCall(dataSource)
                .withProcedureName("deleteTask");

        this.procAssignTask = new SimpleJdbcCall(dataSource)
                .withProcedureName("assignTask");

        this.procDeassignTask = new SimpleJdbcCall(dataSource)
                .withProcedureName("deassignTask");

        this.procSetTaskCompletionByUser = new SimpleJdbcCall(dataSource)
                .withProcedureName("setCompletionByUser");
    }

    @Override
    public void createTask(Task task) {

    }

    @Override
    public void updateTask(Task task) {

    }

    @Override
    public List<Task> getTasks() {
        return null;
    }

    @Override
    public List<Task> getTasksByDeveloper(int developerId) {
        Map out = null;

        final String funcQuery = "Select * From getTasksByDeveloper(?)";

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("dev_id", developerId);

        return storedFunctionCall.query(funcQuery, new Object[] { developerId }, new TaskMapper());
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
