import java.util.List;
import java.util.Stack;

public class Bottle {
    private Stack<String> layers;  // Stack of layers in the bottle, with the top layer at the top of the stack
    private int capacity;          // Maximum number of layers the bottle can hold

    // Constructor
    public Bottle(List<String> layers) {
        this.capacity = layers.size();  // Assume the capacity is the size of the initial layers list
        this.layers = new Stack<>();
        for (String layer : layers) {
            if (!layer.equals("e")) {  // 'e' represents an empty layer, which should be ignored
                this.layers.push(layer);  // Push non-empty layers into the bottle
            }
        }
    }

    // Getter for the layers
    public List<String> getLayers() {
        return layers;
    }

    // Method to check if the bottle is single-colored (i.e., all layers are the same color)
    public boolean isSingleColored() {
        if (layers.isEmpty()) return true;  // An empty bottle is considered single-colored
        String color = layers.peek();
        for (String layer : layers) {
            if (!layer.equals(color)) {
                return false;  // If any layer differs, it's not single-colored
            }
        }
        return true;
    }

    // Method to get the number of mismatched layers
    public int getMismatchedLayers() {
        if (layers.isEmpty()) return 0;
        String color = layers.peek();
        int mismatches = 0;
        for (String layer : layers) {
            if (!layer.equals(color)) {
                mismatches++;
            }
        }
        return mismatches;
    }

    // Method to check if the bottle has space for more layers
    public boolean hasSpace() {
        return layers.size() < capacity;
    }

    // Method to get the top layer's color
    public String getTopLayer() {
        return layers.isEmpty() ? null : layers.peek();
    }

    // Method to pour liquid from this bottle to another bottle
    public void pourTo(Bottle targetBottle) {
        // Check if the target bottle has space and can accept the liquid
        if (!targetBottle.hasSpace() || this.layers.isEmpty()) {
            return;  // Cannot pour if target bottle is full or this bottle is empty
        }

        String topLayer = this.layers.peek();
        String targetTopLayer = targetBottle.getTopLayer();

        // If target bottle is empty or has the same top layer color, we can pour
        if (targetTopLayer == null || targetTopLayer.equals(topLayer)) {
            // Pour as much as possible of the top layer
            while (!this.layers.isEmpty() && this.layers.peek().equals(topLayer) && targetBottle.hasSpace()) {
                targetBottle.layers.push(this.layers.pop());  // Move the top layer to the target bottle
            }
        }
    }

    // Override equals to compare bottles by their layers
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bottle bottle = (Bottle) o;
        return layers.equals(bottle.layers);
    }

    // Override hashCode to ensure bottles can be used in hash sets or maps
    @Override
    public int hashCode() {
        return layers.hashCode();
    }

    @Override
    public String toString() {
        return layers.toString();
    }
}
