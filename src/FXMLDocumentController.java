import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.web.WebView;


public class FXMLDocumentController implements Initializable {
    
    int depthcm;
    int widthcm;
    int heightcm;
    double masskg;
    String name;
    String packageclass;
    String object;
    boolean fragile;
    String finaldestination;
    String finalstart;
    String chosen;
    //needed lists
    private SmartPostReader SPR = null;
    private ObservableList locationlist = null;
    private ObservableList objectlist = null;
    private StorageClass SC = null;
    private ObservableList packageclasslist = null;
    private ArrayList ItemList = null;

    //buttons and other elements
    @FXML
    private Button newpacket;
    
    @FXML
    private Button newobjectbutton;
    
    @FXML
    private TextField depth;
    
    @FXML
    private TextField width;
    
    @FXML
    private TextField height;
    
    @FXML
    private TextField newname;
    
    @FXML
    private TextField massa; 
    
    @FXML
    private ComboBox classescombo; 
    
    @FXML
    private ComboBox objectscombo;  
    
    @FXML
    private CheckBox frahilecheck;  
    
    @FXML
    private ComboBox startcombo;  
    
    @FXML
    private ComboBox destinationcombo; 
    
    @FXML
    private ListView startaddresslistview;
        
    @FXML
    private ListView targetaddresslistview; 
    
    @FXML
    private Label x1;     
    
    @FXML
    private Label x2;        
    
    @FXML
    private Label namelabel;
    
    @FXML
    private Label sizelabel; 
    
    @FXML
    private Label massalabel; 
    
    @FXML
    private Label classlabel;
    
    @FXML
    private WebView web;
    
    @FXML
    private void newpackage(ActionEvent event) {
        int validdistance;
        double distancekm = 0;
        int classnumber = 0;
        int x=0, y=0, z=0;
        try{
            //---------------
            //class of package
            //---------------
            packageclass = classescombo.getValue().toString();
            classnumber = Integer.parseInt(String.valueOf(packageclass.charAt(0)));
        }catch(NullPointerException ex){
            //NullPointerException
        }
        
        try{
            //gets name of the object inside the package
        object = objectscombo.getValue().toString();
        for (ItemClass Item : SC.ItemList) {
            if(object.equals(Item.itemName)){
                break;
                }else{
                x++;
            }
            }
        }catch(NullPointerException ex){
            //NullPointerException
        }  
        
        try{
            //--------------
            //destination of package
            //--------------
            finaldestination = targetaddresslistview.getSelectionModel().getSelectedItem().toString();
            //SmartPost.smartpostCity;
            for (SmartPostClass SmartPost : SPR.SmartPostList) {
                if(finaldestination.equals(SmartPost.smartpostAddress)){
                    break;
                }else{
                y++;
                }
            }            
        }catch(NullPointerException ex){
        }
        
        try{
            //--------------------
            //Start point of package
            //--------------------
            finalstart = startaddresslistview.getSelectionModel().getSelectedItem().toString();
            for (SmartPostClass SmartPost : SPR.SmartPostList) {
                if(finalstart.equals(SmartPost.smartpostAddress)){
                    break;
                }else{
                z++;
                }
            }             
        }catch(NullPointerException ex){         
        }
        if((finalstart== null)||(finaldestination==null)||(packageclass==null)||(object==null)){
            //If you didnt choose all needed info
            System.out.println("Sinulta puuttuu joku valinta");
        }else{
            distancekm = checkTravelDistance(SPR.SmartPostList.get(z), SPR.SmartPostList.get(y));
            validdistance = distancecheck(distancekm, classnumber);  
            if(validdistance==1){//if distance is within class limits
            //luokka, esine, lopullinenkohde, lopullinenlahto
            SC.addPackagesToLists(new PackageClass(SC.ItemList.get(x),SC.PriorityList.get(classnumber-1), SPR.SmartPostList.get(z), SPR.SmartPostList.get(y), checkTravelDistance(SPR.SmartPostList.get(z), SPR.SmartPostList.get(y))));
            closeStage();                
            }else{//if distance isnt within class limits
                System.out.println("Etäisyys on liian suuri tämän luokan paketeille");
                //make popup for message
        try {
            Stage documentcontroller = new Stage();
            Parent page = FXMLLoader.load(getClass().getResource("Popup.fxml"));
            Scene scene = new Scene(page);
            documentcontroller.setScene(scene);
            documentcontroller.show();
            }
        catch (IOException ex) {
            
            }              
        
            }
        }
    }
    
