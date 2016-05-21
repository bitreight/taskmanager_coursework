package com.bitreight.taskmanager;

import com.bitreight.taskmanager.controllers.ScreensController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Taskmanager");
        ScreensController.initStage(primaryStage);
        ScreensController.loadLogin();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
