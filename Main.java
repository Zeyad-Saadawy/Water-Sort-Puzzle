public class Main {
    public static void main(String[] args) {
        // Define an initial state for the water sort puzzle
        // Example: "3;4;r,g,y;e;e,y,b;"
        // This represents:
        // 3 bottles, each with a maximum capacity of 4 layers
        // Bottle 0 has layers: red (r), green (g), yellow (y)
        // Bottle 1 has layers: empty (e), empty (e), yellow (y), blue (b)
        String initialState = "3;4;r,y,r,y;y,r,y,r;e,e,e,e;";
        
        // Specify the search strategy
        String strategy = "BF"; // You can change this to "DFS", "UCS", "Greedy", "A*", or "IDS"
        
        // Set visualize to true or false to control visualization output
        boolean visualize = true;

        // Initialize the root node based on the initial state
        Node initialNode = WaterSortSearch.initializeNode(initialState);
        
        // Create an instance of WaterSortSearch
        WaterSortSearch search = new WaterSortSearch(initialNode);
        
        // Call the solve method on the instance
        String result = search.solve(initialState, strategy, visualize);

        // Print the result
        System.out.println("Result: " + result);
    }
}
