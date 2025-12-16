package org.the.maze.runner.controller;

import javafx.scene.control.Label;

import java.util.List;

import org.the.maze.runner.App;
import org.the.maze.runner.algorithm.*;
import org.the.maze.runner.model.Grid;
import org.the.maze.runner.model.Node;
import org.the.maze.runner.ui.GridView;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class GridController {

    @FXML
    private Label bestCostLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Label costLabel;
    @FXML
    private Label pathLengthLabel;
    @FXML
    private Label nodeVisitedLabel;

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

    @FXML
    public void clearPath() {
        String maze = App.getMaze();
        Pane initialVisualization = gridView.draw(maze);
        updateVisualizationPane(initialVisualization);
    }

    @FXML
    public void runGa() {
        resetEvaluation();
        setStatusRunning();

        int cells = gridView.getGridWidth() * gridView.getGridHeight();

        GaConfig config = new GaConfig(
                300,
                0.3,
                1000,
                cells,
                0.6,
                100);

        Task<Pane> task = new Task<>() {
            long start;
            GaSimple ga;

            @Override
            protected Pane call() {
                start = System.nanoTime();
                ga = new GaSimple(config);
                return gridView.drawPath(ga, Color.web("#4c00b0"));
            }

            @Override
            protected void succeeded() {
                updateVisualizationPane(getValue());

                long timeMs = elapsedMs(start);

                timeLabel.setText("Time: " + timeMs + " ms");
                costLabel.setText("Cost: " + ga.getPathCost());
                pathLengthLabel.setText("Path Length: " + ga.getPathLength());
                nodeVisitedLabel.setText("Visited Nodes: " + ga.getVisitedCount());

                setStatusDone();
            }

            @Override
            protected void failed() {
                setStatusError(getException());
            }
        };

        new Thread(task).start();
    }

    private void setStatusError(Throwable ex) {
        statusLabel.setText("Status: Error");
        statusLabel.setTextFill(Color.RED);
        ex.printStackTrace();
    }

    @FXML
    public void runBFS() {
        resetEvaluation();
        setStatusRunning();

        long start = System.nanoTime();

        BFSAlgorithm bfs = new BFSAlgorithm();
        Pane pathVisualization = gridView.drawPath(bfs, Color.web("#3366ff"));

        updateVisualizationPane(pathVisualization);

        long timeMs = elapsedMs(start);

        timeLabel.setText("Time: " + timeMs + " ms");
        costLabel.setText("Cost: " + bfs.getPathCost());
        pathLengthLabel.setText("Path Length: " + bfs.getPathLength());
        nodeVisitedLabel.setText("Visited Nodes: " + bfs.getVisitedCount());

        setStatusDone();
    }

    @FXML
    public void runDijkstra() {
        resetEvaluation();
        setStatusRunning();

        long start = System.nanoTime();

        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
        Pane pathVisualization = gridView.drawPath(dijkstra, Color.web("#ffaa00"));

        updateVisualizationPane(pathVisualization);

        long timeMs = elapsedMs(start);

        timeLabel.setText("Time: " + timeMs + " ms");
        costLabel.setText("Cost: " + dijkstra.getPathCost());
        pathLengthLabel.setText("Path Length: " + dijkstra.getPathLength());
        nodeVisitedLabel.setText("Visited Nodes: " + dijkstra.getVisitedCount());

        setStatusDone();
    }

    @FXML
    public void runAStar() {
        resetEvaluation();
        setStatusRunning();

        long start = System.nanoTime();

        AStarAlgorithm astar = new AStarAlgorithm();
        Pane pathVisualization = gridView.drawPath(astar, Color.web("#00b06a"));

        updateVisualizationPane(pathVisualization);

        long timeMs = elapsedMs(start);

        timeLabel.setText("Time: " + timeMs + " ms");
        costLabel.setText("Cost: " + astar.getPathCost());
        pathLengthLabel.setText("Path Length: " + astar.getPathLength());
        nodeVisitedLabel.setText("Visited Nodes: " + astar.getVisitedCount());

        setStatusDone();
    }

    // Go back to main page
    public void goBack() {
        App.setRoot("main-view");
    }

    // Go back to input page for new maze input
    public void goInitialize() {
        App.setRoot("input-view");
    }

    private void setStatusRunning() {
        statusLabel.setText("Status: Running");
        statusLabel.setTextFill(Color.ORANGE);
    }

    private void setStatusDone() {
        statusLabel.setText("Status: Complete Finding Path");
        statusLabel.setTextFill(Color.LIMEGREEN);
    }

    private void resetEvaluation() {
        DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();

        Grid grid = gridView.getGrid();
        Node start = grid.getStartNode();
        Node end = grid.getEndNode();
        dijkstra.findPath(grid, start, end);

        statusLabel.setText("Status: Ready");
        timeLabel.setText("Time: - ms");
        costLabel.setText("Cost: -");
        bestCostLabel.setText("Best Cost: " + dijkstra.getPathCost() + "(From Dijkstra)");
        pathLengthLabel.setText("Path Length: -");
        nodeVisitedLabel.setText("Visited Nodes: -");
    }

    private long elapsedMs(long startNano) {
        return (System.nanoTime() - startNano) / 1_000_000;
    }

}