package org.the.maze.runner.ui;

import java.util.List;
import org.the.maze.runner.algorithm.*;
import org.the.maze.runner.model.*;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class GridView {

    // --- Fields ---
    private Pane gridPane; // The drawing surface - WILL BE CREATED HERE
    private Grid grid;

    // Define tile size for visualization
    private final int TILE_SIZE;

    // --- Constructor ---
    public GridView(int TILE_SIZE) {
        this.TILE_SIZE = TILE_SIZE;
    }

    // --- Public Drawing Methods ---

    /**
     * Parses the input string, initializes the Grid model, and draws the initial
     * visualization.
     * 
     * @param MAZE_INPUT_STRING The raw string input of the maze layout.
     * @return The JavaFX Pane containing the drawn grid.
     */
    public Pane draw(String MAZE_INPUT_STRING) {
        // **CRITICAL FIX:** Initialize the Pane instance
        this.gridPane = new Pane();

        // Parse the string and initialize the Grid model
        grid = parseWeightedMaze(MAZE_INPUT_STRING);

        // Draw the initial grid setup (walls, start, end, weights)
        drawGridVisualization(null);

        return gridPane;
    }

    /**
     * Runs the pathfinding algorithm and updates the visualization to show the
     * path.
     * 
     * @param algorithm The algorithm to run (BFS, Dijkstra, A*).
     * @return The JavaFX Pane containing the drawn grid with the path highlighted.
     */
    public Pane drawPath(PathFindingAlgorithm algorithm) {
        if (algorithm == null || grid == null)
            return this.gridPane; // Return current pane if grid/algorithm is invalid

        // Get start/end nodes from the model
        Node start = grid.getStartNode();
        Node end = grid.getEndNode();

        if (start == null || end == null) {
            System.err.println("Start or End node not found in the grid.");
            return this.gridPane;
        }

        // 1. Find the path
        List<Node> path = algorithm.findPath(grid, start, end);

        // 2. Re-draw the entire grid, passing the found path to highlight it.
        drawGridVisualization(path);

        return gridPane;
    }

    // --- Visualization Core ---

    /**
     * Renders the entire grid visualization, optionally highlighting a path.
     * 
     * @param path The list of nodes forming the shortest path (can be null).
     */
    private void drawGridVisualization(List<Node> path) {
        // Ensure the pane exists before clearing/adding children
        if (gridPane == null)
            return;
        gridPane.getChildren().clear();

        for (int y = 0; y < grid.getHeight(); y++) {
            for (int x = 0; x < grid.getWidth(); x++) {
                Node n = grid.getNode(x, y);

                // 1. Create the base Rectangle (The Cell)
                Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
                rect.setX(n.x * TILE_SIZE);
                rect.setY(n.y * TILE_SIZE);

                // Determine base color based on node type
                Color baseColor;
                if (n.isWall()) {
                    baseColor = Color.BLACK; // Wall color
                } else if (n.isStart()) {
                    baseColor = Color.GREEN; // Start color
                } else if (n.isEnd()) {
                    baseColor = Color.RED; // End color
                } else {
                    baseColor = Color.web("#f0f0f0"); // White/light gray for weighted nodes
                }
                rect.setFill(baseColor);
                rect.setStroke(Color.web("#333333")); // Dark border for cell separation

                // If a path exists and this node is on the path, override color
                if (path != null && path.contains(n) && !n.isStart() && !n.isEnd()) {
                    rect.setFill(Color.YELLOW); // Path color
                }

                // Add cell to the Pane
                gridPane.getChildren().add(rect);

                // 2. Add Weight Label if it's a weighted node and not a wall
                // Changed condition from n.getWeight() >= 1 to n.getWeight() > 1
                // to only label non-default (weight=1) nodes, but keeping your original
                // condition for safety.
                if (n.getWeight() >= 1 && !n.isStart() && !n.isEnd()) {
                    Label weightLabel = new Label(String.valueOf(n.getWeight()));

                    // Dynamic font size based on TILE_SIZE
                    weightLabel.setStyle("-fx-font-size:" + ((int) TILE_SIZE / 3)
                            + "px; -fx-text-fill: black; -fx-font-weight: bold;");

                    // Position the label in the center of the cell
                    // Note: Centering labels is complex due to font metrics.
                    // These offsets are approximations (x-5, y-8) from the previous code.
                    weightLabel.setLayoutX(n.x * TILE_SIZE + TILE_SIZE / 2 - (weightLabel.getText().length() * 3)); // dynamic
                                                                                                                    // X
                                                                                                                    // center
                    weightLabel.setLayoutY(n.y * TILE_SIZE + TILE_SIZE / 2 - 8); // approximate Y center

                    gridPane.getChildren().add(weightLabel);
                }
            }
        }

        // Final re-draw of start/end to ensure they are on top of the path color
        drawStartEndNodes(path);
    }

    // Helper to draw start/end on top
    private void drawStartEndNodes(List<Node> path) {
        Node start = grid.getStartNode();
        Node end = grid.getEndNode();

        if (start != null) {
            Rectangle startRect = createSpecialRect(start, Color.GREEN, TILE_SIZE);
            gridPane.getChildren().add(startRect);
        }

        if (end != null) {
            Color endColor = (path != null && path.contains(end)) ? Color.RED.darker() : Color.RED;
            Rectangle endRect = createSpecialRect(end, endColor, TILE_SIZE);
            gridPane.getChildren().add(endRect);
        }
    }

    private Rectangle createSpecialRect(Node n, Color color, int size) {
        Rectangle rect = new Rectangle(size, size);
        rect.setX(n.x * size);
        rect.setY(n.y * size);
        rect.setFill(color);
        rect.setStroke(Color.web("#CCCCCC")); // Light border
        return rect;
    }

    // --- Input Parsing Logic (Kept here as it's tightly coupled to the View's
    // setup) ---

    private Grid parseWeightedMaze(String input) {
        String[] rows = input.trim().split("\n");
        int height = rows.length;

        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("#|S|G|\"\\d+\"").matcher(input);

        // Calculate width by summing tokens in the first meaningful row
        int width = 0;
        if (rows.length > 0) {
            // Use row 1 (the second row) for width calculation as row 0 is a border
            java.util.regex.Matcher widthMatcher = java.util.regex.Pattern.compile("#|S|G|\"\\d+\"").matcher(rows[1]);
            while (widthMatcher.find()) {
                width++;
            }
        }

        Grid newGrid = new Grid(width, height);

        int x = 0;
        int y = 0;

        matcher.reset();
        while (matcher.find()) {
            String token = matcher.group();

            // Row transition logic
            if (x >= width) {
                x = 0;
                y++;
            }
            if (y >= height)
                break;

            Node node = newGrid.getNode(x, y);

            if (token.equals("#")) {
                node.setWall(true);
                node.setWeight(0);
            } else if (token.equals("S")) {
                node.setStart(true);
                node.setWeight(1);
                newGrid.setStartNode(node);
            } else if (token.equals("G")) {
                node.setEnd(true);
                node.setWeight(1);
                newGrid.setEndNode(node);
            } else if (token.startsWith("\"") && token.endsWith("\"")) {
                try {
                    int weight = Integer.parseInt(token.substring(1, token.length() - 1));
                    node.setWeight(weight);
                    node.setWall(false);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid weight token: " + token);
                    node.setWeight(1);
                }
            } else {
                node.setWeight(1);
            }

            x++;
        }

        System.out.println("Maze parsed successfully: " + width + "x" + height);
        return newGrid;
    }
}