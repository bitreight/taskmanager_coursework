package com.bitreight.taskmanager.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;


public class Task {

    private IntegerProperty id;
    private IntegerProperty number;
    private StringProperty name;
    private StringProperty description;
    private Date deadline;
    private IntegerProperty priority;
    private BooleanProperty isCompleted;


}