    private int distancecheck(double distancekm, int classnumber){
        if(SC.PriorityList.get(classnumber-1).maxDistance>distancekm || SC.PriorityList.get(classnumber-1).maxDistance==0){
            return(1);
        }
        else{
            return(0);
        }
    }
    
    @FXML
    private void newitem(ActionEvent event) {
        //creation of new item
        depthcm = Integer.parseInt(depth.getText());
        widthcm = Integer.parseInt(width.getText());
        heightcm = Integer.parseInt(height.getText());
        name = newname.getText();
        masskg = Integer.parseInt(depth.getText());
        fragile = frahilecheck.isSelected();
        try{//adds new item to itemlist
        SC.addItemToItemList(name, widthcm, heightcm, depthcm, fragile, masskg);
        }catch (FileNotFoundException ex)
        {
            //FileNotFound
        }
        catch (IOException ex) {
           
        } catch (Exception ex) {
           
        }
        objectlist.add(SC.ItemList.get(SC.ItemList.size()-1).itemName);
        //hide creation elements
        togglecreation(false);       
        
    }  
    
    @FXML
    private void togglecreation(boolean x) {
        //toggles creation elements
        massa.setVisible(x);
        newname.setVisible(x);
        depth.setVisible(x);
        width.setVisible(x);
        height.setVisible(x);
        x1.setVisible(x);
        x2.setVisible(x);
        namelabel.setVisible(x);
        sizelabel.setVisible(x);
        massalabel.setVisible(x);
        frahilecheck.setVisible(x);
        newobjectbutton.setVisible(x);
        
    } 
      
    
    @FXML
    private void toggleclass(boolean x) {
        //toggles class elements
        classescombo.setVisible(x);
        classlabel.setVisible(x);
        
    }   
    
    
    @FXML
    private void cancel() {
        closeStage();
        
    }    
    
    @FXML
    private void itemselection(ActionEvent event) {
        //choosing item
        int size;
        packageclasslist = FXCollections.observableArrayList();
        //if you select creation of new item
        if(objectscombo.getValue().toString().contains("Luo uusi esine")){
            togglecreation(true);
            toggleclass(false);
            toggleaddress(false);
        }else{
            //if you select old item
            //hide creation just to be sure
            togglecreation(false);
            
            //---------------
            //chosen item
            //---------------
            chosen = objectscombo.getValue().toString();
            
            size = SC.ItemList.size();
            
            for(int x=0;x<size;x++){
                if (SC.ItemList.get(x).itemName.equals(chosen)){
                    for(int y=0;y<3;y++){
                        //toggles available postage classes for your item
                        if((SC.ItemList.get(x).itemSizeWidth<=SC.PriorityList.get(y).maxSizeWidth)&&(SC.ItemList.get(x).itemSizeHeight<=SC.PriorityList.get(y).maxSizeHeight)&&(SC.ItemList.get(x).itemSizeDepth<=SC.PriorityList.get(y).maxSizeDepth)&&(SC.ItemList.get(x).itemWeight<=SC.PriorityList.get(y).maxWeight)&&(SC.ItemList.get(x).itemFragility==(false||(SC.PriorityList.get(y).fragilePost)))){
                            
                            packageclasslist.add((y+1) + ". Luokka");
                        }
                    }

                }
                
            }
            //sets postage classes to combobox
            classescombo.setItems(packageclasslist);
            //shows postage class selection
            toggleclass(true);    
        }
        
    }  
    
    @FXML
    private void chooseclass(ActionEvent event) {
        //when you chose postage class, shows address and location info
        toggleaddress(true);
        
    }
    
