package org.the.maze.runner.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import org.the.maze.runner.App;

public class InputController {

    @FXML
    private TextArea mazeInputArea;

    @FXML
    private TextArea mazeOutputArea;

    @FXML
    private TextArea mazeInputSeed;

    @FXML
    private TextArea mazeGenerateWidth;

    @FXML
    private TextArea mazeGenerateHeight;

    // Called when user clicks "Choose Maze File"
    @FXML
    private void onChooseFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Maze File");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        Stage stage = App.getPrimaryStage(); // convenience method (see below)
        File file = chooser.showOpenDialog(stage);

        if (file != null) {
            try {
                String content = Files.readString(file.toPath());
                mazeInputArea.setText(content);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Example buttons
    @FXML
    private void onExample1() {
        loadFileFromPath("m15_15.txt");
    }

    @FXML
    private void onExample2() {
        loadFileFromPath("m40_40.txt");
    }

    @FXML
    private void onExample3() {
        loadFileFromPath("m50_50.txt");
    }

    @FXML
    private void onExample4() {
        loadFileFromPath("m60_60.txt");
    }

    @FXML
    private void onExample5() {
        loadFileFromPath("m70_60.txt");
    }

    @FXML
    private void onExample6() {
        loadFileFromPath("m100_100.txt");
    }

    private void loadMazeFromFile(File file) {
        try {
            String content = Files.readString(file.toPath());
            mazeInputArea.setText(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFileFromPath(String name) {
        try {
            URL url = getClass().getResource("/org/the/maze/runner/maze_example/" + name);

            if (url == null) {
                System.out.println("File not found: " + url);
                return;
            }

            String content = Files.readString(Path.of(url.toURI()));
            mazeInputArea.setText(content);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadExample(String content) {
        mazeInputArea.setText(content);
    }

    @FXML
    private void onGenerateMaze() {
        loadExample("generate");
    }

    @FXML
    private void onBack() {
        App.setRoot("main-view");
    }

    // CONTINUE → NEXT PAGE
    @FXML
    private void onContinue() {
        String mazeText = mazeInputArea.getText().trim();

        if (mazeText.isEmpty()) {
            System.out.println("Maze is empty!");
            return;
        }

        // Pass data to another controller
        App.setData("mazeInput", mazeText);

        // Load next page
        App.setRoot("grid-view");
    }
}