package com.bitreight.taskmanager.model;

public class Developer {

    private int id;
    private String username = "";
    private String password = "";
    private String name = "";
    private String surname = "";
    private String patronymic = "";
    private String position = "";
    private boolean isAdmin;

   public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        if(surname != null && name != null && patronymic != null) {
            return surname + " " + name.charAt(0) + "." + patronymic.charAt(0) + ".";
        }
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Developer developer = (Developer) o;

        if (id != developer.id) return false;
        if (isAdmin != developer.isAdmin) return false;
        if (!username.equals(developer.username)) return false;
        if (!password.equals(developer.password)) return false;
        if (!name.equals(developer.name)) return false;
        if (!surname.equals(developer.surname)) return false;
        if (!patronymic.equals(developer.patronymic)) return false;
        return position.equals(developer.position);

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + surname.hashCode();
        result = 31 * result + patronymic.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + (isAdmin ? 1 : 0);
        return result;
    }
}
