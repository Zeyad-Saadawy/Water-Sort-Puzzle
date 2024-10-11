import java.util.PriorityQueue;
import java.util.Stack;
import java.util.ArrayList;
import java.util.LinkedList;

public class WaterSortSearch extends GenericSearch {
    public WaterSortSearch(Node root) {
        super(root);
    }

    // Static method to initialize a Node from an initialState string
    public static Node initializeNode(String initialState) {
        // Split the initial state by semicolons
        String[] parts = initialState.split(";");

        // Get number of bottles and bottle capacity
        int numberOfBottles = Integer.parseInt(parts[0]);
        int bottleCapacity = Integer.parseInt(parts[1]);

        // Set max capacity for bottles
        Bottle.setMaxCapacity(bottleCapacity);

        // Create bottles based on the provided colors
        ArrayList<Bottle> bottles = new ArrayList<>();
        for (int i = 2; i < parts.length; i++) { // Start from index 2 for bottle layers
            String[] colors = parts[i].split(","); // Split colors by commas
            Bottle bottle = new Bottle(""); // Create an empty bottle

            // Add each layer to the bottle
            for (String color : colors) {
                bottle.addLayer(color);
            }
            bottles.add(bottle); // Add bottle to the list
        }

        // Create and return a new Node with the parsed state
        return new Node(bottles, null, "initial", 0, 0);
    }

    @Override
    public String solve(String strategy) {
        switch (strategy) {
            case "BFS":
                return breadthFirstSearch();
            case "DFS":
                return depthFirstSearch();
            case "UCS":
                return uniformCostSearch();
            case "Greedy":
                return greedySearch();
            case "A*":
                return aStarSearch();
            case "IDS":
                return iterativeDeepeningSearch();
            default:
                throw new IllegalArgumentException("Invalid search strategy: " + strategy);
        }
    }

    // Implement BFS
    private String breadthFirstSearch() {
        bfsQueue = new LinkedList<>(); // Initialize the bfsQueue for BFS
        addToFrontier(root, "BFS"); // Start with the root node

        String plan = "";
        while (!bfsQueue.isEmpty()) {
            Node currentNode = bfsQueue.poll(); // Get the next node to expand

            if (currentNode.isGoal()) {
                plan = buildPlan(currentNode); // Build the plan from the goal node
                return plan + ";" + currentNode.pathCost + ";" + expandedNodes; // Return the result
            }

            // Expand the current node
            expandNode(currentNode, "BFS");
        }

        return "NOSOLUTION"; // No solution found
    }

    // Implement DFS
    private String depthFirstSearch() {
        dfsStack = new Stack<>(); // Initialize the stack for DFS
        addToFrontier(root, "DFS"); // Start with the root node

        String plan = "";
        while (!dfsStack.isEmpty()) {
            Node currentNode = dfsStack.pop(); // Get the next node to expand

            if (currentNode.isGoal()) {
                plan = buildPlan(currentNode); // Build the plan from the goal node
                return plan + ";" + currentNode.pathCost + ";" + expandedNodes; // Return the result
            }

            // Expand the current node
            expandNode(currentNode, "DFS");
        }

        return "NOSOLUTION"; // No solution found
    }

    // Implement UCS
    private String uniformCostSearch() {
        priorityQueue = new PriorityQueue<>(new NodeComparator()); // Initialize the priority queue
        addToFrontier(root, "UCS"); // Start with the root node

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll(); // Get the node with the lowest cost

            if (currentNode.isGoal()) {
                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes; // Return the result
            }

            // Expand the current node
            expandNode(currentNode, "UCS");
        }

        return "NOSOLUTION"; // No solution found
    }

    // Implement Greedy Search
    private String greedySearch() {
        priorityQueue = new PriorityQueue<>(new NodeComparator()); // Initialize the priority queue
        addToFrontier(root, "Greedy"); // Start with the root node

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll(); // Get the node with the lowest heuristic value

            if (currentNode.isGoal()) {
                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes; // Return the result
            }

            // Expand the current node
            expandNode(currentNode, "Greedy");
        }

        return "NOSOLUTION"; // No solution found
    }

    // Implement A* Search
    private String aStarSearch() {
        priorityQueue = new PriorityQueue<>(new NodeComparator()); // Initialize the priority queue
        addToFrontier(root, "A*"); // Start with the root node

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll(); // Get the node with the lowest f(n) value

            if (currentNode.isGoal()) {
                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes; // Return the result
            }

            // Expand the current node
            expandNode(currentNode, "A*");
        }

        return "NOSOLUTION"; // No solution found
    }

    // Implement Iterative Deepening Search
    private String iterativeDeepeningSearch() {
        // Iterative deepening requires depth-limited DFS
        int depthLimit = 0;

        while (true) {
            String result = depthLimitedDFS(root, depthLimit);
            if (!result.equals("NOSOLUTION")) {
                return result; // Return the result if found
            }
            depthLimit++; // Increase the depth limit
        }
    }

    // Depth-limited DFS implementation
    private String depthLimitedDFS(Node node, int limit) {
        if (node.isGoal()) {
            return buildPlan(node) + ";" + node.pathCost + ";" + expandedNodes; // Return the result if goal is reached
        }

        if (limit == 0) {
            return "NOSOLUTION"; // Return no solution if depth limit is reached
        }

        expandNode(node, "DFS"); // Expand the node to explore children
        for (Node child : node.getChildren()) {
            String result = depthLimitedDFS(child, limit - 1);
            if (!result.equals("NOSOLUTION")) {
                return result; // Return the result if a solution is found
            }
        }

        return "NOSOLUTION"; // No solution found at this depth
    }

    // Method to build the action plan from the goal node to the root
    private String buildPlan(Node goalNode) {
        StringBuilder planBuilder = new StringBuilder();
        Node currentNode = goalNode;

        // Traverse back to the root to build the plan
        while (currentNode.parent != null) {
            planBuilder.insert(0, currentNode.operator + ","); // Insert at the beginning
            currentNode = currentNode.parent; // Move to parent node
        }

        // Remove the trailing comma, if present
        if (planBuilder.length() > 0) {
            planBuilder.setLength(planBuilder.length() - 1); // Remove last comma
        }

        return planBuilder.toString(); // Return the constructed plan
    }
}
