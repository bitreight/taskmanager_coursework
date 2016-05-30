package com.bitreight.taskmanager.repository.dao;

import com.bitreight.taskmanager.exceptions.TaskDaoException;
import com.bitreight.taskmanager.model.Developer;
import com.bitreight.taskmanager.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
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
    private final String getAllTasks = "Select * From getAllTasks()";
    private final String getIncompletedTasks = "Select * From getIncompletedTasks()";
    private final String getTasksByDeveloper = "Select * From getTasksByDeveloper(?)";
    private final String getIncompletedTasksByDeveloper = "Select * From getIncompletedTasksByDeveloper(?)";

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
                .withProcedureName("setTaskCompletionByUser");

        this.storedFunctionCall = new JdbcTemplate(dataSource);
    }

    @Override
    public int createTask(Task task) throws TaskDaoException {
        Map out = null;

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("number", task.getNumber())
                .addValue("task_name", task.getName())
                .addValue("description", task.getDescription())
                .addValue("deadline", task.getDeadlineSql())
                .addValue("priority", task.getPriority())
                .addValue("is_completed", task.getIsCompleted());

        try {
            out = procCreateTask.execute(in);
        } catch (UncategorizedSQLException e) {
            throw new TaskDaoException(e.getSQLException().getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new TaskDaoException("Произошла ошибка при добавлении задачи.");
        }
        return (out != null ? (int) out.get("identity") : 0);
    }

    @Override
    public void updateTask(Task task) throws TaskDaoException {
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
        } catch (UncategorizedSQLException e) {
            throw new TaskDaoException(e.getSQLException().getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new TaskDaoException("Произошла ошибка при сохранении данных о задаче.");
        }
    }

    @Override
    public List<Task> getAllTasks() throws TaskDaoException {
        List<Task> tasks;

        try {
            tasks = storedFunctionCall.query(getAllTasks, new TaskSetExtractor());
        } catch (Exception e) {
            e.printStackTrace();
            throw new TaskDaoException("Произошла ошибка при получении данных о задачах.");
        }
        return tasks != null ? tasks : new ArrayList<Task>();
    }

    @Override
    public List<Task> getIncompletedTasks() throws TaskDaoException {
        List<Task> tasks;

        try {
            tasks = storedFunctionCall.query(getIncompletedTasks, new TaskSetExtractor());
        } catch (Exception e) {
            e.printStackTrace();
            throw new TaskDaoException("Произошла ошибка при получении данных о задачах.");
        }
        return tasks != null ? tasks : new ArrayList<Task>();
    }

    @Override
    public List<Task> getTasksByDeveloper(int developerId) throws TaskDaoException {
        List<Task> tasks;

        try {
            tasks = storedFunctionCall.
                    query(getTasksByDeveloper, new Object[] { developerId }, new TaskSetExtractor());
        } catch (Exception e) {
            e.printStackTrace();
            throw new TaskDaoException("Произошла ошибка при получении данных о задачах.");
        }
        return tasks != null ? tasks : new ArrayList<Task>();
    }

    @Override
    public List<Task> getIncompletedTasksByDeveloper(int developerId) throws TaskDaoException {
        List<Task> tasks;

        try {
            tasks = storedFunctionCall.
                    query(getIncompletedTasksByDeveloper, new Object[] { developerId }, new TaskSetExtractor());
        } catch (Exception e) {
            e.printStackTrace();
            throw new TaskDaoException("Произошла ошибка при получении данных о задачах.");
        }
        return tasks != null ? tasks : new ArrayList<Task>();
    }

    @Override
    public void deleteTask(int taskId) throws TaskDaoException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("task_id", taskId);

        try {
            procDeleteTask.execute(in);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TaskDaoException("Произошла ошибка при удалении задачи.");
        }
    }

    @Override
    public void assignTask(int taskId, int developerId) throws TaskDaoException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("task_id", taskId)
                .addValue("dev_id", developerId);

        try {
            procAssignTask.execute(in);
        } catch (UncategorizedSQLException e) {
            throw new TaskDaoException(e.getSQLException().getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new TaskDaoException("Произошла ошибка при назначении задачи разработчику.");
        }
    }

    @Override
    public void deassignTask(int taskId, int developerId) throws TaskDaoException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("task_id", taskId)
                .addValue("dev_id", developerId);

        try {
            procDeassignTask.execute(in);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TaskDaoException("Произошла ошибка отмене назначения задачи разработчику.");
        }
    }

    @Override
    public void setTaskCompletionByUser(int taskId, boolean isCompleted) throws TaskDaoException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("task_id", taskId)
                .addValue("is_completed", isCompleted);

        try {
            procSetTaskCompletionByUser.execute(in);
        } catch (Exception e) {
            e.printStackTrace();
            throw new TaskDaoException("Произошла ошибка при отметке о выполнении.");
        }
    }

    private class TaskSetExtractor implements ResultSetExtractor<List<Task>> {

        @Override
        public List<Task> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Map<Integer, Task> map = new HashMap<Integer, Task>();
            Task task;
            while (rs.next()) {
                Integer id = rs.getInt("id");
                task = map.get(id);
                if (task == null) {
                    task = new Task();
                    task.setId(id);
                    task.setNumber(rs.getInt("number"));
                    task.setName(rs.getString("task_name"));
                    task.setDescription(rs.getString("description"));
                    task.setDescription(rs.getString("description"));
                    task.setDeadline(rs.getDate("deadline"));
                    task.setPriority(rs.getInt("priority"));
                    task.setIsCompleted(rs.getBoolean("is_completed"));
                    map.put(id, task);
                }
                Integer developerId = rs.getInt("dev_id");
                if (developerId != 0) {
                    Developer developer = new Developer();
                    developer.setId(developerId);
                    developer.setName(rs.getString("dev_name"));
                    developer.setSurname(rs.getString("surname"));
                    developer.setPatronymic(rs.getString("patronymic"));
                    task.addDeveloper(developer);
                }
            }
            return new ArrayList<Task>(map.values());
        }
    }

//    private class TaskMapper implements RowMapper {
//
//        @Override
//        public Task mapRow(ResultSet rs, int i) throws SQLException {
//            Task task = new Task();
//            task.setId(rs.getInt("id"));
//            task.setNumber(rs.getInt("number"));
//            task.setName(rs.getString("task_name"));
//            task.setDescription(rs.getString("description"));
//            task.setDescription(rs.getString("description"));
//            task.setDeadline(rs.getDate("deadline"));
//            task.setPriority(rs.getInt("priority"));
//            task.setIsCompleted(rs.getBoolean("is_completed"));
//            //task.setTaskDevelopers(rs.getString("dev_name"));
//
//            return task;
//        }
//    }
}
