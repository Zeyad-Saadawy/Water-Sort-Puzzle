package code;
import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {
    private final String strategy; // Store the strategy type (UCS, Greedy, A*)

    // Constructor to initialize the strategy
    public NodeComparator(String strategy) {
        this.strategy = strategy;
    }

    @Override
    public int compare(Node n1, Node n2) {
        switch (strategy) {
            case "UC":
                // For UCS, compare based on path cost (g(n))
                return Integer.compare(n1.pathCost, n2.pathCost);

            case "GR1": // Greedy Search with Heuristic 1
            case "GR2": // Greedy Search with Heuristic 2
                // For Greedy Search, compare based on heuristic value (h(n))
                return Integer.compare(n1.heuristicValue, n2.heuristicValue);

            case "AS1": // A* Search with Heuristic 1
            case "AS2": // A* Search with Heuristic 2
                // For A*, compare based on f(n) = g(n) + h(n)
                int f1 = n1.pathCost + n1.heuristicValue; // Total cost for n1
                int f2 = n2.pathCost + n2.heuristicValue; // Total cost for n2
                return Integer.compare(f1, f2);

            default:
                throw new IllegalArgumentException("Invalid search strategy: " + strategy);
        }
    }
}

//Purpose: Comparator for comparing nodes based on specific criteria
// (path cost for UCS, heuristic values for Greedy and A*).