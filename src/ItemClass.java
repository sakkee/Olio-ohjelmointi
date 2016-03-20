public class ItemClass {
    public String itemName;
    public int itemSizeWidth;
    public int itemSizeHeight;
    public int itemSizeDepth;
    public boolean itemFragility;
    public double itemWeight;
    
    public ItemClass (String name, int width, int height, int depth, boolean fragility, double weight) {
        itemName = name;
        itemSizeWidth = width;
        itemSizeHeight = height;
        itemSizeDepth = depth;
        itemFragility = fragility;
        itemWeight = weight;
    }
}