import java.util.*;

public abstract class GenericSearch {
    protected Node root; // The root node of the search tree
    protected Queue<Node> bfsQueue; // Queue for BFS
    protected Stack<Node> dfsStack; // Stack for DFS
    protected PriorityQueue<Node> priorityQueue; // Priority queue for UCS, Greedy, and A*
    protected int expandedNodes; // Count of nodes expanded during search

    // Constructor
    public GenericSearch(Node root) {
        this.root = root;
        this.expandedNodes = 0;
    }

    // Abstract method for solving the search problem
    public abstract String  solve(String strategy);

    // Method to check if a node's state has already been explored
    protected boolean isExplored(Node node) {
        // Implement logic to check if the node's state has already been explored
        // This could involve maintaining a set of explored nodes
        return false; // Placeholder implementation
    }

    // Method to add a node to the frontier based on the search strategy
    protected void addToFrontier(Node node, String strategy) {
        switch (strategy) {
            case "BFS":
                bfsQueue.add(node); // Add node to the queue for BFS
                break;
            case "DFS":
                dfsStack.push(node); // Push node onto the stack for DFS
                break;
            case "UCS":
            case "A*":
            case "Greedy":
                priorityQueue.add(node); // Add node to the priority queue
                break;
            case "IDS":
                dfsStack.push(node); // Push node onto the stack for IDS
                break;
            default:
                throw new IllegalArgumentException("Invalid search strategy: " + strategy);
        }
    }

    // Method to expand a node and add its children to the frontier
    protected void expandNode(Node node, String strategy) {
        expandedNodes++; // Increment the count of expanded nodes
        for (Node child : node.getChildren()) {
            if (!isExplored(child)) {
                addToFrontier(child, strategy); // Add valid children to the frontier
            }
        }
    }
}
