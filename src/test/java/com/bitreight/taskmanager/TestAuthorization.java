package com.bitreight.taskmanager;

import com.bitreight.taskmanager.repository.auth.AuthResult;
import com.bitreight.taskmanager.repository.auth.Authorization;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import static org.junit.Assert.assertTrue;

@ContextConfiguration("classpath:spring/spring-context.xml")
public class TestAuthorization extends AbstractJUnit4SpringContextTests {

    @Autowired
    private Authorization auth;

    @Test
    public void testAuthDefaultAdmin()  {
        String username = "admin";
        String password = "password";

        AuthResult authResultExpected = auth.login(username, password);

        assertTrue(authResultExpected.isValid);
        assertTrue(authResultExpected.isAdmin);
    }
}
