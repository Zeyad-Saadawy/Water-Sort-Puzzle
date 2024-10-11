import java.util.PriorityQueue;
import java.util.Stack;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class WaterSortSearch extends GenericSearch {
    
    public WaterSortSearch(Node root) {
        super(root);
    }

    // Solve method that takes initialState, strategy, and visualize flag
    public String solve(String initialState, String strategy, boolean visualize) {
        // Initialize the root node based on the initialState
        Node initialNode = initializeNode(initialState);
        
        // Set the root for this search instance
        this.root = initialNode; 
        
        // Call the appropriate search method based on the strategy
        switch (strategy) {
            case "BF":
                return breadthFirstSearch(visualize);
            case "DF":
                return depthFirstSearch(visualize);
            case "UC":
                return uniformCostSearch(visualize);
            case "Greedy":
                return greedySearch(visualize);
            case "A*":
                return aStarSearch(visualize);
            case "ID":
                return iterativeDeepeningSearch(visualize);
            default:
                throw new IllegalArgumentException("Invalid search strategy: " + strategy);
        }
    }

    // Static method to initialize a Node from an initialState string
    public static Node initializeNode(String initialState) {
        String[] parts = initialState.split(";");
        int numberOfBottles = Integer.parseInt(parts[0]);
        int bottleCapacity = Integer.parseInt(parts[1]);
        Bottle.setMaxCapacity(bottleCapacity);

        ArrayList<Bottle> bottles = new ArrayList<>();
        for (int i = 2; i < parts.length; i++) {
            String[] colors = parts[i].split(",");
            Bottle bottle = new Bottle(""); // Create a new Bottle
            for (String color : colors) {
                bottle.addLayer(color); // Add each color layer
            }
            bottles.add(bottle); // Add the bottle to the list
        }

        return new Node(bottles, null, "initial", 0, 0); // Create and return the initial Node
    }

    // Breadth-First Search with visualization option
    private String breadthFirstSearch(boolean visualize) {
        bfsQueue.clear(); // Ensure the queue is clear before use
        addToFrontier(root, "BF"); // Add the initial node to the queue

        while (!bfsQueue.isEmpty()) {
            Node currentNode = bfsQueue.poll();
            
            if (visualize) {
                visualizeState(currentNode); // Visualize the current state
            }

            if (currentNode.isGoal()) {
                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes; // Return the result
            }

            expandNode(currentNode, "BF"); // Expand the current node
        }

        return "NOSOLUTION"; // Return if no solution is found
    }

    // Depth-First Search with visualization option
    private String depthFirstSearch(boolean visualize) {
        dfsStack.clear(); // Ensure the stack is clear before use
        addToFrontier(root, "DF"); // Add the initial node to the stack

        while (!dfsStack.isEmpty()) {
            Node currentNode = dfsStack.pop();
            
            if (visualize) {
                visualizeState(currentNode); // Visualize the current state
            }

            if (currentNode.isGoal()) {
                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes; // Return the result
            }

            expandNode(currentNode, "DF"); // Expand the current node
        }

        return "NOSOLUTION"; // Return if no solution is found
    }

    // Uniform Cost Search with visualization option
    private String uniformCostSearch(boolean visualize) {
        priorityQueue.clear(); // Ensure the priority queue is clear before use
        addToFrontier(root, "UC"); // Add the initial node to the priority queue

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            
            if (visualize) {
                visualizeState(currentNode); // Visualize the current state
            }

            if (currentNode.isGoal()) {
                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes; // Return the result
            }

            expandNode(currentNode, "UC"); // Expand the current node
        }

        return "NOSOLUTION"; // Return if no solution is found
    }

    // Greedy Search with visualization option
    private String greedySearch(boolean visualize) {
        priorityQueue.clear(); // Ensure the priority queue is clear before use
        addToFrontier(root, "GR"); // Add the initial node to the priority queue

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            
            if (visualize) {
                visualizeState(currentNode); // Visualize the current state
            }

            if (currentNode.isGoal()) {
                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes; // Return the result
            }

            expandNode(currentNode, "Greedy"); // Expand the current node
        }

        return "NOSOLUTION"; // Return if no solution is found
    }

    // A* Search with visualization option
    private String aStarSearch(boolean visualize) {
        priorityQueue.clear(); // Ensure the priority queue is clear before use
        addToFrontier(root, "AS"); // Add the initial node to the priority queue

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();
            
            if (visualize) {
                visualizeState(currentNode); // Visualize the current state
            }

            if (currentNode.isGoal()) {
                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes; // Return the result
            }

            expandNode(currentNode, "A*"); // Expand the current node
        }

        return "NOSOLUTION"; // Return if no solution is found
    }

    // Iterative Deepening Search with visualization option
    private String iterativeDeepeningSearch(boolean visualize) {
        int depthLimit = 0;
        while (true) {
            String result = depthLimitedDFS(root, depthLimit, visualize);
            if (!result.equals("NOSOLUTION")) {
                return result; // Return if a solution is found
            }
            depthLimit++; // Increase the depth limit
        }
    }

    // Depth-limited DFS with visualization option
    private String depthLimitedDFS(Node node, int limit, boolean visualize) {
        if (node.isGoal()) {
            return buildPlan(node) + ";" + node.pathCost + ";" + expandedNodes; // Return the result if goal is reached
        }
        if (limit == 0) {
            return "NOSOLUTION"; // Return no solution if depth limit is reached
        }

        expandNode(node, "DF"); // Expand the current node

        for (Node child : node.getChildren()) {
            if (!isExplored(child)) {
                String result = depthLimitedDFS(child, limit - 1, visualize);
                if (!result.equals("NOSOLUTION")) {
                    return result; // Return if a solution is found
                }
            }
        }
        return "NOSOLUTION"; // No solution found at this depth
    }

    // Visualize the state (print the current node's state)
    private void visualizeState(Node node) {
        System.out.println("Current State:");
        for (Bottle bottle : node.state) {
            System.out.println(bottle.layers); // Print the layers of each bottle
        }
        System.out.println("----------------");
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

    // Other methods (expandNode, addToFrontier, etc.) should be defined in GenericSearch or here...
}
