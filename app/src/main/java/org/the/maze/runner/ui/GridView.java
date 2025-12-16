package org.the.maze.runner.ui;

import java.util.*;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import org.the.maze.runner.algorithm.PathFindingAlgorithm;
import org.the.maze.runner.model.*;

public class GridView {

    private Grid grid;

    private Pane gridPane;

    private int maxWidth;
    private int maxHeight;

    // 🔑 Node → Rectangle mapping (core optimization)
    private final Map<Node, Rectangle> nodeRects = new HashMap<>();

    public GridView(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public void setScreen(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public Grid getGrid() {
        return grid;
    }

    public int getGridWidth() {
        return grid.getWidth();
    }

    public int getGridHeight() {
        return grid.getHeight();
    }

    // --------------------------------------------------
    // DRAW MAZE (ONCE)
    // --------------------------------------------------
    public Pane draw(String gridText) {
        Pane pane = new Pane();
        nodeRects.clear();

        grid = parseWeightedMaze(gridText);
        drawBaseGrid(pane);

        this.gridPane = pane;

        return pane;
    }

    public Pane drawPath(PathFindingAlgorithm algorithm, Paint color) {
        // Initilize Pane
        Pane gridPane = new Pane(); // If it has no maze grid and algorith, path==null

        if (algorithm == null ||
                grid == null)
            return gridPane;

        // Get start/end nodes from the model

        Node start = grid.getStartNode();
        Node end = grid.getEndNode();

        // Prevent maze solve conflict
        if (start == null || end == null) {
            System.err.println("Start or End    node not found in the grid.");
            return gridPane;
        }

        // Find the path
        List<Node> path = algorithm.findPath(grid, start, end);

        // Re-draw the entire grid, passing the found path to highlight it.
        drawFinalPath(path, color);

        return this.gridPane;
    }

    // --------------------------------------------------
    // FINAL PATH ONLY (NO REDRAW)
    // --------------------------------------------------
    public void drawFinalPath(List<Node> path, Paint color) {
        if (path == null)
            return;

        for (Node n : path) {
            if (n.isStart() || n.isEnd())
                continue;

            Rectangle r = nodeRects.get(n);
            if (r != null) {
                r.setFill(color);
            }
        }
    }

    // --------------------------------------------------
    // VISITED / EXPANDED NODE (INCREMENTAL)
    // --------------------------------------------------
    public void highlightVisited(Node n) {
        if (n == null || n.isStart() || n.isEnd())
            return;

        Rectangle r = nodeRects.get(n);
        if (r != null) {
            r.setFill(Color.LIGHTBLUE);
        }
    }

    // Thread-safe version (for background algorithms)
    public void highlightVisitedAsync(Node n) {
        Platform.runLater(() -> highlightVisited(n));
    }

    // --------------------------------------------------
    // BASE GRID DRAW (STATIC)
    // --------------------------------------------------
    private void drawBaseGrid(Pane pane) {
        pane.getChildren().clear();

        int tileSize = calculateTileSize();

        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                Node n = grid.getNode(x, y);

                Rectangle rect = new Rectangle(tileSize, tileSize);
                rect.setX(n.x * tileSize);
                rect.setY(n.y * tileSize);

                Color color;
                if (n.isVoid()) {
                    color = Color.GRAY;
                } else if (n.isWall()) {
                    color = Color.BLACK;
                } else if (n.isStart()) {
                    color = Color.GREEN;
                } else if (n.isEnd()) {
                    color = Color.RED;
                } else {
                    color = Color.web("#f0f0f0");
                }

                rect.setFill(color);
                rect.setStroke(Color.web("#333333"));

                pane.getChildren().add(rect);
                nodeRects.put(n, rect);

                // Weight label
                if (!n.isWall() && !n.isVoid() && !n.isStart() && !n.isEnd()) {
                    Label w = new Label(String.valueOf(n.getWeight()));
                    w.setStyle("-fx-font-size:" + (tileSize / 3) +
                            "px; -fx-font-weight: bold;");
                    w.setLayoutX(n.x * tileSize + tileSize / 2 - 6);
                    w.setLayoutY(n.y * tileSize + tileSize / 2 - 8);
                    pane.getChildren().add(w);
                }
            }
        }
    }

    private int calculateTileSize() {
        return Math.min(maxWidth / grid.getHeight(), maxHeight / grid.getWidth());
    }

    // --------------------------------------------------
    // PARSER (UNCHANGED)
    // --------------------------------------------------
    private Grid parseWeightedMaze(String input) {
        String[] rows = input.trim().split("\n");
        int height = rows.length;

        int width = 0;
        for (String row : rows) {
            width = Math.max(width, row.replaceAll("\"\\d+\"", "X").length());
        }

        Grid g = new Grid(width, height);

        for (int y = 0; y < height; y++) {
            int x = 0;
            String row = rows[y];
            for (int i = 0; i < row.length();) {
                Node n = g.getNode(x, y);
                char c = row.charAt(i);

                if (c == '#') {
                    n.setWall(true);
                    i++;
                } else if (c == 'S') {
                    n.setStart(true);
                    g.setStartNode(n);
                    i++;
                } else if (c == 'G') {
                    n.setEnd(true);
                    g.setEndNode(n);
                    i++;
                } else if (c == '"') {
                    int j = row.indexOf('"', i + 1);
                    int w = Integer.parseInt(row.substring(i + 1, j));
                    n.setWeight(w);
                    i = j + 1;
                } else {
                    i++;
                }
                x++;
            }
        }
        return g;
    }
}
