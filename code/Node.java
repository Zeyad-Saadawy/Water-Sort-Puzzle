package code;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class Node {
    // Variables
    ArrayList<Bottle> state; // Represents the state in the search space
    Node parent; // Refers to the parent node in the search tree
    String operator; // The action taken to get to this node
    int depth; // Depth of the node in the search tree
    int pathCost; // Cost of the path from the initial state to this node
    int heuristicValue;
    // Constructor
    public Node(ArrayList<Bottle> state, Node parent, String operator, int depth, int pathCost , int heuristicValue) {
        this.state = state;
        this.parent = parent;
        this.operator = operator;
        this.depth = depth;
        this.pathCost = pathCost;
        this.heuristicValue = heuristicValue;
    }

    // Method to get children of the current node
public ArrayList<Node> getChildren() {
    ArrayList<Node> children = new ArrayList<>();

    for (int i = 0; i < state.size(); i++) {
        for (int j = 0; j < state.size(); j++) { // j should iterate over all bottles
            if (i != j && checkPour(state.get(i), state.get(j))) { // Ensure not pouring into itself and valid pour
                Node child = performPour(i, j); // Perform the pour operation and get the new child node
                if (child != null) {
                    children.add(child); // Add the valid child node
                }
            }
        }
    }
    return children;
}


    // Method to check if pouring is valid
    public boolean checkPour(Bottle source, Bottle target) {
        // Check if the source bottle is empty
        if (source.isEmpty()) {
            return false; // Cannot pour from an empty bottle
        }

        // Check if the target bottle is full
        if (target.isFull()) {
            return false; // Cannot pour into a full bottle
        }

        // Check if the topmost layers can be poured
        String topMostSource = source.getTopLayer();
        String topMostTarget = target.getTopLayer();

        // Target is empty or layers match
        return topMostTarget == null || topMostSource.equals(topMostTarget);
    }

    // Method to perform the pour operation
    private Node performPour(int sourceIndex, int targetIndex) {
        // Clone the current state
        ArrayList<Bottle> newState = new ArrayList<>();
        for (Bottle bottle : state) {
            newState.add(bottle.clone()); // Create a deep copy of each bottle
        }

        // Get the source and target bottles
        Bottle sourceBottle = newState.get(sourceIndex);
        Bottle targetBottle = newState.get(targetIndex);

        int cost = 0; // Initialize cost for this operation

        // Continue pouring while possible
        while (!sourceBottle.isEmpty() && !targetBottle.isFull()) {
            String topLayer = sourceBottle.getTopLayer(); // Get the top layer from the source

            // Check if the layer can be poured into the target bottle
            if (topLayer == null || topLayer.equals("e")) {
                break; // Stop pouring if the top layer is empty or invalid
            }

            // Check if we can pour this layer into the target bottle
            if (!checkPour(sourceBottle, targetBottle)) {
                break; // Stop pouring if the pour is no longer valid
            }

            // Pour the layer into the target bottle
            boolean added = targetBottle.addLayer(topLayer);
            if (added) {
                sourceBottle.removeLayer(); // Remove the layer from the source bottle
                cost++; // Increment cost for each successful pour
            } else {
                break; // Stop pouring if the target bottle can't take more layers
            }
        }

        // Only create a new node if at least one layer was poured
        if (cost > 0) {
            return new Node(newState, this, "pour_" + sourceIndex + "_" + targetIndex, this.depth + 1, pathCost + cost , heuristicValue);
        }

        return null; // Return null if no pour was performed
    }

    public boolean isGoal() {
        for (Bottle bottle : state) {
            if (!isUniformColor(bottle)) {
                return false; // Not all layers in the bottle are the same color
            }
        }
        
        if (!allBottlesDifferentColor()) {
            return false; // Not all bottles have different colors
        }

        

        return true; // All bottles have uniform color layers and are different colors
    }
     // Check if all bottles have different colors on their top layers
    public boolean allBottlesDifferentColor() {
        HashSet<String> topColors = new HashSet<>(); // Set to track unique colors

        for (Bottle bottle : state) {
            String topLayer = bottle.getTopLayer(); // Get the top layer color
            if (topLayer != null) { // Check if the bottle is not empty
                if (!topColors.add(topLayer)) {
                    return false; // Found a duplicate color
                }
            }
        }
        return true; // All bottles have different top colors
    }

    // Helper method to check if all layers in the bottle are the same color
    public static boolean isUniformColor(Bottle bottle) {
        // Clone the bottle to avoid modifying the original
        Bottle bottleClone = bottle.clone();

        // Remove leading empty spaces ('e') before checking colors
        while (!bottleClone.layers.isEmpty() && bottleClone.layers.peek().equals("e")) {
            bottleClone.layers.pop(); // Remove leading empty layers
        }

        // Check if the bottle has no layers left after removing empty spaces
        if (bottleClone.layers.isEmpty()) {
            return true;
        }

        // Get the top layer
        String topLayer = bottleClone.getTopLayer();

        // Check if all layers are the same color as the top layer
        return bottleClone.layers.stream().allMatch(layer -> layer.equals(topLayer) || layer.equals("e"));
    }

    public String getStateKey() {
        StringBuilder stateKey = new StringBuilder();
        for (Bottle bottle : state) {
            // Append layers of the bottle in a specific format (e.g., "r,g,y")
            stateKey.append(String.join(",", bottle.layers));
            stateKey.append(";"); // Separator for different bottles
        }
        return stateKey.toString(); // Return the unique state representation
    }

    public  Node getClone(){
        ArrayList<Bottle> newBottles = new ArrayList<>();
        for(Bottle b : state){
            newBottles.add(b.clone());
        }
        return new Node(newBottles,null,"",0,0,0);
    }
    
    // Calculate heuristic values based on the strategy
    public void calculateHeuristics(String strategy) {
        if (strategy.equals("GR1") || strategy.equals("AS1")) {
            this.heuristicValue = calculateH1();  // Set heuristic value to H1 for GR1 and AS1
        } else if (strategy.equals("GR2") || strategy.equals("AS2")) {
            this.heuristicValue = calculateH2();  // Set heuristic value to H2 for GR2 and AS2
        }
    }
 // **NEW** Calculate heuristic 1: Number of non-uniform bottles
    public int calculateH1() {
        int nonUniformBottles = 0;
        for (Bottle bottle : state) {
            if (!bottle.isUniform()) {
                nonUniformBottles++;
            }
        }
        return nonUniformBottles;
    }

    // **NEW** Calculate heuristic 2: Number of out-of-place layers
    public int calculateH2() {
        int outOfPlaceLayers = 0;
        for (Bottle bottle : state) {
            if (!bottle.isUniform()) {
                outOfPlaceLayers += bottle.countOutOfPlaceLayers();  // Helper method to count misplaced layers
            }
        }
        return outOfPlaceLayers;
    }
    
}
