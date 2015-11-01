package main.java;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import main.java.controllers.LoginController;

import java.io.IOException;

/**
 * Created by Алексей on 31.10.2015.
 */
public class ViewLoader {

    private static final String loginView = "/main/resources/view/Login.fxml";
    private static final String adminView = "/main/resources/view/Admin.fxml";
    private static final String userView = "/main/resources/view/User.fxml";
    private static final String userEditorView = "/main/resources/view/UserEditor.fxml";

    private static Stage primaryStage;

    public static void initialize(Stage stage) {
        if (primaryStage == null) {
            primaryStage = stage;
        }
    }

    public static void loadLogin() throws IOException {
        Parent login = FXMLLoader.load(ViewLoader.class.getResource(loginView));
        primaryStage.hide();
        primaryStage.setScene(new Scene(login));
        primaryStage.show();
    }

    public static void loadAdmin() throws IOException {
        Parent admin = FXMLLoader.load(ViewLoader.class.getResource(adminView));
        primaryStage.hide();
        primaryStage.setScene(new Scene(admin));
        primaryStage.show();
    }

    public static void loadUser() throws IOException {
        Parent user = FXMLLoader.load(ViewLoader.class.getResource(userView));
        primaryStage.hide();
        primaryStage.setScene(new Scene(user));
        primaryStage.show();
    }

    public static void loadUserEditor() throws IOException {
        Parent userEditor = FXMLLoader.load(ViewLoader.class.getResource(userEditorView));
        primaryStage.hide();
        primaryStage.setScene(new Scene(userEditor));
        primaryStage.show();
    }
}
