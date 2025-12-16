package org.the.maze.runner.algorithm;

public class GaConfig {

    public final int populationSize;
    public final double survivalRate;
    public final int maxGenerations;
    public final int maxPathLength;
    public final double mutationRate;
    public final int extraGenerationsAfterFound;

    public GaConfig(
            int populationSize,
            double survivalRate,
            int maxGenerations,
            int maxPathLength,
            double mutationRate,
            int extraGenerationsAfterFound) {
        this.populationSize = populationSize;
        this.survivalRate = survivalRate;
        this.maxGenerations = maxGenerations;
        this.maxPathLength = maxPathLength;
        this.mutationRate = mutationRate;
        this.extraGenerationsAfterFound = extraGenerationsAfterFound;
    }
}
