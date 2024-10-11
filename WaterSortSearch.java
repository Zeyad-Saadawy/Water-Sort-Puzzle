import java.util.*;

public class WaterSortSearch extends GenericSearch {
    private static final Map<Character, String> COLOR_MAP = Map.of(
        'r', "red",
        'g', "green",
        'b', "blue",
        'y', "yellow",
        'o', "orange"
    );

    public static String solve(String initialState, String strategy, boolean visualize) {
        List<Bottle> bottles = parseInitialState(initialState);  // Updated to return List<Bottle>
        Node initialNode = new Node(bottles, null, null, 0);

        WaterSortSearch searcher = new WaterSortSearch();
        Node goalNode = searcher.search(initialNode, strategy);

        if (goalNode == null) {
            return "NOSOLUTION";
        }

        // Reconstruct the solution plan and path cost
        List<String> plan = searcher.reconstructPath(goalNode);
        int pathCost = goalNode.getPathCost();

        // Visualize the solution if requested
        if (visualize) {
            visualizeSolution(initialNode, plan);
        }

        // Return the plan, path cost, and number of nodes expanded
        return String.join(",", plan) + ";" + pathCost + ";" + searcher.getNodesExpanded();
    }

    // Parse the initial state from a string into a list of Bottle objects
    private static List<Bottle> parseInitialState(String initialState) {
        String[] parts = initialState.split(";");
        int numberOfBottles = Integer.parseInt(parts[0]);
        int bottleCapacity = Integer.parseInt(parts[1]);

        List<Bottle> bottles = new ArrayList<>();
        for (int i = 2; i < parts.length; i++) {
            String[] layers = parts[i].split(",");
            List<String> colors = new ArrayList<>(Arrays.asList(layers));
            bottles.add(new Bottle(colors));
        }
        return bottles;
    }

    // Visualize the solution by printing out each step
    private static void visualizeSolution(Node initialNode, List<String> plan) {
        System.out.println("Initial state:");
        printState(initialNode);

        Node currentNode = initialNode;
        for (String action : plan) {
            System.out.println("Action: " + action);
            currentNode = applyAction(currentNode, action);  // Apply the action to get the next state
            printState(currentNode);
        }
    }

    // Print the state of the bottles
    private static void printState(Node node) {
        for (Bottle bottle : node.getState()) {
            System.out.println(bottle);
        }
    }

    @Override
    public Node search(Node initialState) {
        return search(initialState, "BF");  // Default to breadth-first search
    }

    // Search using the specified strategy
    public Node search(Node initialState, String strategy) {
        switch (strategy) {
            case "BF":
                return breadthFirstSearch(initialState);
            case "DF":
                return depthFirstSearch(initialState);
            case "ID":
                return iterativeDeepeningSearch(initialState);
            case "UC":
                return uniformCostSearch(initialState);
            case "GR1":
                return greedySearch(initialState, this::heuristic1);
            case "GR2":
                return greedySearch(initialState, this::heuristic2);
            case "AS1":
                return aStarSearch(initialState, this::heuristic1);
            case "AS2":
                return aStarSearch(initialState, this::heuristic2);
            default:
                throw new IllegalArgumentException("Unknown strategy: " + strategy);
        }
    }

    // Heuristic 1: Count the number of misplaced colors
    private int heuristic1(Node node) {
        int misplacedColors = 0;
        for (Bottle bottle : node.getState()) {
            if (!bottle.isSingleColored()) {
                misplacedColors += bottle.getMismatchedLayers();  // Using the getMismatchedLayers from Bottle.java
            }
        }
        return misplacedColors;
    }

    // Heuristic 2: Count the number of non-uniform bottles
    private int heuristic2(Node node) {
        int nonUniformBottles = 0;
        for (Bottle bottle : node.getState()) {
            if (!bottle.isSingleColored()) {
                nonUniformBottles++;
            }
        }
        return nonUniformBottles;
    }

    // Apply an action to a node and return the resulting node
    private static Node applyAction(Node currentNode, String action) {
        // Extract the pour action details (e.g., "pour_0_1" means pour from bottle 0 to bottle 1)
        String[] actionParts = action.split("_");
        int fromBottle = Integer.parseInt(actionParts[1]);
        int toBottle = Integer.parseInt(actionParts[2]);

        // Copy the current state to create the new state
        List<Bottle> newState = new ArrayList<>();
        for (Bottle bottle : currentNode.getState()) {
            newState.add(new Bottle(bottle.getLayers()));  // Deep copy of each bottle
        }

        // Perform the pour action
        Bottle from = newState.get(fromBottle);
        Bottle to = newState.get(toBottle);
        from.pourTo(to);  // Assuming you have a pourTo method in the Bottle class

        // Return a new node representing the resulting state after the action
        return new Node(newState, currentNode, action, currentNode.getPathCost() + 1);
    }
}
