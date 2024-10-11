import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.LinkedList;

public abstract class GenericSearch {
    protected Node root; // The root node of the search tree
    protected Queue<Node> bfsQueue; // Queue for BFS
    protected Stack<Node> dfsStack; // Stack for DFS
    protected PriorityQueue<Node> priorityQueue; // Priority queue for UCS, Greedy, and A*
    protected HashSet<String> explored; // Set to track explored nodes
    protected int expandedNodes; // Count of nodes expanded during search

    // Constructor
    public GenericSearch(Node root) {
        this.root = root;
        this.explored = new HashSet<>(); // Initialize the explored set
        this.expandedNodes = 0;
        this.bfsQueue = new LinkedList<>(); // Initialize the BFS queue
        this.dfsStack = new Stack<>(); // Initialize the DFS stack
        this.priorityQueue = new PriorityQueue<>(new NodeComparator()); // Initialize the priority queue
    }

    // Abstract method for solving the search problem
    public abstract String solve(String initialState, String strategy, boolean visualize);

    // Method to check if a node's state has already been explored
    protected boolean isExplored(Node node) {
        return explored.contains(node); // Check if the node has been explored
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

    // Method to clear the explored set and frontiers for a new search
    protected void reset() {
        explored.clear(); // Clear explored nodes
        expandedNodes = 0; // Reset the expanded nodes count
        bfsQueue.clear(); // Clear the BFS queue
        dfsStack.clear(); // Clear the DFS stack
        priorityQueue.clear(); // Clear the priority queue
    }

    // Method to expand a node and add its children to the frontier
    protected void expandNode(Node node, String strategy) {
        expandedNodes++; // Increment the count of expanded nodes
        explored.add(node.getStateKey()); // Mark the node's state key as explored
    
        for (Node child : node.getChildren()) {
            if (!explored.contains(child.getStateKey())) { // Check if the child's state has been explored
                addToFrontier(child, strategy); // Add valid children to the frontier
            }
        }
    }
    
}
