package org.the.maze.runner.lnwza.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.the.maze.runner.lnwza.App;

import java.io.File;
import java.nio.file.Files;

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
        loadExample("S..#\n.#..\n..#E");
    }

    @FXML
    private void onExample2() {
        loadExample("S....\n###..\n...#E");
    }

    @FXML
    private void onExample3() {
        loadExample("S#..\n..#.\n.#.E");
    }

    @FXML
    private void onExample4() {
        loadExample("S...\n####\n...E");
    }

    @FXML
    private void onExample5() {
        loadExample("S.#.\n#..#\n..#E");
    }

    @FXML
    private void onExample6() {
        loadExample("S.#.\n#..#\n..#E");
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