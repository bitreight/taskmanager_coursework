package com.bitreight.taskmanager.repository.dao;

import com.bitreight.taskmanager.exceptions.DeveloperDaoException;
import com.bitreight.taskmanager.model.Developer;

import java.util.List;

public interface DeveloperDao {

    public int createDeveloper(Developer developer) throws DeveloperDaoException;
    public List<Developer> getDevelopers() throws DeveloperDaoException;
    public void updateDeveloper(Developer developer) throws DeveloperDaoException;
    public void updateCredentials(int developerId, String username, String password) throws DeveloperDaoException;
    public void deleteDeveloper(int developerId) throws DeveloperDaoException;

}
