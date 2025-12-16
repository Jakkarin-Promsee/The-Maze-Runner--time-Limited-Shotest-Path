package org.the.maze.runner.algorithm;

import org.the.maze.runner.model.Grid;
import org.the.maze.runner.model.Node;

import java.util.*;

public class BFSAlgorithm implements PathFindingAlgorithm {

    public List<Node> path = Collections.emptyList();
    private int visitedCount = 0;

    @Override
    public List<Node> findPath(Grid grid, Node start, Node end) {

        // Reset evaluation state
        path = Collections.emptyList();
        visitedCount = 0;

        // 1. Queue for nodes to visit
        Queue<Node> queue = new LinkedList<>();
        queue.add(start);

        // 2. Map to track the parent/predecessor
        Map<Node, Node> parentMap = new HashMap<>();

        // 3. Set to track visited nodes
        Set<Node> visited = new HashSet<>();
        visited.add(start);

        // --- Core BFS Loop ---
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            visitedCount++; // 👈 evaluation metric

            // Goal reached
            if (current.equals(end)) {
                path = AlgorithmUtils.reconstructPath(parentMap, start, end);
                return path;
            }

            // Explore neighbors
            for (Node neighbor : grid.getNeighbors(current)) {
                if (!visited.contains(neighbor) && !neighbor.isVoid()) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        // No path found
        return path;
    }

    // ---------------- EVALUATION ----------------

    public int getPathLength() {
        return AlgorithmUtils.pathLength(path);
    }

    public int getPathCost() {
        return AlgorithmUtils.pathCost(path);
    }

    public int getVisitedCount() {
        return visitedCount;
    }
}
