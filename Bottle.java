import java.util.Stack;

public class Bottle {
    static int MaxCapacity; // Maximum capacity for all bottles
    Stack<String> layers;   // Stack to hold the layers of colors in the bottle
    int currentCapacity;    // Current number of layers in the bottle

    // Constructor
    public Bottle(String colors) {
        layers = new Stack<>();
        currentCapacity = 0; // Initially, the bottle is empty

        // Fill the bottle with the provided colors from the string
        for (char color : colors.toCharArray()) {
            String colorStr = String.valueOf(color); // Convert char to String
            if (!addLayer(colorStr)) {
                // If the layer could not be added (either empty or invalid color), skip it
                continue;
            }
        }
    }

    // Static method to set the MaxCapacity for all bottles
    public static void setMaxCapacity(int capacity) {
        MaxCapacity = capacity;
    }

    // Add a layer to the bottle
    public boolean addLayer(String color) {
        // Check if it's an empty layer ('e')
        if (color.equals("e")) {
            // Empty layer, do not add
            return false; // Skip adding empty layer
        }

        // Check if the color is valid
        if (!isValidColor(color)) {
            System.out.println("Invalid color: " + color);
            return false; // Invalid color, not added
        }

        // Check if the bottle is full
        if (currentCapacity < MaxCapacity) {
            layers.push(color);
            currentCapacity++; // Increment the current capacity when a layer is added
            return true; // Layer was successfully added
        } else {
            System.out.println("Bottle is full!");
            return false; // Layer not added because the bottle is full
        }
    }

    // Remove a layer from the bottle
    public String removeLayer() {
        if (!layers.isEmpty()) {
            String layer = layers.pop(); // Remove the top layer from the stack
            currentCapacity = layers.size(); // Update the current capacity
            return layer;
        }
        return null; // Return null if no layers are present
    }

    // Clone method to create a deep copy of the Bottle
    @Override
    public Bottle clone() {
        // Create a new empty bottle with no layers
        Bottle clonedBottle = new Bottle("");

        // Clone each layer using addLayer
        for (String layer : layers) {
            clonedBottle.addLayer(layer); // Use addLayer to add each layer to the cloned bottle
        }

        // Set the current capacity to match the original bottle
        clonedBottle.currentCapacity = this.currentCapacity;

        return clonedBottle; // Return the new cloned bottle
    }

    // Validate if the color is one of the allowed colors (r, g, b, y, o)
    private boolean isValidColor(String color) {
        return color.equals("r") || color.equals("g") || color.equals("b") ||
                color.equals("y") || color.equals("o");
    }

    // Check if the bottle is full
    public boolean isFull() {
        return currentCapacity == MaxCapacity;
    }

    // Check if the bottle is empty
    public boolean isEmpty() {
        return layers.isEmpty();
    }

    // Get the current top layer of the bottle
    public String getTopLayer() {
        if (!layers.isEmpty()) {
            return layers.peek(); // Return the topmost layer
        } else {
            return null; // Return null if the bottle is empty
        }
    }
}
