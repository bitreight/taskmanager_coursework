package main.java;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Task manager");
        ViewLoader.initialize(primaryStage);
        ViewLoader.loadLogin();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
