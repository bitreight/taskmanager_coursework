package com.bitreight.taskmanager.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class ScreensController {

    private static final String pathToAdminScreen = "/fxml/Admin.fxml";
    private static final String pathToUserScreen = "/fxml/User.fxml";
    private static final String pathToLoginScreen = "/fxml/Login.fxml";

    private static Stage primaryStage;
    private static FXMLLoader screenLoader;
    private static ApplicationContext mainContext;

    private static final int screenSizeX = 1000;
    private static final int screenSizeY = 600;


    static {
        //screenLoader = new FXMLLoader();
        mainContext = new ClassPathXmlApplicationContext("/spring/spring-context.xml");
    }

    public static void initStage(Stage stage) {
        if(primaryStage == null) {
            primaryStage = stage;
        }
    }

    public static void loadAdmin() throws IOException {
        if(primaryStage == null) {
            return;
        }
        screenLoader = new FXMLLoader(ScreensController.class.getResource(pathToAdminScreen));
        loadScreen(screenLoader);
    }

    public static void loadUser() throws IOException {
        if(primaryStage == null) {
            return;
        }
        screenLoader = new FXMLLoader(ScreensController.class.getResource(pathToUserScreen));
        loadScreen(screenLoader);
    }

    public static void loadLogin() throws IOException {
        if(primaryStage == null) {
            return;
        }
        screenLoader = new FXMLLoader(ScreensController.class.getResource(pathToLoginScreen));
        loadScreen(screenLoader);
    }

    private static void loadScreen(FXMLLoader loader) throws IOException {
        primaryStage.setScene(new Scene(loader.load(), screenSizeX, screenSizeY));
        mainContext.getAutowireCapableBeanFactory().autowireBean(loader.getController());
    }
}
