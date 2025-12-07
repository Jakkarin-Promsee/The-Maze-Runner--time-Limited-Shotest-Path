package org.the.maze.runner.algorithm;

import org.the.maze.runner.model.*;
import java.util.List;

public interface PathFindingAlgorithm {
    List<Node> findPath(Grid grid, Node start, Node end);
}
