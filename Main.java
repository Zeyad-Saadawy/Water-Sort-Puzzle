public class Main {
    public static void main(String[] args) {
        // Example initial state: "3;4;r,g,y;e;e,y,b;"
        String initialState = "3;4;r,g,y;e;e,y,b;";

        // Initialize the Node using the WaterSortSearch class
        Node initialNode = WaterSortSearch.initializeNode(initialState);

        // Create an instance of WaterSortSearch and call solve with a chosen strategy
        WaterSortSearch search = new WaterSortSearch(initialNode);
        String solution = search.solve("BFS");

        // Print the result
        System.out.println(solution);
    }
}
