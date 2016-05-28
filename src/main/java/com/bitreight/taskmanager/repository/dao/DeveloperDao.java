package com.bitreight.taskmanager.repository.dao;

import com.bitreight.taskmanager.model.Developer;

import java.util.List;

public interface DeveloperDao {

    public int createDeveloper(Developer developer);
    public void updateDeveloper(Developer developer);
    public List<Developer> getDevelopers();
    public void deleteDeveloper(int developerId);

}
