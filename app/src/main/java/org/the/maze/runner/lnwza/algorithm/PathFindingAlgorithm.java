package org.the.maze.runner.lnwza.algorithm;

import org.the.maze.runner.lnwza.model.*;
import java.util.List;

public interface PathFindingAlgorithm {
    List<Node> findPath(Grid grid, Node start, Node end);
}
