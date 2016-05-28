package com.bitreight.taskmanager.repository.dao;

import com.bitreight.taskmanager.model.Developer;

import java.util.List;

public interface DeveloperDao {

    public int createDeveloper(Developer developer);
    public List<Developer> getDevelopers();
    public boolean updateDeveloper(Developer developer);
    public boolean updateCredentials(int developerId, String username, String password);
    public boolean deleteDeveloper(int developerId);

}
