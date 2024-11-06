package code;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class WaterSortSearch extends GenericSearch {

    public WaterSortSearch(Node root,String strategy) {
        super(root,strategy);
    }

    public static String solve(String initialState, String strategy, boolean visualize) {
        Node start = initializeNode(initialState);

        WaterSortSearch wss = new WaterSortSearch(start,strategy);

        switch (strategy) {
            case "BF":
                return wss.breadthFirstSearch(visualize);
            case "DF":
                return wss.depthFirstSearch(visualize);
            case "UC":
                return wss.uniformCostSearch(visualize);
            case "GR1":
                return wss.greedySearchH1(visualize);
            case "GR2":
                return wss.greedySearchH2(visualize);
            case "AS1":
                return wss.aStarSearchH1(visualize);
            case "AS2":
                return wss.aStarSearchH2(visualize);
            case "ID":
                return wss.iterativeDeepeningSearch(visualize);
            default:
                throw new IllegalArgumentException("Invalid search strategy: " + strategy);
        }
    }

    public static Node initializeNode(String initialState) {
        Bottle.resetMaxCapacity();
        String[] parts = initialState.split(";");
        int numberOfBottles = Integer.parseInt(parts[0]);
        int bottleCapacity = Integer.parseInt(parts[1]);
        Bottle.setMaxCapacity(bottleCapacity);

        ArrayList<Bottle> bottles = new ArrayList<>();
        for (int i = 2; i < parts.length; i++) {
            String colors = parts[i].replace(",", "");
            Bottle bottle = new Bottle(colors);
            bottles.add(bottle);
        }

        return new Node(bottles, null, "initial", 0, 0,0);
    }


    private String breadthFirstSearch(boolean visualize) {
        Runtime runtime = Runtime.getRuntime();

        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory before BFS: " + memoryBefore + " bytes");

        bfsQueue.clear();
        addToFrontier(root, "BF");

        while (!bfsQueue.isEmpty()) {
            Node currentNode = bfsQueue.poll();

            if (visualize) {
                visualizeState(currentNode);
            }
            if (currentNode.isGoal()) {
                long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("Memory after BFS: " + memoryAfter + " bytes");

                long memoryUsed = memoryAfter - memoryBefore;
                System.out.println("BFS - Memory used: " + memoryUsed + " bytes\n");

                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes;
            }

            expandNode(currentNode, "BF");
        }

        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory after BFS (No solution): " + memoryAfter + " bytes");

        long memoryUsed = memoryAfter - memoryBefore;
        System.out.println("BFS - Memory used (No solution): " + memoryUsed + " bytes\n");

        return "NOSOLUTION";
    }


    private String depthFirstSearch(boolean visualize) {
        Runtime runtime = Runtime.getRuntime();

        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory before DFS: " + memoryBefore + " bytes");

        dfsStack.clear();
        addToFrontier(root, "DF");

        while (!dfsStack.isEmpty()) {
            Node currentNode = dfsStack.pop();

            if (visualize) {
                visualizeState(currentNode);
            }

            if (currentNode.isGoal()) {
                long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("Memory after DFS: " + memoryAfter + " bytes");

                long memoryUsed = memoryAfter - memoryBefore;
                System.out.println("DFS - Memory used: " + memoryUsed + " bytes\n");

                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes; // Return the result
            }

            expandNode(currentNode, "DF");
        }

        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory after DFS (No solution): " + memoryAfter + " bytes");

        long memoryUsed = memoryAfter - memoryBefore;
        System.out.println("DFS - Memory used (No solution): " + memoryUsed + " bytes\n");

        return "NOSOLUTION";
    }

    private String uniformCostSearch(boolean visualize) {
        Runtime runtime = Runtime.getRuntime();

        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory before UCS: " + memoryBefore + " bytes");

        priorityQueue.clear();
        addToFrontier(root, "UC");

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();

            if (visualize) {
                visualizeState(currentNode);
            }

            if (currentNode.isGoal()) {
                long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("Memory after UCS: " + memoryAfter + " bytes");

                long memoryUsed = memoryAfter - memoryBefore;
                System.out.println("UCS - Memory used: " + memoryUsed + " bytes\n");

                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes;
            }

            expandNode(currentNode, "UC");
        }

        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory after UCS (No solution): " + memoryAfter + " bytes");

        long memoryUsed = memoryAfter - memoryBefore;
        System.out.println("UCS - Memory used (No solution): " + memoryUsed + " bytes\n");

        return "NOSOLUTION";
    }




    private String iterativeDeepeningSearch(boolean visualize) {
        Runtime runtime = Runtime.getRuntime();

        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory before IDS: " + memoryBefore + " bytes");

        int depthLimit = 0;
        dfsStack.push(root);

        while (!dfsStack.isEmpty()) {
            Node node = dfsStack.pop();

            if (node.isGoal()) {
                long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("Memory after IDS: " + memoryAfter + " bytes");

                long memoryUsed = memoryAfter - memoryBefore;
                System.out.println("IDS - Memory used: " + memoryUsed + " bytes\n");

                return buildPlan(node) + ";" + node.pathCost + ";" + expandedNodes;
            }

            if (node.depth == depthLimit && dfsStack.isEmpty()) {
                Node clonedRoot = root.getClone();
                explored.clear();
                generatedNodes.clear();
                statesEntered.clear();
                dfsStack.add(clonedRoot);
                depthLimit++;
                continue;
            }

            if (node.depth != depthLimit) {
                expandNode(node, "DF");
            }

            if (dfsStack.isEmpty() && !checkSatesEntered()) {
                Node clonedRoot = root.getClone();
                explored.clear();
                generatedNodes.clear();
                statesEntered.clear();
                dfsStack.add(clonedRoot);
                depthLimit++;
            }
        }

        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory after IDS (No solution): " + memoryAfter + " bytes");

        long memoryUsed = memoryAfter - memoryBefore;
        System.out.println("IDS - Memory used (No solution): " + memoryUsed + " bytes\n");

        return "NOSOLUTION";
    }

    public boolean checkSatesEntered(){
        for (String bottles : statesEntered){
            if(!explored.contains(bottles))
                return false;
        }
        return true;
    }


    private void visualizeState(Node node) {
        System.out.println("Current State:");
        for (Bottle bottle : node.state) {
            System.out.println(bottle.layers);
        }
        System.out.println("----------------");
    }

    private String buildPlan(Node goalNode) {
        StringBuilder planBuilder = new StringBuilder();
        Node currentNode = goalNode;

        while (currentNode.parent != null) {
            planBuilder.insert(0, currentNode.operator + ",");
            currentNode = currentNode.parent;
        }

        if (planBuilder.length() > 0) {
            planBuilder.setLength(planBuilder.length() - 1);
        }

        return planBuilder.toString();
    }


    private String greedySearchH1(boolean visualize) {
        Runtime runtime = Runtime.getRuntime();

        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory before Greedy H1: " + memoryBefore + " bytes");

        priorityQueue.clear();
        addToFrontier(root, "GR1");

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();

            if (visualize) {
                visualizeState(currentNode);
            }

            if (currentNode.isGoal()) {
                long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("Memory after Greedy H1: " + memoryAfter + " bytes");

                long memoryUsed = memoryAfter - memoryBefore;
                System.out.println("Greedy H1 - Memory used: " + memoryUsed + " bytes\n");

                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes;
            }

            expandNode(currentNode, "GR1");
        }

        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory after Greedy H1 (No solution): " + memoryAfter + " bytes");

        long memoryUsed = memoryAfter - memoryBefore;
        System.out.println("Greedy H1 - Memory used (No solution): " + memoryUsed + " bytes\n");

        return "NOSOLUTION";
    }

    private String greedySearchH2(boolean visualize) {
        Runtime runtime = Runtime.getRuntime();

        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory before Greedy H2: " + memoryBefore + " bytes");

        priorityQueue.clear();
        addToFrontier(root, "GR2");

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();

            if (visualize) {
                visualizeState(currentNode);
            }

            if (currentNode.isGoal()) {
                long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("Memory after Greedy H2: " + memoryAfter + " bytes");

                long memoryUsed = memoryAfter - memoryBefore;
                System.out.println("Greedy H2 - Memory used: " + memoryUsed + " bytes\n");

                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes;
            }

            expandNode(currentNode, "GR2");
        }

        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory after Greedy H2 (No solution): " + memoryAfter + " bytes");

        long memoryUsed = memoryAfter - memoryBefore;
        System.out.println("Greedy H2 - Memory used (No solution): " + memoryUsed + " bytes\n");

        return "NOSOLUTION";
    }


    private String aStarSearchH1(boolean visualize) {
        Runtime runtime = Runtime.getRuntime();

        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory before A* H1: " + memoryBefore + " bytes");

        priorityQueue.clear();
        addToFrontier(root, "AS1");

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();

            if (visualize) {
                visualizeState(currentNode);
            }

            if (currentNode.isGoal()) {
                long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("Memory after A* H1: " + memoryAfter + " bytes");

                long memoryUsed = memoryAfter - memoryBefore;
                System.out.println("A* H1 - Memory used: " + memoryUsed + " bytes\n");

                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes;
            }

            expandNode(currentNode, "AS1");
        }

        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory after A* H1 (No solution): " + memoryAfter + " bytes");

        long memoryUsed = memoryAfter - memoryBefore;
        System.out.println("A* H1 - Memory used (No solution): " + memoryUsed + " bytes\n");

        return "NOSOLUTION";
    }


    private String aStarSearchH2(boolean visualize) {
        Runtime runtime = Runtime.getRuntime();

        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory before A* H2: " + memoryBefore + " bytes");

        priorityQueue.clear();
        addToFrontier(root, "AS2");

        while (!priorityQueue.isEmpty()) {
            Node currentNode = priorityQueue.poll();

            if (visualize) {
                visualizeState(currentNode);
            }

            if (currentNode.isGoal()) {
                long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("Memory after A* H2: " + memoryAfter + " bytes");

                long memoryUsed = memoryAfter - memoryBefore;
                System.out.println("A* H2 - Memory used: " + memoryUsed + " bytes\n");

                return buildPlan(currentNode) + ";" + currentNode.pathCost + ";" + expandedNodes;
            }

            expandNode(currentNode, "AS2");
        }

        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory after A* H2 (No solution): " + memoryAfter + " bytes");

        long memoryUsed = memoryAfter - memoryBefore;
        System.out.println("A* H2 - Memory used (No solution): " + memoryUsed + " bytes\n");

        return "NOSOLUTION";
    }

    public static void main(String[] args) {

        String initialState = "5;4;" + "b,y,r,b;" + "b,y,r,r;" +
                "y,r,b,y;" + "e,e,e,e;" + "e,e,e,e;";

        String strategy = "AS1"; // You can change this to "DFS", "UCS", "Greedy", "A*", or "IDS"

        // Set visualize to true or false to control visualization output
        boolean visualize = false;

        // Call the solve method on the instance
        String result = WaterSortSearch.solve(initialState, strategy, visualize);

        // Print the result
        System.out.println("Result: " + result);
    }

    public static long getUsedMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();
    }

    public static void measureMemoryUsage(String algorithmName, Runnable algorithm) {
        // Record memory usage before
        long memoryBefore = getUsedMemory();
        System.out.println(algorithmName + " - Memory before execution: " + memoryBefore + " bytes");

        // Run the algorithm
        algorithm.run();

        // Record memory usage after
        long memoryAfter = getUsedMemory();
        System.out.println(algorithmName + " - Memory after execution: " + memoryAfter + " bytes");

        // Calculate and print memory consumed
        long memoryUsed = memoryAfter - memoryBefore;
        System.out.println(algorithmName + " - Memory used: " + memoryUsed + " bytes\n");
    }

}
