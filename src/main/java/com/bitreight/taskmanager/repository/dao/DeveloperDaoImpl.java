package com.bitreight.taskmanager.repository.dao;

import com.bitreight.taskmanager.exceptions.DeveloperDaoException;
import com.bitreight.taskmanager.model.Developer;
import com.bitreight.taskmanager.repository.auth.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.UncategorizedSQLException;
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
import java.util.List;
import java.util.Map;

@Component
public class DeveloperDaoImpl implements DeveloperDao {

    private SimpleJdbcCall procCreateDeveloper;
    private SimpleJdbcCall procUpdateDeveloper;
    private SimpleJdbcCall procUpdateCredentials;
    private SimpleJdbcCall procDeleteDeveloper;

    private JdbcTemplate storedFunctionCall;
    private final String getDevelopers = "Select * From getDevelopers()";

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.procCreateDeveloper = new SimpleJdbcCall(dataSource)
                .withProcedureName("createDeveloper");
        this.procUpdateDeveloper = new SimpleJdbcCall(dataSource)
                .withProcedureName("updateDeveloper");
        this.procUpdateCredentials = new SimpleJdbcCall(dataSource)
                .withProcedureName("updateCredentials");
        this.procDeleteDeveloper = new SimpleJdbcCall(dataSource)
                .withProcedureName("deleteDeveloper");

        this.storedFunctionCall = new JdbcTemplate(dataSource);
    }

    @Override
    public int createDeveloper(Developer developer) throws DeveloperDaoException {
        Map out = null;

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("username", developer.getUsername())
                .addValue("password", Security.hashPasswordSHA256(developer.getPassword()))
                .addValue("dev_name", developer.getName())
                .addValue("surname", developer.getSurname())
                .addValue("patronymic", developer.getPatronymic())
                .addValue("position", developer.getPosition())
                .addValue("is_admin", developer.getIsAdmin());

        try {
            out = procCreateDeveloper.execute(in);
        } catch (UncategorizedSQLException e) {
            throw new DeveloperDaoException(e.getSQLException().getMessage());
        } catch (Exception e) {
            e.getMessage();
        }
        return (out != null ? (int) out.get("identity") : 0);
    }

    @Override
    public List<Developer> getDevelopers() {
        List<Developer> developers = new ArrayList<Developer>();

        try {
            developers = storedFunctionCall.query(getDevelopers, new DeveloperMapper());
        } catch (Exception e) {
            e.getMessage();
        }
        return developers;
    }

    @Override
    public boolean updateDeveloper(Developer developer) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("dev_id", developer.getId())
                .addValue("dev_name", developer.getName())
                .addValue("surname", developer.getSurname())
                .addValue("patronymic", developer.getPatronymic())
                .addValue("position", developer.getPosition())
                .addValue("is_admin", developer.getIsAdmin());

        try {
            procUpdateDeveloper.execute(in);
            return true;
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }

    @Override
    public boolean updateCredentials(int developerId, String username, String password) throws DeveloperDaoException {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("dev_id", developerId)
                .addValue("username", username)
                .addValue("password", Security.hashPasswordSHA256(password));

        try {
            procUpdateCredentials.execute(in);
            return true;
        } catch(UncategorizedSQLException e) {
            throw new DeveloperDaoException(e.getSQLException().getMessage());
        } catch (Exception e) {
            e.getMessage();
            return false;
        }
    }

    @Override
    public boolean deleteDeveloper(int developerId) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("dev_id", developerId);

        try {
            procDeleteDeveloper.execute(in);
            return true;
        }
        catch (Exception e) {
            e.getMessage();
            return false;
        }
    }

    private class DeveloperMapper implements RowMapper<Developer> {

        @Override
        public Developer mapRow(ResultSet rs, int i) throws SQLException {
            Developer developer = new Developer();
            developer.setId(rs.getInt("id"));
            developer.setUsername(rs.getString("username"));
            developer.setName(rs.getString("dev_name"));
            developer.setSurname(rs.getString("surname"));
            developer.setPatronymic(rs.getString("patronymic"));
            developer.setPosition(rs.getString("position"));
            developer.setIsAdmin(rs.getBoolean("is_admin"));

            return developer;
        }
    }
}
