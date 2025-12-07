package org.the.maze.runner.controller;

import org.the.maze.runner.App;

import javafx.event.ActionEvent;

public class MainController {

    public void startApp(ActionEvent e) {
        App.setRoot("input-view");
    }

    public void exitApp(ActionEvent e) {
        System.exit(0);
    }
}
