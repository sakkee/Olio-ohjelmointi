public class PriorityClass {
    int priority;
    int maxDistance;
    boolean fragilePost;
    int maxSizeWidth;
    int maxSizeHeight;
    int maxSizeDepth;
    double maxWeight;
    int duration;
    
    public PriorityClass (int classPriority, int distance, boolean fragility, int width, int height, int depth, double weight, int classDuration) {
        priority = classPriority;
        maxDistance = distance;
        fragilePost = fragility;
        maxSizeWidth = width;
        maxSizeHeight = height;
        maxSizeDepth = depth;
        maxWeight = weight;
        duration = classDuration;
    }
}
