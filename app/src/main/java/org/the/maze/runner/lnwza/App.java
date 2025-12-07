package org.the.maze.runner.lnwza;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage;
    private static final Map<String, Object> dataStore = new HashMap<>();

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(App.class.getResource("main-view.fxml"));

        primaryStage = stage;
        setRoot("main-view");
        stage.setTitle("The Maze Runner lnwza");
        stage.show();
    }

    public static void setRoot(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
            double width = 1080;
            double height = 600;

            Scene scene = new Scene(loader.load(), width, height);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------
    // DATA PASSING BETWEEN PAGES
    // -----------------------------
    public static void setData(String key, Object value) {
        dataStore.put(key, value);
    }

    public static Object getData(String key) {
        return dataStore.get(key);
    }
}
