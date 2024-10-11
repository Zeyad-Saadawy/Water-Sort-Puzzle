import java.util.*;

// Abstract class that defines a generic search framework
public abstract class GenericSearch {
    protected int nodesExpanded = 0; // Keeps track of the number of nodes expanded during the search

    // Abstract method that needs to be implemented by the specific problem subclass (like WaterSortSearch)
    public abstract Node search(Node initialState);

    // Reconstructs the path from the solution node to the initial state by following the parent references
    protected List<String> reconstructPath(Node node) {
        List<String> path = new ArrayList<>();
        while (node.getParent() != null) {
            path.add(0, node.getOperator());  // Adds the action (operator) that led to this node
            node = node.getParent();
        }
        return path;
    }

    // Getter for the number of nodes expanded during the search
    public int getNodesExpanded() {
        return nodesExpanded;
    }

    // Breadth-first search implementation
    protected Node breadthFirstSearch(Node initialState) {
        Queue<Node> frontier = new LinkedList<>();
        Set<List<Bottle>> explored = new HashSet<>();
        frontier.add(initialState);

        while (!frontier.isEmpty()) {
            Node node = frontier.poll();
            nodesExpanded++;

            if (node.isGoalState()) {
                return node;  // Return the goal node if the goal is reached
            }

            explored.add(node.getState());

            for (Node child : node.getChildren()) {
                if (!containsState(explored, child.getState()) && !containsNode(frontier, child)) {
                    frontier.add(child);
                }
            }
        }

        return null;  // Return null if no solution is found
    }

    // Depth-first search implementation
    protected Node depthFirstSearch(Node initialState) {
        Stack<Node> frontier = new Stack<>();
        Set<List<Bottle>> explored = new HashSet<>();
        frontier.push(initialState);

        while (!frontier.isEmpty()) {
            Node node = frontier.pop();
            nodesExpanded++;

            if (node.isGoalState()) {
                return node;  // Return the goal node if the goal is reached
            }

            explored.add(node.getState());

            for (Node child : node.getChildren()) {
                if (!containsState(explored, child.getState()) && !containsNode(frontier, child)) {
                    frontier.push(child);
                }
            }
        }

        return null;  // Return null if no solution is found
    }

    // Iterative deepening search implementation
    protected Node iterativeDeepeningSearch(Node initialState) {
        for (int depth = 0; ; depth++) {
            Node result = depthLimitedSearch(initialState, depth);
            if (result != null) {
                return result;  // Return the result if goal is found within depth limit
            }
        }
    }

    // Depth-limited search used by iterative deepening
    private Node depthLimitedSearch(Node node, int limit) {
        if (node.isGoalState()) return node;
        else if (limit == 0) return null;

        boolean cutoff = false;
        for (Node child : node.getChildren()) {
            Node result = depthLimitedSearch(child, limit - 1);
            if (result == null) cutoff = true;
            else if (result.isGoalState()) return result;
        }

        return cutoff ? null : null;
    }

    // Uniform cost search implementation
    protected Node uniformCostSearch(Node initialState) {
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(Node::getPathCost));
        Set<List<Bottle>> explored = new HashSet<>();
        frontier.add(initialState);

        while (!frontier.isEmpty()) {
            Node node = frontier.poll();
            nodesExpanded++;

            if (node.isGoalState()) {
                return node;  // Return the goal node if the goal is reached
            }

            explored.add(node.getState());

            for (Node child : node.getChildren()) {
                if (!containsState(explored, child.getState()) && !containsNode(frontier, child)) {
                    frontier.add(child);
                } else if (frontier.contains(child) && child.getPathCost() < node.getPathCost()) {
                    frontier.remove(child);
                    frontier.add(child);
                }
            }
        }

        return null;  // Return null if no solution is found
    }

    // Greedy search with two heuristics
    protected Node greedySearch(Node initialState, int heuristicType) {
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(node -> heuristic(node, heuristicType)));
        Set<List<Bottle>> explored = new HashSet<>();
        frontier.add(initialState);

        while (!frontier.isEmpty()) {
            Node node = frontier.poll();
            nodesExpanded++;

            if (node.isGoalState()) {
                return node;
            }

            explored.add(node.getState());

            for (Node child : node.getChildren()) {
                if (!containsState(explored, child.getState()) && !containsNode(frontier, child)) {
                    frontier.add(child);
                }
            }
        }

        return null;
    }

    // A* search with two heuristics
    protected Node aStarSearch(Node initialState, int heuristicType) {
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(node -> node.getPathCost() + heuristic(node, heuristicType)));
        Set<List<Bottle>> explored = new HashSet<>();
        frontier.add(initialState);

        while (!frontier.isEmpty()) {
            Node node = frontier.poll();
            nodesExpanded++;

            if (node.isGoalState()) {
                return node;
            }

            explored.add(node.getState());

            for (Node child : node.getChildren()) {
                if (!containsState(explored, child.getState()) && !containsNode(frontier, child)) {
                    frontier.add(child);
                }
            }
        }

        return null;
    }

    // Heuristic function that selects between two different heuristic types
    protected int heuristic(Node node, int heuristicType) {
        if (heuristicType == 1) {
            return heuristicOne(node);
        } else if (heuristicType == 2) {
            return heuristicTwo(node);
        } else {
            throw new IllegalArgumentException("Unknown heuristic type: " + heuristicType);
        }
    }

    // First heuristic function
    protected int heuristicOne(Node node) {
        // Example heuristic: Number of bottles with more than one color
        int heuristicValue = 0;
        for (Bottle bottle : node.getState()) {
            if (!bottle.isSingleColored()) {
                heuristicValue++;
            }
        }
        return heuristicValue;
    }

    // Second heuristic function
    protected int heuristicTwo(Node node) {
        // Example heuristic: Total number of mismatched layers
        int heuristicValue = 0;
        for (Bottle bottle : node.getState()) {
            heuristicValue += bottle.getMismatchedLayers();
        }
        return heuristicValue;
    }

    // Helper method to check if a set of explored states contains a specific state
    private boolean containsState(Set<List<Bottle>> explored, List<Bottle> state) {
        for (List<Bottle> s : explored) {
            if (statesEqual(s, state)) {
                return true;
            }
        }
        return false;
    }

    // Helper method to check if a collection of nodes contains a node with a specific state
    private boolean containsNode(Collection<Node> frontier, Node child) {
        for (Node node : frontier) {
            if (statesEqual(node.getState(), child.getState())) {
                return true;
            }
        }
        return false;
    }

    // Helper method to compare two states (which are lists of Bottle objects)
    private boolean statesEqual(List<Bottle> state1, List<Bottle> state2) {
        if (state1.size() != state2.size()) return false;
        for (int i = 0; i < state1.size(); i++) {
            if (!state1.get(i).equals(state2.get(i))) {
                return false;
            }
        }
        return true;
    }
}
