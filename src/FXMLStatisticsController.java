import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class FXMLStatisticsController implements Initializable {
    @FXML
    private TextField packetNumber;

    //Initializes StorageClass and ObservableLists
    
    private StorageClass SC = null;
    private ObservableList storageItemList = null;
    private ObservableList storageDistanceList = null;
    private ObservableList storageStartList = null;
    private ObservableList storageDestinationList = null;
    @FXML
    private Button closeButton;
    @FXML
    private ListView<?> packetItems;
    @FXML
    private ListView<?> packetLengths;
    @FXML
    private ListView<?> packetStarts;
    @FXML
    private ListView<?> packetDestinations;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            SC = StorageClass.getInstance();
            
        } catch (Exception ex) {
            //Exception happened
        }
        storageItemList = FXCollections.observableArrayList();
        storageDistanceList = FXCollections.observableArrayList();
        storageStartList = FXCollections.observableArrayList();
        storageDestinationList = FXCollections.observableArrayList();
        packetNumber.setText(Integer.toString(StorageClass.getPacketCount()));
        String s = null;
        
        //Adds items to each ListViewList, reads the PackageHistoryList which tracks all created packages during the session
        for (PackageClass Package: SC.PackageHistoryList) {
            storageItemList.add(Package.Item.itemName);
            s = String.format(Locale.US, "%1$,.2f", Package.distance);
            storageDistanceList.add(s);
            s = String.format(Locale.US, "%s, %d %s", Package.SmartPostStart.smartpostAddress, Package.SmartPostStart.smartpostCode, Package.SmartPostStart.smartpostCity);
            storageStartList.add(s);
            s = String.format(Locale.US, "%s, %d %s", Package.SmartPostDestination.smartpostAddress, Package.SmartPostDestination.smartpostCode, Package.SmartPostDestination.smartpostCity);
            storageDestinationList.add(s);
        }
        //Adds Items from ListViewLists to ListViews
        packetItems.setItems(storageItemList);
        packetLengths.setItems(storageDistanceList);
        packetStarts.setItems(storageStartList);
        packetDestinations.setItems(storageDestinationList);
    }  

    @FXML
    private void closeStage(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
    
}
