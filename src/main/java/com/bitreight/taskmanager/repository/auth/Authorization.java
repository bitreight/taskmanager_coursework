package com.bitreight.taskmanager.repository.auth;

import com.bitreight.taskmanager.exceptions.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Component
public class Authorization {

    private SimpleJdbcCall procCheckCredentials;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.procCheckCredentials = new SimpleJdbcCall(dataSource)
                .withProcedureName("checkCredentials");
    }

    public AuthResult login(String username, String password) throws AuthorizationException {
        Map out = null;

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("password", Security.hashPasswordSHA256(password));

        try {
            out = procCheckCredentials.execute(in);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new AuthorizationException("Ошибка авторизации.");
        }

        boolean isValid =  (out != null ? (Boolean) out.get("is_valid") : false);
        boolean isAdmin =  (out != null ? (Boolean) out.get("is_admin") : false);

        return new AuthResult(isValid, isAdmin);
    }

}
