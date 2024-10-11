import java.util.*;
import java.util.function.Function;

// Abstract class for generic search algorithms
public abstract class GenericSearch {
    protected int nodesExpanded = 0; // Keeps track of the number of nodes expanded during the search

    // Abstract method to be implemented by subclasses (e.g., WaterSortSearch)
    public abstract Node search(Node initialState);

    // Reconstruct the solution path
    protected List<String> reconstructPath(Node node) {
        List<String> path = new ArrayList<>();
        while (node.getParent() != null) {
            path.add(0, node.getOperator());  // Add action to the path
            node = node.getParent();
        }
        return path;
    }

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
                return node;  // Return the goal node if found
            }

            explored.add(node.getState());

            for (Node child : node.getChildren()) {
                if (!containsState(explored, child.getState()) && !containsNode(frontier, child)) {
                    frontier.add(child);
                }
            }
        }

        return null;  // No solution found
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
                return node;
            }

            explored.add(node.getState());

            for (Node child : node.getChildren()) {
                if (!containsState(explored, child.getState()) && !containsNode(frontier, child)) {
                    frontier.push(child);
                }
            }
        }

        return null;
    }

    // Iterative deepening search
    protected Node iterativeDeepeningSearch(Node initialState) {
        for (int depth = 0; ; depth++) {
            Node result = depthLimitedSearch(initialState, depth);
            if (result != null) {
                return result;  // Return if a solution is found within depth
            }
        }
    }

    private Node depthLimitedSearch(Node node, int limit) {
        if (node.isGoalState()) return node;
        else if (limit == 0) return null;

        for (Node child : node.getChildren()) {
            Node result = depthLimitedSearch(child, limit - 1);
            if (result != null && result.isGoalState()) return result;
        }

        return null;
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
                return node;
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

        return null;
    }

    // Greedy search implementation with heuristic
    protected Node greedySearch(Node initialState, Function<Node, Integer> heuristic) {
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(heuristic::apply));
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

    // A* search implementation with heuristic
    protected Node aStarSearch(Node initialState, Function<Node, Integer> heuristic) {
        PriorityQueue<Node> frontier = new PriorityQueue<>(Comparator.comparingInt(node -> node.getPathCost() + heuristic.apply(node)));
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
