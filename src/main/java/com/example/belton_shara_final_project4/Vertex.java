/*
Shara Belton
Programming Project 4
12/12/23
This class is the Vertex class. Here is where all vertices are
initiated when the user is clicked on a panel. The vertices are stored
as an object in an array in GraphDisplay for easy organization and access.
Getter methods are used to access each obs specific parameter.
 */
package com.example.belton_shara_final_project4;

public final class Vertex {
    private final double x;
    private final double y;
    private final int vertexNumber;

    private final char vertexName;

    public Vertex(double x, double y, char vertexName, int vertexNumber) {
        this.x = x;
        this.y = y;
        this.vertexName = vertexName;
        this.vertexNumber = vertexNumber;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public char getVertexName() {
        return vertexName;
    }

    public int getVertexNumber(){
        return vertexNumber;
    }

}