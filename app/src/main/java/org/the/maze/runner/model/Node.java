package org.the.maze.runner.model;

public class Node {

    // --- Core Properties (Immutable Coordinates) ---
    public final int x; // Column index
    public final int y; // Row index

    // --- Maze/Grid State Properties ---
    private int weight = 1; // Cost to traverse this node (default is 1)
    private boolean isVoid = false;
    private boolean isWall = false; // True if it's a blocked cell (#)
    private boolean isStart = false; // True if it's the starting point (S)
    private boolean isEnd = false; // True if it's the destination (G)

    // --- Pathfinding/Algorithm Properties ---
    private double distance = Double.MAX_VALUE; // Distance from the start node (e.g., for Dijkstra's)
    private Node parent; // The preceding node in the shortest path

    // A* specific heuristic (if needed)
    private double fScore = Double.MAX_VALUE; // f = g + h (A* score)
    private double gScore = Double.MAX_VALUE; // g = distance from start (same as 'distance' above)

    // --- Constructor ---

    /**
     * Creates a new Node at the specified grid coordinates.
     * 
     * @param x The column index.
     * @param y The row index.
     */
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // --- Getters and Setters for Maze/Grid State ---

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isWall() {
        return isWall;
    }

    public void setWall(boolean isWall) {
        this.isWall = isWall;
    }

    public boolean isVoid() {
        return isVoid;
    }

    public void setVoid(boolean isVoid) {
        this.isVoid = isVoid;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean isStart) {
        this.isStart = isStart;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }

    // --- Getters and Setters for Pathfinding Properties ---

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    // --- A* Specific Getters and Setters ---

    public double getFScore() {
        return fScore;
    }

    public void setFScore(double fScore) {
        this.fScore = fScore;
    }

    public double getGScore() {
        return gScore;
    }

    public void setGScore(double gScore) {
        this.gScore = gScore;
    }

    // --- Utility Method for Debugging/Hashing ---

    @Override
    public String toString() {
        return "(" + x + ", " + y + ") [Weight: " + weight + "]";
    }

    // Important: Override equals/hashCode for correct use in hash maps/sets (e.g.,
    // PriorityQueue in A*)
    // public boolean equals(Object o) { ... }
    // public int hashCode() { ... }
}