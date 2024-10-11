import java.util.List;
import java.util.ArrayList;

public class Bottle {
    private List<String> layers;  // List representing the colors in the bottle

    // Constructor
    public Bottle(List<String> layers) {
        this.layers = new ArrayList<>(layers);  // Deep copy of the layers list
    }

    // Getter for layers
    public List<String> getLayers() {
        return layers;
    }

    // Check if all layers are the same color (except for empty layers, if any)
    public boolean isSingleColored() {
        String color = null;
        for (String layer : layers) {
            if (!layer.equals("e")) {  // Assuming 'e' represents an empty layer
                if (color == null) {
                    color = layer;
                } else if (!color.equals(layer)) {
                    return false;  // Found a different color
                }
            }
        }
        return true;  // All non-empty layers have the same color
    }

    // Method to count the number of mismatched layers in this bottle
    public int getMismatchedLayers() {
        String color = null;
        int mismatches = 0;
        for (String layer : layers) {
            if (!layer.equals("e")) {  // Ignore empty layers
                if (color == null) {
                    color = layer;
                } else if (!color.equals(layer)) {
                    mismatches++;  // Count mismatches if layers have different colors
                }
            }
        }
        return mismatches;
    }

    // Override equals and hashCode methods for state comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bottle bottle = (Bottle) o;
        return layers.equals(bottle.layers);
    }

    @Override
    public int hashCode() {
        return layers.hashCode();
    }

    @Override
    public String toString() {
        return layers.toString();
    }
}
