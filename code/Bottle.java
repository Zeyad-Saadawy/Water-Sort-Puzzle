package code;
import java.util.Stack;

public class Bottle {
    static int MaxCapacity; // Maximum capacity for all bottles
    Stack<String> layers; // Stack to hold the layers of colors in the bottle

    // Constructor
    public Bottle(String colors) {
        layers = new Stack<>();

        // Reverse the string so that the first color (top) is added last to the stack
        for (int i = colors.length() - 1; i >= 0; i--) {
            String colorStr = String.valueOf(colors.charAt(i)); // Convert char to String
            addLayer(colorStr); // Add the color or empty space
        }

        // Fill the rest of the bottle with empty spaces if it isn't full
        fillWithEmptySpaces();
    }

    // Static method to set the MaxCapacity for all bottles
    public static void setMaxCapacity(int capacity) {
        MaxCapacity = capacity;
    }

    // Add a layer to the bottle
    public boolean addLayer(String color) {
        // If we're adding 'e', just return true without affecting capacity
        if (color.equals("e")) {
            fillWithEmptySpaces();
            return true; // Empty layer added successfully
        }

        // Check if the color is valid
        if (!isValidColor(color)) {
            System.out.println("Invalid color: " + color);
            return false; // Invalid color, not added
        }

        // Remove any leading empty spaces before adding a color
        while (!layers.isEmpty() && layers.peek().equals("e")) {
            layers.pop(); // Remove 'e' to make space for the color
        }

        // Check if the bottle is full
        if (getNonEmptyCapacity() == MaxCapacity) {
            System.out.println("Bottle is full!");
            return false; // Layer not added because the bottle is full
        }

        // Add the color to the bottle
        layers.push(color);
        fillWithEmptySpaces(); // Ensure empty spaces are added after the new layer
        return true; // Layer was successfully added
    }

    // Remove a layer from the bottle
    public String removeLayer() {
        // Ignore empty layers
        while (!layers.isEmpty() && layers.peek().equals("e")) {
            layers.pop();
        }

        if (!layers.isEmpty()) {
            String layer = layers.pop(); // Remove the topmost non-empty layer
            fillWithEmptySpaces(); // Ensure the bottle maintains its empty spaces
            return layer;
        }

        return null; // Return null if no layers are present
    }

    // Fill the remaining space in the bottle with empty layers ('e')
    private void fillWithEmptySpaces() {
        while (layers.size() < MaxCapacity) {
            layers.push("e");
        }
    }

    // Clone method to create a deep copy of the Bottle
    @Override
    public Bottle clone() {
        // Create a new empty bottle with no layers
        Bottle clonedBottle = new Bottle("");

        // Clone each layer using addLayer, including empty spaces
        for (String layer : layers) {
            clonedBottle.addLayer(layer); // Use addLayer to add each layer to the cloned bottle
        }

        return clonedBottle; // Return the new cloned bottle
    }

    // Validate if the color is one of the allowed colors (r, g, b, y, o)
    private boolean isValidColor(String color) {
        return color.equals("r") || color.equals("g") || color.equals("b") ||
                color.equals("y") || color.equals("o");
    }

    // Check if the bottle is full (taking into account both colors and empty
    // spaces)
    public boolean isFull() {
        return getNonEmptyCapacity() >= MaxCapacity;
    }

    // Check if the bottle is empty (contains only empty spaces)
    public boolean isEmpty() {
        // A bottle is empty if all layers are 'e'
        return getNonEmptyCapacity() == 0;
    }

    // Get the current top layer of the bottle, ignoring empty spaces
    public String getTopLayer() {
        if (isEmpty()) {
            return null;
        }
        // Remove any leading empty spaces before adding a color
        while (!layers.isEmpty() && layers.peek().equals("e")) {
            layers.pop(); // Remove 'e' to make space for the color
        }
        String layer = layers.peek();
        fillWithEmptySpaces();
        return layer;

    }

    // Get the current number of non-empty layers
    public int getNonEmptyCapacity() {
        // Count non-empty layers (ignoring 'e')
        return (int) layers.stream().filter(layer -> !layer.equals("e")).count();
    }
    
    // Static method to reset MaxCapacity to a default value
    public static void resetMaxCapacity() {
      MaxCapacity = 0; // Set to a default value, or you can choose any value that makes sense
  }
 // **NEW** Count the number of layers that are not matching the top color
    public int countOutOfPlaceLayers() {
        if (layers.isEmpty()) return 0;
        String topColor = layers.peek();
        int count = 0;
        for (String layer : layers) {
            if (!layer.equals(topColor)) {
                count++;
            }
        }
        return count;
    }
 // Check if all layers in the bottle are uniform (excluding empty spaces)
    public boolean isUniform() {
        // If the bottle is empty or has only empty spaces, consider it uniform
        if (isEmpty()) return true;

        // Get the color of the top layer, ignoring empty spaces
        String topLayer = getTopLayer();
        if (topLayer == null) return true; // If all layers are empty, return uniform

        // Check if all visible layers are the same color
        for (String layer : layers) {
            if (!layer.equals("e") && !layer.equals(topLayer)) {
                return false; // Found a different color
            }
        }

        return true; // All layers are uniform, excluding empty spaces
    }
    
}
