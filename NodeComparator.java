import java.util.Comparator;

class NodeComparator implements Comparator<Node> {
    @Override
    public int compare(Node n1, Node n2) {
        // Define your comparison logic based on the search strategy
        // For UCS, you might compare pathCost; for A*, compare f(n) = g(n) + h(n)
        return Integer.compare(n1.pathCost, n2.pathCost); // Example for UCS
    }
}
//Purpose: Comparator for comparing nodes based on specific criteria
// (path cost for UCS, heuristic values for Greedy and A*).