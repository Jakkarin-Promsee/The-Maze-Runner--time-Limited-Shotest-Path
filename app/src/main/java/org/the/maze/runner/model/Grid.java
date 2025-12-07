package org.the.maze.runner.model;

import java.util.List;

public class Grid {

    private final int width;
    private final int height;
    private final Node[][] nodes; // Stores all nodes in the grid
    private Node startNode;
    private Node endNode;

    /**
     * Constructs a new Grid with the specified dimensions, initializing all Node
     * objects.
     * 
     * @param width  The width of the grid (number of columns).
     * @param height The height of the grid (number of rows).
     */
    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.nodes = new Node[width][height];

        // Initialize all nodes in the grid
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.nodes[x][y] = new Node(x, y);
            }
        }
    }

    // --- Essential Accessor Methods ---

    /**
     * Retrieves the Node at the specified (x, y) coordinates.
     * 
     * @param x The column index (0 to width - 1).
     * @param y The row index (0 to height - 1).
     * @return The Node object at (x, y).
     */
    public Node getNode(int x, int y) {
        // Basic boundary check
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return nodes[x][y];
        }
        return null; // Return null if coordinates are out of bounds
    }

    /**
     * Retrieves a list of neighbors for a given node.
     * This is crucial for pathfinding algorithms.
     * 
     * @param node The node to find neighbors for.
     * @return A list of valid, non-wall neighbor nodes.
     */
    public List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new java.util.ArrayList<>();
        int x = node.x;
        int y = node.y;

        // Check 4 directions: Right, Left, Down, Up
        int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            Node neighbor = getNode(newX, newY);

            // Add the neighbor if it exists and is not a wall
            if (neighbor != null && !neighbor.isWall()) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    // --- Getters for Grid Properties ---

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // --- Getters and Setters for Start and End Nodes (Used by GridController
    // parsing logic) ---

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }
}