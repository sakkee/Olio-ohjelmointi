import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;


public class SmartPostReader {
    
    //SmartPostList contains the data of each SmartPost dispenser
    public ArrayList<SmartPostClass> SmartPostList = new ArrayList();
    
    private String fileNameSmartPosts = "fi_apt.xml";
    
    //Declaring the class constructor
    static private SmartPostReader SPR = null;
    
    private SmartPostReader() throws Exception {
        //Does XML_Reader function when the class is constructed
        XML_Reader();
    }
    
    //Checks if the class has already been constructed, and won't construct it the 2nd time if it isn't null
    static public SmartPostReader getInstance() throws Exception {
        if (SPR==null)
            SPR = new SmartPostReader();
        return SPR;
    }
    
    //Function for reading the XML data
    private void XML_Reader() throws Exception {
        
        //Opens the filereader
        BufferedReader fileIn;
        fileIn = new BufferedReader(new FileReader(fileNameSmartPosts));
        String inputLine;
        
        //Default variables for dispenser variables
        int Code = 0;
        String City = null;
        String Address = null;
        String Availability = null;
        String PostOffice = null;
        float Lat = 0;
        float Lng = 0;
        
        //Reads the XML, adds data to variables
        while ((inputLine = fileIn.readLine()) != null) {
            
            //Removes extra whitespace from both sides of inputLine
            inputLine = inputLine.trim();
            
            //Starts to analyze the XML data, adding data to variables
            if (inputLine.contains("<code>")) {
                inputLine = inputLine.substring("<code>".length(), inputLine.length()-"</code>".length());
                Code = Integer.parseInt(inputLine);
            }
            else if (inputLine.contains("<city>")) {
                inputLine = inputLine.substring("<city>".length(), inputLine.length()-"</city>".length());
                City = inputLine;
            }
            else if (inputLine.contains("<address>")) {
                inputLine = inputLine.substring("<address>".length(), inputLine.length()-"</address>".length());
                Address = inputLine;
            }
            else if (inputLine.contains("<availability>")) {
                inputLine = inputLine.substring("<availability>".length(), inputLine.length()-"</availability>".length());
                Availability = inputLine;
            }
            else if (inputLine.contains("<postoffice>")) {
                inputLine = inputLine.substring("<postoffice>".length(), inputLine.length()-"</postoffice>".length());
                PostOffice = inputLine;
            }
            else if (inputLine.contains("<lat>")) { 
                inputLine = inputLine.substring("<lat>".length(), inputLine.length()-"</lat>".length());
                Lat = Float.parseFloat(inputLine);
            }
            else if (inputLine.contains("<lng>")) {
                inputLine = inputLine.substring("<lng>".length(), inputLine.length()-"</lng>".length());
                Lng = Float.parseFloat(inputLine);
            }
            else if (inputLine.contains("</place>")) {
                //Adding constructed SmartPostClass items to SmartPostList, using the read variables
                SmartPostList.add(new SmartPostClass(Code, City, Address, Availability, PostOffice, Lat, Lng));
            }
        }
    }
}
