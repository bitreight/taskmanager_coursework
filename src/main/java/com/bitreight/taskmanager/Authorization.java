package com.bitreight.taskmanager;

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

    public AuthResult login(String username, String password)  {
        Map out = null;

        SqlParameterSource in = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("password", hashPasswordSHA256(password));

        try {
            out = procCheckCredentials.execute(in);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        boolean isValid =  (out != null ? (Boolean) out.get("is_valid") : false);
        boolean isAdmin =  (out != null ? (Boolean) out.get("is_admin") : false);

        return new AuthResult(isValid, isAdmin);
    }

    private byte[] hashPasswordSHA256(String password)  {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            if (md != null) {
                md.update(password.getBytes("UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return md != null ? md.digest() : new byte[0];
    }
}
