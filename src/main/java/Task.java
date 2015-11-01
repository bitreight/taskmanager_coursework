package main.java;

public class Task {
    private int id;
    private String name;
    private String description;
    private String term;
    private String state;
    
    public Task(int id, String name, String description, String term, boolean isDone) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.term = term;
        if (isDone) {
           this.state = "Выполнена"; 
        }            
        else {
            this.state = "";
        }
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getTerm() {
        return term;
    }
    
    public void setTerm(String term) {
        this.term = term;
    }
    
    public String getState() {
        return state;
    }
    
    public void setState(boolean isDone) {
        if (isDone) {
           this.state = "Выполнена"; 
        }            
        else {
            this.state = "";
        }
    }    
}
