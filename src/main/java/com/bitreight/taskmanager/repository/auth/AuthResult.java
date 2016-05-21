package com.bitreight.taskmanager.repository.auth;

public class AuthResult {
    public final boolean isValid;
    public final boolean isAdmin;

    public AuthResult(boolean isValid, boolean isAdmin) {
        this.isValid = isValid;
        this.isAdmin = isAdmin;
    }
}
