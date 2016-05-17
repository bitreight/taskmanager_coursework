package com.bitreight.taskmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

@Component
public class Authorization {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcCall procCheckCredentials;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.procCheckCredentials = new SimpleJdbcCall(dataSource)
                .withProcedureName("checkCredentials");
    }

    public boolean login(String username, String password) {
        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("password", password);
        Map out = procCheckCredentials.execute(in);

        boolean isValid = (Boolean) out.get("is_valid");
        boolean isAdmin = (Boolean) out.get("is_admin");

        return isValid;
    }

}
