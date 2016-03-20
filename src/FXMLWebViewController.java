import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.ArrayList;


//buttons and gui elements
public class FXMLWebViewController implements Initializable {
    @FXML
    private Button createnew;
    
    @FXML
    private Button send; 
    
    @FXML
    private Button exit;     
    
    @FXML
    private ListView packets;
    
    @FXML
    private ComboBox cities;
    
    @FXML
    private WebView web;
    //Lists
    private SmartPostReader SPR;
    private StorageClass SC;
    private ObservableList places = null;
    @FXML
    private Button refresh;
    @FXML
    private Button statistics;
    @FXML
    private Button clearbutton;
    @FXML
    private Button loadButtonStorage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        places = FXCollections.observableArrayList();
        String place;
        try {
            SC = StorageClass.getInstance();
            SPR = SmartPostReader.getInstance();
            for (SmartPostClass SmartPost : SPR.SmartPostList) {
                //Adds cities to the cityList
                place = SmartPost.smartpostCity;
                if(places.contains(place)){
                }
                else{
                    places.add(place);
                }
            }  
        }
        catch (Exception ex) {
            //Exception
        }
        //Loads the Google Maps Engine
        web.getEngine().load(getClass().getResource("index.html").toExternalForm());
    cities.setItems(places);
    }    
    
    
    @FXML
    private void sendpacket(ActionEvent event){
        String packet;
        if(packets.getSelectionModel().getSelectedItem()==null){
        }else{
        //Draws route, when you send packet
        drawRoad((PackageClass) SC.PackageList.get(packets.getSelectionModel().getSelectedIndex()));
        //Removes sent packet from list
        SC.removePackageFromList(SC.PackageList.get(packets.getSelectionModel().getSelectedIndex()));
        //Clears selection @listview
        packets.getSelectionModel().clearSelection();
        //Refreshes listview
        refresh();           

        }
    }  
    
    @FXML
    private void refresh(){
        ObservableList namelist = FXCollections.observableArrayList();
            for (PackageClass Package : SC.PackageList) {
                //adds items to list
                namelist.add(Package.Item.itemName);
            }        
        packets.setItems(namelist);

    }      
    
    @FXML
    private void exit(ActionEvent event){
        //Closes the main window and saves the remaining packages to a file
        SC.writeStorageToFile();
        closeStage();
    }  
    
    private void closeStage() {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }     
    
    @FXML
    private void clearroutes(ActionEvent event){
        //Clears routes from the map
        removeRoads();
    }      
    
    @FXML
    private void statistics(ActionEvent event){
         try {//Tries to open statistics .txt file
              //and makes new window for it
                Stage Statistics = new Stage();
                Parent page = FXMLLoader.load(getClass().getResource("FXMLStatistics.fxml"));
                Scene scene = new Scene(page);
                Statistics.setResizable(false);
                Statistics.setScene(scene);
                Statistics.show();
            } catch (IOException ex) {
                //IOException
            }
    }      
    
    @FXML
    private void addposts(ActionEvent event){
        //Adds smartposts to the map
        String s;
            for (SmartPostClass SmartPost : SPR.SmartPostList) {
                //Goes trough the smartpost list. if city is wanted..
                if (SmartPost.smartpostCity.equals(cities.getValue())) {
                    s = String.format(Locale.US, "%s, %d %s", SmartPost.smartpostAddress, SmartPost.smartpostCode, SmartPost.smartpostCity);
                    //..marks the location on the map
                    web.getEngine().executeScript("document.goToLocation('" + s + "','" + SmartPost.smartpostAvailability +"', 'blue')");
                }
            }
    }   
           
    @FXML
    private void newpacket(ActionEvent event){
        try {
            //create new packet -window
            Stage documentcontroller = new Stage();
            Parent page = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
            Scene scene = new Scene(page);
            documentcontroller.setScene(scene);
            documentcontroller.show();
            }
        catch (IOException ex) {
            //IOException
            }
    }       

    private void drawRoad(PackageClass Package) {
        int speedclass;
        //draws road to the map
        ArrayList<Float> al = new ArrayList<>();
        String x;
        
        // start and end point, draws slowly line between them   
        al.add(Package.SmartPostStart.GeoPoint.smartpostLat);
        al.add(Package.SmartPostStart.GeoPoint.smartpostLng);
        al.add(Package.SmartPostDestination.GeoPoint.smartpostLat);
        al.add(Package.SmartPostDestination.GeoPoint.smartpostLng);
        x = Integer.toString(Package.Priority.priority);
        if(Package.Priority.priority == 1){
            web.getEngine().executeScript("document.createPath(" +al + ", 'red'," + x + ")");
        }else if(Package.Priority.priority == 2){
            web.getEngine().executeScript("document.createPath(" +al + ", 'blue'," + x + ")");
        }else{
            web.getEngine().executeScript("document.createPath(" +al + ", 'green'," + x + ")");
        }
        al.clear();
    }

    
    private void removeRoads() {
        //removes drawn routes from map
        web.getEngine().executeScript("document.deletePaths()");
    }

    @FXML
    private void loadStorage(ActionEvent event) {
        //Reads saved packages from a File and adds them to PackageLists
        SC.readPackageHistoryFromFile();
        refresh();
    }
}