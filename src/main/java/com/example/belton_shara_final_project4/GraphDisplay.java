/*
Shara Belton
Programming Project 4
12/12/23
This class is the GraphDisplay class. Here is where all of the info
given by the user is translated to a JavaFX pane. Everything, including
the graph is displayed. Each button uses a method, and some methods call
other methods for better organization. A list of each vertex object is
kept here.
 */
package com.example.belton_shara_final_project4;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class GraphDisplay extends Application {
    public Pane graphPane;
    private char nextLabel = 'A';
    static public ArrayList<Vertex> vertices = new ArrayList<>();

    Graph graph = new Graph();
    private int vertexNumber = 0;

    @Override
    public void start(Stage primaryStage) throws IOException {

        // Create buttons

        HBox AddEdgeSection = new HBox(20);
        //initiate padding
        Insets padding = new Insets(20, 5, 20, 5);
        Insets textPadding = new Insets(10); // 10 pixels on all sides

        AddEdgeSection.setPadding(padding);

        Button addEdge = new Button("Add Edge");

        // user input text
        Label edgeVertex1 = new Label("Vertex 1 ");

        TextField edgeVertexText1 = new TextField();
        edgeVertexText1.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String input = event.getCharacter();
            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                // Reject the input if it's not a single letter
                event.consume();
            }
        });

        Label edgeVertex2 = new Label("Vertex 2 ");

        TextField edgeVertexText2 = new TextField();
        edgeVertexText2.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String input = event.getCharacter();
            if (input.length() != 1 || !Character.isLetter(input.charAt(0))) {
                // Reject the input if it's not a single letter
                event.consume();
            }
        });
        addEdge.setOnAction(e -> handleUserInput(edgeVertexText1, edgeVertexText2));


        AddEdgeSection.getChildren().addAll(addEdge, edgeVertex1, edgeVertexText1, edgeVertex2, edgeVertexText2);
        AddEdgeSection.setAlignment(Pos.CENTER);

        // Graph Section
        graphPane = new Pane();
        graphPane.setMinSize(300, 400);
        // Add event handler for adding points on the graph
        graphPane.setOnMouseClicked(this::handleMouseClick);
        graphPane.setStyle("-fx-background-color: white;"); // Set background color for visibility

        // graph methods section
        VBox bottomSection = new VBox();
        HBox graphAnalysisSection = new HBox(20);
        graphAnalysisSection.setPadding(padding);
        graphAnalysisSection.setAlignment(Pos.CENTER);

        Button isConnected = new Button("Is Connected?");

        Button hasCycles = new Button("Has Cycles?");

        Button depthFirst = new Button("Depth First Search");


        Button breadthFirst = new Button("Breadth First Search");

        // Results Section
        TextField analysisResults = new TextField();
        analysisResults.setEditable(false);
        analysisResults.setPrefWidth(100);
        VBox textBox = new VBox(analysisResults);
        textBox.setPadding(textPadding);

        depthFirst.setOnAction(e -> analysisResults.setText(handleDFSButton()));
        isConnected.setOnAction(e -> analysisResults.setText(handleConnectedButton()));
        breadthFirst.setOnAction(e -> analysisResults.setText((handleBfsButton())));
        hasCycles.setOnAction(e -> analysisResults.setText(handleCycleButton()));
        // Add two bottom sections together in Vbox

        graphAnalysisSection.getChildren().addAll(isConnected, hasCycles, depthFirst, breadthFirst);
        bottomSection.getChildren().addAll(graphAnalysisSection, textBox);

        // Create entire pane
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: lightblue;");
        root.setTop(AddEdgeSection);
        root.setCenter(graphPane);
        root.setBottom(bottomSection);


        Scene scene = new Scene(root, 600, 800);

        primaryStage.setScene(scene);
        primaryStage.setTitle("User-initiated Undirected Graph");
        primaryStage.show();

    }

    private void handleMouseClick(MouseEvent e) {
        graphPane.getChildren().add(Graph.addVertex(e.getX(), e.getY()));
        //vertices.add(new Vertex(e.getX(), e.getY(), nextLabel));
        addVertexLabel(e.getX(), e.getY());
        for (Vertex vertex : vertices) {
            System.out.println(vertex.getVertexName());
        }
    }

    private void addVertexLabel(double x, double y) {
        String currentLabel = Character.toString(nextLabel);
        Label label = new Label(currentLabel);

        // Set the position of the label at the mouse click coordinates
        label.setLayoutX(x + 0.5);
        label.setLayoutY(y + 0.5);

        // Add the label to the graph pane
        graphPane.getChildren().add(label);
        vertices.add(new Vertex(x, y, nextLabel, vertexNumber));
        graph.addVertexToList(vertexNumber);
        // Increment the nextLabel for the next point
        nextLabel++;
        vertexNumber++;
    }

    private void handleUserInput(TextField first, TextField second) {

        String firstInput = first.getText();
        char charInput1;
        char charInput2;

        String secondInput = second.getText();

        if(!firstInput.isEmpty() && !secondInput.isEmpty()) {
            charInput1 = firstInput.charAt(0);
            charInput2 = secondInput.charAt(0);

            System.out.println("The first character chosen is: "+ charInput1);
            System.out.println("The second character chosen is: "+ charInput2);

            Vertex vertex1 = searchVertexByLabel(vertices, charInput1);
            Vertex vertex2 = searchVertexByLabel(vertices, charInput2);
            assert vertex1 != null;
            assert vertex2 != null;

            System.out.println(vertex1.getVertexNumber());
            // Create the edge object for DFS


            // Add the edgeline to the graph
            Line edgeLine = Graph.addEdge(vertex1, vertex2);
            edgeLine.setStroke(Color.BLACK);
            graphPane.getChildren().add(edgeLine);
        } else {
            System.out.println("User input creates an error.");
        }
    }

    private static Vertex searchVertexByLabel(List<Vertex> objects, char vertexName) {
        for (Vertex obj : objects) {
            if(obj.getVertexName() == vertexName) {
                return obj;
            }
        }
        System.out.print("The point "+ vertexName +" does not exist.");
        return null;
    }

    private String handleDFSButton() {
        Integer[] resultText = graph.dfs(0).toArray(new Integer[0]);
        StringBuilder dfsResult = new StringBuilder();
        for (Integer result : resultText) {
            dfsResult.append(searchLabelByVertexNum(vertices, result));
        }

        return dfsResult.toString().trim();
    }

    private String searchLabelByVertexNum(List<Vertex> objects, int vertexNum) {
        StringBuilder dfsResult = new StringBuilder();
        for (Vertex obj : objects) {
            if(obj.getVertexNumber() == vertexNum) {
                dfsResult.append(obj.getVertexName());
            }
        }
        return dfsResult.toString();
    }

    private String handleConnectedButton() {
        return graph.isConnected(0);
    }

    private String handleBfsButton() {
        Integer[] resultText = graph.breadthFirstResults(0).toArray(new Integer[0]);
        StringBuilder bfsResult = new StringBuilder();
        for (Integer result : resultText) {
            bfsResult.append(searchLabelByVertexNum(vertices, result));
        }

        return bfsResult.toString().trim();
    }

    private String handleCycleButton() {
        if (graph.hasCycles()) {
            return "Graph has cycles";
        } else {
            return"Graph does not have cycles";
        }
    }
    public static void main(String[] args) {

        Application.launch(args);

    }
}