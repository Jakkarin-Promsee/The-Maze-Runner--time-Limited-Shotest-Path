package org.the.maze.runner.lnwza.model;

public class Grid {
    private Node[][] nodes;

    public Grid(int w, int h) {
        nodes = new Node[w][h];
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
                nodes[i][j] = new Node(i, j);
    }

    public Node getNode(int x, int y) {
        return nodes[x][y];
    }
}
