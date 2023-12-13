/*
Shara Belton
Programming Project 4
12/12/23
This class is the Graph class. All functionalities of each button in
Graph Display lies here. All main algorithms used to analyze the graph are
used here.
 */
package com.example.belton_shara_final_project4;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.util.*;

public class Graph {
    private static Map<Integer, List<Integer>> adjacencyList;

    static ArrayList<Integer> dfsResultList = new ArrayList<>();
    static ArrayList<Integer> bfsResultList = new ArrayList<>();

    // constructor
    public Graph() {
        adjacencyList = new HashMap<>();
    }
    // Method to add vertex
    static Shape addVertex(double x, double y) {
        return new Circle(x, y, 5);
    }

    public static void addVertexToList(int vertexNumber) {
            adjacencyList.put(vertexNumber, new LinkedList<>());
    }

    // Method to add an edge
    static Line addEdge(Vertex first, Vertex second) {
        adjacencyList.get(first.getVertexNumber()).add(second.getVertexNumber());
        System.out.println(first.getVertexNumber());
        System.out.println(second.getVertexNumber());

        adjacencyList.get(second.getVertexNumber()).add(first.getVertexNumber()); // because undirected

        return new Line(first.getX(), first.getY(), second.getX(), second.getY());

    }

    // Method to  check whether the graph has cycles

    static boolean hasCycles() {
        Set<Integer> visited = new HashSet<>();
        for (int vertex : adjacencyList.keySet()) {
            if (!visited.contains(vertex) && hasCycleHelper(vertex, -1, visited)) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasCycleHelper(int current, int parent, Set<Integer> visited) {
        visited.add(current);

        for (int neighbor : adjacencyList.getOrDefault(current, Collections.emptyList())) {
            if (!visited.contains(neighbor)) {
                if (hasCycleHelper(neighbor, current, visited)) {
                    return true;
                }
            } else if (neighbor != parent) {
                // If the neighbor is visited and is not the parent of the current vertex, it's a back edge
                return true;
            }
        }
        return false;
    }

    // Method check whether the graph is connected


    public static ArrayList<Integer> dfs(int startVex) {
        Set<Integer> visited = new HashSet<>();
        System.out.println(depthFirstResults(startVex, visited));
        return depthFirstResults(startVex, visited);
        //depthFirstResults(startVex, visited);
    }

    // Method that returns a list of vertices resulting from a depth-first graph search
    private static ArrayList<Integer> depthFirstResults(int vertexNumber, Set<Integer> visited) {

        visited.add(vertexNumber);
        System.out.println("Added: " + vertexNumber + " ");
        if (!dfsResultList.contains(vertexNumber)) {
            dfsResultList.add(vertexNumber);
        }

        List<Integer> neighbors = adjacencyList.get(vertexNumber);
        if (neighbors != null) {
            for (Integer neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    depthFirstResults(neighbor, visited);
                }
            }
        }
        return dfsResultList;
    }

    public static String isConnected(int x) {
        Set<Integer> visited = new HashSet<>();
        dfsConnect(adjacencyList.keySet().iterator().next(), visited);

        return visited.size() == adjacencyList.size() ? "Graph is connected." : "Graph is not connected.";
    }

    private static void dfsConnect(int start, Set<Integer> visited) {
        if (!visited.contains(start)) {
            visited.add(start);
            for (int neighbor : adjacencyList.getOrDefault(start, Collections.emptyList())) {
                dfsConnect(neighbor, visited);
            }
        }
    }
    // Method that returns a list of vertices resulting from a breadth first graph search
    public static ArrayList<Integer> breadthFirstResults(int vertexStart) {
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        visited.add(vertexStart);
        queue.offer(vertexStart);

        while (!queue.isEmpty()) {
            int currentVertex = queue.poll();
            System.out.print(currentVertex + " ");
            System.out.println("Next Added: " + currentVertex + " ");
            if (!bfsResultList.contains(currentVertex)) {
                bfsResultList.add(currentVertex);
            }

            for (int neighbor : adjacencyList.getOrDefault(currentVertex, Collections.emptyList())) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        return bfsResultList;
    }
    // Other methods needed

}
