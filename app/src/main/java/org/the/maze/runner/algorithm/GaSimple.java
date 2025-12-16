package org.the.maze.runner.algorithm;

import org.the.maze.runner.model.*;
import java.util.*;

public class GaSimple implements PathFindingAlgorithm {

    private final GaConfig config;
    private final Random random = new Random();

    private static final int FAILURE_PENALTY = 1_000_000;
    private static final int LOOP_PENALTY = 5;
    private static final double GOAL_BIAS = 0.7;

    // ---------- Evaluation ----------
    public List<Node> path = Collections.emptyList();
    private int evaluatedCount = 0; // GA "visited"

    public GaSimple(GaConfig config) {
        this.config = config;
    }

    @Override
    public List<Node> findPath(Grid grid, Node start, Node end) {

        List<List<Node>> population = new ArrayList<>();
        for (int i = 0; i < config.populationSize; i++) {
            population.add(randomPath(grid, start, end));
        }

        boolean targetFound = false;
        int extraGenerations = 0;

        int log = 0;

        for (int generation = 0; generation < config.maxGenerations; generation++) {
            System.out.println(log++);

            population.sort(Comparator.comparingInt(p -> fitness(p, end)));

            List<Node> best = population.get(0);

            int hitIndex = firstHitIndex(best, end);
            if (hitIndex >= 0) {
                best = new ArrayList<>(best.subList(0, hitIndex + 1));
                population.set(0, best);
                targetFound = true;
            }

            if (targetFound) {
                if (extraGenerations >= config.extraGenerationsAfterFound) {
                    path = best;
                    return path;
                }
                extraGenerations++;
            }

            int survivors = Math.max(1, (int) (config.populationSize * config.survivalRate));
            List<List<Node>> survivorsList = new ArrayList<>(population.subList(0, survivors));

            List<List<Node>> nextPopulation = new ArrayList<>();
            nextPopulation.add(survivorsList.get(0)); // elitism

            while (nextPopulation.size() < config.populationSize) {
                List<Node> p1 = survivorsList.get(random.nextInt(survivors));
                List<Node> p2 = survivorsList.get(random.nextInt(survivors));

                List<Node> child = crossover(p1, p2);

                if (random.nextDouble() < config.mutationRate) {
                    mutate(grid, child, end);
                }

                nextPopulation.add(child);
            }

            population = nextPopulation;
        }

        population.sort(Comparator.comparingInt(p -> fitness(p, end)));

        path = population.get(0);
        return path;
    }

    // --------------------------------------------------
    // FITNESS (Dynamic Weight, First-Hit Based)
    // --------------------------------------------------

    private int fitness(List<Node> path, Node end) {
        evaluatedCount++;

        int hitIndex = firstHitIndex(path, end);

        // ---------- CASE 1: Reaches target ----------
        if (hitIndex >= 0) {

            int cost = 0;
            Set<Node> visited = new HashSet<>();

            for (int i = 1; i <= hitIndex; i++) {
                Node n = path.get(i);
                cost += 1 + n.getWeight();
                visited.add(n);
            }

            int loops = (hitIndex + 1) - visited.size();

            int distance = manhattan(path.get(hitIndex - 1), end);

            // Earlier + cheaper arrival dominates everything
            return cost + loops * LOOP_PENALTY + distance * 10;
        }

        // ---------- CASE 2: Not reached ----------
        int cost = 0;
        Set<Node> visited = new HashSet<>();

        for (int i = 1; i < path.size(); i++) {
            Node n = path.get(i);
            cost += 1 + n.getWeight();
            visited.add(n);
        }

        int loops = path.size() - visited.size();
        Node last = path.get(path.size() - 1);
        int distance = manhattan(last, end);

        return FAILURE_PENALTY + cost + distance * 10 + loops * LOOP_PENALTY;
    }

    // --------------------------------------------------
    // Helpers
    // --------------------------------------------------

    private int firstHitIndex(List<Node> path, Node end) {
        for (int i = 0; i < path.size(); i++) {
            if (path.get(i).equals(end))
                return i;
        }
        return -1;
    }

    private List<Node> randomPath(Grid grid, Node start, Node end) {
        List<Node> path = new ArrayList<>();
        path.add(start);
        Node current = start;

        for (int i = 0; i < config.maxPathLength; i++) {
            List<Node> neighbors = grid.getNeighbors(current);
            if (neighbors.isEmpty())
                break;

            neighbors.sort(Comparator.comparingInt(n -> manhattan(n, end)));

            Node next = random.nextDouble() < GOAL_BIAS
                    ? neighbors.get(0)
                    : neighbors.get(random.nextInt(neighbors.size()));

            if (next.isVoid())
                break;

            path.add(next);
            current = next;

            if (current.equals(end))
                break;
        }
        return path;
    }

    private List<Node> crossover(List<Node> a, List<Node> b) {
        Set<Node> aSet = new HashSet<>(a);

        // Collect all common nodes
        List<Integer> commonIndicesB = new ArrayList<>();
        for (int i = 1; i < b.size(); i++) {
            if (aSet.contains(b.get(i))) {
                commonIndicesB.add(i);
            }
        }

        // No common node → fallback
        if (commonIndicesB.isEmpty()) {
            return new ArrayList<>(a);
        }

        // Pick one common node at random
        int bCut = commonIndicesB.get(new Random().nextInt(commonIndicesB.size()));
        int aCut = a.indexOf(b.get(bCut));

        List<Node> child = new ArrayList<>(a.subList(0, aCut));
        child.addAll(b.subList(bCut, b.size()));

        return child;

    }

    private void mutate(Grid grid, List<Node> path, Node end) {
        if (path.size() <= 1)
            return;

        int cut = random.nextInt(path.size() - 1) + 1;
        path.subList(cut, path.size()).clear();

        Node current = path.get(path.size() - 1);

        for (int i = path.size(); i < config.maxPathLength; i++) {
            List<Node> neighbors = grid.getNeighbors(current);
            if (neighbors.isEmpty())
                break;

            neighbors.sort(Comparator.comparingInt(n -> manhattan(n, end)));

            Node next = random.nextDouble() < GOAL_BIAS
                    ? neighbors.get(0)
                    : neighbors.get(random.nextInt(neighbors.size()));

            if (next.isVoid())
                break;

            path.add(next);
            current = next;

            if (current.equals(end))
                break;
        }
    }

    private int manhattan(Node a, Node b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    // --------------------------------------------------
    // Evaluation getters (UI uses these)
    // --------------------------------------------------

    public int getPathLength() {
        return AlgorithmUtils.pathLength(path);
    }

    public int getPathCost() {
        return AlgorithmUtils.pathCost(path);
    }

    public int getVisitedCount() {
        return evaluatedCount;
    }
}
