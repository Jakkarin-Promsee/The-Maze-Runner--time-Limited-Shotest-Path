package org.the.maze.runner.algorithm;

import org.the.maze.runner.model.*;
import java.util.*;

public class DijkstraAlgorithm implements PathFindingAlgorithm {

    @Override
    public List<Node> findPath(Grid grid, Node start, Node end) {
        // TODO: Real BFS
        return Arrays.asList(start, end);
    }
}
