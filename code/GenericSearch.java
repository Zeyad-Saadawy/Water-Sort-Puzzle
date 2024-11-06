package code;
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
    protected HashSet<String> generatedNodes = new HashSet<>(); // Set to track explored nodes
    protected HashSet<String> statesEntered = new HashSet<>();
    protected int expandedNodes; // Count of nodes expanded during search

    // Constructor
    public GenericSearch(Node root,String strategy) {
        this.root = root;
        this.explored = new HashSet<>(); // Initialize the explored set
        this.expandedNodes = 0;
        this.bfsQueue = new LinkedList<>(); // Initialize the BFS queue
        this.dfsStack = new Stack<>(); // Initialize the DFS stack
        this.priorityQueue = new PriorityQueue<>(new NodeComparator(strategy)); // Initialize the priority queue
    }


    

    // Method to add a node to the frontier based on the search strategy
    // Method to add a node to the frontier based on the search strategy
    protected void addToFrontier(Node node, String strategy) {
        statesEntered.add(node.getStateKey()); // Track states that have been entered
    
        // Calculate heuristics based on the strategy before adding to the frontier
        if (strategy.equals("GR1") || strategy.equals("AS1")) {
            node.heuristicValue = node.calculateH1(); // Calculate H1 for GR1 and AS1
        } else if (strategy.equals("GR2") || strategy.equals("AS2")) {
            node.heuristicValue = node.calculateH2(); // Calculate H2 for GR2 and AS2
        }
    
        // Add the node to the appropriate data structure based on the strategy
        switch (strategy) {
            case "BF":
                bfsQueue.add(node); // Add node to the queue for BFS
                break;
            case "DF":
                dfsStack.push(node); // Push node onto the stack for DFS
                break;
            case "UC": // Uniform Cost Search
            case "AS1": // A* Search with Heuristic 1
            case "AS2": // A* Search with Heuristic 2
            case "GR1": // Greedy Search with Heuristic 1
            case "GR2": // Greedy Search with Heuristic 2
                priorityQueue.add(node); // Add node to the priority queue
                break;
            case "ID":
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
            if (!generatedNodes.contains(child.getStateKey())) { // Check if the child's state has been explored
                generatedNodes.add(child.getStateKey());
                addToFrontier(child, strategy); // Add valid children to the frontier
            }
        }
    }
}
