package org.the.maze.runner.controller;

import org.the.maze.runner.App;
import org.the.maze.runner.algorithm.*;
import org.the.maze.runner.ui.GridView;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class GridController {

    // Maze Visualize, Using javafx Pane to store 2D tiles
    @FXML
    private Pane gridPane;

    // The class to build javafx pane
    private GridView gridView;

    // Define tile size for visualization
    private static final int maxWidth = 478;
    private static final int maxHeight = 478;

    // Intialize when load scene
    @FXML
    public void initialize() {
        // Intialize grid view class size
        this.gridView = new GridView(maxWidth, maxHeight);

        // Load maze from previous step, Then build maze grid
        String maze = App.getMaze();
        Pane initialVisualization = gridView.draw(maze);
        updateVisualizationPane(initialVisualization);
    }

    // Set screen size
    private void updateVisualizationPaneScreen(int weidth, int height) {
        gridView.setScreen(weidth, height);
    }

    // Update Pane with new Pane
    private void updateVisualizationPane(Pane newPane) {
        if (gridPane == null) {
            System.err.println("Error: FXML gridPane container is null.");
            return;
        }

        // 1. Clear all existing children from the container
        gridPane.getChildren().clear();

        // 2. Add the new Pane containing the visualization
        gridPane.getChildren().add(newPane);
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

    // Go back to main page
    public void goBack() {
        App.setRoot("main-view");
    }

    // Go back to input page for new maze input
    public void goInitialize() {
        App.setRoot("input-view");
    }
}