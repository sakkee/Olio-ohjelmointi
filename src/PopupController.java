/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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




//some buttons and gui elements
public class PopupController implements Initializable {
    
    @FXML
    private Button continuee; 
    
    @FXML
    private void close(ActionEvent event){
        closeStage();
    } 
    
    private void closeStage() {
        Stage stage = (Stage) continuee.getScene().getWindow();
        stage.close();
    }         
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
