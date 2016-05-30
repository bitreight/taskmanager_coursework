package com.bitreight.taskmanager;

import java.time.LocalDate;

public class Validation {

    public static boolean checkUsername(String username) {
        return !(username.length() < 3 | username.length() > 10);
    }

    public static boolean checkPassword(String password) {
        return !(password.length() < 3 | password.length() > 10);
    }

    public static boolean checkDeveloperName(String name) {
        return !(name.length() < 1 | name.length() > 20);
    }

    public static boolean checkDeveloperSurname(String surname) {
        return !(surname.length() < 1 | surname.length() > 20);
    }

    public static boolean checkDeveloperPatronymic(String patronymic) {
        return !(patronymic.length() < 1 | patronymic.length() > 20);
    }

    public static boolean checkDeveloperPosition(String position) {
        return !(position.length() < 1 | position.length() > 50);
    }

    public static boolean checkTaskName(String name) {
        return !(name.length() < 1 | name.length() > 60);
    }

    public static boolean checkTaskNumber(String number) {
        return number.matches("[0-9]+") && !(number.length() < 1 | number.length() > 8);
    }

    public static boolean checkTaskDate(LocalDate date) {
        return !(date == null);
    }

    public static boolean checkTaskDescription(String description) {
        return !(description.length() > 500);
    }
}

