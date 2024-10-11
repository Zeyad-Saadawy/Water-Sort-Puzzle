import java.util.Stack;

public class Bottle {
    static int MaxCapacity;
    Stack<String> layers;
    int currentCapacity;

    // Constructor
    public Bottle(String colors) {
        layers = new Stack<>();
        currentCapacity = 0; // Initially, the bottle is empty

        // Fill the bottle with the provided colors from the string
        for (char color : colors.toCharArray()) {
            String colorStr = String.valueOf(color); // Convert char to String
            addLayer(colorStr); // Add the layer to the bottle
        }
    }

    // Static method to set the MaxCapacity for all bottles
    public static void setMaxCapacity(int capacity) {
        MaxCapacity = capacity;
    }

    // Add a layer to the bottle
    public boolean addLayer(String color) {
        // Check if the color is valid (not empty and one of the allowed colors)
        if (color.equals("e")) {
            System.out.println("Empty layer");
            return false; // Ignore empty layer
        }
        if (!isValidColor(color)) {
            System.out.println("Invalid color: " + color);
            return false; // Invalid color
        }
        if (currentCapacity < MaxCapacity) {
            layers.push(color);
            currentCapacity++; // Increment the current capacity when a layer is added
            return true; // Layer was successfully added
        } else {
            System.out.println("Bottle is full!");
            return false; // Layer was not added because the bottle is full
        }
    }

    // Remove a layer from the bottle
    public String removeLayer() {
        if (!layers.isEmpty()) {
            String layer = layers.pop();
            currentCapacity = layers.size();
            return layer;
        }
        return null;
    }

   // Clone method
   @Override
   public Bottle clone() {
       // Create a new empty bottle
       Bottle clonedBottle = new Bottle("");

       // Clone each layer using addLayer
       for (String layer : layers) {
           clonedBottle.addLayer(layer); // Use addLayer to add each layer to the cloned bottle
       }

       // Set the current capacity
       clonedBottle.currentCapacity = this.currentCapacity;

       return clonedBottle; // Return the new cloned bottle
   }

    // Method to validate if the color is one of the allowed colors
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
            return layers.peek();
        } else {
            return null; // Return null if the bottle is empty
        }
    }
}