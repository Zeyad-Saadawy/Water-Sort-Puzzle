import java.util.List;

public class Node {
    private List<Bottle> state;  // List of bottles representing the current state
    private Node parent;
    private String operator;  // The action that led to this node
    private int pathCost;     // The cost to reach this node

    // Constructor
    public Node(List<Bottle> state, Node parent, String operator, int pathCost) {
        this.state = state;
        this.parent = parent;
        this.operator = operator;
        this.pathCost = pathCost;
    }

    // Getter for state
    public List<Bottle> getState() {
        return state;
    }

    // Getter for parent node
    public Node getParent() {
        return parent;
    }

    // Getter for the action/operator that led to this node
    public String getOperator() {
        return operator;
    }

    // Getter for path cost
    public int getPathCost() {
        return pathCost;
    }

    // Method to check if the current node represents a goal state
    public boolean isGoalState() {
        // Implement your logic to check if the state is a goal state
        return false; // Replace with actual goal check logic
    }

    // Method to generate children of this node (i.e., possible next states)
    public List<Node> getChildren() {
        // Implement logic to generate and return child nodes
        return null;  // Replace with actual child generation logic
    }
}
