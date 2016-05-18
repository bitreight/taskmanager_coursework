package com.bitreight.taskmanager;

public class AuthResult {
    public final boolean isValid;
    public final boolean isAdmin;

    public AuthResult(boolean isValid, boolean isAdmin) {
        this.isValid = isValid;
        this.isAdmin = isAdmin;
    }
}
