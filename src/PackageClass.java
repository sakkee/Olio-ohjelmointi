public class PackageClass {
    
    //Package includes Item, Priority, SmartPostStart, SmartPostDestination and distance (between the SmartPosts)
    
    public ItemClass Item = null;
    public PriorityClass Priority = null;
    public SmartPostClass SmartPostStart = null;
    public SmartPostClass SmartPostDestination = null;
    public double distance = 0;
    
    public PackageClass (ItemClass ItemClass, PriorityClass PriorityClass, SmartPostClass SmartPostClassStart, SmartPostClass SmartPostClassDestination, double distanceClass) {
        Item = ItemClass;
        Priority = PriorityClass;
        SmartPostStart = SmartPostClassStart;
        SmartPostDestination = SmartPostClassDestination;
        distance = distanceClass;
    }   
}
