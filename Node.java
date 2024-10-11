import java.util.ArrayList;

public class Node {
    // Variables
    ArrayList<Bottle> state; // Represents the state in the search space
    Node parent; // Refers to the parent node in the search tree
    String operator; // The action taken to get to this node
    int depth; // Depth of the node in the search tree
    int pathCost; // Cost of the path from the initial state to this node

    // Constructor
    public Node(ArrayList<Bottle> state, Node parent, String operator, int depth, int pathCost) {
        this.state = state;
        this.parent = parent;
        this.operator = operator;
        this.depth = depth;
        this.pathCost = pathCost;
    }

    // Method to get children of the current node
    public ArrayList<Node> getChildren() {
        ArrayList<Node> children = new ArrayList<>();

        for (int i = 0; i < state.size(); i++) {
            for (int j = 0; j < state.size(); j++) { // j should iterate over all bottles, not just up to MaxCapacity
                if (i != j && checkPour(state.get(i), state.get(j))) { // Ensure not pouring into itself
                    ArrayList<Bottle> newState = new ArrayList<>();

                    // Clone the current state
                    for (Bottle bottle : state) {
                        newState.add(bottle.clone());
                    }

                    // Execute the pour action
                    int cost = 0;
                    String topLayer = newState.get(i).removeLayer(); // Remove top layer from source bottle
                    if (topLayer != null) {
                        newState.get(j).addLayer(topLayer); // Add it to the target bottle
                        cost++; // Increment cost for the action
                    }

                    // Create a new node for the child
                    Node child = new Node(newState, this, "pour_" + i + "_" + j, this.depth + 1, pathCost + cost);
                    children.add(child);
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

    // Check if this node represents a goal state
    public boolean isGoal() {
        for (Bottle bottle : state) {
            if (!isUniformColor(bottle)) {
                return false; // Not all layers in the bottle are the same color
            }
        }
        return true; // All bottles have uniform color layers
    }

    // Helper method to check if all layers in the bottle are the same color
    private boolean isUniformColor(Bottle bottle) {
        String topLayer = bottle.getTopLayer();
        return topLayer != null && bottle.layers.stream().allMatch(layer -> layer.equals(topLayer));
    }
}
