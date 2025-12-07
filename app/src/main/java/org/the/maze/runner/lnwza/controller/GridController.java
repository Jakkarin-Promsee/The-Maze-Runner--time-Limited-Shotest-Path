package org.the.maze.runner.lnwza.controller;

import org.the.maze.runner.lnwza.App;
import org.the.maze.runner.lnwza.algorithm.*;
import org.the.maze.runner.lnwza.model.*;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

import java.util.List;

public class GridController {

    @FXML
    private Pane gridPane;

    private Grid grid;
    private PathFindingAlgorithm algorithm;

    public void initialize() {
        grid = new Grid(20, 20); // 20x20 maze
    }

    public void runBFS() {
        algorithm = new BFSAlgorithm();
        drawPath();
    }

    public void runDijkstra() {
        algorithm = new DijkstraAlgorithm();
        drawPath();
    }

    public void runAStar() {
        algorithm = new AStarAlgorithm();
        drawPath();
    }

    private void drawPath() {
        if (algorithm == null)
            return;

        Node start = grid.getNode(0, 0);
        Node end = grid.getNode(19, 19);

        List<Node> path = algorithm.findPath(grid, start, end);

        gridPane.getChildren().clear();

        for (Node n : path) {
            javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(
                    n.x * 30, n.y * 30, 30, 30);
            rect.setStyle("-fx-fill: yellow;");
            gridPane.getChildren().add(rect);
        }
    }

    public void goBack() {
        App.setRoot("main-view");
    }
}
