package org.the.maze.runner.controller;

import org.the.maze.runner.App;
import org.the.maze.runner.algorithm.*;
import org.the.maze.runner.model.*;
import org.the.maze.runner.ui.GridView;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane; // Using StackPane as the FXML container is often best

public class GridController {

    // IMPORTANT: Assuming gridPane is a container (like VBox, HBox, or StackPane)
    // in your FXML that will hold the actual visualization surface.
    @FXML
    private Pane gridPane; // The FXML-injected container for the visualization

    private GridView gridView;

    // Define tile size for visualization
    private static final int TILE_SIZE = 30;

    // The input string you provided
    private final String MAZE_INPUT_STRING = "###############\n" +
            "#S#\"2\"\"1\"\"5\"\"8\"\"3\"\"7\"\"1\"\"2\"\"4\"\"4\"\"9\"#\n" +
            "#\"10\"#\"3\"#\"1\"\"5\"\"10\"####\"10\"\"2\"#\n" +
            "#\"10\"\"1\"\"6\"\"7\"\"2\"\"5\"\"3\"\"4\"\"10\"#\"3\"\"9\"\"9\"#\n" +
            "#\"7\"\"4\"\"1\"#####\"3\"#####\n" +
            "#\"2\"#\"7\"\"5\"\"1\"#\"5\"\"8\"\"2\"#\"5\"\"7\"\"4\"#\n" +
            "#\"5\"##\"10\"\"7\"#\"5\"\"6\"\"5\"#\"3\"\"3\"\"2\"#\n" +
            "#\"5\"\"7\"\"1\"\"2\"\"6\"#\"3\"\"10\"\"3\"\"9\"\"4\"#\"10\"#\n" +
            "#\"3\"#\"1\"####\"1\"\"4\"###\"9\"#\n" +
            "#\"2\"#\"10\"\"8\"\"4\"#\"1\"\"9\"\"8\"\"5\"\"5\"#\"10\"#\n" +
            "#\"6\"#\"7\"#\"4\"#\"4\"###\"8\"#\"5\"#\n" +
            "#\"6\"\"5\"\"4\"#\"5\"\"4\"\"10\"#\"9\"#\"9\"#\"1\"#\n" +
            "#\"4\"\"7\"###\"2\"\"3\"#\"8\"#\"8\"#\"7\"#\n" +
            "#\"7\"\"4\"\"1\"\"8\"\"1\"\"7\"\"10\"\"1\"\"6\"\"4\"\"10\"\"5\"G#\n" +
            "###############";

    @FXML
    public void initialize() {
        this.gridView = new GridView(TILE_SIZE);
        Pane initialVisualization = gridView.draw(MAZE_INPUT_STRING);

        // CRITICAL: Call the update function to place the new Pane into the FXML
        // container
        updateVisualizationPane(initialVisualization);
    }

    // --- Core Function to Update the FXML Pane ---

    /**
     * Replaces the current content of the FXML-injected gridPane with the new
     * visualization Pane.
     * 
     * @param newPane The Pane containing the freshly drawn maze/path.
     */
    private void updateVisualizationPane(Pane newPane) {
        if (gridPane == null) {
            System.err.println("Error: FXML gridPane container is null.");
            return;
        }
        // 1. Clear all existing children from the container
        gridPane.getChildren().clear();

        // 2. Add the new Pane containing the visualization
        gridPane.getChildren().add(newPane);

        // Optional: Ensure the new Pane is stretched to fill the container if gridPane
        // is a layout like StackPane/BorderPane
        // newPane.maxWidth(gridPane.getWidth());
        // newPane.maxHeight(gridPane.getHeight());
    }

    // --- Algorithm Runners ---

    @FXML
    public void runBFS() {
        Pane pathVisualization = gridView.drawPath(new BFSAlgorithm());
        updateVisualizationPane(pathVisualization);
    }

    @FXML
    public void runDijkstra() {
        Pane pathVisualization = gridView.drawPath(new DijkstraAlgorithm());
        updateVisualizationPane(pathVisualization);
    }

    @FXML
    public void runAStar() {
        Pane pathVisualization = gridView.drawPath(new AStarAlgorithm());
        updateVisualizationPane(pathVisualization);
    }

    // --- Path Drawing Logic ---

    public void goBack() {
        App.setRoot("main-view");
    }
}