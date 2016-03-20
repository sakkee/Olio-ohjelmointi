/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author SAKKEE
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StorageClass {
    //Declaring ArrayLists that are used
    public ArrayList<ItemClass> ItemList = new ArrayList();
    public ArrayList<PriorityClass> PriorityList = new ArrayList();
    public ArrayList<PackageClass> PackageList = new ArrayList();
    //PackageHistoryList tracks all the packages created, not only the remaining ones, during the session
    public ArrayList<PackageClass> PackageHistoryList = new ArrayList();
    
    //Declaring names of the files
    private String fileNameItems = "itemlist.txt";
    private String fileNamePriorities = "classlist.txt";
    private String fileNameRecipe = "kuitti.txt";
    private String fileNameOfHistoryList = "varastolista.txt";
    
    //Counter to calculate the amount of packets
    static private int packetCount = 0;
    public static int getPacketCount() {
        return packetCount;
    }
    
    static private StorageClass StorageClass = null;
    
    private StorageClass() throws Exception {
        //Does XML_Reader function when the class is constructed
        addItemsFromFile();
        addPrioritiesFromFile();
    }
    
    //Checks if the class has already been constructed, and won't construct it the 2nd time if it isn't null
    static public StorageClass getInstance() throws Exception {
        if (StorageClass==null)
            StorageClass = new StorageClass();
        return StorageClass;
    }
    
    //Add Item to ItemList and write it to the ItemList file
    public void addItemToItemList(String name, int width, int height, int depth, boolean fragility, double weight) throws IOException {
        ItemList.add(new ItemClass(name, width, height, depth, fragility, weight));
        String s;
        if (fragility) {
            s = String.format(Locale.US, "\nITEM\nName: %s\nSize: %dx%dx%d\nFragile: True\nWeight: %.1f", name, width, height, depth, weight);
        }
        else {
            s = String.format(Locale.US, "\nITEM\nName: %s\nSize: %dx%dx%d\nFragile: False\nWeight: %.1f", name, width, height, depth, weight);
        }
        
        try (FileWriter filewriter = new FileWriter(fileNameItems,true)) {
            filewriter.write(s);
            filewriter.close();
        }
    };
    
    //Reads Priorities File to push Priority List
    private void addPrioritiesFromFile() {
        try {
            //Opens the priorities file
            BufferedReader fileIn;
            fileIn = new BufferedReader(new FileReader(fileNamePriorities));
            String inputLine;
            
            //Declares default variables to Priority Class
            int priority = 0;
            int maxDistance = 0;
            boolean fragilePost = false;
            int maxSizeWidth = 0;
            int maxSizeHeight = 0;
            int maxSizeDepth = 0;
            double maxWeight = 0;
            int duration = 0;
            
            String[] stringParts;
            
            //Reads the File and adds the values to the default values
            while ((inputLine = fileIn.readLine()) != null) {
                if (inputLine.contains("Priority:")) {
                    stringParts = inputLine.split(":");
                    priority = Integer.parseInt(stringParts[1].trim());
                }
                else if (inputLine.contains("Max Distance (kms):")) {
                    stringParts = inputLine.split(":");
                    maxDistance = Integer.parseInt(stringParts[1].trim());
                }
                else if (inputLine.contains("Fragile Allowed:")) {
                    stringParts = inputLine.split(":");
                    if (stringParts[1].contains("True")) {
                        fragilePost = true;
                    }
                    else if (stringParts[1].contains("False")) {
                        fragilePost = false;
                    }
                }
                else if (inputLine.contains("Max Size (cms):")) {
                    stringParts = inputLine.split(":");
                    stringParts = stringParts[1].trim().split("x");
                    maxSizeWidth = Integer.parseInt(stringParts[0]);
                    maxSizeHeight = Integer.parseInt(stringParts[1]);
                    maxSizeDepth = Integer.parseInt(stringParts[2]);
                }
                else if (inputLine.contains("Max Weight (kgs):")) {
                    stringParts = inputLine.split(":");
                    maxWeight = Double.parseDouble(stringParts[1].trim());
                }
                else if (inputLine.contains("Duration (hrs):")) {
                    stringParts = inputLine.split(":");
                    duration = Integer.parseInt(stringParts[1].trim());
                    //Adds new Priority Class to PriorityList
                    PriorityList.add(new PriorityClass(priority, maxDistance, fragilePost, maxSizeWidth, maxSizeHeight, maxSizeDepth, maxWeight, duration));
                }
            }
            fileIn.close();
        } catch (FileNotFoundException ex) {
            //FileNotFoundException
        } catch (IOException ex) {
            //IOException
        }
    }
    
    //Adds Packages to PackageList
    public void addPackagesToLists(PackageClass Package) {
        PackageList.add(Package);
        PackageHistoryList.add(Package);
        packetCount++;
        String time = timeGet();
        String s = null;
        
        //Constructs the string for recipe
        if (Package.Item.itemFragility) {
            s = String.format(Locale.US, "***PAKATTU UUSI PAKETTI %s***\nPaketin nimi: %s\nPaketin koko: %dx%dx%d\nPaketin särkyvyys: True\nPaketin paino (kg): %.1f\nPaketin prioriteettiluokka: %d", time, Package.Item.itemName, Package.Item.itemSizeWidth, Package.Item.itemSizeHeight, Package.Item.itemSizeDepth, Package.Item.itemWeight, Package.Priority.priority);
        }
        else {
            s = String.format(Locale.US, "***PAKATTU UUSI PAKETTI %s***\nPaketin nimi: %s\nPaketin koko: %dx%dx%d\nPaketin särkyvyys: False\nPaketin paino (kg): %.1f\nPaketin prioriteettiluokka: %d", time, Package.Item.itemName, Package.Item.itemSizeWidth, Package.Item.itemSizeHeight, Package.Item.itemSizeDepth, Package.Item.itemWeight, Package.Priority.priority);
        }
        
        String t = String.format(Locale.US, "%s\nLähtöosoite: %s\nLähtökaupunki: %s\nLähtöpostinumero: %d\nLähtöpostin aukioloajat: %s\nLähtöpostin tiedot: %s\nLähtöosoitteen koordinaatit: %.7f,%.7f", s, Package.SmartPostStart.smartpostAddress, Package.SmartPostStart.smartpostCity,Package.SmartPostStart.smartpostCode,Package.SmartPostStart.smartpostAvailability,Package.SmartPostStart.smartpostPostOffice,Package.SmartPostStart.GeoPoint.smartpostLat, Package.SmartPostStart.GeoPoint.smartpostLng);
        String u = String.format(Locale.US, "%s\nKohdeosoite: %s\nKohdekaupunki: %s\nKohdepostinumero: %d\nKohdepostin aukioloajat: %s\nKohdepostin tiedot: %s\nKohdeosoitteen koordinaatit: %.7f,%.7f\nPaketin kilometrit (km): %.2f\n", t, Package.SmartPostDestination.smartpostAddress, Package.SmartPostDestination.smartpostCity,Package.SmartPostDestination.smartpostCode,Package.SmartPostDestination.smartpostAvailability,Package.SmartPostDestination.smartpostPostOffice,Package.SmartPostDestination.GeoPoint.smartpostLat, Package.SmartPostDestination.GeoPoint.smartpostLng, Package.distance);
       
        //Adds the constructed String to a recipe file
        try (FileWriter filewriter = new FileWriter(fileNameRecipe,true)) {
            filewriter.write(u);
            filewriter.close();
        } catch (IOException ex) {
            //IOException
        }
    }
    public void removePackageFromList(PackageClass Package) {
        //Removes Package from PackageList
        PackageList.remove(Package);
        //Gets time
        String time = timeGet();
        String s = null;
        if (Package.Item.itemFragility) {
            s = String.format(Locale.US, "***LÄHETETTY PAKETTI %s***\nPaketin nimi: %s\nPaketin koko: %dx%dx%d\nPaketin särkyvyys: True\nPaketin paino (kg): %.1f\nPaketin prioriteettiluokka: %d", time, Package.Item.itemName, Package.Item.itemSizeWidth, Package.Item.itemSizeHeight, Package.Item.itemSizeDepth, Package.Item.itemWeight, Package.Priority.priority);
        }
        else {
            s = String.format(Locale.US, "***LÄHETETTY PAKETTI %s***\nPaketin nimi: %s\nPaketin koko: %dx%dx%d\nPaketin särkyvyys: False\nPaketin paino (kg): %.1f\nPaketin prioriteettiluokka: %d", time, Package.Item.itemName, Package.Item.itemSizeWidth, Package.Item.itemSizeHeight, Package.Item.itemSizeDepth, Package.Item.itemWeight, Package.Priority.priority);
        }
        String t = String.format(Locale.US, "%s\nLähtöosoite: %s\nLähtökaupunki: %s\nLähtöpostinumero: %d\nLähtöpostin aukioloajat: %s\nLähtöpostin tiedot: %s\nLähtöosoitteen koordinaatit: %.7f,%.7f", s, Package.SmartPostStart.smartpostAddress, Package.SmartPostStart.smartpostCity,Package.SmartPostStart.smartpostCode,Package.SmartPostStart.smartpostAvailability,Package.SmartPostStart.smartpostPostOffice,Package.SmartPostStart.GeoPoint.smartpostLat, Package.SmartPostStart.GeoPoint.smartpostLng);
        String u = String.format(Locale.US, "%s\nKohdeosoite: %s\nKohdekaupunki: %s\nKohdepostinumero: %d\nKohdepostin aukioloajat: %s\nKohdepostin tiedot: %s\nKohdeosoitteen koordinaatit: %.7f,%.7f\nPaketin kilometrit (km): %.2f\n", t, Package.SmartPostDestination.smartpostAddress, Package.SmartPostDestination.smartpostCity,Package.SmartPostDestination.smartpostCode,Package.SmartPostDestination.smartpostAvailability,Package.SmartPostDestination.smartpostPostOffice,Package.SmartPostDestination.GeoPoint.smartpostLat, Package.SmartPostDestination.GeoPoint.smartpostLng, Package.distance);
        try (FileWriter filewriter = new FileWriter(fileNameRecipe,true)) {
            filewriter.write(u);
            filewriter.close();
        } catch (IOException ex) {
            //IOException
        }
    }
    private String timeGet(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy - hh:mm:ss");
        return dateFormat.format(calendar.getTime());
    }
    
    //When the program is closed, all the remaining packages in PackageList are saved in a file
    public void writeStorageToFile () {
        try {
            BufferedWriter fileOut;
            fileOut = new BufferedWriter(new FileWriter(fileNameOfHistoryList));
            String s = "";
            for (PackageClass Package : PackageList) {
                if (Package.Item.itemFragility) {
                    s = String.format(Locale.US, "Paketin nimi: %s\nPaketin koko: %dx%dx%d\nPaketin särkyvyys: True\nPaketin paino (kg): %.1f\nPaketin prioriteettiluokka: %d", Package.Item.itemName, Package.Item.itemSizeWidth, Package.Item.itemSizeHeight, Package.Item.itemSizeDepth, Package.Item.itemWeight, Package.Priority.priority);
                }
                else {
                    s = String.format(Locale.US, "Paketin nimi: %s\nPaketin koko: %dx%dx%d\nPaketin särkyvyys: False\nPaketin paino (kg): %.1f\nPaketin prioriteettiluokka: %d", Package.Item.itemName, Package.Item.itemSizeWidth, Package.Item.itemSizeHeight, Package.Item.itemSizeDepth, Package.Item.itemWeight, Package.Priority.priority);
                }
                String t = String.format(Locale.US, "%s\nLähtöosoite: %s\nLähtökaupunki: %s\nLähtöpostinumero: %d\nLähtöpostin aukioloajat: %s\nLähtöpostin tiedot: %s\nLähtöosoitteen koordinaatit: %.7f,%.7f", s, Package.SmartPostStart.smartpostAddress, Package.SmartPostStart.smartpostCity,Package.SmartPostStart.smartpostCode,Package.SmartPostStart.smartpostAvailability,Package.SmartPostStart.smartpostPostOffice,Package.SmartPostStart.GeoPoint.smartpostLat, Package.SmartPostStart.GeoPoint.smartpostLng);
                String u = String.format(Locale.US, "%s\nKohdeosoite: %s\nKohdekaupunki: %s\nKohdepostinumero: %d\nKohdepostin aukioloajat: %s\nKohdepostin tiedot: %s\nKohdeosoitteen koordinaatit: %.7f,%.7f\nPaketin kilometrit (km): %.2f\n", t, Package.SmartPostDestination.smartpostAddress, Package.SmartPostDestination.smartpostCity,Package.SmartPostDestination.smartpostCode,Package.SmartPostDestination.smartpostAvailability,Package.SmartPostDestination.smartpostPostOffice,Package.SmartPostDestination.GeoPoint.smartpostLat, Package.SmartPostDestination.GeoPoint.smartpostLng, Package.distance);
                fileOut.write(u);
            }
            fileOut.close();

        } catch (IOException ex) {
            //IOException
        }
        
        
    }
    //Reads the saved packages file and adds the packages to PackageList and PackageHistoryList
    public void readPackageHistoryFromFile() {
        try {
            BufferedReader fileIn;
            fileIn = new BufferedReader(new FileReader(fileNameOfHistoryList));
            String inputLine;
            String packetName = null;
            int packetWidth = 0;
            int packetHeight = 0;
            int packetDepth = 0;
            double packetWeight = 0;
            boolean fragility = true;
            int priority = 1;
            String StartAddress = null;
            String StartCity = null;
            String StartAvailability = null;
            String StartPostOffice = null;
            int StartCode = 0;
            float StartLat = 0;
            float StartLng = 0;
            String DestinationAddress = null;
            String DestinationCity = null;
            String DestinationAvailability = null;
            String DestinationPostOffice = null;
            int DestinationCode = 0;
            float DestinationLat = 0;
            float DestinationLng = 0;
            double distance = 0;
            String[] stringParts;
            
            while ((inputLine = fileIn.readLine()) != null) {
                if (inputLine.contains("Paketin nimi:")) {
                    stringParts = inputLine.split(":");
                    packetName= stringParts[1].trim();
                }
                else if (inputLine.contains("Paketin koko:")) {
                    stringParts = inputLine.split(":");
                    stringParts = stringParts[1].trim().split("x");
                    packetWidth = Integer.parseInt(stringParts[0]);
                    packetHeight = Integer.parseInt(stringParts[1]);
                    packetDepth = Integer.parseInt(stringParts[2]);
                }
                else if (inputLine.contains("Paketin paino (kg):")) {
                    stringParts = inputLine.split(":");
                    packetWeight = Double.parseDouble(stringParts[1].trim());
                }
                else if (inputLine.contains("Paketin särkyvyys:")) {
                    fragility = inputLine.contains("True");
                }
                else if (inputLine.contains("Paketin prioriteettiluokka:")) {
                    stringParts = inputLine.split(":");
                    priority= Integer.parseInt(stringParts[1].trim());
                }
                else if (inputLine.contains("Lähtöosoite:")) {
                    stringParts = inputLine.split(":");
                    StartAddress= stringParts[1].trim();
                }
                else if (inputLine.contains("Lähtökaupunki:")) {
                    stringParts = inputLine.split(":");
                    StartCity= stringParts[1].trim();
                }
                else if (inputLine.contains("Lähtöpostinumero:")) {
                    stringParts = inputLine.split(":");
                    StartCode= Integer.parseInt(stringParts[1].trim());
                }
                else if (inputLine.contains("Lähtöpostin aukioloajat:")) {
                    stringParts = inputLine.split(":");
                    StartAvailability= stringParts[1].trim();
                }
                else if (inputLine.contains("Lähtöpostin tiedot:")) {
                    stringParts = inputLine.split(":");
                    StartPostOffice= stringParts[1].trim();
                }
                else if (inputLine.contains("Lähtöosoitteen koordinaatit:")) {
                    stringParts = inputLine.split(":");
                    stringParts = stringParts[1].split(",");
                    StartLat= Float.parseFloat(stringParts[0].trim());
                    StartLng= Float.parseFloat(stringParts[1].trim());
                }
                else if (inputLine.contains("Kohdeosoite:")) {
                    stringParts = inputLine.split(":");
                    DestinationAddress= stringParts[1].trim();
                }
                else if (inputLine.contains("Kohdekaupunki:")) {
                    stringParts = inputLine.split(":");
                    DestinationCity= stringParts[1].trim();
                }
                else if (inputLine.contains("Kohdepostinumero:")) {
                    stringParts = inputLine.split(":");
                    DestinationCode= Integer.parseInt(stringParts[1].trim());
                }
                else if (inputLine.contains("Kohdepostin aukioloajat:")) {
                    stringParts = inputLine.split(":");
                    DestinationAvailability= stringParts[1].trim();
                }
                else if (inputLine.contains("Kohdepostin tiedot:")) {
                    stringParts = inputLine.split(":");
                    DestinationPostOffice= stringParts[1].trim();
                }
                else if (inputLine.contains("Kohdeosoitteen koordinaatit:")) {
                    stringParts = inputLine.split(":");
                    stringParts = stringParts[1].split(",");
                    DestinationLat= Float.parseFloat(stringParts[0].trim());
                    DestinationLng= Float.parseFloat(stringParts[1].trim());
                }
                else if (inputLine.contains("Paketin kilometrit (km):")) {
                    stringParts = inputLine.split(":");
                    distance = Float.parseFloat(stringParts[1].trim());
                    //Adds a new Package Class
                    addPackagesToLists(new PackageClass(new ItemClass(packetName, packetWidth, packetHeight, packetDepth, fragility, packetWeight), PriorityList.get(priority-1), new SmartPostClass(StartCode, StartCity, StartAddress, StartAvailability, StartPostOffice, StartLat, StartLng),new SmartPostClass(DestinationCode, DestinationCity, DestinationAddress, DestinationAvailability, DestinationPostOffice, DestinationLat, DestinationLng), distance));
                }
                
            }
            fileIn.close();
        } catch (FileNotFoundException ex) {
            //FileNotFoundException
        }
        catch (IOException ex) {
            //IOException
        }
        
    }
    //Reads ItemList File and adds the Items to ItemList
    private void addItemsFromFile() {
        try {
            BufferedReader fileIn;
            fileIn = new BufferedReader(new FileReader(fileNameItems));
            String inputLine;
            
            String name = null;
            int width = 0;
            int height = 0;
            int depth = 0;
            boolean fragility = true;
            double weight = 0;
            String[] stringParts;
            
            while ((inputLine = fileIn.readLine()) != null) {
                if (inputLine.contains("Name:")) {
                    stringParts = inputLine.split(":");
                    name = stringParts[1].trim();
                }
                else if (inputLine.contains("Size:")) {
                    stringParts = inputLine.split(":");
                    stringParts = stringParts[1].trim().split("x");
                    width = Integer.parseInt(stringParts[0]);
                    height = Integer.parseInt(stringParts[1]);
                    depth = Integer.parseInt(stringParts[2]);
                }
                else if (inputLine.contains("Fragile:")) {
                    if (inputLine.contains("True")) {
                        fragility = true;
                    }
                    else {
                        fragility = false;
                    }
                }
                else if (inputLine.contains("Weight:")) {
                    stringParts = inputLine.split(":");
                    weight = Double.parseDouble(stringParts[1].trim());
                    ItemList.add(new ItemClass(name, width, height, depth, fragility, weight));
                }
            }
            fileIn.close();
        } catch (FileNotFoundException ex) {
            //FileNotFoundException
        }
        catch (IOException ex) {
            //IOException
        }
    }
}