    @FXML
    private void toggleaddress(boolean x) {
        //toggles address boxes
        startaddresslistview.setVisible(x);
        targetaddresslistview.setVisible(x);
        destinationcombo.setVisible(x);
        startcombo.setVisible(x);
        newpacket.setVisible(x);
        
    }   
    
    
    private void closeStage() {
        //closes window
        Stage stage = (Stage) newpacket.getScene().getWindow();
        stage.close();
    }    
    
    @FXML
    private void choosestartlocation(ActionEvent event) {
        //selection of start city
        String startlocation;
        startlocation = startcombo.getValue().toString();
        ObservableList addresses = FXCollections.observableArrayList();
    
        for (SmartPostClass SmartPost : SPR.SmartPostList) {
            if(startlocation.contains(SmartPost.smartpostCity)){
                //adds addresses in the chosen city to list
               addresses.add(SmartPost.smartpostAddress);
            }

        }
        //puts addresses from the list to listview
    startaddresslistview.setItems(addresses);
        
        
        
    }
    
      
@FXML
    private void choosedestination(ActionEvent event) {
        //selection of destination city
        String destinationlocation;
        destinationlocation = destinationcombo.getValue().toString();
        ObservableList addresses = FXCollections.observableArrayList();
            
    
        for (SmartPostClass SmartPost : SPR.SmartPostList) {
            if(destinationlocation.contains(SmartPost.smartpostCity)){
                //adds addresses in the chosen city yo list
                 addresses.add(SmartPost.smartpostAddress);
            }
        }
        //puts addresses from the list to listview
        targetaddresslistview.setItems(addresses);
        
        
        
    }    
     private double checkTravelDistance(SmartPostClass SmartPostStart, SmartPostClass SmartPostDestination) {
         //distance calculation
            ArrayList<Float> al = new ArrayList<>();
            //adds start and endpoints to list
            al.add(SmartPostStart.GeoPoint.smartpostLat);
            al.add(SmartPostStart.GeoPoint.smartpostLng);
            al.add(SmartPostDestination.GeoPoint.smartpostLat);
            al.add(SmartPostDestination.GeoPoint.smartpostLng);
            
            //
            //
            // KILOMETRIEN LASKEMISESSA JOTAIN HÄIKKÄÄ?!?!
            //
            //
            
            //should calculate distance (WIP)
            web.getEngine().load(getClass().getResource("index.html").toExternalForm());
            double y = 0;
            y = (double) web.getEngine().executeScript("document.createPath(" +al + ", 'red',1)");
            //returns distance
            return y;
        }
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        web.getEngine().load(getClass().getResource("index.html").toExternalForm());

        String place;
        //lists
        locationlist = FXCollections.observableArrayList();
        objectlist = FXCollections.observableArrayList();
        //adds option for creating a new item
        objectlist.add("Luo uusi esine");

        
        try {
            //imports class, item, smartpost data
            SC = StorageClass.getInstance();
            SPR = SmartPostReader.getInstance();
            
            //HUOM! ESIMERKKITULOSTUS KUINKA LUKEA SMARTPOSTDATAA, POISTOON LOPULTA, SC.ItemList = itemlista, SC.PriorityList = pakettiluokkalista
            
            for (ItemClass Item : SC.ItemList) {
                //adds items to list
                objectlist.add(Item.itemName);
            }
            for (PriorityClass Priority : SC.PriorityList) {
            }
    
            for (SmartPostClass SmartPost : SPR.SmartPostList) {
                //makes list for different citys
                
                place = SmartPost.smartpostCity;
                //if city is in the lsit, its not added
                if(locationlist.contains(place)){
                    
                }
                else{
                //if city isnt on the list, it is added
                    locationlist.add(SmartPost.smartpostCity);
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            //FileNotFoundException
        }
        catch (IOException ex) {
            //IOException
        } catch (Exception ex) {
            //Exception
        }
        
        //puts locations and items to combolists
        startcombo.setItems(locationlist);
        destinationcombo.setItems(locationlist);
        objectscombo.setItems(objectlist);
        
    }  
    
    
    
}
