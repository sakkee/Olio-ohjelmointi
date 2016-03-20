public class SmartPostClass {
    
    // Variables for each SmartPost dispenser
    public int smartpostCode;
    public String smartpostCity;
    public String smartpostAddress;
    public String smartpostAvailability;
    public String smartpostPostOffice;
    public GeoPointClass GeoPoint = null;
    
    //Constructor for SmartPost dispensers
    public SmartPostClass (int Code, String City, String Address, String Availability, String PostOffice, Float Lat, Float Lng) {
        smartpostCode = Code;
        smartpostCity = City;
        smartpostAddress = Address;
        smartpostAvailability = Availability;
        smartpostPostOffice = PostOffice;
        //GeoPoint is the geographical location of the SmartPost dispenser
        GeoPoint = new GeoPointClass(Lat, Lng);
    }
}
