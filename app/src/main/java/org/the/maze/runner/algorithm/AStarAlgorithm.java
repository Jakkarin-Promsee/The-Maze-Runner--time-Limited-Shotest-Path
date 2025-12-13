package org.the.maze.runner.algorithm;

import org.the.maze.runner.model.*;
import java.util.*;

public class AStarAlgorithm implements PathFindingAlgorithm {

    // Heuristic function: estimate cost from current node to the end node.
    // Using Manhattan Distance because movement is restricted to 4 directions.
    private int calculateHeuristic(Node current,Node end){
        // h(n) = |x1 - x2| + |y1 - y2|
        return Math.abs(current.x - end.x) + Math.abs(current.y - end.y);
    }

    @Override
    public List<Node> findPath(Grid grid, Node start, Node end) {

        // gScore: actual cost from start to a given node.
        // Initialize g(start) = 0.
        Map<Node,Integer> gScore = new HashMap<>();
        gScore.put(start,0);

        // fScore: estimated total cost f(n) = g(n) + h(n).
        Map<Node,Integer> fScore = new HashMap<>();
        fScore.put(start,calculateHeuristic(start,end));

        // Stores the path (where each node came from).
        Map<Node,Node> parentMap = new HashMap<>();

        // Open Set: nodes to be evaluated (priority queue sorted by best fScore).
        PriorityQueue<Node> openSet = new PriorityQueue<>(
            Comparator.comparingInt(node -> fScore.get(node))
        );
        openSet.add(start);

        while(!openSet.isEmpty()){
            // Select the node with the lowest fScore.
            Node current = openSet.poll();

            // If we reached the goal, reconstruct the full path.
            if(current.equals(end)){
                return AlgorithmUtils.reconstructPath(parentMap,start,end);
            }

            // Explore all valid neighboring cells.
            for(Node neighbor : grid.getNeighbors(current)){

                // Skip neighbors that are walls/blocked.
                if(neighbor.isWall()){
                    continue;
                }
                
                // The movement cost
                int costToNeighbor = neighbor.getWeight();

                // g(current): cost from start to node n
                int currentGScore = gScore.getOrDefault(current,Integer.MAX_VALUE);

                // g(neighbor): cost from start to neighbor of node n
                int tentative_gScore = currentGScore + costToNeighbor;

                // If this path to neighbor is better, update scores.
                if(tentative_gScore < gScore.getOrDefault(neighbor,Integer.MAX_VALUE)){

                    // Change to best path
                    parentMap.put(neighbor,current);

                    // Update gScore
                    gScore.put(neighbor,tentative_gScore);

                    // Update fScore
                    int new_fScore = gScore.get(neighbor) + calculateHeuristic(neighbor,end);
                    fScore.put(neighbor,new_fScore);

                    if(!openSet.contains(neighbor)){
                        openSet.add(neighbor);
                    }
                }

            }
        }
        return Collections.emptyList();
    }
}
