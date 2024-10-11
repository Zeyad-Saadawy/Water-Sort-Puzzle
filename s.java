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